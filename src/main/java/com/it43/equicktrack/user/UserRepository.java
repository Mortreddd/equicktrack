package com.it43.equicktrack.user;

import com.it43.equicktrack.util.DateUtilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


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

    default void saveSuperAdminIfNotExists() {

        List<User> users = findAll();
        boolean hasSuperAdmin = users.stream()
                .anyMatch((u) -> u.getRoles()
                        .stream()
                        .anyMatch( (r) -> Objects.equals(r.getName(), RoleName.SUPER_ADMIN))
                );

        if(!hasSuperAdmin) {
            User superAdmin = User.builder()
                    .fullName("Emmanuel Male")
                    .googleUid(null)
                    .email("emmanmale@gmail.com")
                    .roles(Set.of(Role.builder().name(RoleName.SUPER_ADMIN).build()))
                    .contactNumber("09670778658")
                    .password(new BCryptPasswordEncoder().encode("12345678"))
                    .emailVerifiedAt(DateUtilities.now())
                    .createdAt(DateUtilities.now())
                    .build();

            save(superAdmin);
        }
    }
}

