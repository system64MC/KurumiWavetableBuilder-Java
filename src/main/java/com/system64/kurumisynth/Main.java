package com.system64.kurumisynth;

import com.system64.kurumisynth.view.SynthView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("kurumi-layout.fxml"));
        //var myView = new SynthView();
        Scene scene = new Scene(fxmlLoader.load(), 1240, 720);
        stage.setTitle("Kurumi Wavetable Builder");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
