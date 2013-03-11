package MiniProject1;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * This is the main UI interaction class for MiniProject1. It contains the main
 * loop which controls execution and interaction with the user. it also has many user
 * interaction elicitation methods. Furthermore, it also keeps track of databse usernames/password
 * and the email of the user.
 *
 * Calls methods in DBHandler in order to access the database for based on user interactions.
 */
public class UI {
    private String jdbcURL = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
    private String jdbcUserName;
    private String jdbcPassword;
    private boolean loggedIn = false;
    private static String email;
    private static Scanner in = null;

    /*
     * Empty private constructor
     */
    private UI(){

    }

    /*
     * Launches the main dialog execution of the text based UI
     */
    public static void main(String[] args) {
        /*
         * variable declarations
         */
        //for testing return values for errors
        boolean rval;
        //A handle for our input stream
        in = new Scanner(System.in);
        //A handle for our UI functions and variables
        UI ui = new UI();

        /*
         * Oracle db connections
         */
        System.out.print("Enter your Orcale username: ");
        ui.setJdbcUserName(in.nextLine());
        System.out.print("Enter your Oracle password: ");
        ui.setJdbcPassword(in.nextLine());
        System.out.println("Thanks, connecting you to the database now.");

        //get a DB Handler object so we can talk to the database
        DBHandler db = new DBHandler(ui.jdbcURL, ui.getJdbcUserName(), ui.getJdbcPassword());

        //Connect to the Database
        rval = db.openConnection();
        if(rval) {
            System.out.println("Connection Successful! Switching to Ujiji mode...");
            System.out.println();
            System.out.println();
        } else {
            System.err.println("Error: Failed to connect to database. Check your username/password, then restart the program");
            System.exit(0);
        }

        //Welcome message
        System.out.println("Welcome to Ujiji!");

        /*
         * Display Login Screen
         */
        while(ui.isLoggedIn() == false) {
            //print the login menu
            ui.printLoginMenu();
            //read in response
            String response = in.nextLine();
            try {
                int index = Integer.parseInt(response);
                //Launch the Function based on input
                switch(index) {
                    case 1: ui.logIn(db);
                            break;
                    case 2: ui.createAccount(db);
                            break;
                    case 3: exit(db);
                            break;
                    default: System.out.print("Input not recognized. Try again."); //user entered a bad number
                            continue;
                }
            } catch (NumberFormatException nFE) {
                //User entered a non-Integer
                System.out.print("Input not recognized. Try again");
                continue;
            }
        }

        /*
         * List the reviews since last login.
         */
        ui.displayReviews(db.getReviews(getEmail()));

        /*
         * Display Main Menu Screen
         */
        while(true) {
            //print the main menu
            ui.printMainMenu();
            //read in response
            String response = in.nextLine();
            try {
                int index = Integer.parseInt(response);
                //Launch the Function based on input
                switch(index) {
                    case 1: ui.searchForAds(db, ui); //Search for ads funtionality
                            break;
                    case 2: ui.listOwnAds(db, ui); //List my own ads functionality
                            break;
                    case 3: ui.userSearch(db, ui); //Search for users functionality
                            break;
                    case 4: ui.postAd(db, ui); //Post an ad functionality
                            break;
                    case 5: db.updateLoginDate(getEmail()); //Logout functionality
                            exit(db);
                            break;
                    default: System.out.print("Input not recognized. Try again."); //user entered a bad number
                            continue;
                }
            } catch (NumberFormatException nFE) {
                //User entered a non-Integer
                System.out.print("Input not recognized. Try again");
                continue;
            }
        }
    }

    /*
     * Takes as argument an Ad, for which the user is prompted
     * to select one of the listed offers from the database. The
     * promotion is then added to the Ad and inserted into the
     * purchases table in the database.
     */
    private void promoteAd(DBHandler db, Ad ad) {
        if(db.hasPromotion(ad)) {
            System.out.println("Sorry, this ad already has a promotion. Cannot promote further");
            return;
        }

        //Display the offers
        System.out.println();
        System.out.println("The available offers are as follows:");
        System.out.println("-----------------------------------------");

        ArrayList<Offer> offers = db.getOffers();
        for(int i = 1; i <= offers.size()  ;i++) {
            System.out.println( i +") " + offers.get(i-1));
        }
        //get the offer
        Offer offer = null;
        while(offer == null) {
            System.out.println();
            System.out.print("Enter the number of the offer you want: ");
            //read in the response
            String response = in.nextLine();
            try {
                int index = Integer.parseInt(response);
                if(index < 1 || index > offers.size()) {
                    //out of bounds number
                    System.out.print("Incorrect number. Try again");
                    continue;
                } else {
                    offer = offers.get(index - 1);
                }
            }  catch (NumberFormatException nFE) {
                //User entered a non-Integer
                System.out.print("Input not recognized. Try again");
                continue;
            }
        }

        if (db.promoteAd(ad, offer) ){
            System.out.println("promotion added Successfully");
        } else {
            System.out.println("Some sort of database error added the promotion");
        }
    }

    /*
     * Takes an Ad object as an argument. Calls DBHandler's
     * deleteAd() function to remove the add from the database
     */
    private void deleteAd(DBHandler db, Ad ad) {
       //call DBHandler to delete the ad
	   boolean rval = db.deleteAd(ad);
	   //return value check
       if(rval) {
           System.out.println("Add successfully removed.");
       } else {
           System.out.println("Database error removing add");
       }
    }

    /*
     * Takes a selected Ad object and prompts the user to choose
     * to either Delete the ad, promote the ad, or return to the
     * main menu.
     */
    private void manageAd(DBHandler db, UI ui, Ad ad) {
        while(true) {
            //display the menu
             ui.printManageAdMenu();
             String response = in.nextLine();
             try {
                 int index = Integer.parseInt(response);
                 //Launch the Function based on input
                 switch(index) {
                     case 1: ui.deleteAd(db, ad); //launch delete ad UI
                             return;
                     case 2: ui.promoteAd(db, ad); //launch promote ad UI
                             return;
                     case 3: return; //return to main menu
                     default: System.out.print("Input not recognized. Try again."); //user entered a bad number
                             continue;
                 }
             } catch (NumberFormatException nFE) {
                 //User entered a non-Integer
                 System.out.print("Input not recognized. Try again");
                 continue;
             }
        }
    }

    /*
     * Lists the users own ads, displaying 5 initially,
     * asking whether the user wants to display 5 more ads.
     * Gives users the option to promote or delete their ads.
     */
    private void listOwnAds(DBHandler db, UI ui) {
        //get the ads
        ArrayList<Ad> ads = db.getOwnAds(UI.getEmail());

		//DBHandler returned an SQL error
        if(ads == null) {
            System.out.println("SQL error with the query.");
            return;
        }
		//user has no ads
        if(ads.isEmpty()) {
            System.out.println("No ads were found.");
            return;
        }

		//Display the ads
        System.out.println();
        int count = 1;
        if(ads.size() > 0){
            System.out.println("Found " + ads.size() + " Ads. Displaying search results...");
        }

        while(count <= ads.size()) {

            System.out.println( count + ")" + ads.get(count-1).toStringListOwnAds());
            count++;
            if(count <= ads.size()) {
                System.out.println( count + ")" + ads.get(count-1).toStringListOwnAds());
                count++;
            }
            if(count <= ads.size()) {
                System.out.println( count + ")" + ads.get(count-1).toStringListOwnAds());
                count++;
            }
            if(count <= ads.size()) {
                System.out.println( count + ")" + ads.get(count-1).toStringListOwnAds());
                count++;
            }
            if(count <= ads.size()) {
                System.out.println( count + ")" + ads.get(count-1).toStringListOwnAds());
                count++;
            }
			//query to display more ads
            if(count <= ads.size()) {
                System.out.print("Input S to stop displaying ads, or press Enter for more:");
                //get the response
                String response = in.nextLine();
                if(response.trim().equalsIgnoreCase("S")) {
                    break;
                }
            }
        }
		//ask the user if they want to promote or delete the ad
        if(ads.size() > 0) {
            while(true) {
                System.out.print("If you would like to delete or promote of any of the above ads, " +
                                   "input its number, or press Enter to return to main menu: "
                                  );
                //get the response
                String response = in.nextLine();
                try {
                    int index = Integer.parseInt(response);
                    manageAd(db, ui, ads.get(index-1)); //launch the add management UI
                    return;
                } catch (NumberFormatException nFE) {
                    if(response.trim().equalsIgnoreCase("")) {
                        return; //user wants to exit()
                    } else {
                        System.out.println("Not a valid Ad Number. Try again.");
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Not a valid Ad Number. Try again.");
                    continue;
                }
            }
        }
    }

    /*
     * Takes an email argument parameter, calls DBHandler to get the
     * reviews from the database that correspond to the email as a reviewee.
     * Prints out the review texts of the reviews
     */
    private void printUsersReviews(DBHandler db, UI ui, String email) {
		//Esthetics
        System.out.println();
		//get the reviews of the user
        ArrayList<Review> reviews = db.getUserReviews(email);
        if(reviews == null) {
			//error check
            System.out.println("Had some sort of SQL error with the database");
            return;
        }
        else if(reviews.isEmpty()) {
			//User has no reviews
            System.out.println("That user has no reviews");
            return;
        }
		//print out the reviews
        System.out.println("Here is " + email +" review texts:");
        for(Review r : reviews) {
            System.out.println(r.toStringFullText());
        }
    }

    /*
	 * UI to write a review for another user
     * Takes as argument the email of a reviewee. Elicits a Rating
     * and optional review Text from the user. Creates a Review
     * object and send the review object to DBHandler to be stored into
     * the database.
     */
    private void writeUserReview(DBHandler db, UI ui, String email) {
		//get the rating and text
        Integer rating = elicitRating();
        String text = elicitText();

		//call DBHandler to add the review
        boolean rval = db.addReview(new Review(rating, text, UI.email, email));
		//check the return value
        if(!rval) {
            System.out.println("SQL error when inserting review");
        } else {
            System.out.println("Review entered successfully.");
        }
    }

    /*
     * Takes an ArrayList of Users objects which have been returned
     * from a search for users, and prints out the toString representation of
     * each user. Allows System User to select one of these users and pass the
     * User object to other functions to either write a review, or view the users
     * reviews, or go back to main menu.
     */
    private void displayUsers(DBHandler db, UI ui, ArrayList<User> users) {

        //Shouldn't happen, but sanity check
        if( users.isEmpty()) {
            System.out.println("Sorry, no users found");
            return;
        }
		//print out the users
        System.out.println("Here are the found users: ");
        for(int i = 1; i <= users.size() ;i++) {
            System.out.println( i +") " + users.get(i-1).toString());
        }

        //get the user
        User usr = null;
        while(usr == null) {
            System.out.println();
            System.out.print("Enter the number of the user you want to select: ");
            //read in the response
            String response = in.nextLine();
            try {
                int index = Integer.parseInt(response);
                if(index < 1 || index > users.size()) {
                    //out of bounds number
                    System.out.print("Incorrect number. Try again");
                    continue;
                } else {
					//get the user, ends the loop
                    usr = users.get(index - 1);
                }
            }  catch (NumberFormatException nFE) {
                //User entered a non-Integer
                System.out.print("Input not recognized. Try again");
                continue;
            }
        }
		//Elicit a selection from the user
        while(true) {
           //display the menu
            ui.printUserDisplayMenu();
            String response = in.nextLine();
            try {
                int index = Integer.parseInt(response);
                //Launch the Function based on input
                switch(index) {
                    case 1: ui.printUsersReviews(db, ui, usr.getEmail()); //UI to print the reviews of the user
                            continue;
                    case 2: ui.writeUserReview(db, ui, usr.getEmail()); // UI to write a review for the user
                            continue;
                    case 3: return;
                    default: System.out.print("Input not recognized. Try again."); //user entered a bad number
                            continue;
                }
            } catch (NumberFormatException nFE) {
                //User entered a non-Integer
                System.out.print("Input not recognized. Try again");
                continue;
            }
        }

    }

    /*
     * Elicits an email from the console. Sends this email through the
     * DBHandler in order to select the User. The user is then displayed,
     * if exists.
     */
    private void searchForUsersByEmail(DBHandler db, UI ui) {
        //Esthetics
		System.out.println();
		//get the email
        String email = elicitUserEmail();
		//search for the user
        User user = db.searchForUserByEmail(email);
        if (user != null) {
            ArrayList<User> users = new ArrayList<User>();
            users.add(user);
            ui.displayUsers(db, ui, users); // UI for dislplaying the user
            return;
        } else {
            System.out.println("Sorry, no user found with that email.");
            return;
        }
    }

    /*
     * Elicits the user to enter a Name, and then calls the DB handler
     * to find the users with the same name in the database. Calls displayUsers
     * to print out the Users returned by the DB Handler
     */
    private void searchForUsersByName(DBHandler db, UI ui) {
        //Esthetic padding
        System.out.println();
        //get a user name
        String name = elicitUserName();
        //call the DB handler to get the Users
        ArrayList<User> users = db.searchForUserByName(name);
        if(users == null) {
            //Error, shouldn't happen. Sanity check.
            System.out.println("searchForUsersByName: some sort of SQL error");
        } else if(users.isEmpty()) {
            System.out.println("Sorry, no users found with that name.");
        } else {
            //Pass the Users along to be displayed
            ui.displayUsers(db, ui, users);
        }
        return;
    }

    /*
     * Displays the User search Functionality Menu and launches
     * either email search or name search based on user input.
     */
    private void userSearch(DBHandler db, UI ui) {
        //elicit the search type
        while(true) {
            //print the menu
            ui.printUserSearchMenu();
            String response = in.nextLine();
            try {
                int index = Integer.parseInt(response);
                //Launch the Function based on input
                switch(index) {
                    case 1: ui.searchForUsersByEmail(db, ui); //launch search by email
                            return;
                    case 2: ui.searchForUsersByName(db, ui); //launch search by name
                            return;
                    case 3: return;
                    default: System.out.print("Input not recognized. Try again."); //user entered a bad number
                            continue;
                }
            } catch (NumberFormatException nFE) {
                //User entered a non-Integer
                System.out.print("Input not recognized. Try again");
                continue;
            }
        }
    }

    /*
     * Elicits input from the user in order to create
     * an Ad with the elicited info. Calls the DB handler
     * to insert the Ad into the database as a row in the
     * ads table.
     */
    private void postAd(DBHandler db, UI ui) {

        //Elicit the Ad type
        String atype = null;
        while(atype == null) {
            ui.printAdTypes();

            String response = in.nextLine();
            try {
                int index = Integer.parseInt(response);
                //Launch the Function based on input
                switch(index) {
                    case 1: atype = "W";
                            break;
                    case 2: atype = "S";
                            break;
                    case 3: return;
                    default: System.out.print("Input not recognized. Try again."); //user entered a bad number
                            continue;
                }
            } catch (NumberFormatException nFE) {
                //User entered a non-Integer
                System.out.print("Input not recognized. Try again");
                continue;
            }
        }

        //Display the categories
        System.out.println();
        System.out.println("The available categories are as follows:");
        System.out.println("-----------------------------------------");

        ArrayList<String> cats = db.getCategories();
        for(int i = 1; i <= cats.size()  ;i++) {
            System.out.println( i +") " + cats.get(i-1));
        }
        //get the category
        String cat = null;
        while(cat == null) {
            System.out.println();
            System.out.print("Enter the number of the category you want: ");
            //read in the response
            String response = in.nextLine();
            try {
                int index = Integer.parseInt(response);
                if(index < 1 || index > cats.size()) {
                    //out of bounds number
                    System.out.print("Incorrect number. Try again");
                    continue;
                } else {
                    cat = cats.get(index - 1);
                }

            }  catch (NumberFormatException nFE) {
                //User entered a non-Integer
                System.out.print("Input not recognized. Try again");
                continue;
            }
        }
        //Elicit required information
        String title = elicitTitle();
        Integer price = elicitPrice();
        String descr = elicitDescr();
        String location = elicitLocation();
        //build the new Ad type
        Ad ad = new Ad(atype, title, price, descr, location, cat);
        //call the DB handler to insert the ad.
        boolean rval = db.insertAd(ad, email);
        if(!rval) {
            System.out.println("Failed to insert Ad");
        } else {
            System.out.println("Add entered successfully!");
        }

    }

    /*
     * Displays ads that are found in searchForAds. The ads are
     * displayed 5 at a time until exhausted. Allows user to select
     * a specific ad by number and recieve more details.
     */
    private void displaySearchedAds(ArrayList<Ad> ads) {
        //Esthetic padding
        System.out.println();
        //ads have been found
        if(ads.size() > 0){
            System.out.println("Found " + ads.size() + " Ads. Displaying search results...");
        }
        //loop through the ads displaying 5 at a time.
        int count = 1;
        while(count <= ads.size()) {

            System.out.println( count + ")" + ads.get(count-1).toStringKeywordSearch());
            count++;
            if(count <= ads.size()) {
                System.out.println( count + ")" + ads.get(count-1).toStringKeywordSearch());
                count++;
            }
            if(count <= ads.size()) {
                System.out.println( count + ")" + ads.get(count-1).toStringKeywordSearch());
                count++;
            }
            if(count <= ads.size()) {
                System.out.println( count + ")" + ads.get(count-1).toStringKeywordSearch());
                count++;
            }
            if(count <= ads.size()) {
                System.out.println( count + ")" + ads.get(count-1).toStringKeywordSearch());
                count++;
            }

            if(count <= ads.size()) {
                //see if the user wants to continue seeing ads.
                System.out.print("Input S to stop displaying ads, or press Enter for more:");
                //get the response
                String response = in.nextLine();
                if(response.trim().equalsIgnoreCase("S")) {
                    break;
                }
            }
        }
        if(ads.size() > 0) {
            //See if the user would like to see more details of an ad, by printed number.
            while(true) {
                System.out.println();
                System.out.print("If you would like to see more details of any of the above ads, " +
                                   "input its number, or press Enter to continue: "
                                  );
                //get the response
                String response = in.nextLine();
                try {
                    //read the index and print the extra ad info
                    int index = Integer.parseInt(response);
                    System.out.println(index + ")" + ads.get(index-1).toStringKeywordSearchAdvanced());
                } catch (NumberFormatException nFE) {
                    //user wants to return to main menu
                    if(response.trim().equalsIgnoreCase("")) {
                        break;
                    } else {
                        continue;
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Not a valid Ad Number. Try again.");
                    continue;
                }
            }
        }
    }

    /*
     * Allows users to enter keywords seperated by
     * whitespace. Calls the DB Handler to query the database for
     * Ad titles or descriptions matching the keywords.
     */
    private void searchForAds(DBHandler db, UI ui) {
        ArrayList<String> keywords = new ArrayList<String>();
        System.out.println();
        System.out.println("Ads can be searched for keyword in their titles or description.");
        System.out.print("Enter each search keyword seperated by whitespace, then press Enter when done: ");

        for( String s : in.nextLine().split("\\s+")) {
            keywords.add(s);
        }
        ArrayList<Ad> ads = db.keywordSearch(keywords);
        if(ads == null) {
            System.out.print("SQL error happened");
        } else if(ads.isEmpty()) {
            System.out.println("No ads matched those keywords.");
            return;
        } else {
            ui.displaySearchedAds(ads);
        }
        return;
    }

    /*
     * Prints the Ad type Selection menu.
     */
    private void printAdTypes(){
        System.out.println();
        System.out.println("Available Ad types:");
        System.out.println("----------------------");
        System.out.println("1) Wanted");
        System.out.println("2) For Sale");
        System.out.println("3) Go back to main menu");
        System.out.print("Enter the number corresponding to the Ad type you want: ");
    }

    /*
     * Prints the ad management menu.
     */
    private void printManageAdMenu() {
        System.out.println();
        System.out.println("Manage Ad Menu:");
        System.out.println("------------------");
        System.out.println("1) Delete the Ad");
        System.out.println("2) Promote the Ad");
        System.out.println("3) Return to Main Menu");
        System.out.print("Enter the corresponding number for your choice (1-3): ");
    }

    /*
     * Prints the user display menu.
     */
    private void printUserDisplayMenu() {
        System.out.println();
        System.out.println("User Display Menu:");
        System.out.println("------------------");
        System.out.println("1) Read the Users' Reviews");
        System.out.println("2) Write a Review for the User");
        System.out.println("3) Return to Main Menu");
        System.out.print("Enter the corresponding number for your choice (1-3): ");
    }

    /*
     * Prints the user search root menu.
     */
    private void printUserSearchMenu() {
        System.out.println();
        System.out.println("Search Menu:");
        System.out.println("------------------");
        System.out.println("1) Search By Email");
        System.out.println("2) Search By Name");
        System.out.println("3) Return to Main Menu");
        System.out.print("Enter the corresponding number for your choice (1-3): ");
    }

    /*
     * Prints the Main root menu for System Functions.
     */
    private void printMainMenu() {
        System.out.println();
        System.out.println("Main Menu:");
        System.out.println("------------------");
        System.out.println("1) Search for Ads");
        System.out.println("2) List my own Ads");
        System.out.println("3) Search for Users");
        System.out.println("4) Post a new Ad");
        System.out.println("5) Logout");
        System.out.print("Enter the corresponding number for your choice (1-5): ");
    }

    /*
     * Prints the Login root menu.
     */
    private void printLoginMenu() {
        System.out.println();
        System.out.println("Login Menu:");
        System.out.println("------------------");
        System.out.println("1) I want to Login");
        System.out.println("2) I want to create an account");
        System.out.println("3) Exit the program");
        System.out.print("Enter the corresponding number for your choice (1-3): ");
    }

    /*
     * Executes the display reviews portion of the UI interaction.
     * Takes as argument an ArrayList of reviews to display to the
     * user.
     */
    private void displayReviews(ArrayList<Review> reviews) {
        //Esthetic padding
        System.out.println();
        int count = 1;
        if(reviews.size() > 0){
            //there are reviews to display
            System.out.println("Displaying new reviews since last login...");
        }
        //print ou the reviews
        while(count <= reviews.size()) {
            //display the reviews in pairs of 3
            System.out.println( count + ")" + reviews.get(count-1).toStringListing());
            count++;
            if(count <= reviews.size()) {
                System.out.println( count + ")" + reviews.get(count-1).toStringListing());
                count++;
            }
            if(count <= reviews.size()) {
                System.out.println( count + ")" + reviews.get(count-1).toStringListing());
                count++;
            }

            if(count <= reviews.size()) {
                System.out.print("Input S to stop displaying reviews, or press Enter for more:");
                //get the response
                String response = in.nextLine();
                if(response.trim().equalsIgnoreCase("S")) {
                    //stop displaying reviews
                    break;
                }
            }
        }
        if(reviews.size() > 0) {
            //see if user wants to see the rest of reviews
            while(true) {
                System.out.print("If you would like to see more text of any of the above reviews " +
                                   "Enter its number, or E to continue: "
                                  );
                //get the response
                String response = in.nextLine();
                try {
                    //print out the full review text.
                    int index = Integer.parseInt(response);
                    System.out.println(index + ")" + reviews.get(index-1).toStringFullText());
                } catch (NumberFormatException nFE) {
                    if(response.trim().equalsIgnoreCase("E")) {
                        //user wants to exit
                        break;
                    } else {
                        continue;
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Not a valid Review Number. Try again.");
                    continue;
                }
            }
        } else {
            //there arent any new reviews
            System.out.println("Sorry, no new reviews since last login.");
        }
    }

    /*
     * Elicits an email and password from console. Checks with the
     * DBHandler to see if the user name and password are correct.
     * If so, logs the user in.
     */
    private void logIn(DBHandler db) {
        boolean rval;
        //prompt for username
        System.out.print("Enter your email: ");
        //get the username
        setEmail(Utils.stringChop(in.nextLine().trim(), 20));

        //prompt for password
        System.out.print("Now enter your password: ");
        //get the password
        String password = Utils.stringChop(in.nextLine().trim(), 20);

        //Ask the DB Handler to verify the username/password
        rval = db.verifyUser(getEmail(), password);
        if (!rval) {
            System.out.println("Couldn't find that email/password in the database. Lets start over.");
            return;
        } else {
            System.out.println("You are now logged in.");
            //log the user in
            setLoggedIn(true);
            return;
        }
    }

    /*
     * Elicits an email, name, and password from the console.
     * Passes these onto the DBHandler to add a user to the database.
     * Logs the user in on success.
     */
    private void createAccount(DBHandler db) {
        //get the email and set it globally
        setEmail(elicitEmail(db));
        //get the Name
        String name = elicitName();
        //get the password
        String password = elicitPassword();
        //get the DBHandler to add the user
        if(!db.addNewUser(getEmail(), name, password)) {
            System.err.println("Some sort of database error.");
            exit(db);
        }
        System.out.println("You're account is created and you are now logged in.");
        //Log the user in
        setLoggedIn(true);
    }

    /*
     * Elicits and returns a 4 char max password from the console.
     * Does not accept empty string.
     */
    public static String elicitPassword() {
        String response;
        while(true) {
            System.out.print("Choose a Password (4 chars max): ");
            response = in.nextLine().trim();
            if(response.length() > 4) {
                System.out.println("That's longer than 4 chars!");
            } else if(response.equalsIgnoreCase("")) {
                System.out.println("field cannot be empty!");
            } else {
                break;
            }
        }
        return response;
    }

    /*
     * Elicits an integer representing a rating.
     * The value must be between 1 and 5.
     * Does not accept empty string nor a negative number.
     */
    public static Integer elicitRating() {
        String response;
        Integer rval = null;
        while(rval == null) {
            System.out.print("Enter an Rating (1-5): ");
            response = in.nextLine().trim();
            try {
                int index = Integer.parseInt(response);
                if(index < 1 || index > 5) {
                    System.out.print("Input should be between 1 and 5. Try again");
                    continue;
                } else {
                    rval = index;
                }
            }  catch (NumberFormatException nFE) {
                //User entered a non-Integer
                System.out.print("Input not recognized. Try again");
                continue;
            }
        }
        return rval;
    }

    /*
     * Elicits an integer representing Price from the console.
     * Does not accept empty string nor a negative number.
     */
    public static Integer elicitPrice() {
        String response;
        Integer rval = null;
        while(rval == null) {
            System.out.print("Enter an Price (Integer): ");
            response = in.nextLine().trim();
            try {
                int index = Integer.parseInt(response);
                if(index >= 0) {
                    rval = index;
                } else {
                    System.out.println("Price cannot be negative");
                    continue;
                }
            }  catch (NumberFormatException nFE) {
                //User entered a non-Integer
                System.out.print("Input not recognized. Try again");
                continue;
            }
        }
        return rval;
    }

    /*
     * Elicits and returns a 15 char max location from the console.
     * Does not accept empty string.
     */
    public static String elicitLocation() {
        String response;
        while(true) {
            System.out.print("Enter your Location (15 chars max): ");
            response = in.nextLine().trim();
            if(response.length() > 15) {
                System.out.println("That's longer than 15 chars!");
            } else if(response.equalsIgnoreCase("")) {
                System.out.println("field cannot be empty!");
            } else {
                break;
            }
        }
        return response;
    }

    /*
     * Elicits and returns a 80 char max review text from the console.
     * Accepts empty string.
     */
    public static String elicitText() {
        String response;
        while(true) {
            System.out.print("Enter the review text (80 chars max): ");
            response = in.nextLine().trim();
            if(response.length() > 80) {
                System.out.println("That's longer than 80 chars!");
            } else {
                break;
            }
        }
        return response;
    }

    /*
     * Elicits and returns a 50 char max descr from the console.
     * Does not accept empty string.
     */
    public static String elicitDescr() {
        String response;
        while(true) {
            System.out.print("Enter the description (50 chars max): ");
            response = in.nextLine().trim();
            if(response.length() > 50) {
                System.out.println("That's longer than 50 chars!");
            } else if(response.equalsIgnoreCase("")) {
                System.out.println("field cannot be empty!");
            } else {
                break;
            }
        }
        return response;
    }

    /*
     * Elicits and returns a 20 char max title from the console.
     * Does not accept empty string.
     */
    public static String elicitTitle() {
        String response;
        while(true) {
            System.out.print("Enter the title (20 chars max): ");
            response = in.nextLine().trim();
            if(response.length() > 20) {
                System.out.println("That's longer than 20 chars!");
            } else if(response.equalsIgnoreCase("")) {
                System.out.println("field cannot be empty!");
            } else {
                break;
            }
        }
        return response;
    }

    /*
     * Elicits and returns a 20 char max name from the console.
     * Does not accept empty string.
     */
    public static String elicitUserName() {
        String response;
        while(true) {
            System.out.print("Enter the search name (20 chars max): ");
            response = in.nextLine().trim();
            if(response.length() > 20) {
                System.out.println("That's longer than 20 chars!");
            } else if(response.equalsIgnoreCase("")) {
                System.out.println("field cannot be empty!");
            } else {
                break;
            }
        }
        return response;
    }

    /*
     * Elicits and returns a 20 char max name from the console.
     * Does not accept empty string.
     */
    public static String elicitName() {
        String response;
        while(true) {
            System.out.print("Enter your name (20 chars max): ");
            response = in.nextLine().trim();
            if(response.length() > 20) {
                System.out.println("That's longer than 20 chars!");
            } else if(response.equalsIgnoreCase("")) {
                System.out.println("field cannot be empty!");
            } else {
                break;
            }
        }
        return response;
    }

    /*
     * Elicits and returns a 20 char max email(username) from the console.
     * Does not accept empty string
     */
    public static String elicitUserEmail() {
        String response;
        while(true) {
            System.out.print("Enter the users email (20 chars max): ");
            response = in.nextLine().trim();
            if(response.length() > 20) {
                System.out.println("That's longer than 20 chars!");
            } else if(response.equalsIgnoreCase("")) {
                System.out.println("field cannot be empty!");
            } else {
                break;
            }
        }
        return response;
    }

    /*
     * Elicits and returns a 20 char max email address from the console.
     * Does not accept empty string.
     */
    public static String elicitEmail(DBHandler db) {
        String response;
        while(true) {
            System.out.print("Enter your email (20 chars max): ");
            response = in.nextLine().trim();
            if(response.length() > 20) {
                System.out.println("That's longer than 20 chars!");
            } else if(response.equalsIgnoreCase("")) {
                System.out.println("field cannot be empty!");
            } else {
                if(db.existsUser(response)) {
                    System.out.println("That email already has an account. Try a different one.");
                } else {
                    break;
                }
            }
        }
        return response;
    }

    /*
     * Closes the connection to the DB connector and
     * exits the program gracefully.
     */
    private static void exit(DBHandler db) {
        // close connection
        boolean rval = db.closeConnection();
        if(rval) {
            System.out.println("Goodbye!");
            System.exit(0);
        } else {
            System.out.println("Error: Connection failed to close");
            System.exit(-1);
        }
    }

    /*
     * Just getters and setters beyond this point, no commenting required.
     */

    public String getJdbcUserName() {
        return jdbcUserName;
    }


    public void setJdbcUserName(String jdbcUserName) {
        this.jdbcUserName = jdbcUserName;
    }


    public String getJdbcPassword() {
        return jdbcPassword;
    }


    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }


    public static String getEmail() {
        return email;
    }


    public static void setEmail(String email) {
        UI.email = email;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
