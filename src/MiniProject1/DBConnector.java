package MiniProject1;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * 
 * 
 * 
 * 
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
     * Opens a connection to the Oracle DB
     */
    public void openConnection() {
        
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
        }
    }
    
    /*
     * Closes the connection to the Oracle DB
     */
    public void closeConnection() {
        try {
            //close the connection
            connection.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /*
     * Takes as input a 'username' and returns an ArrayList of the reviews corresponding 'username' which have been written 
     * since his/her last login, ordered based on date with more recent reviews listed first.
     * 
     * TODO:
     *  Unsure what the format of last reviews should be. just doing "date: xx reviewer: yy Reviewtext: zz"
     *  for now... this may need to be changed.
     * 
     * TODO: More specs
     */
    public ArrayList<String> getReviews(String username) {
        
        //input length conversion
        String usr = stringChop(username,20);
        
        //query to get the users reviews which are new since last login
        String query = "select *" +
                      " from reviews R, users U" +
                      " where U.email = "+ "'" + usr + "'" + 
                      " and  R.reviewee = U.email" +
                      " and R.rdate > U.last_login" +
                      " order by R.rdate DESC";
        
        ArrayList<String> out = new ArrayList<String>();
        
        try {
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()){
                out.add("Date: " + rs.getDate(6) +
                		", Reviewer: " + (rs.getString(4)).trim() +
                		", Review Text: " + rs.getString(3));
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return out;
    }
    
    /*
     * Updates the login_date of the user specified in 'username'
     * to the current system date.
     * 
     * TODO: Error checks or return value?? Not sure if needed
     * 
     */
    public void updateLoginDate(String username){
        
        //input length conversion
        String usr = stringChop(username,20);
        
        //the query to be passed in
        String query = "Select email,last_login from users where email = " + "'" + usr +"'";
        
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
        }
        
    }
    
    /*
     *  Checks the users table to see if the user exists and the password matches
     *  Returns true if the user exists and password matches
     *  Returns false if either user not found or password is wrong
     */
    public boolean verifyUser(String username, String password) {
        
        //input length conversion
        String usr = stringChop(username,20);
        String pwd = stringChop(password,4);
        
        //query to get all users
        String query ="select * from users";
        
        try {
            //execute the query
            ResultSet rs = stmt.executeQuery(query);
            
            //loop through the results
            while (rs.next()) {
                if(rs.getString("email").trim().equalsIgnoreCase(usr)  //TODO: is ignorecase right here??
                        && rs.getString("pwd").trim().equalsIgnoreCase(pwd)) { //TODO: is ignorecase right here??
                    //user matches username && pswd
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //no match, user does not exist
        return false;
    }
    
    /*
     *  Checks the users table to see if the user exists
     *  Returns true if the user exists
     */
    public boolean existsUser(String username) {
        
        //input length conversion
        String usr = stringChop(username,20);
        
        //query to select primary key of all users
        String query ="select email from users";
        
        try {
            //execute query
            ResultSet rs = stmt.executeQuery(query);
            //loop through results
            while (rs.next()) {
                if(rs.getString("email").trim().equalsIgnoreCase(usr)) { //TODO: is ignorecase right here??
                    //user found
                    return true;
                }
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //user not found
        return false;
    }
    
    /*
     * Adds a new user to the Users table.
     * 
     * TODO: Verify correct input ??
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
    
    /*
     * Takes an input string and a max.
     * If the string is shorter than max, it is returned.
     * If the string is longer than max, it is shortened to be
     * max chars long. Simply cuts off the end of strings that are
     * too long for the DB.
     * 
     * TODO: refactoring: rename this function to something useful
     */
    private static String stringChop(String s, int max) {
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
