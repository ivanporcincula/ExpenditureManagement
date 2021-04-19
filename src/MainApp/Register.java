package MainApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Register {
    private double x;
    private double y;

    public TextField username;
    public TextField password;
    public TextField personalName;
    public TextField initialSavings;
    public Text sysMessage;
    public Button cancel;
    public Button done;

    private Connection dbLink;
    private Connection newDbLink;

    public void initialize() {

        done.setDisable(true); //is disabled until the user types in all fields

        // if cancel button is clicked, it will go back to the login screen.
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

        //To connect to the AWS MySQL Database Instance
        String schemaName = "test";
        String databaseUser = "dumanyoroporc";
        String databasePassword = "lbycpd2PROJECT";

        //url of the database instance host
        String databaseURL = "jdbc:mysql://cpd2-database.c42q90fut081.ap-southeast-1.rds.amazonaws.com:3306/"+schemaName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbLink = DriverManager.getConnection(databaseURL,databaseUser,databasePassword);
            newDbLink = DriverManager.getConnection(databaseURL,databaseUser,databasePassword);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void register(){


        //get the text that the user typed
        String user = username.getText();
        String pass = password.getText();
        String pName = personalName.getText();
        String savings = initialSavings.getText();
        double initSavings = Double.parseDouble(savings);


        //to create a statement in MySQL through IntelliJ and execute it through query.
        //username is selected from the database as it will serve as a flag to determine whether the username already exists or not.
        String checkCredentials = "SELECT * FROM personal_info where username= '" + user +"'";


        //MySQL statement to add the new user to the table of the database.
        String statement = "INSERT INTO personal_info(username, password, initialSavings, customerName) VALUES ('";
        String values = user + "','" + pass + "'," + initSavings + ",'"+pName+"')";
        String addUser = statement + values;
        try{

            //to create a statement in MySQL through IntelliJ and execute it through query.
            Statement line = dbLink.createStatement();
            ResultSet queryRes = line.executeQuery(checkCredentials);

            int count = 0;
            while(queryRes.next()) count++;

            System.out.println(count);


            //to determine whether the username/password exists or is correct.
            // 1 means the username is already taken.
            // 0 means the username is not yet taken..
            if(count == 1){
                //if username is taken
                username.setText("");
                sysMessage.setText("That username has been taken. Please choose another username.");
            }
            else{
                //if the username is not yet registered

                try{
                    Statement newAcc = newDbLink.createStatement();
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
            }
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }


    //method to check whether if one field is empty. if one field is empty then the login button is disabled
    public void infoFilled(){
        if (!username.getText().isEmpty() && !password.getText().isEmpty() && !personalName.getText().isEmpty() && !initialSavings.getText().isEmpty()) done.setDisable(false);
        else done.setDisable(true);
    }





}
