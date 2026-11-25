package universidad.eccomerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import universidad.eccomerce.model.CartItem;

import java.util.Optional;

public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
  Optional<CartItem> findByCartIdAndProductId(String cartId, String productId);
}