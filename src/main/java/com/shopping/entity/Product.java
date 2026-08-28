package com.shopping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int price;
    private String description;
    private String imageUrl;

    @Transient   // DB 컬럼으로 만들지 않음
    public String getFormattedPrice() {
        return String.format("%,d", this.price);
    }
}
