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
        return customerRepository.findAll();
    }

    public Mono<Customer> findCustomerById(Long customerId) {
        log.info("Finding customer by ID: {}", customerId);
        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.error(new RuntimeException("Customer not found")));
    }

    public Mono<Customer> saveCustomer(Customer customer) {
        log.info("Saving new customer: {}", customer);
        String passCodificado = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(passCodificado);
        return customerRepository.save(customer);
    }

    public Mono<Customer> updateCustomer(Long customerId, Customer customerDetails) {
        log.info("Updating customer with ID: {}", customerId);
        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.error(new RuntimeException("Customer not found")))
                .flatMap(customer -> {
                    customer.setName(customerDetails.getName());
                    customer.setAddress(customerDetails.getAddress());
                    customer.setPhone(customerDetails.getPhone());
                    customer.setStatus(customerDetails.getStatus());
                    if (customerDetails.getPassword() != null && !customerDetails.getPassword().isEmpty()) {
                        String passCodificado = passwordEncoder.encode(customerDetails.getPassword());
                        customer.setPassword(passCodificado);
                    }
                    return customerRepository.save(customer);
                });
    }

    public Mono<Void> deleteCustomer(Long customerId) {
        log.info("Deleting customer with ID: {}", customerId);
        return customerRepository.deleteById(customerId);
    }

    public Mono<Customer> findByIdentification(String identification) {
        log.info("Finding customer by identification: {}", identification);
        return customerRepository.findByIdentification(identification)
                .switchIfEmpty(Mono.error(new RuntimeException("Customer not found")));
    }
}