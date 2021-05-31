package MainApp;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatisticalReport {

    private String username;
    private String customerName;
    private Connection dbLink;
    private double x;
    private double y;

    private double expensesSummary;
    private double incomeSummary;
    private PieChart categoricalGraph;
    private ResultSet all;
    private ResultSet incOrExp;
    private String month_year;

    public Button dashboard;
    public Button incomeTracker;
    public Button expensesTracker;
    public Button statisticalReport;
    public Button logout;

    public HBox incomeOrExpenses;
    public Pane generalPane;
    public Pane incomePane;
    public Pane expensesPane;

    public Text displayMonthYear;
    public Text displayTotalIncome;
    public Text displayTotalExpenses;
    public Text displayPeriodBalance;
    public Text displayCumulativeBalance;

    public Text displayMonthYear1;
    public Text displayAllowance;
    public Text displayWork1;

    public Text displayMonthYear2;
    public Text displayTotalInc;
    public Text displayFood;
    public Text displayTransportation;
    public Text displayGrocery;
    public Text displayHealth;
    public Text displayEducation;
    public Text displayUtilities;
    public Text displayWork2;
    public Text displayMisc;
    public Text displayTotalExp;


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

        incomeOrExpenses.setVisible(false);
        generalPane.setVisible(false);
        incomePane.setVisible(false);
        expensesPane.setVisible(false);
        initNewMonth();
        updateCategoryIncome();
        updateCategoryExpenses();


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

    public void comprehensiveView(){
        incomeOrExpenses.setVisible(true);
    }

    public void graphRanking(){
        incomeOrExpenses.setVisible(false);
        generalPane.setVisible(false);
        incomePane.setVisible(false);
        expensesPane.setVisible(false);
    }

    public void all() throws SQLException {
        incomePane.setVisible(false);
        expensesPane.setVisible(false);
        generalPane.setVisible(true);
        DecimalFormat roundOff = new DecimalFormat("###0.00");

        String statement1 = "SELECT * FROM  dashboard WHERE username='"+ username +"'";
        Statement query = dbLink.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        all = query.executeQuery(statement1);

        all.first();
        month_year = all.getString("month_year");
        incomeSummary = all.getDouble("totalIncome");
        expensesSummary = all.getDouble("totalExpenses");
        displayMonthYear.setText(month_year);
        displayTotalIncome.setText(String.valueOf(roundOff.format(incomeSummary)));
        displayTotalExpenses.setText(String.valueOf(roundOff.format(expensesSummary)));
        displayPeriodBalance.setText(String.valueOf(roundOff.format(incomeSummary - expensesSummary)));
        displayCumulativeBalance.setText(String.valueOf(roundOff.format(incomeSummary - expensesSummary)));

    }

    public void nextAll() throws SQLException {
        DecimalFormat roundOff = new DecimalFormat("###0.00");
        all.next();
        month_year = all.getString("month_year");
        incomeSummary = all.getDouble("totalIncome");
        expensesSummary = all.getDouble("totalExpenses");
        displayMonthYear.setText(month_year);
        displayTotalIncome.setText(String.valueOf(roundOff.format(incomeSummary)));
        displayTotalExpenses.setText(String.valueOf(roundOff.format(expensesSummary)));
        displayPeriodBalance.setText(String.valueOf(roundOff.format(incomeSummary - expensesSummary)));
        displayCumulativeBalance.setText(String.valueOf(roundOff.format(incomeSummary - expensesSummary)));

    }

    public void previousAll() throws SQLException {
        DecimalFormat roundOff = new DecimalFormat("###0.00");
        all.previous();
        month_year = all.getString("month_year");
        incomeSummary = all.getDouble("totalIncome");
        expensesSummary = all.getDouble("totalExpenses");
        displayMonthYear.setText(month_year);
        displayTotalIncome.setText(String.valueOf(roundOff.format(incomeSummary)));
        displayTotalExpenses.setText(String.valueOf(roundOff.format(expensesSummary)));
        displayPeriodBalance.setText(String.valueOf(roundOff.format(incomeSummary - expensesSummary)));
        displayCumulativeBalance.setText(String.valueOf(roundOff.format(incomeSummary - expensesSummary)));

    }


    public void income() throws Exception{
        incomePane.setVisible(true);
        expensesPane.setVisible(false);
        generalPane.setVisible(false);

        double allowance, work;
        DecimalFormat roundOff = new DecimalFormat("###0.00");

        String query = "SELECT * FROM statistical_report WHERE username='"+username+"'";
        Statement statement = dbLink.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        incOrExp = statement.executeQuery(query);

        incOrExp.first();
        month_year = incOrExp.getString("month_year");
        allowance = incOrExp.getDouble("allowance");
        work = incOrExp.getDouble("work1");

        displayMonthYear1.setText(month_year);
        displayAllowance.setText(""+roundOff.format(allowance));
        displayWork1.setText(""+roundOff.format(work));
        displayTotalInc.setText(""+roundOff.format(allowance+work));

    }

    public void nextIncome() throws SQLException {
        double allowance, work;
        DecimalFormat roundOff = new DecimalFormat("###0.00");

        incOrExp.next();
        month_year = incOrExp.getString("month_year");
        allowance = incOrExp.getDouble("allowance");
        work = incOrExp.getDouble("work1");

        displayMonthYear1.setText(month_year);
        displayAllowance.setText(""+roundOff.format(allowance));
        displayWork1.setText(""+roundOff.format(work));
        displayTotalInc.setText(""+roundOff.format(allowance+work));


    }

    public void previousIncome() throws SQLException {

        double allowance, work;
        DecimalFormat roundOff = new DecimalFormat("###0.00");

        incOrExp.previous();
        month_year = incOrExp.getString("month_year");
        allowance = incOrExp.getDouble("allowance");
        work = incOrExp.getDouble("work1");

        displayMonthYear1.setText(month_year);
        displayAllowance.setText(""+roundOff.format(allowance));
        displayWork1.setText(""+roundOff.format(work));
        displayTotalInc.setText(""+roundOff.format(allowance+work));

    }

    public void expenses() throws Exception{
        incomePane.setVisible(false);
        expensesPane.setVisible(true);
        generalPane.setVisible(false);

        double food, transportation, grocery, health, education, utilities, work,miscellaneous;
        DecimalFormat roundOff = new DecimalFormat("###0.00");

        String query = "SELECT * FROM statistical_report WHERE username='"+username+"'";
        Statement statement = dbLink.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        incOrExp = statement.executeQuery(query);

        incOrExp.first();
        month_year = incOrExp.getString("month_year");
        food = incOrExp.getDouble("food");
        transportation = incOrExp.getDouble("transportation");
        grocery = incOrExp.getDouble("grocery");
        health = incOrExp.getDouble("health");
        education = incOrExp.getDouble("education");
        utilities = incOrExp.getDouble("utilities");
        work = incOrExp.getDouble("work2");
        miscellaneous = incOrExp.getDouble("miscellaneous");

        displayMonthYear2.setText(month_year);
        displayFood.setText(""+roundOff.format(food));
        displayTransportation.setText(""+roundOff.format(transportation));
        displayGrocery.setText(""+roundOff.format(grocery));
        displayHealth.setText(""+roundOff.format(health));
        displayEducation.setText(""+roundOff.format(education));
        displayUtilities.setText(""+roundOff.format(utilities));
        displayWork2.setText(""+roundOff.format(work));
        displayMisc.setText(""+roundOff.format(miscellaneous));
        displayTotalExp.setText(""+roundOff.format(food+transportation+grocery+health+education+utilities+work+miscellaneous));
    }

    public void nextExpenses() throws SQLException {

        double food, transportation, grocery, health, education, utilities, work,miscellaneous;
        DecimalFormat roundOff = new DecimalFormat("###0.00");

        incOrExp.next();
        month_year = incOrExp.getString("month_year");
        food = incOrExp.getDouble("food");
        transportation = incOrExp.getDouble("transportation");
        grocery = incOrExp.getDouble("grocery");
        health = incOrExp.getDouble("health");
        education = incOrExp.getDouble("education");
        utilities = incOrExp.getDouble("utilities");
        work = incOrExp.getDouble("work2");
        miscellaneous = incOrExp.getDouble("miscellaneous");

        displayMonthYear2.setText(month_year);
        displayFood.setText(""+roundOff.format(food));
        displayTransportation.setText(""+roundOff.format(transportation));
        displayGrocery.setText(""+roundOff.format(grocery));
        displayHealth.setText(""+roundOff.format(health));
        displayEducation.setText(""+roundOff.format(education));
        displayUtilities.setText(""+roundOff.format(utilities));
        displayWork2.setText(""+roundOff.format(work));
        displayMisc.setText(""+roundOff.format(miscellaneous));
        displayTotalExp.setText(""+roundOff.format(food+transportation+grocery+health+education+utilities+work+miscellaneous));

    }

    public void previousExpenses() throws SQLException {
        double food, transportation, grocery, health, education, utilities, work,miscellaneous;
        DecimalFormat roundOff = new DecimalFormat("###0.00");

        incOrExp.previous();
        month_year = incOrExp.getString("month_year");
        food = incOrExp.getDouble("food");
        transportation = incOrExp.getDouble("transportation");
        grocery = incOrExp.getDouble("grocery");
        health = incOrExp.getDouble("health");
        education = incOrExp.getDouble("education");
        utilities = incOrExp.getDouble("utilities");
        work = incOrExp.getDouble("work2");
        miscellaneous = incOrExp.getDouble("miscellaneous");

        displayMonthYear2.setText(month_year);
        displayFood.setText(""+roundOff.format(food));
        displayTransportation.setText(""+roundOff.format(transportation));
        displayGrocery.setText(""+roundOff.format(grocery));
        displayHealth.setText(""+roundOff.format(health));
        displayEducation.setText(""+roundOff.format(education));
        displayUtilities.setText(""+roundOff.format(utilities));
        displayWork2.setText(""+roundOff.format(work));
        displayMisc.setText(""+roundOff.format(miscellaneous));
        displayTotalExp.setText(""+roundOff.format(food+transportation+grocery+health+education+utilities+work+miscellaneous));

    }

    private void initNewMonth() throws Exception{
        int count = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");
        Date date = new Date();

        String checkTable = "SELECT * FROM statistical_report WHERE month_year='"+formatter.format(date)+"' AND username='"+username+"'";
        Statement checkQuery = dbLink.createStatement();
        ResultSet checking = checkQuery.executeQuery(checkTable);
        while(checking.next()){
            count++;
        }
        if(count == 0){
            String newMonthRow = "INSERT INTO statistical_report(month_year, username, allowance, work1, food, " +
                    "transportation, grocery,health, education, utilities, work2, miscellaneous) " +
                    "VALUES ('"+formatter.format(date)+"','"+ username +"',"+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+")";
            Statement newQuery = dbLink.createStatement();
            newQuery.executeUpdate(newMonthRow);
        }

    }


    private void updateCategoryIncome() throws Exception{

        double total;
        String date;

        String extractWorkIncome = "SELECT DATE_FORMAT(date, '%M %Y') as Dates, SUM(amount) as Total FROM income WHERE username='"+ username +"' AND category='Work' GROUP BY MONTH(date), YEAR(date)";
        Statement workIncomeStatement = dbLink.createStatement();
        ResultSet workSet = workIncomeStatement.executeQuery(extractWorkIncome);

        while(workSet.next()){
            total = workSet.getDouble("Total");
            date = workSet.getString("Dates");
            String updateTable = "UPDATE statistical_report SET work1='"+total+"' WHERE username='"+username+"' AND month_year='"+date+"'";
            Statement query = dbLink.createStatement();
            query.executeUpdate(updateTable);
        }

        String extractAllowanceIncome = "SELECT DATE_FORMAT(date, '%M %Y') as Dates, SUM(amount) as Total FROM income WHERE username='"+ username +"' AND category='Allowance' GROUP BY MONTH(date), YEAR(date)";
        Statement allowanceIncomeStatement = dbLink.createStatement();
        ResultSet allowanceSet = allowanceIncomeStatement.executeQuery(extractAllowanceIncome);

        while(allowanceSet.next()){
            total = workSet.getDouble("Total");
            date = workSet.getString("Dates");
            String updateTable = "UPDATE statistical_report SET allowance='"+total+"' WHERE username='"+username+"' AND month_year='"+date+"'";
            Statement query = dbLink.createStatement();
            query.executeUpdate(updateTable);
        }


    }
    private void updateCategoryExpenses() throws Exception{

        double total;
        String date;

        /* FOOD */
        String extractFoodExpenses = "SELECT DATE_FORMAT(date, '%M %Y') as Dates, SUM(amount) as Total FROM expenses WHERE username='"+ username +"' AND category='Food' GROUP BY MONTH(date), YEAR(date)";
        Statement foodExpensesStatement = dbLink.createStatement();
        ResultSet foodSet = foodExpensesStatement.executeQuery(extractFoodExpenses);

        while(foodSet.next()){
            total = foodSet.getDouble("Total");
            date = foodSet.getString("Dates");
            String updateTable = "UPDATE statistical_report SET food='"+total+"' WHERE username='"+username+"' AND month_year='"+date+"'";
            Statement query = dbLink.createStatement();
            query.executeUpdate(updateTable);
        }

        /* GROCERY */

        String extractGroceryExpenses = "SELECT DATE_FORMAT(date, '%M %Y') as Dates, SUM(amount) as Total FROM expenses WHERE username='"+ username +"' AND category='Grocery' GROUP BY MONTH(date), YEAR(date)";
        Statement groceryExpensesStatement = dbLink.createStatement();
        ResultSet grocerySet = groceryExpensesStatement.executeQuery(extractGroceryExpenses);

        while(grocerySet.next()){
            total = grocerySet.getDouble("Total");
            date = grocerySet.getString("Dates");
            String updateTable = "UPDATE statistical_report SET grocery='"+total+"' WHERE username='"+username+"' AND month_year='"+date+"'";
            Statement query = dbLink.createStatement();
            query.executeUpdate(updateTable);
        }

        /* HEALTH */

        String healthGroceryExpenses = "SELECT DATE_FORMAT(date, '%M %Y') as Dates, SUM(amount) as Total FROM expenses WHERE username='"+ username +"' AND category='Health' GROUP BY MONTH(date), YEAR(date)";
        Statement healthExpensesStatement = dbLink.createStatement();
        ResultSet healthSet = healthExpensesStatement.executeQuery(healthGroceryExpenses);

        while(healthSet.next()){
            total = healthSet.getDouble("Total");
            date = healthSet.getString("Dates");
            String updateTable = "UPDATE statistical_report SET health='"+total+"' WHERE username='"+username+"' AND month_year='"+date+"'";
            Statement query = dbLink.createStatement();
            query.executeUpdate(updateTable);
        }

        /* EDUCATION */

        String extractEducationExpenses = "SELECT DATE_FORMAT(date, '%M %Y') as Dates, SUM(amount) as Total FROM expenses WHERE username='"+ username +"' AND category='Education' GROUP BY MONTH(date), YEAR(date)";
        Statement educationExpensesStatement = dbLink.createStatement();
        ResultSet educationSet = educationExpensesStatement.executeQuery(extractEducationExpenses);

        while(educationSet.next()){
            total = educationSet.getDouble("Total");
            date = educationSet.getString("Dates");
            String updateTable = "UPDATE statistical_report SET education='"+total+"' WHERE username='"+username+"' AND month_year='"+date+"'";
            Statement query = dbLink.createStatement();
            query.executeUpdate(updateTable);
        }

        /* UTILITIES */

        String extractUtilitiesExpenses = "SELECT DATE_FORMAT(date, '%M %Y') as Dates, SUM(amount) as Total FROM expenses WHERE username='"+ username +"' AND category='Utilities' GROUP BY MONTH(date), YEAR(date)";
        Statement utilitiesExpensesStatement = dbLink.createStatement();
        ResultSet utilitiesSet = utilitiesExpensesStatement.executeQuery(extractUtilitiesExpenses);

        while(utilitiesSet.next()){
            total = utilitiesSet.getDouble("Total");
            date = utilitiesSet.getString("Dates");
            String updateTable = "UPDATE statistical_report SET utilities='"+total+"' WHERE username='"+username+"' AND month_year='"+date+"'";
            Statement query = dbLink.createStatement();
            query.executeUpdate(updateTable);
        }

        /* WORK */

        String extractWorkExpenses = "SELECT DATE_FORMAT(date, '%M %Y') as Dates, SUM(amount) as Total FROM expenses WHERE username='"+ username +"' AND category='Work' GROUP BY MONTH(date), YEAR(date)";
        Statement workExpensesStatement = dbLink.createStatement();
        ResultSet workSet = workExpensesStatement.executeQuery(extractWorkExpenses);

        while(workSet.next()){
            total = workSet.getDouble("Total");
            date = workSet.getString("Dates");
            String updateTable = "UPDATE statistical_report SET work2='"+total+"' WHERE username='"+username+"' AND month_year='"+date+"'";
            Statement query = dbLink.createStatement();
            query.executeUpdate(updateTable);
        }

        /* MISC */
        String extractMiscExpenses = "SELECT DATE_FORMAT(date, '%M %Y') as Dates, SUM(amount) as Total FROM expenses WHERE username='"+ username +"' AND category='Miscellaneous' GROUP BY MONTH(date), YEAR(date)";
        Statement groceryMiscStatement = dbLink.createStatement();
        ResultSet miscSet = groceryMiscStatement.executeQuery(extractMiscExpenses);

        while(miscSet.next()){
            total = miscSet.getDouble("Total");
            date = miscSet.getString("Dates");
            String updateTable = "UPDATE statistical_report SET miscellaneous='"+total+"' WHERE username='"+username+"' AND month_year='"+date+"'";
            Statement query = dbLink.createStatement();
            query.executeUpdate(updateTable);
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
