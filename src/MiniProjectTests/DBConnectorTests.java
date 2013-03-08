package MiniProjectTests;

import java.sql.SQLException;
import java.util.ArrayList;

import MiniProject1.DBConnector;

public class DBConnectorTests {

    /**
     * @param args
     */
    public static void main(String[] args) {
        boolean rval;
        ArrayList<String> rlist;
        
        //Oracle db connection info
        String jdbcURL = "jdbc:oracle:thin:@localhost:1525:CRS"; //I port forwarded to make this work from my 1525 to gwynne 1521    
        String jdbcUserName = "mgallowa";
        String jdbcPassword = "!MG~26^rx";
        
        //Test constructor
        DBConnector db = new DBConnector(jdbcURL, jdbcUserName, jdbcPassword);
        assert db != null;
        assert db.getJdbcPassword() == "!MG~26^rx";
        assert db.getJdbcUserName() == "mgallowa";
        assert db.getJdbcURL() == "jdbc:oracle:thin:@localhost:1525:CRS";
        
        //open connection
        db.openConnection();
        System.out.println("TEST: Connection Successful");
        
        //drop tables
        dropTables(db);
        
        //create tables
        createTables(db);
        
        //insert test data
        try {
            db.stmt.executeUpdate("insert into categories values ('buy/sell', null)");
            db.stmt.executeUpdate("insert into categories values ('services', null)");
            db.stmt.executeUpdate("insert into categories values ('tickets', 'buy/sell')");
            db.stmt.executeUpdate("insert into categories values ('sports', 'buy/sell')");
            db.stmt.executeUpdate("insert into categories values ('cameras', 'buy/sell')");
            db.stmt.executeUpdate("insert into categories values ('computer', 'services')");
            
            db.stmt.executeUpdate("insert into users values ('joe@ujiji.com','Joe Plumber', '1111', SYSDATE-2)");
            db.stmt.executeUpdate("insert into users values ('bob@ujiji.com','Bob Carpenter', '2222', SYSDATE-5)");
            
            db.stmt.executeUpdate("insert into reviews values (1,5,'This review shows up for bob', 'joe@ujiji.com', 'bob@ujiji.com', SYSDATE-2)");
            db.stmt.executeUpdate("insert into reviews values (2,5,'This review shows up for joe', 'bob@ujiji.com', 'joe@ujiji.com', SYSDATE)");
            db.stmt.executeUpdate("insert into reviews values (3,4,'This review shouldnt show up for joe', 'bob@ujiji.com', 'joe@ujiji.com', SYSDATE-3)");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        //test listReviews
        rlist = db.listReviews("bob@ujiji.com");
        for (String s : rlist) {
            System.out.println(s);
        }
        System.out.println("TEST: End bob@ujiji.com");
        
        rlist = db.listReviews("joe@ujiji.com");
        for (String s : rlist) {
            System.out.println(s);
        }
        System.out.println("TEST: End joe@ujiji.com");

        //test update login_date
        db.updateLoginDate("bob@ujiji.com");
        System.out.println("TEST: Date Updated Successfully");
        
        //add new user test
        rval = db.addNewUser("mgallowa@email.com", "Mark Galloway", "1234");
        if(rval) {
            System.out.println("PASS: User entered successfully");
        } else {
            System.out.println("FAIL: User not entered");
        }
        
        //userExists check Valid
        rval = db.existsUser("mgallowa@email.com");
        if(rval) {
            System.out.println("PASS: Mark exists!");
        } else {
            System.out.println("FAIL: existsUser failed to detect Mark");
        }
        
        //userExists check invalid
        rval = db.existsUser("unknown@email.com");
        if(rval) {
            System.out.println("FAIL: userExists returned true on invalid user");
        } else {
            System.out.println("PASS: existsUser detected bad user");
        }
        
        
        //addNewUser - try to add an existing user
        rval = db.addNewUser("mgallowa@email.com", "Mark Galloway", "1234");
        if(!rval){
            System.out.println("PASS: Existing user check succeeded");
        } else {
            System.out.println("FAIL: addNewUser failed to recognize existing user");
        }
        
        //verifyUser check
        rval = db.verifyUser("mgallowa@email.com", "1234");
        if (rval) {
            System.out.println("PASS: verifyUser succeeded");
        } else {
            System.out.println("FAIL: verifyUser is broken or user does not exist");
        }
        
        //verifyUser incorrect password test
        rval = db.verifyUser("mgallowa@email.com", "4321");
        if (!rval) {
            System.out.println("PASS: verifyUser detected wrong password");
        } else {
            System.out.println("FAIL: verifyUser is broken or user does not exist");
        }
        
        
        
        
        // close connection
        db.closeConnection();
        System.out.println("TEST: Connection Successfully closed");
    }

    /*
     * Creates the hard coded tables
     */
    public static void createTables(DBConnector db) {
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
            db.stmt.executeUpdate(categories);
            db.stmt.executeUpdate(users);
            db.stmt.executeUpdate(reviews);
            db.stmt.executeUpdate(offers);
            db.stmt.executeUpdate(ads);
            db.stmt.executeUpdate(purchases);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Drops the hard coded tables
     */
    public static void dropTables(DBConnector db) {
        try {   
            db.stmt.executeUpdate("drop table purchases");
            db.stmt.executeUpdate("drop table offers");
            db.stmt.executeUpdate("drop table ads");
            db.stmt.executeUpdate("drop table reviews");
            db.stmt.executeUpdate("drop table users");
            db.stmt.executeUpdate("drop table categories");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
