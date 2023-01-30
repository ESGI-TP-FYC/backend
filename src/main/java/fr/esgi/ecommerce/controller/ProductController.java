package fr.esgi.ecommerce.controller;

import fr.esgi.ecommerce.dto.GraphQLRequest;
import fr.esgi.ecommerce.dto.product.ProductResponse;
import fr.esgi.ecommerce.dto.product.ProductSearchRequest;
import fr.esgi.ecommerce.mapper.ProductMapper;
import fr.esgi.ecommerce.service.graphql.GraphQLProvider;
import graphql.ExecutionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductMapper productMapper;
    private final GraphQLProvider graphQLProvider;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productMapper.findAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(productMapper.findProductById(productId));
    }

    @PostMapping("/ids")
    public ResponseEntity<List<ProductResponse>> getProductsByIds(@RequestBody List<Long> productsIds) {
        return ResponseEntity.ok(productMapper.findProductsByIds(productsIds));
    }

    @PostMapping("/search")
    public ResponseEntity<List<ProductResponse>> findProductsByFilterParams(@RequestBody ProductSearchRequest filter) {
        return ResponseEntity.ok(productMapper.filter(filter.getProductrs(), filter.getGenders(), filter.getPrices(), filter.isSortByPrice()));
    }

    @PostMapping("/search/gender")
    public ResponseEntity<List<ProductResponse>> findByProductGender(@RequestBody ProductSearchRequest filter) {
        return ResponseEntity.ok(productMapper.findByProductGenderOrderByPriceDesc(filter.getProductGender()));
    }

    @PostMapping("/search/productr")
    public ResponseEntity<List<ProductResponse>> findByProductr(@RequestBody ProductSearchRequest filter) {
        return ResponseEntity.ok(productMapper.findByProductrOrderByPriceDesc(filter.getProductr()));
    }

    @PostMapping("/graphql/ids")
    public ResponseEntity<ExecutionResult> getProductsByIdsQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }

    @PostMapping("/graphql/products")
    public ResponseEntity<ExecutionResult> getAllProductsByQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }

    @PostMapping("/graphql/product")
    public ResponseEntity<ExecutionResult> getProductByQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }
}
