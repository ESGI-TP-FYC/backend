package fr.esgi.ecommerce.repository;

import fr.esgi.ecommerce.domain.Auction;
import fr.esgi.ecommerce.domain.BidProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidProductRepository extends JpaRepository<BidProduct, Long> {

}
