package fr.esgi.ecommerce.dto.auction;


import fr.esgi.ecommerce.domain.BidProduct;
import fr.esgi.ecommerce.dto.review.ReviewResponse;
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
public class BidProductDto {
    private Long id;
    private String productTitle;
    private String productr;
    private Integer year;
    private String country;
    private String productGender;
    private String description;
    private String filename;
    private Integer price;
    private String volume;
    private String type;
    private List<BidDto> bids;
    private byte[] file;
}
