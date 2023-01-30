package fr.esgi.ecommerce.service.Impl;

import fr.esgi.ecommerce.domain.Product;
import fr.esgi.ecommerce.domain.Review;
import fr.esgi.ecommerce.domain.User;
import fr.esgi.ecommerce.repository.ProductRepository;
import fr.esgi.ecommerce.repository.ReviewRepository;
import fr.esgi.ecommerce.repository.UserRepository;
import fr.esgi.ecommerce.service.UserService;
import graphql.schema.DataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAllByOrderByIdAsc();
    }

    @Override
    public DataFetcher<User> getUserByQuery() {
        return dataFetchingEnvironment -> {
            Long userId = Long.parseLong(dataFetchingEnvironment.getArgument("id"));
            return userRepository.findById(userId).get();
        };
    }

    @Override
    public DataFetcher<List<User>> getAllUsersByQuery() {
        return dataFetchingEnvironment -> userRepository.findAllByOrderByIdAsc();
    }

    @Override
    public List<Product> getCart(List<Long> productIds) {
        return productRepository.findByIdIn(productIds);
    }

    @Override
    public User updateProfile(String email, User user) {
        User userFromDb = userRepository.findByEmail(email);
        userFromDb.setFirstName(user.getFirstName());
        userFromDb.setLastName(user.getLastName());
        userFromDb.setCity(user.getCity());
        userFromDb.setAddress(user.getAddress());
        userFromDb.setPhoneNumber(user.getPhoneNumber());
        userFromDb.setPostIndex(user.getPostIndex());
        userRepository.save(userFromDb);
        return userFromDb;
    }

    @Override
    public Product addReviewToProduct(Review review, Long productId) {
        Product product = productRepository.getOne(productId);
        List<Review> reviews = product.getReviews();
        reviews.add(review);
        double totalReviews = reviews.size();
        double sumRating = reviews.stream().mapToInt(Review::getRating).sum();
        product.setProductRating(sumRating / totalReviews);
        reviewRepository.save(review);
        return product;
    }
}
