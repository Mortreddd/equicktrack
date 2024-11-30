package com.it43.equicktrack.dto.user;

import com.it43.equicktrack.user.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRoleRequest {

    private RoleName roleName;
}
