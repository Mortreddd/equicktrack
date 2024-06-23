package com.it43.equicktrack.transaction;


import com.it43.equicktrack.borrower.Borrower;
import com.it43.equicktrack.equipment.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, CrudRepository<Transaction, Long> {

    Optional<List<Transaction>> findTransactionsByBorrower(Borrower borrower);
    Optional<Equipment> findEquipmentById(Long _id);
}
