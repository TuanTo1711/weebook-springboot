package org.weebook.api.config;

import org.weebook.api.dto.RoleDto;

import java.util.Set;

public class RoleDtoConfig {
    public static RoleDto DEFAULT_ROLE_CONFIG = new RoleDto("user" , Set.of("read_book", "purchase_book", "manage_orders","manage_profile", "manage_comment"));
    public static RoleDto STAFF_ROLE_CONFIG = new RoleDto("staff" , Set.of("add_book", "update_book", "manage_orders_user", "view_statistics_purchase_book"));
    public static RoleDto ADMIN_ROLE_CONFIG = new RoleDto("admin" , Set.of("full_permission", "manage_staff", "manage_settings", "generate_reports", "backup_data", "manage_products", "manage_orders", "view_sales_reports"));
    public static RoleDto MODERATOR_ROLE_CONFIG = new RoleDto("moderator", Set.of("moderate_comments"));
    public static RoleDto SUPPORT_ROLE_CONFIG = new RoleDto("support", Set.of("answer_tickets", "resolve_issues"));

}
