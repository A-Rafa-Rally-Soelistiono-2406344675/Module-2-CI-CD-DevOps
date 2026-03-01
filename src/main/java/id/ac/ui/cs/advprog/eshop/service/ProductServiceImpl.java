package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductReadRepository;
import id.ac.ui.cs.advprog.eshop.repository.ProductWriteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    //implemented dip: service bergantung pada abstraksi repository, bukan class konkret.
    //implemented isp: service menggunakan kontrak read/write yang spesifik.
    private final ProductReadRepository productReadRepository;
    private final ProductWriteRepository productWriteRepository;

    public ProductServiceImpl(ProductReadRepository productReadRepository,
                              ProductWriteRepository productWriteRepository) {
        this.productReadRepository = productReadRepository;
        this.productWriteRepository = productWriteRepository;
    }

    @Override
    public Product create(Product product) {
        if (product.getProductId() == null || product.getProductId().isBlank()) {
            product.setProductId(UUID.randomUUID().toString());
        }
        productWriteRepository.create(product);
        return product;
    }

    @Override
    public List<Product> findAll() {
        Iterator<Product> productIterator = productReadRepository.findAll();
        List<Product> allProduct = new ArrayList<>();
        productIterator.forEachRemaining(allProduct::add);
        return allProduct;
    }

    @Override
    public Product findById(String productId) {
        return productReadRepository.findById(productId);
    }

    @Override
    public Product update(Product product) {
        return productWriteRepository.update(product);
    }

    @Override
    public boolean deleteById(String productId) {
        return productWriteRepository.deleteById(productId);
    }
}
