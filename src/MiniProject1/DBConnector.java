package MiniProject1;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

/*
 * 
 * COMMENT ME BETTER!
 * 
 * Handles the nitty gritty syntax of interacting with the DB
 * so we don't need try/catch all over the place in the UI loop
 */
public class DBConnector {

    private static String mDriverName = "oracle.jdbc.driver.OracleDriver";
    private static Class drvClass = null;
    private static Connection connection = null;
    public Statement stmt = null;
    private String jdbcURL;
    private String jdbcUserName;
    private String jdbcPassword;
    
    /*
     * Constructor
     */
    public DBConnector(String URL, String uname, String pwd ) {
        super();
        this.setJdbcUserName(uname);
        this.setJdbcPassword(pwd);
        this.setJdbcURL(URL);
    }
    
    /*
     * Opens a connection to the Oracle DB.
     * Returns true on success.
     */
    public boolean openConnection() {
        
        if (connection != null) {
            closeConnection();
        }
        
        try {
            //load driver
            drvClass = Class.forName(mDriverName);
            DriverManager.registerDriver((Driver)drvClass.newInstance());
            
            //establish the connection
            connection = DriverManager.getConnection(getJdbcURL(), getJdbcUserName(), getJdbcPassword());
            stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    
    /*
     * Closes the connection to the Oracle DB.
     * Returns true on success.
     */
    public boolean closeConnection() {
        try {
            //close the connection
            connection.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    
    /*
     * Takes as input a 'username' and returns an ArrayList of the Reviews corresponding 'username' which have been written 
     * since his/her last login, ordered based on date with more recent reviews listed first.
     */
    public ArrayList<Review> getReviews(String username) {
        //to hold return values
        ArrayList<Review> reviews = new ArrayList<Review>();
        
        //input length and sql string conversion
        String usr = "'" + stringChop(username,20)  + "'";
        
        //query to get the users reviews which are new since last login
        String query = "select *" +
                      " from reviews R, users U" +
                      " where U.email = "+ usr + 
                      " and  R.reviewee = U.email" +
                      " and R.rdate > U.last_login" +
                      " order by R.rdate DESC";

        try {
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()){
                //add returned data to arraylist
                reviews.add(new Review(rs.getInt("rno"), rs.getInt("rating"), rs.getString("text").trim(), 
                        rs.getString("reviewer").trim(), rs.getString("reviewee").trim(), rs.getDate("rdate")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return reviews;
    }
    
    /*
     * Updates the login_date of the user specified in 'username'
     * to the current system date.
     * 
     * Returns True on success
     * 
     */
    public boolean updateLoginDate(String username){
        
        //input length conversion
        String usr = "'" + stringChop(username,20) + "'";
        
        //the query to be passed in
        String query = "Select email,last_login from users where email = " + usr ;
        
        //the new current date
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        
        try {
            //get the resultset from query
            ResultSet rs = stmt.executeQuery(query);
            //move to the row
            rs.absolute(1);
            //update the date
            rs.updateDate("last_login", sqlDate);
            //update the change in the database
            rs.updateRow();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    
    /*
     *  Checks the users table to see if the user exists and the password matches
     *  Returns true if the user exists and password matches
     *  Returns false if either user not found or password is wrong
     *  
     */
    public boolean verifyUser(String username, String password) {
        
        //input length and SQLstring conversion
        String usr = "'" + stringChop(username,20) + "'";
        String pwd = "'" + stringChop(password,4) + "'";
        
        //query to get all users
        String query ="select email,pwd from users" +
                     " where email = " + usr +
                     " and pwd = " + pwd;
        
        try {
            //execute the query
            ResultSet rs = stmt.executeQuery(query);
            
            //check for a result. if there is one, then this username/password combo is valid.
            if (!rs.next()) {
                return false; //either user doesn't exist or password is wrong.
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        //fall-through. email and password MUST be correct, due to query returning a value
        return true;
    }
    
    /*
     *  Checks the users table to see if the user exists
     *  Returns true if the user exists, false otherwise
     *  
     */
    public boolean existsUser(String username) {
        
        //input length conversion
        String usr = "'" + stringChop(username,20) + "'";
        
        //query to select primary key of all users
        String query ="select email from users where email = " + usr;
        
        try {
            //execute query
            ResultSet rs = stmt.executeQuery(query);
            
            //check for a result. if there is one, then this username is valid
            if (!rs.next()) {
                return false; //this username does not exist
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //fall-through. User MUST exist, due to query returning a value
        return true;
    }
    
    /*
     * Adds a new user to the Users table.
     * 
     * returns true if user added successfully
     * returns false if user already exists
     * 
     */
    public boolean addNewUser(String username, String nameString, String password) {        
        
        //input length conversion
        String usr = stringChop(username,20);
        String pwd = stringChop(password,4);
        String name = stringChop(nameString,20);
        
        //if user already exists, then reject.
        if(existsUser(usr)) {
            return false;
        }
        
        //query to return the users table
        String query = "select email,name,pwd,last_login from users";
        //the current date
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        
        try {
            //execute query
            ResultSet rs = stmt.executeQuery(query);
            //Insert the new user as a row in the table
            rs.moveToInsertRow();
            //Insert values into new row
            rs.updateString("email", usr);
            rs.updateString("name",name);
            rs.updateString("pwd", pwd);
            rs.updateDate("last_login", sqlDate);
            //make permanent
            rs.insertRow();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    
    /* Assumed case-insensitivity. Not clear in spec.
     * 
     * Takes as argument some keywords. Launches an SQL query which searches for 
     * each keyword find rows in the ads table which have a the keyword in either title or descr.
     * 
     * Returns all found rows as Ad objects in an ArrayList
     */
    public ArrayList<Ad> keywordSearch(ArrayList<String> keywords) {
        //return values holder
        ArrayList<Ad> ads = new ArrayList<Ad>();
        
        //Return nothing if given nothing. not sure if this is necessary
        if (keywords.isEmpty()) {
            return ads;
        }
        
        //build the beginning of the query
        String query = "select aid, atype, title, price, descr, location, pdate, cat, poster, AVG(rating) as avg" +
                " from ads left outer join reviews on poster = reviewee" +
                " where (lower(title) LIKE '%" + keywords.get(0).toLowerCase() + "%'" + 
                " or lower(descr) LIKE '%" + keywords.get(0).toLowerCase() + "%')";
        
        //Dynamically add each keyword to the where clause of the query.
        for (int i = 1; i < keywords.size(); i++) {
            query = query + " or (lower(title) LIKE '%" + keywords.get(i).toLowerCase() + "%'" + 
                            " or lower(descr) LIKE '%" + keywords.get(i).toLowerCase() + "%')";
        }

        //attach the end of the query
        query = query + " group by aid, atype, title, price, descr, location, pdate, cat, poster" +
                        " order by pdate DESC";
        try {
            ResultSet rs = stmt.executeQuery(query);
            //converts each result to an Ad datatype
            while(rs.next()) {
                ads.add(new Ad(rs.getString("atype").trim(), rs.getString("title").trim(), rs.getInt("price"), 
                               rs.getString("descr").trim(), rs.getString("location").trim(), rs.getDate("pdate"), 
                               rs.getString("cat").trim(), rs.getString("poster").trim(), rs.getDouble("avg")
                              )
                       );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //return the matching ads
        return ads;
    }
    
    /*
     * Takes as argument a username string. Queries the database for all
     * ads which have been posted by that username. Returns an Ad object containing
     * the relevant data: aid, atype, title, price, pdate, and ldate.
     * ldate is the number of days remaining on any purchased offers.
     */
    public ArrayList<Ad> getOwnAds(String username) {
        //return value holder
        ArrayList<Ad> ads = new ArrayList<Ad>();
        
        //convert username
        String usr = "'" + stringChop(username,20) + "'";
        //auery to return all ads which have been posted by the username and the time left on purchased offers (if any)
        String query = "select A.aid, atype, title, A.price, pdate, round(ndays - (sysdate - start_date)) as ldate" +
        		      " from ads A left outer join purchases P on A.aid=P.aid left outer join offers O on P.ono=O.ono" +
        		      " where poster = " + usr;
        
        try {
            ResultSet rs = stmt.executeQuery(query);
            //converts each result to an Ad datatype
            while(rs.next()) {
                ads.add(new Ad(rs.getString("aid").trim(), rs.getString("atype").trim(), rs.getString("title").trim(), rs.getInt("price"), 
                               rs.getDate("pdate"), rs.getInt("ldate")
                              )
                       );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //return the matching ads
        return ads;
    }
    
    /*
     * Takes as argument an Ad object. Launches 2 queries to the database.
     * The first query is to find if the Ad has any purchased offers. If yes,
     * the purchased offers are deleted from the database. Then, the ad itself
     * is queried for and deleted from the database
     */
    public boolean deleteAd(Ad ad) {
        String aid = ad.getAid();
        aid = "'" + aid +"'";
        
        //query to find the purchase
        String query1 = "select pur_id" +
        		      " from purchases" +
                      " where aid =" + aid;
        //query to find the ad
        String query2 = "select aid" +
                " from ads" +
                " where aid =" + aid;
        try {
            //find the purchase
            ResultSet rs = stmt.executeQuery(query1);
            //if query returned a purchase
            if(rs.next()) {
                //delete purchase
                rs.absolute(1);
                rs.deleteRow();
            }
            //get the ad
            rs = stmt.executeQuery(query2);
            //delete the ad
            rs.absolute(1);
            rs.deleteRow();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        //success
        return true;
    }
    
    /*
     * Searches the Database for the available offers and records them into
     * Offers objects, returning an ArrayList of Offers objects containing all the offers.
     * 
     */
    public ArrayList<Offer> getOffers() {
        ArrayList<Offer> offers = new ArrayList<Offer>();
        //query to get the offers
        String query = "select * from offers";
        
        try {
            ResultSet rs = stmt.executeQuery(query);
            //store the offers into the ArrayList
            while(rs.next()) {
                offers.add(new Offer(rs.getString("ono"), rs.getInt("ndays"), rs.getFloat("price")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //return the offers
        return offers;
    }
    
    /*
     * Takes as arguments an Ad object and an Offer object.
     * Generates unique pur_id and the current date.
     * Inserts a row into purchases containing the Ads aid, Offers ono, 
     * the new date, and the random unique pur_id. 
     * 
     * Returns true on success.
     */
    public boolean promoteAd(Ad ad, Offer offer) {
        //random new (possibly) Unique ID
        String pur_id = generateRandom();
        //the new current date
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        
        String query1 = "Select pur_id from purchases where pur_id = " + "'" + pur_id + "'";
        String query2 = "Select pur_id, start_date, aid, ono from purchases";
        
        ResultSet rs;
        try {
            //keep generating new random ID until we have a unique one
            rs = stmt.executeQuery(query1);
            while(rs.next()) {
                //query returned a match, id already exists
                pur_id = generateRandom();
                rs = stmt.executeQuery(query1);
            }
            //get the table
            rs = stmt.executeQuery(query2);
            //add new row and values to table
            rs.moveToInsertRow();
            rs.updateString("pur_id", pur_id);
            rs.updateDate("start_date", sqlDate);
            rs.updateString("aid", ad.getAid());
            rs.updateString("ono", offer.getOno());
            rs.insertRow();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    
    /*
     * Takes as arguments an Ad object and a username string. Generates a
     * unique id and the current date. Uses the unique id, current date,
     * username string, and the values stored in the Ad object to create
     * a new row in the ads table correspondign to the values contained in
     * the arguments.
     * 
     * Returns true on success
     */
    public boolean insertAd(Ad ad, String username) {
        //random new (possibly) Unique ID
        String aid = generateRandom();
        //the new current date
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        
        String query1 = "Select aid from ads where aid = " + "'" + aid + "'";
        String query2 = "Select aid, atype, title, price, descr, location, pdate, cat, poster from ads";
        
        ResultSet rs;
        try {
            //keep generating new random ID until we have a unique one
            rs = stmt.executeQuery(query1);
            while(rs.next()) {
                //query returned a match, id already exists
                aid = generateRandom();
                rs = stmt.executeQuery(query1);
            }
            //get the table
            rs = stmt.executeQuery(query2);
            //add new row and values to table
            rs.moveToInsertRow();
            rs.updateString("aid", aid);
            rs.updateString("atype", ad.getAtype());
            rs.updateString("title", ad.getTitle());
            rs.updateInt("price", ad.getPrice().intValue());
            rs.updateString("descr", ad.getDescr());
            rs.updateString("location", ad.getLocation());
            rs.updateDate("pdate", sqlDate);
            rs.updateString("cat", ad.getCat());
            rs.updateString("poster", username);
            rs.insertRow();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    
    /*
     * Generates a random number in the range of 0-9999 and converts it to a string
     * 
     * TODO: move to utils package
     */
    public String generateRandom() {
        return String.valueOf((new Random()).nextInt(10000));
    }
    
    /*
     * Takes an input string and a max.
     * If the string is shorter than max, it is returned.
     * If the string is longer than max, it is shortened to be
     * max chars long. Simply cuts off the end of strings that are
     * too long for the DB.
     * 
     * TODO: refactoring: rename this function to something useful
     * TODO: refactoring: pull method into Utils package
     */
    public static String stringChop(String s, int max) {
        int maxLength = (s.length() < max) ? s.length() 
                                           : max;
        return s.substring(0, maxLength);
    }
    
    public String getJdbcURL() {
        return jdbcURL;
    }

    public void setJdbcURL(String jdbcURL) {
        this.jdbcURL = jdbcURL;
    }

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
}
