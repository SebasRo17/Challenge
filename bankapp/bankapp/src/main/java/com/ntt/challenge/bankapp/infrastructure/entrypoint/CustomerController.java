package com.ntt.challenge.bankapp.infrastructure.entrypoint;

import com.ntt.challenge.bankapp.application.dto.CustomerDto;
import com.ntt.challenge.bankapp.application.mapper.CustomerDtoMapper;
import com.ntt.challenge.bankapp.application.usecase.CustomerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerUseCase customerUseCase;

    @GetMapping
    public Flux<CustomerDto> getAllCustomers() {
        log.info("GET /api/v1/customers");
        return customerUseCase.findAllCustomers()
                .map(CustomerDtoMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<CustomerDto> getCustomerById(@PathVariable Long id) {
        log.info("GET /api/v1/customers/{}", id);
        return customerUseCase.findCustomerById(id)
                .map(CustomerDtoMapper::toDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customer) {
        log.info("POST /api/v1/customers");
        return customerUseCase.saveCustomer(CustomerDtoMapper.toDomain(customer))
                .map(CustomerDtoMapper::toDto);
    }

    @PutMapping("/{id}")
    public Mono<CustomerDto> updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customer) {
        log.info("PUT /api/v1/customers/{}", id);
        return customerUseCase.updateCustomer(id, CustomerDtoMapper.toDomain(customer))
                .map(CustomerDtoMapper::toDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCustomer(@PathVariable Long id) {
        log.info("DELETE /api/v1/customers/{}", id);
        return customerUseCase.deleteCustomer(id);
    }
}
