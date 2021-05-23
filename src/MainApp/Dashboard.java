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
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Dashboard{

    private String customerName;
    private String username;
    private String month_year;
    private Connection dbLink;

    private double x;
    private double y;

    private double totalExpenses;
    private double totalIncome;
    private double savingsGoalDaily;
    private double savingsGoalWeekly;
    private double savingsGoalMonthly;
    private double savingsProgress;

    public Text displayName;
    public Text displaySavings;
    public Text displayTotalExpenses;
    public Text displayTotalIncome;
    public Text displayBudgetLeft;
    public Text displaySavingsDaily;
    public Text displaySavingsWeekly;
    public Text displaySavingsMonthly;
    public TextField monthlyGoal;
    public ImageView incomeButton;
    public ImageView expensesButton;

    public Button dashboard;
    public Button incomeTracker;
    public Button expensesTracker;
    public Button statisticalReport;
    public Button logout;

    public void initialize(String username, String customerName) throws Exception {
        this.username = username;
        this.customerName = customerName;
        displayName.setText(customerName);

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

        /*Every money displayed on the dashboard is reset to 0, except for the budget*/
        if(resetMonth()) reset();

        displayTotalExpenses();
        displayTotalIncome();
        updatedDashboardDb();
        displayBudgetLeft();
    }


    private void displayBudgetLeft(){

        double readInitPersonal=0;
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
        displayBudgetLeft.setText(String.valueOf(readInitPersonal));
    }

    private void displayTotalExpenses(){

        String extractExpenses = "SELECT DATE_FORMAT(date, '%M %Y') as Dates, SUM(amount) as Total FROM expenses WHERE username='"+ username +"' GROUP BY MONTH(date), YEAR(date)";

        try{
            Statement expensesStatement = dbLink.createStatement();
            ResultSet expensesSet = expensesStatement.executeQuery(extractExpenses);
            while(expensesSet.next()){
                month_year = expensesSet.getString("Dates");
                totalExpenses = expensesSet.getDouble("Total");
            }

            displayTotalExpenses.setText(String.valueOf(totalExpenses));

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    private void displayTotalIncome(){

        String extractIncome = "SELECT DATE_FORMAT(date, '%M %Y') as Dates, SUM(amount) as Total FROM income WHERE username='"+ username +"' GROUP BY MONTH(date), YEAR(date)";

        try{
            Statement incomeStatement = dbLink.createStatement();
            ResultSet incomeSet = incomeStatement.executeQuery(extractIncome);
            while(incomeSet.next()){
                month_year = incomeSet.getString("Dates");
                totalIncome = incomeSet.getDouble("Total");
            }

            displayTotalIncome.setText(String.valueOf(totalIncome));

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        savingsProgress = totalIncome - totalExpenses;
        if(savingsProgress < 0){
            savingsProgress = 0;
        }
        displaySavings.setText(String.valueOf(savingsProgress));

    }


    private void updatedDashboardDb(){

        String dashboard = "SELECT * FROM dashboard WHERE month_year='"+month_year+"' AND username='"+ username +"'";
        int count=0;
        try{
            Statement dashboardStatement = dbLink.createStatement();
            ResultSet dashboardSet = dashboardStatement.executeQuery(dashboard);
            while(dashboardSet.next()){
                count++;
            }

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        if(count==0 && month_year != null){
            String update = "INSERT INTO dashboard(month_year, username, savingsProgress, totalExpenses, totalIncome, savingsGoalMonthly) " +
                    "VALUES ('"+ month_year +"','"+ username + "',"+(totalIncome - totalExpenses)+","+totalExpenses+","+totalIncome+","+savingsGoalMonthly+")";
            try{
                Statement query = dbLink.createStatement();
                query.executeUpdate(update);
            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
        else{
            String updateValues = "UPDATE dashboard SET savingsProgress="+(totalIncome - totalExpenses)+", totalExpenses="+totalExpenses+", totalIncome="+totalIncome+" WHERE month_year='"+month_year+"' AND username='"+ username +"'";
            try{
                Statement query = dbLink.createStatement();
                query.executeUpdate(updateValues);
            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }

    }

    private boolean resetMonth(){

        Calendar compare = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM");
        String today = dateFormat.format(compare.getTime());
        compare.add(Calendar.DATE, -1);
        String yesterday = dateFormat.format(compare.getTime());

        return !today.equals(yesterday);

    }

    private void reset(){

        String statement = "INSERT INTO income(date, username, category, amount) VALUES ('"+new Timestamp(System.currentTimeMillis())+"','"+ username +"','None', 0)";
        String statement1 = "INSERT INTO expenses(date, username, category, amount) VALUES ('"+new Timestamp(System.currentTimeMillis())+"','"+ username +"','None', 0)";

        try{
            Statement line = dbLink.createStatement();
            Statement line1 = dbLink.createStatement();
            line.executeUpdate(statement);
            line1.executeUpdate(statement1);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }


    public void displaySavingsPeriodically(){


    }

    public void saveNewGoal(){

    }

    public void enter(){ displaySavingsMonthly.setText(monthlyGoal.getText()); }

    public void income(){

        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/incomeManager.fxml"));
        Parent root;

        try {
            root = main.load();
            Income sendUser = main.getController();
            sendUser.initialize(username,customerName);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void expense(){

        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/expensesManager.fxml"));
        Parent root;

        try {
            root = main.load();
            Expenses sendUser = main.getController();
            sendUser.initialize(username,customerName);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public void statisticalReport(){


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
