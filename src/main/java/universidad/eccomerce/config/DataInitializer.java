package universidad.eccomerce.config;

import universidad.eccomerce.model.Product;
import universidad.eccomerce.model.User;
import universidad.eccomerce.repository.IProductRepository;
import universidad.eccomerce.repository.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

  @Bean
  public CommandLineRunner initData(IProductRepository productRepo, IUserRepository userRepo) {
    return args -> {
      if (userRepo.count() == 0) {
        userRepo.save(new User("user-admin", "Comandante Admin", "admin@stalkear.com", "admin", "admin"));
        userRepo.save(new User("user-001", "Stalker Operador", "cliente@stalkear.com", "123", "client"));
      }

      if (productRepo.count() == 0) {
        Product p1 = new Product();
        p1.setId("prod-001");
        p1.setNombre("Stalker Bivy & Tarp System");
        p1.setCategoria("Refugios & Acampada");
        p1.setPrecio(3499.00);
        p1.setStock(15);
        p1.setImagen("/assets/images/stalker-bivy-and-tarp-system.webp");
        p1.setDescripcion("Sistema de refugio modular de camuflaje, impermeable y de bajo perfil.");
        p1.setMaterial("Nylon Ripstop 70D con recubrimiento PU");
        p1.setPeso("1.2 kg");
        p1.setResistenciaAgua("5000 mm");
        p1.setCamuflaje("MultiCam / Woodland");
        p1.setGradoMilitar("MIL-SPEC");
        productRepo.save(p1);

        Product p2 = new Product();
        p2.setId("prod-002");
        p2.setNombre("Stalker Canteen & Mess Kit");
        p2.setCategoria("Hidratación & Cocina");
        p2.setPrecio(899.00);
        p2.setStock(25);
        p2.setImagen("/assets/images/stalker-canteen-and-mess-kit.webp");
        p2.setDescripcion("Cantimplora táctica de aluminio anodizado con jarro acoplable y funda MOLLE.");
        p2.setMaterial("Aluminio grado alimenticio 100%");
        p2.setPeso("0.45 kg");
        p2.setResistenciaAgua("N/A (Estanqueidad total)");
        p2.setCamuflaje("Verde Oliva");
        p2.setGradoMilitar("MIL-SPEC");
        productRepo.save(p2);

        Product p3 = new Product();
        p3.setId("prod-003");
        p3.setNombre("Mochila Táctica Recon 45L");
        p3.setCategoria("Carga & Almacenamiento");
        p3.setPrecio(2199.00);
        p3.setStock(10);
        p3.setImagen("/assets/images/mochila-tactica-recon-45l.webp");
        p3.setDescripcion("Mochila ergonómica de combate con expansiones MOLLE y compartimento para vejiga.");
        p3.setMaterial("Cordura 1000D");
        p3.setPeso("1.8 kg");
        p3.setResistenciaAgua("1500 mm (Repelente)");
        p3.setCamuflaje("Coyote Tan");
        p3.setGradoMilitar("Táctico Comercial");
        productRepo.save(p3);

        Product p4 = new Product();
        p4.setId("prod-004");
        p4.setNombre("Botas de Combate All-Terrain");
        p4.setCategoria("Calzado");
        p4.setPrecio(2899.00);
        p4.setStock(8);
        p4.setImagen("/assets/images/botas-de-combate-all-terrain.webp");
        p4.setDescripcion("Calzado de alta tracción con plantilla anti-perforación y soporte de tobillo.");
        p4.setMaterial("Piel de ante y Nylon Balístico");
        p4.setPeso("1.4 kg (Par)");
        p4.setResistenciaAgua("Membrana Transpirable Waterproof");
        p4.setCamuflaje("Negro Táctico");
        p4.setGradoMilitar("MIL-SPEC");
        productRepo.save(p4);

        Product p5 = new Product();
        p5.setId("prod-005");
        p5.setNombre("Cuchillo de Supervivencia Full-Tang");
        p5.setCategoria("Herramientas");
        p5.setPrecio(1450.00);
        p5.setStock(12);
        p5.setImagen("/assets/images/cuchillo-de-supervivencia-full-tang.webp");
        p5.setDescripcion("Hoja fija de acero al carbono con rompecristales integrado y funda de Kydex.");
        p5.setMaterial("Acero D2 / Empuñadura G10");
        p5.setPeso("0.38 kg");
        p5.setResistenciaAgua("Recubrimiento Anti-corrosión Titanium");
        p5.setCamuflaje("Negro Mate");
        p5.setGradoMilitar("Supervivencia Extrema");
        productRepo.save(p5);

        Product p6 = new Product();
        p6.setId("prod-006");
        p6.setNombre("Linterna Táctica 2000 Lumens Strobe");
        p6.setCategoria("Iluminación & Óptica");
        p6.setPrecio(750.00);
        p6.setStock(20);
        p6.setImagen("/assets/images/linterna-tactica-2000-lumens-strobe.webp");
        p6.setDescripcion("Linterna recargable de alta intensidad con modo estroboscópico de emergencia.");
        p6.setMaterial("Aluminio Aeronáutico T6061");
        p6.setPeso("0.22 kg");
        p6.setResistenciaAgua("Sumergible IPX8 (2m)");
        p6.setCamuflaje("Anodizado Negro");
        p6.setGradoMilitar("Táctico Policial");
        productRepo.save(p6);

        Product p7 = new Product();
        p7.setId("prod-007");
        p7.setNombre("Chest Rig Modular Stalker");
        p7.setCategoria("Vestimenta Táctica");
        p7.setPrecio(1850.00);
        p7.setStock(14);
        p7.setImagen("/assets/images/chest-rig-modular-stalker.webp");
        p7.setDescripcion("Arnés pectoral ligero con porta-cargadores intercambiables y panel velcro.");
        p7.setMaterial("Nylon 500D LAMINEX");
        p7.setPeso("0.65 kg");
        p7.setResistenciaAgua("Repelente al agua");
        p7.setCamuflaje("Ranger Green");
        p7.setGradoMilitar("MIL-SPEC");
        productRepo.save(p7);

        Product p8 = new Product();
        p8.setId("prod-008");
        p8.setNombre("Botiquín Táctico de Trauma IFAK");
        p8.setCategoria("Médico & Emergencias");
        p8.setPrecio(1200.00);
        p8.setStock(18);
        p8.setImagen("/assets/images/botiquin-tactico-de-trauma-ifak.webp");
        p8.setDescripcion("Bolsa médica de desprendimiento rápido con organizadores elásticos internos.");
        p8.setMaterial("Cordura 500D Ripstop");
        p8.setPeso("0.30 kg");
        p8.setResistenciaAgua("Resistente a salpicaduras");
        p8.setCamuflaje("Rojo / Coyote");
        p8.setGradoMilitar("Táctico Médico");
        productRepo.save(p8);

        Product p9 = new Product();
        p9.setId("prod-009");
        p9.setNombre("Cuerda Paracord 550 (30m)");
        p9.setCategoria("Accesorios & Herramientas");
        p9.setPrecio(299.00);
        p9.setStock(40);
        p9.setImagen("/assets/images/cuerda-paracord-550-30m.webp");
        p9.setDescripcion("Cuerda de paracord grado 550 con 7 hebras internas de nylon trenzado.");
        p9.setMaterial("100% Nylon Grado 550");
        p9.setPeso("0.25 kg");
        p9.setResistenciaAgua("Resistente a moho y rayos UV");
        p9.setCamuflaje("Camuflaje Digital");
        p9.setGradoMilitar("MIL-C-5040H");
        productRepo.save(p9);

        Product p10 = new Product();
        p10.setId("prod-010");
        p10.setNombre("Binoculares Tácticos 10x42 Nitrógeno");
        p10.setCategoria("Iluminación & Óptica");
        p10.setPrecio(3100.00);
        p10.setStock(6);
        p10.setImagen("/assets/images/binoculares-tacticos-10x42-nitrogeno.webp");
        p10.setDescripcion("Binoculares sellados con nitrógeno para evitar empañamiento bajo condiciones extremas.");
        p10.setMaterial("Polímero de alta resistencia con engomado");
        p10.setPeso("0.78 kg");
        p10.setResistenciaAgua("Sumergible IPX7");
        p10.setCamuflaje("Coyote Brown");
        p10.setGradoMilitar("Óptica Avanzada");
        productRepo.save(p10);
      }
    };
  }
}