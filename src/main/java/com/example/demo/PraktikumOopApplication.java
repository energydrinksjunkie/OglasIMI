package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class PraktikumOopApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(PraktikumOopApplication.class, args);
		DataBase DB = new DataBase();
		try {
			DB.kreirajPocetnuBazu();
		}
		catch(SQLException e)
		{
			System.out.println("Baza vec postoji");
		}
	}

}
