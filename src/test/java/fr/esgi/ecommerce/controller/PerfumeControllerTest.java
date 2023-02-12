package fr.esgi.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.ecommerce.dto.GraphQLRequest;
import fr.esgi.ecommerce.dto.product.ProductSearchRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.esgi.ecommerce.util.TestConstants.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/create-products-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/create-products-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private ProductSearchRequest filter;
    private GraphQLRequest graphQLRequest;

    @Before
    public void init() {
        List<Integer> prices = new ArrayList<>();
        List<String> productrs = new ArrayList<>();
        List<String> genders = new ArrayList<>();
        productrs.add(PRODUCTR_CHANEL);
        genders.add(PRODUCT_GENDER);

        filter = new ProductSearchRequest();
        filter.setProductrs(productrs);
        filter.setGenders(genders);
        filter.setPrices(prices);

        graphQLRequest = new GraphQLRequest();
    }

    @Test
    public void getAllProducts() throws Exception {
        mockMvc.perform(get(URL_PRODUCTS_BASIC))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").exists())
                .andExpect(jsonPath("$[*].productTitle").exists())
                .andExpect(jsonPath("$[*].productr").exists())
                .andExpect(jsonPath("$[*].year").exists())
                .andExpect(jsonPath("$[*].country").exists())
                .andExpect(jsonPath("$[*].productGender").exists())
                .andExpect(jsonPath("$[*].fragranceTopNotes").exists())
                .andExpect(jsonPath("$[*].fragranceMiddleNotes").exists())
                .andExpect(jsonPath("$[*].fragranceBaseNotes").exists())
                .andExpect(jsonPath("$[*].description").exists())
                .andExpect(jsonPath("$[*].filename").exists())
                .andExpect(jsonPath("$[*].price").exists())
                .andExpect(jsonPath("$[*].volume").exists())
                .andExpect(jsonPath("$[*].type").exists())
                .andExpect(jsonPath("$[*].reviews[*]", iterableWithSize(greaterThan(1))))
                .andExpect(jsonPath("$[*].reviews[*].author").isNotEmpty());
    }

    @Test
    public void getProduct() throws Exception {
        mockMvc.perform(get(URL_PRODUCTS_BASIC + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.productTitle", equalTo("Boss Bottled Night")))
                .andExpect(jsonPath("$.productr", equalTo("Hugo Boss")))
                .andExpect(jsonPath("$.country", equalTo("Germany")));
    }

    @Test
    public void getProductsByIds() throws Exception {
        mockMvc.perform(post(URL_PRODUCTS_BASIC + "/ids")
                .content(mapper.writeValueAsString(Arrays.asList(3L, 4L, 5L)))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").isNotEmpty())
                .andExpect(jsonPath("$[*].productTitle").isNotEmpty())
                .andExpect(jsonPath("$[*].productr").isNotEmpty())
                .andExpect(jsonPath("$[*].year").isNotEmpty())
                .andExpect(jsonPath("$[*].country").isNotEmpty())
                .andExpect(jsonPath("$[*].productGender").isNotEmpty())
                .andExpect(jsonPath("$[*].fragranceTopNotes").isNotEmpty())
                .andExpect(jsonPath("$[*].fragranceMiddleNotes").isNotEmpty())
                .andExpect(jsonPath("$[*].fragranceBaseNotes").isNotEmpty())
                .andExpect(jsonPath("$[*].description").isNotEmpty())
                .andExpect(jsonPath("$[*].filename").isNotEmpty())
                .andExpect(jsonPath("$[*].price").isNotEmpty())
                .andExpect(jsonPath("$[*].volume").isNotEmpty())
                .andExpect(jsonPath("$[*].type").isNotEmpty());
    }

    @Test
    public void findProductsByFilterParams() throws Exception {
        mockMvc.perform(post(URL_PRODUCTS_SEARCH)
                .content(mapper.writeValueAsString(filter))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").isNotEmpty())
                .andExpect(jsonPath("$[*].productTitle").isNotEmpty())
                .andExpect(jsonPath("$[*].productr").isNotEmpty())
                .andExpect(jsonPath("$[*].year").isNotEmpty())
                .andExpect(jsonPath("$[*].country").isNotEmpty())
                .andExpect(jsonPath("$[*].productGender").isNotEmpty())
                .andExpect(jsonPath("$[*].fragranceTopNotes").isNotEmpty())
                .andExpect(jsonPath("$[*].fragranceMiddleNotes").isNotEmpty())
                .andExpect(jsonPath("$[*].fragranceBaseNotes").isNotEmpty())
                .andExpect(jsonPath("$[*].description").isNotEmpty())
                .andExpect(jsonPath("$[*].filename").isNotEmpty())
                .andExpect(jsonPath("$[*].price").isNotEmpty())
                .andExpect(jsonPath("$[*].volume").isNotEmpty())
                .andExpect(jsonPath("$[*].type").isNotEmpty());
    }

    @Test
    public void findProductsByFilterParamsProductrs() throws Exception {
        ProductSearchRequest filter = new ProductSearchRequest();
        List<String> productrs = new ArrayList<>();
        productrs.add(PRODUCTR_CHANEL);
        List<Integer> prices = new ArrayList<>();
        prices.add(150);
        prices.add(250);

        filter.setProductrs(productrs);
        filter.setGenders(new ArrayList<>());
        filter.setPrices(prices);
        filter.setSortByPrice(true);

        mockMvc.perform(post(URL_PRODUCTS_SEARCH)
                .content(mapper.writeValueAsString(filter))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").isNotEmpty())
                .andExpect(jsonPath("$[*].productTitle").isNotEmpty())
                .andExpect(jsonPath("$[*].productr").isNotEmpty())
                .andExpect(jsonPath("$[*].year").isNotEmpty())
                .andExpect(jsonPath("$[*].country").isNotEmpty())
                .andExpect(jsonPath("$[*].productGender").isNotEmpty())
                .andExpect(jsonPath("$[*].fragranceTopNotes").isNotEmpty())
                .andExpect(jsonPath("$[*].fragranceMiddleNotes").isNotEmpty())
                .andExpect(jsonPath("$[*].fragranceBaseNotes").isNotEmpty())
                .andExpect(jsonPath("$[*].description").isNotEmpty())
                .andExpect(jsonPath("$[*].filename").isNotEmpty())
                .andExpect(jsonPath("$[*].price").isNotEmpty())
                .andExpect(jsonPath("$[*].volume").isNotEmpty())
                .andExpect(jsonPath("$[*].type").isNotEmpty());
    }

    @Test
    public void findByProductGender() throws Exception {
        ProductSearchRequest filter = new ProductSearchRequest();
        filter.setProductGender(PRODUCT_GENDER);

        mockMvc.perform(post(URL_PRODUCTS_SEARCH + "/gender")
                .content(mapper.writeValueAsString(filter))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").isNotEmpty())
                .andExpect(jsonPath("$[*].productTitle").isNotEmpty())
                .andExpect(jsonPath("$[*].productr").isNotEmpty())
                .andExpect(jsonPath("$[*].year").isNotEmpty())
                .andExpect(jsonPath("$[*].country").isNotEmpty())
                .andExpect(jsonPath("$[*].productGender").isNotEmpty())
                .andExpect(jsonPath("$[*].fragranceTopNotes").isNotEmpty())
                .andExpect(jsonPath("$[*].fragranceMiddleNotes").isNotEmpty())
                .andExpect(jsonPath("$[*].fragranceBaseNotes").isNotEmpty())
                .andExpect(jsonPath("$[*].description").isNotEmpty())
                .andExpect(jsonPath("$[*].filename").isNotEmpty())
                .andExpect(jsonPath("$[*].price").isNotEmpty())
                .andExpect(jsonPath("$[*].volume").isNotEmpty())
                .andExpect(jsonPath("$[*].type").isNotEmpty());
    }

    @Test
    public void findByProductr() throws Exception {
        ProductSearchRequest filter = new ProductSearchRequest();
        filter.setProductr(PRODUCTR_CHANEL);

        mockMvc.perform(post(URL_PRODUCTS_SEARCH + "/productr")
                .content(mapper.writeValueAsString(filter))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").isNotEmpty())
                .andExpect(jsonPath("$[*].productTitle").isNotEmpty())
                .andExpect(jsonPath("$[*].productr").isNotEmpty())
                .andExpect(jsonPath("$[*].year").isNotEmpty())
                .andExpect(jsonPath("$[*].country").isNotEmpty())
                .andExpect(jsonPath("$[*].productGender").isNotEmpty())
                .andExpect(jsonPath("$[*].fragranceTopNotes").isNotEmpty())
                .andExpect(jsonPath("$[*].fragranceMiddleNotes").isNotEmpty())
                .andExpect(jsonPath("$[*].fragranceBaseNotes").isNotEmpty())
                .andExpect(jsonPath("$[*].description").isNotEmpty())
                .andExpect(jsonPath("$[*].filename").isNotEmpty())
                .andExpect(jsonPath("$[*].price").isNotEmpty())
                .andExpect(jsonPath("$[*].volume").isNotEmpty())
                .andExpect(jsonPath("$[*].type").isNotEmpty());
    }
    @Test
    public void getProductsByIdsQuery() throws Exception {
        graphQLRequest.setQuery(GRAPHQL_QUERY_PRODUCTS_BY_IDS);

        mockMvc.perform(post(URL_PRODUCTS_GRAPHQL + "/ids")
                .content(mapper.writeValueAsString(graphQLRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.productsIds[*].id").isNotEmpty())
                .andExpect(jsonPath("$.data.productsIds[*].productTitle").isNotEmpty())
                .andExpect(jsonPath("$.data.productsIds[*].productr").isNotEmpty())
                .andExpect(jsonPath("$.data.productsIds[*].price").isNotEmpty());
    }

    @Test
    public void getAllProductsByQuery() throws Exception {
        graphQLRequest.setQuery(GRAPHQL_QUERY_PRODUCTS);

        mockMvc.perform(post(URL_PRODUCTS_GRAPHQL + "/products")
                .content(mapper.writeValueAsString(graphQLRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.products[*].id").isNotEmpty())
                .andExpect(jsonPath("$.data.products[*].productTitle").isNotEmpty())
                .andExpect(jsonPath("$.data.products[*].productr").isNotEmpty())
                .andExpect(jsonPath("$.data.products[*].price").isNotEmpty())
                .andExpect(jsonPath("$.data.products[*].filename").isNotEmpty());
    }

    @Test
    public void getProductByQuery() throws Exception {
        graphQLRequest.setQuery(GRAPHQL_QUERY_PRODUCT);

        mockMvc.perform(post(URL_PRODUCTS_GRAPHQL + "/product")
                .content(mapper.writeValueAsString(graphQLRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.product.id", equalTo(1)))
                .andExpect(jsonPath("$.data.product.productTitle", equalTo("Boss Bottled Night")))
                .andExpect(jsonPath("$.data.product.productr", equalTo("Hugo Boss")))
                .andExpect(jsonPath("$.data.product.price", equalTo(35)));
    }
}
