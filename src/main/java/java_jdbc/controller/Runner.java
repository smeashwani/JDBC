package java_jdbc.controller;

import java_jdbc.service.DatabaseService;

public class Runner {
	public static void main(String[] args) {
		DatabaseService database = new DatabaseService();
		database.connect();
		database.save("ducat1", "java", 20000);
		System.out.println("Successfully save");
		database.list();	
	}
}
