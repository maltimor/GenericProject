package es.maltimor.webUtils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class JDBCBridge {
	private DataSource dataSource;
	private static JDBCBridge instance;

	public JDBCBridge(){
		instance = this;
	}
	
	public static Connection getConnection() throws SQLException{
		return instance.dataSource.getConnection();
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}