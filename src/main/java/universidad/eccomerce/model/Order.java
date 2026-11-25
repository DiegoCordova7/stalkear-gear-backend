package universidad.eccomerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  @Id
  private String id;

  private String userId;
  private String customerName;
  private String customerEmail;

  @Column(length = 500)
  private String shippingAddress;

  private Double subtotal;
  private Double totalAmount;
  private String paymentMethod;
  private String paymentId;
  private String status;

  private LocalDateTime createdAt = LocalDateTime.now();

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();
}