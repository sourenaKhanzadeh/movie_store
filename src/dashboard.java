import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.sql.*;

/**
 * Created by Owner on 11/15/2019.
 */
public class Dashboard extends Application {

    private String username;
    private boolean admin;
    private int userID;

    final double headerFont = 22;

    Dashboard(String username){
        this.username = username;

    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        // create a Top menu

        boolean check = checkUserExist();

        Button back;
        Scene scene;

        if(check){
            BorderPane root = new BorderPane();
            back = userExist(root);
            // create a new scene and set scene
            scene = new Scene(root, Main.SCREEN_W*2, Main.SCREEN_H*2);

        }else{
            GridPane root = new GridPane();
            back = userNotExits(root);
            // create a new scene and set scene
            scene = new Scene(root, Main.SCREEN_W*2, Main.SCREEN_H*2);

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

                ResultSet rs = stmt.executeQuery("SELECT USERID, USERNAME FROM MOVIE_USER WHERE USERNAME='" + getUsername() +"'");
                rs.next();
                String user = rs.getString("USERNAME");
                userID = rs.getInt("USERID");
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

            // check if user is admin
            try (Statement stmt = con.createStatement()) {

                ResultSet rs = stmt.executeQuery("SELECT ADMINID FROM ADMIN WHERE ADMINID=" + userID + "");
                rs.next();
                rs.getInt("ADMINID");
                admin = true;

                System.out.println("Welcome Admin, " + getUsername());

            }catch (SQLException e) {

                System.out.println("Welcome User, " + getUsername());
                admin = false;
            }
            System.out.println(userID + ": " + admin);




        }catch (Exception e){
            System.out.println("Could not connect to the database....." + e);
            exitCode = false;
        }
        return exitCode;
    }

    Button userExist(BorderPane root){
        //create top menu
        HBox top = createTopMenu();
        HBox bottom = createBottomMenu();

        // create Dashboard
        GridPane dashboard = createDashboard();
        dashboard.setAlignment(Pos.CENTER);
        
        VBox left = createOptionMenue(dashboard);


        Button back = new Button("Back");
        bottom.setAlignment(Pos.BASELINE_RIGHT);

        bottom.getChildren().addAll(back);

        // set Border Pane
        root.setTop(top);
        root.setBottom(bottom);
        root.setLeft(left);
        root.setCenter(dashboard);

        return back;
    }

    HBox createTopMenu(){
        HBox box = createMenu();
        // create a label username
        Button username = new Button("Welcome, " + getUsername());

        box.getChildren().addAll(username);
        return box;
    }

    HBox createBottomMenu(){
        return createMenu();
    }

    HBox createMenu(){
        HBox box = new HBox();
        box.setStyle("-fx-background-color: #41bcfd;");
        box.setPadding(new Insets(15, 12, 15, 12));
        return box;
    }

    GridPane createDashboard(){
        GridPane grid = new GridPane();

        return grid;
    }

    GridPane adminDash(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
//        product (ProductID, Name, Length, Description, Genre, SellPrice, MaturityRating, RottenTomatoRating, ReleaseDate)

        // create a "Create a new product label"
        // admin only
        Label create = new Label("Create a new product");
        create.setFont(Font.font(headerFont));
        grid.add(create, 0, 0, 2, 1);

        GridPane.setHalignment(create, HPos.CENTER);

        // create a list of label and textfield
        String attributes[] = {"Name: ", "Length: ", "Description: ", "Genre: ", "MaturityRating: ", "ReleaseDate: "};
        TextField fields[] = new TextField[attributes.length];

        for (int att = 0; att < attributes.length; att++) {
            Label l = new Label(attributes[att]);
            grid.add(l, 0, att+1);

            TextField t = new TextField();

            fields[att] = t;

            grid.add(fields[att], 1, att+1);
        }


        return grid;
    }

    VBox createOptionMenue(final GridPane dashboard){
        VBox options = new VBox();
        options.setStyle("-fx-background-color: #aabcfd;");
        options.setPadding(new Insets(15, 12, 15, 12));

        // create an admin button if user is admin
        if(admin){
            Button admin = new Button("Admin");
            options.getChildren().add(admin);

            // if admin is clicked allow admin to add products
            admin.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("COOL");
                    dashboard.getChildren().clear();
                    dashboard.add(adminDash(), 0, 0);
                }
            });
        }

        return options;
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
