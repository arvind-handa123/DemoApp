package com.epay.epayApp.spring.root;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.jolbox.bonecp.BoneCPDataSource;
/**
 * 
 * @author Asad Ali
 *
 */
@Configuration(value = "domainPersistenceConfig")
@EnableTransactionManagement
@PropertySource(value = {"classpath:/persistence.properties"})

public class PersistenceConfig {

    private static final String PROPERTY_NAME_JDBC_DRIVER_CLASSNAME = "jdbc.driver.classname";
    private static final String PROPERTY_NAME_JDBC_URL = "jdbc.url";
    private static final String PROPERTY_NAME_JDBC_USERNAME = "jdbc.username";
    private static final String PROPERTY_NAME_JDBC_PASSWORD = "jdbc.password";
    
    private static final String PROPERTY_NAME_JDBC_SHOW_SQL = "jdbc.show.sql";
    private static final String PROPERTY_NAME_JDBC_GENERATE_DDL = "jdbc.generate.ddl";
    private static final String PROPERTY_NAME_JDBC_DATABASE = "jdbc.database";
    private static final String PROPERTY_NAME_JDBC_DATABASE_PLATFORM = "jdbc.database.platform";
    private static final String PROPERTY_NAME_JDBC_DATABASE_PLATFORM1 = "jdbc.database.platform";
    private static final String PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        BoneCPDataSource dataSource = new BoneCPDataSource();

        dataSource.setDriverClass(environment.getRequiredProperty(PROPERTY_NAME_JDBC_DRIVER_CLASSNAME));
        dataSource.setJdbcUrl(environment.getRequiredProperty(PROPERTY_NAME_JDBC_URL));
        dataSource.setUsername(environment.getRequiredProperty(PROPERTY_NAME_JDBC_USERNAME));
        dataSource.setPassword(environment.getRequiredProperty(PROPERTY_NAME_JDBC_PASSWORD));
        dataSource.setPartitionCount(2);
        dataSource.setMinConnectionsPerPartition(20);
        dataSource.setMaxConnectionsPerPartition(50);
        dataSource.setAcquireIncrement(10);
        dataSource.setStatementsCacheSize(100);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        factoryBean.setDataSource(dataSource());
        factoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter());
        factoryBean.setJpaDialect(hibernateJpaDialect());
        factoryBean.setJpaPropertyMap(jpaPropertyMap());
        factoryBean.setPackagesToScan("com.epay.epayApp.entity");
        return factoryBean;
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager() {
        final JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();

        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        jpaTransactionManager.setDataSource(dataSource());

        return jpaTransactionManager;
    }
    
    @Bean
    public HibernateJpaDialect hibernateJpaDialect() {
        return new HibernateJpaDialect();
    }
    
    @Bean
    public HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
        final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(Boolean.parseBoolean(environment.getProperty(PROPERTY_NAME_JDBC_SHOW_SQL)));
        jpaVendorAdapter.setGenerateDdl(Boolean.parseBoolean(environment.getProperty(PROPERTY_NAME_JDBC_GENERATE_DDL)));
        jpaVendorAdapter.setDatabase(Database.valueOf(environment.getProperty(PROPERTY_NAME_JDBC_DATABASE)));
        String databasePlatform = environment.getProperty(PROPERTY_NAME_JDBC_DATABASE_PLATFORM);
        if (databasePlatform != null) {
            jpaVendorAdapter.setDatabasePlatform(environment.getProperty(PROPERTY_NAME_JDBC_DATABASE_PLATFORM1));
        }

        return jpaVendorAdapter;
    }

    @Bean(name = "jpaPropertyMap")
    public Map<String, Object> jpaPropertyMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("hibernate.jdbc.batch_size", Integer.parseInt(environment.getProperty(PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE)));
        map.put("hibernate.connection.useUnicode",true);
        map.put("hibernate.connection.characterEncoding","UTF-8");
        return map;
    }

 
}