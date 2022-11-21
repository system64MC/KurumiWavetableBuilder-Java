package com.system64.kurumisynth.view;

import com.system64.kurumisynth.Main;
import com.system64.kurumisynth.model.Synth;
import com.system64.kurumisynth.viewmodel.SynthViewModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;

public class SynthView {
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
    void initialize() {
        synthVM = new SynthViewModel();
        //algDisplay.setImage(image);
        doBindings();
        algDisplay.setViewport(new Rectangle2D(algSlider.getValue() * 128, 0.0, 128, 64.0));
        addListeners();

        //var myOpView = new OperatorView();
        //var myView = new SynthView();

    }

    void loadOperatorsUI() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("operator.fxml"));
        Node myVbox = fxmlLoader.load();
        opBox.getChildren().add(myVbox);
    }
    void doBindings() {
        //synthVM.algProp()
        algSlider.valueProperty().bindBidirectional(synthVM.algProp());
        gainSlider.valueProperty().bindBidirectional(synthVM.gainProp());
        smoothSlider.valueProperty().bindBidirectional(synthVM.smoothWinProp());
        macSlider.valueProperty().bindBidirectional(synthVM.macroProp());
        macLenSlider.valueProperty().bindBidirectional(synthVM.macroLenProp());
    }
    void addListeners() {
        algSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            algSlider.setValue(newVal.intValue());
            algDisplay.setViewport(new Rectangle2D(synthVM.algProp().get() * 128, 0.0, 128, 64.0));
            //synthVM.algProp().set(newVal.byteValue());
            //System.out.println(synthVM.algProp());
            //synth.setAlgorithm(newVal.byteValue());
        });

        gainSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            //synth.setGain(newVal.floatValue());
        });

        smoothSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            //synth.setSmoothWin(newVal.intValue());
        });
    }
}
