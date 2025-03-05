package com.market.service;

import com.market.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.market.repository.CategoryRepository;
import com.market.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RestTemplate restTemplate;

    @Value("${fixer.url}")
    private String fixerUrl;

    @Value("${fixer.apiKey}")
    private String fixerApiKey;

    public Product createProduct(Product product) {
        // Якщо currency != "EUR", то конвертуємо price у EUR
        if (!"EUR".equalsIgnoreCase(product.getCurrency())) {
            BigDecimal rate = getConversionRate(product.getCurrency(), "EUR");
            BigDecimal priceInEur = product.getPrice().multiply(rate);
            product.setPrice(priceInEur);
            product.setCurrency("EUR");
        }
        return productRepository.save(product);
    }

    private BigDecimal getConversionRate(String from, String to) {
        // Приклад виклику Fixer.io (необхідно вказати заголовок з API-KEY)
        // URL може виглядати так: https://api.apilayer.com/fixer/latest?base=FROM&symbols=TO
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", fixerApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = fixerUrl + "?base=" + from + "&symbols=" + to;
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("rates")) {
            throw new RuntimeException("Can't get exchange rate from Fixer.io");
        }
        Map<String, Double> rates = (Map<String, Double>) body.get("rates");
        Double rate = rates.get(to);
        return BigDecimal.valueOf(rate);
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, Product updated) {
        Product existing = getProduct(id);
        existing.setName(updated.getName());
        // Аналогічно оновлюємо price/currency з конвертацією
        // ...
        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
