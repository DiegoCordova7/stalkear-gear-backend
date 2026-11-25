package universidad.eccomerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import universidad.eccomerce.dto.CartItemResponse;
import universidad.eccomerce.dto.CartResponse;
import universidad.eccomerce.model.Cart;
import universidad.eccomerce.model.CartItem;
import universidad.eccomerce.model.Product;
import universidad.eccomerce.repository.ICartItemRepository;
import universidad.eccomerce.repository.ICartRepository;
import universidad.eccomerce.repository.IProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CartService {

  @Autowired
  private ICartRepository cartRepository;

  @Autowired
  private ICartItemRepository cartItemRepository;

  @Autowired
  private IProductRepository productRepository;

  @Transactional
  public Cart getOrCreateCart(String userId) {
    return cartRepository.findByUserId(userId).orElseGet(() -> {
      Cart cart = new Cart();
      cart.setId("cart-" + UUID.randomUUID().toString().substring(0, 8));
      cart.setUserId(userId);
      cart.setUpdatedAt(LocalDateTime.now());
      return cartRepository.save(cart);
    });
  }

  @Transactional(readOnly = true)
  public CartResponse getCartResponse(String userId) {
    Cart cart = cartRepository.findByUserId(userId).orElse(null);
    if (cart == null) return new CartResponse(null, userId, List.of(), 0.0);
    return toResponse(cart);
  }

  @Transactional
  public CartResponse addItem(String userId, String productId, int quantity) {
    if (quantity <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
    Product product = productRepository
        .findById(productId)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productId));
    Cart cart = getOrCreateCart(userId);
    CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId).orElse(null);
    int newQty = (item != null ? item.getQuantity() : 0) + quantity;
    if (product.getStock() < newQty)
      throw new RuntimeException("Stock insuficiente para " + product.getNombre() + " (disponibles: " + product.getStock() + ")");
    if (item == null) {
      item = new CartItem();
      item.setCart(cart);
      item.setProductId(productId);
      item.setQuantity(newQty);
      cart.getItems().add(item);
    } else {
      item.setQuantity(newQty);
    }
    cart.setUpdatedAt(LocalDateTime.now());
    cartRepository.save(cart);
    return toResponse(cart);
  }

  @Transactional
  public CartResponse updateQuantity(String userId, String productId, int quantity) {
    if (quantity <= 0) return removeItem(userId, productId);
    Cart cart = getOrCreateCart(userId);
    CartItem item = cartItemRepository
        .findByCartIdAndProductId(cart.getId(), productId)
        .orElseThrow(() -> new RuntimeException("El producto no está en el carrito"));
    Product product = productRepository
        .findById(productId)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productId));
    if (product.getStock() < quantity)
      throw new RuntimeException("Stock insuficiente para " + product.getNombre() + " (disponibles: " + product.getStock() + ")");
    item.setQuantity(quantity);
    cart.setUpdatedAt(LocalDateTime.now());
    cartRepository.save(cart);
    return toResponse(cart);
  }

  @Transactional
  public CartResponse removeItem(String userId, String productId) {
    Cart cart = cartRepository.findByUserId(userId).orElse(null);
    if (cart == null) return new CartResponse(null, userId, List.of(), 0.0);
    cart.getItems().removeIf(i -> i.getProductId().equals(productId));
    cart.setUpdatedAt(LocalDateTime.now());
    cartRepository.save(cart);
    return toResponse(cart);
  }

  @Transactional
  public void clearCart(String userId) {
    cartRepository.findByUserId(userId).ifPresent(cart -> {
      cart.getItems().clear();
      cart.setUpdatedAt(LocalDateTime.now());
      cartRepository.save(cart);
    });
  }

  private CartResponse toResponse(Cart cart) {
    List<CartItemResponse> itemResponses = new ArrayList<>();
    double total = 0.0;
    for (CartItem item : cart.getItems()) {
      Product product = productRepository.findById(item.getProductId()).orElse(null);
      if (product == null) continue;
      double lineSubtotal = product.getPrecio() * item.getQuantity();
      total += lineSubtotal;
      itemResponses.add(new CartItemResponse(
          item.getId(),
          product.getId(),
          product.getNombre(),
          product.getImagen(),
          product.getPrecio(),
          item.getQuantity(),
          product.getStock(),
          lineSubtotal
      ));
    }
    return new CartResponse(cart.getId(), cart.getUserId(), itemResponses, total);
  }
}