package java_jdbc.controller;

import java.util.Scanner;

import java_jdbc.service.DatabaseService;

public class Runner {
	//22' OR name !='1
	
	public static void main(String[] args) {
		DatabaseService database = new DatabaseService();	
		database.connect();
		//database.save("ducat1", "java", 20000);
		//database.saveByPS("ducat5", "java", 20000);
		//System.out.println("Successfully save "+ database.saveByCallabe("ducat7", "java", 20000));
//		System.out.println("Enter name");
//		Scanner scan = new Scanner(System.in);
//		String name = scan.nextLine();
//		database.batchInsert();
//		database.list();
		database.getDatabaseInfo();
		//database.update();
		
	}
}
