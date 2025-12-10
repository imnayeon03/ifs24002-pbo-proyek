package org.delcom.app.services;

import org.delcom.app.entities.Product;
import org.delcom.app.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts(UUID userId) {
        return productRepository.findByUserId(userId);
    }

    public Product getProductById(UUID id, UUID userId) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null && product.getUserId().equals(userId)) {
            return product;
        }
        return null;
    }

    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
    
    public List<Object[]> getChartData(UUID userId) {
        return productRepository.countStockByCategory(userId);
    }
}