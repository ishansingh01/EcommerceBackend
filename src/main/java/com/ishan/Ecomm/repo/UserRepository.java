package com.ishan.Ecomm.repo;

import com.ishan.Ecomm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>  {
    User findByEmail(String email);
}
