package com.cairone.odataexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.cairone.odataexample.ctrls.ODataController;

@SpringBootApplication
public class OlingoOdataExample extends SpringBootServletInitializer
{
    public static void main( String[] args ) {
        SpringApplication.run(OlingoOdataExample.class, args);
    }
    
    @Autowired ODataController dispatcherServlet = null;
    
    @Bean
    public ServletRegistrationBean dispatcherServletRegistration() {
    	ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet, "/odata/appexample.svc/*");
    	return registration;
    }
    
  
    // *** SERA NECESARIO ????
    /*
    @Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    	return builder.sources(OlingoOdataExample.class);
	}
*/
}
