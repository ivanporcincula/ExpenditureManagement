package MainApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Dashboard {

    private double x;
    private double y;

    private String customerName;
    private String currentUser;
    private double savingsUponRegistration;
    private double savings;
    private double totalExpenses;
    private double totalIncome;
    private double budgetLeft;
    private double savingsGoalDaily;
    private double savingsGoalWeekly;

    public ImageView logout;
    public Text displayName;
    public Text displaySavings;
    public Text displayTotalExpenses;
    public Text displayTotalIncome;
    public Text displayBudgetLeft;
    public Text displaySavingsGoalDaily;
    public Text displaySavingsGoalWeekly;
    public Text displaySavingsMonthly;
    public TextField monthlyGoal;
    public Button incomeButton;
    public Button expensesButton;


    public Connection dbLink;



    public void initialize(){


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

            while(queryRes.next()) {
                currentUser=queryRes.getString("username");
                savingsUponRegistration=queryRes.getDouble("initialSavings");
                customerName=queryRes.getString("customerName");
            }
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        budgetLeft = budgetLeft + savingsUponRegistration;

        //first time login after registration
        if (savingsUponRegistration != 0){
            //String selectToRemove = "SELECT * FROM personal_info where username= '" + currentUser +"'";
            String changeInitialSavings = "REPLACE INTO personal_info(username, initialSavings) VALUES('"+currentUser+"',"+0+"";

            try{
                Statement zero = dbLink.createStatement();
                zero.executeUpdate(changeInitialSavings);

            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }


        //displayName.setText(customerName);
        //displaySavings.setText(""+savings);

        updateTotalIncome();
        updateTotalExpenses();
        updateSavingsGoalDaily();
        updateSavingsGoalWeekly();
        updateBudgetLeft();

    }

    public void addExpense(){
        System.out.println(customerName);
        System.out.println(savingsUponRegistration);
    }


    public void income(){

        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/incomeManager.fxml")); //loads the dashboard
        Parent root;

        //opens the dashboard
        try {
            root = main.load();
            Stage stage = (Stage) incomeButton.getScene().getWindow();
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
    public void expense(){

        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/dashboard.fxml")); //loads the dashboard
        Parent root;

        //opens the dashboard
        try {
            root = main.load();
            Stage stage = (Stage) expensesButton.getScene().getWindow();
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


    public void updateTotalExpenses(){

        String checkTotal= "SELECT amount FROM expenses where username= '"+currentUser+"'";
        double total = 0;

        try{
            Statement line = dbLink.createStatement();
            ResultSet queryRes = line.executeQuery(checkTotal);

            while(queryRes.next()) total += queryRes.getDouble("amount");
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();

        }
        totalExpenses = total;
        displayTotalExpenses.setText(""+total);
    }

    public void updateTotalIncome(){

        String checkTotal= "SELECT amount FROM income where username= '"+currentUser+"'";
        double total = 0;

        try{
            Statement line = dbLink.createStatement();
            ResultSet queryRes = line.executeQuery(checkTotal);
            while(queryRes.next()) total += queryRes.getDouble("amount");
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        totalIncome = total;
        displayTotalIncome.setText(""+total);


    }

    public void updateBudgetLeft(){
        budgetLeft = budgetLeft + totalIncome - totalExpenses;
        displayBudgetLeft.setText(""+budgetLeft);

    }

    public void updateSavingsGoalDaily(){

    }

    public void updateSavingsGoalWeekly(){

    }

    public void enter(){ displaySavingsMonthly.setText(monthlyGoal.getText()); }


    public void logout(){

        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/login.fxml")); //loads the dashboard
        Parent root;

        //opens the dashboard
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
