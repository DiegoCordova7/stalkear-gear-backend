package universidad.eccomerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  @Id
  private String id;

  @NotBlank
  private String nombre;

  @NotBlank
  private String categoria;

  @NotNull
  @Min(0)
  private Double precio;

  @NotNull
  @Min(0)
  private Integer stock;

  @Column(length = 500)
  private String imagen;

  @Column(length = 1000)
  private String descripcion;

  private String material;
  private String peso;
  private String resistenciaAgua;
  private String camuflaje;
  private String gradoMilitar;
}