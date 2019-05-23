package com.xiabuxiabu.storemanage.repository.user;

import com.xiabuxiabu.storemanage.entity.user.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 权限数据层
 */
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Query("from Permission where pid =:pid")
    List<Permission> findAllByPid(@Param("pid")int pid);
}
