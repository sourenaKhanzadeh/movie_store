import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


/**
 * Created by Sourena Khanzadeh on 11/14/2019.
 */
public class User extends Application {


    private Main.userType userType;

    User(Main.userType type){
        this.userType = type;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // root pane
        GridPane root = new GridPane();

        // check if user is an admin or a regular
        if(getType() == Main.userType.Admin)
            admin(root);
        else
            user(root);

        // create a new scene and set scene
        Scene scene = new Scene(root, Main.SCREEN_W, Main.SCREEN_H);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    void admin(Pane root){
                
    }

    void user(Pane root){

    }

    Main.userType getType(){return this.userType;}
}
