package com.system64.kurumisynth.model;

public class Adsr {
    private int attack;
    private int decay;
    private float sustain;

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDecay() {
        return decay;
    }

    public void setDecay(int decay) {
        this.decay = decay;
    }

    public float getSustain() {
        return sustain;
    }

    public void setSustain(float sustain) {
        this.sustain = sustain;
    }

    public Adsr(int attack, int decay, float sustain) {
        this.attack = attack;
        this.decay = decay;
        this.sustain = sustain;
    }
    public Adsr() {
        this.attack = 0;
        this.decay = 0;
        this.sustain = 0;
    }

    @Override
    public String toString() {
        return "[ADSR Enveloppe]" +
                "\nAttack : " + this.attack +
                "\nDecay : " + this.decay +
                "\nSustain : " + this.sustain;
    }
}
