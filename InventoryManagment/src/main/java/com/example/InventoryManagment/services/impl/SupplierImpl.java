package com.example.InventoryManagment.services.impl;


import com.example.InventoryManagment.dtos.Response;
import com.example.InventoryManagment.dtos.SupplierDTO;
import com.example.InventoryManagment.exception.NotFoundException;
import com.example.InventoryManagment.models.Supplier;
import com.example.InventoryManagment.repository.SupplierRepository;
import com.example.InventoryManagment.services.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response addSupplier(SupplierDTO supplierDTO) {

        Supplier supplierToSave = modelMapper.map(supplierDTO, Supplier.class);

        supplierRepository.save(supplierToSave);

        return Response.builder()
                .status(200)
                .message("Adding successfully")
                .build();
    }

    @Override
    public Response deleteSupplier(Long id) {

        supplierRepository.findById(id)
                        .orElseThrow(()-> new NotFoundException("Not found"));

        supplierRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Deleted successfully")
                .build();
    }

    @Override
    public Response getAllSupplier() {
        List<Supplier> suppliers = supplierRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<SupplierDTO> supplierDTOList = modelMapper.map(suppliers, new TypeToken<List<SupplierDTO>>(){}.getType());

        return Response.builder()
                .status(200)
                .message("Deleted successfully")
                .build();
    }

    @Override
    public Response getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Not found"));

        SupplierDTO supplierDTO = modelMapper.map(supplier, SupplierDTO.class);

        return Response.builder()
                .status(200)
                .message("Found it")
                .supplier(supplierDTO)
                .build();
    }

    @Override
    public Response UpdateSupplier(Long id, SupplierDTO supplierDTO) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found"));

        if(supplierDTO.getName() != null) existingSupplier.setName(supplierDTO.getName());
        if(supplierDTO.getContactInfo() != null) existingSupplier.setContactInfor(supplierDTO.getContactInfo());
        if(supplierDTO.getAddress() != null) existingSupplier.setAddress(supplierDTO.getAddress());

        supplierRepository.save(existingSupplier);

        return Response.builder()
                .status(200)
                .message("Update successfully")
                .build();
    }
}
