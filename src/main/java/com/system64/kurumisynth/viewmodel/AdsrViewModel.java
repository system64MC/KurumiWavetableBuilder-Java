package com.system64.kurumisynth.viewmodel;

import com.system64.kurumisynth.model.Adsr;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class AdsrViewModel {
    private IntegerProperty attack = new SimpleIntegerProperty();
    private IntegerProperty decay = new SimpleIntegerProperty();
    private FloatProperty sustain = new SimpleFloatProperty();
    private Adsr adsr;
    public AdsrViewModel() {
        this.adsr = new Adsr();
    }
}
