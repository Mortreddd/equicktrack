package com.it43.equicktrack.equipment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long>, CrudRepository<Equipment, Long> {

    @Query("SELECT e FROM Equipment e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Equipment> findEquipmentByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT e FROM Equipment e WHERE e.available = :available")
    Page<Equipment> findAvailableEquipments(@Param("available") boolean available, Pageable pageable);

    @Query("SELECT e FROM Equipment e WHERE e.available = :available")
    Page<Equipment> findUnavailableEquipments(@Param("available") boolean available, Pageable pageable);


    Optional<Equipment> findByQrcodeData(String qrcode);
    Optional<Equipment> findBySerialNumber(String serialNumber);
    List<Equipment> findByRemark(Remark remark);
}
