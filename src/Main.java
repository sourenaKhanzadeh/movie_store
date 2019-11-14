/**
 * Created by Owner on 9/7/2019.
 */

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.*;



public class Main  extends Application{

    final int SCREEN_W = 400;
    final int SCREEN_H = 100;

    final String TITLE = "Movie Front Store";

    final double H_GAP = 10;
    final double V_GAP = 10;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
//            Class.forName("Main Front");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:system/root@localhost:1521:xe");
            System.out.println("Successfully Connected to the database.... " + con.toString());
        }catch (Exception e){
            System.out.println("Could not connect to the database....." + e);
        }

        // root pane
        GridPane root = new GridPane();
        // configure the root pane
        root.setAlignment(Pos.CENTER);
        root.setGridLinesVisible(false);
        root.setHgap(H_GAP);
        root.setVgap(V_GAP);

        // create three buttons
        int userManagementNum = 2;
        String userManagementNames[] = {"Admin", "User"};
        Button userManagementBTNS[] = new Button[userManagementNum];

        // create the buttons
        for (int i = 0; i < userManagementNum; i++) {
            userManagementBTNS[i] = new Button();
            userManagementBTNS[i].setText(userManagementNames[i]);
            root.add(userManagementBTNS[i], i, 0);
        }



        // create a new scene
        Scene scene = new Scene(root, SCREEN_W, SCREEN_H);

        // create the stage
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}


