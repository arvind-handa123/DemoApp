package com.epay.epayApp.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epay.epayApp.entity.User;

/**
 * 
 * @author Asad Ali created on 19/02/2017
 *
 */

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

	User findByAccessToken(String accessToken);

}
