package MainApp;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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


    public Button save;
    public Button edit;

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

        sideMenu();

        /*Every money displayed on the dashboard is reset to 0 if a new month emerges, except for the budget*/
        if(resetMonth()) reset();

        displayTotalExpenses();
        displayTotalIncome();
        updatedDashboardDb();
        displayBudgetLeft();
        displayGoalsPeriodically();

        monthlyGoal.setVisible(false);
        save.setVisible(false);
    }




    private void displayBudgetLeft(){

        double readInitPersonal=0;
        String readPersonalInfoUpdate = "SELECT initialSavings FROM personal_info WHERE username='"+ username +"'";
        DecimalFormat roundOff = new DecimalFormat("###0.00");

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
        displayBudgetLeft.setText(roundOff.format(readInitPersonal));
    }

    private void displayTotalExpenses(){

        String extractExpenses = "SELECT DATE_FORMAT(date, '%M %Y') as Dates, SUM(amount) as Total FROM expenses WHERE username='"+ username +"' GROUP BY MONTH(date), YEAR(date)";
        DecimalFormat roundOff = new DecimalFormat("###0.00");

        try{
            Statement expensesStatement = dbLink.createStatement();
            ResultSet expensesSet = expensesStatement.executeQuery(extractExpenses);
            while(expensesSet.next()){
                month_year = expensesSet.getString("Dates");
                totalExpenses = expensesSet.getDouble("Total");
            }
            displayTotalExpenses.setText(roundOff.format(totalExpenses));
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    private void displayTotalIncome(){

        String extractIncome = "SELECT DATE_FORMAT(date, '%M %Y') as Dates, SUM(amount) as Total FROM income WHERE username='"+ username +"' GROUP BY MONTH(date), YEAR(date)";
        DecimalFormat roundOff = new DecimalFormat("###0.00");

        try{
            Statement incomeStatement = dbLink.createStatement();
            ResultSet incomeSet = incomeStatement.executeQuery(extractIncome);
            while(incomeSet.next()){
                month_year = incomeSet.getString("Dates");
                totalIncome = incomeSet.getDouble("Total");
            }

            displayTotalIncome.setText(roundOff.format(totalIncome));

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        savingsProgress = totalIncome - totalExpenses;
        if(savingsProgress < 0){
            displaySavings.setText(roundOff.format(0));
        }
        else{
            displaySavings.setText(roundOff.format(savingsProgress));
        }

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
            String updateValues = "UPDATE dashboard SET savingsProgress="+(totalIncome - totalExpenses)+", totalExpenses="+totalExpenses+", " +
                    "totalIncome="+totalIncome+" WHERE month_year='"+month_year+"' AND username='"+ username +"'";
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


    public void editGoal(){
        monthlyGoal.setVisible(true);
        save.setVisible(true);
    }

    public void saveNewGoal() throws ParseException {

        SimpleDateFormat originalFormat = new SimpleDateFormat("MMM yyyy");
        DecimalFormat roundOff = new DecimalFormat("###0.00");
        Date current = new Date();
        month_year = originalFormat.format(current); // CURRENT DATE

        Date date = originalFormat.parse(month_year);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        /* GET THE NUMBER OF DAYS AND WEEKS OF THE CURRENT MONTH */
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int count = 0;

        String monthlyGoalText = monthlyGoal.getText();
        savingsGoalMonthly = Double.parseDouble(monthlyGoalText);
        savingsGoalWeekly = (savingsGoalMonthly - (totalIncome - totalExpenses)) / weeks;
        savingsGoalDaily = (savingsGoalMonthly - (totalIncome - totalExpenses)) / days;

        displaySavingsMonthly.setText(roundOff.format(savingsGoalMonthly));
        displaySavingsWeekly.setText(roundOff.format(savingsGoalWeekly));
        displaySavingsDaily.setText(roundOff.format(savingsGoalDaily));

        String dashboard = "SELECT * FROM dashboard WHERE month_year='"+month_year+"' AND username='"+ username +"'";

        /* CHECK WHETHER THE INFORMATION OF THE USER IN A SPECIFIC MONTH IN DASHBOARD TABLE ALREADY EXISTS */
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

        /* IF-ELSE STATEMENT TO IDENTIFY WHETHER THE INFORMATION OF THE USER IN DASHBOARD TABLE ALREADY EXISTS */
        if (count == 0){
            String add = "INSERT INTO dashboard(month_year, username, savingsProgress, totalExpenses, totalIncome, savingsGoalMonthly) " +
                    "VALUES ('"+ month_year +"','"+ username + "',"+(totalIncome - totalExpenses)+","+totalExpenses+","+totalIncome+","+savingsGoalMonthly+")";
            try{
                Statement query = dbLink.createStatement();
                query.executeUpdate(add);
            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
        else{
            String updateValues = "UPDATE dashboard SET savingsGoalMonthly="+savingsGoalMonthly+" WHERE month_year='"+month_year+"' AND username='"+ username +"'";
            try{
                Statement query = dbLink.createStatement();
                query.executeUpdate(updateValues);
            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }


        monthlyGoal.setVisible(false);
        save.setVisible(false);

    }

    private void displayGoalsPeriodically() throws ParseException {
        int count = 0;
        DecimalFormat roundOff = new DecimalFormat("###0.00");
        SimpleDateFormat originalFormat = new SimpleDateFormat("MMM yyyy");

        String extractMonthYear = "SELECT * FROM dashboard WHERE username='"+ username +"'";
        try{
            Statement dashboardStatement = dbLink.createStatement();
            ResultSet dashboardSet = dashboardStatement.executeQuery(extractMonthYear);
            while(dashboardSet.next()){
                month_year = dashboardSet.getString("month_year");
            }

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        String dashboard = "SELECT * FROM dashboard WHERE month_year='"+month_year+"' AND username='"+ username +"'";

        try{
            Statement dashboardStatement = dbLink.createStatement();
            ResultSet dashboardSet = dashboardStatement.executeQuery(dashboard);
            while(dashboardSet.next()){
                savingsGoalMonthly = dashboardSet.getDouble("savingsGoalMonthly");
                count++;
            }

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        if(count !=0){
            Date date = originalFormat.parse(month_year);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);

            savingsGoalWeekly = (savingsGoalMonthly - (totalIncome - totalExpenses)) / weeks;
            savingsGoalDaily = (savingsGoalMonthly - (totalIncome - totalExpenses)) / days;
            displaySavingsMonthly.setText(roundOff.format(savingsGoalMonthly));
            displaySavingsWeekly.setText(roundOff.format(savingsGoalWeekly));
            displaySavingsDaily.setText(roundOff.format(savingsGoalDaily));
        }
        else{
            displaySavingsMonthly.setText(roundOff.format(savingsGoalMonthly));
            displaySavingsWeekly.setText(roundOff.format(savingsGoalWeekly));
            displaySavingsDaily.setText(roundOff.format(savingsGoalDaily));
        }


    }


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
