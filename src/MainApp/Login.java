package MainApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;

import java.io.IOException;

public class Login {


    private double x;
    private double y;

    public TextField username;
    public TextField password;

    public Button login;
    public Button exit;
    public Button register;

    public Connection dbLink;
    public Connection newDbLink;

    public Text errorLogin;


    public void initialize() {
        login.setDisable(true); //is disabled until the user types in both fields of username and password
        //Opens the GUI for account creation
        register.setOnMouseClicked(e->{
            FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/createAccount.fxml"));
            Parent root = null;
            try {

                root = main.load();
                Stage stage = (Stage) register.getScene().getWindow();
                root.setOnMousePressed(d->{
                    x = d.getSceneX();
                    y = d.getSceneY();
                });

                root.setOnMouseDragged(d->{
                    stage.setX(d.getScreenX()-x);
                    stage.setY(d.getScreenY()-y);
                });
                stage.setTitle("Register");
                stage.setScene(new Scene(root));

                stage.show();
            } catch (IOException d) {
                d.printStackTrace();
            }

        });

        //To connect to the AWS MySQL Database Instance
        String schemaName = "user";
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

        /*exit.setOnMouseClicked(e->{
            Stage stage = (Stage) exit.getScene().getWindow();
            stage.close();
        });*/

    }

    //method to check whether the username or password is empty. if one is empty then the login button is disabled
    public void infoFilled(){
        login.setDisable(username.getText().isEmpty() || password.getText().isEmpty());
    }

    public void login(){

        //get the text that the user typed
        String user = username.getText();
        String pass = password.getText();
        String customerName="";

        //this is a MySQL syntax/statement
        //username and password are both selected from the database as the username-password combination must be correct
        String checkCredentials = "SELECT * FROM personal_info where username= '" + user +"' AND password= '"+pass+"'";

        try{
            //to create a statement in MySQL through IntelliJ and execute it through query.
            Statement line = dbLink.createStatement();
            ResultSet queryRes = line.executeQuery(checkCredentials);

            //to determine whether the username/password exists or is correct.
            // 1 means the username and password combination is correct.
            // 0 means either the username and password combination is incorrect or the account does not exist.
            int count = 0;
            while(queryRes.next()) {
                count++;
                customerName = queryRes.getString("customerName");
                //System.out.println(customerName);
            }

            if(count == 1){
                username.setText("");
                password.setText("");


                FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/dashboard.fxml")); //loads the dashboard
                Parent root;

                //opens the dashboard
                try {
                    root = main.load();
                    Dashboard sendUser = main.getController();
                    sendUser.initialize(user,customerName);
                    Stage stage = (Stage) login.getScene().getWindow();
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
            else{
                errorLogin.setText("Invalid username or password combination! Please try again.");
                username.setText("");
                password.setText("");
                login.setDisable(true);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
}
