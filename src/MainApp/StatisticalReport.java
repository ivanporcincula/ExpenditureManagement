package MainApp;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class StatisticalReport {

    private String username;
    private String customerName;
    private Connection dbLink;
    private double x;
    private double y;

    private double expensesSummary;
    private double incomeSummary;
    private double periodBalance;
    private double cumulativeBalance;
    private PieChart categoricalGraph;

    public Button general1;
    public Button allowance;
    public Button work1;

    public Button general2;
    public Button food;
    public Button transportation;
    public Button grocery;
    public Button health;
    public Button education;
    public Button utilities;
    public Button work2;
    public Button miscellaneous;

    public Button dashboard;
    public Button incomeTracker;
    public Button expensesTracker;
    public Button statisticalReport;
    public Button logout;

    public Pane openMenu;
    public Pane closeMenu;
    public VBox menu;

    public void initialize(String username, String customerName) throws Exception {
        this.username = username;
        this.customerName = customerName;

        /*To connect to the AWS MySQL Database Instance*/
        String schemaName = "user";
        String databaseUser = "dumanyoroporc";
        String databasePassword = "lbycpd2PROJECT";
        String databaseURL = "jdbc:mysql://cpd2-database.c42q90fut081.ap-southeast-1.rds.amazonaws.com:3306/"+schemaName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbLink = DriverManager.getConnection(databaseURL,databaseUser,databasePassword);
        }catch(Exception e){
            e.printStackTrace();
        }

        sideMenu();


    }

    private void sideMenu(){

        menu.setTranslateX(-306);

        openMenu.setOnMouseClicked(e->{
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(menu);
            slide.setToX(0);

            slide.play();

            menu.setTranslateX(-306);

            slide.setOnFinished((ActionEvent d)->{
                openMenu.setVisible(false);
                closeMenu.setVisible(true);
            });
        });

        closeMenu.setOnMouseClicked(e->{
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(menu);
            slide.setToX(-306);

            slide.play();

            menu.setTranslateX(0);

            slide.setOnFinished((ActionEvent d)->{
                openMenu.setVisible(true);
                closeMenu.setVisible(false);
            });
        });
    }

    public void sortByAscending(){

    }

    public void sortByDescending(){

    }

    public void viewByYear(){


    }

    public void viewByMonth(){


    }

    public void viewByWeek(){


    }

    /* BUTTONS FROM THE SIDE MENU */
    public void dashboard() throws Exception {

        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/dashboard.fxml"));
        Parent root;

        //logout
        try {
            root = main.load();
            Dashboard sendUser = main.getController();
            sendUser.initialize(username,customerName);
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




    public void incomeTracker(){

        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/incomeTracker.fxml"));
        Parent root;

        try {
            root = main.load();
            IncomeTracker sendUser = main.getController();
            sendUser.initialize(username,customerName);
            Stage stage = (Stage) incomeTracker.getScene().getWindow();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void expensesTracker(){

        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/expensesTracker.fxml"));
        Parent root;

        try {
            root = main.load();
            ExpenseTracker sendUser = main.getController();
            sendUser.initialize(username,customerName);
            Stage stage = (Stage) expensesTracker.getScene().getWindow();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void statisticalReport(){

        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/statReport.fxml"));
        Parent root;

        try {
            root = main.load();
            StatisticalReport sendUser = main.getController();
            sendUser.initialize(username,customerName);
            Stage stage = (Stage) statisticalReport.getScene().getWindow();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout(){
        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/login.fxml"));
        Parent root;

        //logout
        try {
            root = main.load();
            Stage stage = (Stage) logout.getScene().getWindow();
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
