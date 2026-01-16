package com.marketspy.api.config;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.marketspy.api.model.Product;
import com.marketspy.api.repository.ProductRepository;
import com.marketspy.api.service.ScraperService;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner loadInitialData(ProductRepository repository, ScraperService scraperService) {
        return args -> {
            if (repository.count() == 0) {
                System.out.println("Banco de dados vazio. Iniciando Seed (População automática)...");

                String urlPs5 = "https://www.mercadolivre.com.br/sony-playstation-5-slim-1tb-cor-branco/p/MLB28616777";

                try {
                    System.out.println("Consultando API Python para dados iniciais...");
                    Map<String, Object> data = scraperService.getProductDataFromPython(urlPs5);

                    // cria o objeto
                    Product product = new Product();
                    product.setName((String) data.get("title"));
                    product.setUrl(urlPs5);
                    product.setLastPrice((Double) data.get("price"));
                    product.setLastUpdate(LocalDateTime.now());

                    // salva no banco
                    repository.save(product);
                    
                    System.out.println("Produto Inicial (PS5) cadastrado com sucesso!");

                } catch (Exception e) {
                    System.err.println("Não foi possível cadastrar o produto inicial. A API Python está rodando?");
                    System.err.println("Erro: " + e.getMessage());
                }
            } else {
                System.out.println("O banco de dados já possui registros.");
            }
        };
    }
}