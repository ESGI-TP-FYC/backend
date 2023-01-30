package fr.esgi.ecommerce.mapper;

import fr.esgi.ecommerce.domain.Review;
import fr.esgi.ecommerce.domain.User;
import fr.esgi.ecommerce.dto.RegistrationRequest;
import fr.esgi.ecommerce.dto.product.ProductResponse;
import fr.esgi.ecommerce.dto.review.ReviewRequest;
import fr.esgi.ecommerce.dto.user.UserRequest;
import fr.esgi.ecommerce.dto.user.UserResponse;
import fr.esgi.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;
    private final ProductMapper productMapper;
    private final UserService userService;

    private User convertToEntity(UserRequest userRequest) {
        return modelMapper.map(userRequest, User.class);
    }

    User convertToEntity(RegistrationRequest registrationRequest) {
        return modelMapper.map(registrationRequest, User.class);
    }

    private Review convertToEntity(ReviewRequest reviewRequest) {
        return modelMapper.map(reviewRequest, Review.class);
    }

    UserResponse convertToResponseDto(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    public UserResponse findUserById(Long userId) {
        return convertToResponseDto(userService.findUserById(userId));
    }

    public UserResponse findUserByEmail(String email) {
        return convertToResponseDto(userService.findUserByEmail(email));
    }

    public List<ProductResponse> getCart(List<Long> productsIds) {
        return productMapper.convertListToResponseDto(userService.getCart(productsIds));
    }

    public List<UserResponse> findAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public UserResponse updateProfile(String email, UserRequest userRequest) {
        return convertToResponseDto(userService.updateProfile(email, convertToEntity(userRequest)));
    }

    public ProductResponse addReviewToProduct(ReviewRequest reviewRequest, Long productId) {
        return productMapper.convertToResponseDto(userService.addReviewToProduct(convertToEntity(reviewRequest), productId));
    }
}
