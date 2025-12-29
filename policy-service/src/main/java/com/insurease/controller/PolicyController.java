package com.insurease.controller;

import com.insurease.client.CustomerClient;
import com.insurease.dto.PolicyUpdateDTO;
import com.insurease.service.CustomerCacheService;
import com.insurease.model.Policy;
import com.insurease.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PolicyController {

    private final PolicyService service;
    private final CustomerCacheService customerCacheService;
    private final CustomerClient customerClient;


    @PostMapping("/customer/{customerRegNo}")
    public ResponseEntity<Map<String, Object>> createPolicyForCustomer(
            @PathVariable("customerRegNo") String customerRegNo, // Added explicit name
            @RequestBody Policy policy) {

        // 1. Fetch customer details
        Map<String, Object> customer = customerCacheService.getCustomerByRegNo(customerRegNo);

        // 2. Extract Customer ID (The Missing Step!)
        // We must convert the ID from the map to a Long and set it on the policy
        Long customerId = Long.valueOf(customer.get("id").toString());
        policy.setCustomerId(customerId);

        // 3. Create new policy (Now it has the ID!)
        Policy createdPolicy = service.createPolicy(policy);

        // 4. Return response
        return ResponseEntity.ok(Map.of(
                "customer", customer,
                "policy", createdPolicy
        ));
    }
    // @PostMapping("/customer/{customerRegNo}")
    // public ResponseEntity<Map<String, Object>> createPolicyForCustomer(
    //         @PathVariable String customerRegNo,
    //         @RequestBody Policy policy) {

    //     // Fetch customer details from CustomerService using Feign
    //     Map<String, Object> customer = customerCacheService.getCustomerByRegNo(customerRegNo);

    //     // Create new policy
    //     Policy createdPolicy = service.createPolicy(policy);

    //     // Return both details together
    //     return ResponseEntity.ok(Map.of(
    //             "customer", customer,
    //             "policy", createdPolicy
    //     ));
    // }

    @GetMapping
    public ResponseEntity<List<Policy>> getAllPolicies() {
        return ResponseEntity.ok(service.getAllPolicies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Policy> getPolicyById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getPolicyById(id));
    }

@PutMapping("/{id}")
    public ResponseEntity<Policy> updatePolicy(
            @PathVariable("id") Long id, // <--- Added ("id")
            @RequestBody PolicyUpdateDTO dto) {

        return ResponseEntity.ok(service.updatePolicy(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePolicy(@PathVariable("id") Long id) {
        service.deletePolicy(id);
        return ResponseEntity.ok("Policy deleted successfully");
    }
    
    @GetMapping("/{id}/with-customer/{customerRegNo}")
    public ResponseEntity<Map<String, Object>> getPolicyWithCustomer(
    @PathVariable("id") Long id, 
    @PathVariable("customerRegNo") String customerRegNo) {

    // Fetch the policy
    Policy policy = service.getPolicyById(id);

    // Fetch customer details using Feign
    Map<String, Object> customer = customerClient.getCustomerByRegNo(customerRegNo);

    // Combine both
    return ResponseEntity.ok(Map.of(
            "policy", policy,
            "customer", customer
    ));
}
    @GetMapping("/customer/{customerRegNo}")
    public ResponseEntity<List<Policy>> getPoliciesByCustomerRegNo(@PathVariable("customerRegNo") String customerRegNo) {
        
        // 1. Get Customer Details (from Cache or Feign)
        Map<String, Object> customer = customerCacheService.getCustomerByRegNo(customerRegNo);
        
        // 2. Extract the Numeric ID
        Long customerId = Long.valueOf(customer.get("id").toString());

        // 3. Find policies using that ID
        return ResponseEntity.ok(service.getPoliciesByCustomerId(customerId));
    }
    //@GetMapping("/customer/{customerRegNo}")
    //public ResponseEntity<List<Policy>> getPoliciesByCustomerRegNo(@PathVariable String customerRegNo) {
    //    return ResponseEntity.ok(service.getPoliciesByCustomerRegNo(customerRegNo));
    //}
}