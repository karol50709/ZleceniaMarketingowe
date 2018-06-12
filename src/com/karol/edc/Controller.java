package com.karol.edc;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import java.util.*;

public class Controller implements Initializable {



    @FXML
    public Button plusHour,minusHour,plusMinute,minusMinute,plusFiveMinutes,minusFiveMinutes;

    @FXML
    public Slider sliderHours,sliderMinutes;

    @FXML
    public TextField textHour,textMinute,id;

    @FXML
    public ComboBox<String> descriptions;

    @FXML
    public TextArea description;

    @FXML
    public Button addTask, save;

    @FXML
    TableView<Task> table = new TableView<Task>();



    ObservableList<String> observableList = FXCollections.observableArrayList(
            "Porównywanie treści pisma",
            "Porównywanie treści pisma, nowa baza",
            "Porównywanie treści pisma, zaczytanie danych do e-nadawcy",
            "Tworzenie projektu",
            "Porównywanie treści pisma, zmiana mapowania bazy",
            "Pieczątki",
            "Faktoring",
            "Hipoteki"
    );

    ObservableList<Task> taskObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("oooo");

        descriptions.setItems(observableList);

        textHour.setText("0 h");
        textMinute.setText("0 m");

        table.setItems(taskObservableList);

        TableColumn<Task,Integer> c1 = new TableColumn<Task,Integer>("ID");
        TableColumn<Task,String> c2 = new TableColumn<Task,String>("Opis");
        TableColumn<Task,Integer> c3 = new TableColumn<Task,Integer>("Czas");

        c1.setCellValueFactory(new PropertyValueFactory<Task, Integer>("id"));
        c2.setCellValueFactory(new PropertyValueFactory<Task, String>("text"));
        c3.setCellValueFactory(new PropertyValueFactory<Task, Integer>("time"));

        c1.setMinWidth(100);
        c2.setMinWidth(350);
        c3.setMinWidth(86);

        c1.setSortType(TableColumn.SortType.DESCENDING);
        table.getSortOrder().addAll(c1,c2,c3);

        table.getColumns().addAll(c1,c2,c3);

        try {
            ArrayList<Task> temp = Utils.getDataFromCSV();
            for(int x=0;x<temp.size();x++){
                taskObservableList.add(temp.get(x));
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        table.sort();

        sliderHours.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                textHour.setText(Utils.Double2String(sliderHours.getValue())+" h");
            }
        });

        sliderMinutes.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                textMinute.setText(Utils.Double2String(sliderMinutes.getValue())+" m");
            }
        });


    }

    @FXML
    public void plusHour(){
        sliderHours.setValue(sliderHours.getValue()+1);
        textHour.setText(Utils.Double2String(sliderHours.getValue())+" h");
    }

    @FXML
    public void minusHour(){
        sliderHours.setValue(sliderHours.getValue()-1);
        textHour.setText(Utils.Double2String(sliderHours.getValue())+" h");
    }

    @FXML
    public void plusMinute(){
        sliderMinutes.setValue(sliderMinutes.getValue()+1);
        textMinute.setText(Utils.Double2String(sliderMinutes.getValue())+" m");
    }

    @FXML
    public void minusMinute(){
        sliderMinutes.setValue(sliderMinutes.getValue()-1);
        textMinute.setText(Utils.Double2String(sliderMinutes.getValue())+" m");
    }

    @FXML
    public void plusFiveMinutes(){
        sliderMinutes.setValue(sliderMinutes.getValue()+5);
        textMinute.setText(Utils.Double2String(sliderMinutes.getValue())+" m");
    }

    @FXML
    public void minusFiveMinutes(){
        sliderMinutes.setValue(sliderMinutes.getValue()-5);
        textMinute.setText(Utils.Double2String(sliderMinutes.getValue())+" m");
    }

    @FXML
    public void setDescription(){
        description.setText(descriptions.getValue());
    }

    @FXML
    public void addTask(){
        if(validData()){
            int time = Utils.String2Int(textHour.getText())*60+Utils.String2Int(textMinute.getText());
            taskObservableList.add(new Task(Integer.parseInt(id.getText()),description.getText(),time));
            clearTextAreas();
            resetSliders();
            table.sort();
        }
        else {
            System.out.println("not ok");
        }
    }

    private boolean validData(){
        if(!id.getText().isEmpty() && !description.getText().isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

    @FXML
    public void clearTextAreas(){
        id.setText("");
        textHour.setText("0 h");
        textMinute.setText("0 m");
        description.setText("");
    }

    @FXML
    public void resetSliders(){
        sliderHours.setValue(0);
        sliderMinutes.setValue(0);
    }


    @FXML
    public void save2file(){
        ArrayList<Task> tmp = new ArrayList<>();
        try {
            Utils.createCopyofCSV();
            Utils.safe2csv(arrayListFromObserveArrayList(taskObservableList));
                    }
        catch (IOException e){
            e.printStackTrace();
            shomExeptionWindow(e);
        }

    }

    private ArrayList<Task> arrayListFromObserveArrayList(ObservableList<Task> taskObservableArray){
        ArrayList<Task> result = new ArrayList<>();
        for (int x=0;x<taskObservableArray.size();x++){
            result.add(taskObservableArray.get(x));
        }
        return result;
    }

    @FXML
    public void openAbout(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("O programie");
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteTask(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Usunięcie zlecenia");
        alert.setHeaderText("Czy na pewno chcesz usunąć zlecenie");
        alert.setContentText(table.getSelectionModel().getSelectedItem().idProperty().toString()
                +" "+table.getSelectionModel().getSelectedItem().textProperty().toString()
                +" czas "+table.getSelectionModel().getSelectedItem().timeProperty().toString());


        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            taskObservableList.remove(table.getSelectionModel().getSelectedItem());
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Usunięto zlecenie");
            alert1.setHeaderText(null);
            alert1.setContentText("Staraj się wpisywać poprawnie zlecenia");
            alert1.showAndWait();
        }
        else{
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Nie usunięto zlecenia");
            alert2.setHeaderText(null);
            alert2.setContentText("Zdecydowania trochę!");
            alert2.showAndWait();
        }


    }

    public void shomExeptionWindow(Exception e, String title){

    }


}


