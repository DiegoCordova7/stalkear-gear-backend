package universidad.eccomerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import universidad.eccomerce.model.Cart;

import java.util.Optional;

public interface ICartRepository extends JpaRepository<Cart, String> {
  Optional<Cart> findByUserId(String userId);
}