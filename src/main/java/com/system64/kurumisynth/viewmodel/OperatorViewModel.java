package com.system64.kurumisynth.viewmodel;

import com.system64.kurumisynth.model.Globals;
import com.system64.kurumisynth.model.Operator;
import javafx.beans.property.*;

import java.util.Arrays;


public class OperatorViewModel {
    private BooleanProperty isADSR = new SimpleBooleanProperty();

    public Operator getOpModel() {
        return opModel;
    }

    private Operator opModel;

    private FloatProperty tlVolumeProp = new SimpleFloatProperty();
    private FloatProperty phaseProp = new SimpleFloatProperty();
    private ObjectProperty adsr = new SimpleObjectProperty();
    private IntegerProperty waveProp = new SimpleIntegerProperty();
    private IntegerProperty modProp = new SimpleIntegerProperty();
    private IntegerProperty interpolationProp = new SimpleIntegerProperty();
    private IntegerProperty multProp = new SimpleIntegerProperty();
    private FloatProperty feedbackProp = new SimpleFloatProperty();
    private StringProperty waveStrProp = new SimpleStringProperty();
    private BooleanProperty phaseModProp = new SimpleBooleanProperty();
    private IntegerProperty attackProp = new SimpleIntegerProperty();
    private IntegerProperty decayProp = new SimpleIntegerProperty();
    private FloatProperty susProp = new SimpleFloatProperty();
    private BooleanProperty revPhaseprop = new SimpleBooleanProperty();
    private IntegerProperty detuneProp = new SimpleIntegerProperty();
    private StringProperty volStrProp = new SimpleStringProperty();
    private StringProperty phaseStrProp = new SimpleStringProperty();
    private BooleanProperty standardPhaseProp = new SimpleBooleanProperty();
    private BooleanProperty isMorphEnabledProp = new SimpleBooleanProperty();
    private StringProperty morphStrProp = new SimpleStringProperty();

    private IntegerProperty morphFramesProp = new SimpleIntegerProperty();

    public OperatorViewModel() {
        opModel = new Operator();
        Globals.opVMs.add(this);
        isMorphEnabledProp.set(opModel.isMorphDisabled());
        morphFramesProp.set(opModel.getMorphFrames());
        setListeners();
        if (Globals.opVMs.indexOf(this) == 3)
        {
            this.opModel.setTl(1);
            //this.opModel.setMult(1);
            this.opModel.getAdsr().setSustain(1);
            this.tlVolumeProp.set(1.0f);
            this.susProp.set(1.0f);
            System.out.println(opModel.getAdsr());
        }
        waveStrProp.set(getWTStr());
        volStrProp.set(getEnvStr());
        phaseStrProp.set(getPhaseStr());
        morphStrProp.set(getMorphStr());
        System.out.println("Creating Operator VM!");
    }

    public String getWTStr() {
        String out = "";
        for(int i = 0; i < opModel.getWavetable().length; i++)
        {
            out += opModel.getWavetable()[i] + " ";
        }
        return out;
    }

    public String getMorphStr() {
        String out = "";
        for(int i = 0; i < opModel.getWavetableMorph().length; i++)
        {
            out += opModel.getWavetableMorph()[i] + " ";
        }
        return out;
    }

    public String getEnvStr() {
        String out = "";
        for(int i = 0; i < opModel.getCustEnv().length; i++)
        {
            out += opModel.getCustEnv()[i] + " ";
        }
        return out;
    }

    public String getPhaseStr() {
        String out = "";
        for(int i = 0; i < opModel.getPhaseEnv().length; i++)
        {
            out += opModel.getPhaseEnv()[i] + " ";
        }
        return out;
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

    public IntegerProperty modProp() {
        return this.modProp;
    }

    public StringProperty waveStrProp() {
        return this.waveStrProp;
    }

    public StringProperty phaseStrProp() {
        return this.phaseStrProp;
    }

    public StringProperty volStrProp() {
        return this.volStrProp;
    }

    public BooleanProperty phaseModProp() {
        return this.phaseModProp;
    }

    public IntegerProperty attackProp() {
        return this.attackProp;
    }

    public IntegerProperty decayProp() {
        return this.decayProp;
    }

    public FloatProperty susProp() {
        return this.susProp;
    }

    public BooleanProperty revPhaseProp() {
        return this.revPhaseprop;
    }

    public IntegerProperty detuneProp() {
        return this.detuneProp;
    }

    public BooleanProperty isADSR() {
        return this.isADSR;
    }

    public BooleanProperty standardPhaseProp() {
        return this.standardPhaseProp;
    }

    public BooleanProperty isMorphEnabledProp() {
        return this.isMorphEnabledProp;
    }

    public StringProperty morphStrProp() {
        return this.morphStrProp;
    }

    public IntegerProperty morphFramesProp() {
        return this.morphFramesProp;
    }

    private void strToWT() {
        int len = waveStrProp.get().length();
        if(len == 0)
        {
            int[] wt = {0};
            opModel.setWavetable(wt);
            return;
        }
        int[] myWT = Arrays.stream(Arrays.stream(waveStrProp.get()
                .trim()
                .split(" "))
                .filter(x -> !x.equals(""))
                .mapToInt(Integer::parseInt).toArray()).toArray();
        opModel.setWavetable(myWT);
    }

    private void strToMorph() {
        int len = morphStrProp.get().length();
        if(len == 0)
        {
            int[] wt = {0};
            opModel.setWavetableMorph(wt);
            return;
        }
        int[] myWT = Arrays.stream(Arrays.stream(morphStrProp.get()
                        .trim()
                        .split(" "))
                .filter(x -> !x.equals(""))
                .mapToInt(Integer::parseInt).toArray()).toArray();
        opModel.setWavetableMorph(myWT);
    }

    private void strToEnv() {
        int len = volStrProp.get().length();
        if(len == 0)
        {
            int[] wt = {255};
            opModel.setCustEnv(wt);
            return;
            // Set env in the model
        }
        int[] myWT = Arrays.stream(Arrays.stream(volStrProp.get()
                        .trim()
                        .split(" "))
                .filter(x -> !x.equals(""))
                .mapToInt(Integer::parseInt).toArray()).toArray();
        int[] myWT2 = new int[myWT.length];
        for(int i = 0; i < myWT.length; i++)
            myWT2[i] = Math.min(myWT[i], 255);
        opModel.setCustEnv(myWT2);

    }

    private void strToPhase() {
        int len = phaseStrProp.get().length();
        if(len == 0)
        {
            int[] wt = {0};
            opModel.setPhaseEnv(wt);
            return;
            // Set env in the model
        }
        int[] myWT = Arrays.stream(Arrays.stream(phaseStrProp.get()
                        .trim()
                        .split(" "))
                .filter(x -> !x.equals(""))
                .mapToInt(Integer::parseInt).toArray()).toArray();
        int[] myWT2 = new int[myWT.length];
        for(int i = 0; i < myWT.length; i++)
            myWT2[i] = Math.min(myWT[i], 255);
        opModel.setPhaseEnv(myWT2);

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

        modProp.addListener((obs, oldVal, newVal) -> {
            opModel.setModMode(newVal.intValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        waveStrProp().addListener((obs, oldVal, newVal) -> {
            strToWT();
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        volStrProp.addListener((obs, oldVal, newVal) -> {
            strToEnv();
            Globals.synth.synthesize();
            Globals.drawWaveOut();

        });

        phaseStrProp.addListener((obs, oldVal, newVal) -> {
            strToPhase();
            Globals.synth.synthesize();
            Globals.drawWaveOut();

        });

        phaseModProp.addListener((obs, oldVal, newVal) -> {
            opModel.setPhaseMod(phaseModProp.get());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        attackProp.addListener((obs, oldVal, newVal) -> {
            opModel.getAdsr().setAttack(newVal.intValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        decayProp.addListener((obs, oldVal, newVal) -> {
            opModel.getAdsr().setDecay(newVal.intValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        susProp.addListener((obs, oldVal, newVal) -> {
            opModel.getAdsr().setSustain(newVal.floatValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        revPhaseprop.addListener((obs, oldVal, newVal) -> {
            opModel.setPhaseRev(newVal.booleanValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        detuneProp.addListener((obs, oldVal, newVal) -> {
            opModel.setDetune(newVal.intValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        isADSR.addListener((obs, oldVal, newVal) -> {
            opModel.setUseADSR(newVal.booleanValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        standardPhaseProp.addListener((obs, oldVal, newVal) -> {
            opModel.setStandardPhase(newVal.booleanValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        morphStrProp.addListener((obs, oldVal, newVal) -> {
            strToMorph();
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        morphFramesProp.addListener((obs, oldVal, newVal) -> {
            opModel.setMorphFrames(newVal.intValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });

        isMorphEnabledProp.addListener((obs, oldVal, newVal) -> {
            opModel.setMorphDisabled(newVal.booleanValue());
            Globals.synth.synthesize();
            Globals.drawWaveOut();
        });
    }

}
