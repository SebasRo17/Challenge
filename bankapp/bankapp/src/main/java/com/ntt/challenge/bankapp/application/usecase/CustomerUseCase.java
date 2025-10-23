package com.ntt.challenge.bankapp.application.usecase;

import com.banco.challenge.domain.model.Customer;
import com.banco.challenge.domain.service.CustomerService;
import com.banco.challenge.infrastructure.repository.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // importacion para logs
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j // anotacion para logs
@Service
@RequiredArgsConstructor

public class CustomerUseCase implements CustomerService {
    private final CustomerJpaRepository customerJpaRepository;

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
        return Mono.fromCallable(() -> customerJpaRepository.save(customer))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Customer> updateCustomer(Long customerId, Customer customer) {
        log.info("Updating customer with ID: {}", customerId); // log
        return Mono.fromCallable(() -> {
            Customer customer = customerJpaRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            customer.setName(customer.getName());
            customer.setAddress(customer.getAddress());
            customer.setPhone(customer.getPhone());
            customer.setStatus(customer.getStatus());

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
                    .orElseThrow(() -> new RuntimeException("Customer not found")));
                .subscribeOn(Schedulers.boundedElastic());
    }
}