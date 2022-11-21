package com.system64.kurumisynth.view;

import com.system64.kurumisynth.viewmodel.OperatorViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;

public class OperatorView {
    @FXML
    private Slider tlSlider;

    private OperatorViewModel opVM;

    @FXML
    void initialize() {
        opVM = new OperatorViewModel();
        //tlSlider = new Slider(10, 10, 10);
    }

}
