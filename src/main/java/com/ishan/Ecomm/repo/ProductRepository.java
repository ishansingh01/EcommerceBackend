package com.ishan.Ecomm.repo;

import com.ishan.Ecomm.model.Product;
import com.ishan.Ecomm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
