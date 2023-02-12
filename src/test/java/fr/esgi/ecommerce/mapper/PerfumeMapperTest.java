package fr.esgi.ecommerce.mapper;

import fr.esgi.ecommerce.domain.Product;
import fr.esgi.ecommerce.dto.product.ProductRequest;
import fr.esgi.ecommerce.dto.product.ProductResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static fr.esgi.ecommerce.util.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductMapperTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void convertToEntity() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductr(PRODUCTR_CHANEL);
        productRequest.setProductTitle(PRODUCT_TITLE);
        productRequest.setYear(YEAR);
        productRequest.setCountry(COUNTRY);
        productRequest.setProductGender(PRODUCT_GENDER);
        productRequest.setFragranceTopNotes(FRAGRANCE_TOP_NOTES);
        productRequest.setFragranceMiddleNotes(FRAGRANCE_MIDDLE_NOTES);
        productRequest.setFragranceBaseNotes(FRAGRANCE_BASE_NOTES);
        productRequest.setPrice(PRICE);
        productRequest.setVolume(VOLUME);
        productRequest.setType(TYPE);

        Product product = modelMapper.map(productRequest, Product.class);
        assertEquals(productRequest.getProductr(), product.getProductr());
        assertEquals(productRequest.getProductTitle(), product.getProductTitle());
        assertEquals(productRequest.getYear(), product.getYear());
        assertEquals(productRequest.getCountry(), product.getCountry());
        assertEquals(productRequest.getProductGender(), product.getProductGender());
        assertEquals(productRequest.getFragranceTopNotes(), product.getFragranceTopNotes());
        assertEquals(productRequest.getFragranceMiddleNotes(), product.getFragranceMiddleNotes());
        assertEquals(productRequest.getFragranceBaseNotes(), product.getFragranceBaseNotes());
        assertEquals(productRequest.getPrice(), product.getPrice());
        assertEquals(productRequest.getVolume(), product.getVolume());
        assertEquals(productRequest.getType(), product.getType());
    }

    @Test
    public void convertToResponseDto() {
        Product product = new Product();
        product.setId(1L);
        product.setProductr(PRODUCTR_CHANEL);
        product.setProductTitle(PRODUCT_TITLE);
        product.setYear(YEAR);
        product.setCountry(COUNTRY);
        product.setProductGender(PRODUCT_GENDER);
        product.setFragranceTopNotes(FRAGRANCE_TOP_NOTES);
        product.setFragranceMiddleNotes(FRAGRANCE_MIDDLE_NOTES);
        product.setFragranceBaseNotes(FRAGRANCE_BASE_NOTES);
        product.setPrice(PRICE);
        product.setVolume(VOLUME);
        product.setType(TYPE);

        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        assertEquals(product.getId(), productResponse.getId());
        assertEquals(product.getProductr(), productResponse.getProductr());
        assertEquals(product.getProductTitle(), productResponse.getProductTitle());
        assertEquals(product.getYear(), productResponse.getYear());
        assertEquals(product.getCountry(), productResponse.getCountry());
        assertEquals(product.getProductGender(), productResponse.getProductGender());
        assertEquals(product.getFragranceTopNotes(), productResponse.getFragranceTopNotes());
        assertEquals(product.getFragranceMiddleNotes(), productResponse.getFragranceMiddleNotes());
        assertEquals(product.getFragranceBaseNotes(), productResponse.getFragranceBaseNotes());
        assertEquals(product.getPrice(), productResponse.getPrice());
        assertEquals(product.getVolume(), productResponse.getVolume());
        assertEquals(product.getType(), productResponse.getType());
    }
}
