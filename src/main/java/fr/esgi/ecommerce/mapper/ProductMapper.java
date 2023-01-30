package fr.esgi.ecommerce.mapper;

import fr.esgi.ecommerce.domain.Product;
import fr.esgi.ecommerce.dto.product.ProductRequest;
import fr.esgi.ecommerce.dto.product.ProductResponse;
import fr.esgi.ecommerce.service.FilesStorageService;
import fr.esgi.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final FilesStorageService filesStorageService;

    private Product convertToEntity(ProductRequest productRequest) {
        return modelMapper.map(productRequest, Product.class);
    }

    ProductResponse convertToResponseDto(Product product) {
        var response = modelMapper.map(product, ProductResponse.class);
        if(!(response.getFilename().isEmpty() && response.getFilename().isBlank())){
            var resource = filesStorageService.load(response.getFilename());
            response.setFile(resource);
        }
        return response;
    }

    List<ProductResponse> convertListToResponseDto(List<Product> products) {
        return products.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public ProductResponse findProductById(Long productId) {
        return convertToResponseDto(productService.findProductById(productId));
    }

    public List<ProductResponse> findProductsByIds(List<Long> productsId) {
        return convertListToResponseDto(productService.findProductsByIds(productsId));
    }

    public List<ProductResponse> findAllProducts() {
        return convertListToResponseDto(productService.findAllProducts());
    }

    public List<ProductResponse> filter(List<String> productrs, List<String> genders, List<Integer> prices, boolean sortByPrice) {
        return convertListToResponseDto(productService.filter(productrs, genders, prices, sortByPrice));
    }

    public List<ProductResponse> findByProductrOrderByPriceDesc(String productr) {
        return convertListToResponseDto(productService.findByProductrOrderByPriceDesc(productr));
    }

    public List<ProductResponse> findByProductGenderOrderByPriceDesc(String productGender) {
        return convertListToResponseDto(productService.findByProductGenderOrderByPriceDesc(productGender));
    }

    public ProductResponse saveProduct(ProductRequest productRequest, MultipartFile file) {
        return convertToResponseDto(productService.saveProduct(convertToEntity(productRequest), file));
    }

    public List<ProductResponse> deleteOrder(Long productId) {
        return convertListToResponseDto(productService.deleteProduct(productId));
    }
}
