package com.ntt.challenge.bankapp.infrastructure.repository.adapter;

import com.ntt.challenge.bankapp.application.mapper.CustomerEntityMapper;
import com.ntt.challenge.bankapp.domain.model.Customer;
import com.ntt.challenge.bankapp.domain.repository.CustomerRepository;
import com.ntt.challenge.bankapp.infrastructure.persistence.entity.CustomerEntity;
import com.ntt.challenge.bankapp.infrastructure.repository.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return customerJpaRepository.findAll().stream()
                .map(CustomerEntityMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findById(Long id) {
        return customerJpaRepository.findById(id)
                .map(CustomerEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findByIdentification(String identification) {
        return customerJpaRepository.findByIdentification(identification)
                .map(CustomerEntityMapper::toDomain);
    }

    @Override
    @Transactional
    public Customer save(Customer customer) {
        CustomerEntity entity = CustomerEntityMapper.toEntity(customer);
        CustomerEntity saved = customerJpaRepository.save(entity);
        return CustomerEntityMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        customerJpaRepository.deleteById(id);
    }
}
