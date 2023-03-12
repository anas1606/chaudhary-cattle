package com.chaudhary.chaudharycattle.repositories;

import com.chaudhary.chaudharycattle.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, Integer> {
    User findByUsername (String userName);
}
