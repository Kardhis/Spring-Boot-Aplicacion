package com.springBootAppication.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springBootAppication.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

	
}
