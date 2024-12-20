package com.it43.equicktrack.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>, CrudRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);

    default void saveIfNotExists(Role role){
        if(findByName(role.getName()).isEmpty()){
            save(role);
        }
    }
}
