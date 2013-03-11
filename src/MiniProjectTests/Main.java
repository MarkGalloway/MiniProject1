package MiniProjectTests;




import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import MiniProject1.DBHandler;



/*
 * Comment me!
 */

public class Main extends JFrame{

	//private static String jdbcURL = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";  //this is to connect from school
	//FIX
	private static String jdbcURL = "jdbc:oracle:thin:@localhost:1525:CRS"; //I port forwarded to make this work from my 1525 to gwynne 1521    
	private static String jdbcUserName = "mgallowa"; //Hard coded, make dynamic before hand-in
	private static String jdbcPassword = "!MG~26^rx"; //Hard coded, make dynamic before hand-in
	//END FIX    
	private static DBHandler db = null;


	public static void main(String[] args) {


		//Connect to DB
		db = new DBHandler(jdbcURL, jdbcUserName, jdbcPassword);
		db.openConnection();

		Main mymain = new Main();
		//BEGIN DEBUG
		System.out.println("Connection successful");
		//END DEBUG

		//drop tables
		//db.dropTables(); //works

		//create tables
		//db.createTables(); //works




		//Interact with user (while loop ?)

		// THE LOGIN SCREEN
		//The first screen should provide options for both registered and unregistered users. There must be also an option to exit the program.

		//String username = ""; //read this from input
		//String password = ""; //read this from input


		//                Registered users should be able to login using a valid email and password, respectively referred to as email and pass in table users
		//                    After a registered user signs in, the system should list all the reviews written for the user since his/her last login, 
		//                    ordered based on date with more recent reviews listed first. The list would include for each review, the date, the rating 
		//                    and at most 40 characters of the review text. If there are more than 3 such reviews, only 3 would be shown and the user 
		//                    would be given an option to see more but again 3 at a time. If a review text is longer than 40 characters, the user should 
		//                    be able to select the review and see the full text.

		//list the reviews
		//db.listReviews(username); // not implemented yet, but will query and print the above reviews
		//NEED FUNCTION: update user last login with SYSDATE

		//           Unregistered users should be able to sign up by providing a name, email, and password. 


		//           After a successful login or signup, users 
		//           should be able to perform the subsequent operations (possibly chosen from a menu) as discussed next.

		//AFTER LOGIN
		//search for ads

		//List own ads

		//Search for Users

		//Post an Ad

		//Logout



		//End While Loop


		//Disconnect from DB
		//db.closeConnection();

		//BEGIN DEBUG
		//System.out.println("Connection closed");

		System.out.println("Done Execution. see ya");
		//END DEBUG
	}



}
