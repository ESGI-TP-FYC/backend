package fr.esgi.ecommerce.service.Impl;

import fr.esgi.ecommerce.domain.Product;
import fr.esgi.ecommerce.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static fr.esgi.ecommerce.util.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void findProductById() {
        Product product = new Product();
        product.setId(123L);

        when(productRepository.findById(123L)).thenReturn(java.util.Optional.of(product));
        productService.findProductById(123L);
        assertEquals(123L, product.getId());
        assertNotEquals(1L, product.getId());
        verify(productRepository, times(1)).findById(123L);
    }

    @Test
    public void findAllProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product());
        productList.add(new Product());

        when(productRepository.findAllByOrderByIdAsc()).thenReturn(productList);
        productService.findAllProducts();
        assertEquals(2, productList.size());
        verify(productRepository, times(1)).findAllByOrderByIdAsc();
    }

    @Test
    public void filter() {
        Product productChanel = new Product();
        productChanel.setProductr(PRODUCTR_CHANEL);
        productChanel.setProductGender(PRODUCT_GENDER);
        productChanel.setPrice(101);
        Product productCreed = new Product();
        productCreed.setProductr(PRODUCTR_CREED);
        productCreed.setProductGender(PRODUCT_GENDER);
        productCreed.setPrice(102);

        List<Product> productList = new ArrayList<>();
        productList.add(productChanel);
        productList.add(productCreed);

        List<String> productrs = new ArrayList<>();
        productrs.add(PRODUCTR_CHANEL);
        productrs.add(PRODUCTR_CREED);

        List<String> genders = new ArrayList<>();
        genders.add(PRODUCT_GENDER);

        when(productRepository.findByProductrIn(productrs)).thenReturn(productList);
        productService.filter(productrs, new ArrayList<>(), new ArrayList<>(), false);
        assertEquals(2, productList.size());
        assertEquals(productList.get(0).getProductr(), PRODUCTR_CHANEL);
        verify(productRepository, times(1)).findByProductrIn(productrs);

        when(productRepository.findByProductGenderIn(genders)).thenReturn(productList);
        productService.filter(new ArrayList<>(), genders, new ArrayList<>(), false);
        assertEquals(2, productList.size());
        verify(productRepository, times(1)).findByProductGenderIn(genders);
    }

    @Test
    public void findByProductrOrderByPriceDesc() {
        Product productChanel = new Product();
        productChanel.setProductr(PRODUCTR_CHANEL);
        Product productCreed = new Product();
        productCreed.setProductr(PRODUCTR_CREED);
        List<Product> productList = new ArrayList<>();
        productList.add(productChanel);
        productList.add(productCreed);

        when(productRepository.findByProductrOrderByPriceDesc(PRODUCTR_CHANEL)).thenReturn(productList);
        productService.findByProductrOrderByPriceDesc(PRODUCTR_CHANEL);
        assertEquals(productList.get(0).getProductr(), PRODUCTR_CHANEL);
        assertNotEquals(productList.get(0).getProductr(), PRODUCTR_CREED);
        verify(productRepository, times(1)).findByProductrOrderByPriceDesc(PRODUCTR_CHANEL);
    }

    @Test
    public void findByProductGenderOrderByPriceDesc() {
        Product productChanel = new Product();
        productChanel.setProductGender(PRODUCT_GENDER);
        List<Product> productList = new ArrayList<>();
        productList.add(productChanel);

        when(productRepository.findByProductGenderOrderByPriceDesc(PRODUCT_GENDER)).thenReturn(productList);
        productService.findByProductGenderOrderByPriceDesc(PRODUCT_GENDER);
        assertEquals(productList.get(0).getProductGender(), PRODUCT_GENDER);
        assertNotEquals(productList.get(0).getProductGender(), "male");
        verify(productRepository, times(1)).findByProductGenderOrderByPriceDesc(PRODUCT_GENDER);
    }

    @Test
    public void saveProduct() {
        MultipartFile multipartFile = new MockMultipartFile(FILE_NAME, FILE_NAME, "multipart/form-data", FILE_PATH.getBytes());
        Product product = new Product();
        product.setId(1L);
        product.setProductr(PRODUCTR_CHANEL);
        product.setFilename(multipartFile.getOriginalFilename());

        when(productRepository.save(product)).thenReturn(product);
        productService.saveProduct(product, multipartFile);
        verify(productRepository, times(1)).save(product);
    }
}
