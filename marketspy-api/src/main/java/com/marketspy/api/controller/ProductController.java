package com.marketspy.api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketspy.api.model.Product;
import com.marketspy.api.repository.ProductRepository;
import com.marketspy.api.service.ScraperService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ScraperService scraperService;

    @GetMapping
    public List<Product> listAll() {
        return repository.findAll();
    }

    @PostMapping
    public Product saveProduct(@RequestBody Map<String, String> payload) {
        String url = payload.get("url");

        // chama o Python para pegar os dados atuais
        Map<String, Object> data = scraperService.getProductDataFromPython(url);

        // cria e salva o produto no banco H2
        Product product = new Product();
        product.setName((String) data.get("title"));
        product.setUrl(url);
        product.setLastPrice((Double) data.get("price"));
        product.setLastUpdate(LocalDateTime.now());

        return repository.save(product);
    }
}