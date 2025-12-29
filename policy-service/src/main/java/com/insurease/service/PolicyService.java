package com.insurease.service;

import com.insurease.dto.PolicyUpdateDTO;
import com.insurease.mapper.PolicyMapper;
import com.insurease.model.Policy;
import com.insurease.model.Policy.PolicyStatus;
import com.insurease.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository repo;

    // Create a new policy (used by controller)
    public Policy createPolicy(Policy policy) {
        policy.setStatus(PolicyStatus.SUBMITTED);
        policy.setSubmittedAt(LocalDateTime.now());
        return repo.save(policy);
    }

    // Get all policies
    public List<Policy> getAllPolicies() {
        return repo.findAll();
    }

    // Get policy by ID
    public Policy getPolicyById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
    }

    // Update policy details
    private final PolicyMapper policyMapper;

    public Policy updatePolicy(Long id, PolicyUpdateDTO dto) {

        Policy existing = getPolicyById(id);

        // MapStruct updates only the present fields
        policyMapper.updatePolicyFromDto(dto, existing);

        return repo.save(existing);
    }

    // Delete policy
    public void deletePolicy(Long id) {
        if(!repo.existsById(id)) {
            throw new RuntimeException("Policy with id " + id + " not found");
        }
        repo.deleteById(id);
    }


    public List<Policy> getPoliciesByCustomerId(Long customerId) {
        return repo.findByCustomerId(customerId);
    }

    // Get all policies for a particular customer by registration number
    //public List<Policy> getPoliciesByCustomerRegNo(String regNo) {
    //    return repo.findByCustomerRegNo(regNo);
    //}

}
