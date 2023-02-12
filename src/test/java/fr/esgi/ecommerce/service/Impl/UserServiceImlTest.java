package fr.esgi.ecommerce.service.Impl;

import fr.esgi.ecommerce.domain.Product;
import fr.esgi.ecommerce.domain.Review;
import fr.esgi.ecommerce.domain.Role;
import fr.esgi.ecommerce.domain.User;
import fr.esgi.ecommerce.repository.ProductRepository;
import fr.esgi.ecommerce.repository.ReviewRepository;
import fr.esgi.ecommerce.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static fr.esgi.ecommerce.util.TestConstants.FIRST_NAME;
import static fr.esgi.ecommerce.util.TestConstants.USER_EMAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImlTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ReviewRepository reviewRepository;

    @Test
    public void findUserById() {
        User user = new User();
        user.setId(122L);

        when(userRepository.findById(122L)).thenReturn(java.util.Optional.of(user));
        userService.findUserById(122L);
        assertEquals(122L, user.getId());
        verify(userRepository, times(1)).findById(122L);
    }

    @Test
    public void findUserByEmail() {
        User user = new User();
        user.setEmail(USER_EMAIL);
        userService.findUserByEmail(USER_EMAIL);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(user);
        assertEquals(USER_EMAIL, user.getEmail());
        verify(userRepository, times(1)).findByEmail(USER_EMAIL);
    }

    @Test
    public void findAllUsers() {
        List<User> usersList = new ArrayList<>();
        usersList.add(new User());
        usersList.add(new User());
        userService.findAllUsers();

        when(userRepository.findAllByOrderByIdAsc()).thenReturn(usersList);
        assertEquals(2, usersList.size());
        verify(userRepository, times(1)).findAllByOrderByIdAsc();
    }

    @Test
    public void getCart() {
        List<Long> productIds = new ArrayList<>(Arrays.asList(2L, 4L));
        Product firstProduct = new Product();
        firstProduct.setId(2L);
        Product secondProduct = new Product();
        secondProduct.setId(4L);
        List<Product> productList = new ArrayList<>(Arrays.asList(firstProduct, secondProduct));
        userService.getCart(productIds);

        when(productRepository.findByIdIn(productIds)).thenReturn(productList);
        assertEquals(2, productList.size());
        assertEquals(2, productIds.size());
        assertNotNull(productList);
        verify(productRepository, times(1)).findByIdIn(productIds);
    }

    @Test
    public void loadUserByUsername() {
        User user = new User();
        user.setEmail(USER_EMAIL);
        user.setActive(true);
        user.setFirstName(FIRST_NAME);
        user.setRoles(Collections.singleton(Role.USER));

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(user);
        assertEquals(USER_EMAIL, user.getEmail());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertTrue(user.isActive());
    }

    @Test
    public void updateProfile() {
        User user = new User();
        user.setEmail(USER_EMAIL);
        user.setFirstName(FIRST_NAME);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        userService.updateProfile(USER_EMAIL, user);
        assertEquals(USER_EMAIL, user.getEmail());
        assertEquals(FIRST_NAME, user.getFirstName());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void addReviewToProduct() {
        List<Review> reviewList = new ArrayList<>();
        Review review = new Review();
        review.setRating(5);
        reviewList.add(review);
        Product product = new Product();
        product.setId(123L);
        product.setReviews(reviewList);

        when(productRepository.getOne(123L)).thenReturn(product);
        when(reviewRepository.save(review)).thenReturn(review);
        userService.addReviewToProduct(review, 123L);
        assertEquals(123L, product.getId());
        assertNotNull(product.getReviews());
        verify(productRepository, times(1)).getOne(123L);
        verify(reviewRepository, times(1)).save(review);
    }
}
