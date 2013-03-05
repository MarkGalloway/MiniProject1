import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;


public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        try {
            
            //step1: load driver
            String mDriverName = "oracle.jdbc.driver.OracleDriver";
            Class drvClass=Class.forName(mDriverName);
            DriverManager.registerDriver((Driver)drvClass.newInstance());
            
            //Step2: establish the connection
            String jdbcURL = "jdbc:oracle:thin:@localhost:1525:CRS"; //I port forwarded to make this work from my 1525 to gwynne 1521
            String username = "mgallowa";
            String password = "!MG~26^rx";
            
            Connection con = DriverManager.getConnection(jdbcURL, username, password);
            
            con.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
