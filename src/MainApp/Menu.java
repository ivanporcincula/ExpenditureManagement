package MainApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Menu {

    public Button dashboard;
    public Button income;
    public Button expenses;
    public Button statistics;

    private double x;
    private double y;

    public void initialize(){

    }


    public void dashboard(){
        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/dashboard.fxml")); //loads the dashboard
        Parent root;

        //opens the dashboard
        try {
            root = main.load();
            Stage stage = (Stage) dashboard.getScene().getWindow();
            root.setOnMousePressed(e->{
                x = e.getSceneX();
                y = e.getSceneY();
            });

            root.setOnMouseDragged(e->{
                stage.setX(e.getScreenX()-x);
                stage.setY(e.getScreenY()-y);
            });
            stage.setTitle("Monrec");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void income(){
        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/income.fxml")); //loads the dashboard
        Parent root;

        //opens the dashboard
        try {
            root = main.load();
            Stage stage = (Stage) dashboard.getScene().getWindow();
            root.setOnMousePressed(e->{
                x = e.getSceneX();
                y = e.getSceneY();
            });

            root.setOnMouseDragged(e->{
                stage.setX(e.getScreenX()-x);
                stage.setY(e.getScreenY()-y);
            });
            stage.setTitle("Monrec");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void expenses(){

        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/expenses.fxml")); //loads the dashboard
        Parent root;

        //opens the dashboard
        try {
            root = main.load();
            Stage stage = (Stage) expenses.getScene().getWindow();
            root.setOnMousePressed(e->{
                x = e.getSceneX();
                y = e.getSceneY();
            });

            root.setOnMouseDragged(e->{
                stage.setX(e.getScreenX()-x);
                stage.setY(e.getScreenY()-y);
            });
            stage.setTitle("Monrec");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void statistics(){

        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/statistics.fxml")); //loads the dashboard
        Parent root;

        //opens the dashboard
        try {
            root = main.load();
            Stage stage = (Stage) statistics.getScene().getWindow();
            root.setOnMousePressed(e->{
                x = e.getSceneX();
                y = e.getSceneY();
            });

            root.setOnMouseDragged(e->{
                stage.setX(e.getScreenX()-x);
                stage.setY(e.getScreenY()-y);
            });
            stage.setTitle("Monrec");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}
