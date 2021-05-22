package MainApp;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Locale;

public class Income {

    private String currentUser;

    public TableView<Money> generalTable;
    public TableView<Money> categoricalTable;

    public TableColumn<Money, String> categoryCol;

    public TableColumn<Money, String> dateCol;
    public TableColumn<Money, String> amountCol;

    public TableColumn<Money, String> date1Col;
    public TableColumn<Money, String> amount1Col;

    private Connection dbLink;
    private ObservableList<Money> moneyList = FXCollections.observableArrayList();

    public Button general;
    public Button allowance;
    public Button work;
    public Button edit;

    public ComboBox<String> newCategory;
    public TextField newAmount;
    public Button save;

    private Money added;


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

        newCategory.setVisible(false);
        newAmount.setVisible(false);
        save.setVisible(false);
        newCategory.getItems().addAll("Allowance", "Work");
        save.disableProperty().bind(newAmount.textProperty().isEmpty().or(newCategory.valueProperty().isNull()));

        loggedInUser();

        System.out.println(currentUser);

        loadGeneralTable();

    }

    private void loggedInUser(){
        //to check which user is currently logged in
        String checkLog = "SELECT * FROM logs ORDER BY log_no DESC LIMIT 1";
        try{

            Statement line = dbLink.createStatement();
            ResultSet queryRes = line.executeQuery(checkLog);
            while(queryRes.next()) currentUser=queryRes.getString("username");
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void loadGeneralTable(){

        generalTable.setVisible(true);
        categoricalTable.setVisible(false);

        edit.disableProperty().bind(Bindings.isEmpty(generalTable.getSelectionModel().getSelectedItems()));

        moneyList.clear();

        try{
            String query = "SELECT * FROM income WHERE username='"+currentUser+"'" ;
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
        String changeCase;
        String category="";

        if(event.getSource() == allowance){
            changeCase = allowance.getText();
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

        System.out.println(category);


        try{
            String query = "SELECT * FROM income WHERE category='"+category+"' AND username='"+currentUser+"'";
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
        String[] categories = {"Allowance", "Work"};

        for(String s : categories){
            try{
                String query = " SELECT * FROM income WHERE category='"+s+"' AND username='"+currentUser+"'";
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

    public void removeIncome(){


        try{

            if(generalTable.isVisible()) added = generalTable.getSelectionModel().getSelectedItem();
            else if(categoricalTable.isVisible()) added=categoricalTable.getSelectionModel().getSelectedItem();

            System.out.println(added.getAmount());

            String delete = "DELETE FROM income WHERE date='"+added.getDatetime()+"' AND username='"+currentUser+"'";
            Statement deleteThis = dbLink.createStatement();
            deleteThis.execute(delete);

            loadGeneralTable();
            refreshCategory();

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        double readInitPersonal = 0, newBudgetPersonalInfo = 0;

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

        newBudgetPersonalInfo = readInitPersonal - added.getAmount();
        String writePersonalInfoUpdate = "UPDATE personal_info SET initialSavings= "+newBudgetPersonalInfo+" WHERE username='"+currentUser+"'";

        try{
            Statement writePersonalInfoStatement = dbLink.createStatement();
            writePersonalInfoStatement.executeUpdate(writePersonalInfoUpdate);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    public void editIncome(){

        newCategory.setVisible(true);
        newAmount.setVisible(true);
        save.setVisible(true);
        edit.disableProperty().bind(Bindings.isNotEmpty(generalTable.getSelectionModel().getSelectedItems()));


        if(generalTable.isVisible()) added = generalTable.getSelectionModel().getSelectedItem();
        else if(categoricalTable.isVisible()) added = categoricalTable.getSelectionModel().getSelectedItem();

    }

    public void save(){

        newCategory.setVisible(false);
        newAmount.setVisible(false);
        save.setVisible(false);

        generalTable.getSelectionModel().clearSelection();
        edit.disableProperty().bind(Bindings.isEmpty(generalTable.getSelectionModel().getSelectedItems()));

    }

}
