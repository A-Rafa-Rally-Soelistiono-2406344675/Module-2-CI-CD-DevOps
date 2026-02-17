package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void createShouldGenerateProductIdWhenProductIdIsNull() {
        Product product = new Product();
        product.setProductId(null);

        Product result = productService.create(product);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).create(productCaptor.capture());
        assertNotNull(productCaptor.getValue().getProductId());
        assertFalse(productCaptor.getValue().getProductId().isBlank());
        assertSame(product, result);
    }

    @Test
    void createShouldGenerateProductIdWhenProductIdIsBlank() {
        Product product = new Product();
        product.setProductId("   ");

        Product result = productService.create(product);

        verify(productRepository).create(product);
        assertNotNull(product.getProductId());
        assertFalse(product.getProductId().isBlank());
        assertSame(product, result);
    }

    @Test
    void createShouldKeepExistingProductId() {
        Product product = new Product();
        product.setProductId("existing-id");

        Product result = productService.create(product);

        verify(productRepository).create(product);
        assertEquals("existing-id", product.getProductId());
        assertSame(product, result);
    }

    @Test
    void findAllShouldConvertIteratorToList() {
        Product product1 = new Product();
        Product product2 = new Product();
        Iterator<Product> iterator = List.of(product1, product2).iterator();
        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> result = productService.findAll();

        assertEquals(2, result.size());
        assertSame(product1, result.get(0));
        assertSame(product2, result.get(1));
    }

    @Test
    void findByIdShouldDelegateToRepository() {
        Product product = new Product();
        when(productRepository.findById("product-1")).thenReturn(product);

        Product result = productService.findById("product-1");

        verify(productRepository).findById("product-1");
        assertSame(product, result);
    }

    @Test
    void updateShouldDelegateToRepository() {
        Product product = new Product();
        when(productRepository.update(product)).thenReturn(product);

        Product result = productService.update(product);

        verify(productRepository).update(product);
        assertSame(product, result);
    }

    @Test
    void deleteByIdShouldDelegateToRepository() {
        when(productRepository.deleteById("product-1")).thenReturn(true);

        boolean result = productService.deleteById("product-1");

        verify(productRepository).deleteById("product-1");
        assertTrue(result);
    }
}
