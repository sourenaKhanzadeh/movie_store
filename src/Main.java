/**
 * Created by Owner on 9/7/2019.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;




public class Main  extends Application{

    final static int SCREEN_W = 600;
    final static int SCREEN_H = 300;
    final static String TITLE = "Movie Front Store";

    final double H_GAP = 10;
    final double V_GAP = 10;

    static enum userType {User , Admin}

    //TODO: DOCUMENT PROPERLY

    @Override
    public void start(final Stage primaryStage) throws Exception {

        // root pane
        GridPane root = new GridPane();
        // configure the root pane
        root.setAlignment(Pos.CENTER);
        root.setGridLinesVisible(false);
        root.setHgap(H_GAP);
        root.setVgap(V_GAP);

        // Label for user
        Label userLabel = new Label("Create A New: ");

        // add to the root
        root.add(userLabel, 0, 0);

        // create three buttons
        int userManagementNum = 2;
        String userManagementNames[] = {"Admin", "User"};
        Button userManagementBTNS[] = new Button[userManagementNum];

        // create the buttons and add to the root
        for (int col = 0; col < userManagementNum; col++) {
            userManagementBTNS[col] = new Button();
            userManagementBTNS[col].setText(userManagementNames[col]);
            root.add(userManagementBTNS[col], col, 1);
        }

        // if admin btn is clicked
        userManagementBTNS[0].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                User admin = new User(userType.Admin);
                try {
                    admin.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // if user btn is clicked
        userManagementBTNS[1].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                User user = new User(userType.User);
                try {
                    user.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });




        // create a sign in label
        Label sign = new Label("Sign In");

        // add sign to the root
        root.add(sign, 0, 2);

        // username text box to sign in
        final TextField username = new TextField();
        Label user = new Label("Username: ");

        // add text box to the root
        root.add(user, 0, 3);
        root.add(username, 1, 3, 2, 1);

        // sign in button
        Button signBTN = new Button("Sign In");
        signBTN.setAlignment(Pos.BASELINE_RIGHT);

        // user is signing in
        signBTN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Dashboard user = new Dashboard(username.getText());
                try {
                    user.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // add the button
        root.add(signBTN, 2, 4);

        // create a btn that allows deletion/creation of the tables
        Button initBTN = new Button("Initialize/Recreate");

        initBTN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Initiate init = new Initiate();
                init.initiate();

                // Alert the user
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Initiation Successful");
                alert.setHeaderText("restarted/initiated tables....");
                alert.showAndWait();
            }
        });

        // add the button
        root.add(initBTN, 0, 5);

        // create a new scene
        Scene scene = new Scene(root, SCREEN_W, SCREEN_H);

        // create the stage
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}


