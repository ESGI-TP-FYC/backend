package fr.esgi.ecommerce.repository;

import fr.esgi.ecommerce.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderByIdAsc();

    List<Order> findOrderByEmail(String email);
}
