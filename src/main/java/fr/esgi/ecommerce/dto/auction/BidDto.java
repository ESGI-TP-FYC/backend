package fr.esgi.ecommerce.dto.auction;


import fr.esgi.ecommerce.domain.BidProduct;
import fr.esgi.ecommerce.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BidDto {
    private Long id;
    private String userEmail;
    private Integer amount;
    private LocalDateTime date;
}
