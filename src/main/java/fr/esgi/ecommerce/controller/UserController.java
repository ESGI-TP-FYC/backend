package fr.esgi.ecommerce.controller;

import fr.esgi.ecommerce.dto.GraphQLRequest;
import fr.esgi.ecommerce.dto.order.OrderRequest;
import fr.esgi.ecommerce.dto.order.OrderResponse;
import fr.esgi.ecommerce.dto.product.ProductResponse;
import fr.esgi.ecommerce.dto.review.ReviewRequest;
import fr.esgi.ecommerce.dto.user.UserRequest;
import fr.esgi.ecommerce.dto.user.UserResponse;
import fr.esgi.ecommerce.exception.InputFieldException;
import fr.esgi.ecommerce.mapper.OrderMapper;
import fr.esgi.ecommerce.mapper.UserMapper;
import fr.esgi.ecommerce.security.UserPrincipal;
import fr.esgi.ecommerce.service.graphql.GraphQLProvider;
import graphql.ExecutionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final GraphQLProvider graphQLProvider;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/info")
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(userMapper.findUserByEmail(user.getEmail()));
    }

    @PostMapping("/graphql/info")
    public ResponseEntity<ExecutionResult> getUserInfoByQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }

    @PutMapping("/edit")
    public ResponseEntity<UserResponse> updateUserInfo(@AuthenticationPrincipal UserPrincipal user,
                                                       @Valid @RequestBody UserRequest request,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        } else {
            return ResponseEntity.ok(userMapper.updateProfile(user.getEmail(), request));
        }
    }

    @PostMapping("/cart")
    public ResponseEntity<List<ProductResponse>> getCart(@RequestBody List<Long> productsIds) {
        return ResponseEntity.ok(userMapper.getCart(productsIds));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(orderMapper.findOrderByEmail(user.getEmail()));
    }

    @PostMapping("/graphql/orders")
    public ResponseEntity<ExecutionResult> getUserOrdersByQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResponse> postOrder(@Valid @RequestBody OrderRequest order, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        } else {
            return ResponseEntity.ok(orderMapper.postOrder(order));
        }
    }

    @PostMapping("/review")
    public ResponseEntity<ProductResponse> addReviewToProduct(@Valid @RequestBody ReviewRequest review,
                                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        } else {
            ProductResponse product = userMapper.addReviewToProduct(review, review.getProductId());
            messagingTemplate.convertAndSend("/topic/reviews/" + product.getId(), product);
            return ResponseEntity.ok(product);
        }
    }
}
