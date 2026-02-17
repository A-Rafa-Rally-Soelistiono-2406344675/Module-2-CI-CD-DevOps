package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product findById(String productId) {
        for (Product product : productData) {
            if (product.getProductId() != null && product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    public Product update(Product product) {
        for (Product storedProduct : productData) {
            if (storedProduct.getProductId() != null
                    && storedProduct.getProductId().equals(product.getProductId())) {
                storedProduct.setProductName(product.getProductName());
                storedProduct.setProductQuantity(product.getProductQuantity());
                return storedProduct;
            }
        }
        return null;
    }

    public boolean deleteById(String productId) {
        Iterator<Product> iterator = productData.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getProductId() != null && product.getProductId().equals(productId)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}
