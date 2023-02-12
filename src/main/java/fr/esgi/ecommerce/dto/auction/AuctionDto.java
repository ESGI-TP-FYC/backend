package fr.esgi.ecommerce.dto.auction;


import fr.esgi.ecommerce.domain.BidProduct;
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
public class AuctionDto {
    private Long id;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private List<BidProductDto> products;
}
