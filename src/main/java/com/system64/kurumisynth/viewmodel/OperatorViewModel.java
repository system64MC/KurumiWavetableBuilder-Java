package com.system64.kurumisynth.viewmodel;

import com.system64.kurumisynth.model.Globals;
import com.system64.kurumisynth.model.Operator;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;


public class OperatorViewModel {
    private Operator opModel;

    private FloatProperty tlVolumeProp = new SimpleFloatProperty();
    private ObjectProperty adsr = new SimpleObjectProperty();
    public OperatorViewModel() {
        opModel = new Operator();
        Globals.opVMs.add(this);
        setListeners();
        if (Globals.opVMs.indexOf(this) == 3)
        {
            this.opModel.setTl(1);
            this.opModel.getAdsr().setSustain(1);
            this.tlVolumeProp.set(1.0f);
            System.out.println(opModel.getAdsr());
        }
        System.out.println("Creating Operator VM!");
    }

    public FloatProperty tlVolumeProp() {
        return this.tlVolumeProp;
    }

    public void setListeners() {
        tlVolumeProp.addListener((obs, oldVal, newVal) -> {
            opModel.setTl(newVal.floatValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });
    }

}
