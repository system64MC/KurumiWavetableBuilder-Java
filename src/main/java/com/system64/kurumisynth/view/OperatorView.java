package com.system64.kurumisynth.view;

import com.system64.kurumisynth.viewmodel.OperatorViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class OperatorView {
    @FXML
    private Slider tlSlider;

    @FXML
    private Slider phaseSlider;

    @FXML
    private Slider waveSlider;

    @FXML
    private VBox vBox;
    private OperatorViewModel opVM;

    @FXML
    void initialize() {
        System.out.println();
        opVM = new OperatorViewModel();
        doBindings();
        //tlSlider = new Slider(10, 10, 10);
    }

    void doBindings() {
        tlSlider.valueProperty().bindBidirectional(opVM.tlVolumeProp());
    }

}
