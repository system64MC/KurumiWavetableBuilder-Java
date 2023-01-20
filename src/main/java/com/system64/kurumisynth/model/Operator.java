package com.system64.kurumisynth.model;

import java.util.ArrayList;
import java.util.Arrays;
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

    public int[] getWavetableMorph() {
        return wavetableMorph;
    }

    public void setWavetableMorph(int[] wavetableMorph) {
        this.wavetableMorph = wavetableMorph;
    }

    private int[] wavetableMorph = {16, 20, 15, 11, 11, 24, 30, 31, 28, 20, 10, 2, 0, 3, 5, 0, 16, 31, 26, 28, 31, 29, 21, 11, 3, 0, 1, 7, 20, 20, 16, 11};

    public boolean isMorphDisabled() {
        return morphDisabled;
    }

    public void setMorphDisabled(boolean morphDisabled) {
        this.morphDisabled = morphDisabled;
    }

    private boolean morphDisabled = true;

    public void setCustEnv(int[] custEnv) {
        this.custEnv = custEnv;
    }

    public int[] getCustEnv() {
        return custEnv;
    }

    private int[] custEnv = {255};


    private ArrayList<Integer> customVolEnv = new ArrayList<Integer>();

    public boolean isUseADSR() {
        return useADSR;
    }

    public void setUseADSR(boolean useADSR) {
        this.useADSR = useADSR;
    }

    private boolean useADSR;

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
        //System.out.println("FEEDBACK");
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
    }

    private boolean phaseMod;

    public void setDetune(int detune) {
        this.detune = detune;
    }

    private  int detune = 1;

    public void setPhaseRev(boolean isRev) {
        this.phaseRev = isRev ? -1 : 1;
    }

    public boolean getPhaseRev() {
        return phaseRev < 1 ? true : false;
    }

    public int[] getPhaseEnv() {
        return phaseEnv;
    }

    public void setPhaseEnv(int[] phaseEnv) {
        this.phaseEnv = phaseEnv;
    }

    //private ArrayList<Integer> customPhaseEnv = new ArrayList<Integer>();
    private int[] phaseEnv = {0};

    private int waveformId;

    public int getInterpolation() {
        return interpolation;
    }

    public void setInterpolation(int interpolation) {
        this.interpolation = interpolation;
    }

    private int interpolation;

    public boolean isStandardPhase() {
        return standardPhase;
    }

    public void setStandardPhase(boolean standardPhase) {
        this.standardPhase = standardPhase;
    }

    private boolean standardPhase;

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
        this.standardPhase = true;
        this.detune = 1;
        this.phaseMod = false;
        this.feedback = 0;
        this.prev = 0;
        this.useADSR = true;
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
        this.standardPhase = true;
        this.detune = 1;
        this.phaseMod = false;
        this.feedback = 0;
        this.prev = 0;
        this.useADSR = true;
        this.interpolation = 0;
        Globals.synth.insertOp(this);
        initWaves();
    }

    private float pGetPhase() {
        float myPhaseMod = phaseMod ? 1.0f : 0.0f;
        float macro = Globals.synth.getMacro();
        float macLen = Globals.synth.getMacLen();
        if(!standardPhase)
            return (phaseEnv[(int)(clamp(0, macro, phaseEnv.length-1))]/255.0f) *
                    phaseRev * detune * myPhaseMod;
        return (float) ( macro/(macLen-1)) * phaseRev * detune * myPhaseMod;
    }

    public float getFB() {
        return feedback * (prev / (6 * mult));
        //return feedback * prev;
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
        waves.add(x -> getWTSample(x));
    }

    private double lerp(double x, double y, double a)
    {
        return x * (1 - a) + y * a;
    }

    public int getMorphFrames() {
        return morphFrames;
    }

    public void setMorphFrames(int morphFrames) {
        this.morphFrames = morphFrames;
    }

    private int morphFrames = 64;

    private double getWTSample(double x)
    {
        if(!morphDisabled)
        {
            double a = interpolate(x * wavetable.length * mult + (phase * wavetable.length + pGetPhase() * wavetable.length), wavetable);
            double b = interpolate(x * wavetableMorph.length * mult + (phase * wavetableMorph.length + pGetPhase() * wavetableMorph.length), wavetableMorph);
            double c = Globals.synth.getMacro() > morphFrames ? 1 : Globals.synth.getMacro() / (double)morphFrames;
            return lerp(a, b, c);
        }
        return interpolate(x * wavetable.length * mult + (phase * wavetable.length + pGetPhase() * wavetable.length), wavetable);
    }

    private double noInterpolation(double x, int[] wt) {
        double t = x;
        int idx = (int) Math.floor(t);
        int len = wt.length;
        double max = (double)Arrays.stream(wt).max().getAsInt() / 2;
        double s0 = (wt[(int) modulo(idx, len)] / max) - 1;
        return s0;
    }

    private double linear(double x, int[] wt) {
        double t = x;
        int idx = (int) Math.floor(t);
        int len = wt.length;
        double max = (double)Arrays.stream(wt).max().getAsInt() / 2;
        double mu = (t - idx);
        double s0 = (double)wt[modulo(idx, len)] / max - 1;
        double s1 = (double)wt[modulo(idx + 1, len)] / max - 1;
        return s0 + mu * s1 - (mu * s0);
    }

    private double cosine(double x, int[] wt) {
        double t = x;
        int idx = (int) Math.floor(t);
        int len = wt.length;
        double max = (double)Arrays.stream(wt).max().getAsInt() / 2;
        double mu = (t - idx);
        double muCos = (1 - Math.cos(mu * Math.PI)) / 2;
        double s0 = (double)wt[modulo(idx, len)] / max - 1;
        double s1 = (double)wt[modulo(idx + 1, len)] / max - 1;
        return s0 + muCos * s1 - (muCos * s0);
    }

    private double cubic(double x, int[] wt) {
        double max = (double)Arrays.stream(wt).max().getAsInt() / 2;
        int len = wt.length;
        double t = x;
        int idx = (int) Math.floor(t);
        double s0 = (double)wt[modulo(idx - 1, len)] / max - 1;
        double s1 = (double)wt[modulo(idx, len)] / max - 1;
        double s2 = (double)wt[modulo(idx + 1, len)] / max - 1;
        double s3 = (double)wt[modulo(idx + 2, len)] / max - 1;

        double mu = (t - idx);
        double mu2 = mu * mu;

        double a0 = -0.5 * s0 + 1.5 * s1 - 1.5 * s2 + 0.5 * s3;
        double a1 = s0 - 2.5 * s1 + 2 * s2 - 0.5 * s3;
        double a2 = -0.5 * s0 + 0.5 * s2;
        double a3 = s1;

        return (a0 * mu * mu2 + a1 * mu2 + a2 * mu + a3);

    }
    private double interpolate(double x, int[] wt) {
        switch (interpolation)
        {
            case 0:
                return noInterpolation(x, wt);
            case 1:
                return linear(x, wt);
            case 2:
                return cosine(x, wt);
            case 3:
                return cubic(x, wt);
        }
        return noInterpolation(x, wt);
    }

    /*List<Function<Double, Double>> ops = Arrays.asList(
            x -> Math.sin((x * mult * 2.0 * Math.PI) + (phase * Math.PI * 2.0 + (pGetPhase() * Math.PI * 2.0))),
            x -> ops.get(0).apply(x) > 0 ? ops.get(0).apply(x) : 0
    );*/
    public float oscillate2(float x) {
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

    private static final double[] volROM = {0.0, 0.00390625, 0.0078125, 0.01171875, 0.015625, 0.01953125, 0.0234375, 0.02734375, 0.03125, 0.03515625, 0.0390625, 0.04296875, 0.046875, 0.05078125, 0.0546875, 0.05859375, 0.0625, 0.06640625, 0.0703125, 0.07421875, 0.078125, 0.08203125, 0.0859375, 0.08984375, 0.09375, 0.09765625, 0.1015625, 0.10546875, 0.109375, 0.11328125, 0.1171875, 0.12109375, 0.125, 0.12890625, 0.1328125, 0.13671875, 0.140625, 0.14453125, 0.1484375, 0.15234375, 0.15625, 0.16015625, 0.1640625, 0.16796875, 0.171875, 0.17578125, 0.1796875, 0.18359375, 0.1875, 0.19140625, 0.1953125, 0.19921875, 0.203125, 0.20703125, 0.2109375, 0.21484375, 0.21875, 0.22265625, 0.2265625, 0.23046875, 0.234375, 0.23828125, 0.2421875, 0.24609375, 0.25, 0.25390625, 0.2578125, 0.26171875, 0.265625, 0.26953125, 0.2734375, 0.27734375, 0.28125, 0.28515625, 0.2890625, 0.29296875, 0.296875, 0.30078125, 0.3046875, 0.30859375, 0.3125, 0.31640625, 0.3203125, 0.32421875, 0.328125, 0.33203125, 0.3359375, 0.33984375, 0.34375, 0.34765625, 0.3515625, 0.35546875, 0.359375, 0.36328125, 0.3671875, 0.37109375, 0.375, 0.37890625, 0.3828125, 0.38671875, 0.390625, 0.39453125, 0.3984375, 0.40234375, 0.40625, 0.41015625, 0.4140625, 0.41796875, 0.421875, 0.42578125, 0.4296875, 0.43359375, 0.4375, 0.44140625, 0.4453125, 0.44921875, 0.453125, 0.45703125, 0.4609375, 0.46484375, 0.46875, 0.47265625, 0.4765625, 0.48046875, 0.484375, 0.48828125, 0.4921875, 0.49609375, 0.5, 0.50390625, 0.5078125, 0.51171875, 0.515625, 0.51953125, 0.5234375, 0.52734375, 0.53125, 0.53515625, 0.5390625, 0.54296875, 0.546875, 0.55078125, 0.5546875, 0.55859375, 0.5625, 0.56640625, 0.5703125, 0.57421875, 0.578125, 0.58203125, 0.5859375, 0.58984375, 0.59375, 0.59765625, 0.6015625, 0.60546875, 0.609375, 0.61328125, 0.6171875, 0.62109375, 0.625, 0.62890625, 0.6328125, 0.63671875, 0.640625, 0.64453125, 0.6484375, 0.65234375, 0.65625, 0.66015625, 0.6640625, 0.66796875, 0.671875, 0.67578125, 0.6796875, 0.68359375, 0.6875, 0.69140625, 0.6953125, 0.69921875, 0.703125, 0.70703125, 0.7109375, 0.71484375, 0.71875, 0.72265625, 0.7265625, 0.73046875, 0.734375, 0.73828125, 0.7421875, 0.74609375, 0.75, 0.75390625, 0.7578125, 0.76171875, 0.765625, 0.76953125, 0.7734375, 0.77734375, 0.78125, 0.78515625, 0.7890625, 0.79296875, 0.796875, 0.80078125, 0.8046875, 0.80859375, 0.8125, 0.81640625, 0.8203125, 0.82421875, 0.828125, 0.83203125, 0.8359375, 0.83984375, 0.84375, 0.84765625, 0.8515625, 0.85546875, 0.859375, 0.86328125, 0.8671875, 0.87109375, 0.875, 0.87890625, 0.8828125, 0.88671875, 0.890625, 0.89453125, 0.8984375, 0.90234375, 0.90625, 0.91015625, 0.9140625, 0.91796875, 0.921875, 0.92578125, 0.9296875, 0.93359375, 0.9375, 0.94140625, 0.9453125, 0.94921875, 0.953125, 0.95703125, 0.9609375, 0.96484375, 0.96875, 0.97265625, 0.9765625, 0.98046875, 0.984375, 0.98828125, 0.9921875, 1};

    private float customEnv(int mac) {

        int index = (int) clamp(0.0, (double) mac, (double) (custEnv.length-1));

        return (float) (tl * (volROM[custEnv[index] & 0b11111111]));
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
        return 1;
    }

    public float getVolume(int mac) {
        if(!useADSR)
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
