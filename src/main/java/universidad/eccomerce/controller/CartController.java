package universidad.eccomerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import universidad.eccomerce.dto.CartResponse;
import universidad.eccomerce.service.CartService;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

  @Autowired
  private CartService cartService;

  @GetMapping("/{userId}")
  public ResponseEntity<CartResponse> getCart(@PathVariable String userId) {
    return ResponseEntity.ok(cartService.getCartResponse(userId));
  }

  @PostMapping("/{userId}/items")
  public ResponseEntity<?> addItem(@PathVariable String userId, @RequestBody Map<String, Object> body) {
    try {
      String productId = (String) body.get("productId");
      int quantity = body.get("quantity") != null ? ((Number) body.get("quantity")).intValue() : 1;
      if (productId == null || productId.isBlank()) {
        return ResponseEntity.badRequest().body(Map.of("error", "productId es obligatorio"));
      }
      return ResponseEntity.ok(cartService.addItem(userId, productId, quantity));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
  }

  @PatchMapping("/{userId}/items/{productId}")
  public ResponseEntity<?> updateQuantity(
      @PathVariable String userId,
      @PathVariable String productId,
      @RequestBody Map<String, Object> body
  ) {
    try {
      if (body.get("quantity") == null) {
        return ResponseEntity.badRequest().body(Map.of("error", "quantity es obligatorio"));
      }
      int quantity = ((Number) body.get("quantity")).intValue();
      return ResponseEntity.ok(cartService.updateQuantity(userId, productId, quantity));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
  }

  @DeleteMapping("/{userId}/items/{productId}")
  public ResponseEntity<CartResponse> removeItem(@PathVariable String userId, @PathVariable String productId) {
    return ResponseEntity.ok(cartService.removeItem(userId, productId));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<?> clearCart(@PathVariable String userId) {
    cartService.clearCart(userId);
    return ResponseEntity.ok(Map.of("cleared", true));
  }
}