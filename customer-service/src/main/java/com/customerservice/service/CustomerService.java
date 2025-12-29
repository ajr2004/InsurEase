package com.customerservice.service;

import com.customerservice.model.Customer;
import com.customerservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repo;

    public Customer createCustomer(Customer c) {
        return repo.save(c);
    }

    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }

    public Customer getCustomerById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer updateCustomer(Long id, Customer updated) {
        Customer existing = getCustomerById(id);
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setContactNumber(updated.getContactNumber());
        existing.setAddress(updated.getAddress());
        return repo.save(existing);
    }

    public void deleteCustomer(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Customer with ID " + id + " not found");
        }

        repo.deleteById(id);
    }
    
    

    public Customer getCustomerByRegNo(String regNo) {
        return repo.findByCustomerRegNo(regNo)
            .orElseThrow(() -> new RuntimeException("Customer not found with RegNo: " + regNo));
    }

}
