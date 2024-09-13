package com.it43.equicktrack.dto.user;

import com.it43.equicktrack.dto.transaction.TransactionDTO;

import java.util.List;

public record UserTransactionDTO(List<TransactionDTO> transactions) { }
