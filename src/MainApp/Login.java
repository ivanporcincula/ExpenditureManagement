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
import java.util.Objects;

public class Login {


    private double x;
    private double y;

    public TextField username;
    public TextField password;

    public Button login;

    public Button exit;

    public Button register;

    public Connection dbLink;

    public Text errorLogin;


    public void initialize() {
        login.setDisable(true);

        /*exit.setOnMouseClicked(e->{
            Stage stage = (Stage) exit.getScene().getWindow();
            stage.close();
        });*/

        register.setOnMouseClicked(e->{

            System.out.println("AHHH" + 7);
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

        String schemaName = "test";
        String databaseUser = "dumanyoroporc";
        String databasePassword = "lbycpd2PROJECT";

        String databaseURL = "jdbc:mysql://cpd2-database.c42q90fut081.ap-southeast-1.rds.amazonaws.com:3306/"+schemaName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbLink = DriverManager.getConnection(databaseURL,databaseUser,databasePassword);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void infoFilled(){
        login.setDisable(username.getText().isEmpty() || password.getText().isEmpty());
    }

    public void login(){

        String user = username.getText();
        String pass = password.getText();

        String checkCredentials = "SELECT * FROM personal_info where username= '" + user +"' AND password= '"+pass+"'";

        try{

            Statement line = dbLink.createStatement();
            ResultSet queryRes = line.executeQuery(checkCredentials);

            int count = 0;
            while(queryRes.next()) count++;

            System.out.println(count);
            if(count == 1){

                username.setText("");
                password.setText("");

                FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/dashboard.fxml"));
                Parent root;

                try {
                    root = main.load();
                    Stage stage = (Stage) login.getScene().getWindow();
                    root.setOnMousePressed(e->{
                        x = e.getSceneX();
                        y = e.getSceneY();
                    });

                    root.setOnMouseDragged(e->{
                        stage.setX(e.getScreenX()-x);
                        stage.setY(e.getScreenY()-y);
                    });
                    stage.setTitle("Expenditure");
                    stage.setScene(new Scene(root));

                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //login successful

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
