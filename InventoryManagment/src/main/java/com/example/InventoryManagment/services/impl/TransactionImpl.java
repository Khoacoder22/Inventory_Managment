package com.example.InventoryManagment.services.impl;

import com.example.InventoryManagment.dtos.Response;
import com.example.InventoryManagment.dtos.TransactionDTO;
import com.example.InventoryManagment.dtos.TransactionRequest;
import com.example.InventoryManagment.enums.*;
import com.example.InventoryManagment.exception.*;
import com.example.InventoryManagment.models.*;
import com.example.InventoryManagment.repository.*;
import com.example.InventoryManagment.services.TransactionService;
import com.example.InventoryManagment.services.UserService;
import com.example.InventoryManagment.specification.TransactionFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public Response purchase(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        int quantity = transactionRequest.getQuantity();

        if(supplierId == null) throw new NameValueRequiredException("Supplier is not Required");

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new NotFoundException("Product Not found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found"));

        User user = userService.getCurrentLoggedInUser();

        //save
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        //create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Purchase successfully")
                .build();
    }

    @Override
    public Response sell(TransactionRequest transactionRequest) {
            Long productId = transactionRequest.getProductId();
            int quantity = transactionRequest.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Product Not Found"));

            User user = userService.getCurrentLoggedInUser();

            //Update the stock quantity and re-save
            product.setStockQuantity(product.getStockQuantity() + quantity);
            productRepository.save(product);

            //tạo transaction
            Transaction transaction = Transaction.builder()
                    .transactionType(TransactionType.PURCHASE)
                    .status(TransactionStatus.COMPLETED)
                    .product(product)
                    .user(user)
                    .totalProducts(quantity)
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                    .description(transactionRequest.getDescription())
                    .note(transactionRequest.getNote())
                    .build();

        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Purchase successfully")
                .build();
    }

    @Override
    public Response returnToSupplier(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        int quantity = transactionRequest.getQuantity();
        Long supplierId = transactionRequest.getSupplierId();

        User user = userService.getCurrentLoggedInUser();

        if(supplierId == null) throw new NameValueRequiredException("Supplier is not Required");

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new NotFoundException("Product Not found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found"));


        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.RETURN_TO_SUPPLIER)
                .status(TransactionStatus.PROCESSING)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(BigDecimal.ZERO)
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Purchase successfully")
                .build();
    }

    @Override
    public Response getAllTransaction(int page, int size, String filter) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Specification<Transaction> spec = TransactionFilter.byFilter(filter);
        Page<Transaction> transactionPage = transactionRepository.findAll(spec, pageable);

        List<TransactionDTO> transactionDTOS = modelMapper.map(transactionPage.getContent(), new TypeToken<List<List<TransactionDTO>>>(){}.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setProduct(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .totalElements(transactionPage.getTotalElements())
                .totalPages(transactionPage.getTotalPages())
                .build();
    }

    @Override
    public Response getAllTransactionBy(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("transaction not found"));

        TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);

        return Response.builder()
                .status(200)
                .message("success")
                .transaction(transactionDTO)
                .build();
    }

    @Override
    public Response getAllTransactionByMonthAndYear(int month, int year) {
        List<Transaction> transactions = transactionRepository.findAll(TransactionFilter.byMonthAndYear(month, year));

        //lấy list từ DTO -> modelmapp laấy attribute những gì cần thiết
        List<TransactionDTO> transactionDTOS = modelMapper.map(transactions, new TypeToken<List<TransactionDTO>>(){}.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setProduct(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .build();
    }

    @Override
    public Response updateTransactionStatus(Long TransactionId, TransactionStatus status) {
        Transaction exisitngtransaction = transactionRepository.findById(TransactionId)
                .orElseThrow(() -> new NotFoundException("Transaction Not found"));

        exisitngtransaction.setStatus(status);
        exisitngtransaction.setUpdateAt(LocalDateTime.now());

        transactionRepository.save(exisitngtransaction);

        return Response.builder()
                .status(200)
                .message("success")
                .build();
    }
}
