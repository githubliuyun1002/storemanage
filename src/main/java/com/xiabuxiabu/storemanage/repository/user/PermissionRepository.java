package com.xiabuxiabu.storemanage.repository.user;

import com.xiabuxiabu.storemanage.entity.user.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 权限数据层
 */
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

}
