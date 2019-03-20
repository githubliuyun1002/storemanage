package com.xiabuxiabu.storemanage.repository.store;

import com.xiabuxiabu.storemanage.entity.store.AccessMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface AccessMethodRepository extends JpaRepository<AccessMethod, Integer> {
}
