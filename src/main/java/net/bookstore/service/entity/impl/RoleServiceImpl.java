package net.bookstore.service.entity.impl;

import lombok.extern.slf4j.Slf4j;
import net.bookstore.common.cache.EntityCacheNames;
import net.bookstore.entity.Role;
import net.bookstore.entity.enums.RoleType;
import net.bookstore.exception.common.EntityNotFoundException;
import net.bookstore.service.entity.RoleService;
import net.bookstore.repository.RoleRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Cacheable(value = EntityCacheNames.ALL_ROLES)
    public List<Role> getAllEntities() {
        return roleRepository.findAll();
    }

    @Override
    @Cacheable(value = EntityCacheNames.ROLE, key = "#id", unless = "#result == null")
    public Role getEntity(Long id) {
        Optional<Role> optRole = roleRepository.findById(id);
        if (optRole.isEmpty()) {
            throw new EntityNotFoundException(String.format("Role with id {%d} not found", id));
        }
        return optRole.get();
    }

    @Override
    public Role getByRoleType(RoleType roleType) {
        Optional<Role> optRole = roleRepository.findByRoleType(roleType);
        if (optRole.isEmpty()) {
            throw new EntityNotFoundException(String.format("Role with role type {%s} not found", roleType));
        }
        return optRole.get();
    }
}
