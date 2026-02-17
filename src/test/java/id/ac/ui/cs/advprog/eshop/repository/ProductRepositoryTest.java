package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setup() {
        // no setup needed
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb5589f1-c39-460e-8860-71afa6f36db");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());

        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb5589f1-c39-460e-8860-71afa6f36db");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("e0f9e64c-90b1-437d-ab0f-d0821dde9096");
        product2.setProductName("Sampo Cap Udep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());

        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());

        assertTrue(productIterator.hasNext());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());

        assertFalse(productIterator.hasNext());
    }

    @Test
    void testUpdateExistingProduct() {
        Product product = new Product();
        product.setProductId("eb5589f1-c39-460e-8860-71afa6f36db");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId(product.getProductId());
        updatedProduct.setProductName("Sampo Cap Super");
        updatedProduct.setProductQuantity(250);

        Product result = productRepository.update(updatedProduct);
        assertNotNull(result);
        assertEquals(updatedProduct.getProductName(), result.getProductName());
        assertEquals(updatedProduct.getProductQuantity(), result.getProductQuantity());
    }

    @Test
    void testUpdateNonExistingProduct() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId("not-found");
        updatedProduct.setProductName("Sampo Cap Super");
        updatedProduct.setProductQuantity(250);

        Product result = productRepository.update(updatedProduct);
        assertNull(result);
    }

    @Test
    void testDeleteExistingProduct() {
        Product product = new Product();
        product.setProductId("eb5589f1-c39-460e-8860-71afa6f36db");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        boolean deleted = productRepository.deleteById(product.getProductId());
        assertTrue(deleted);
        assertFalse(productRepository.findAll().hasNext());
    }

    @Test
    void testDeleteNonExistingProduct() {
        boolean deleted = productRepository.deleteById("not-found");
        assertFalse(deleted);
    }

    @Test
    void testUpdateIgnoresStoredProductWithNullId() {
        Product product = new Product();
        product.setProductId(null);
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("eb5589f1-c39-460e-8860-71afa6f36db");
        updatedProduct.setProductName("Sampo Cap Super");
        updatedProduct.setProductQuantity(250);

        Product result = productRepository.update(updatedProduct);
        assertNull(result);
    }

    @Test
    void testDeleteIgnoresStoredProductWithNullId() {
        Product product = new Product();
        product.setProductId(null);
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        boolean deleted = productRepository.deleteById("eb5589f1-c39-460e-8860-71afa6f36db");
        assertFalse(deleted);
        assertTrue(productRepository.findAll().hasNext());
    }
}
