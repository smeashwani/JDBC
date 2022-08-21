package java_jdbc.service;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseService {
	private Connection con;
	
	public void connect() {
		try { 
		 con = DriverManager.getConnection("jdbc:mysql://localhost:3306/myDb", "root", "root"); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void save(String name, String pos, int salary) {
		try (Statement stmt = con.createStatement()) {
			String sql =  String.format("insert into myDb.employees(name,position,salary) values(\"%s\",\"%s\",%d)", name, pos, salary);
			stmt.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void list() {
		try (Statement stmt = con.createStatement()) {
			String sql =  "select * from myDb.employees";
			ResultSet executeQuery = stmt.executeQuery(sql);
			while(executeQuery.next()) {
				System.out.println(executeQuery.getInt(1) +"-"+ executeQuery.getString(2) +"-"+ executeQuery.getString(3) +  executeQuery.getInt(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
