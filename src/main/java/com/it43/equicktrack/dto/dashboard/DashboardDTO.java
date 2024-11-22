package com.it43.equicktrack.dto.dashboard;



import com.it43.equicktrack.dto.equipment.EquipmentDTO;
import com.it43.equicktrack.dto.transaction.TransactionDTO;
import com.it43.equicktrack.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {

    private List<TransactionDTO> transactions;
    private List<EquipmentDTO> equipments;
    private List<UserDTO> users;

}
