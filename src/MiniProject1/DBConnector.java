package MiniProject1;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Handles the nitty gritty syntax of interacting with the DB
 */
public class DBConnector {

    private static String mDriverName = "oracle.jdbc.driver.OracleDriver";
    private static Class drvClass = null;
    private static Connection connection = null;
    private Statement stmt = null;
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
        
        if (connection == null) {
           return;
        }
        
        try {
            //close the connection
            connection.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /*
     * Drops the hard coded tables
     */
    public void dropTables() {
        try {   
            stmt.executeUpdate("drop table purchases");
            stmt.executeUpdate("drop table offers");
            stmt.executeUpdate("drop table ads");
            stmt.executeUpdate("drop table reviews");
            stmt.executeUpdate("drop table users");
            stmt.executeUpdate("drop table categories");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /*
     * Creates the hard coded tables
     */
    public void createTables() {
        String categories = "create table categories " +
                "(cat char(10)," +
                "supercat char(10)," +
                "primary key (cat)," +
                "foreign key (supercat) references categories)";
        String users = "create table users " +
                "(email char(20)," + 
                "name char(20)," +
                "pwd char(4)," +
                "last_login date," +
                "primary key (email))";
        String reviews = "create table reviews " +
                "(rno int," +
                "rating int," +
                "text char(80)," +
                "reviewer char(20)," +
                "reviewee char(20)," +
                "rdate date," +
                "primary key (rno)," +
                "foreign key (reviewer) references users," +
                "foreign key (reviewee) references users)";
        String offers = "create table offers " +
                "(ono int," +
                "ndays int," +
                "price float," +
                "primary key (ono))";
        String ads = "create table ads " +
                "(aid char(4)," +
                "atype char(1) check (atype='S' OR atype='W')," +
                "title char(20)," +
                "price int," +
                "descr char(40)," +
                "location char(15)," +
                "pdate date," +
                "cat char(10)," +
                "poster char(20)," +
                "primary key (aid)," +
                "foreign key (cat) references categories," +
                "foreign key (poster) references users)";
        String purchases ="create table purchases " +
                "(pur_id char(4)," +
                "start_date date," +
                "aid char(4)," +
                "ono int," +
                "primary key (pur_id)," +
                "foreign key (aid) references ads," +
                "foreign key (ono) references offers)";
        
        try {
            stmt.executeUpdate(categories);
            stmt.executeUpdate(users);
            stmt.executeUpdate(reviews);
            stmt.executeUpdate(offers);
            stmt.executeUpdate(ads);
            stmt.executeUpdate(purchases);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Lists the reviews for the user corresponding to argument 'username' which have been written 
     * since his/her last login, ordered based on date with more recent reviews listed first.
     */
    public void listReviews(String username) {
        //TODO
        System.out.println("listReview called, but not yet implemented");
        return;
    }

    /*
     * inserts the statement in string S
     */
    public void insert(String s) {
        
        //add some sort of empty string check
        // cause it throws on s = "";
        
        try {
            stmt.executeUpdate(s);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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
