package MainApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    private double totalExpenses;
    private double totalIncome;
    private double budgetLeft;
    private double savingsGoalDaily;
    private double savingsGoalWeekly;
    private double savingsGoalMonthly;
    private double savingsProgress;
    private int getDate;
    private int size;
    private int newSize = size + 1;
    private boolean resetMonth = false;

    //public ImageView logout;
    public Text displayName;
    public Text displaySavings;
    public Text displayTotalExpenses;
    public Text displayTotalIncome;
    public Text displayBudgetLeft;
    public Text displaySavingsDaily;
    public Text displaySavingsWeekly;
    public Text displaySavingsMonthly;
    public TextField monthlyGoal;
    public Button incomeButton;
    public Button expensesButton;


    public Connection dbLink;

    public Button logout;



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

        //to check which user is currently logged in
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

        //the initial savings is the same as the budget left that the user has
        //it is updated every time a user makes a transaction in the application
        budgetLeft = savingsUponRegistration;

        //displayName.setText(customerName);


        resetMonth = resetMonthlyCycle();
        //System.out.println(resetMonthlyCycle());
        if(resetMonth){
            //reset();
        }
        else{
            //updateTotalExpenses();
            //updateTotalIncome();
            //updateSavingsMonthly();
            //displayInfo();
        }

        //updateBudgetLeft();

    }


    public void updateBudgetLeft(){

        budgetLeft = budgetLeft + totalIncome - totalExpenses;

        String changeInitialSavings = "UPDATE personal_info SET initialSavings = "+budgetLeft+" WHERE username="+currentUser;

        try{
            Statement zero = dbLink.createStatement();
            zero.executeUpdate(changeInitialSavings);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void updateTotalExpenses(){

        String extractingExpenses= "SELECT EXTRACT(YEAR_MONTH from date) as Month_Year, sum(Amount) as Total FROM expenses WHERE username ='"+currentUser+"'";
        try{
            Statement table = dbLink.createStatement();
            ResultSet rq = table.executeQuery(extractingExpenses);

            while(rq.next()){
                getDate = rq.getInt("Month_Year");
                totalExpenses = rq.getDouble("Total");

                String update = "INSERT INTO dashboard(month_year, username, savingsProgress, totalExpenses, totalIncome, savingsMonthly) " +
                        "VALUES ("+getDate+","+currentUser+","+(totalIncome-totalExpenses)+","+totalExpenses+","+totalIncome+","+savingsGoalMonthly+")" +
                        "ON DUPLICATE KEY UPDATE totalExpenses="+totalExpenses+",savingsProgress="+(totalIncome-totalExpenses)+"";
                try{
                    Statement query = dbLink.createStatement();
                    query.executeUpdate(update);


                }catch (Exception e){
                    e.printStackTrace();
                    e.getCause();
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }


    public void updateTotalIncome(){

        String extractingIncome= "SELECT EXTRACT(YEAR_MONTH from date) as Month_Year, sum(Amount) as Total FROM income WHERE username ='"+currentUser+"'";
        try{
            Statement table = dbLink.createStatement();
            ResultSet rq = table.executeQuery(extractingIncome);

            while(rq.next()){
                getDate = rq.getInt("Month_Year");
                totalIncome = rq.getDouble("Total");

                String update = "INSERT INTO dashboard(month_year, username, savingsProgress, totalExpenses, totalIncome, savingsMonthly) " +
                        "VALUES ("+getDate+","+currentUser+","+(totalIncome-totalExpenses)+","+totalExpenses+","+totalIncome+","+savingsGoalMonthly+")" +
                        "ON DUPLICATE KEY UPDATE totalIncome="+totalIncome+",savingsProgress="+(totalIncome-totalExpenses)+"";
                try{
                    Statement query = dbLink.createStatement();
                    query.executeUpdate(update);

                }catch (Exception e){
                    e.printStackTrace();
                    e.getCause();
                }
            }


        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    private boolean resetMonthlyCycle(){
        String checkRows = "SELECT * FROM dashboard";

        try{
            Statement count = dbLink.createStatement();
            ResultSet set = count.executeQuery(checkRows);
            while(set.next()) size++;

            if(size == 0) newSize=newSize+1;

            return size==newSize;

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return false;
    }


    public void reset(){

        totalExpenses=0;
        totalIncome=0;
        displayTotalIncome.setText(String.valueOf(totalIncome));
        displaySavings.setText(String.valueOf(totalIncome-totalExpenses));
        displayTotalExpenses.setText(String.valueOf(totalExpenses));
        newSize=newSize+1;

    }

    private void displayInfo(){
        displayTotalIncome.setText(String.valueOf(totalIncome));
        displaySavings.setText(String.valueOf(totalIncome-totalExpenses));
        displayTotalExpenses.setText(String.valueOf(totalExpenses));
        displayBudgetLeft.setText(String.valueOf(budgetLeft));
    }

    public void off(MouseEvent event){
        if(event.getSource() != logout){
            System.out.println("AH");
        }
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

        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/expensesManager.fxml")); //loads the dashboard
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

    public void displaySavingsPeriodically(){


    }

    public void saveNewGoal(){

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
