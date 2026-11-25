package universidad.eccomerce.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import universidad.eccomerce.model.Order;
import universidad.eccomerce.repository.IOrderRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

  @Value("${stripe.secret-key}")
  private String secretKey;

  @Value("${stripe.currency:mxn}")
  private String currency;

  @Autowired
  private IOrderRepository orderRepository;

  @PostConstruct
  public void init() {
    if (secretKey != null && !secretKey.isBlank()) Stripe.apiKey = secretKey;
  }

  @Transactional
  public Map<String, Object> createPaymentIntentForOrder(String orderId) throws StripeException {
    if (secretKey == null || secretKey.isBlank()) throw new IllegalStateException("Falta configurar STRIPE_SECRET_KEY");
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + orderId));
    if (order.getTotalAmount() == null || order.getTotalAmount() <= 0) throw new IllegalArgumentException("El monto de la orden no es válido");
    long amountInCents = Math.round(order.getTotalAmount() * 100.0);
    PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
        .setAmount(amountInCents)
        .setCurrency(currency.toLowerCase())
        .putMetadata("orderId", order.getId())
        .setAutomaticPaymentMethods(
            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                .setEnabled(true)
                .build()
        )
        .build();
    PaymentIntent intent = PaymentIntent.create(params);
    order.setPaymentId(intent.getId());
    if (order.getStatus() == null || order.getStatus().isBlank()) order.setStatus("PENDIENTE_PAGO");
    orderRepository.save(order);
    Map<String, Object> response = new HashMap<>();
    response.put("orderId", order.getId());
    response.put("paymentIntentId", intent.getId());
    response.put("clientSecret", intent.getClientSecret());
    response.put("amount", order.getTotalAmount());
    response.put("currency", currency.toLowerCase());
    return response;
  }

  @Transactional
  public Order confirmOrderPayment(String orderId) throws StripeException {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + orderId));
    if (order.getPaymentId() == null || order.getPaymentId().isBlank())
      throw new IllegalStateException("La orden no tiene un paymentId asociado");
    PaymentIntent intent = PaymentIntent.retrieve(order.getPaymentId());
    if (!"succeeded".equals(intent.getStatus()))
      throw new IllegalStateException("El pago aún no ha sido completado en Stripe (estado actual: " + intent.getStatus() + ")");
    order.setStatus("PAGADO");
    if (order.getPaymentMethod() == null || order.getPaymentMethod().isBlank()) order.setPaymentMethod("STRIPE");
    return orderRepository.save(order);
  }
}