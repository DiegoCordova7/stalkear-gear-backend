package universidad.eccomerce.dto;

public record CartItemResponse(
    Long id,
    String productId,
    String productName,
    String productImage,
    Double unitPrice,
    Integer quantity,
    Integer availableStock,
    Double lineSubtotal
) {}