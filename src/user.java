import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


/**
 * Created by Sourena Khanzadeh on 11/14/2019.
 */
public class User extends Application {

    private Main.userType userType;
    final int headingFont = 22;

    User(Main.userType type){
        this.userType = type;
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        // root pane
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);

        Button back;

        // check if user is an admin or a regular
        if(getType() == Main.userType.Admin)
            back = admin(root);
        else
            back = user(root);

        // go back to main menu if back is clicked
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

    Button admin(GridPane root){
        return menu(root, "Admin");
    }

    Button user(GridPane root){
        return menu(root, "User");
    }

    Button menu(GridPane root, String type){
        // make labels and the text field
        String fieldNames[] = {"First Name: ", "Last Name: ", "Email: ", "Username: "};
        final TextField textFields[] = new TextField[fieldNames.length];

        // create a Heading
        Label welcome = new Label("Welcome " + type);
        welcome.setFont(Font.font(headingFont));
        root.add(welcome, 0, 0, 3, 1);
        GridPane.setHalignment(welcome, HPos.CENTER);

        // display fields
        for (int field = 0; field < fieldNames.length; field++) {
            // label configs
            Label fieldLabel = new Label(fieldNames[field]);
            root.add(fieldLabel, 0, field + 1);

            // text fields configs
            textFields[field] = new TextField();
            root.add(textFields[field], 1, field + 1);
        }

        // create a submit button
        Button submit = new Button("Submit");
        GridPane.setHalignment(submit, HPos.RIGHT);

        final int ty = type.equals("Admin")?1:0;

        // new user is created
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addToDataBase(textFields, ty);
            }
        });


        // add button
        root.add(submit, 1, fieldNames.length + 1);

        // add back button
        Button back = new Button("Back");
        root.add(back, 0, fieldNames.length+1);
        GridPane.setHalignment(back, HPos.LEFT);


        return back;
    }


    void addToDataBase(TextField fields[], int type){
        try{
            // add user to the database
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "root");
            System.out.println("Successfully Connected to the database.... " + con.toString());

            Statement stmt = con.createStatement();

            // generate random id
            int id = (int) (Math.random() * 9999999 + 100000);

            // insert into the database
            stmt.executeUpdate("INSERT INTO movie_user(USERID, FIRSTNAME, LASTNAME, EMAIL, USERNAME)" +
                    "VALUES(" +
                    id + ", '" +
                    fields[0].getText() + "','" +
                    fields[1].getText() + "','" +
                    fields[2].getText() + "','" +
                    fields[3].getText() + "')");


            // check if user is admin or regular
            switch (type){
                case 1:
                    stmt.executeUpdate("INSERT INTO ADMIN(ADMINID)VALUES(" + id + ")");
                    break;
                default:
                    stmt.executeUpdate("INSERT INTO CUSTOMER(CUSTOMERID)VALUES(" + id + ")");
                    break;

            }

            //Alert the user
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New User Created");
            alert.setHeaderText("Success \u263A");
            alert.setContentText(fields[0].getText() + " Successfully Added To the database");
            alert.showAndWait();

            System.out.println("Successfully Added To the database.... " + con.toString());

        }catch (Exception e){
            System.out.println("Could not connect to the database....." + e);
        }
    }


    Main.userType getType(){return this.userType;}
}
