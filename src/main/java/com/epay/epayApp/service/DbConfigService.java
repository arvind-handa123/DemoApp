package com.epay.epayApp.service;

import java.util.List;

import com.epay.epayApp.entity.DbConfig;

/**
 * 
 * @author Asad Ali
 *
 */
public interface DbConfigService {

	Boolean getBooleanProperty(String propertyName, Boolean defaultValue);

	Integer getIntProperty(String propertyName, Integer defaultValue);

	Long getLongProperty(String propertyName, Long defaultValue);

	Double getDoubleProperty(String propertyName, Double defaultValue);

	Float getFloatProperty(String propertyName, Float defaultValue);

	String getProperty(String propertyName, String defaultValue);

	public String getPropertyWithEmptyCheck(String propertyName, String defaultValue);

	List<String> getPropertyValues(String string, List<String> asList);

}
