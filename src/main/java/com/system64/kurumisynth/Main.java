package com.system64.kurumisynth;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("kurumi-layout.fxml"));
        //var myView = new SynthView();
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        stage.setTitle("Kurumi Wavetable Builder");
        stage.setScene(scene);
        setIcon(stage);
        stage.show();
    }

    private void setIcon(Stage stage) throws IOException {
        BASE64Decoder b64 = new BASE64Decoder();
        ByteArrayInputStream is = new ByteArrayInputStream(b64.decodeBuffer(
                // Temporary Kurumi icon
            "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"
        ));
        Image img = new Image(is);
        //algDisplay.imageProperty().setValue(img);
        stage.getIcons().add(img);
    }

    public static void main(String[] args) {
        launch();
    }
}
