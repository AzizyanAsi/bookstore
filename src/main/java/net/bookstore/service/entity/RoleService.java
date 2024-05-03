package net.bookstore.service.entity;

import net.bookstore.entity.Role;
import net.bookstore.entity.enums.RoleType;

public interface RoleService extends EntityReadService<Role> {
    Role getByRoleType(RoleType roleType);
}
