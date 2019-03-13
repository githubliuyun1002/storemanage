package com.xiabuxiabu.storemanage.repository.user;

import com.xiabuxiabu.storemanage.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 用户层数据层
 */
public interface UserRepository extends JpaRepository<User,Integer> {

    List<User> findAll();
    @Query("from User where username = :username")
    User findByName(@Param("username") String username);

    Page<User> findAll(Pageable pageable);

    Page<User> findAll(Specification<User> specification);

    Page<User> findAll(Specification<User> specification, Pageable pageable);

}
