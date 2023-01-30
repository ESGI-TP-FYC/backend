package fr.esgi.ecommerce.dto.order;

import fr.esgi.ecommerce.dto.product.ProductResponse;
import lombok.Data;

@Data
public class OrderItemResponse {
    private Long id;
    private Long amount;
    private Long quantity;
    private ProductResponse product;
}
