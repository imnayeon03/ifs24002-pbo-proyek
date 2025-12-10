package org.delcom.app.views;

import org.delcom.app.entities.Product;
import org.delcom.app.entities.User;
import org.delcom.app.services.ProductService;
import org.delcom.app.services.UserService;
import org.delcom.app.utils.ConstUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
public class HomeView {

    private final UserService userService;
    private final ProductService productService;

    public HomeView(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // 1. Ambil Authentication dari Context Holder
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = null;

        // Cek apakah principal yang login adalah tipe User kita
        if (auth != null && auth.getPrincipal() instanceof User) {
            // Casting langsung dari sesi
            user = (User) auth.getPrincipal();

            // Refresh data user dari DB untuk memastikan data terbaru
            user = userService.getUserById(user.getId());
        }

        // Kirim object user ke template (bisa null jika belum login)
        model.addAttribute("user", user);

        if (user != null) {
            // 2. Ambil semua produk milik user
            List<Product> products = productService.getAllProducts(user.getId());

            // Jaga-jaga jika products null
            if (products == null) {
                products = Collections.emptyList();
            }

            // 3. Hitung Statistik Toko
            int totalProducts = products.size();

            // Menghitung jumlah kategori unik
            long totalCategories = products.stream()
                    .map(Product::getCategory)
                    .filter(Objects::nonNull) // Hindari null category
                    .distinct()
                    .count();

            // Menghitung Total Nilai Aset (Harga * Stok)
            // Menggunakan Double karena di Entity Product kita pakai Double & Integer
            Double totalAssetValue = products.stream()
                    .filter(p -> p.getPrice() != null && p.getStock() != null)
                    .mapToDouble(p -> p.getPrice() * p.getStock())
                    .sum();

            // 4. Masukkan ke Model
            model.addAttribute("totalProducts", totalProducts);
            model.addAttribute("totalCategories", totalCategories);
            model.addAttribute("totalAssetValue", totalAssetValue);
        } else {
            // Default value jika user belum login
            model.addAttribute("totalProducts", 0);
            model.addAttribute("totalCategories", 0);
            model.addAttribute("totalAssetValue", 0.0);
        }

        return ConstUtil.TEMPLATE_PAGES_HOME;
    }
}