package fr.esgi.ecommerce.service;

import fr.esgi.ecommerce.domain.Order;
import graphql.schema.DataFetcher;

import java.util.List;
import java.util.Map;

public interface OrderService {

    List<Order> findAll();

    List<Order> findOrderByEmail(String email);

    Order postOrder(Order validOrder, Map<Long, Long> productsId);

    List<Order> deleteOrder(Long orderId);

    DataFetcher<List<Order>> getAllOrdersByQuery();

    DataFetcher<List<Order>> getUserOrdersByEmailQuery();

     Order postAuctionOrder(Long bidProductId);
}
