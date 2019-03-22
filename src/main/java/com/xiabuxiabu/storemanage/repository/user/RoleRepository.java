package com.xiabuxiabu.storemanage.repository.user;

import com.xiabuxiabu.storemanage.entity.user.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 角色数据层
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findAll();
    Page<Role> findAll(Pageable pageable);

    Page<Role> findAll(Specification<Role> specification);

    Page<Role> findAll(Specification<Role> specification, Pageable pageable);
}
