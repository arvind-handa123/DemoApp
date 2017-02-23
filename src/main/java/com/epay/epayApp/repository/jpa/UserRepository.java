package com.epay.epayApp.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epay.epayApp.entity.EpayUser;

/**
 * 
 * @author Asad Ali created on 19/02/2017
 *
 */

@Repository("userRepository")
public interface UserRepository extends JpaRepository<EpayUser, Long> {

	EpayUser findByAccessToken(String accessToken);

}
