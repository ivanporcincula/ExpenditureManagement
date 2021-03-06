package MainApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class Main extends Application {

    private double x;
    private double y;

    @Override
    public void start(Stage primaryStage) throws Exception{

        SimpleDateFormat formatter = new SimpleDateFormat("MMMMMMMMM yyyy");
        Date date = new Date();
        System.out.println(formatter.format(date));

        Parent root = FXMLLoader.load(getClass().getResource("GUI/login.fxml"));

        root.setOnMousePressed(e->{
            x = e.getSceneX();
            y = e.getSceneY();
        });

        root.setOnMouseDragged(e->{
            primaryStage.setX(e.getScreenX()-x);
            primaryStage.setY(e.getScreenY()-y);
        });
        primaryStage.setTitle("Monrec");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
