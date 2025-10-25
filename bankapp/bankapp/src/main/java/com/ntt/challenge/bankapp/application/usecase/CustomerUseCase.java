package com.ntt.challenge.bankapp.application.usecase;

import com.ntt.challenge.bankapp.domain.model.Customer;
import com.ntt.challenge.bankapp.domain.service.CustomerService;
import com.ntt.challenge.bankapp.infrastructure.repository.CustomerJpaRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j; // importacion para logs
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j // anotacion para logs
@Service
@Getter
@Setter
@RequiredArgsConstructor

public class CustomerUseCase implements CustomerService {
    private final CustomerJpaRepository customerJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Flux<Customer> findAllCustomers() {
        log.info("Executing findAllCustomers"); // log de informacion
        return Flux.defer(() -> Flux.fromIterable(customerJpaRepository.findAll()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Customer> findCustomerById(Long customerId) {
        log.info("Finding customer by ID: {}", customerId); // log
        return Mono.fromCallable(() -> customerJpaRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found")))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Customer> saveCustomer(Customer customer) {
        log.info("Saving new customer: {}", customer); // log
        return Mono.fromCallable(() -> {
            // Codificar la contraseña antes de guardar
            String passCodificado = passwordEncoder.encode(customer.getPassword());
            customer.setPassword(passCodificado);
            return customerJpaRepository.save(customer);
        })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Customer> updateCustomer(Long customerId, Customer customerDetails) {
        log.info("Updating customer with ID: {}", customerId); // log
        return Mono.fromCallable(() -> {
            Customer customer = customerJpaRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            customer.setName(customerDetails.getName());
            customer.setAddress(customerDetails.getAddress());
            customer.setPhone(customerDetails.getPhone());
            customer.setStatus(customerDetails.getStatus());
            if (customerDetails.getPassword() != null && !customerDetails.getPassword().isEmpty()) {
                String passCodificado = passwordEncoder.encode(customerDetails.getPassword());
                customer.setPassword(passCodificado);
            }
            return customerJpaRepository.save(customer);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> deleteCustomer(Long customerId) {
        log.info("Deleting customer with ID: {}", customerId); // log
        return Mono.fromRunnable(() -> customerJpaRepository.deleteById(customerId))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Customer> findByIdentification(String identification) {
        log.info("Finding customer by identification: {}", identification); // log
        return Mono.fromCallable(() -> customerJpaRepository.findByIdentification(identification)
                .orElseThrow(() -> new RuntimeException("Customer not found")))
                .subscribeOn(Schedulers.boundedElastic());
    }
}