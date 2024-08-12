package com.it43.equicktrack.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByName(RoleName name);

    default void saveIfNotExists(Role role){
        if(findRoleByName(role.getName()).isEmpty()){
            save(role);
        }
    }
}
