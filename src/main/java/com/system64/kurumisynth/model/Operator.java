package com.system64.kurumisynth.model;

import java.util.ArrayList;

import static com.system64.kurumisynth.model.Globals.macLen;
import static com.system64.kurumisynth.model.Globals.macro;
import static org.controlsfx.tools.Utils.clamp;

public class Operator {
    private Adsr adsr;

    public int[] getWavetable() {
        return wavetable;
    }

    public void setWavetable(int[] wavetable) {
        this.wavetable = wavetable;
    }

    private int[] wavetable = {16, 25, 30, 31, 30, 29, 26, 25, 25, 28, 31, 28, 18, 11, 10, 13, 17, 20, 22, 20, 15, 6, 0, 2, 6, 5, 3, 1, 0, 0, 1, 4};

    private ArrayList<Integer> customVolEnv = new ArrayList<Integer>();

    public boolean isCustomVolumeEnv() {
        return customVolumeEnv;
    }

    public void setCustomVolumeEnv(boolean customVolumeEnv) {
        this.customVolumeEnv = customVolumeEnv;
    }

    private boolean customVolumeEnv;

    public float getPrev() {
        return prev;
    }

    public void setPrev(float prev) {
        this.prev = prev;
    }

    private float prev;

    public int getFeedback() {
        return feedback;
    }

    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

    private int feedback;
    private int phaseRev;

    public int getDetune() {
        return detune;
    }

    public boolean isPhaseMod() {
        return phaseMod;
    }

    public void setPhaseMod(boolean phaseMod) {
        this.phaseMod = phaseMod;
    }

    private boolean phaseMod;

    public void setDetune(int detune) {
        this.detune = detune;
    }

    private  int detune;

    public void setPhaseRev(boolean isRev) {
        this.phaseRev = isRev ? -1 : 1;
    }

    private ArrayList<Integer> customPhaseEnv = new ArrayList<Integer>();

    private int waveformId;

    public boolean isCustomPhase() {
        return customPhase;
    }

    public void setCustomPhase(boolean customPhase) {
        this.customPhase = customPhase;
    }

    private boolean customPhase;

    public float getPhase() {
        return phase;
    }

    public void setPhase(float phase) {
        this.phase = phase;
    }

    private float phase;

    private int mult;

    public int getMult() {
        return mult;
    }

    public void setMult(int mult) {
        this.mult = mult;
    }

    public int getWaveformId() {
        return waveformId;
    }

    public void setWaveformId(int waveformId) {
        this.waveformId = waveformId;
    }

    public float getTl() {
        return tl;
    }

    public void setTl(float tl) {
        this.tl = tl;
    }

    private float tl;

    public Adsr getAdsr() {
        return adsr;
    }

    public void setAdsr(Adsr adsr) {
        this.adsr = adsr;
    }

    public Operator() {
        this.tl = 0;
        this.adsr = new Adsr();
        this.waveformId = 0;
        this.mult = 1;
        this.phase = 0;
        this.customPhase = false;
        this.detune = 0;
        this.phaseMod = false;
        this.feedback = 0;
        this.prev = 0;
        this.customVolumeEnv = false;
    }

    public Operator(float tl, int attack, int decay, float sustain, int waveformId, int mult, float phase) {
        this.tl = tl;
        this.adsr = new Adsr(attack, decay, sustain);
        this.waveformId = waveformId;
        this.mult = mult;
        this.phase = phase;
        this.customPhase = false;
        this.detune = 0;
        this.phaseMod = false;
        this.feedback = 0;
        this.prev = 0;
        this.customVolumeEnv = false;
    }

    private float pGetPhase() {
        int myPhaseMod = phaseMod ? 1 : 0;
        if(customPhase)
            return (customPhaseEnv.get((int)(clamp(0, macro, customPhaseEnv.size()-1)))/macLen-1) *
                    phaseRev * detune * myPhaseMod;
        return (macro/macLen-1) * phaseRev * detune * myPhaseMod;
    }

    public float getFB() {
        return feedback * (prev / 6 * mult);
    }
    public float oscillate(float x) {
        switch (this.waveformId)
        {
            case 0: // 0 - Sine
                return (float) Math.sin((x * mult * 2 * Math.PI) + (phase * Math.PI * 2 + (pGetPhase() * Math.PI * 2)));
        }
        return (float) Math.sin((x * mult * 2 * Math.PI) + (phase * Math.PI * 2 + (pGetPhase() * Math.PI * 2)));
    }

    private float customEnv(int mac) {
        int index = (int) clamp(0.0, (double) mac, (double) customVolEnv.size()-1);
        return tl * (customVolEnv.get(index)/ 63);
    }

    private float adsr(int mac) {
        var atk = adsr.getAttack();
        var dec = adsr.getDecay();
        var sus = adsr.getSustain();
        // Attack
        if(mac <= atk)
        {
            if(atk <= 0)
                return tl;
            float step = tl / atk;
            if(mac * step - macLen >= tl)
                return tl;
            return mac * step;
        }

        // Decay and Sustain
        if(mac > atk)
        {
            if(dec <= 0)
                return sus;
            float tick = ((mac - atk) / dec);
            float decVal = (sus - tl) * tick + tl;
            return decVal < sus ? sus : decVal;
        }

        return 0;
    }

    public float getVolume(int mac) {
        if(customVolumeEnv)
            return customEnv(mac);
        return adsr(mac);
    }

    @Override
    public String toString() {
        return  "[FM Operator]" +
                "\nTL : " + this.tl +
                "\nWaveform : " + this.waveformId +
                "\nMult : " + this.mult +
                "\nPhase : " + this.phase +
                "\nDetune : " + this.detune +
                "\nFeedback : " + this.feedback;
    }
}
