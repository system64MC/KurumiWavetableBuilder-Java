package com.system64.kurumisynth.viewmodel;

import com.system64.kurumisynth.model.Globals;
import com.system64.kurumisynth.model.Synth;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;

public class SynthViewModel {
    private IntegerProperty algProp = new SimpleIntegerProperty();
    private IntegerProperty smoothWinProp = new SimpleIntegerProperty();
    private IntegerProperty gainProp = new SimpleIntegerProperty();
    private IntegerProperty macroLenProp = new SimpleIntegerProperty();
    private IntegerProperty macroProp = new SimpleIntegerProperty();
    private ListProperty opList = new SimpleListProperty();
    private ListProperty waveOutProp = new SimpleListProperty();
    private IntegerProperty waveLenProp = new SimpleIntegerProperty();
    private IntegerProperty waveHeiProp = new SimpleIntegerProperty();

    private Synth synthModel;

    public SynthViewModel() {
        synthModel = new Synth();
        Globals.synth = synthModel;
        waveLenProp.set(Globals.waveLen);
        waveHeiProp.set(Globals.waveHeight);
        System.out.println("Init Synth VM");
        setListeners();
        //algProp().addListener(e -> setAlgPropAction());
    }

    private void setListeners() {
        algProp.addListener((obs, oldVal, newVal) -> {
            synthModel.setAlgorithm(newVal.byteValue());
            synthModel.synthesize();
            Globals.drawWaveOut();
        });

        waveLenProp.addListener((obs, oldVal, newVal) -> {
            Globals.waveLen = newVal.intValue();
            synthModel.synthesize();
            Globals.drawWaveOut();
        });

        waveHeiProp.addListener((obs, oldVal, newVal) -> {
            Globals.waveHeight = newVal.intValue();
            synthModel.synthesize();
            Globals.drawWaveOut();
        });
    }

    public ListProperty waveOutProp() {
        return new SimpleListProperty();
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

    public IntegerProperty waveLenProp() {
        return this.waveLenProp;
    }

    public IntegerProperty waveHeiProp() {
        return this.waveHeiProp;
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
