package com.banco.challenge.infrastructure.entrypoint;

import com.banco.challenge.domain.model.Customer;
import com.banco.challenge.domain.service.CustomerService; 
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor

public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public Flux<Customer> getAllCustomers() {
        log.info("GET /api/v1/customers");
        return customerService.findAllCustomers();
    }

    @GetMapping("/{id}")
    public Mono<Customer> getCustomerById(@PathVariable Long id) {
        log.info("GET /api/v1/customers/{}", id);
        return customerService.findCustomerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Customer> createCustomer(@RequestBody Customer customer) {
        log.info("POST /api/v1/customers");
        return customerService.saveCustomer(customer);
    }

    @PutMapping("/{id}")
    public Mono<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        log.info("PUT /api/v1/customers/{}", id);
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCustomer(@PathVariable Long id) {
        log.info("DELETE /api/v1/customers/{}", id);
        return customerService.deleteCustomer(id);
    }
}
