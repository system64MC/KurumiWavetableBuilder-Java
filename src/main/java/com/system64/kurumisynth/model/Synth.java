package com.system64.kurumisynth.model;

import java.util.ArrayList;

import static com.sun.javafx.util.Utils.clamp;
import static com.system64.kurumisynth.model.Globals.*;

public class Synth {
    //private Operator[] operators = new Operator[4];
    private ArrayList<Operator> operators = new ArrayList<>();

    public byte getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(byte algorithm) {
        this.algorithm = algorithm;
    }

    public void insertOp(Operator op) {
        operators.add(op);
    }

    public Operator getOpIndex(int index) {
        return operators.get(index);
    }

    private byte algorithm = 0;

    public int getMacLen() {
        return macLen;
    }

    public void setMacLen(int macLen) {
        this.macLen = macLen;
    }

    public int getMacro() {
        return macro;
    }

    public void setMacro(int macro) {
        this.macro = macro;
    }

    private int waveLen = 32;

    public int getWaveLen() {
        return waveLen;
    }

    public void setWaveLen(int waveLen) {
        this.waveLen = waveLen;
    }

    public int getWaveHeight() {
        return waveHeight;
    }

    public void setWaveHeight(int waveHeight) {
        this.waveHeight = waveHeight;
    }

    private int waveHeight = 31;
    private int macLen = 64;
    private int macro = 0;

    public byte getSmoothWin() {
        return smoothWin;
    }

    public void setSmoothWin(byte smoothWin) {
        this.smoothWin = smoothWin;
    }

    private byte smoothWin = 0;

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

    public int getOversample() {
        return oversample;
    }

    public void setOversample(int oversample) {
        this.oversample = oversample;
    }

    private int oversample = 1;

    public void resetFeedback() {
        for (Operator op:
                operators) {
            op.setPrev(0);
        }
    }






    public void synthesize() {
        /*for (Operator op:
                operators) {
            op.setPrev(0);
        }*/
        ArrayList<Float> myArrayList = new ArrayList<Float>();
        for (int x = 0; x < waveLen * oversample; x++)
        {
            myArrayList.add(clamp(-1, fm(x) * gain, 1));
        }
        myArrayList = smooth(myArrayList);
        ArrayList<Integer> outList = new ArrayList<Integer>();
        for(int c = 0; c < myArrayList.size(); c+=oversample)
        {
            // Calculate sum before avg
            float res = 0;
            for(int i = 0; i < oversample; i++)
            {
                res += myArrayList.get(c + i);
            }
            // doing avg
            res = res / (float) oversample;
            int tmp = Math.round((res + 1) * ((float) waveHeight / 2));
            //System.out.println(tmp);
            outList.add(tmp);
        }
        waveOutput = outList;
        //System.out.println(myArrayList);
        //System.out.println(operators[3]);
    }

    public ArrayList<Integer> synthesize2() {
        ArrayList<Float> myArrayList = new ArrayList<Float>();
        for (int x = 0; x < waveLen * oversample; x++)
        {
            myArrayList.add(clamp(-1, fm(x) * gain, 1));
        }
        myArrayList = smooth(myArrayList);
        ArrayList<Integer> outList = new ArrayList<Integer>();
        for(int c = 0; c < myArrayList.size(); c+=oversample)
        {
            // Calculate sum before avg
            float res = 0;
            for(int i = 0; i < oversample; i++)
            {
                res += myArrayList.get(c + i);
            }
            // doing avg
            res = res / (float) oversample;
            int tmp = Math.round((res + 1) * ((float) waveHeight / 2));
            //System.out.println(tmp);
            outList.add(tmp);
        }
        return outList;
        //System.out.println(myArrayList);
        //System.out.println(operators[3]);
    }

    private int modulo(int a, int b) {
        return ((a % b) + b) % b;
    }
    private ArrayList<Float> smooth(ArrayList<Float> arrayToSmooth)
    {
        ArrayList<Float> out = new ArrayList<>();
        for (int i = 0; i < arrayToSmooth.size(); i++)
        {
            float smp = 0;
            for(int j = -smoothWin; j <= smoothWin; j++)
            {
                smp += arrayToSmooth.get(modulo(i+j, arrayToSmooth.size()));
            }
            float avg = smp/((smoothWin*2) + 1.0f);
            out.add(avg);
        }
        return out;
    }

    private float logicMod2(float x, float modValue, Operator op)
    {
        switch (op.getModMode())
        {
            case 0: // FM
                return op.oscillate(x + modValue + op.getFB()) * op.getVolume(macro);
            case 1: // OR
            {
                int a = (int) Math.round((modValue + 1) * 32767.5f);
                int b = (int) Math.round(((op.oscillate(x) * op.getVolume(macro)) + (1 * op.getVolume(macro))) * 32767.5f);
                return ((float) (a | b) / 32767.5f)-(1 * op.getVolume(macro));
            }
            case 2: // XOR
            {
                int a = (int) Math.round((modValue + 1) * 32767.5f);
                int b = (int) Math.round(((op.oscillate(x) * op.getVolume(macro)) + (1 * op.getVolume(macro))) * 32767.5f);
                return ((float) (a ^ b) / 32767.5f)-(1 * op.getVolume(macro));
            }
            case 3: // AND
            {
                int a = (int) Math.round((modValue + 1) * 32767.5f);
                int b = (int) Math.round(((op.oscillate(x) * op.getVolume(macro)) + (1 * op.getVolume(macro))) * 32767.5f);
                return ((float) (a & b) / 32767.5f)-(1 * op.getVolume(macro));
            }
            case 4: // NAND
            {
                int a = (int) Math.round((modValue + 1) * 32767.5f);
                int b = (int) Math.round(((op.oscillate(x) * op.getVolume(macro)) + (1 * op.getVolume(macro))) * 32767.5f);
                return ((float) ~(a & b) / 32767.5f)-(1 * op.getVolume(macro));
            }
            case 5: // ADD
            {
                return (modValue + (op.oscillate(x) * op.getVolume(macro)));
            }
            case 6: // SUB
            {
                return (op.oscillate(x)* op.getVolume(macro) - modValue);
            }
            case 7: // MUL
            {
                return (modValue * (op.oscillate(x) * op.getVolume(macro)));
            }
        }
        return op.oscillate(x + modValue + op.getFB()) * op.getVolume(macro);
    }

    private float logicMod(float x, float modValue, Operator op)
    {
        switch (op.getModMode())
        {
            case 0: // FM
                return op.oscillate(x + modValue + op.getFB()) * op.getVolume(macro);
            case 1: // OR
            {
                int a = (int) Math.round((modValue + 1) * 32767.5f);
                int b = (int) Math.round(((op.oscillate(x + op.getFB()) * op.getVolume(macro)) + (1 * op.getVolume(macro))) * 32767.5f);
                return ((float) (a | b) / 32767.5f)-(1 * op.getVolume(macro));
            }
            case 2: // XOR
            {
                int a = (int) Math.round((modValue) * 32767.5f);
                int b = (int) Math.round(((op.oscillate(x + op.getFB()) * op.getVolume(macro))) * 32767.5f);
                return ((float) (a ^ b) / 32767.5f);
            }
            case 3: // AND
            {
                int a = (int) Math.round((modValue) * 32767.5f);
                int b = (int) Math.round(((op.oscillate(x + op.getFB()) * op.getVolume(macro))) * 32767.5f);
                return ((float) (a & b) / 32767.5f);
            }
            case 4: // NAND
            {
                int a = (int) Math.round((modValue) * 32767.5f);
                int b = (int) Math.round(((op.oscillate(x + op.getFB()) * op.getVolume(macro))) * 32767.5f);
                return ((float) ~(a & b) / 32767.5f);
            }
            case 5: // ADD
            {
                return (modValue + (op.oscillate(x + op.getFB()) * op.getVolume(macro)));
            }
            case 6: // SUB
            {
                return (op.oscillate(x + op.getFB())* op.getVolume(macro) - modValue);
            }
            case 7: // MUL
            {
                return (modValue * (op.oscillate(x + op.getFB()) * op.getVolume(macro)));
            }
        }
        return op.oscillate(x + modValue + op.getFB()) * op.getVolume(macro);
    }

    boolean[][] matrix = {
            {false, false, false, false},
            {true , false, false, false},
            {false, true , false, false},
            {false, false, true , false},
    };

    boolean matrixEnabled = true;

    float[] samples = {0.0f, 0.0f, 0.0f, 0.0f};

    public float fm(float x) {
        if(operators.size() < 4)
            return 0;
        Operator op1 = operators.get(0);
        Operator op2 = operators.get(1);
        Operator op3 = operators.get(2);
        Operator op4 = operators.get(3);

        if(matrixEnabled) {
            float[] out = {0.0f, 0.0f, 0.0f, 0.0f};


            x = x / (float) (waveLen * oversample);

            for(int operator = 0; operator < 4; operator++)
            {
                float sum = 0.0f;
                for(int modulator = 0; modulator < 4; modulator++)
                {
                    sum += matrix[operator][modulator] ? samples[modulator] : 0.0f;
                }
                samples[operator] = logicMod(x, sum, operators.get(operator));
                operators.get(operator).setPrev(samples[operator]);
            }
            return samples[3];

//            samples[0] = logicMod(x,
//                    (matrix[0][0] ? samples[0] : 0) +
//                            (matrix[0][1] ? samples[1] : 0) +
//                            (matrix[0][2] ? samples[2] : 0) +
//                            (matrix[0][3] ? samples[3] : 0),
//                    op1
//            );
//            op1.setPrev(samples[0]);
//
//            samples[1] = logicMod(x,
//                    (matrix[1][0] ? samples[0] : 0) +
//                            (matrix[1][1] ? samples[1] : 0) +
//                            (matrix[1][2] ? samples[2] : 0) +
//                            (matrix[1][3] ? samples[3] : 0),
//                    op2
//            );
//            op2.setPrev(samples[1]);
//
//            samples[2] = logicMod(x,
//                    (matrix[2][0] ? samples[0] : 0) +
//                            (matrix[2][1] ? samples[1] : 0) +
//                            (matrix[2][2] ? samples[2] : 0) +
//                            (matrix[2][3] ? samples[3] : 0),
//                    op3
//            );
//            op3.setPrev(samples[2]);
//
//            samples[3] = logicMod(x,
//                    (matrix[3][0] ? samples[0] : 0) +
//                            (matrix[3][1] ? samples[1] : 0) +
//                            (matrix[3][2] ? samples[2] : 0) +
//                            (matrix[3][3] ? samples[3] : 0),
//                    op4
//            );
//            op4.setPrev(samples[3]);
//            //System.out.println(samples[1]);
//            //for(int modY = 0; modY < 4; modY++) {
//            //
//            //    float sum = 0.0f;
//            //    for(int modX = 0; modX < 4; modX++)
//            //    {
//            //        //System.out.println(matrix[modY][modX]);
//            //        if(matrix[modY][modX])
//            //        {
//            //            Operator op = operators.get(modX);
//            //            float smp = op.oscillate(x + op.getFB()) * op.getVolume(macro);
//            //            op.setPrev(smp);
//            //            sum += smp;
//            //        }
//            //    }
//            //    out[modY] = logicMod(x, sum, operators.get(modY));
//            //    operators.get(modY).setPrev(out[modY]);
//            //    System.out.println(sum);
//            //}
//
//            return samples[3];
        }



        switch (algorithm)
        {
            case 0:
            {

                /*
                ALG 0
                1 --> 2 --> 3 --> 4 --> Out
                */

                float s1 = op1.oscillate(x + op1.getFB()) * op1.getVolume(macro);
                op1.setPrev(s1);
                float s2 = logicMod(x, s1, op2);
                op2.setPrev(s2);
                float s3 = logicMod(x, s2, op3);
                op3.setPrev(s3);
                float s4 = logicMod(x, s3, op4);
                op4.setPrev(s4);
                return s4;
            }
            case 1:
            {
                /*
                ALG 1
                1-+
                  |--> 3 --> 4 --> Out
                2-+
                */

                float s1 = op1.oscillate(x + op1.getFB()) * op1.getVolume(macro);
                op1.setPrev(s1);
                float s2 = op2.oscillate(x + op2.getFB()) * op2.getVolume(macro);
                op2.setPrev(s2);
                float sum12 = s1 = s2;
                float s3 = logicMod(x, sum12, op3);
                op3.setPrev(s3);
                float s4 = logicMod(x, s3, op4);
                op4.setPrev(s4);
                return s4;
            }
            case 2:
            {
                /*
                ALG2
                      1 -+
                         |--> 4 --> Out
                2 --> 3 -+
                */

                float s1 = op1.oscillate(x + op1.getFB()) * op1.getVolume(macro);
                op1.setPrev(s1);
                float s2 = op2.oscillate(x + op2.getFB()) * op2.getVolume(macro);
                op2.setPrev(s2);
                float s3 = logicMod(x, s2, op3);
                op3.setPrev(s3);
                float sum13 = s1 + s3;
                float s4 = logicMod(x, sum13, op4);
                op4.setPrev(s4);

                return s4;
            }
            case 3:
            {
                /*
                ALG3
                1 --> 2 -+
                         |--> Out
                3 --> 4 -+
                */

                float s1 = op1.oscillate(x + op1.getFB()) * op1.getVolume(macro);
                op1.setPrev(s1);
                float s2 = logicMod(x, s1, op2);
                op2.setPrev(s2);

                float s3 = op3.oscillate(x + op3.getFB()) * op3.getVolume(macro);
                op3.setPrev(s3);
                float s4 = logicMod(x, s3, op4);
                op4.setPrev(s4);

                return (s2 + s4);
            }
            case 4:
            {
                /*
                ALG4
                  +--> 2 --+
                  |        |
                1-+--> 3 --+--> Out
                  |        |
                  +--> 4 --+
                */

                float s1 = op1.oscillate(x + op1.getFB()) * op1.getVolume(macro);
                op1.setPrev(s1);
                float s2 = logicMod(x, s1, op2);
                op2.setPrev(s2);
                float s3 = logicMod(x, s1, op3);
                op3.setPrev(s3);
                float s4 = logicMod(x, s1, op4);
                op4.setPrev(s4);
                return (s2 + s3 + s4);
            }
            case 5:
            {
                /*
                ALG5
                1 --> 2 --+
                          |
                      3 --+--> Out
                          |
                      4 --+

                */

                float s1 = op1.oscillate(x + op1.getFB()) * op1.getVolume(macro);
                op1.setPrev(s1);
                float s2 = logicMod(x, s1, op2);
                op2.setPrev(s2);

                float s3 = op3.oscillate(x + op3.getFB()) * op3.getVolume(macro);
                op3.setPrev(s3);
                float s4 = op4.oscillate(x + op4.getFB()) * op4.getVolume(macro);
                op4.setPrev(s4);
                return (s2 + s3 + s4);
            }
            case 6:
            {
                /*
                ALG6
                1 -+
                   |
                2 -+
                   |--> Out
                3 -+
                   |
                4 -+
                */

                float s1 = op1.oscillate(x + op1.getFB()) * op1.getVolume(macro);
                op1.setPrev(s1);

                float s2 = op2.oscillate(x + op2.getFB()) * op2.getVolume(macro);
                op2.setPrev(s2);

                float s3 = op3.oscillate(x + op3.getFB()) * op3.getVolume(macro);
                op3.setPrev(s3);

                float s4 = op4.oscillate(x + op4.getFB()) * op4.getVolume(macro);
                op4.setPrev(s4);
                return (s1 + s2 + s3 + s4);
            }
            case 7:
            {
                /*
                ALG7
                   +-> 2 -+
                1 -|      |-> 4 --> Out
                   +-> 3 -+
                */

                float s1 = op1.oscillate(x + op1.getFB()) * op1.getVolume(macro);
                op1.setPrev(s1);
                float s2 = logicMod(x, s1, op2);
                op2.setPrev(s2);
                float s3 = logicMod(x, s1, op3);
                op2.setPrev(s3);
                float sum23 = s3 + s2;
                float s4 = logicMod(x, sum23, op4);
                op4.setPrev(s4);
                return  s4;
            }
            case 8:
            {
                /*
                ALG8
                         +-> 3 -+
                1 --> 2 -|      |--> Out
                         +-> 4 -+
                */

                float s1 = op1.oscillate(x + op1.getFB()) * op1.getVolume(macro);
                op1.setPrev(s1);
                float s2 = logicMod(x, s1, op2);
                op2.setPrev(s2);
                float s3 = logicMod(x, s2, op3);
                op3.setPrev(s3);
                float s4 = logicMod(x, s2, op4);
                op4.setPrev(s4);
                return (s3 + s4);
            }
            case 9:
            {
                /*
                ALG 9
                1 --> 2 --> 3 -+
                               |--> Out
                            4 -+
                */

                float s1 = op1.oscillate(x + op1.getFB()) * op1.getVolume(macro);
                op1.setPrev(s1);
                float s2 = logicMod(x, s1, op2);
                op2.setPrev(s2);
                float s3 = logicMod(x, s2, op3);
                op3.setPrev(s3);

                float s4 = op4.oscillate(x + op4.getFB()) * op4.getVolume(macro);
                op4.setPrev(s4);
                return (s3 + s4);
            }
            case 10:
            {
                /*
                ALG A
                    +-> 2 -+
                1 --|      |
                    +-> 3 -+--> Out
                           |
                        4 -+
                */

                float s1 = op1.oscillate(x + op1.getFB()) * op1.getVolume(macro);
                op1.setPrev(s1);
                float s2 = logicMod(x, s1, op2);
                op2.setPrev(s2);
                float s3 = logicMod(x, s1, op3);
                op3.setPrev(s3);

                float s4 = op4.oscillate(x + op4.getFB()) * op4.getVolume(macro);
                op4.setPrev(s4);
                return (s2 + s3 + s4);
            }
            case 11:
            {
                /*
                ALG B
                1 --+
                    |
                2 --+-4-> Out
                    |
                3 --+
                */

                float s1 = op1.oscillate(x + op1.getFB()) * op1.getVolume(macro);
                op1.setPrev(s1);

                float s2 = op2.oscillate(x + op2.getFB()) * op2.getVolume(macro);
                op2.setPrev(s2);

                float s3 = op3.oscillate(x + op3.getFB()) * op3.getVolume(macro);
                op3.setPrev(s3);

                float sum123 = s1 + s2 + s3;

                float s4 = logicMod(x, sum123, op4);
                op4.setPrev(s4);
                return s4;
            }
        }
        float s1 = op1.oscillate(x + op1.getFB()) * op1.getVolume(macro);
        op1.setPrev(s1);
        float s2 = logicMod(x, s1, op2);
        op2.setPrev(s2);
        float s3 = logicMod(x, s2, op3);
        op3.setPrev(s3);
        float s4 = logicMod(x, s3, op4);
        op4.setPrev(s4);
        return s4;
        /*float s1 = op0.oscillate(x + op0.getFB()) * op0.getVolume(macro);
        float s2 = op1.oscillate(x + s1 + op1.getFB()) * op1.getVolume(macro);
        float s3 = op2.oscillate(x + s2 + op2.getFB()) * op2.getVolume(macro);
        float s4 = op3.oscillate(x + s3 + op3.getFB()) * op3.getVolume(macro);
        op3.setPrev(s4);*/
        //float s4 = op3.oscillate(x) * 1;
        //System.out.println("S4 = " + op3.getVolume(macro));
    }
}
