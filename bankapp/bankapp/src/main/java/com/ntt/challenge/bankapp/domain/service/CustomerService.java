package com.ntt.challenge.bankapp.domain.service;

import com.ntt.challenge.bankapp.domain.model.Customer;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface CustomerService{
    Flux<Customer> findAllCustomers();
    Mono<Customer> findCustomerById(Long customerId);
    Mono<Customer> saveCustomer(Customer customer);
    Mono<Customer> updateCustomer(Long customerId, Customer customer);
    Mono<Void> deleteCustomer(Long customerId);
    Mono<Customer> findByIdentification(String identification);
}