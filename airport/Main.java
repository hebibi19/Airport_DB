package airport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
	
	// change this to your info
	static final String DB_URL = "jdbc:postgresql://cs1.calstatela.edu/[database]"; // your user here
	static final String USER = "[user]"; // your user here
	static final String PASS = "[password]"; // your password here
	
	
	static final String DisplayAirplanes = "SELECT * FROM Airplane";
	static final String DisplayTests = "SELECT * FROM Test";
	static final String DisplayModels = "SELECT * FROM Model";
	static final String DisplayTraffic = "SELECT * FROM Traffic_Controller";
	static final String DisplayTechnicians = "SELECT * FROM Technician";
	static final String DisplayExperts = "SELECT * FROM Expert_On";

	public static void main(String[] args) {
		
		// connecting to the database
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			Statement stmt = conn.createStatement();) 
		{
			// confirming the connection
			System.out.println("connection made!");
			// calling the initial menu
			initialMenu(stmt);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * Method adds a new airplane to the AIRPLANE table 
	 */
	public static void addPlane(Statement stmt) {
	    try {
	        Scanner input = new Scanner(System.in);

	        // displaying models
	        ResultSet rs = stmt.executeQuery(DisplayModels);

	        HashMap<Integer, String> h = new HashMap<>();
	        int count = 1;

	        System.out.println("Select a model for the airplane: \n");
	        while (rs.next()) {
	            System.out.println("[" + count + "] model number: " + rs.getString("model_no"));
	            h.put(count, rs.getString("model_no"));
	            count++;
	        }

	        int choice = input.nextInt();
	        String model_no = h.get(choice);

	        // prompts menu
	        System.out.println("Enter a new registration number for the airplane: \n");
	        String registration_no = input.next();

	      
	        String addPlane = "INSERT INTO Airplane (registration_no, model_no) VALUES ('" + registration_no + "', '" + model_no + "')";
	        stmt.executeUpdate(addPlane);

	        System.out.println("Airplane with model number: " + model_no + " and registration number: " + registration_no + " was added \n");

	        // Call the initial menu again
	        initialMenu(stmt);

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Method removes chosen Airplane from database
	 */
	public static void removePlane(Statement stmt) {
		// stores the primary key so deletion is easier to accomplish
		HashMap<Integer, String> h = new HashMap<>();
		
		try {
			// display airplanes
			ResultSet rs = stmt.executeQuery(DisplayAirplanes);
			
			int count = 1;
			// display airplanes + add primary key to hash map
			while (rs.next()) {
				System.out.print("\n[" + count + "]");
				System.out.println("\tRegistration Number: " + rs.getString("registration_no"));
				System.out.println("\tModel Number: " + rs.getString("model_no"));
				h.put(count, rs.getString("registration_no"));
				count++;

			}
			// asking which airplane they want to remove (input a number)
			System.out.println("\n Which AIRPLANE would you like to remove?");
			Scanner input = new Scanner(System.in);
			// deletion choice
			int choice = input.nextInt();
			
			// delete chosen plane
			String deletePlane = "DELETE FROM Airplane WHERE registration_no = '" + h.get(choice) + "'";
			stmt.executeUpdate(deletePlane);
			
			// display new result set of airplanes
			ResultSet rs2 = stmt.executeQuery(DisplayAirplanes);
			
			count = 1;
			
			while (rs2.next()) {
				System.out.print("\n[" + count + "]");
				System.out.println("\tRegistration Number: " + rs2.getString("registration_no"));
				System.out.println("\tModel Number: " + rs2.getString("model_no"));
				count++;
			}
			
			// call initial menu again
			initialMenu(stmt);
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void addTest(Statement stmt) {
	    try {
	        Scanner input = new Scanner(System.in);
	        ResultSet rs = stmt.executeQuery(DisplayAirplanes);
	        HashMap<Integer, String> h = new HashMap<>();
	        int count = 1;

	        System.out.println("Select the airplane for the new test: \n");
	        while (rs.next()) {
	            System.out.println("[" + count + "] Registration Number: " + rs.getString("registration_no"));
	            h.put(count, rs.getString("registration_no"));
	            count++;
	        }
	        int choice = input.nextInt();
	        String registration_no = h.get(choice);

	        // query to display all technicians who have tested less than 3 airplanes
	        ResultSet rs2 = stmt.executeQuery("SELECT SSN FROM Technician WHERE SSN NOT IN (SELECT SSN FROM Test GROUP BY SSN HAVING COUNT(*) >= 3)");
	        HashMap<Integer, String> h2 = new HashMap<>();
	        count = 1;

	        // shows list of technicians to select 
	        System.out.println("Please select the technician for the new test: \n");
	        while (rs2.next()) {
	            System.out.println("[" + count + "] SSN: " + rs2.getString("SSN"));
	            h2.put(count, rs2.getString("SSN"));
	            count++;
	        }

	        choice = input.nextInt();
	        String SSN = h2.get(choice);

	        System.out.println("Enter the test date (YYYY-MM-DD): \n");
	        String test_date = input.next();

	
	        System.out.println("Enter number of hours: \n");
	        int hours = input.nextInt();

	        System.out.println("Enter the score: \n");
	        int score = input.nextInt();

	        // query to insert the new test into the TEST table
	        String addTest = "INSERT INTO Test (registration_no, SSN, test_date, hours, score) VALUES ('" + registration_no + "', '" + SSN + "', '" + test_date + "', " + hours + ", " + score + ")";
	        stmt.executeUpdate(addTest);


			//success
	        System.out.println("Test for airplane with registration number " + registration_no + " was added.");

	        initialMenu(stmt);

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void removeTest(Statement stmt) {
		// stores the primary key so deletion is easier to accomplish
		HashMap<Integer, String[]> h = new HashMap<>();
		
		try {
			// display tests
			ResultSet rs = stmt.executeQuery(DisplayTests);
			
			int count = 1;
			// display tests + add primary keys (2 of them) to hash map
			while (rs.next()) {
				System.out.print("\n[" + count + "]");
				System.out.println("\tRegistration Number: " + rs.getString("registration_no"));
				System.out.println("\tSSN: " + rs.getString("ssn"));
				h.put(count, new String[] {rs.getString("registration_no"), rs.getString("ssn")});
				System.out.println("\tDate of Test: " + rs.getDate("test_date"));
				System.out.println("\tHours: " + rs.getInt("hours"));
				System.out.println("\tScore: " + rs.getInt("score"));

				count++;

			}
			// asking which test they want to remove (input a number)
			System.out.println("\n Which TEST would you like to remove?");
			Scanner input = new Scanner(System.in);
			// deletion choice
			int choice = input.nextInt();
			
			// delete chosen test
			String deletePlane = "DELETE FROM Test WHERE registration_no = '" + h.get(choice)[0] + "' AND ssn = '" + h.get(choice)[1] + "'";
			stmt.executeUpdate(deletePlane);
			
			// display new result set of test
			ResultSet rs2 = stmt.executeQuery(DisplayTests);
			
			count = 1;
			
			while (rs2.next()) {
				System.out.print("\n[" + count + "]");
				System.out.println("\tRegistration Number: " + rs2.getString("registration_no"));
				System.out.println("\tSSN: " + rs2.getString("ssn"));
				System.out.println("\tDate of Test: " + rs2.getDate("test_date"));
				System.out.println("\tHours: " + rs2.getInt("hours"));
				System.out.println("\tScore: " + rs2.getInt("score"));

				count++;
			}
			
			// call initial menu again
			initialMenu(stmt);
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void displayAll(Statement stmt) {
		try {
			// display Airplanes
			ResultSet rs = stmt.executeQuery(DisplayAirplanes);
			
			int count = 1;
			
			System.out.println("------------ AIRPLANE TABLE -----------");
			while (rs.next()) {
				System.out.print("\n[" + count + "]");
				System.out.println("\tRegistration Number: " + rs.getString("registration_no"));
				System.out.println("\tModel Number: " + rs.getString("model_no"));
				count++;
			}
			System.out.println("----------------------------------------");

			
			// display Models
			ResultSet rs2 = stmt.executeQuery(DisplayModels);
			
			count = 1;
			
			System.out.println("\n\n------------ MODEL TABLE -----------");
			while (rs2.next()) {
				System.out.print("\n[" + count + "]");
				System.out.println("\tModel Number: " + rs2.getString("model_no"));
				System.out.println("\tCapacity: " + rs2.getInt("capacity"));
				System.out.println("\tWeight: " + rs2.getInt("weight"));
				count++;
			}
			System.out.println("----------------------------------------");

			// display Traffic Controllers
			ResultSet rs3 = stmt.executeQuery(DisplayTraffic);
			
			count = 1;
			
			System.out.println("\n\n------------ TRAFFIC CONTROLLER TABLE -----------");
			while (rs3.next()) {
				System.out.print("\n[" + count + "]");
				System.out.println("\tSSN: " + rs3.getString("ssn"));
				System.out.println("\tUnion ID: " + rs3.getInt("union_id"));
				System.out.println("\tLast Exam Date: " + rs3.getDate("last_exam"));
				count++;
			}
			System.out.println("-------------------------------------------------");
			
			// display Traffic Controllers
			ResultSet rs4 = stmt.executeQuery(DisplayTechnicians);
			
			count = 1;
			
			System.out.println("\n\n---------------- TECHNICIAN TABLE ------------------");
			while (rs4.next()) {
				System.out.print("\n[" + count + "]");
				System.out.println("\tSSN: " + rs4.getString("ssn"));
				System.out.println("\tUnion ID: " + rs4.getInt("union_id"));
				System.out.println("\tFirst Name: " + rs4.getString("fname"));
				System.out.println("\tLast Name: " + rs4.getString("lname"));
				System.out.println("\tAddress: " + rs4.getString("address"));
				System.out.println("\tPhone: " + rs4.getString("phone"));
				System.out.println("\tSalary: " + rs4.getString("salary"));
				count++;
			}
			System.out.println("-------------------------------------------------");
			
			// display Traffic Controllers
			ResultSet rs5 = stmt.executeQuery(DisplayExperts);
			
			count = 1;
			
			System.out.println("\n\n------------- EXPERT TABLE ------------");
			while (rs5.next()) {
				System.out.print("\n[" + count + "]");
				System.out.println("\tSSN: " + rs5.getString("ssn"));
				System.out.println("\tModel Number: " + rs5.getString("model_no"));
				count++;
			}
			System.out.println("---------------------------------------");
			
			// display Traffic Controllers
			ResultSet rs6 = stmt.executeQuery(DisplayTests);
			
			count = 1;
			
			System.out.println("\n\n---------------- TEST TABLE --------------------");
			while (rs6.next()) {
				System.out.print("\n[" + count + "]");
				System.out.println("\tRegistration Number: " + rs6.getString("registration_no"));
				System.out.println("\tSSN: " + rs6.getString("ssn"));
				System.out.println("\tTest Date: " + rs6.getDate("test_date"));
				System.out.println("\tHours: " + rs6.getInt("hours"));
				System.out.println("\tScore: " + rs6.getInt("score"));
				count++;
			}
			System.out.println("-------------------------------------------------");

			
			// call initial menu again
			initialMenu(stmt);
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Initial Menu displays all available options
	 */
	public static void initialMenu(Statement stmt) {
		System.out.println("\nWelcome to Group 16's Airport Database Management System!");
		System.out.println("\nWhat would you like to do?");
		System.out.println("[1] Add an Airplane");
		System.out.println("[2] Remove an Airplane");
		System.out.println("[3] Test an Airplane");
		System.out.println("[4] Remove a Test");
		System.out.println("[5] Display All Information");
		System.out.println("[6] Exit\n");
		
		Scanner input = new Scanner(System.in);
		int choice = input.nextInt();
		
		int cont = 1;
		while (cont == 1) {
			if (choice < 1 || choice > 6) {
				System.out.println("ERROR! Please enter a valid input.");
				choice = input.nextInt();
				cont = 1;
			}
			if (choice == 1) {
				cont = 0;
				addPlane(stmt);
			}
			if (choice == 2) {
				cont = 0;
				removePlane(stmt);
			}
			if (choice == 3) {
				cont = 0;
				addTest(stmt);
			}
			if (choice == 4) {
				cont = 0;
				removeTest(stmt);
			}
			if (choice == 5) {
				cont = 0;
				displayAll(stmt);
			}
			if (choice == 6) {
				System.out.println("program terminated.");
				System.exit(0);
			}
		}
		
	}

}
