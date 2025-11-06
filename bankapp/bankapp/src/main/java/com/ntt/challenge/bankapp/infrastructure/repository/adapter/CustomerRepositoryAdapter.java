package com.ntt.challenge.bankapp.infrastructure.repository.adapter;

import com.ntt.challenge.bankapp.application.mapper.CustomerEntityMapper;
import com.ntt.challenge.bankapp.domain.model.Customer;
import com.ntt.challenge.bankapp.domain.repository.CustomerRepository;
import com.ntt.challenge.bankapp.infrastructure.persistence.entity.CustomerEntity;
import com.ntt.challenge.bankapp.infrastructure.repository.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public List<Customer> findAll() {
        return customerJpaRepository.findAll().stream()
                .map(CustomerEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerJpaRepository.findById(id)
                .map(CustomerEntityMapper::toDomain);
    }

    @Override
    public Optional<Customer> findByIdentification(String identification) {
        return customerJpaRepository.findByIdentification(identification)
                .map(CustomerEntityMapper::toDomain);
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = CustomerEntityMapper.toEntity(customer);
        CustomerEntity saved = customerJpaRepository.save(entity);
        return CustomerEntityMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        customerJpaRepository.deleteById(id);
    }
}
