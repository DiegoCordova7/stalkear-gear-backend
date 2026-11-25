package universidad.eccomerce.dto;

import java.util.List;

public record CartResponse(
    String cartId,
    String userId,
    List<CartItemResponse> items,
    Double subtotal
) {}