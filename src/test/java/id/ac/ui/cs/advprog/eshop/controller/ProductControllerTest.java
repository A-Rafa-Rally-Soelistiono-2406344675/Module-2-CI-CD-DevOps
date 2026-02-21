package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    private static final String PRODUCT_KEY = "product";
    private static final String PRODUCT_ID = "product-1";
    private static final String MISSING_ID = "missing-id";

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @Test
    void createProductPageShouldReturnCreateProductViewAndSetModel() {
        String viewName = productController.createProductPage(model);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(model).addAttribute(eq(PRODUCT_KEY), productCaptor.capture());
        assertNotNull(productCaptor.getValue());
        assertEquals("createProduct", viewName);
    }

    @Test
    void createProductPostShouldCreateProductAndRedirectToList() {
        Product product = new Product();

        String viewName = productController.createProductPost(product, model);

        verify(productService).create(product);
        assertEquals("redirect:list", viewName);
    }

    @Test
    void productListPageShouldSetProductsInModelAndReturnProductListView() {
        List<Product> products = List.of(new Product(), new Product());
        when(productService.findAll()).thenReturn(products);

        String viewName = productController.productListPage(model);

        verify(model).addAttribute("products", products);
        assertEquals("productList", viewName);
    }

    @Test
    void editProductPageShouldRedirectWhenProductNotFound() {
        when(productService.findById(MISSING_ID)).thenReturn(null);

        String viewName = productController.editProductPage(MISSING_ID, model);

        verify(productService).findById(MISSING_ID);
        verify(model, never()).addAttribute(eq(PRODUCT_KEY), any());
        assertEquals("redirect:/product/list", viewName);
    }

    @Test
    void editProductPageShouldSetProductWhenFound() {
        Product product = new Product();
        product.setProductId(PRODUCT_ID);
        when(productService.findById(PRODUCT_ID)).thenReturn(product);

        String viewName = productController.editProductPage(PRODUCT_ID, model);

        verify(model).addAttribute(PRODUCT_KEY, product);
        assertEquals("editProduct", viewName);
    }

    @Test
    void editProductPostShouldUpdateProductAndRedirectToList() {
        Product product = new Product();

        String viewName = productController.editProductPost(product);

        verify(productService).update(product);
        assertEquals("redirect:list", viewName);
    }

    @Test
    void deleteProductShouldDeleteByIdAndRedirectToList() {
        String viewName = productController.deleteProduct(PRODUCT_ID);

        verify(productService).deleteById(PRODUCT_ID);
        assertEquals("redirect:/product/list", viewName);
    }
}
