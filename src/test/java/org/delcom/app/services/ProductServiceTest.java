package org.delcom.app.services;

import org.delcom.app.entities.Product;
import org.delcom.app.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // ==========================================================
    // 1. TEST GET ALL PRODUCTS (Menutup Baris 18-20)
    // ==========================================================
    @Test
    void getAllProducts_ShouldReturnListFromRepository() {
        UUID userId = UUID.randomUUID();
        List<Product> mockList = Arrays.asList(new Product(), new Product());
        
        when(productRepository.findByUserId(userId)).thenReturn(mockList);

        List<Product> result = productService.getAllProducts(userId);

        assertEquals(2, result.size());
        verify(productRepository).findByUserId(userId);
    }

    // ==========================================================
    // 2. TEST GET PRODUCT BY ID (Logika If-Else Baris 22-28)
    // ==========================================================
    
    // Case 1: Produk Ada & User ID Cocok (Happy Path)
    @Test
    void getProductById_WhenFoundAndUserMatches_ReturnsProduct() {
        UUID pid = UUID.randomUUID();
        UUID uid = UUID.randomUUID();
        Product p = new Product();
        p.setId(pid);
        p.setUserId(uid);

        when(productRepository.findById(pid)).thenReturn(Optional.of(p));

        Product result = productService.getProductById(pid, uid);

        assertNotNull(result);
        assertEquals(pid, result.getId());
    }

    // Case 2: Produk Ada TAPI User ID Beda (Unhappy Path)
    @Test
    void getProductById_WhenFoundButUserMismatch_ReturnsNull() {
        UUID pid = UUID.randomUUID();
        UUID uid = UUID.randomUUID();
        UUID otherUid = UUID.randomUUID();
        
        Product p = new Product();
        p.setId(pid);
        p.setUserId(otherUid); // ID User beda

        when(productRepository.findById(pid)).thenReturn(Optional.of(p));

        Product result = productService.getProductById(pid, uid);

        assertNull(result); // Harus null karena user tidak berhak
    }

    // Case 3: Produk Tidak Ditemukan (Null Path)
    @Test
    void getProductById_WhenNotFound_ReturnsNull() {
        UUID pid = UUID.randomUUID();
        UUID uid = UUID.randomUUID();

        when(productRepository.findById(pid)).thenReturn(Optional.empty());

        Product result = productService.getProductById(pid, uid);

        assertNull(result);
    }

    // ==========================================================
    // 3. TEST SAVE PRODUCT (Menutup Baris 31-33)
    // ==========================================================
    @Test
    void saveProduct_ShouldCallRepositorySave() {
        Product p = new Product();
        p.setName("Test Save");
        
        when(productRepository.save(p)).thenReturn(p);

        Product result = productService.saveProduct(p);

        assertEquals("Test Save", result.getName());
        verify(productRepository).save(p);
    }

    // ==========================================================
    // 4. TEST DELETE PRODUCT (Menutup Baris 36-38)
    // ==========================================================
    @Test
    void deleteProduct_ShouldCallRepositoryDelete() {
        UUID pid = UUID.randomUUID();
        
        productService.deleteProduct(pid);

        verify(productRepository).deleteById(pid);
    }

    // ==========================================================
    // 5. TEST GET CHART DATA (Menutup Baris 40-42)
    // ==========================================================
    @Test
    void getChartData_ShouldCallRepositoryCount() {
        UUID userId = UUID.randomUUID();
        List<Object[]> mockData = Arrays.asList(
            new Object[]{"Electronics", 10L},
            new Object[]{"Clothes", 5L}
        );

        when(productRepository.countStockByCategory(userId)).thenReturn(mockData);

        List<Object[]> result = productService.getChartData(userId);

        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0)[0]);
        verify(productRepository).countStockByCategory(userId);
    }
}