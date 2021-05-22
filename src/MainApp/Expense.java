package MainApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Expense {

    private String currentUser;

    public TableView<Money> generalTable;
    public TableView<Money> categoricalTable;

    public TableColumn<Money, String> dateCol;
    public TableColumn<Money, String> categoryCol;
    public TableColumn<Money, String> amountCol;

    private Connection dbLink;
    private ObservableList<Money> moneyList = FXCollections.observableArrayList();

    public Button general;
    public Button food;
    public Button transportation;

    public Button grocery;
    public Button health;
    public Button education;
    public Button utilities;
    public Button work;
    public Button miscellaneous;

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

        String checkLog = "SELECT * FROM logs ORDER BY log_no DESC LIMIT 1";

        try{

            Statement line = dbLink.createStatement();
            ResultSet queryRes = line.executeQuery(checkLog);

            while(queryRes.next()) currentUser=queryRes.getString("username");

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        System.out.println(currentUser);


        loadGeneralTable();

        general.setOnMouseClicked(e->{
            loadGeneralTable();
            generalTable.setVisible(true);
            categoricalTable.setVisible(false);

        });

        food.setOnMouseClicked(e->{
            generalTable.setVisible(false);
            categoricalTable.setVisible(true);
        });

        transportation.setOnMouseClicked(e->{
            generalTable.setVisible(false);
            categoricalTable.setVisible(true);
        });

        grocery.setOnMouseClicked(e->{
            generalTable.setVisible(false);
            categoricalTable.setVisible(true);
        });

        health.setOnMouseClicked(e->{
            generalTable.setVisible(false);
            categoricalTable.setVisible(true);
        });
        education.setOnMouseClicked(e->{
            generalTable.setVisible(false);
            categoricalTable.setVisible(true);
        });
        utilities.setOnMouseClicked(e->{
            generalTable.setVisible(false);
            categoricalTable.setVisible(true);
        });
        work.setOnMouseClicked(e->{
            generalTable.setVisible(false);
            categoricalTable.setVisible(true);
        });
        miscellaneous.setOnMouseClicked(e->{
            generalTable.setVisible(false);
            categoricalTable.setVisible(true);
        });





    }

    public void loadGeneralTable(){

        moneyList.clear();

        try{
            String query = "SELECT * FROM expenses";
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
        dateCol.setCellValueFactory(new PropertyValueFactory<>("datetime"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

    }



    public void loadCategoryTable(MouseEvent event){

        moneyList.clear();
        String category = "";

        if(event.getSource() == food){
            category = food.getText();
        }
        else if(event.getSource() == transportation){
            category = transportation.getText();
        }
        else if(event.getSource() == grocery){
            category = grocery.getText();
        }
        else if(event.getSource() == health){
            category = health.getText();
        }
        else if(event.getSource() == education){
            category = education.getText();
        }
        else if(event.getSource() == utilities){
            category = utilities.getText();
        }
        else if(event.getSource() == work){
            category = work.getText();
        }
        else if(event.getSource() == miscellaneous){
            category = miscellaneous.getText();
        }

        try{
            String query = "SELECT * FROM expenses WHERE category='"+category+"' AND username='"+currentUser+"'";
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
                String query = "SELECT * FROM expenses category='"+s+"' AND username='"+currentUser+"'";
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

        try{

            if(generalTable.isVisible()) added = generalTable.getSelectionModel().getSelectedItem();
            else if(categoricalTable.isVisible()) added = categoricalTable.getSelectionModel().getSelectedItem();


            String delete = "DELETE FROM expenses WHERE date="+added.getDatetime()+" AND username='"+currentUser+"'" ;
            Statement deleteThis = dbLink.createStatement();
            deleteThis.execute(delete);

            loadGeneralTable();
            refreshCategory();

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    public void editExpense(){

    }

}
