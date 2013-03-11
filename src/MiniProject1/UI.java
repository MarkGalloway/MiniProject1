package MiniProject1;

import java.util.ArrayList;
import java.util.Scanner;

public class UI {
    private String jdbcURL = "jdbc:oracle:thin:@localhost:1525:CRS"; //TODO fix it   
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
        DBConnector db = new DBConnector(ui.jdbcURL, ui.getJdbcUserName(), ui.getJdbcPassword());

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
                    case 1: ui.searchForAds(db, ui);
                            break;
                    case 2: ui.listOwnAds(db, ui);
                            break;
                    case 3: ui.userSearch(db, ui);
                            break;
                    case 4: ui.postAd(db, ui);
                            break;
                    case 5: db.updateLoginDate(getEmail());
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
     * Prompt user to choose an ad to promote.
     * Promotes ad that has not yet been promoted.
     */
    private void promoteAd(DBConnector db, Ad ad) {
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
     * Propmts user to delete an ad.
     */
    private void deleteAd(DBConnector db, Ad ad) {
       boolean rval = db.deleteAd(ad);
       if(rval) {
           System.out.println("Add successfully removed.");
       } else {
           System.out.println("Database error removing add");
       }
    }
    
    /*
     * Manages the options of deleting or promoting an
     * ad.
     */
    private void manageAd(DBConnector db, UI ui, Ad ad) {
        while(true) {
            //display the menu
             ui.printManageAdMenu();
             String response = in.nextLine();
             try {
                 int index = Integer.parseInt(response);
                 //Launch the Function based on input
                 switch(index) {
                     case 1: ui.deleteAd(db, ad);
                             return;
                     case 2: ui.promoteAd(db, ad);
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
     * Lists the users own ads, displaying 5 initially,
     * asking whether the user wants to display 5 more ads.
     * Gives users the option to promote or delete their ads.
     */
    private void listOwnAds(DBConnector db, UI ui) {
        //get the ads
        ArrayList<Ad> ads = db.getOwnAds(UI.getEmail());
        
        if(ads == null) {
            System.out.println("SQL error with the query.");
            return;
        }
        
        if(ads.isEmpty()) {
            System.out.println("No ads were found.");
            return;
        }
        
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
            
            if(count <= ads.size()) {
                System.out.print("Input S to stop displaying ads, or press Enter for more:");
                //get the response
                String response = in.nextLine();
                if(response.trim().equalsIgnoreCase("S")) {
                    break;
                }
            }
        }
        if(ads.size() > 0) {
            while(true) {
                System.out.print("If you would like to delete or promote of any of the above ads, " +
                                   "input its number, or press Enter to return to main menu: "
                                  );
                //get the response
                String response = in.nextLine();
                try {
                    int index = Integer.parseInt(response);
                    manageAd(db, ui, ads.get(index-1));
                    return;
                } catch (NumberFormatException nFE) {
                    if(response.trim().equalsIgnoreCase("")) {
                        return;
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
     * Shows reviews made by the users that matches input.
     */
    private void printUsersReviews(DBConnector db, UI ui, String email) {
        
        System.out.println();
        
        ArrayList<Review> reviews = db.getUserReviews(email);
        if(reviews == null) {
            System.out.println("Had some sort of SQL error with the database");
            return;
        }
        else if(reviews.isEmpty()) {
            System.out.println("That user has no reviews");
            return;
        }        
        System.out.println("Here is " + email +" review texts:");
        for(Review r : reviews) {
            System.out.println(r.toStringFullText());
        }
    }
    
    /*
     * Allows user to write a review.
     */
    private void writeUserReview(DBConnector db, UI ui, String email) {
        
        Integer rating = elicitRating();
        String text = elicitText();
        
        boolean rval = db.addReview(new Review(rating, text, UI.email, email));
        if(!rval) {
            System.out.println("SQL error when inserting review");
        } else {
            System.out.println("Review entered successfully.");
        }
    }
    
    /*
     * Displys a user.
     */
    private void displayUsers(DBConnector db, UI ui, ArrayList<User> users) {
        
        //Shouldn't happen, but sanity check
        if( users.isEmpty()) {
            System.out.println("Sorry, no users found");
            return;
        }
        
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
                    usr = users.get(index - 1);
                }
            }  catch (NumberFormatException nFE) {
                //User entered a non-Integer
                System.out.print("Input not recognized. Try again");
                continue;
            }
        }
        
        while(true) {
           //display the menu
            ui.printUserDisplayMenu();
            String response = in.nextLine();
            try {
                int index = Integer.parseInt(response);
                //Launch the Function based on input
                switch(index) {
                    case 1: ui.printUsersReviews(db, ui, usr.getEmail());
                            continue;
                    case 2: ui.writeUserReview(db, ui, usr.getEmail());
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
     * Displays user that matches the email given.
     */
    private void searchForUsersByEmail(DBConnector db, UI ui) {
        System.out.println();
        String email = elicitUserEmail();
        
        User user = db.searchForUserByEmail(email);
        if (user != null) {
            ArrayList<User> users = new ArrayList<User>();
            users.add(user);
            ui.displayUsers(db, ui, users);
            return;
        } else {
            System.out.println("Sorry, no user found with that email.");
            return;
        }
    }
    
    /*
     * Displays user that matches the name given.
     */
    private void searchForUsersByName(DBConnector db, UI ui) {
        System.out.println();
        String name = elicitUserName();
        
        ArrayList<User> users = db.searchForUserByName(name);
        if(users == null) {
            System.out.println("searchForUsersByName: some sort of SQL error");   
        } else if(users.isEmpty()) {
            System.out.println("Sorry, no users found with that name.");
        } else {
            ui.displayUsers(db, ui, users);
        }
        return;
    }
    
    /*
     * Allows user to search for other users using their name
     * or email.
     */
    private void userSearch(DBConnector db, UI ui) {
        //elicit the search type
        while(true) {
            ui.printUserSearchMenu();
            String response = in.nextLine();
            try {
                int index = Integer.parseInt(response);
                //Launch the Function based on input
                switch(index) {
                    case 1: ui.searchForUsersByEmail(db, ui);
                            return;
                    case 2: ui.searchForUsersByName(db, ui);
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
     * Allows user to create an ad.
     */
    private void postAd(DBConnector db, UI ui) {
        
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
        
        String title = elicitTitle();
        Integer price = elicitPrice();
        String descr = elicitDescr();
        String location = elicitLocation();
        Ad ad = new Ad(atype, title, price, descr, location, cat);
        
        boolean rval = db.insertAd(ad, email);
        if(!rval) {
            System.out.println("Failed to insert Ad");
        } else {
            System.out.println("Add entered successfully!");
        }
        
    }
    
    /*
     * Displays ads that match input, displays 5 initially,
     * gives users the option to view 5 more ads.
     */
    private void displaySearchedAds(ArrayList<Ad> ads) {
        int count = 1;
        System.out.println();
        if(ads.size() > 0){
            System.out.println("Found " + ads.size() + " Ads. Displaying search results...");
        }
         
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
                System.out.print("Input S to stop displaying ads, or press Enter for more:");
                //get the response
                String response = in.nextLine();
                if(response.trim().equalsIgnoreCase("S")) {
                    break;
                }
            }
        }
        if(ads.size() > 0) {
            while(true) {
                System.out.println();
                System.out.print("If you would like to see more details of any of the above ads, " +
                                   "input its number, or press Enter to continue: "
                                  );
                //get the response
                String response = in.nextLine();
                try {
                    int index = Integer.parseInt(response);
                    System.out.println(index + ")" + ads.get(index-1).toStringKeywordSearchAdvanced());
                } catch (NumberFormatException nFE) {
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
     * Allows users to search for ads by entering keywords.
     */
    private void searchForAds(DBConnector db, UI ui) {
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
     * Prints the Ad type Selection menu
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
     * Prints the ad management menu
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
     * Prints the user display menu
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
     * Prints the user search root menu
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
     * Prints the Main root menu for System Functions
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
     * Prints the Login root menu
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
    
    /* TODO: comment inside me better
     * Executes the display reviews portion of the UI interaction.
     * Takes as argument an ArrayList of reviews to display to the
     * user. Returns no value.
     */
    private void displayReviews(ArrayList<Review> reviews) {
        System.out.println();
        int count = 1;
        if(reviews.size() > 0){
            System.out.println("Displaying new reviews since last login...");
        }
        
        while(count <= reviews.size()) {
            
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
                    break;
                }
            }
        }
        if(reviews.size() > 0) {
            while(true) {
                System.out.print("If you would like to see more text of any of the above reviews " +
                                   "Enter its number, or E to continue: "
                                  );
                //get the response
                String response = in.nextLine();
                try {
                    int index = Integer.parseInt(response);
                    System.out.println(index + ")" + reviews.get(index-1).toStringFullText());
                } catch (NumberFormatException nFE) {
                    if(response.trim().equalsIgnoreCase("E")) {
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
            System.out.println("Sorry, no new reviews since last login.");
        }
    }
    
    /*
     * Elicits an email and password from console. Checks with the 
     * DBHandler to see if the user name and password are correct.
     * If so, logs the user in.
     */
    private void logIn(DBConnector db) {
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
            System.out.println("Whoops, couldn't find that email/password in the database. Lets start over.");
            return;
        } else {
            System.out.println("You are now logged in.");
            setLoggedIn(true);
            return;
        }
    }
    
    /*
     * Elicits an email, name, and password from the console.
     * Passes these onto the DBHandler to add a user to the database.
     * Logs the user in on success.
     */
    private void createAccount(DBConnector db) {
        setEmail(elicitEmail(db));
        String name = elicitName(); 
        String password = elicitPassword();
        if(!db.addNewUser(getEmail(), name, password)) {
            System.err.println("Some sort of database error.");
            exit(db);
        }
        System.out.println("Great! You're account is created and you are now logged in.");
        setLoggedIn(true);
    }
    
    /*
     * Elicits and returns a 4 char max password from the console
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
     * Elicits an integer representing Price from the console
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
     * Elicits and returns a 15 char max location from the console
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
     * Elicits and returns a 80 char max review text from the console
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
     * Elicits and returns a 50 char max descr from the console
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
     * Elicits and returns a 20 char max title from the console
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
     * Elicits and returns a 20 char max name from the console
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
     * Elicits and returns a 20 char max name from the console
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
     * Elicits and returns a 20 char max email(username) from the console
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
     * Elicits and returns a 20 char max email address from the console
     */
    public static String elicitEmail(DBConnector db) {
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
    private static void exit(DBConnector db) {
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
     * Just getters and setters beyond this point, no commenting required
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
