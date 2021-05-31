package MainApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Register {
    private double x;
    private double y;
    private Connection dbLink;

    public TextField username;
    public PasswordField password;
    public TextField personalName;
    public TextField initialSavings;
    public Text sysMessage;
    public Button cancel;
    public Button done;



    public void initialize() {
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


        /*If cancel button is clicked, it will go back to the login screen.*/
        cancel.setOnMouseClicked(e->{

            FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/login.fxml"));
            Parent root = null;
            try {

                root = main.load();
                Stage stage = (Stage) cancel.getScene().getWindow();
                root.setOnMousePressed(d->{
                    x = d.getSceneX();
                    y = d.getSceneY();
                });

                root.setOnMouseDragged(d->{
                    stage.setX(d.getScreenX()-x);
                    stage.setY(d.getScreenY()-y);
                });
                stage.setTitle("Monrec");
                stage.setScene(new Scene(root));

                stage.show();
            } catch (IOException d) {
                d.printStackTrace();
            }
        });
        done.setDisable(true); //is disabled until the user types in all fields
    }

    public void register(){
        /* Get the text that the user typed */
        String user = username.getText();
        String pass = password.getText();
        String pName = personalName.getText();
        String savings = initialSavings.getText();
        double initSavings = Double.parseDouble(savings);


        /*To create a statement in MySQL through IntelliJ and execute it through query.
        username is selected from the database as it will serve as a flag to determine whether the username already exists or not.*/
        String checkCredentials = "SELECT * FROM personal_info where username= '" + user +"'";

        /*MySQL statement to add the new user to the table of the database.*/
        String statement = "INSERT INTO personal_info(username, password, initialSavings, customerName) VALUES ('";
        String values = user + "','" + pass + "'," + initSavings + ",'"+pName+"')";
        String addUser = statement + values;
        try{

            /*To create a statement in MySQL through IntelliJ and execute it through query.*/
            Statement line = dbLink.createStatement();
            ResultSet queryRes = line.executeQuery(checkCredentials);

            int count = 0;
            while(queryRes.next()) count++;

            /*To determine whether the username/password exists or is correct.
             1 means the username is already taken else, 0 means the username is not yet taken..
             */
            if(count == 1){
                username.setText("");
                sysMessage.setText("That username has been taken. Please choose another username.");
            }
            else{
                try{
                    Statement newAcc = dbLink.createStatement();
                    newAcc.executeUpdate(addUser);
                    sysMessage.setText("Registered successfully!");

                }catch (Exception e){
                    e.printStackTrace();
                    e.getCause();
                }
                username.setText("");
                password.setText("");
                initialSavings.setText("");
                personalName.setText("");


                /* TO INITIALIZE THE INCOME AND EXPENSES TABLE AND STAT REPORT*/
                SimpleDateFormat formatter = new SimpleDateFormat("MMMMMMMMM yyyy");
                Date date = new Date();

                String newDashboard = "INSERT INTO dashboard(month_year, username, savingsProgress, totalExpenses, totalIncome, savingsGoalMonthly) " +
                        "VALUES ('"+formatter.format(date)+"','"+ user +"',"+0+","+0+","+0+","+0+")";
                String newStatReport = "INSERT INTO statistical_report(month_year, username, allowance, work1, food, transportation, grocery,health, education, utilities, work2, miscellaneous) " +
                        "VALUES ('"+formatter.format(date)+"','"+ user +"',"+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+")";
                String newIncome = "INSERT INTO income(date, username, category, amount) VALUES ('"+formatter.format(date)+"','"+user+"', 'None',"+0+")";
                String newExpenses = "INSERT INTO expenses(date, username, category, amount) VALUES ('"+formatter.format(date)+"','"+user+"', 'None',"+0+")";

                try{
                    Statement newStatement = dbLink.createStatement();
                    Statement new1Statement = dbLink.createStatement();
                    Statement new2Statement = dbLink.createStatement();
                    Statement new3Statement = dbLink.createStatement();
                    newStatement.executeUpdate(newDashboard);
                    new1Statement.executeUpdate(newStatReport);
                    new2Statement.executeUpdate(newIncome);
                    new3Statement.executeUpdate(newExpenses);

                }catch (Exception e){
                    e.printStackTrace();
                    e.getCause();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }


    /* Method to check whether if one field is empty. if one field is empty then the login button is disabled */
    public void infoFilled(){
        if (!username.getText().isEmpty() && !password.getText().isEmpty() && !personalName.getText().isEmpty() && !initialSavings.getText().isEmpty()) done.setDisable(false);
        else done.setDisable(true);
    }





}
