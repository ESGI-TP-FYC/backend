package fr.esgi.ecommerce.service.Impl;

import fr.esgi.ecommerce.domain.BidProduct;
import fr.esgi.ecommerce.domain.Order;
import fr.esgi.ecommerce.domain.OrderItem;
import fr.esgi.ecommerce.domain.Product;
import fr.esgi.ecommerce.repository.*;
import fr.esgi.ecommerce.service.OrderService;
import fr.esgi.ecommerce.service.email.MailSenderService;
import graphql.schema.DataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final MailSenderService mailSenderService;
    private final BidProductRepository bidProductRepository;
    private final UserRepository userRepository;
    @Override
    public List<Order> findAll() {
        return orderRepository.findAllByOrderByIdAsc();
    }

    @Override
    public DataFetcher<List<Order>> getAllOrdersByQuery() {
        return dataFetchingEnvironment -> orderRepository.findAllByOrderByIdAsc();
    }

    @Override
    public DataFetcher<List<Order>> getUserOrdersByEmailQuery() {
        return dataFetchingEnvironment -> {
            String email = dataFetchingEnvironment.getArgument("email").toString();
            return orderRepository.findOrderByEmail(email);
        };
    }

    @Override
    public List<Order> findOrderByEmail(String email) {
        return orderRepository.findOrderByEmail(email);
    }

    @Override
    @Transactional
    public Order postOrder(Order validOrder, Map<Long, Long> productsId) {
        Order order = new Order();
        List<OrderItem> orderItemList = new ArrayList<>();

        for (Map.Entry<Long, Long> entry : productsId.entrySet()) {
            Product product = productRepository.findById(entry.getKey()).get();
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setAmount((product.getPrice() * entry.getValue()));
            orderItem.setQuantity(entry.getValue());
            orderItemList.add(orderItem);
            orderItemRepository.save(orderItem);
        }
        order.getOrderItems().addAll(orderItemList);
        order.setTotalPrice(validOrder.getTotalPrice());
        order.setFirstName(validOrder.getFirstName());
        order.setLastName(validOrder.getLastName());
        order.setCity(validOrder.getCity());
        order.setAddress(validOrder.getAddress());
        order.setPostIndex(validOrder.getPostIndex());
        order.setEmail(validOrder.getEmail());
        order.setPhoneNumber(validOrder.getPhoneNumber());
        orderRepository.save(order);

        String subject = "Order #" + order.getId();
        String template = "order-template";
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("order", order);
        mailSenderService.sendMessageHtml(order.getEmail(), subject, template, attributes);
        return order;
    }

    @Override
    @Transactional
    public Order postAuctionOrder(Long productId) {
        Order order = new Order();
        List<OrderItem> orderItemList = new ArrayList<>();
        var product = bidProductRepository.findById(productId).get();
        var bid = product.getBids().stream().max((o1, o2) -> (int) (o1.getAmount() - o2.getAmount())).get();
            OrderItem orderItem = new OrderItem();
            orderItem.setBidProduct(product);
            orderItem.setAmount(Long.valueOf(bid.getAmount()));
            orderItem.setQuantity(1l);
            orderItemList.add(orderItem);
            orderItemRepository.save(orderItem);
            var user = userRepository.findByEmail(bid.getUserEmail());
        order.getOrderItems().addAll(orderItemList);
        order.setTotalPrice(bid.getAmount() * 1.0);
        order.setFirstName(user.getFirstName());
        order.setLastName(user.getLastName());
        order.setCity(user.getCity());
        order.setAddress(user.getAddress());
        order.setPostIndex(Integer.valueOf(user.getPostIndex()));
        order.setEmail(user.getEmail());
        order.setPhoneNumber(user.getPhoneNumber());
        orderRepository.save(order);

        String subject = "Auction order #" + order.getId();
        String template = "order-template";
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("order", order);
        mailSenderService.sendMessageHtml(order.getEmail(), subject, template, attributes);
        return order;
    }

    @Override
    @Transactional
    public List<Order> deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new IllegalArgumentException());
        order.getOrderItems().forEach(orderItem -> orderItemRepository.deleteById(orderItem.getId()));
        orderRepository.delete(order);
        return orderRepository.findAllByOrderByIdAsc();
    }
}
