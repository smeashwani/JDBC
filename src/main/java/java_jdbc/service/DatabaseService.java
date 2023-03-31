package java_jdbc.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class DatabaseService {
	private Connection con;

	/*
	 * static { try { Class.forName("com.mysql.jdbc.Driver"); } catch
	 * (ClassNotFoundException e) { e.printStackTrace(); } }
	 */

	public void connect() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/myDb", "root", "root");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void save(String name, String pos, int salary) {
		try (Statement stmt = con.createStatement()) {
			String sql = String.format("insert into employees(name,position,salary) values(\"%s\",\"%s\",%d)", name,
					pos, salary);
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void saveByPS(String name, String pos, int salary) {
		String sql = String.format("insert into employees(name,position,salary) values(?,?,?)");
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, pos);
			pstmt.setInt(3, salary);
			pstmt.executeUpdate();
			System.out.println("Successfully save");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int saveByCallabe(String name, String pos, int salary) {
		String preparedSql = "{call insertEmployee(?,?,?,?)}";
		try (CallableStatement cstmt = con.prepareCall(preparedSql)) {
			cstmt.registerOutParameter(1, Types.INTEGER);
			cstmt.setString(2, name);
			cstmt.setString(3, pos);
			cstmt.setDouble(4, salary);
			cstmt.execute();
			return cstmt.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public void list() {
		try (Statement stmt = con.createStatement()) {
			String sql = "select * from employees";
			ResultSet executeQuery = stmt.executeQuery(sql);
			while (executeQuery.next()) {
				System.out.println(executeQuery.getInt(1) + "-" + executeQuery.getString(2) + "-"
						+ executeQuery.getString(3) + executeQuery.getInt(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void list(String name) {
		// --22' OR name !='1
		try (Statement stmt = con.createStatement()) {
			String sql = "select * from employees where name =?";
			System.out.println(sql);
			ResultSet executeQuery = stmt.executeQuery(sql);
			while (executeQuery.next()) {
				System.out.println(executeQuery.getInt(1) + "-" + executeQuery.getString(2) + "-"
						+ executeQuery.getString(3) + executeQuery.getInt(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		Statement stmt;
		try {
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			/** Getting ResultSet */
			ResultSet rs = stmt.executeQuery("select * from employees");
			while (rs.next()) {
				System.out.print("Emp Id is " + rs.getInt(1));
				System.out.println("Emp Name is " + rs.getString(2));
			}
			rs.absolute(1); // Move to 1 row
			rs.updateString(2, "DUCAT_UPDATE"); // modify the second column of first row
			rs.updateRow(); // update it
			rs.beforeFirst(); // move to top of table
			System.out.println("After Updation");
			while (rs.next()) {
				System.out.print("Emp Id is " + rs.getInt(1));
				System.out.println("Emp Name is " + rs.getString(2));
			}
			rs.last(); // move to last row
			rs.deleteRow(); // delete row
			/** Closing the Connection */
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void batchInsert() {
		String query = "insert into employees(name,position,salary) values(?,?,?)";
		long start = System.currentTimeMillis();
		try {
			con.setAutoCommit(false);
			PreparedStatement ps = con.prepareStatement(query);
			for (int i = 0; i <= 10_000; i++) {
				ps.setString(1, "DUCAT" + i);
				ps.setString(2, "DEV");
				ps.setInt(3, 50_000);
				ps.addBatch();
				if (i % 1_000 == 0) {
					ps.executeBatch();
					con.commit();
				}else {
					con.setAutoCommit(false);
				}
			}
		}  catch (SQLException e) {
			e.printStackTrace();
		}finally {
			System.out.println("Time Taken=" + (System.currentTimeMillis() - start));
		}
	}
	
	public void getDatabaseInfo() {
		try {
			DatabaseMetaData dbmd = con.getMetaData();
			ResultSet tablesResultSet = dbmd.getTables(null, null, "%", null);
			while (tablesResultSet.next()) {
			  System.out.println(tablesResultSet.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void saveImage() {
		try {
			//CREATE TABLE  IMGTABLE(NAME TEXT,PHOTO BLOB)
			PreparedStatement ps=con.prepareStatement("insert into imgtable values(?,?)");
			ps.setString(1,"JAVA");
			ps.setBinaryStream(2, new FileInputStream("jdbcImage.jpeg"));
			ps.executeUpdate();
			System.out.println("Successfully save image");
		} catch (SQLException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void getImage() {
		try {
			//CREATE TABLE  IMGTABLE(NAME TEXT,PHOTO BLOB)
			PreparedStatement ps=con.prepareStatement("select * from imgtable");
			ResultSet rs=ps.executeQuery();  
			if(rs.next()){//now on 1st row  
				Blob b=rs.getBlob(2);//2 means 2nd column data  
				byte barr[]=b.getBytes(1,(int)b.length());//1 means first image  
				FileOutputStream fout=new FileOutputStream("outputImage.jpg");  
				fout.write(barr);
			}
			System.out.println("Successfully save image");
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
	
}
