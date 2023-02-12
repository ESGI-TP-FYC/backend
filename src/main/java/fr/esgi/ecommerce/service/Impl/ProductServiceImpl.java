package fr.esgi.ecommerce.service.Impl;

import fr.esgi.ecommerce.domain.Product;
import fr.esgi.ecommerce.domain.Review;
import fr.esgi.ecommerce.repository.ProductRepository;
import fr.esgi.ecommerce.repository.ReviewRepository;
import fr.esgi.ecommerce.service.FilesStorageService;
import fr.esgi.ecommerce.service.ProductService;
import graphql.schema.DataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final FilesStorageService filesStorageService;

    @Override
    public DataFetcher<Product> getProductByQuery() {
        return dataFetchingEnvironment -> {
            Long productId = Long.parseLong(dataFetchingEnvironment.getArgument("id"));
            return productRepository.findById(productId).get();
        };
    }

    @Override
    public DataFetcher<List<Product>> getAllProductsByQuery() {
        return dataFetchingEnvironment -> productRepository.findAllByOrderByIdAsc();
    }

    @Override
    public DataFetcher<List<Product>> getAllProductsByIdsQuery() {
        return dataFetchingEnvironment -> {
            List<String> objects = dataFetchingEnvironment.getArgument("ids");
            List<Long> productsId = objects.stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            return productRepository.findByIdIn(productsId);
        };
    }

    @Override
    public Product findProductById(Long productId) {
        return productRepository.findById(productId).get();
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAllByOrderByIdAsc();
    }

    @Override
    public List<Product> findProductsByIds(List<Long> productsId) {
        return productRepository.findByIdIn(productsId);
    }

    @Override
    public List<Product> filter(List<String> productrs, List<String> genders, List<Integer> prices, boolean sortByPrice) {
        List<Product> productList = new ArrayList<>();

        if (!productrs.isEmpty() || !genders.isEmpty() || !prices.isEmpty()) {
            if (!productrs.isEmpty()) {
                if (!productList.isEmpty()) {
                    List<Product> productrsList = new ArrayList<>();
                    for (String productr : productrs) {
                        productrsList.addAll(productList.stream()
                                .filter(product -> product.getProductr().equals(productr))
                                .collect(Collectors.toList()));
                    }
                    productList = productrsList;
                } else {
                    productList.addAll(productRepository.findByProductrIn(productrs));
                }
            }
            if (!genders.isEmpty()) {
                if (!productList.isEmpty()) {
                    List<Product> gendersList = new ArrayList<>();
                    for (String gender : genders) {
                        gendersList.addAll(productList.stream()
                                .filter(product -> product.getProductGender().equals(gender))
                                .collect(Collectors.toList()));
                    }
                    productList = gendersList;
                } else {
                    productList.addAll(productRepository.findByProductGenderIn(genders));
                }
            }
            if (!prices.isEmpty()) {
                productList = productRepository.findByPriceBetween(prices.get(0), prices.get(1));
            }
        } else {
            productList = productRepository.findAllByOrderByIdAsc();
        }
        if (sortByPrice) {
            productList.sort(Comparator.comparing(Product::getPrice));
        } else {
            productList.sort((product1, product2) -> product2.getPrice().compareTo(product1.getPrice()));
        }
        return productList;
    }

    @Override
    public List<Product> findByProductrOrderByPriceDesc(String productr) {
        return productRepository.findByProductrOrderByPriceDesc(productr);
    }

    @Override
    public List<Product> findByProductGenderOrderByPriceDesc(String productGender) {
        return productRepository.findByProductGenderOrderByPriceDesc(productGender);
    }

    @Override
    public Product saveProduct(Product product, MultipartFile multipartFile) {
            String fileName = UUID.randomUUID() + "." + multipartFile.getOriginalFilename();
            filesStorageService.save(multipartFile, Path.of(fileName));
            product.setFilename(fileName);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public List<Product> deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).get();
        product.getReviews().forEach(review -> reviewRepository.deleteById(review.getId()));
        productRepository.delete(product);
        return productRepository.findAllByOrderByIdAsc();
    }
}
