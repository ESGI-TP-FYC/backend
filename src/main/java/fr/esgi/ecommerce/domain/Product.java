package fr.esgi.ecommerce.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @SequenceGenerator(name = "product_id_seq", sequenceName = "product_id_seq", initialValue = 109, allocationSize = 1)
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

    @OneToMany
    private List<Review> reviews;
}
