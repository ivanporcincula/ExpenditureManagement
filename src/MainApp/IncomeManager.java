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

public class IncomeManager {

    private String currentUser;
    private double x;
    private double y;

    public TextField amount;
    public ComboBox<String> category;

    private Connection dbLink;
    private ObservableList<String> arrayCategory;
    public Button add;
    public Button back;

    public void initialize(){

        add.setDisable(true);

        arrayCategory = FXCollections.observableArrayList("Allowance", "Work");
        category = new ComboBox(arrayCategory);
        category.setPromptText("Please select a category...");
        category.setItems(arrayCategory);
        category.setEditable(true);

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


    }

    public void addIncome(){

        String inc = amount.getText();
        double amt = Double.parseDouble(inc);
        String categ = category.getSelectionModel().getSelectedItem();
        String statement = "INSERT INTO income(date, username, category, amt) VALUES ('"+new Timestamp(System.currentTimeMillis())+"','"+currentUser+"','"+categ+"',"+amt+")";

        try{
            Statement line = dbLink.createStatement();
            line.executeUpdate(statement);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
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
