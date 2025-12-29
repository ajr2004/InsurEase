package com.insurease.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "policies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String policyNumber;

    private String policyType;

    private String policyHolderName;

    private Double premiumAmount;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private PolicyStatus status;

    private String remarks;

    private LocalDateTime submittedAt;

    private LocalDateTime closedAt;

    // 🔗 Foreign Key-like field — this links policy to a customer
    private Long customerId;

    // ✅ Enum for policy status
    public enum PolicyStatus {
        SUBMITTED,
        APPROVED,
        REJECTED,
        CLOSED
    }
}
