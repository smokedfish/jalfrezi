package org.jalfrezi.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfiguration {

	@Bean
	public Connection connection(@Value("${derby.connectionUrl}") String derbyUrl) throws SQLException, ClassNotFoundException {
		return DriverManager.getConnection(derbyUrl);
	}
}
