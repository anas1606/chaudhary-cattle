package com.chaudhary.chaudharycattle.repositories;

import com.chaudhary.chaudharycattle.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <UserInfo, Integer> {
    UserInfo findByUsername (String userName);
}
