package com.system64.kurumisynth.viewmodel;

import com.system64.kurumisynth.model.Globals;
import com.system64.kurumisynth.model.Operator;
import javafx.beans.property.*;


public class OperatorViewModel {
    private Operator opModel;

    private FloatProperty tlVolumeProp = new SimpleFloatProperty();
    private FloatProperty phaseProp = new SimpleFloatProperty();
    private ObjectProperty adsr = new SimpleObjectProperty();
    private IntegerProperty waveProp = new SimpleIntegerProperty();
    private IntegerProperty interpolationProp = new SimpleIntegerProperty();
    private IntegerProperty multProp = new SimpleIntegerProperty();
    private FloatProperty feedbackProp = new SimpleFloatProperty();
    public OperatorViewModel() {
        opModel = new Operator();
        Globals.opVMs.add(this);
        setListeners();
        if (Globals.opVMs.indexOf(this) == 3)
        {
            this.opModel.setTl(1);
            //this.opModel.setMult(1);
            this.opModel.getAdsr().setSustain(1);
            this.tlVolumeProp.set(1.0f);
            System.out.println(opModel.getAdsr());
        }
        System.out.println("Creating Operator VM!");
    }

    public FloatProperty tlVolumeProp() {
        return this.tlVolumeProp;
    }

    public FloatProperty phaseProp() {
        return this.phaseProp;
    }

    public IntegerProperty waveProp() {
        return this.waveProp;
    }

    public IntegerProperty multProp() {
        return this.multProp;
    }
    public IntegerProperty interpolationProp() { return this.interpolationProp; }

    public FloatProperty feedbackProp() {
        return this.feedbackProp;
    }

    public void setListeners() {
        tlVolumeProp.addListener((obs, oldVal, newVal) -> {
            opModel.setTl(newVal.floatValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        phaseProp.addListener((obs, oldVal, newVal) -> {
            opModel.setPhase(newVal.floatValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        feedbackProp.addListener((obs, oldVal, newVal) -> {
            opModel.setFeedback(newVal.floatValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        waveProp.addListener((obs, oldVal, newVal) -> {
            opModel.setWaveformId(newVal.intValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        multProp.addListener((obs, oldVal, newVal) -> {
            opModel.setMult(newVal.intValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        interpolationProp.addListener((obs, oldVal, newVal) -> {
            opModel.setInterpolation(newVal.intValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });
    }

}
