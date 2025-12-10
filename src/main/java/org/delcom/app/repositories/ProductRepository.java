package org.delcom.app.repositories;

import org.delcom.app.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByUserId(UUID userId);

    @Query("SELECT p.category, SUM(p.stock) FROM Product p WHERE p.userId = ?1 GROUP BY p.category")
    List<Object[]> countStockByCategory(UUID userId);
}