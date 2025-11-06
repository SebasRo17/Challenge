package com.ntt.challenge.bankapp.application.mapper;

import com.ntt.challenge.bankapp.application.dto.CustomerDto;
import com.ntt.challenge.bankapp.domain.model.Customer;

public class CustomerDtoMapper {
    private CustomerDtoMapper() {
    }

    public static Customer toDomain(CustomerDto dto) {
        if (dto == null)
            return null;
        Customer c = new Customer();
        c.setCustomerId(dto.getCustomerId());
        c.setName(dto.getName());
        c.setGender(dto.getGender());
        c.setAddress(dto.getAddress());
        c.setPhone(dto.getPhone());
        c.setIdentification(dto.getIdentification());
        c.setPassword(dto.getPassword());
        c.setStatus(dto.getStatus());
        return c;
    }

    public static CustomerDto toDto(Customer domain) {
        if (domain == null)
            return null;
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(domain.getCustomerId());
        dto.setName(domain.getName());
        dto.setGender(domain.getGender());
        dto.setAddress(domain.getAddress());
        dto.setPhone(domain.getPhone());
        dto.setIdentification(domain.getIdentification());
        dto.setPassword(domain.getPassword());
        dto.setStatus(domain.getStatus());
        return dto;
    }
}
