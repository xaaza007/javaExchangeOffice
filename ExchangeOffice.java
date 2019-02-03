package exercises.kantor;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ExchangeOffice extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {

    VBox root = new VBox();
    root.setAlignment(Pos.CENTER_LEFT);
    root.setPadding(new Insets(50, 100, 100, 100));
    root.setSpacing(10);

    List<CurrencyJsonReader.CurrencyRate> list;
    List<CurrencyJsonReader.CurrencyRate> temp = new ArrayList<>();

    Label currencyLabel = new Label("Wybierz walute");
    root.getChildren().addAll(currencyLabel);
    ComboBox<String> currencyChoice = new ComboBox<>();
    try {
      CurrencyJsonReader reader = new CurrencyJsonReader();
      temp = reader.getCurrencies();
      for (CurrencyJsonReader.CurrencyRate item : temp) {
        currencyChoice.getItems().add(item.name);
      }
      currencyChoice.getSelectionModel().select(0);
    } catch (MalformedURLException e) {
      Alert info = new Alert(Alert.AlertType.ERROR);
      info.setHeaderText("Błąd sieci");
      info.setContentText("Nie można pobrać kursów walut");
      info.show();
    } catch (IOException e) {
      Alert info = new Alert(Alert.AlertType.ERROR);
      info.setHeaderText("Niepoprawna forma url do API");
      info.setContentText("Nie można pobrać kursów walut");
      info.show();
    } finally {
      list = temp;
    }
    root.getChildren().addAll(currencyChoice);


    Label currencyPln = new Label("Kwota w PLN");
    root.getChildren().addAll(currencyPln);
    TextField amountPln = new TextField();
    root.getChildren().addAll(amountPln);

    Label amountInCurrency = new Label("Kwota w walucie");
    root.getChildren().addAll(amountInCurrency);
    TextField amount = new TextField();
    amount.setEditable(false);
    root.getChildren().addAll(amount);

    currencyChoice.setOnAction(event -> {
      if (!amountPln.getText().equals("") && currencyChoice.getSelectionModel().getSelectedIndex() > -1) {
        //pobieranie indeksu z pozycji wybranej z listy walut
        int index = currencyChoice.getSelectionModel().getSelectedIndex();
        //pobieranie rekordu z listy kursow
        CurrencyJsonReader.CurrencyRate rate = list.get(index);
        //konwercja pola tekstowego z kwotą w złotówkach na typ double
        double kwota = Double.parseDouble(amountPln.getText());
        //obliczenie kwoty w walucie i umieszczenie jej w kontrolce kwoty w walucie
        amount.setText((kwota / rate.rate) + "");
      }
    });

    amountPln.setOnAction(event -> {
      if (!amountPln.getText().equals("") && currencyChoice.getSelectionModel().getSelectedIndex() > -1) {
        //pobieranie indeksu z pozycji wybranej z listy walut
        int index = currencyChoice.getSelectionModel().getSelectedIndex();
        //pobieranie rekordu z listy kursow
        CurrencyJsonReader.CurrencyRate rate = list.get(index);
        //konwercja pola tekstowego z kwotą w złotówkach na typ double
        double kwota = Double.parseDouble(amountPln.getText());
        //obliczenie kwoty w walucie i umieszczenie jej w kontrolce kwoty w walucie
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        amount.setText(format.format(kwota/rate.rate));
      }
    });

    amountPln.setOnKeyTyped(event -> {
      String str = amountPln.getText();
      try {
        double kwota = Double.parseDouble(amountPln.getText());
      } catch (NumberFormatException e) {
        amountPln.setText("");
      }
    });


    Scene scene = new Scene(root, 400, 600);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Kantor Online");
    primaryStage.setResizable(false);
    primaryStage.show();
  }
}
