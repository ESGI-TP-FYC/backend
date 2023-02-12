package fr.esgi.ecommerce.repository;

import fr.esgi.ecommerce.domain.Auction;
import fr.esgi.ecommerce.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

}
