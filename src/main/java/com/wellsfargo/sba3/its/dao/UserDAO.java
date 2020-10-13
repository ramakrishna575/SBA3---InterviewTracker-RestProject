package com.wellsfargo.sba3.its.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wellsfargo.sba3.its.entity.UserEntity;

@Repository
public interface UserDAO extends JpaRepository<UserEntity, Integer>{

}
