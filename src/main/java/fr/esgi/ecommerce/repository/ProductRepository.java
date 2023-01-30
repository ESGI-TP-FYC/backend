package fr.esgi.ecommerce.repository;

import fr.esgi.ecommerce.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByOrderByIdAsc();

    List<Product> findByProductrIn(List<String> productrs);

    List<Product> findByProductGenderIn(List<String> genders);

    List<Product> findByPriceBetween(Integer startingPrice, Integer endingPrice);

    List<Product> findByProductrOrderByPriceDesc(String productr);

    List<Product> findByProductGenderOrderByPriceDesc(String productGender);

    List<Product> findByIdIn(List<Long> productsIds);
}
