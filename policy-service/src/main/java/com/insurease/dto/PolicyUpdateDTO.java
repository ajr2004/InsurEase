package com.insurease.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PolicyUpdateDTO {
    private String policyNumber;
    private String policyType;
    private String policyHolderName;
    private Double premiumAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String remarks;
}
