package MainApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Expenses {

    private String username;
    private String customerName;
    private Connection dbLink;

    private double x;
    private double y;

    public TextField amount;
    public ComboBox<String> category;

    public Button add;
    public Button back;

    public Button dashboard;
    public Button incomeTracker;
    public Button expensesTracker;
    public Button statisticalReport;
    public Button logout;

    public void initialize(String username, String customerName) throws Exception {
        this.username = username;
        this.customerName = customerName;

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

        add.disableProperty().bind(amount.textProperty().isEmpty().or(category.valueProperty().isNull()));
        category.getItems().addAll("Food", "Transportation", "Grocery", "Health", "Education", "Utilities", "Work", "Miscellaneous");


    }

    public void addExpense(){

        double readInitPersonal = 0, newBudgetPersonalInfo = 0;

        String inc = amount.getText();
        double amt = Double.parseDouble(inc);
        String categ = category.getSelectionModel().getSelectedItem();
        String statement = "INSERT INTO expenses(date, username, category, amount) VALUES ('"+new Timestamp(System.currentTimeMillis())+"','"+ username +"','"+categ+"',"+amt+")";
        String statement1 = "INSERT INTO historyExpenses(date, username, category, amount) VALUES ('"+new Timestamp(System.currentTimeMillis())+"','"+ username +"','"+categ+"',"+amt+")";
        try{
            Statement line = dbLink.createStatement();
            Statement line1 = dbLink.createStatement();
            line.executeUpdate(statement);
            line1.executeUpdate(statement1);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        String readPersonalInfoUpdate = "SELECT initialSavings FROM personal_info WHERE username='"+ username +"'";
        try{
            Statement readPesonalInfoStatement = dbLink.createStatement();
            ResultSet readPersonalInfoQuery = readPesonalInfoStatement.executeQuery(readPersonalInfoUpdate);
            while(readPersonalInfoQuery.next()){
                readInitPersonal = readPersonalInfoQuery.getDouble("initialSavings");
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        newBudgetPersonalInfo = readInitPersonal - amt;
        String writePersonalInfoUpdate = "UPDATE personal_info SET initialSavings= "+newBudgetPersonalInfo+" WHERE username='"+ username +"'";

        try{
            Statement writePersonalInfoStatement = dbLink.createStatement();
            writePersonalInfoStatement.executeUpdate(writePersonalInfoUpdate);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        amount.setText("");
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

        //logout
        try {
            root = main.load();
            Expenses sendUser = main.getController();
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

}
