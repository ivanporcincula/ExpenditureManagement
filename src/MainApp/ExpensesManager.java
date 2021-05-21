package MainApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ExpensesManager {

    private String currentUser;
    private double x;
    private double y;

    public TextField amount;
    public ComboBox<String> category;

    private Connection dbLink;

    public Button add;
    public Button back;

    public void initialize(){
        add.setDisable(true);
        category.getItems().addAll("Food", "Transportation", "Grocery", "Health", "Education", "Utilities", "Work", "Miscellaneous");

        //To connect to the AWS MySQL Database Instance
        String schemaName = "user";
        String databaseUser = "dumanyoroporc";
        String databasePassword = "lbycpd2PROJECT";

        //url of the database instance host
        String databaseURL = "jdbc:mysql://cpd2-database.c42q90fut081.ap-southeast-1.rds.amazonaws.com:3306/"+schemaName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbLink = DriverManager.getConnection(databaseURL,databaseUser,databasePassword);
        }catch(Exception e){
            e.printStackTrace();
        }

        String checkLog = "SELECT * FROM logs ORDER BY log_no DESC LIMIT 1";

        try{

            Statement line = dbLink.createStatement();
            ResultSet queryRes = line.executeQuery(checkLog);

            while(queryRes.next()) currentUser=queryRes.getString("username");

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        System.out.println(currentUser);


    }

    public void addExpense(){

        String inc = amount.getText();
        double amt = Double.parseDouble(inc);
        String categ = category.getSelectionModel().getSelectedItem();
        String statement = "INSERT INTO expenses(date, username, category, amount) VALUES ('"+new Timestamp(System.currentTimeMillis())+"','"+currentUser+"','"+categ+"',"+amt+")";
        String statement1 = "INSERT INTO historyExpenses(date, username, category, amount) VALUES ('"+new Timestamp(System.currentTimeMillis())+"','"+currentUser+"','"+categ+"',"+amt+")";
        try{
            Statement line = dbLink.createStatement();
            Statement line1 = dbLink.createStatement();
            line.executeUpdate(statement);
            line1.executeUpdate(statement1);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        amount.setText("");
    }

    public void infoFilled(){
        add.setDisable(amount.getText().isEmpty() || category.getSelectionModel().getSelectedItem().isEmpty());
    }

    public void back(){
        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/dashboard.fxml")); //loads the dashboard
        Parent root;

        //opens the dashboard
        try {
            root = main.load();
            Stage stage = (Stage) back.getScene().getWindow();
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
