package com.system64.kurumisynth.model;

import java.util.ArrayList;

import static com.system64.kurumisynth.model.Globals.*;

public class Synth {
    //private Operator[] operators = new Operator[4];
    private ArrayList<Operator> operators = new ArrayList<>();

    public byte getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(byte algorithm) {
        this.algorithm = algorithm;
        System.out.println(this.algorithm);
    }

    public void insertOp(Operator op) {
        operators.add(op);
    }

    public Operator getOpIndex(int index) {
        return operators.get(index);
    }

    private byte algorithm = 0;

    public byte getSmoothWin() {
        return smoothWin;
    }

    public void setSmoothWin(byte smoothWin) {
        this.smoothWin = smoothWin;
    }

    private byte smoothWin = 1;

    public float getGain() {
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    private float gain = 1.0f;

    public ArrayList<Integer> output = new ArrayList<Integer>();

    public Synth() {
        /*for(int i = 0; i < operators.length; i ++)
            if(i < 3)
                operators[i] = new Operator();
            else
                operators[i] = new Operator(1.0f, 0, 0, 1.0f, 0, 1, 0f);*/
    }

    public void synthesize() {
        ArrayList<Float> myArrayList = new ArrayList<Float>();
        for (int x = 0; x < waveLen; x++)
        {
            myArrayList.add(fm(x));
        }
        // Smoothing here
        ArrayList<Integer> outList = new ArrayList<Integer>();
        for(int c = 0; c < waveLen; c++)
        {
            System.out.println(myArrayList.get(c));
            int tmp = Math.round((myArrayList.get(c) + 1) * ((float) waveHeight / 2));
            //System.out.println(tmp);
            outList.add(tmp);
        }
        waveOutput = outList;
        //System.out.println(myArrayList);
        //System.out.println(operators[3]);
    }

    private void smooth(ArrayList<Float> arrayToSmooth)
    {

    }

    public float fm(float x) {
        Operator op0 = operators.get(0);
        Operator op1 = operators.get(1);
        Operator op2 = operators.get(2);
        Operator op3 = operators.get(3);

        x = x / waveLen;
        System.out.println(x);
        //System.out.println(operators.size());

        switch (algorithm)
        {
            case 0:
            {

                /*
                ALG 0
                1 --> 2 --> 3 --> 4 --> Out
                */

                float s1 = op0.oscillate(x + op0.getFeedback()) * op0.getVolume(macro);
                float s2 = op1.oscillate(x + s1 + op1.getFeedback()) * op1.getVolume(macro);
                float s3 = op2.oscillate(x + s2 + op2.getFeedback()) * op2.getVolume(macro);
                float s4 = op3.oscillate(x + s3 + op3.getFeedback()) * op3.getVolume(macro);
                //float s4 = op3.oscillate(x) * 1;
                //System.out.println("S4 = " + op3.getVolume(macro));
                return s4;
            }
        }
        float s1 = op0.oscillate(x + op0.getFeedback()) * op0.getVolume(macro);
        float s2 = op1.oscillate(x + s1 + op1.getFeedback()) * op1.getVolume(macro);
        float s3 = op2.oscillate(x + s2 + op2.getFeedback()) * op2.getVolume(macro);
        float s4 = op3.oscillate(x + s3 + op3.getFeedback()) * op3.getVolume(macro);
        //float s4 = op3.oscillate(x) * 1;
        //System.out.println("S4 = " + s4);
        return s4;
    }
}
