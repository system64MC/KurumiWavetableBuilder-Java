package com.system64.kurumisynth.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import com.system64.kurumisynth.model.Synth;
import javafx.fxml.FXML;

public class SynthViewModel {
    private IntegerProperty algProp = new SimpleIntegerProperty();
    private IntegerProperty smoothWinProp = new SimpleIntegerProperty();
    private IntegerProperty gainProp = new SimpleIntegerProperty();
    private IntegerProperty macroLenProp = new SimpleIntegerProperty();
    private IntegerProperty macroProp = new SimpleIntegerProperty();

    private Synth synthModel;

    public SynthViewModel() {
        synthModel = new Synth();
        System.out.println("Init Synth VM");
        SetListeners();
        //algProp().addListener(e -> setAlgPropAction());
    }

    private void SetListeners() {
        algProp.addListener((obs, oldVal, newVal) -> {
            synthModel.setAlgorithm(newVal.byteValue());
            synthModel.synthesize();
        });
    }


    public IntegerProperty algProp() {
        //return algProp;
        //System.out.println("EEEEE");
        return this.algProp;
    }

    public void setAlgPropAction() {
        System.out.println("EEE");
    }

    public IntegerProperty smoothWinProp() {
        //return smoothWinProp;
        return new SimpleIntegerProperty(2);
    }

    public IntegerProperty gainProp() {

        //return gainProp;
        return new SimpleIntegerProperty(1);
    }

    public IntegerProperty macroLenProp() {

        //return macroLenProp;
        return new SimpleIntegerProperty(5);
    }

    public IntegerProperty macroProp() {

        //return macroProp;
        return new SimpleIntegerProperty(9);
    }
}
