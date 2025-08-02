package com.example.InventoryManagment.controller;

import com.example.InventoryManagment.dtos.Response;
import com.example.InventoryManagment.dtos.TransactionRequest;
import com.example.InventoryManagment.enums.TransactionStatus;
import com.example.InventoryManagment.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/purchase")
    public ResponseEntity<Response> purchase(@RequestBody @Valid TransactionRequest transactionRequest){
        return ResponseEntity.ok(transactionService.purchase(transactionRequest));
    }

    @PostMapping("/sell")
    public ResponseEntity<Response> makeSale(@RequestBody @Valid TransactionRequest transactionRequest){
        return ResponseEntity.ok(transactionService.sell(transactionRequest));
    }

    @PostMapping("/return")
    public ResponseEntity<Response> returnSupplier(@RequestBody @Valid TransactionRequest transactionRequest){
        return ResponseEntity.ok(transactionService.returnToSupplier(transactionRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllTransaction(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "1000") int size,@RequestParam(defaultValue = "0") String searchValue){
        return ResponseEntity.ok(transactionService.getAllTransaction(page, size, searchValue));
   }

   @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable Long id){
        return ResponseEntity.ok(transactionService.getAllTransactionBy(id));
   }

    @GetMapping("/byTime")
    public ResponseEntity<Response> getTransactionByMonthAndYear(@RequestParam int month, @RequestParam int year){
        return ResponseEntity.ok(transactionService.getAllTransactionByMonthAndYear(month, year));
     }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Response> updateTransaction(@PathVariable Long transactionId, @RequestBody TransactionStatus status){
        return ResponseEntity.ok(transactionService.updateTransactionStatus(transactionId, status));
    }

}
