package com.customerservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerRegNo;  // Registration Number (like "CUST-XXXX")

    private String name;
    private String email;
    private String contactNumber;
    private String address;

    // 🔹 Auto-generate customerRegNo before saving
    @PrePersist
    public void generateCustomerRegNo() {
        if (this.customerRegNo == null || this.customerRegNo.isBlank()) {
            this.customerRegNo = "CUST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }
}
