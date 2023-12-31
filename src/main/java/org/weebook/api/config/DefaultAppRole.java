package org.weebook.api.config;

import org.weebook.api.dto.RoleDto;

import java.util.Set;

public class DefaultAppRole {

    private DefaultAppRole() {
    }

    public static final RoleDto DEFAULT_USER_ROLE = new RoleDto(
            "user",
            Set.of("read_book",
                    "purchase_book",
                    "manage_orders",
                    "manage_profile",
                    "manage_comment")
    );
    public static final RoleDto STAFF_ROLE_CONFIG = new RoleDto(
            "staff",
            Set.of("add_book",
                    "update_book",
                    "manage_orders_user",
                    "view_statistics_purchase_book")
    );
    public static final RoleDto ADMIN_ROLE_CONFIG = new RoleDto(
            "admin",
            Set.of("full_permission",
                    "manage_staff",
                    "manage_settings",
                    "generate_reports",
                    "backup_data",
                    "manage_products",
                    "manage_orders",
                    "view_sales_reports")
    );
    public static final RoleDto MODERATOR_ROLE_CONFIG = new RoleDto(
            "moderator", Set.of("moderate_comments"));
    public static final RoleDto SUPPORT_ROLE_CONFIG = new RoleDto(
            "support",
            Set.of("answer_tickets",
                    "resolve_issues")
    );

    public static final RoleDto getRoleConfigByUserType(String userType){
        switch (userType){
            case "admin":
                return ADMIN_ROLE_CONFIG;
            case "staff":
                return STAFF_ROLE_CONFIG;
            case "moderator":
                return MODERATOR_ROLE_CONFIG;
            case "support":
                return SUPPORT_ROLE_CONFIG;
            default:
                return DEFAULT_USER_ROLE;
        }
    }
}
