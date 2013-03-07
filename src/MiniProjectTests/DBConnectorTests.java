package MiniProjectTests;

import MiniProject1.DBConnector;

public class DBConnectorTests {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
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
        System.out.println("TEST: Constructor tests pass");
        
        
        //test connection
        db.openConnection();
        System.out.println("TEST: Connection Successful");
        
        //drop tables
        db.dropTables();
        System.out.println("TEST: Tables Dropped");
        
        //create tables
        db.createTables();
        System.out.println("TEST: Tables Created");
        
        //insert test data
        db.insert("insert into categories values ('buy/sell', null)");
        db.insert("insert into categories values ('services', null)");
        db.insert("insert into categories values ('tickets', 'buy/sell')");
        db.insert("insert into categories values ('sports', 'buy/sell')");
        db.insert("insert into categories values ('cameras', 'buy/sell')");
        db.insert("insert into categories values ('computer', 'services')");
        
        db.insert("insert into users values ('joe@ujiji.com','Joe Plumber', '1111', SYSDATE-2)");
        db.insert("insert into users values ('bob@ujiji.com','Bob Carpenter', '2222', SYSDATE-1)");
        
        db.insert("insert into reviews values (1,5,'This review shows up', 'joe@ujiji.com', 'bob@ujiji.com', SYSDATE-2)");
        db.insert("insert into reviews values (2,5,'This review shows up', 'bob@ujiji.com', 'joe@ujiji.com', SYSDATE)");
        db.insert("insert into reviews values (3,4,'This review shouldnt show up', 'bob@ujiji.com', 'joe@ujiji.com', SYSDATE-3)");
        
        
        
        System.out.println("TEST: Statements Inserted Successfully");
        
        //test listReviews
        db.listReviews("bob@ujuji.com");
        System.out.println("TEST: End bob@ujuji.com");
        db.listReviews("joe@ujiji.com");
        System.out.println("TEST: End joe@ujiji.com");
        
        System.out.println("TEST: Testing Reviews Successful");

       
        
        //test close connection
        db.closeConnection();
        System.out.println("TEST: Connection Successfully closed");
    }

}
