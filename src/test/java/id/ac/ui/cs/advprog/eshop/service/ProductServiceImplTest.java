package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductReadRepository;
import id.ac.ui.cs.advprog.eshop.repository.ProductWriteRepository;
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
    private static final String PRODUCT_ID = "product-1";

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductReadRepository productReadRepository;

    @Mock
    private ProductWriteRepository productWriteRepository;

    @Test
    void createShouldGenerateProductIdWhenProductIdIsNull() {
        Product product = new Product();
        product.setProductId(null);

        Product result = productService.create(product);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productWriteRepository).create(productCaptor.capture());
        assertNotNull(productCaptor.getValue().getProductId());
        assertFalse(productCaptor.getValue().getProductId().isBlank());
        assertSame(product, result);
    }

    @Test
    void createShouldGenerateProductIdWhenProductIdIsBlank() {
        Product product = new Product();
        product.setProductId("   ");

        Product result = productService.create(product);

        verify(productWriteRepository).create(product);
        assertNotNull(product.getProductId());
        assertFalse(product.getProductId().isBlank());
        assertSame(product, result);
    }

    @Test
    void createShouldKeepExistingProductId() {
        Product product = new Product();
        product.setProductId("existing-id");

        Product result = productService.create(product);

        verify(productWriteRepository).create(product);
        assertEquals("existing-id", product.getProductId());
        assertSame(product, result);
    }

    @Test
    void findAllShouldConvertIteratorToList() {
        Product product1 = new Product();
        Product product2 = new Product();
        Iterator<Product> iterator = List.of(product1, product2).iterator();
        when(productReadRepository.findAll()).thenReturn(iterator);

        List<Product> result = productService.findAll();

        assertEquals(2, result.size());
        assertSame(product1, result.get(0));
        assertSame(product2, result.get(1));
    }

    @Test
    void findByIdShouldDelegateToRepository() {
        Product product = new Product();
        when(productReadRepository.findById(PRODUCT_ID)).thenReturn(product);

        Product result = productService.findById(PRODUCT_ID);

        verify(productReadRepository).findById(PRODUCT_ID);
        assertSame(product, result);
    }

    @Test
    void updateShouldDelegateToRepository() {
        Product product = new Product();
        when(productWriteRepository.update(product)).thenReturn(product);

        Product result = productService.update(product);

        verify(productWriteRepository).update(product);
        assertSame(product, result);
    }

    @Test
    void deleteByIdShouldDelegateToRepository() {
        when(productWriteRepository.deleteById(PRODUCT_ID)).thenReturn(true);

        boolean result = productService.deleteById(PRODUCT_ID);

        verify(productWriteRepository).deleteById(PRODUCT_ID);
        assertTrue(result);
    }
}
