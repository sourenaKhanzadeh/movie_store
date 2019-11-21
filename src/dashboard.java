import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.LinkedList;

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

    Button userExist(BorderPane root) throws FileNotFoundException {
        //create top menu
        HBox top = createTopMenu();
        HBox bottom = createBottomMenu();

        // create Dashboard
        GridPane dashboard = createDashboard();
        dashboard.setAlignment(Pos.CENTER);

        VBox left = createOptionMenu(dashboard);


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
        grid.setHgap(5);
        grid.setVgap(10);
//        product (ProductID, Name, Length, Description, Genre, SellPrice, MaturityRating, RottenTomatoRating, ReleaseDate)

        // create a "Create a new product label"
        // admin only
        Label create = new Label("Create a new product");
        create.setFont(Font.font(headerFont));
        grid.add(create, 0, 0, 2, 1);

        GridPane.setHalignment(create, HPos.CENTER);

        // create a list of label and textfield
        String attributes[] = {"Name: ", "Length: ", "Description: ", "Genre: ","Sell Price:", "ReleaseDate: "};
        final TextField fields[] = new TextField[attributes.length];


        // create them
        for (int att = 0; att < attributes.length; att++) {
            Label l = new Label(attributes[att]);
            grid.add(l, 0, att+1);

            TextField t = new TextField();

            fields[att] = t;

            grid.add(fields[att], 1, att+1);
        }

        // create a label for choice box
        Label productType = new Label("Product Type: ");
        grid.add(productType,0, attributes.length + 1);

        // create a choice box
        final ChoiceBox cb = new ChoiceBox();
        cb.setItems(FXCollections.observableArrayList(
                        "Movie", "Music", "TV Series")
        );


        grid.add(cb, 1, attributes.length + 1);

        // create a  "create" button
        Button createBTN = new Button("Create");
        createBTN.setMinWidth(100);


        // if create is clicked
        createBTN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    // add user to the database
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "root");
                    System.out.println("Successfully Connected to the database.... " + con.toString());

                    Statement stmt = con.createStatement();

                    // generate random id
                    int id = (int) (Math.random() * 9999999 + 100000);

                    stmt.executeUpdate("INSERT INTO product " +
                            "(ProductID, Name, Length, Description, Genre, SellPrice, ReleaseDate)" +
                            "VALUES("+ id +
                            ", '" + fields[0].getText() + "'" +
                            ", " + fields[1].getText() +
                            ", '" + fields[2].getText()+"'" +
                            ", '" + fields[3].getText() + "'" +
                            ", '" + fields[4].getText() + "'"+
                            ", TO_DATE('" + fields[5].getText()+ "'" +
                            ", 'yyyy/mm/dd'))");

                    // check if product is movie/Tv_serenes/music
                    if(cb.getValue().equals("Movie")){
                        stmt.executeUpdate("INSERT INTO movie(MovieID)" +
                                "VALUES(" + id + ")");
                    }else if(cb.getValue().equals("Music")){
                        stmt.executeUpdate("INSERT INTO music(SongID, ArtistName)VALUES(" + id + ", ' ')");
                    }else{
                        stmt.executeUpdate("INSERT INTO tvseries(SeriesID, Seasons, Episodes, TV_CAST)VALUES(" + id + ", 0, 0, ' ')");
                    }

                    System.out.println("product added to the database.....");

                }catch (Exception e){
                    System.out.println("Could not connect to the database....." + e);
                }
            }
        });

        GridPane.setHalignment(createBTN, HPos.CENTER);
        grid.add(createBTN, 0, attributes.length + 2);

        return grid;
    }

    GridPane homeDash() throws FileNotFoundException {
        GridPane grid = new GridPane();
        //Passing FileInputStream object as a parameter
        FileInputStream inputstream = new FileInputStream("D:\\Users\\ProgrammingProjects\\Java\\movie_store_front\\movie_front_logo.png");
        Image image = new Image(inputstream);

        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(400);
        imageView.setFitWidth(400);

        grid.add(imageView, 0, 0);

        return grid;
    }

    ScrollPane productDash(GridPane dashboard) throws FileNotFoundException{
        ScrollPane scroll = new ScrollPane();

        GridPane grid = new GridPane();

        grid.setPadding(new Insets(10, 10 ,10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        queryProducts(grid, dashboard);

        scroll.setContent(grid);
        return scroll;
    }

    VBox createOptionMenu(final GridPane dashboard) throws FileNotFoundException {
        VBox options = new VBox();
        options.setStyle("-fx-background-color: #aabcfd;");
        options.setPadding(new Insets(15, 12, 15, 12));
        options.setSpacing(10);

        // create a home button
        Button home = new Button("Home");
        options.getChildren().add(home);

        dashboard.add(homeDash(), 0, 0);

        // if home is pressed then set dashboard to home
        home.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dashboard.getChildren().clear();
                try {
                    dashboard.getChildren().add(homeDash());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        // create an admin button if user is admin
        if(admin){
            Button admin = new Button("Admin");
            options.getChildren().add(admin);

            // if admin is clicked allow admin to add products
            admin.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    dashboard.getChildren().clear();
                    dashboard.add(adminDash(), 0, 0);
                }
            });
        }

        // create a product button
        Button product = new Button("Product");

        // show all products if product btn is clicked
        product.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dashboard.getChildren().clear();
                try {
                    dashboard.add(productDash(dashboard), 0, 0);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });

        options.getChildren().add(product);

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

    void queryProducts(GridPane grid, final GridPane dashboard){
        Connection conn1 = null;
        try {
            // registers Oracle JDBC driver - though this is no longer required
            // since JDBC 4.0, but added here for backward compatibility
            Class.forName("oracle.jdbc.OracleDriver");


            //   String dbURL1 = "jdbc:oracle:thin:username/password@oracle.scs.ryerson.ca:1521:orcl";


            String dbURL1 = "jdbc:oracle:thin:system/root@localhost:1521:xe";

            conn1 = DriverManager.getConnection(dbURL1);
            if (conn1 != null) {
                System.out.println("Connected with connection #1");
            }





            String query = "SELECT * FROM product";

            try (Statement stmt = conn1.createStatement()) {

                ResultSet rs = stmt.executeQuery(query);


                int i = 0, j= 0;
                // query all the products
                while (rs.next()) {
                    VBox box = new VBox();

                    final String name = rs.getString("NAME");
                    final int id = rs.getInt("ProductID");

                    Label nLabel = new Label(name);
                    try {
                        FileInputStream inputstream = new FileInputStream("D:\\Users\\ProgrammingProjects\\Java\\movie_store_front\\movie_poster.jpg");
                        Image image = new Image(inputstream);


                        ImageView imageView = new ImageView(image);

                        // if image is clicked on
                        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                Product product = new Product(id);
                                LinkedList<Object> attrs = product.getProduct();

                                dashboard.getChildren().clear();

                                int type = product.getType();

                                LinkedList<String> fields = new LinkedList<>();
                                fields.add("Name ");
                                fields.add("Length ");
                                fields.add("Description ");
                                fields.add("Genre ");
                                fields.add("SellPrice ");
                                fields.add("ReleaseDate ");

                                switch (type){
                                    case 0:
                                        fields.add("Movie_Casts ");
                                        fields.add("Languages ");
                                        fields.add("Director ");
                                        fields.add("Country ");
                                        fields.add("MaturityRating ");
                                        break;
                                    case 1:
                                        fields.add("NO.Seasons ");
                                        fields.add("NO.Episodes ");
                                        fields.add("TV_Cast ");
                                        fields.add("MaturityRating ");
                                        fields.add("Rotten_Tomato_Rating ");
                                        break;
                                    default:
                                        fields.add("Artist_Name ");
                                        break;

                                }

                                int i=0;
                                for(String field:fields){
                                    Label l = new Label(field);
                                    dashboard.add(l, i, 0);
                                    i++;
                                }

                                i = 0;
                                for (Object attr:attrs){
                                    if(attr != null) {
                                        Label l = new Label(attr.toString());
                                        dashboard.add(l, i, 1);
                                        i++;
                                    }
                                }
                                dashboard.setHgap(10);
                                dashboard.setVgap(10);


                            }
                        });

                        imageView.setFitHeight(150);
                        imageView.setFitWidth(100);

                        box.getChildren().add(imageView);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    box.getChildren().add(nLabel);
                    grid.add(box, j, i);
                    if(j == 3){i++;j=-1;}
                    j++;


                }

//                grid.add();
            } catch (SQLException e) {
                System.out.println(e.getErrorCode());
            }



        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
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

}
