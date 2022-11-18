package com.system64.kurumisynth.view;

import com.system64.kurumisynth.model.Synth;
import com.system64.kurumisynth.viewmodel.SynthViewModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;

public class SynthView {
    private SynthViewModel synthVM;
    private Synth synth;
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
    void initialize() {
        synthVM = new SynthViewModel();
        //synth = new Synth();
        doBindings();
        addListeners();
    }
    void doBindings() {
        algSlider.valueProperty().bindBidirectional(synthVM.algProp());
        gainSlider.valueProperty().bindBidirectional(synthVM.gainProp());
        smoothSlider.valueProperty().bindBidirectional(synthVM.smoothWinProp());
        macSlider.valueProperty().bindBidirectional(synthVM.macroProp());
        macLenSlider.valueProperty().bindBidirectional(synthVM.macroLenProp());
    }
    void addListeners() {
        algSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            algSlider.setValue(newVal.intValue());
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
