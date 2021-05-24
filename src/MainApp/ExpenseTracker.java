package MainApp;

import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

public class ExpenseTracker {

    private String username;
    private String customerName;
    private Money added;

    private double x;
    private double y;

    private Connection dbLink;
    private ObservableList<Money> moneyList = FXCollections.observableArrayList();

    public TableView<Money> generalTable;
    public TableView<Money> categoricalTable;

    public TableColumn<Money, String> dateCol;
    public TableColumn<Money, String> categoryCol;
    public TableColumn<Money, String> amountCol;

    public TableColumn<Money, String> date1Col;
    public TableColumn<Money, String> amount1Col;

    public Button general;
    public Button food;
    public Button transportation;
    public Button grocery;
    public Button health;
    public Button education;
    public Button utilities;
    public Button work;
    public Button miscellaneous;
    public Button edit;
    public Button add;

    public ComboBox<String> newCategory;
    public TextField newAmount;
    public Button save;

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

        /* To connect to the AWS MySQL Database Instance */
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
        newCategory.setVisible(false);
        newAmount.setVisible(false);
        save.setVisible(false);
        newCategory.getItems().addAll("Food", "Transportation",
                "Grocery", "Health", "Education", "Utilities",
                "Work", "Miscellaneous");
        save.disableProperty().bind(newAmount.textProperty().isEmpty().or(newCategory.valueProperty().isNull()));
        loadGeneralTable();
    }

    public void loadGeneralTable(){

        generalTable.setVisible(true);
        categoricalTable.setVisible(false);
        edit.disableProperty().bind(Bindings.isEmpty(generalTable.getSelectionModel().getSelectedItems()));

        moneyList.clear();

        try{
            String query = "SELECT * FROM expenses WHERE username='"+ username +"'";
            Statement line = dbLink.createStatement();
            ResultSet queryRes = line.executeQuery(query);

            while(queryRes.next()){
                moneyList.add(new Money(queryRes.getTimestamp("date"),
                        queryRes.getString("category"),
                        queryRes.getDouble("amount")));
                generalTable.setItems(moneyList);
            }
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        date1Col.setCellValueFactory(new PropertyValueFactory<>("datetime"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        amount1Col.setCellValueFactory(new PropertyValueFactory<>("amount"));

    }



    public void loadCategoryTable(MouseEvent event){

        generalTable.setVisible(false);
        categoricalTable.setVisible(true);
        edit.disableProperty().bind(Bindings.isEmpty(categoricalTable.getSelectionModel().getSelectedItems()));

        moneyList.clear();
        String changeCase = "";
        String category = "";

        if(event.getSource() == food){
            changeCase = food.getText();
            String firstLetter = changeCase.substring(0,1);
            String remainingLetters = changeCase.substring(1,changeCase.length());
            remainingLetters = remainingLetters.toLowerCase(Locale.ROOT);
            category = firstLetter + remainingLetters;
        }
        else if(event.getSource() == transportation){
            changeCase = transportation.getText();
            String firstLetter = changeCase.substring(0,1);
            String remainingLetters = changeCase.substring(1,changeCase.length());
            remainingLetters = remainingLetters.toLowerCase(Locale.ROOT);
            category = firstLetter + remainingLetters;
        }
        else if(event.getSource() == grocery){
            changeCase = grocery.getText();
            String firstLetter = changeCase.substring(0,1);
            String remainingLetters = changeCase.substring(1,changeCase.length());
            remainingLetters = remainingLetters.toLowerCase(Locale.ROOT);
            category = firstLetter + remainingLetters;
        }
        else if(event.getSource() == health){
            changeCase = health.getText();
            String firstLetter = changeCase.substring(0,1);
            String remainingLetters = changeCase.substring(1,changeCase.length());
            remainingLetters = remainingLetters.toLowerCase(Locale.ROOT);
            category = firstLetter + remainingLetters;
        }
        else if(event.getSource() == education){
            changeCase = education.getText();
            String firstLetter = changeCase.substring(0,1);
            String remainingLetters = changeCase.substring(1,changeCase.length());
            remainingLetters = remainingLetters.toLowerCase(Locale.ROOT);
            category = firstLetter + remainingLetters;
        }
        else if(event.getSource() == utilities){
            changeCase = utilities.getText();
            String firstLetter = changeCase.substring(0,1);
            String remainingLetters = changeCase.substring(1,changeCase.length());
            remainingLetters = remainingLetters.toLowerCase(Locale.ROOT);
            category = firstLetter + remainingLetters;
        }
        else if(event.getSource() == work){
            changeCase = work.getText();
            String firstLetter = changeCase.substring(0,1);
            String remainingLetters = changeCase.substring(1,changeCase.length());
            remainingLetters = remainingLetters.toLowerCase(Locale.ROOT);
            category = firstLetter + remainingLetters;
        }
        else if(event.getSource() == miscellaneous){
            changeCase = miscellaneous.getText();
            String firstLetter = changeCase.substring(0,1);
            String remainingLetters = changeCase.substring(1,changeCase.length());
            remainingLetters = remainingLetters.toLowerCase(Locale.ROOT);
            category = firstLetter + remainingLetters;
        }

        System.out.println(category);
        try{
            String query = "SELECT * FROM expenses WHERE category='"+category+"' AND username='"+ username +"'";
            Statement line = dbLink.createStatement();
            ResultSet queryRes = line.executeQuery(query);

            while(queryRes.next()){
                moneyList.add(new Money(queryRes.getTimestamp("date"), queryRes.getDouble("amount")));
                categoricalTable.setItems(moneyList);
            }

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        dateCol.setCellValueFactory(new PropertyValueFactory<>("datetime"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    public void refreshCategory(){

        moneyList.clear();

        String[] categories = {"Food", "Transportation",
                "Grocery", "Health", "Education", "Utilities",
                "Work", "Miscellaneous"};
        for(String s : categories){
            try{
                String query = "SELECT * FROM expenses  WHERE category='"+s+"' AND username='"+ username +"'";
                Statement line = dbLink.createStatement();
                ResultSet queryRes = line.executeQuery(query);

                while(queryRes.next()){
                    moneyList.add(new Money(queryRes.getTimestamp("date"), queryRes.getDouble("amount")));
                    categoricalTable.setItems(moneyList);
                }

            }catch(Exception e){
                e.printStackTrace();
                e.getCause();
            }

            dateCol.setCellValueFactory(new PropertyValueFactory<>("datetime"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        }

    }


    public void removeExpense(){
        if(generalTable.isVisible()) added = generalTable.getSelectionModel().getSelectedItem();
        else if(categoricalTable.isVisible()) added = categoricalTable.getSelectionModel().getSelectedItem();

        try{
            String delete = "DELETE FROM expenses WHERE date='"+added.getDatetime()+"' AND username='"+ username +"'" ;
            Statement deleteThis = dbLink.createStatement();
            deleteThis.execute(delete);
            loadGeneralTable();
            refreshCategory();

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        double readInitPersonal = 0, newBudgetPersonalInfo = 0;
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

        newBudgetPersonalInfo = readInitPersonal + added.getAmount();
        String writePersonalInfoUpdate = "UPDATE personal_info SET initialSavings= "+newBudgetPersonalInfo+" WHERE username='"+ username +"'";

        try{
            Statement writePersonalInfoStatement = dbLink.createStatement();
            writePersonalInfoStatement.executeUpdate(writePersonalInfoUpdate);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void editExpense(){

        newCategory.setVisible(true);
        newAmount.setVisible(true);
        save.setVisible(true);

        if(generalTable.isVisible()) added = generalTable.getSelectionModel().getSelectedItem();
        else if(categoricalTable.isVisible()) added = categoricalTable.getSelectionModel().getSelectedItem();

    }

    public void save(){

        String inc = newAmount.getText();
        String categ = newCategory.getSelectionModel().getSelectedItem();
        double amt = Double.parseDouble(inc);
        double readInitPersonal = 0, newBudgetPersonalInfo = 0;

        /* 1. UPDATE the selected item from the database */
        try{
            String update = "UPDATE expenses SET amount="+amt+", category='"+categ+"' WHERE date='"+added.getDatetime()+"' AND username='"+ username +"'";
            Statement updateThis = dbLink.createStatement();
            updateThis.executeUpdate(update);
            loadGeneralTable();
            refreshCategory();
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        /* 2. UPDATE the budget/initial savings from the personal_info table */
        String readPersonalInfoUpdate = "SELECT initialSavings FROM personal_info WHERE username='"+ username +"'";
        try{
            Statement readPersonalInfoStatement = dbLink.createStatement();
            ResultSet readPersonalInfoQuery = readPersonalInfoStatement.executeQuery(readPersonalInfoUpdate);
            while(readPersonalInfoQuery.next()){
                readInitPersonal = readPersonalInfoQuery.getDouble("initialSavings");
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        newBudgetPersonalInfo = readInitPersonal - (amt - added.getAmount()) ;
        String writePersonalInfoUpdate = "UPDATE personal_info SET initialSavings= "+newBudgetPersonalInfo+" WHERE username='"+ username +"'";

        try{
            Statement writePersonalInfoStatement = dbLink.createStatement();
            writePersonalInfoStatement.executeUpdate(writePersonalInfoUpdate);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }


        /* UI feature for buttons */
        newCategory.setVisible(false);
        newAmount.setVisible(false);
        save.setVisible(false);

        edit.disableProperty().bind(Bindings.isEmpty(generalTable.getSelectionModel().getSelectedItems()));
        if(generalTable.isVisible()) {
            generalTable.getSelectionModel().clearSelection();
            edit.disableProperty().bind(Bindings.isEmpty(generalTable.getSelectionModel().getSelectedItems()));
        }
        else if(categoricalTable.isVisible()){
            categoricalTable.getSelectionModel().clearSelection();
            edit.disableProperty().bind(Bindings.isEmpty(categoricalTable.getSelectionModel().getSelectedItems()));
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

    public void expense(){

        FXMLLoader main = new FXMLLoader(getClass().getResource("GUI/expensesManager.fxml"));
        Parent root;

        try {
            root = main.load();
            Expenses sendUser = main.getController();
            sendUser.initialize(username,customerName);
            Stage stage = (Stage) add.getScene().getWindow();
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
