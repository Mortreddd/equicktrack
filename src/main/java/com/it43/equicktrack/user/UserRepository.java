package com.it43.equicktrack.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long>, CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

}

