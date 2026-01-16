package com.marketspy.api.scheduler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.marketspy.api.model.PriceHistory;
import com.marketspy.api.model.Product;
import com.marketspy.api.repository.ProductRepository;
import com.marketspy.api.service.ScraperService;

@Component
public class ProductScheduler {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ScraperService scraperService;

    // roda a cada 1 minuto (60000 ms) - fins de teste 
    // na prática, poderia ser a cada hora ou mais
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updatePrices() {
        System.out.println("Iniciando verificação agendada de preços...");
        
        List<Product> products = repository.findAll();

        for (Product product : products) {
            try {
                System.out.println("Verificando: " + product.getName());
                
                // chama o Python
                Map<String, Object> data = scraperService.getProductDataFromPython(product.getUrl());
                Double currentPrice = (Double) data.get("price");

                System.out.println("Preço atual: " + currentPrice + " | Último preço: " + product.getLastPrice());

                // cria registro no histórico
                PriceHistory history = new PriceHistory();
                history.setPrice(currentPrice);
                history.setDate(LocalDateTime.now());
                history.setProduct(product);

                // atualiza o produto principal
                product.setLastPrice(currentPrice);
                product.setLastUpdate(LocalDateTime.now());
                product.getHistory().add(history); // adiciona na lista

                repository.save(product); // salva tudo (produto + histórico novo)
                
            } catch (Exception e) {
                System.err.println("Erro ao atualizar " + product.getName() + ": " + e.getMessage());
            }
        }
    }
}