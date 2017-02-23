package com.epay.epayApp.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epay.epayApp.entity.DbConfig;

/**
 * 
 * @author Asad Ali
 *
 */
@Repository("dbConfigRepository")
public interface DbConfigRepository extends JpaRepository<DbConfig, String> {
	DbConfig findByPropertyName(String propertyName);
}