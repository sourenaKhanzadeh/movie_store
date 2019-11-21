import java.sql.*;
import java.sql.Statement;

/**
 * Created by Owner on 11/20/2019.
 */
public class Initiate {

    void initiate(){
        Connection conn1 = null;

        try {
            // registers Oracle JDBC driver - though this is no longer required
            // since JDBC 4.0, but added here for backward compatibility
            Class.forName("oracle.jdbc.OracleDriver");


            //   String dbURL1 = "jdbc:oracle:thin:username/password@oracle.scs.ryerson.ca:1521:orcl";  // that is school Oracle database and you can only use it in the labs


            String dbURL1 = "jdbc:oracle:thin:system/root@localhost:1521:xe";
			/* This XE or local database that you installed on your laptop. 1521 is the default port for database, change according to what you used during installation.
			xe is the sid, change according to what you setup during installation. */

            conn1 = DriverManager.getConnection(dbURL1);
            if (conn1 != null) {
                System.out.println("Connected with connection #1");
            }

            String tables[] = {"movie_user","admin","customer",
                    "product","movie","music",
                    "tvseries","subscription",
                    "streams","manages","purchases"};

            if (conn1 != null) {
                Statement stat = conn1.createStatement();
                try {
                    for (String table : tables) {
                        stat.execute("DROP TABLE " + table + " CASCADE CONSTRAINTS");
                    }

                }catch (Exception e){
                    System.out.println("Error: " + e.getMessage());
                }finally{
                    creteTables(stat);
                }

            }




        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn1 != null && !conn1.isClosed()) {
                    conn1.close();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }


    }


    void creteTables(Statement stm) throws SQLException {
        stm.execute("CREATE TABLE movie_user (UserID NUMBER PRIMARY KEY," +
                "FirstName VARCHAR2(30) NOT NULL," +
                "LastName VARCHAR2(30) NOT NULL," +
                "Email VARCHAR2(50) NOT NULL," +
                "Username VARCHAR2(30) NOT NULL )");

        stm.execute(
                "CREATE TABLE admin (" +
                        "AdminID NUMBER PRIMARY KEY REFERENCES movie_user(UserID)" +
                        ")"
        );

        stm.execute(
                "CREATE TABLE customer (" +
                        "CustomerID NUMBER PRIMARY KEY REFERENCES movie_user(UserID)" +
                        ")"
        );

        stm.execute(
                "CREATE TABLE product (" +
                        "ProductID NUMBER PRIMARY KEY," +
                        "Name VARCHAR2(50) NOT NULL," +
                        "Length NUMBER NOT NULL," +
                        "Description VARCHAR2(280)," +
                        "Genre VARCHAR2(50)," +
                        "SellPrice NUMBER NOT NULL," +
                        "ReleaseDate  DATE" +
                        ")"
        );

        stm.execute(
                "CREATE TABLE movie (" +
                        "MovieID NUMBER PRIMARY KEY REFERENCES product(ProductID)," +
                        "Movie_Cast VARCHAR2(100)," +
                        "Languages VARCHAR2(100)," +
                        "Director VARCHAR2(50)," +
                        "Country VARCHAR2(50)," +
                        "MaturityRating VARCHAR2(10)," +
                        "RottenTomatoRating VARCHAR2(10)" +
                        ")"
        );

        stm.execute(
                "CREATE TABLE music (" +
                        "SongID NUMBER PRIMARY KEY REFERENCES product(ProductID)," +
                        "ArtistName VARCHAR2(100) NOT NULL" +
                        ")"
        );

        stm.execute(
                "CREATE TABLE tvseries (" +
                        "SeriesID NUMBER PRIMARY KEY REFERENCES product(ProductID)," +
                        "Seasons NUMBER NOT NULL," +
                        "Episodes NUMBER NOT NULL," +
                        "TV_Cast VARCHAR2(100)," +
                        "MaturityRating VARCHAR2(10)," +
                        "RottenTomatoRating VARCHAR2(10)" +
                        ")"
        );

        stm.execute(
                "CREATE TABLE subscription (" +
                        "Type VARCHAR2(20) PRIMARY KEY," +
                        "Price NUMBER NOT NULL," +
                        "TVSeriesPerMonth NUMBER NOT NULL," +
                        "MoviesPerMonth NUMBER NOT NULL," +
                        "MusicPerMonth NUMBER NOT NULL" +
                        ")"
        );


        stm.execute(
                "CREATE TABLE streams (" +
                        "CustomerID NUMBER NOT NULL REFERENCES customer(CustomerID)," +
                        "ProductID NUMBER NOT NULL REFERENCES product(ProductID)" +
                        ")"
        );

        stm.execute(
                "CREATE TABLE manages (" +
                        "AdminID NUMBER NOT NULL REFERENCES admin(AdminID)," +
                        "CustomerID NUMBER REFERENCES customer(CustomerID)," +
                        "ProductID NUMBER REFERENCES product(ProductID)" +
                        ")"
        );

        stm.execute(
                "CREATE TABLE purchases (" +
                        "CustomerID NUMBER NOT NULL REFERENCES customer(CustomerID)," +
                        "SubscriptionType VARCHAR2(20) REFERENCES subscription(Type)," +
                        "ProductID NUMBER REFERENCES product(ProductID)," +
                        "DateOfPurchase DATE" +
                        ")"
        );
        System.out.println("Tables reset...");
    }


}
