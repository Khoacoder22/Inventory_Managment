package com.example.InventoryManagment.services;


import com.example.InventoryManagment.dtos.Response;
import com.example.InventoryManagment.dtos.TransactionRequest;

public interface TransactionService {

    Response purchase(TransactionRequest transactionRequest);

    Response sell(TransactionRequest transactionRequest);

    Response returnToSupplier(TransactionRequest transactionRequest);

    Response getAllTransaction(int page, int size, String searchValue);

    Response getAllTransactionBy(Long id);

    Response getAllTransactionByMonthAndYear(int month, int year);

    Response updateTransactionStatus(Long TransactionId,TransactionRequest transactionRequest);

}
