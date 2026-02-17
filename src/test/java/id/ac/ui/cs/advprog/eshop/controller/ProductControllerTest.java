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
        verify(model).addAttribute(eq("product"), productCaptor.capture());
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
        when(productService.findById("missing-id")).thenReturn(null);

        String viewName = productController.editProductPage("missing-id", model);

        verify(productService).findById("missing-id");
        verify(model, never()).addAttribute(eq("product"), any());
        assertEquals("redirect:/product/list", viewName);
    }

    @Test
    void editProductPageShouldSetProductWhenFound() {
        Product product = new Product();
        product.setProductId("product-1");
        when(productService.findById("product-1")).thenReturn(product);

        String viewName = productController.editProductPage("product-1", model);

        verify(model).addAttribute("product", product);
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
        String viewName = productController.deleteProduct("product-1");

        verify(productService).deleteById("product-1");
        assertEquals("redirect:/product/list", viewName);
    }
}
