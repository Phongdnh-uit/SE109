package com.uit.se109.repositories;

import com.uit.se109.entities.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CommonRepository<User, Long> {}
