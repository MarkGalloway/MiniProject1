package MiniProjectTests;




import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import java.awt.Container;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

	JPanel panel = new JPanel();

	static String enteredtext = "a";
	String[] arr = new String[2];

	public Main() {
		setSize(800,300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);//Sets if its visible.

		setTitle("MiniProject1");

		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		getContentPane().setBackground(Color.gray);

		springLayout.putConstraint(SpringLayout.NORTH, panel, 10,
				SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, panel, 10,
				SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panel, 46,
				SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panel, 775,
				SpringLayout.WEST, getContentPane());
		getContentPane().add(panel);
		

		JButton btnSignUp = new JButton("Sign Up");
		//add(btnLogin);
				btnSignUp.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						System.out.print("Username:");//what the button says when clicked.
						
						Input.Bufferedreader();
					    
					    System.out.print("Password:");
						
					    Input.Bufferedreader();
					}
					
				});
				
				panel.add(btnSignUp);

		JButton btnLogin = new JButton("Login");
		//add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.print("Username:");//what the button says when clicked.

				Input.Bufferedreader();
				
				System.out.println(enteredtext);
				
				arr[0] = enteredtext;
				
				System.out.print("Password:");

				Input.Bufferedreader();
				
				System.out.println(enteredtext);
				
				arr[1] = enteredtext;
				
				System.out.println( db.verifyUser(arr[0], arr[1]));
				
			}

		});

		panel.add(btnLogin);

		JButton btnSearchAds = new JButton("Search for Ads");
		//add(btnOpen);
		btnSearchAds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.print("Search:");

				Input.Bufferedreader();
				
			}
		});

		panel.add(btnSearchAds);


		JButton btnListAd = new JButton("List an Ad");
		//add(btnOpen);
		btnListAd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.print("Search:");

				Input.Bufferedreader();
			}
		});

		panel.add(btnListAd);

		JButton btnSearchforUser = new JButton("Search for Users");
		//add(btnOpen);
		btnSearchforUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.print("Search for User:");

				Input.Bufferedreader();
			}
		});

		panel.add(btnSearchforUser);

		JButton btnPostanAd = new JButton("Post an Ad");
		//add(btnOpen);
		btnPostanAd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.print("Post an ad:");

				Input.Bufferedreader();
			}
		});

		panel.add(btnPostanAd);

		JButton btnLogout = new JButton("Logout");
		//add(btnOpen);
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.print("Goodbye");

				panel.setVisible(false);
				System.exit(0);
			}
		});

		panel.add(btnLogout);

		panel.revalidate(); //refresh panel
	}
	public static class Input {

		public static void Bufferedreader(){
			BufferedReader brText = new BufferedReader(new InputStreamReader(System.in));{
				try {
					 enteredtext = brText.readLine();
					 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}


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
		db.closeConnection();

		//BEGIN DEBUG
		System.out.println("Connection closed");

		System.out.println("Done Execution. see ya");
		//END DEBUG
	}



}
