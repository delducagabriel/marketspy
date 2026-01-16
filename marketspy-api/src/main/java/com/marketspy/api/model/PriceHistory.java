package com.marketspy.api.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class PriceHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;
    private LocalDateTime date;

    // relacionamento: muitos históricos pertem a um produto
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore // evita loop infinito ao gerar JSON (produto -> histórico -> produto -> ...)
    private Product product;
}


