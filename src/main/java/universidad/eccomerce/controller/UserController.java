package universidad.eccomerce.controller;

import universidad.eccomerce.model.User;
import universidad.eccomerce.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody User loginRequest) {
    Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
    if (userOpt.isPresent()) {
      User user = userOpt.get();
      if (user.getPassword().equals(loginRequest.getPassword())) return ResponseEntity.ok(user);
    }
    return ResponseEntity.status(401).body("Credenciales inválidas");
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody User newUser) {
    if (userRepository.findByEmail(newUser.getEmail()).isPresent())
      return ResponseEntity.badRequest().body("El correo ya está registrado");
    if (newUser.getId() == null || newUser.getId().isEmpty())
      newUser.setId("user-" + UUID.randomUUID().toString().substring(0, 6));
    if (newUser.getRole() == null || newUser.getRole().isEmpty()) newUser.setRole("client");
    User savedUser = userRepository.save(newUser);
    return ResponseEntity.ok(savedUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User updatedData) {
    Optional<User> userOpt = userRepository.findById(id);
    if (userOpt.isEmpty()) return ResponseEntity.status(404).body("Operador no encontrado");
    User user = userOpt.get();
    if (updatedData.getNombre() != null && !updatedData.getNombre().isEmpty()) user.setNombre(updatedData.getNombre());
    if (updatedData.getEmail() != null && !updatedData.getEmail().isEmpty()) {
      Optional<User> existingEmail = userRepository.findByEmail(updatedData.getEmail());
      if (existingEmail.isPresent() && !existingEmail.get().getId().equals(id))
        return ResponseEntity.badRequest().body("El correo ya está registrado por otro operador");
      user.setEmail(updatedData.getEmail());
    }
    User savedUser = userRepository.save(user);
    return ResponseEntity.ok(savedUser);
  }
}