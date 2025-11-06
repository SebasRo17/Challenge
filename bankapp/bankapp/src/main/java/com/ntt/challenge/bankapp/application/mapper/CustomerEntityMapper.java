package com.ntt.challenge.bankapp.application.mapper;

import com.ntt.challenge.bankapp.domain.model.Customer;
import com.ntt.challenge.bankapp.infrastructure.persistence.entity.CustomerEntity;

public class CustomerEntityMapper {
    private CustomerEntityMapper() {
    }

    public static Customer toDomain(CustomerEntity entity) {
        if (entity == null)
            return null;
        Customer c = new Customer();
        c.setCustomerId(entity.getCustomerId());
        c.setName(entity.getName());
        c.setGender(entity.getGender());
        c.setAddress(entity.getAddress());
        c.setPhone(entity.getPhone());
        c.setIdentification(entity.getIdentification());
        c.setPassword(entity.getPassword());
        c.setStatus(entity.getStatus());
        // No mapeamos accounts para evitar ciclos
        return c;
    }

    public static CustomerEntity toEntity(Customer domain) {
        if (domain == null)
            return null;
        CustomerEntity e = new CustomerEntity();
        e.setCustomerId(domain.getCustomerId());
        e.setName(domain.getName());
        e.setGender(domain.getGender());
        e.setAddress(domain.getAddress());
        e.setPhone(domain.getPhone());
        e.setIdentification(domain.getIdentification());
        e.setPassword(domain.getPassword());
        e.setStatus(domain.getStatus());
        return e;
    }
}
