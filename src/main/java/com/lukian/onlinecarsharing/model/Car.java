package com.lukian.onlinecarsharing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@SQLDelete(sql = "UPDATE cars SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted=false")
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "type",
            nullable = false,
            columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private Type type;
    /**
     * inventory - the number of this specific car
     * available for now in the car sharing service
     */
    @Column(name = "inventory", nullable = false)
    private int inventory;

    @Column(name = "daily_fee", nullable = false)
    private BigDecimal dailyFee;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public enum Type {
        SEDAN,
        SUV,
        HATCHBACK,
        UNIVERSAL
    }
}
