package com.it43.equicktrack.equipment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long>, CrudRepository<Equipment, Long> {

    Optional<Equipment> findEquipmentByQrcode(String qrcode);
}
