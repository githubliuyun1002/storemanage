package com.xiabuxiabu.storemanage.repository.ccuser;

import com.xiabuxiabu.storemanage.entity.ccuser.CCRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CCRoleRepository extends JpaRepository<CCRole,Integer> {
    Page<CCRole> findAll(Pageable pageable);
    Page<CCRole> findAll(Specification<CCRole> specification);
    Page<CCRole> findAll(Specification<CCRole> specification,Pageable pageable);

}
