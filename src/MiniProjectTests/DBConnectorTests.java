package MiniProjectTests;

import java.sql.SQLException;
import java.util.ArrayList;
import MiniProject1.Ad;
import MiniProject1.DBConnector;
import MiniProject1.Offer;
import MiniProject1.Review;
import MiniProject1.User;


/* TODO LIST:::::
 * 
 * High priority:
 * TODO: The Design Document
 * TODO: Comment every Class //TODO: Mega Comments HIGH
 * //HIGH: sanity checks. Email must not be empty string. most categories should not be empty string. FUCK
 * 
 * Low Priority:
 * TODO: Superclass Ad
 * //TODO: put jdbc url back to normal
  // change review/ad printing BS to use continue and modulo
    
 * 
 */


/*
 * ssh -L 1525:gwynne.cs.ualberta.ca:1521 mgallowa@ohaton.cs.ualberta.ca
 */


public class DBConnectorTests {

    /**
     * @param args
     */
    public static void main(String[] args) {
        boolean rval;
        ArrayList<Review> rlist;
        
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
        rval = db.openConnection();
        if(rval) {
            System.out.println("PASS: Connection Successful");
        } else {
            System.out.println("FAIL: Connection Failed");
            System.exit(0);
        }
        
        //drop tables
        dropTables(db);
        
        //create tables
        createTables(db);
        
        //insert test data
        insertTestData(db);
        
        //test listReviews
        rlist = db.getReviews("joe@ujiji.com");
        for (Review r : rlist) {
            System.out.println(r.toStringListing() + " ;;; " + r.toStringFullText());
        }
        System.out.println("DONE: End joe@ujiji.com, check visually");
        
        rlist = db.getReviews("bob@ujiji.com");
        for (Review r : rlist) {
            System.out.println(r.toStringListing() + " ;;; " + r.toStringFullText());
        }
        System.out.println("DONE: End bob@ujiji.com, check visually");
        
        //test update login_date
//        db.updateLoginDate("bob@ujiji.com");
//        System.out.println("DONE: Date Updated, check visually");
        
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
//        rval = db.addNewUser("mgallowa@email.com", "Mark Galloway", "1234");
//        if(!rval){
//            System.out.println("PASS: Existing user check succeeded");
//        } else {
//            System.out.println("FAIL: addNewUser failed to recognize existing user");
//        }
        
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
        
        //Keyword Search tests
        ArrayList<String> keywords = new ArrayList<String>();
        keywords.add("snowmobile");
        keywords.add("camera");
        ArrayList<Ad> ads = db.keywordSearch(keywords);
        for (Ad a : ads) {
            System.out.println(a.toStringKeywordSearch() + " " + a.toStringKeywordSearchAdvanced());
        }
        System.out.println("DONE: keywordSeach() tests, check visually");
        
        //get own ads test
        ads = db.getOwnAds("Hagrid@hogwarts.com");
        for (Ad a : ads) {
            System.out.println(a.toStringListOwnAds());
        }
        System.out.println("DONE: Hagrids ads");
        ads = db.getOwnAds("mgallowa@email.com");
        for (Ad a : ads) {
            System.out.println(a.toStringListOwnAds());
        }
        System.out.println("DONE: marks ads");
        ads = db.getOwnAds("divorcee42@ujiji.com");
        for (Ad a : ads) {
            System.out.println(a.toStringListOwnAds());
        }
        System.out.println("DONE: divorcee's ads");
        System.out.println("DONE: listOwnAds() tests, check visually");
        
        //delete ad test
        rval = db.deleteAd(ads.get(1));
        if(rval) {
            System.out.println("PASS: deleteAd() returns success, also check visually");
        } else {
            System.out.println("FAIL: deleteAd() returned failure");
        }
        
        //getOffers() test
        ArrayList<Offer> offers = db.getOffers();
        for (Offer o : offers){
            System.out.println(o.toString());
        }
        System.out.println("DONE: listOffers() tests, check visually");
        
        //promoteAd() test
        rval = db.promoteAd( db.getOwnAds("vlad04@ujiji.com").get(0), db.getOffers().get(2));
        if(rval) {
            System.out.println("PASS: promoteAd() returns success, also check visually");
        } else {
            System.out.println("FAIL: deleteAd() had some error or something");
        }
        
        //insertAd() test
        rval = db.insertAd(new Ad("S", "WTB Motherboard", 129, "AMD FX-880 PRO", "Edmonton", "computer" ), "mgallowa@email.com");
        if(rval) {
            System.out.println("PASS: insertAd() returns success, also check visually");
        } else {
            System.out.println("FAIL: insertAd() had some error or something");
        }
        
        //searchForUserByEmail test
        User user = db.searchForUserByEmail("Hagrid@hogwarts.com");
        if (user != null) {
            System.out.println("PASS: searchForUserByEmail() returned successfully, printing:");
            System.out.println(user.toString());
        } else {
            System.out.println("FAIL: searchForUserByEmail() had some error or something");
        }
        
        //searchForUserByEmail wrong user test
        user = db.searchForUserByEmail("jasneiwsk@email.com");
        if (user == null) {
            System.out.println("PASS: searchForUserByEmail() returned not found");
        } else {
            System.out.println("FAIL: searchForUserByEmail() shouldnt find jasnewski");
        }
        
        //searchForUserByName test
        ArrayList<User> users = db.searchForUserByName("Rubeus Hagred");
        if(users != null) {
            System.out.println("PASS: searchForUserByName() returned successfully, printing:");
            for(User u: users) {
                System.out.println(u.toString());
            }
        } else {
            System.out.println("FAIL: searchForUserByName() had some error or something");
        }
        //searchForUserByEmail wrong user test
        users = db.searchForUserByName("jasneiwsk");
        if (users.isEmpty()) {
            System.out.println("PASS: searchForUserByName() returned empty (not found)");
        } else {
            System.out.println("FAIL: searchForUserByName() shouldnt find jasnewski");
        }
        
        //addReview() test
        rval = db.addReview(new Review(1, "This is a terrible new review", "mgallowa@email.com", "Hagrid@hogwarts.com"));
        if (users.isEmpty()) {
            System.out.println("PASS: addReview() returned successfully, check visually");
        } else {
            System.out.println("FAIL: addReview() had some sort of error");
        }
        
        //getCategories() test
        ArrayList<String> cats = db.getCategories();
        if (cats != null) {
            System.out.println("PASS: getCategories() returned successfully, check visually");
            for (String s : cats) {
                System.out.println(s);
            }
        } else {
            System.out.println("FAIL: getCategories() had some sort of error");
        }
        
        // close connection
        rval = db.closeConnection();
        if(rval) {
            System.out.println("PASS: Connection Successfully closed");
        } else {
            System.out.println("FAIL: Connection failed to close");
            System.exit(0);
        }
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
            System.exit(-1);
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
            System.exit(-1);
        }
    }

    /*
     * inserts test data
     */
    public static void insertTestData(DBConnector db) {
        try {
            db.stmt.executeUpdate("insert into categories values ('buy/sell', null)");
            db.stmt.executeUpdate("insert into categories values ('services', null)");
            db.stmt.executeUpdate("insert into categories values ('tickets', 'buy/sell')");
            db.stmt.executeUpdate("insert into categories values ('sports', 'buy/sell')");
            db.stmt.executeUpdate("insert into categories values ('cameras', 'buy/sell')");
            db.stmt.executeUpdate("insert into categories values ('computer', 'services')");
            
            db.stmt.executeUpdate("insert into users values ('joe@ujiji.com','Joe Plumber', '1111', SYSDATE-2)");
            db.stmt.executeUpdate("insert into users values ('bob@ujiji.com','Bob Carpenter', '2222', SYSDATE-6)");
            db.stmt.executeUpdate("insert into users values ('davood@ujiji.com','Davood Teacher', '3333', SYSDATE-1)");
            db.stmt.executeUpdate("insert into users values ('adam@sport.com','Adam Fan', '4444', SYSDATE-1)");
            db.stmt.executeUpdate("insert into users values ('Hagrid@hogwarts.com','Rubeus Hagred', '1212', SYSDATE-1)");
            db.stmt.executeUpdate("insert into users values ('imposter@email.com','Rubeus Hagred', '6666', SYSDATE-1)");
            db.stmt.executeUpdate("insert into users values ('vlad04@ujiji.com','Vladimir Lenin', '1917', SYSDATE-1)");
            db.stmt.executeUpdate("insert into users values ('bombardier@ujiji.com','Joseph Bombardier', '0123', SYSDATE-1)");
            db.stmt.executeUpdate("insert into users values ('H0ckeyg1rl@ujiji.com','Stacey Smyth', '1235', SYSDATE-1)");
            db.stmt.executeUpdate("insert into users values ('somejerk@ujiji.com','Rob Reseller', '4321', SYSDATE-1)");
            db.stmt.executeUpdate("insert into users values ('andy@eureka.com','Deputy Andy', '4312', SYSDATE-1)");
            db.stmt.executeUpdate("insert into users values ('divorcee42@ujiji.com','Jen Jones', '4132', SYSDATE-1)");
            db.stmt.executeUpdate("insert into users values ('Aphotos@ujiji.com','A Photos', '0234', SYSDATE-1)");
            db.stmt.executeUpdate("insert into users values ('CameraNerd@ujiji.com','B Petruk', '0345', SYSDATE-1)");
            
            db.stmt.executeUpdate("insert into reviews values (1,5,'This review shows up for bob first', 'joe@ujiji.com', 'bob@ujiji.com', SYSDATE-2)");
            db.stmt.executeUpdate("insert into reviews values (5,5,'This review shows up for bob third', 'joe@ujiji.com', 'bob@ujiji.com', SYSDATE-4)");
            db.stmt.executeUpdate("insert into reviews values (12,5,'This review shows up for bob fourth', 'joe@ujiji.com', 'bob@ujiji.com', SYSDATE-5)");
            db.stmt.executeUpdate("insert into reviews values (4,5,'This review shows up for bob second', 'joe@ujiji.com', 'bob@ujiji.com', SYSDATE-3)");
            db.stmt.executeUpdate("insert into reviews values (2,5,'This shows up for joe and cuts out  now and does not show more', 'bob@ujiji.com', 'joe@ujiji.com', SYSDATE)");
            db.stmt.executeUpdate("insert into reviews values (3,4,'This review shouldnt show up for joe', 'bob@ujiji.com', 'joe@ujiji.com', SYSDATE-3)");
            db.stmt.executeUpdate("insert into reviews values (6,5,'an all around nice fellow' , 'joe@ujiji.com', 'Hagrid@hogwarts.com', SYSDATE-1)");
            db.stmt.executeUpdate("insert into reviews values (7,4,'Very Handsome' , 'divorcee42@ujiji.com', 'vlad04@ujiji.com', SYSDATE-1)");
            db.stmt.executeUpdate("insert into reviews values (8,4,'doesnt know how to haggle' , 'bob@ujiji.com', 'Hagrid@hogwarts.com', SYSDATE-1)");
            db.stmt.executeUpdate("insert into reviews values (9,4,'Showed up late, but paid cash' , 'andy@eureka.com', 'Hagrid@hogwarts.com', SYSDATE-1)");
            
            db.stmt.executeUpdate("insert into offers values (1,3,5)");
            db.stmt.executeUpdate("insert into offers values (2,7,10)");
            db.stmt.executeUpdate("insert into offers values (3,14,15)");
            
            db.stmt.executeUpdate("insert into ads values ('a001','S','oilers ticket',90,'Feb 28, againts Stars','Edmonton','30-JAN-2013','tickets','bob@ujiji.com')");
            db.stmt.executeUpdate("insert into ads values ('a002','S','nikon camera',50,'working condition','Edmonton',sysdate,'cameras','davood@ujiji.com')");
            db.stmt.executeUpdate("insert into ads values ('a003','S','used oilers jersey',100,'Worn by Ryan Smyth, UNWASHED!!','Edmonton','01-FEB-2013','sports','H0ckeyg1rl@ujiji.com')");
            db.stmt.executeUpdate("insert into ads values ('a004','W','oilers tickets',20,'Need tickets. Paying $20 Firm','Edmonton','02-FEB-2013','tickets','somejerk@ujiji.com')");
            db.stmt.executeUpdate("insert into ads values ('a005','S','oilers tickets',50,'Tickets for sale. Low Price','Edmonton','03-FEB-2013','tickets','somejerk@ujiji.com')");
            db.stmt.executeUpdate("insert into ads values ('a006','S','robot sports oiler',50,'Keeps robot joints from squeeking','Edmonton','03-FEB-2013','sports','andy@eureka.com')");
            db.stmt.executeUpdate("insert into ads values ('a007','S','oilers towel',19,'only used for drying my tears','Edmonton','29-JAN-2013','sports','divorcee42@ujiji.com')");
            db.stmt.executeUpdate("insert into ads values ('a008','S','oilers hockey stick',101,'signed by Ryan Smyth','Edmonton','04-FEB-2013','sports','H0ckeyg1rl@ujiji.com')");
            db.stmt.executeUpdate("insert into ads values ('a009','S','polaris 800 RUSH',5000,'ex''s old snowmobile','Edmonton','29-JAN-2013','buy/sell','divorcee42@ujiji.com')");
            db.stmt.executeUpdate("insert into ads values ('a010','S','rusty snowmobile',100,'needs repair','Calgary','30-JAN-2013','buy/sell','somejerk@ujiji.com')");
            db.stmt.executeUpdate("insert into ads values ('a011','W','flying snowmobile',2500,'must work','Hogwarts','01-FEB-2013','buy/sell','Hagrid@hogwarts.com')");
            db.stmt.executeUpdate("insert into ads values ('a012','S','russian snowmobile',100,'might be broken','Moscow','01-FEB-2013','buy/sell','vlad04@ujiji.com')");
            db.stmt.executeUpdate("insert into ads values ('a013','S','shares in company',4,'my snowmobile division','Montreal','01-FEB-1960','buy/sell','bombardier@ujiji.com')");
            db.stmt.executeUpdate("insert into ads values ('a014','S','nikon',75,'ex''s old camera','Edmonton','29-JAN-2013','cameras','divorcee42@ujiji.com')");
            db.stmt.executeUpdate("insert into ads values ('a015','W','camera',100,'preferrably a nikon','Edmonton',SYSDATE-3,'cameras','Aphotos@ujiji.com')");
            db.stmt.executeUpdate("insert into ads values ('a016','S','my old camera',82,'selling my old camera','Victoria',SYSDATE-1,'cameras','CameraNerd@ujiji.com')");
            db.stmt.executeUpdate("insert into ads values ('a017','W','nikon',101,'Need to take pics of Fluffy','Hogwarts',SYSDATE-2,'cameras','Hagrid@hogwarts.com')");
            db.stmt.executeUpdate("insert into ads values ('a018','S','flying snowmobile',150,'kind of broken','Hogwarts','01-FEB-2013','buy/sell','Hagrid@hogwarts.com')");
            
            db.stmt.executeUpdate("insert into purchases values ('p002',SYSDATE-2,'a018',3)");
            db.stmt.executeUpdate("insert into purchases values ('p003',SYSDATE-1,'a017',1)");
            db.stmt.executeUpdate("insert into purchases values ('p004',SYSDATE-3,'a009',2)");
            db.stmt.executeUpdate("insert into purchases values ('p005',SYSDATE-4,'a014',3)");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }    
    }
}
