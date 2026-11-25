package universidad.eccomerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import universidad.eccomerce.model.Cart;
import universidad.eccomerce.model.CartItem;
import universidad.eccomerce.model.Order;
import universidad.eccomerce.model.OrderItem;
import universidad.eccomerce.model.Product;
import universidad.eccomerce.repository.ICartRepository;
import universidad.eccomerce.repository.IOrderRepository;
import universidad.eccomerce.repository.IProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

  @Autowired
  private IOrderRepository orderRepository;

  @Autowired
  private IProductRepository productRepository;

  @Autowired
  private ICartRepository cartRepository;

  public List<String> validateCartStock(List<OrderItem> items) {
    List<String> errors = new ArrayList<>();
    for (OrderItem item : items) {
      Product product = productRepository.findById(item.getProductId()).orElse(null);
      if (product == null) errors.add("Producto no encontrado: " + item.getProductId());
      else if (product.getStock() < item.getQuantity())
        errors.add("Stock insuficiente para " + product.getNombre() + " (disponibles: " + product.getStock() + ")");
    }
    return errors;
  }

  @Transactional
  public Order createOrderFromCart(String userId, Order checkoutData) {
    Cart cart = cartRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("El carrito está vacío"));
    if (cart.getItems() == null || cart.getItems().isEmpty()) throw new RuntimeException("El carrito está vacío");
    Order order = new Order();
    order.setId("ord-" + UUID.randomUUID().toString().substring(0, 6));
    order.setUserId(userId);
    order.setCustomerName(checkoutData != null ? checkoutData.getCustomerName() : null);
    order.setCustomerEmail(checkoutData != null ? checkoutData.getCustomerEmail() : null);
    order.setShippingAddress(checkoutData != null ? checkoutData.getShippingAddress() : null);
    order.setPaymentMethod(checkoutData != null && checkoutData.getPaymentMethod() != null
        ? checkoutData.getPaymentMethod() : "STRIPE");
    order.setStatus("PENDIENTE_PAGO");
    order.setCreatedAt(LocalDateTime.now());
    double calculatedSubtotal = 0.0;
    for (CartItem cartItem : cart.getItems()) {
      Product product = productRepository.findById(cartItem.getProductId())
          .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + cartItem.getProductId()));
      if (product.getStock() < cartItem.getQuantity()) throw new RuntimeException(
          "Stock insuficiente para: " + product.getNombre() + " (Disponible: " + product.getStock() + ")");
      product.setStock(product.getStock() - cartItem.getQuantity());
      productRepository.save(product);
      OrderItem orderItem = new OrderItem();
      orderItem.setProductId(product.getId());
      orderItem.setProductName(product.getNombre());
      orderItem.setProductImage(product.getImagen());
      orderItem.setQuantity(cartItem.getQuantity());
      orderItem.setUnitPrice(product.getPrecio());
      orderItem.setOrder(order);

      order.getItems().add(orderItem);
      calculatedSubtotal += product.getPrecio() * cartItem.getQuantity();
    }
    order.setSubtotal(calculatedSubtotal);
    order.setTotalAmount(calculatedSubtotal);
    Order savedOrder = orderRepository.save(order);
    cart.getItems().clear();
    cart.setUpdatedAt(LocalDateTime.now());
    cartRepository.save(cart);
    return savedOrder;
  }

  @Transactional
  public Order createOrder(Order orderRequest) {
    if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty())
      throw new RuntimeException("El carrito está vacío");
    if (orderRequest.getId() == null || orderRequest.getId().isEmpty())
      orderRequest.setId("ord-" + UUID.randomUUID().toString().substring(0, 6));
    double calculatedSubtotal = 0.0;
    for (OrderItem item : orderRequest.getItems()) {
      Product product = productRepository.findById(item.getProductId())
          .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductId()));
      if (product.getStock() < item.getQuantity())
        throw new RuntimeException("Stock insuficiente para: " + product.getNombre() + " (Disponible: " + product.getStock() + ")");
      product.setStock(product.getStock() - item.getQuantity());
      productRepository.save(product);
      item.setId(null);
      item.setProductName(product.getNombre());
      item.setProductImage(product.getImagen());
      item.setUnitPrice(product.getPrecio());
      item.setOrder(orderRequest);
      calculatedSubtotal += product.getPrecio() * item.getQuantity();
    }
    orderRequest.setSubtotal(calculatedSubtotal);
    orderRequest.setTotalAmount(calculatedSubtotal);
    orderRequest.setCreatedAt(LocalDateTime.now());
    if (orderRequest.getStatus() == null || orderRequest.getStatus().isEmpty()) orderRequest.setStatus("PENDIENTE_PAGO");
    return orderRepository.save(orderRequest);
  }

  public List<Order> getAllOrders() {
    return orderRepository.findAllByOrderByCreatedAtDesc();
  }

  public List<Order> getOrdersByUser(String userId) {
    return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
  }

  public Order getOrderById(String id) {
    return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + id));
  }

  @Transactional
  public Order updateOrderStatus(String id, String newStatus) {
    Order order = getOrderById(id);
    order.setStatus(newStatus);
    return orderRepository.save(order);
  }
}