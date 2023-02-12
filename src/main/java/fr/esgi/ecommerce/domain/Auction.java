package fr.esgi.ecommerce.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "auction")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auction_id_seq")
    @SequenceGenerator(name = "auction_id_seq", sequenceName = "auction_id_seq", initialValue = 1, allocationSize = 1)
    private Long id;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BidProduct> products;

}
