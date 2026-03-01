package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;

import java.util.Iterator;

//implemented isp: operasi baca dipisahkan dari operasi tulis.
public interface ProductReadRepository {
    Iterator<Product> findAll();

    Product findById(String productId);
}
