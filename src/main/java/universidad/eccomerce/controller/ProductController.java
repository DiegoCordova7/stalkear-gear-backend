package universidad.eccomerce.controller;

import universidad.eccomerce.model.Product;
import universidad.eccomerce.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  @Autowired
  private IProductRepository productRepository;

  @GetMapping
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable String id) {
    return productRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    if (product.getId() == null || product.getId().isEmpty()) {
      product.setId("prod-" + UUID.randomUUID().toString().substring(0, 6));
    }
    return ResponseEntity.ok(productRepository.save(product));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody Product updated) {
    return productRepository.findById(id).map(existing -> {
      existing.setNombre(updated.getNombre());
      existing.setCategoria(updated.getCategoria());
      existing.setPrecio(updated.getPrecio());
      existing.setStock(updated.getStock());
      existing.setImagen(updated.getImagen());
      existing.setDescripcion(updated.getDescripcion());
      existing.setMaterial(updated.getMaterial());
      existing.setPeso(updated.getPeso());
      existing.setResistenciaAgua(updated.getResistenciaAgua());
      existing.setCamuflaje(updated.getCamuflaje());
      existing.setGradoMilitar(updated.getGradoMilitar());
      return ResponseEntity.ok(productRepository.save(existing));
    }).orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}/stock")
  public ResponseEntity<?> updateStock(@PathVariable String id, @RequestBody Map<String, Integer> body) {
    Integer newStock = body.get("stock");
    if (newStock == null || newStock < 0) return ResponseEntity.badRequest().body("Cantidad de stock inválida");
    return productRepository.findById(id).map(product -> {
      product.setStock(newStock);
      return ResponseEntity.ok(productRepository.save(product));
    }).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteProduct(@PathVariable String id) {
    if (!productRepository.existsById(id)) return ResponseEntity.notFound().build();
    productRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }
}