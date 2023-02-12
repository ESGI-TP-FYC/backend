package fr.esgi.ecommerce.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bid")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bid_id_seq")
    @SequenceGenerator(name = "bid_id_seq", sequenceName = "bid_id_seq", initialValue = 1, allocationSize = 1)
    private Long id;
    private String userEmail;
    private Integer amount;
    private LocalDateTime date;

}
