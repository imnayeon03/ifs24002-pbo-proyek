package org.delcom.app.configs;

import org.delcom.app.interceptors.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry; // Pastikan import ini
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Path; // Tambahan Import
import java.nio.file.Paths; // Tambahan Import

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/**")
                .excludePathPatterns("/api/public/**");
    }

    // --- BAGIAN INI YANG DIPERBAIKI ---
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mengambil path folder 'uploads' secara absolut (lengkap)
        // Ini memastikan server tidak bingung mencari lokasinya
        Path uploadDir = Paths.get("./uploads");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}