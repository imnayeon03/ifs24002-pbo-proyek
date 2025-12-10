package org.delcom.app.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void onCreate_ShouldSetCreatedAndUpdatedTime() {
        Product product = new Product();
        
        // Sebelum persist, null
        assertNull(product.getCreatedAt());
        assertNull(product.getUpdatedAt());

        // Simulasi JPA memanggil @PrePersist
        product.onCreate();

        // Branch logic: createdAt = now, updatedAt = now
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
        // Memastikan keduanya di-set (mungkin selisih milidetik sangat kecil, bisa dianggap sama)
        assertEquals(product.getCreatedAt(), product.getUpdatedAt());
    }

    @Test
    void onUpdate_ShouldUpdateOnlyUpdatedTime() throws InterruptedException {
        Product product = new Product();
        product.onCreate(); // Set awal
        LocalDateTime oldCreatedAt = product.getCreatedAt();
        LocalDateTime oldUpdatedAt = product.getUpdatedAt();

        // Tunggu sebentar agar waktu berubah (untuk test)
        Thread.sleep(10); 

        // Simulasi JPA memanggil @PreUpdate
        product.onUpdate();

        // Branch logic: updatedAt berubah, createdAt TETAP
        assertEquals(oldCreatedAt, product.getCreatedAt()); 
        assertNotEquals(oldUpdatedAt, product.getUpdatedAt());
        assertTrue(product.getUpdatedAt().isAfter(oldCreatedAt));
    }
    
    @Test
    void testGettersSetters() {
        // Validasi data passing biasa
        Product product = new Product();
        product.setName("Test Item");
        assertEquals("Test Item", product.getName());
    }
}