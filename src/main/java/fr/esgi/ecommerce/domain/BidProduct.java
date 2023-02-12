package fr.esgi.ecommerce.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "bid_product")
public class BidProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bid_product_id_seq")
    @SequenceGenerator(name = "bid_product_id_seq", sequenceName = "bid_product_id_seq", initialValue = 109, allocationSize = 1)
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
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Bid> bids;
}
