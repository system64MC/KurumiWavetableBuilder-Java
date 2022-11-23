package com.system64.kurumisynth.view;

import com.system64.kurumisynth.model.Globals;
import com.system64.kurumisynth.viewmodel.OperatorViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class OperatorView {
    @FXML
    private Slider feedbackSlider;
    @FXML
    private Label fbLabel;
    @FXML
    private Label phaseLabel;
    @FXML
    private Label waveLabel;
    @FXML
    private Label multLabel;
    @FXML
    private Label tlLabel;
    @FXML
    private Slider tlSlider;

    @FXML
    private Slider phaseSlider;

    @FXML
    private Slider waveSlider;

    @FXML
    private VBox vBox;

    @FXML
    private Slider multSlider;
    private OperatorViewModel opVM;

    @FXML
    void initialize() {
        System.out.println();
        opVM = new OperatorViewModel();
        //tlSlider.setValue(opVM.tlVolumeProp().getValue());
        tlLabel.setText("TL : " + opVM.tlVolumeProp().getValue());
        doBindings();
        addListeners();
        //tlSlider = new Slider(10, 10, 10);
    }

    void doBindings() {
        tlSlider.valueProperty().bindBidirectional(opVM.tlVolumeProp());
        phaseSlider.valueProperty().bindBidirectional(opVM.phaseProp());
        waveSlider.valueProperty().bindBidirectional(opVM.waveProp());
        multSlider.valueProperty().bindBidirectional(opVM.multProp());
        feedbackSlider.valueProperty().bindBidirectional(opVM.feedbackProp());
    }

    void addListeners() {
        waveSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            waveSlider.setValue(newVal.intValue());
            Globals.setStringTextField();
        });

        multSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            multSlider.setValue(newVal.intValue());
            multLabel.setText("Mult : " + opVM.multProp().get());
            Globals.setStringTextField();
        });

        tlSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            tlLabel.setText("TL : " + opVM.tlVolumeProp().get());
            Globals.setStringTextField();
        });

        phaseSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            phaseLabel.setText("Phase : " + opVM.phaseProp().get());
            Globals.setStringTextField();
        });

        feedbackSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            fbLabel.setText("Feedback : " + opVM.feedbackProp().get());
            Globals.setStringTextField();
        });
    }

}
