package com.it43.equicktrack.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long>, CrudRepository<User, Long> {

    Page<User> findByFullName(@Param("fullName") String fullName, Pageable pageable);
//    TODO: Fixed search users for first name with pagination
//    @Query(value = "SELECT * FROM `users` WHERE LOWER(full_name) LIKE LOWER(CONCAT('%', :fullName, '%'))", nativeQuery = true)
//    Page<User> findBySearchFullName(@Param("fullName") String fullName, Pageable pageable);
    Optional<User> findByEmail(String email);
    Optional<User> findByGoogleUid(String uuid);

    default boolean emailExists(String email){
        return findByEmail(email).isPresent();
    };
}

