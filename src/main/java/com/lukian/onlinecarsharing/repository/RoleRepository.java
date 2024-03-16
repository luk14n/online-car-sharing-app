package com.lukian.onlinecarsharing.repository;

import com.lukian.onlinecarsharing.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByRole(Role.RoleName roleName);
}
