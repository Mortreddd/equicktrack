package com.it43.equicktrack.dto;

import com.it43.equicktrack.borrower.Borrower;
import com.it43.equicktrack.transaction.Transaction;
import lombok.Builder;

import java.util.List;

public record BorrowerTransactionsDTO (Borrower borrower, List<Transaction> transactions){}