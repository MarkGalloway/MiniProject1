package MiniProjectTests;

import MiniProject1.DBConnector;

public class DBConnectorTests {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        //Oracle db connection info
        String jdbcURL = "jdbc:oracle:thin:@localhost:1525:CRS"; //I port forwarded to make this work from my 1525 to gwynne 1521    
        String jdbcUserName = "mgallowa"; //Hard coded, make dynamic before hand-in
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
        
        //db.insert("");
        
        System.out.println("TEST: Statements Inserted Successfully");
        
        //test listReviews
        System.out.println("Testing List reviews");
        db.listReviews("fred");
        
        
        System.out.println("TEST: Testing Reviews Successful");

       
        
        //test close connection
        db.closeConnection();
        System.out.println("Connection Successfully closed");
    }

}
