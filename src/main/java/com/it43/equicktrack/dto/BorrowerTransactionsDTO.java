package com.it43.equicktrack.dto;

import com.it43.equicktrack.user.User;
import com.it43.equicktrack.transaction.Transaction;

import java.util.List;

public record BorrowerTransactionsDTO (User user, List<Transaction> transactions){}