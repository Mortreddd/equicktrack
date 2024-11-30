package com.it43.equicktrack.dto.dashboard;

import com.it43.equicktrack.user.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUserRoleRequest {

    private RoleName role;
}
