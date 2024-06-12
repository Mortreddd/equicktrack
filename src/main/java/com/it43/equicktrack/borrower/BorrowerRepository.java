package com.it43.equicktrack.borrower;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;


@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long>, CrudRepository<Borrower, Long> {
    Optional<Borrower> findByEmail(String email);

}

