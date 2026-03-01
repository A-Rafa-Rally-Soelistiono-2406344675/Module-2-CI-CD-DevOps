package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;

//implemented isp: operasi tulis dipisahkan untuk klien yang hanya memodifikasi data.
public interface ProductWriteRepository {
    Product create(Product product);

    Product update(Product product);

    boolean deleteById(String productId);
}
