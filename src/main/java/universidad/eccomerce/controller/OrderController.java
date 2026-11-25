package universidad.eccomerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import universidad.eccomerce.model.Order;
import universidad.eccomerce.model.OrderItem;
import universidad.eccomerce.service.OrderService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @PostMapping("/validate-stock")
  public ResponseEntity<?> validateStock(@RequestBody List<OrderItem> items) {
    List<String> errors = orderService.validateCartStock(items);
    if (!errors.isEmpty()) return ResponseEntity.badRequest().body(Map.of("valid", false, "errors", errors));
    return ResponseEntity.ok(Map.of("valid", true));
  }

  @PostMapping
  public ResponseEntity<?> createOrder(@RequestBody Order order) {
    try {
      Order created = orderService.createOrder(order);
      return ResponseEntity.ok(created);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
  }

  @GetMapping
  public ResponseEntity<List<Order>> getAllOrders() {
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getOrderById(@PathVariable String id) {
    try {
      return ResponseEntity.ok(orderService.getOrderById(id));
    } catch (RuntimeException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable String userId) {
    return ResponseEntity.ok(orderService.getOrdersByUser(userId));
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<?> updateOrderStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
    String status = body.get("status");
    if (status == null || status.isEmpty()) return ResponseEntity.badRequest().body("El estado es obligatorio");
    try {
      return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    } catch (RuntimeException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }

  @PostMapping("/from-cart/{userId}")
  public ResponseEntity<?> createOrderFromCart(@PathVariable String userId, @RequestBody(required = false) Order checkoutData) {
    try {
      Order created = orderService.createOrderFromCart(userId, checkoutData);
      return ResponseEntity.ok(created);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
  }
}