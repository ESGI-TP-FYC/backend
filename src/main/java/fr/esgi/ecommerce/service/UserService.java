package fr.esgi.ecommerce.service;

import fr.esgi.ecommerce.domain.Product;
import fr.esgi.ecommerce.domain.Review;
import fr.esgi.ecommerce.domain.User;
import graphql.schema.DataFetcher;

import java.util.List;

public interface UserService {

    User findUserById(Long userId);

    User findUserByEmail(String email);

    DataFetcher<List<User>> getAllUsersByQuery();

    DataFetcher<User> getUserByQuery();

    List<User> findAllUsers();

    List<Product> getCart(List<Long> productIds);

    User updateProfile(String email, User user);

    Product addReviewToProduct(Review review, Long productId);
}
