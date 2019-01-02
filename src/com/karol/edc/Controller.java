package com.karol.edc;


import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {


    @FXML
    public Button plusHour, minusHour, plusMinute, minusMinute, plusFiveMinutes, minusFiveMinutes, closeButton ,editButton;

    @FXML
    public Slider sliderHours, sliderMinutes;

    @FXML
    public TextField textHour, textMinute, id,editID, count, timetext, search, editHours, editMinutes;

    @FXML
    public ComboBox<String> descriptions;

    @FXML
    public TextArea description, editdescription;

    @FXML
    public Button addTask, save;

    @FXML
    public MenuBar MenuBar;

    @FXML
    public Menu Tools;

    @FXML
    public MenuItem OpenReports, Help,menuSave, deleteTask;


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



    private void initFilter() {
        search.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (search.textProperty().get().isEmpty()) {
                    table.setItems(taskObservableList);
                    return;
                }
                ObservableList<Task> tableItems = FXCollections.observableArrayList();
                ObservableList<TableColumn<Task, ?>> cols = table.getColumns();
                for (int i = 0; i < taskObservableList.size(); i++) {
                    for (int j = 0; j < cols.size(); j++) {
                        TableColumn col = cols.get(j);
                        String cellValue = col.getCellData(taskObservableList.get(i)).toString().toLowerCase();
                        if (cellValue.contains(search.getText().toLowerCase())) {
                            tableItems.add(taskObservableList.get(i));
                        }

                    }
                }
                table.setItems(tableItems);
                if (tableItems.size() < 1) {
                    search.setStyle("-fx-text-fill: red;");
                } else {
                    search.setStyle("-fx-text-fill: black;");
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(location.toString().endsWith("sample.fxml")) {



            OpenReports.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_ANY));
            Help.setAccelerator(new KeyCodeCombination(KeyCode.F1));
            menuSave.setAccelerator(new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_ANY));
            deleteTask.setAccelerator(new KeyCodeCombination(KeyCode.DELETE,KeyCombination.CONTROL_ANY));


            


            System.out.println("oooo");

            table.setPlaceholder(new Label("Brak elementów"));

            descriptions.setItems(observableList);

            textHour.setText("0 h");
            textMinute.setText("0 m");

            table.setItems(taskObservableList);

            TableColumn<Task, Integer> c1 = new TableColumn<Task, Integer>("ID");
            TableColumn<Task, String> c2 = new TableColumn<Task, String>("Opis");
            TableColumn<Task, Integer> c3 = new TableColumn<Task, Integer>("Czas");

            c1.setCellValueFactory(new PropertyValueFactory<Task, Integer>("id"));
            c2.setCellValueFactory(new PropertyValueFactory<Task, String>("text"));
            c3.setCellValueFactory(new PropertyValueFactory<Task, Integer>("time"));

            c1.setMinWidth(100);
            c2.setMinWidth(350);
            c3.setMinWidth(86);

            c1.setSortType(TableColumn.SortType.DESCENDING);
            table.getSortOrder().addAll(c1, c2, c3);

            table.getColumns().addAll(c1, c2, c3);

            try {
                ArrayList<Task> temp = Utils.getDataFromCSV();
                for (int x = 0; x < temp.size(); x++) {
                    taskObservableList.add(temp.get(x));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            table.sort();
            setStats();
            initFilter();

            sliderHours.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    textHour.setText(Utils.Double2String(sliderHours.getValue()) + " h");
                }
            });

            sliderMinutes.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    textMinute.setText(Utils.Double2String(sliderMinutes.getValue()) + " m");
                }
            });
        }

    }

    @FXML
    private void test() {

        Task task = table.getSelectionModel().getSelectedItem();



        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit.fxml"));
            loader.setController(this);
            Parent root1 = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Edytuj");

            editID.setText(String.valueOf(task.idProperty().get()));
            editdescription.setText(task.textProperty().getValue());
            editHours.setText(String.valueOf(task.timeProperty().get()/60));
            editMinutes.setText(String.valueOf(task.timeProperty().get()%60));

            editButton.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent event) {
                    System.out.println("opk");


                    if (validData(true)) {
                        int time = Utils.String2Int(editHours.getText()) * 60 + Utils.String2Int(editMinutes.getText());
                        Task temp = new Task(Integer.parseInt(editID.getText()), editdescription.getText(), time);
                        taskObservableList.remove(task);
                        taskObservableList.add(temp);
                        //clearTextAreas();
                        //resetSliders();
                        table.sort();
                        setStats();
                    } else {
                        System.out.println("not ok");
                    }


                    closeButtonAction();
                }
            });


            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }





    }

    @FXML
    private void openInExcel(){
        try {
            Runtime.getRuntime().exec(String.format("\"%s\" /t \"%s\""
                    , "excelPath"
                    , "xlspath"));
        }
        catch (IOException e){
            showExeptionWindow(e,"Komunikat wyjątku", Alert.AlertType.ERROR);

        }
    }

    @FXML
    private void closeButtonAction(){
        // get a handle to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    private void plusHour() {
        sliderHours.setValue(sliderHours.getValue() + 1);
        textHour.setText(Utils.Double2String(sliderHours.getValue()) + " h");
    }

    @FXML
    private void minusHour() {
        sliderHours.setValue(sliderHours.getValue() - 1);
        textHour.setText(Utils.Double2String(sliderHours.getValue()) + " h");
    }

    @FXML
    private void plusMinute() {
        sliderMinutes.setValue(sliderMinutes.getValue() + 1);
        textMinute.setText(Utils.Double2String(sliderMinutes.getValue()) + " m");
    }

    @FXML
    private void minusMinute() {
        sliderMinutes.setValue(sliderMinutes.getValue() - 1);
        textMinute.setText(Utils.Double2String(sliderMinutes.getValue()) + " m");
    }

    @FXML
    private void plusFiveMinutes() {
        sliderMinutes.setValue(sliderMinutes.getValue() + 5);
        textMinute.setText(Utils.Double2String(sliderMinutes.getValue()) + " m");
    }

    @FXML
    private void minusFiveMinutes() {
        sliderMinutes.setValue(sliderMinutes.getValue() - 5);
        textMinute.setText(Utils.Double2String(sliderMinutes.getValue()) + " m");
    }

    @FXML
    private void setDescription() {
        description.setText(descriptions.getValue());
    }

    @FXML
    private void addTask() {
        if (validData()) {
            int time = Utils.String2Int(textHour.getText()) * 60 + Utils.String2Int(textMinute.getText());
            taskObservableList.add(new Task(Integer.parseInt(id.getText()), description.getText(), time));
            clearTextAreas();
            resetSliders();
            table.sort();
            setStats();
        } else {
            System.out.println("not ok");
        }
    }

    private boolean validData() {

        boolean output = true;

        if (id.getText().isEmpty()) {
            output = false;
            showWarningWindow("Zlecenia Marketingowe", "Puste id", "Zlecenie nie zostanie dodane");
        }

        if (description.getText().isEmpty()) {
            output = false;
            showWarningWindow("Zlecenia Marketingowe", "Pusty opis zlecenia", "Zlecenie nie zostanie dodane");
        }

        if (Utils.String2Int(textHour.getText()) * 60 + Utils.String2Int(textMinute.getText()) < 0) {
            output = false;
            showWarningWindow("Zlecenia Marketingowe", "Ujemny czas zlecenia", "Zlecenie nie zostanie dodane");
        }

        if (description.getText().trim().isEmpty()) {
            output = false;
            showWarningWindow("Zlecenia Marketingowe", "Opis zlecenia jest pusty", "Zlecenie nie zostanie dodane");
        }


        return output;
    }

    private boolean validData(boolean fromEditWindow) {

        boolean output = true;

        if (editID.getText().isEmpty()) {
            output = false;
            showWarningWindow("Zlecenia Marketingowe", "Puste id", "Zlecenie nie zostanie dodane");
        }

        if (editdescription.getText().isEmpty()) {
            output = false;
            showWarningWindow("Zlecenia Marketingowe", "Pusty opis zlecenia", "Zlecenie nie zostanie dodane");
        }

        if (Utils.String2Int(editHours.getText()) * 60 + Utils.String2Int(editMinutes.getText()) < 0) {
            output = false;
            showWarningWindow("Zlecenia Marketingowe", "Ujemny czas zlecenia", "Zlecenie nie zostanie dodane");
        }

        if (editdescription.getText().trim().isEmpty()) {
            output = false;
            showWarningWindow("Zlecenia Marketingowe", "Opis zlecenia jest pusty", "Zlecenie nie zostanie dodane");
        }


        return output;
    }

    @FXML
    private void clearTextAreas() {
        id.setText("");
        textHour.setText("0 h");
        textMinute.setText("0 m");
        description.setText("");
    }

    @FXML
    private void resetSliders() {
        sliderHours.setValue(0);
        sliderMinutes.setValue(0);
    }


    @FXML
    private void save2file() {
        ArrayList<Task> tmp = new ArrayList<>();
        try {
            Utils.createCopyofCSV();
            Utils.safe2csv(arrayListFromObserveArrayList(taskObservableList));
            showInformationWindow("Zlecenia marketingowe", "Zapisano dane do pliku zlecenia.csv", "OK!");
        } catch (IOException e) {
            e.printStackTrace();
            showWarningWindow("Zlecenia marketingowe", "Wystąpił problem przy zapisie do pliku", e.getMessage());
        }

    }

    private ArrayList<Task> arrayListFromObserveArrayList(ObservableList<Task> taskObservableArray) {
        ArrayList<Task> result = new ArrayList<>();
        for (Task task : taskObservableArray) {
            result.add(task);
        }
        return result;
    }

    @FXML
    private void openAbout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("O programie");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteTask() {

        if (table.getSelectionModel().getSelectedItem() != null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Usunięcie zlecenia");
            alert.setHeaderText("Czy na pewno chcesz usunąć zlecenie");
            alert.setContentText(table.getSelectionModel().getSelectedItem().idProperty().get()
                    + " " + table.getSelectionModel().getSelectedItem().textProperty().get()
                    + " czas " + table.getSelectionModel().getSelectedItem().timeProperty().get());


            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                taskObservableList.remove(table.getSelectionModel().getSelectedItem());
                showInformationWindow("Zlecenia marketingowe", "Usunięto zlecenie", "Postaraj się wpisywać dobrze");
                setStats();

            } else {
                showInformationWindow("Zlecenia marketingowe", "Nie usunięto zlecenie", "Pomyśl dwa razy");
            }

        } else {
            showWarningWindow("Zlecenia marketingowe", "Nie wybrano zlecenia", "Wybierz zlecenie");
        }


    }

    @FXML
    private void setStats() {
        count.setText(String.valueOf(taskObservableList.size()));
        int time = 0;
        for (int i = 0; i < taskObservableList.size(); i++) {
            time += taskObservableList.get(i).timeProperty().get();
        }
        timetext.setText(time(time));

    }

    public void showExeptionWindow(Exception exeption, String title, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(exeption.getMessage());
        alert.showAndWait();


    }

    private void showInformationWindow(String title, String header, String context) {
        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        alert1.setTitle(title);
        alert1.setHeaderText(header);
        alert1.setContentText(context);
        alert1.showAndWait();

    }

    private void showWarningWindow(String title, String header, String context) {
        Alert alert1 = new Alert(Alert.AlertType.WARNING);
        alert1.setTitle(title);
        alert1.setHeaderText(header);
        alert1.setContentText(context);
        alert1.showAndWait();

    }

    private Optional<ButtonType> showYesOrNoWindow(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        return alert.showAndWait();
    }

    private String time(int time) {
        int x = time / 60;
        int y = time % 60;
        return x + " h " + y + " m";
    }

    @FXML
    private void loadReport() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wskaż raport");
        fileChooser.setInitialDirectory(new File("./"));

        try {
            File newreport = fileChooser.showOpenDialog(stage);
            ArrayList<Task> temp = Utils.getDataFromCSV(newreport);
            taskObservableList.removeAll(taskObservableList);
            for (int x = 0; x < temp.size(); x++) {
                taskObservableList.add(temp.get(x));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        table.sort();
        setStats();
    }

    @FXML
    private void endMonth() {
        List<String> dialogData = new LinkedList<>();
        for (Months months : Months.values()) {
            dialogData.add(months.name());
        }

        Dialog dialog = new ChoiceDialog(dialogData.get(0), dialogData);

        dialog.setTitle("Zakończ miesiąc");

        dialog.setHeaderText("Wybierz miesiąc");

        Optional<String> result = dialog.showAndWait();

        System.out.println(result.get());

        try {
            String month = result.get();
            Utils.safe2csvEndMonth(arrayListFromObserveArrayList(taskObservableList), month);


            Optional<ButtonType> resultx = showYesOrNoWindow("Pytanie", "Czy chcesz usunąc plik:", "zlecenia.csv");
            if (resultx.get() == ButtonType.OK) {
                try {
                    Utils.delZlecCsv();
                } catch (IOException e) {
                    showExeptionWindow(e, "Błąd przy usuwaniu zlecenia", Alert.AlertType.WARNING);
                }
            }

            Optional<ButtonType> resulty = showYesOrNoWindow("Pytanie", "Czy chcesz wyczyścić zlecenia", "");
            if (resulty.get() == ButtonType.OK) {
                taskObservableList.removeAll(taskObservableList);
            }


        } catch (NoSuchElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            showExeptionWindow(e, "Problem przy zapisie do pliku", Alert.AlertType.ERROR);
        }


    }

    @FXML
    private void closeApp() {
        System.exit(0);
    }

    /*
    TODO:
    -Podczas zamykania wyświetl monit o zapisie




     */


}


