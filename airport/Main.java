package airport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
	
	// i literally cannot figure out how to connect to the database im :sobbing:
	
	static final String DB_URL = "jdbc:postgresql://cs1.calstatela.edu/cs4222f23hp39";
	static final String USER = "cs4222f23hp39";
	static final String PASS = "rW2MPLBR";

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
	
	
	public static void addPlane(Statement stmt) {
		
		
	}
	
	public static void removePlane(Statement stmt) {
		
	}
	
	public static void addTest(Statement stmt) {
		
	}
	
	public static void removeTest(Statement stmt) {
		
	}
	
	public static void displayAll(Statement stmt) {
		
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
