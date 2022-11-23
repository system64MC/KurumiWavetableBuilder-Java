package com.system64.kurumisynth.view;

import com.sun.jndi.toolkit.url.Uri;
import com.system64.kurumisynth.Main;
import com.system64.kurumisynth.model.Globals;
import com.system64.kurumisynth.viewmodel.SynthViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import sun.misc.BASE64Decoder;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import static com.sun.javafx.util.Utils.clamp;

public class SynthView {
    @FXML
    private Label lenLabel;
    @FXML
    private Label heiLabel;
    @FXML
    private Label macLabel;
    @FXML
    private Label macLenLabel;
    @FXML
    private Label gainLabel;
    private SynthViewModel synthVM;
    @FXML
    private Slider algSlider;
    @FXML
    private Slider gainSlider;
    @FXML
    private Slider smoothSlider;
    @FXML
    private Slider macLenSlider;
    @FXML
    private Slider macSlider;

    @FXML
    private ImageView algDisplay;

    @FXML
    private ScrollPane opList;

    @FXML
    private VBox opBox;

    @FXML
    private Canvas waveDrawCvs;

    @FXML
    private TextField waveOutTextField;

    @FXML
    private Slider lenSlider;

    @FXML
    private Slider heiSlider;

    @FXML
    private Label algLabel;

    @FXML
    void initialize() {
        synthVM = new SynthViewModel();
        Globals.txtField = this.waveOutTextField;
        Globals.setStringTextField();
        waveOutTextField.setOnMouseClicked(e -> {
            copyToClipboard();
        });
        Globals.waveDrawCvs = waveDrawCvs;

        //algDisplay.setImage(image);

        doBindings();
        algDisplay.setViewport(new Rectangle2D(algSlider.getValue() * 128, 0.0, 128, 64.0));
        gainSlider.setValue(1);
        macLenSlider.setValue(Globals.synth.getMacLen());
        addListeners();
        //var myView = new SynthView();


        try {
            loadOperatorsUI();
            loadImage();
        }
        catch (Exception e)
        {
            System.out.println("E");
        }


        //var myOpView = new OperatorView();
        //var myView = new SynthView();

    }

    void loadImage() throws IOException {
        BASE64Decoder b64 = new BASE64Decoder();
        ByteArrayInputStream is = new ByteArrayInputStream(b64.decodeBuffer(
                "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"
        ));
        Image img = new Image(is);
        //algDisplay.imageProperty().setValue(img);
        algDisplay.setImage(img);
        //algDisplay.setFitHeight(img.getHeight());
        //algDisplay.setFitWidth(img.getWidth());
        //algDisplay.setPreserveRatio(true);
    }

    void loadOperatorsUI() throws IOException {
        URL res = Main.class.getResource("operator.fxml");
        System.out.println("Op lenght = " + Globals.opVMs.size());
        for(int i = 0; i < 4; i++)
        {
            FXMLLoader fxmlLoader = new FXMLLoader(res);
            VBox myVbox = fxmlLoader.load();
            opBox.getChildren().add(myVbox);
        }
    }
    void doBindings() {
        //synthVM.algProp()
        algSlider.valueProperty().bindBidirectional(synthVM.algProp());
        gainSlider.valueProperty().bindBidirectional(synthVM.gainProp());
        smoothSlider.valueProperty().bindBidirectional(synthVM.smoothWinProp());
        macSlider.valueProperty().bindBidirectional(synthVM.macroProp());
        macLenSlider.valueProperty().bindBidirectional(synthVM.macroLenProp());
        lenSlider.valueProperty().bindBidirectional(synthVM.waveLenProp());
        heiSlider.valueProperty().bindBidirectional(synthVM.waveHeiProp());
        gainSlider.valueProperty().bindBidirectional(synthVM.gainProp());
    }
    void addListeners() {
        algSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            algSlider.setValue(newVal.intValue());
            algDisplay.setViewport(new Rectangle2D(synthVM.algProp().get() * 128, 0.0, 128, 64.0));
            algLabel.setText("Algorithm : " + synthVM.algProp().get());
            Globals.setStringTextField();
            //synthVM.algProp().set(newVal.byteValue());
            //System.out.println(synthVM.algProp());
            //synth.setAlgorithm(newVal.byteValue());
        });

        lenSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            lenSlider.setValue(newVal.intValue());
            lenLabel.setText("Length : " + synthVM.waveLenProp().get());
            Globals.setStringTextField();
        });

        heiSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            heiSlider.setValue(newVal.intValue());
            heiLabel.setText("Height : " + synthVM.waveHeiProp().get());
            Globals.setStringTextField();
        });

        gainSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            gainLabel.setText("Gain : " + synthVM.gainProp().get());
            Globals.setStringTextField();
        });

        smoothSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            Globals.setStringTextField();
            //synth.setSmoothWin(newVal.intValue());
        });

        macLenSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            macLenSlider.setValue(newVal.intValue());
            //synthVM.macroLenProp().set(newVal.intValue());
            macLenLabel.setText("Macro Length : " + synthVM.macroLenProp().get());
            macSlider.setValue(clamp(0, macSlider.getValue(), newVal.intValue() - 1));
            macSlider.setMax(newVal.intValue() - 1);
            Globals.setStringTextField();
        });

        macSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            macSlider.setValue(newVal.intValue());
            //Globals.macro = newVal.intValue();
            //algDisplay.setViewport(new Rectangle2D(synthVM.algProp().get() * 128, 0.0, 128, 64.0));
            macLabel.setText("Wave Sequence : " + synthVM.macroProp().get());
            Globals.setStringTextField();
        });
    }

    private void copyToClipboard() {
        StringSelection selection = new StringSelection(waveOutTextField.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }


}
