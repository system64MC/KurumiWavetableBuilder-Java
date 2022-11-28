package com.system64.kurumisynth.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.*;

import static com.sun.javafx.util.Utils.clamp;
//import static org.controlsfx.tools.Utils.clamp;

public class Operator {
    private Adsr adsr;

    public int getModMode() {
        return modMode;
    }

    public void setModMode(int modMode) {
        this.modMode = modMode;
    }

    private int modMode = 0;

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

    public float getFeedback() {
        return feedback;
    }

    public void setFeedback(float feedback) {
        System.out.println("FEEDBACK");
        this.feedback = feedback;
    }

    private float feedback;
    private int phaseRev = 1;

    public int getDetune() {
        return detune;
    }

    public boolean isPhaseMod() {
        return phaseMod;
    }

    public void setPhaseMod(boolean phaseMod) {
        //System.out.println("CHECK");
        this.phaseMod = phaseMod;
        System.out.println(this.phaseMod);
    }

    private boolean phaseMod;

    public void setDetune(int detune) {
        this.detune = detune;
    }

    private  int detune = 1;

    public void setPhaseRev(boolean isRev) {
        this.phaseRev = isRev ? -1 : 1;
    }

    private ArrayList<Integer> customPhaseEnv = new ArrayList<Integer>();

    private int waveformId;

    public int getInterpolation() {
        return interpolation;
    }

    public void setInterpolation(int interpolation) {
        this.interpolation = interpolation;
    }

    private int interpolation;

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
        System.out.println("New TL : " + this.tl);
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
        this.detune = 1;
        this.phaseMod = false;
        this.feedback = 0;
        this.prev = 0;
        this.customVolumeEnv = false;
        Globals.synth.insertOp(this);
        interpolation = 0;
        initWaves();
    }

    public Operator(float tl, int attack, int decay, float sustain, int waveformId, int mult, float phase) {
        this.tl = tl;
        this.adsr = new Adsr(attack, decay, sustain);
        this.waveformId = waveformId;
        this.mult = mult;
        this.phase = phase;
        this.customPhase = false;
        this.detune = 1;
        this.phaseMod = false;
        this.feedback = 0;
        this.prev = 0;
        this.customVolumeEnv = false;
        this.interpolation = 0;
        Globals.synth.insertOp(this);
        initWaves();
    }

    private float pGetPhase() {
        float myPhaseMod = phaseMod ? 1.0f : 0.0f;
        float macro = Globals.synth.getMacro();
        float macLen = Globals.synth.getMacLen();
        if(customPhase)
            return (customPhaseEnv.get((int)(clamp(0, macro, customPhaseEnv.size()-1)))/macLen-1) *
                    phaseRev * detune * myPhaseMod;
        return (float) ( macro/(macLen-1)) * phaseRev * detune * myPhaseMod;
    }

    public float getFB() {
        return feedback * (prev / 6 * mult);
    }

    private float getSin(float x) {
        return (float) Math.sin((x * mult * 2.0 * Math.PI) + (phase * Math.PI * 2.0 + (pGetPhase() * Math.PI * 2.0)));
    }

    private ArrayList<DoubleUnaryOperator> waves = new ArrayList<>();

    private double modulo(double a, double b) {
        return ((a % b) + b) % b;
    }

    private int modulo(int a, int b) {
        return ((a % b) + b) % b;
    }
    private void initWaves() {
        // 0 - Sine
        waves.add(x -> Math.sin((x * mult * 2.0 * Math.PI) + (phase * Math.PI * 2.0 + (pGetPhase() * Math.PI * 2.0))));
        // 1 - Rectified Sine
        waves.add(x -> waves.get(0).applyAsDouble(x) > 0 ? waves.get(0).applyAsDouble(x) : 0);
        // 2 - Absolute Sine
        waves.add(x -> Math.abs(waves.get(0).applyAsDouble(x)));
        // 3 - Quarter Sine
        waves.add(x -> ((x * mult + phase + pGetPhase()) % 0.5 <= 0.25 ? waves.get(2).applyAsDouble(x) : 0));
        // 4 - Squished Sine
        waves.add(x -> waves.get(0).applyAsDouble(x) > 0 ? Math.sin((x * mult * 4 * Math.PI) + (phase * Math.PI * 4 + (pGetPhase() * Math.PI * 4))) : 0);
        // 5 - Squished Abs. Sine
        waves.add(x -> Math.abs(waves.get(4).applyAsDouble(x)));
        // 6 - Square
        waves.add(x -> modulo(x * Math.PI * 2 * mult + (phase * Math.PI * 2 + pGetPhase() * Math.PI * 2), Math.PI * 2) >= Math.PI ? -1 : 1);
        // 7 - Saw
        waves.add(x -> (Math.atan(Math.tan(x * Math.PI * mult + (phase * Math.PI * 1 + (pGetPhase() * Math.PI * 1)))) / (Math.PI * 0.5)));
        // 8 - Rectified Saw
        waves.add(x -> waves.get(7).applyAsDouble(x) > 0 ? waves.get(7).applyAsDouble(x) : 0);
        // 9 - Absolute Saw
        waves.add(x -> waves.get(7).applyAsDouble(x) < 0 ? waves.get(7).applyAsDouble(x) + 1 : waves.get(7).applyAsDouble(x));
        // 10 - Cubed Saw
        waves.add(x -> Math.pow(waves.get(7).applyAsDouble(x), 3));
        // 11 - Rectified Cubed Saw
        waves.add(x -> Math.pow(waves.get(8).applyAsDouble(x), 3));
        // 12 - Absolute Cubed Saw
        waves.add(x -> Math.pow(waves.get(9).applyAsDouble(x), 3));
        // 13 - Cubed Sine
        waves.add(x -> Math.pow(waves.get(0).applyAsDouble(x), 3));
        // 14 - Cubed Rect. Sine
        waves.add(x -> Math.pow(waves.get(1).applyAsDouble(x), 3));
        // 15 - Abs. Cubed Sine
        waves.add(x -> Math.pow(waves.get(2).applyAsDouble(x), 3));
        // 16 - Quarter Cubed Sine
        waves.add(x -> Math.pow(waves.get(3).applyAsDouble(x), 3));
        // 17 - Squished Cubed Sine
        waves.add(x -> Math.pow(waves.get(4).applyAsDouble(x), 3));
        // 18 - Squi. Abs. Cub. Sine
        waves.add(x -> Math.pow(waves.get(5).applyAsDouble(x), 3));
        // 19 - Triangle
        waves.add(x -> Math.asin(waves.get(0).applyAsDouble(x)) / (Math.PI * 0.5));
        // 20 - Rectified Triangle
        waves.add(x -> waves.get(19).applyAsDouble(x) > 0 ? waves.get(19).applyAsDouble(x) : 0);
        // 21 - Absolute Triangle
        waves.add(x -> Math.abs(waves.get(19).applyAsDouble(x)));
        // 22 - Quarter Triangle
        waves.add(x -> ((x * mult + (phase + pGetPhase())) % 0.5) <= 0.25 ? waves.get(19).applyAsDouble(x) : 0);
        // 23 - Squished Triangle
        waves.add(x -> (waves.get(0).applyAsDouble(x) > 0 ? Math.asin(Math.sin((x * mult * 4 * Math.PI)   + (phase * Math.PI * 4 + (pGetPhase() * Math.PI * 4)) ))/ (Math.PI/2) : 0));
        // 24 - Abs. Squished Triangle
        waves.add(x -> Math.abs(waves.get(23).applyAsDouble(x)));
        // 25 - Cubed Triangle
        waves.add(x -> Math.pow(waves.get(19).applyAsDouble(x), 3));
        // 26 - Rect. Cubed Triangle
        waves.add(x -> Math.pow(waves.get(20).applyAsDouble(x), 3));
        // 27 - Abs. Cubed Triangle
        waves.add(x -> Math.pow(waves.get(21).applyAsDouble(x), 3));
        // 28 - Quarter Cubed Triangle
        waves.add(x -> Math.pow(waves.get(22).applyAsDouble(x), 3));
        // 29 - Squi. Cubed Triangle
        waves.add(x -> Math.pow(waves.get(23).applyAsDouble(x), 3));
        // 30 - Squi. Abs. Cubed Triangle
        waves.add(x -> Math.pow(waves.get(24).applyAsDouble(x), 3));
        // 31 - Custom
        waves.add(x -> interpolate(x * wavetable.length * mult + (phase * wavetable.length + pGetPhase() * wavetable.length)));
    }

    private double noInterpolation(double x) {
        double t = x;
        int idx = (int) Math.floor(t);
        int len = wavetable.length;
        double max = (double)Arrays.stream(wavetable).max().getAsInt() / 2;
        double s0 = (wavetable[(int) modulo(idx, len)] / max) - 1;
        return s0;
    }

    private double linear(double x) {
        double t = x;
        int idx = (int) Math.floor(t);
        int len = wavetable.length;
        double max = (double)Arrays.stream(wavetable).max().getAsInt() / 2;
        double mu = (t - idx);
        double s0 = (double)wavetable[modulo(idx, len)] / max - 1;
        double s1 = (double)wavetable[modulo(idx + 1, len)] / max - 1;
        return s0 + mu * s1 - (mu * s0);
    }

    private double cosine(double x) {
        double t = x;
        int idx = (int) Math.floor(t);
        int len = wavetable.length;
        double max = (double)Arrays.stream(wavetable).max().getAsInt() / 2;
        double mu = (t - idx);
        double muCos = (1 - Math.cos(mu * Math.PI)) / 2;
        double s0 = (double)wavetable[modulo(idx, len)] / max - 1;
        double s1 = (double)wavetable[modulo(idx + 1, len)] / max - 1;
        return s0 + muCos * s1 - (muCos * s0);
    }

    private double cubic(double x) {
        double max = (double)Arrays.stream(wavetable).max().getAsInt() / 2;
        int len = wavetable.length;
        double t = x;
        int idx = (int) Math.floor(t);
        double s0 = (double)wavetable[modulo(idx - 1, len)] / max - 1;
        double s1 = (double)wavetable[modulo(idx, len)] / max - 1;
        double s2 = (double)wavetable[modulo(idx + 1, len)] / max - 1;
        double s3 = (double)wavetable[modulo(idx + 2, len)] / max - 1;

        double mu = (t - idx);
        double mu2 = mu * mu;

        double a0 = -0.5 * s0 + 1.5 * s1 - 1.5 * s2 + 0.5 * s3;
        double a1 = s0 - 2.5 * s1 + 2 * s2 - 0.5 * s3;
        double a2 = -0.5 * s0 + 0.5 * s2;
        double a3 = s1;

        return (a0 * mu * mu2 + a1 * mu2 + a2 * mu + a3);

    }
    private double interpolate(double x) {
        switch (interpolation)
        {
            case 0:
                return noInterpolation(x);
            case 1:
                return linear(x);
            case 2:
                return cosine(x);
            case 3:
                return cubic(x);
        }
        return noInterpolation(x);
    }

    /*List<Function<Double, Double>> ops = Arrays.asList(
            x -> Math.sin((x * mult * 2.0 * Math.PI) + (phase * Math.PI * 2.0 + (pGetPhase() * Math.PI * 2.0))),
            x -> ops.get(0).apply(x) > 0 ? ops.get(0).apply(x) : 0
    );*/
    public float oscillate2(float x) {
        //System.out.println("X = " + (float) Math.sin((x * mult * 2 * Math.PI) + (phase * Math.PI * 2 + (pGetPhase() * Math.PI * 2))));
        switch (this.waveformId)
        {
            case 0: // 0 - Sine
                return getSin(x);
            case 1: // Rectified Sine
                float tmp = getSin(x);
                return tmp > 0 ? tmp : 0;
            case 2: // Absolute Sine
                return Math.abs(getSin(x));
            case 3: // Quarter Sine
                return ((x * mult + phase + (pGetPhase())) % (1.0 / 2.0)) <= (1.0 / 4.0) ? Math.abs(getSin(x)) : 0;

        }
        return (float) Math.sin((x * mult * 2 * Math.PI) + (phase * Math.PI * 2 + (pGetPhase() * Math.PI * 2)));
    }

    public float oscillate(float x) {
        return (float) waves.get(waveformId % waves.size()).applyAsDouble(x);
    }

    private float customEnv(int mac) {
        int index = (int) clamp(0.0, (double) mac, (double) customVolEnv.size()-1);
        return tl * (customVolEnv.get(index)/ 63.0f);
    }

    private float adsr(int mac) {
        float atk = adsr.getAttack();
        float dec = adsr.getDecay();
        float sus = adsr.getSustain();
        float macLen = Globals.synth.getMacLen();
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
            float tick = ( (mac - atk) / dec);
            float decVal = (sus - tl) * tick + tl;
            return decVal < sus ? sus : decVal;
        }
        System.out.println("Returning 0");
        return 1;
    }

    public float getVolume(int mac) {
        if(customVolumeEnv)
        {
            return customEnv(mac);
        }
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
