package com.epay.epayApp.spring.root;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 * 
 * @author Asad Ali
 *
 */
@Configuration
@EnableTransactionManagement
@ImportResource({"classpath:/com/epay/epayApp/spring/root/application-config.xml"})

public class ApplicationConfig {
	
	
}
