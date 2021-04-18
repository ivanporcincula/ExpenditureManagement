package MainApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {
    public TextField username;
    public TextField password;

    private double x;
    private double y;

    public Button login;

    public Button exit;

    public Button register;


    public void initialize() {
        login.setDisable(true);

        exit.setOnMouseClicked(e->{
            Stage stage = (Stage) exit.getScene().getWindow();
            stage.close();
        });

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

    }

    public void infoFilled(){
        login.setDisable(username.getText().isEmpty() || password.getText().isEmpty());
    }

    public void login(){

        String user = username.getText();
        String pass = password.getText();





    }


}
