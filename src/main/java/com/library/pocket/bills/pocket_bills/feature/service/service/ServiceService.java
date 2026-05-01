package com.library.pocket.bills.pocket_bills.feature.service.service;

import com.library.pocket.bills.pocket_bills.common.exception.ResourceNotFoundException;
import com.library.pocket.bills.pocket_bills.feature.service.dto.request.ServiceCreateRequest;
import com.library.pocket.bills.pocket_bills.feature.service.dto.request.ServiceUpdateRequest;
import com.library.pocket.bills.pocket_bills.feature.service.dto.response.ServiceResponse;
import com.library.pocket.bills.pocket_bills.feature.service.entity.Service;
import com.library.pocket.bills.pocket_bills.feature.service.mapper.ServiceMapper;
import com.library.pocket.bills.pocket_bills.feature.service.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    @Transactional
    public ServiceResponse createService(ServiceCreateRequest request) {
        if (serviceRepository.existsByCode(request.code())) {
            throw new IllegalArgumentException("Service with this code already exists");
        }

        Service service = Service.builder()
                .code(request.code())
                .name(request.name())
                .unit(request.unit())
                .billingType(request.billingType())
                .build();

        Service savedService = serviceRepository.save(service);
        return serviceMapper.toResponse(savedService);
    }

    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findAll(Sort.by("name"))
                .stream()
                .map(serviceMapper::toResponse)
                .toList();
    }

    public ServiceResponse getServiceById(Long serviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service"));

        return serviceMapper.toResponse(service);
    }

    @Transactional
    public ServiceResponse updateService(Long serviceId, ServiceUpdateRequest request) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service"));

        if (serviceRepository.existsByCodeAndIdNot(request.code(), serviceId)) {
            throw new IllegalArgumentException("Service with this code already exists");
        }

        service.setCode(request.code());
        service.setName(request.name());
        service.setUnit(request.unit());
        service.setBillingType(request.billingType());

        Service savedService = serviceRepository.save(service);
        return serviceMapper.toResponse(savedService);
    }

    @Transactional
    public void deleteService(Long serviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service"));

        serviceRepository.delete(service);
    }
}
