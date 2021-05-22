package MainApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Dashboard{

    private double x;
    private double y;

    private String customerName;
    private String currentUser;
    private double savingsUponRegistration;
    private double totalExpenses;
    private double totalIncome;
    private double savingsGoalDaily;
    private double savingsGoalWeekly;
    private double savingsGoalMonthly;
    private double savingsProgress;
    private int getDate;
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
    public ImageView incomeButton;
    public ImageView expensesButton;


    public Connection dbLink;

    public Button logout;



    public void initialize() throws Exception {


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
        loggedInUser();

        //the initial savings is the same as the budget left that the user has
        //it is updated every time a user makes a transaction in the application

        displayName.setText(customerName);

        resetMonth = resetMonthlyCycle();

        if(resetMonth){
            System.out.println("HELLO");
            reset();
        }
        else{
            updateTotalExpenses();
            updateTotalIncome();
            displayInfo();
        }

        updateBudgetLeft();

    }

    private void loggedInUser(){

        //to check which user is currently logged in
        String checkLog = "SELECT * FROM logs ORDER BY log_no DESC LIMIT 1";

        try{
            Statement line = dbLink.createStatement();
            ResultSet queryRes = line.executeQuery(checkLog);

            while(queryRes.next()) {
                currentUser=queryRes.getString("username");
                customerName=queryRes.getString("customerName");
            }
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }


    public void updateBudgetLeft(){

        double readInitPersonal=0;

        String readPersonalInfoUpdate = "SELECT initialSavings FROM personal_info WHERE username='"+currentUser+"'";

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

    public void updateTotalExpenses(){

        if(!expensesTableIsEmpty()){

            String extractingExpenses= "SELECT EXTRACT(YEAR_MONTH from date) as Month_Year, sum(Amount) as Total FROM expenses WHERE username ='"+currentUser+"'";
            try{
                Statement table = dbLink.createStatement();
                ResultSet rq = table.executeQuery(extractingExpenses);

                while(rq.next()){

                    getDate = rq.getInt("Month_Year");
                    totalExpenses = rq.getDouble("Total");

                    String update = "INSERT INTO dashboard(month_year, username, savingsProgress, totalExpenses, totalIncome, savingsGoalMonthly) " +
                            "VALUES ("+getDate+",'"+currentUser+"',"+(totalIncome-totalExpenses)+","+totalExpenses+","+totalIncome+","+savingsGoalMonthly+")" +
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

            String update = "DELETE FROM expenses";
            try{
                Statement query = dbLink.createStatement();
                query.executeUpdate(update);

            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }

        }


    }


    public void updateTotalIncome(){

        if(!incomeTableIsEmpty()){

            String extractingIncome= "SELECT EXTRACT(YEAR_MONTH from date) as Month_Year, sum(Amount) as Total FROM income WHERE username ='"+currentUser+"'";

            try{
                Statement table = dbLink.createStatement();
                ResultSet rq = table.executeQuery(extractingIncome);

                while(rq.next()){
                    getDate = rq.getInt("Month_Year");
                    totalIncome = rq.getDouble("Total");

                    String update = "INSERT INTO dashboard(month_year, username, savingsProgress, totalExpenses, totalIncome, savingsGoalMonthly) " +
                            "VALUES ("+getDate+",'"+currentUser+"',"+(totalIncome-totalExpenses)+","+totalExpenses+","+totalIncome+","+savingsGoalMonthly+")" +
                            "ON DUPLICATE KEY UPDATE totalIncome="+totalIncome+",savingsProgress="+(totalIncome-totalExpenses)+"";
                    try{
                        Statement query = dbLink.createStatement();
                        System.out.println("YOU");
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

            String update = "DELETE FROM income";
            try{
                Statement query = dbLink.createStatement();
                query.executeUpdate(update);
            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }

    }

    private boolean incomeTableIsEmpty(){
        String check = "SELECT * FROM income where username='"+currentUser+"'";

        int counter=0;

        try{
            Statement count = dbLink.createStatement();
            ResultSet set = count.executeQuery(check);
            while(set.next()) counter++;

            if(counter == 0) return true;

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return false;

    }

    private boolean expensesTableIsEmpty(){
        String check = "SELECT * FROM expenses where username='"+currentUser+"'";
        int counter=0;

        try{
            Statement count = dbLink.createStatement();
            ResultSet set = count.executeQuery(check);
            while(set.next()) counter++;

            if(counter == 0) return true;

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return false;
    }

    private boolean resetMonthlyCycle() throws  SQLException,Exception {
        String checkRows = "SELECT * FROM dashboard where username='"+currentUser+"'";
        ArrayList<Integer> newMonth = new ArrayList<>();

        Statement count = dbLink.createStatement();
        ResultSet set = count.executeQuery(checkRows);

        while(set.next()){
            int date = set.getInt("month_year");
            newMonth.add(date);
        }

        String insertStatement = "INSERT INTO tableSizes(username, size) VALUES ('"+currentUser+"',"+newMonth.size()+")";
        Statement insert = dbLink.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        insert.executeUpdate(insertStatement);

        newMonth.clear();

        String sizeStatement = "SELECT * FROM tableSizes where username='"+currentUser+"'";
        Statement checkSize = dbLink.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet checking = checkSize.executeQuery(sizeStatement);

        checking.afterLast();
        checking.previous();
        int latestSize = checking.getInt("size");

        if(checking.previous()){

            checking.previous();
            int previousSize = checking.getInt("size");
            System.out.println(latestSize);
            System.out.println(previousSize);
            if(latestSize > previousSize) {
                System.out.println("INSIDE");
                String update = "DELETE FROM tableSizes where username='"+currentUser+"'";
                Statement query = dbLink.createStatement();
                query.executeUpdate(update);

                String insertStatement1 = "INSERT INTO tableSizes(username, size) VALUES ('"+currentUser+"',"+latestSize+")";
                Statement insert1 = dbLink.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                insert1.executeUpdate(insertStatement1);
                insert1.executeUpdate(insertStatement1);
                return true;
            }
            else return false;
        }
        else return false;

    }


    public void reset(){

        totalExpenses=0;
        totalIncome=0;
        displayTotalIncome.setText(String.valueOf(totalIncome));
        displaySavings.setText(String.valueOf(totalIncome-totalExpenses));
        displayTotalExpenses.setText(String.valueOf(totalExpenses));

    }

    private void displayInfo(){
        displayTotalIncome.setText(String.valueOf(totalIncome));
        displaySavings.setText(String.valueOf(totalIncome-totalExpenses));
        displayTotalExpenses.setText(String.valueOf(totalExpenses));

    }

    public void off(MouseEvent event){
        if(event.getSource() == logout){
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

        String update = "DELETE FROM logs";
        try{
            Statement query = dbLink.createStatement();
            query.executeUpdate(update);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/login.fxml")); //loads the dashboard
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
