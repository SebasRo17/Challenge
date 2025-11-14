package com.ntt.challenge.bankapp.infrastructure.controller;

import com.ntt.challenge.bankapp.application.mapper.CustomerDtoMapper;
import com.ntt.challenge.bankapp.application.usecase.CustomerUseCase;
import com.ntt.challenge.bankapp.infrastructure.api.ClientesApi;
import com.ntt.challenge.bankapp.infrastructure.api.model.CustomerRequest;
import com.ntt.challenge.bankapp.infrastructure.api.model.CustomerResponse;
import com.ntt.challenge.bankapp.infrastructure.api.model.CustomerUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerApiController implements ClientesApi {

    private final CustomerUseCase customerUseCase;

    @Override
    public Mono<ResponseEntity<CustomerResponse>> createCustomer(
            Mono<CustomerRequest> customerRequest,
            ServerWebExchange exchange) {
        log.info("POST /customers");
        return customerRequest
                .map(this::toCustomerDto)
                .flatMap(dto -> customerUseCase.saveCustomer(CustomerDtoMapper.toDomain(dto)))
                .map(CustomerDtoMapper::toDto)
                .map(this::toCustomerResponse)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteCustomer(Long customerId, ServerWebExchange exchange) {
        log.info("DELETE /customers/{}", customerId);
        return customerUseCase.deleteCustomer(customerId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    @Override
    public Mono<ResponseEntity<Flux<CustomerResponse>>> getAllCustomers(ServerWebExchange exchange) {
        log.info("GET /customers");
        Flux<CustomerResponse> customers = customerUseCase.findAllCustomers()
                .map(CustomerDtoMapper::toDto)
                .map(this::toCustomerResponse);
        return Mono.just(ResponseEntity.ok(customers));
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> getCustomerById(Long customerId, ServerWebExchange exchange) {
        log.info("GET /customers/{}", customerId);
        return customerUseCase.findCustomerById(customerId)
                .map(CustomerDtoMapper::toDto)
                .map(this::toCustomerResponse)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> updateCustomer(
            Long customerId,
            Mono<CustomerUpdateRequest> customerUpdateRequest,
            ServerWebExchange exchange) {
        log.info("PUT /customers/{}", customerId);
        return customerUpdateRequest
                .map(this::toCustomerDtoForUpdate)
                .flatMap(dto -> customerUseCase.updateCustomer(customerId, CustomerDtoMapper.toDomain(dto)))
                .map(CustomerDtoMapper::toDto)
                .map(this::toCustomerResponse)
                .map(ResponseEntity::ok);
    }

    // Mappers de OpenAPI models a DTOs internos
    private com.ntt.challenge.bankapp.application.dto.CustomerDto toCustomerDto(CustomerRequest request) {
        com.ntt.challenge.bankapp.application.dto.CustomerDto dto = new com.ntt.challenge.bankapp.application.dto.CustomerDto();
        dto.setName(request.getName());
        dto.setGender(request.getGender() != null ? request.getGender().getValue() : null);
        dto.setAddress(request.getAddress());
        dto.setPhone(request.getPhone());
        dto.setIdentification(request.getIdentification());
        dto.setPassword(request.getPassword());
        dto.setStatus(request.getStatus());
        return dto;
    }

    private com.ntt.challenge.bankapp.application.dto.CustomerDto toCustomerDtoForUpdate(
            CustomerUpdateRequest request) {
        com.ntt.challenge.bankapp.application.dto.CustomerDto dto = new com.ntt.challenge.bankapp.application.dto.CustomerDto();
        dto.setName(request.getName());
        dto.setAddress(request.getAddress());
        dto.setPhone(request.getPhone());
        dto.setPassword(request.getPassword());
        dto.setStatus(request.getStatus());
        return dto;
    }

    private CustomerResponse toCustomerResponse(com.ntt.challenge.bankapp.application.dto.CustomerDto dto) {
        CustomerResponse response = new CustomerResponse();
        response.setCustomerId(dto.getCustomerId());
        response.setName(dto.getName());
        response.setGender(dto.getGender());
        response.setAddress(dto.getAddress());
        response.setPhone(dto.getPhone());
        response.setIdentification(dto.getIdentification());
        response.setStatus(dto.getStatus());
        return response;
    }
}
