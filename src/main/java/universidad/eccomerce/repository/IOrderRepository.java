package universidad.eccomerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import universidad.eccomerce.model.Order;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, String> {
  List<Order> findByUserIdOrderByCreatedAtDesc(String userId);
  List<Order> findAllByOrderByCreatedAtDesc();
}