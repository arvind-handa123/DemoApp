package com.epay.epayApp.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epay.epayApp.entity.DbConfig;
import com.epay.epayApp.repository.jpa.DbConfigRepository;
import com.epay.epayApp.service.DbConfigService;

/**
 * 
 * @author Asad Ali
 *
 */
@Service
public class DbConfigServiceImpl implements DbConfigService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DbConfigServiceImpl.class);

	@Autowired
	DbConfigRepository dbConfigRepository;

	private Map<String, String> propertyMap = new ConcurrentHashMap<>();

	private volatile boolean isLoaded = false;

	@Override
	public String getProperty(String propertyName, String defaultValue) {
		String value = getProperty(propertyName);
		return value == null ? defaultValue : value;
	}

	@Override
	public String getPropertyWithEmptyCheck(String propertyName, String defaultValue) {
		String value = getProperty(propertyName);
		return value == null || value.isEmpty() ? defaultValue : value;
	}

	public String getProperty(String propertyName) {
		checkIfLoadOrRefresh();
		return propertyMap.get(propertyName);
	}

	public Boolean getBooleanProperty(String propertyName) {
		checkIfLoadOrRefresh();
		String propertyValue = propertyMap.get(propertyName);
		if (propertyValue == null)
			return null;
		boolean value = Boolean.parseBoolean(propertyValue);
		return value;
	}

	@Override
	public Boolean getBooleanProperty(String propertyName, Boolean defaultValue) {
		Boolean value = getBooleanProperty(propertyName);
		return value == null ? defaultValue : value;
	}

	@Override
	public Integer getIntProperty(String propertyName, Integer defaultValue) {
		Integer value = getIntProperty(propertyName);
		return value == null ? defaultValue : value;
	}

	public Long getLongProperty(String propertyName) {
		checkIfLoadOrRefresh();
		String propertyValue = propertyMap.get(propertyName);
		if (propertyValue == null)
			return null;
		try {
			long value = Long.parseLong(propertyValue);
			return value;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Long getLongProperty(String propertyName, Long defaultValue) {
		Long value = getLongProperty(propertyName);
		return value == null ? defaultValue : value;
	}

	public Integer getIntProperty(String propertyName) {
		checkIfLoadOrRefresh();
		String propertyValue = propertyMap.get(propertyName);
		if (propertyValue == null)
			return null;
		try {
			int value = Integer.parseInt(propertyValue);
			return value;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Double getDoubleProperty(String propertyName, Double defaultValue) {
		Double value = getDoubleProperty(propertyName);
		return value == null ? defaultValue : value;
	}

	public Double getDoubleProperty(String propertyName) {
		checkIfLoadOrRefresh();
		String propertyValue = propertyMap.get(propertyName);
		if (propertyValue == null)
			return null;
		try {
			double value = Double.parseDouble(propertyValue);
			return value;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Float getFloatProperty(String propertyName, Float defaultValue) {
		Float value = getFloatProperty(propertyName);
		return value == null ? defaultValue : value;
	}

	public Float getFloatProperty(String propertyName) {
		checkIfLoadOrRefresh();
		String propertyValue = propertyMap.get(propertyName);
		if (propertyValue == null)
			return null;
		try {
			float value = Float.parseFloat(propertyValue);
			return value;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void checkIfLoadOrRefresh() {
		if (isLoaded)
			return;

		synchronized (propertyMap) {
			if (!isLoaded) {
				List<DbConfig> propertyList = dbConfigRepository.findAll();
				for (DbConfig dbConfig : propertyList) {
					propertyMap.put(dbConfig.getPropertyName(), dbConfig.getPropertyValue());
				}
				LOGGER.info("db config loaded :" + propertyMap);

				isLoaded = true;
			}
		}
	}


	@Override
	public List<String> getPropertyValues(String propertyName, List<String> asList) {
		DbConfig config = dbConfigRepository.findByPropertyName(propertyName);
		if (config == null)
			return asList;
		return Arrays.asList(config.getPropertyValue());
	}

}