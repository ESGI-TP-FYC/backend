package fr.esgi.ecommerce.dto.product;

import fr.esgi.ecommerce.dto.review.ReviewResponse;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductResponse {
    private Long id;
    private String productTitle;
    private String productr;
    private Integer year;
    private String country;
    private String productGender;
    private String fragranceTopNotes;
    private String fragranceMiddleNotes;
    private String fragranceBaseNotes;
    private String description;
    private String filename;
    private Integer price;
    private String volume;
    private String type;
    private Double productRating;
    private List<ReviewResponse> reviews;
    private byte[] file;
}
