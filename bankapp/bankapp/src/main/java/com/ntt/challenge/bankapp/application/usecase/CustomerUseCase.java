package com.ntt.challenge.bankapp.application.usecase;

import com.ntt.challenge.bankapp.domain.model.Customer;
import com.ntt.challenge.bankapp.domain.repository.CustomerRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@Getter
@Setter
@RequiredArgsConstructor
public class CustomerUseCase {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public Flux<Customer> findAllCustomers() {
        log.info("Executing findAllCustomers");
        return Flux.defer(() -> Flux.fromIterable(customerRepository.findAll()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Customer> findCustomerById(Long customerId) {
        log.info("Finding customer by ID: {}", customerId);
        return Mono.fromCallable(() -> customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found")))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Customer> saveCustomer(Customer customer) {
        log.info("Saving new customer: {}", customer);
        return Mono.fromCallable(() -> {
            String passCodificado = passwordEncoder.encode(customer.getPassword());
            customer.setPassword(passCodificado);
            return customerRepository.save(customer);
        })
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Customer> updateCustomer(Long customerId, Customer customerDetails) {
        log.info("Updating customer with ID: {}", customerId);
        return Mono.fromCallable(() -> {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            customer.setName(customerDetails.getName());
            customer.setAddress(customerDetails.getAddress());
            customer.setPhone(customerDetails.getPhone());
            customer.setStatus(customerDetails.getStatus());
            if (customerDetails.getPassword() != null && !customerDetails.getPassword().isEmpty()) {
                String passCodificado = passwordEncoder.encode(customerDetails.getPassword());
                customer.setPassword(passCodificado);
            }
            return customerRepository.save(customer);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Void> deleteCustomer(Long customerId) {
        log.info("Deleting customer with ID: {}", customerId);
        return Mono.fromRunnable(() -> customerRepository.deleteById(customerId))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    public Mono<Customer> findByIdentification(String identification) {
        log.info("Finding customer by identification: {}", identification);
        return Mono.fromCallable(() -> customerRepository.findByIdentification(identification)
                .orElseThrow(() -> new RuntimeException("Customer not found")))
                .subscribeOn(Schedulers.boundedElastic());
    }
}