package com.example.InventoryManagment.services;


import com.example.InventoryManagment.dtos.Response;
import com.example.InventoryManagment.dtos.SupplierDTO;

public interface SupplierService {

    Response addSupplier(SupplierDTO supplierDTO);

    Response deleteSupplier(Long id);

    Response getAllSupplier();

    Response getSupplierById(Long id);

    Response UpdateSupplier(Long id, SupplierDTO supplierDTO);
}
