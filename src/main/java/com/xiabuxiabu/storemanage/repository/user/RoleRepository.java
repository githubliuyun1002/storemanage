package com.xiabuxiabu.storemanage.repository.user;

import com.xiabuxiabu.storemanage.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 角色数据层
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
