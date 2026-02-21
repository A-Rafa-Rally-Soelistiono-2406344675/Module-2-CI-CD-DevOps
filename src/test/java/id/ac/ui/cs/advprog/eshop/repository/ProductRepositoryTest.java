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
    private static final String PRODUCT_ID_MAIN = "eb5589f1-c39-460e-8860-71afa6f36db";
    private static final String PRODUCT_ID_SECOND = "e0f9e64c-90b1-437d-ab0f-d0821dde9096";
    private static final String PRODUCT_ID_GENERIC = "product-1";
    private static final String EXISTING_ID = "existing-id";
    private static final String MISSING_ID = "missing-id";
    private static final String NOT_FOUND_ID = "not-found";
    private static final String PRODUCT_NAME_MAIN = "Sampo Cap Bambang";
    private static final String PRODUCT_NAME_SECOND = "Sampo Cap Udep";
    private static final String PRODUCT_NAME_UPDATED = "Sampo Cap Super";
    private static final int DEFAULT_QUANTITY = 100;
    private static final int SECOND_QUANTITY = 50;
    private static final int UPDATED_QUANTITY = 250;

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setup() {
        // no setup needed
    }

    @Test
    void testCreateAndFind() {
        Product product = createProduct(PRODUCT_ID_MAIN, PRODUCT_NAME_MAIN, DEFAULT_QUANTITY);

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
        Product product1 = createProduct(PRODUCT_ID_MAIN, PRODUCT_NAME_MAIN, DEFAULT_QUANTITY);
        productRepository.create(product1);

        Product product2 = createProduct(PRODUCT_ID_SECOND, PRODUCT_NAME_SECOND, SECOND_QUANTITY);
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
        Product product = createProduct(PRODUCT_ID_MAIN, PRODUCT_NAME_MAIN, DEFAULT_QUANTITY);
        productRepository.create(product);

        Product updatedProduct = createProduct(product.getProductId(), PRODUCT_NAME_UPDATED, UPDATED_QUANTITY);

        Product result = productRepository.update(updatedProduct);
        assertNotNull(result);
        assertEquals(updatedProduct.getProductName(), result.getProductName());
        assertEquals(updatedProduct.getProductQuantity(), result.getProductQuantity());
    }

    @Test
    void testUpdateNonExistingProduct() {
        Product existingProduct = createProduct(EXISTING_ID, PRODUCT_NAME_MAIN, DEFAULT_QUANTITY);
        productRepository.create(existingProduct);

        Product updatedProduct = createProduct(NOT_FOUND_ID, PRODUCT_NAME_UPDATED, UPDATED_QUANTITY);

        Product result = productRepository.update(updatedProduct);
        assertNull(result);
    }

    @Test
    void testDeleteExistingProduct() {
        Product product = createProduct(PRODUCT_ID_MAIN, PRODUCT_NAME_MAIN, DEFAULT_QUANTITY);
        productRepository.create(product);

        boolean deleted = productRepository.deleteById(product.getProductId());
        assertTrue(deleted);
        assertFalse(productRepository.findAll().hasNext());
    }

    @Test
    void testDeleteNonExistingProduct() {
        Product existingProduct = createProduct(EXISTING_ID, PRODUCT_NAME_MAIN, DEFAULT_QUANTITY);
        productRepository.create(existingProduct);

        boolean deleted = productRepository.deleteById(NOT_FOUND_ID);
        assertFalse(deleted);
    }

    @Test
    void testUpdateIgnoresStoredProductWithNullId() {
        Product product = createProduct(null, PRODUCT_NAME_MAIN, DEFAULT_QUANTITY);
        productRepository.create(product);

        Product updatedProduct = createProduct(PRODUCT_ID_MAIN, PRODUCT_NAME_UPDATED, UPDATED_QUANTITY);

        Product result = productRepository.update(updatedProduct);
        assertNull(result);
    }

    @Test
    void testFindByIdWhenProductExists() {
        Product product = createProduct(PRODUCT_ID_GENERIC, PRODUCT_NAME_MAIN, DEFAULT_QUANTITY);
        productRepository.create(product);

        Product result = productRepository.findById(PRODUCT_ID_GENERIC);
        assertNotNull(result);
        assertEquals(PRODUCT_ID_GENERIC, result.getProductId());
    }

    @Test
    void testFindByIdReturnsNullWhenStoredProductIdIsNull() {
        Product product = createProduct(null, PRODUCT_NAME_MAIN, DEFAULT_QUANTITY);
        productRepository.create(product);

        Product result = productRepository.findById(PRODUCT_ID_GENERIC);
        assertNull(result);
    }

    @Test
    void testFindByIdReturnsNullWhenProductNotFound() {
        Product product = createProduct(PRODUCT_ID_GENERIC, PRODUCT_NAME_MAIN, DEFAULT_QUANTITY);
        productRepository.create(product);

        Product result = productRepository.findById(MISSING_ID);
        assertNull(result);
    }

    @Test
    void testDeleteIgnoresStoredProductWithNullId() {
        Product product = createProduct(null, PRODUCT_NAME_MAIN, DEFAULT_QUANTITY);
        productRepository.create(product);

        boolean deleted = productRepository.deleteById(PRODUCT_ID_MAIN);
        assertFalse(deleted);
        assertTrue(productRepository.findAll().hasNext());
    }

    private Product createProduct(String id, String name, int quantity) {
        Product product = new Product();
        product.setProductId(id);
        product.setProductName(name);
        product.setProductQuantity(quantity);
        return product;
    }
}
