import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.*;

/**
 * Created by Owner on 11/15/2019.
 */
public class Dashboard extends Application {

    private String username;
    final double headerFont = 22;

    Dashboard(String username){
        this.username = username;
        // menu screen

    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        // create a Top menu
        GridPane root = new GridPane();

        boolean check = checkUserExist();

        Button back;

        if(check){
           back = userExist();
        }else{
            back = userNotExits(root);
        }



        // user clicks the back button
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main main = new Main();
                try {
                    main.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        // create a new scene and set scene
        Scene scene = new Scene(root, Main.SCREEN_W, Main.SCREEN_H);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    String getUsername(){
        return this.username;
    }

    boolean checkUserExist(){
        boolean exitCode = true;
        try{
            // add user to the database
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "root");


            // query data
            try (Statement stmt = con.createStatement()) {

                ResultSet rs = stmt.executeQuery("SELECT USERNAME FROM MOVIE_USER WHERE USERNAME='" + getUsername() +"'");
                rs.next();
                String user = rs.getString("USERNAME");
                System.out.println(user + " Exist in the database....");

                exitCode = true;

            } catch (SQLException e) {
                //Alert the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(getUsername() + " Does Not Exist... ");
                alert.setHeaderText("Failure \u2639");
                alert.showAndWait();

                System.out.println(getUsername() + " does not exist in the database.... " + con.toString());
                System.out.println("Error Code: " + e.getErrorCode());

                exitCode = false;
            }




        }catch (Exception e){
            System.out.println("Could not connect to the database....." + e);
            exitCode = false;
        }
        return exitCode;
    }

    Button userExist(){

        Button back = new Button("Back");

        return back;
    }

    Button userNotExits(GridPane root){
        root.setAlignment(Pos.CENTER);
        // create a label saying no such user exist
        Label sorry = new Label(getUsername() + " Does Not Exists....");
        sorry.setFont(Font.font(headerFont));
        root.add(sorry, 0,0);

        GridPane.setHalignment(sorry, HPos.CENTER);

        // create a back button
        Button back = new Button("Back");

        GridPane.setHalignment(sorry, HPos.CENTER);

        root.add(back, 0, 1, 2, 1);

        return back;

    }

}
