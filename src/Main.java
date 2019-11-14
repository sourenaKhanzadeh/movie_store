/**
 * Created by Owner on 9/7/2019.
 */

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.*;

public class Main  extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
//            Class.forName("Main Front");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:system/root@localhost:1521:xe");
            System.out.println("Successfully Connected to the database.... " + con.toString());
        }catch (Exception e){
            System.out.println("Could not connect to the database....." + e);
        }

        
    }
}


