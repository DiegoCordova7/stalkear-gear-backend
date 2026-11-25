package universidad.eccomerce.controller;

import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import universidad.eccomerce.model.Order;
import universidad.eccomerce.service.PaymentService;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

  @Autowired
  private PaymentService paymentService;

  @PostMapping("/create-intent/{orderId}")
  public ResponseEntity<?> createIntent(@PathVariable String orderId) {
    try {
      return ResponseEntity.ok(paymentService.createPaymentIntentForOrder(orderId));
    } catch (StripeException | RuntimeException e) {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
  }

  @PostMapping("/confirm/{orderId}")
  public ResponseEntity<?> confirmPayment(@PathVariable String orderId) {
    try {
      Order updated = paymentService.confirmOrderPayment(orderId);
      return ResponseEntity.ok(updated);
    } catch (StripeException | RuntimeException e) {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
  }
}