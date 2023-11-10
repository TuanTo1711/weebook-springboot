package org.weebook.api.admin.web.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.weebook.api.admin.dto.AdminDto;

@Builder
@Getter
@Setter
public class AdminUpdateStaffResponse {
    private AdminDto old;
    private AdminDto neq;
}
