package com.revature;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class exampleConnMain {
	public static void main(String[] args) {	
		Connection singleton;
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("connection.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			singleton = ConnectionUtil.getConnection(prop.getProperty("url"), prop.getProperty("user"), prop.getProperty("password"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		artistDAO artistDAO = new artistDAO(singleton);
		List<exampleArtist> artists = artistDAO.getAll();
		
	}
}
