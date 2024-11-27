package com.it43.equicktrack.transaction;


import com.it43.equicktrack.user.User;
import com.it43.equicktrack.equipment.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, CrudRepository<Transaction, Long>{

    Transaction findTransactionsByUser(User user);
    Equipment findEquipmentById(Long _id);

}
