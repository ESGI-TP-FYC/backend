package fr.esgi.ecommerce.service;

import fr.esgi.ecommerce.domain.Product;
import graphql.schema.DataFetcher;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    DataFetcher<Product> getProductByQuery();

    DataFetcher<List<Product>> getAllProductsByQuery();

    DataFetcher<List<Product>> getAllProductsByIdsQuery();

    Product findProductById(Long productId);

    List<Product> findAllProducts();

    List<Product> findProductsByIds(List<Long> productsId);

    List<Product> filter(List<String> productrs, List<String> genders, List<Integer> prices, boolean sortByPrice);

    List<Product> findByProductrOrderByPriceDesc(String productr);

    List<Product> findByProductGenderOrderByPriceDesc(String productGender);

    Product saveProduct(Product product, MultipartFile file);

    List<Product> deleteProduct(Long productId);
}
