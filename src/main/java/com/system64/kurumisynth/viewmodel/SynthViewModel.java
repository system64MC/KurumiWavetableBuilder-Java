package com.system64.kurumisynth.viewmodel;

import com.system64.kurumisynth.model.Globals;
import com.system64.kurumisynth.model.Synth;
import javafx.beans.property.*;

public class SynthViewModel {
    private IntegerProperty algProp = new SimpleIntegerProperty();
    private IntegerProperty smoothWinProp = new SimpleIntegerProperty();
    private IntegerProperty macroLenProp = new SimpleIntegerProperty();
    private IntegerProperty macroProp = new SimpleIntegerProperty();
    private ListProperty opList = new SimpleListProperty();
    private ListProperty waveOutProp = new SimpleListProperty();
    private FloatProperty gainProp = new SimpleFloatProperty();
    private IntegerProperty waveLenProp = new SimpleIntegerProperty();
    private IntegerProperty waveHeiProp = new SimpleIntegerProperty();

    private Synth synthModel;

    public SynthViewModel() {
        synthModel = new Synth();
        Globals.synth = synthModel;
        waveLenProp.set(synthModel.getWaveLen());
        waveHeiProp.set(synthModel.getWaveHeight());
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
            synthModel.setWaveLen(newVal.intValue());
            synthModel.synthesize();
            Globals.drawWaveOut();
        });

        waveHeiProp.addListener((obs, oldVal, newVal) -> {
            synthModel.setWaveHeight(newVal.intValue());
            synthModel.synthesize();
            Globals.drawWaveOut();
        });

        gainProp.addListener((obs, oldVal, newVal) -> {
            synthModel.setGain(newVal.floatValue());
            synthModel.synthesize();
            Globals.drawWaveOut();
        });

        macroProp.addListener((obs, oldVal, newVal) -> {
            synthModel.setMacro(newVal.intValue());
            synthModel.synthesize();
            Globals.drawWaveOut();
        });

        macroLenProp.addListener((obs, oldVal, newVal) -> {
            synthModel.setMacLen(newVal.intValue());
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
    public FloatProperty gainProp() {

        //return gainProp;
        return this.gainProp;
    }

    public IntegerProperty macroLenProp() {

        //return macroLenProp;
        return this.macroLenProp;
    }

    public IntegerProperty macroProp() {

        //return macroProp;
        return this.macroProp;
    }
}
