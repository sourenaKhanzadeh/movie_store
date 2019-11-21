import javafx.scene.layout.GridPane;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by Owner on 11/20/2019.
 */
public class Product {

    private int id;
    private LinkedList<Object> objs;

    final int MOVIE = 0;
    final int SERIES = 1;
    final int MUSIC = 2;

    int type;

    Product(int id){
        this.id = id;
        this.objs = new LinkedList<>();
    }


    LinkedList<Object> getProduct(){
        try{
            // add user to the database
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "root");
            System.out.println("Successfully Connected to the database.... " + con.toString());

            try(Statement stmt = con.createStatement()) {


                ResultSet rs = stmt.executeQuery("SELECT * FROM product LEFT JOIN movie on MovieID=ProductID " +
                        "LEFT JOIN tvseries ON SeriesID=ProductID " +
                        "LEFT JOIN music ON ProductID=music.SongID WHERE ProductID='" + getId() + "'");
                rs.next();

                // store all types of id
                int type[] = {rs.getInt("MovieID"), rs.getInt("SeriesID"),rs.getInt("SongID")};

                String name = rs.getString("Name");
                int length = rs.getInt("Length");
                String desc = rs.getString("Description");
                String genre = rs.getString("Genre");
                double price = rs.getDouble("SellPrice");
                Date date = rs.getDate("ReleaseDate");

                objs.add(name);
                objs.add(length);
                objs.add(desc);
                objs.add(genre);
                objs.add(price);
                objs.add(date);

                // check product type
                if(type[MOVIE] != 0){
                    // TYPE MOVIE
                    this.type = MOVIE;
                    String cast = rs.getString("Movie_Cast");
                    String lang = rs.getString("Languages");
                    String director = rs.getString("Director");
                    String country = rs.getString("Country");

                    objs.add(cast);
                    objs.add(lang);
                    objs.add(director);
                    objs.add(country);

                }else if(type[SERIES] != 0){
                    // TYPE SERIES
                    this.type = SERIES;
                    int seasons = rs.getInt("Seasons");
                    int episodes = rs.getInt("Episodes");
                    String cast = rs.getString("TV_Cast");

                    objs.add(seasons);
                    objs.add(episodes);
                    objs.add(cast);

                }else{
                    //TYPE MUSIC
                    this.type = MUSIC;
                    objs.add(rs.getString("ArtistName"));
                }

                if(type[MOVIE] != 0 || type[SERIES] !=0){
                    String maturity = rs.getString("MaturityRating");
                    String rotten = rs.getString("RottenTomatoRating");

                    objs.add(maturity);
                    objs.add(rotten);
                }


            } catch (SQLException e) {
                System.out.println("Couldn't find product: " + e.toString());
            }

        }catch (Exception e){
            System.out.println("Could not connect to the database....." + e.toString());
        }

        return this.objs;
    }



    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }
}
