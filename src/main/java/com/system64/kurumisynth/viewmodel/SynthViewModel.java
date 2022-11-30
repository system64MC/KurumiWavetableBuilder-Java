package com.system64.kurumisynth.viewmodel;

import com.sun.media.sound.WaveFileWriter;
import com.system64.kurumisynth.model.Globals;
import com.system64.kurumisynth.model.Operator;
import com.system64.kurumisynth.model.Synth;
import javafx.beans.property.*;

import javax.sound.sampled.AudioFileFormat;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SynthViewModel {
    private IntegerProperty algProp = new SimpleIntegerProperty();
    private IntegerProperty smoothWinProp = new SimpleIntegerProperty();
    private IntegerProperty macroLenProp = new SimpleIntegerProperty();
    private IntegerProperty macroProp = new SimpleIntegerProperty();
    private ListProperty opList = new SimpleListProperty();
    private ListProperty waveOutProp = new SimpleListProperty();
    private FloatProperty gainProp = new SimpleFloatProperty();
    private IntegerProperty waveLenProp = new SimpleIntegerProperty();
    private IntegerProperty waveHeiProp = new SimpleIntegerProperty();

    private Synth synthModel;

    public SynthViewModel() {
        synthModel = new Synth();
        Globals.synth = synthModel;
        waveLenProp.set(synthModel.getWaveLen());
        waveHeiProp.set(synthModel.getWaveHeight());
        System.out.println("Init Synth VM");
        setListeners();
        //algProp().addListener(e -> setAlgPropAction());
    }

    private void setListeners() {
        algProp.addListener((obs, oldVal, newVal) -> {
            synthModel.setAlgorithm(newVal.byteValue());
            synthModel.synthesize();
            Globals.drawWaveOut();
        });

        waveLenProp.addListener((obs, oldVal, newVal) -> {
            synthModel.setWaveLen(newVal.intValue());
            synthModel.synthesize();
            Globals.drawWaveOut();
        });

        waveHeiProp.addListener((obs, oldVal, newVal) -> {
            synthModel.setWaveHeight(newVal.intValue());
            synthModel.synthesize();
            Globals.drawWaveOut();
        });

        gainProp.addListener((obs, oldVal, newVal) -> {
            synthModel.setGain(newVal.floatValue());
            synthModel.synthesize();
            Globals.drawWaveOut();
        });

        macroProp.addListener((obs, oldVal, newVal) -> {
            synthModel.setMacro(newVal.intValue());
            synthModel.synthesize();
            Globals.drawWaveOut();
        });

        macroLenProp.addListener((obs, oldVal, newVal) -> {
            synthModel.setMacLen(newVal.intValue());
            synthModel.synthesize();
            Globals.drawWaveOut();
        });

        smoothWinProp.addListener((obs, oldVal, newVal) -> {
            synthModel.setSmoothWin(newVal.byteValue());
            synthModel.synthesize();
            Globals.drawWaveOut();
        });
    }

    public ListProperty waveOutProp() {
        return new SimpleListProperty();
    }

    public String genMacro() {
        int tmp = synthModel.getMacro();
        String out = "";
        for(int i = 0; i < synthModel.getMacLen(); i++)
        {
            synthModel.setMacro(i);
            ArrayList<Integer> arr = synthModel.synthesize2();
            for(int j = 0; j < Globals.waveOutput.size(); j++)
            {
                out += arr.get(j) + " ";
            }
            out += ";\n";
        }
        synthModel.setMacro(tmp);
        synthModel.synthesize();
        return out;
    }

    public IntegerProperty algProp() {
        //return algProp;
        //System.out.println("EEEEE");
        return this.algProp;
    }

    public void setAlgPropAction() {
        System.out.println("EEE");
    }

    public IntegerProperty smoothWinProp() {
        //return smoothWinProp;
        return smoothWinProp;
    }

    public IntegerProperty waveLenProp() {
        return this.waveLenProp;
    }

    public IntegerProperty waveHeiProp() {
        return this.waveHeiProp;
    }
    public FloatProperty gainProp() {

        //return gainProp;
        return this.gainProp;
    }

    public IntegerProperty macroLenProp() {

        //return macroLenProp;
        return this.macroLenProp;
    }

    public IntegerProperty macroProp() {

        //return macroProp;
        return this.macroProp;
    }


    public boolean writeWav2(File file) throws IOException {
        int bufLen = Globals.waveOutput.size();
        short[] intBuffer = new short[bufLen + 23];

        intBuffer[0] = 0x4952; // "RI"
        intBuffer[1] = 0x4646; // "FF"

        intBuffer[2] = (short) ((2*bufLen + 15) & 0x0000ffff); // RIFF size
        intBuffer[3] = (short) (((2*bufLen + 15) & 0xffff0000) >> 16); // RIFF size

        intBuffer[4] = 0x4157; // "WA"
        intBuffer[5] = 0x4556; // "VE"

        intBuffer[6] = 0x6d66; // "fm"
        intBuffer[7] = 0x2074; // "t "

        intBuffer[8] = 0x0012; // fmt chunksize: 18
        intBuffer[9] = 0x0000; //

        intBuffer[10] = 0x0001; // format tag : 1
        intBuffer[11] = 0x0001; // channels: 2

        intBuffer[12] = (short) (getSampleRate() & 0x0000ffff); // sample per sec
        intBuffer[13] = (short) ((getSampleRate() & 0xffff0000) >> 16); // sample per sec

        intBuffer[14] = (short) ((2*0x0001*getSampleRate()) & 0x0000ffff); // byte per sec
        intBuffer[15] = (short) (((2*0x0001*getSampleRate()) & 0xffff0000) >> 16); // byte per sec

        intBuffer[16] = 0x0004; // block align
        intBuffer[17] = 16 & 0xFFFF; // bit per sample
        intBuffer[18] = 0x0000; // cb size
        intBuffer[19] = 0x6164; // "da"
        intBuffer[20] = 0x6174; // "ta"
        intBuffer[21] = (short) ((2*bufLen) & 0x0000ffff); // data size[byte]
        intBuffer[22] = (short) (((2*bufLen) & 0xffff0000) >> 16); // data size[byte]

        for (int i = 0; i < bufLen; i++) {
            double tmp = ((synthModel.getWaveHeight() & 0x0001) == 1) ? Globals.waveOutput.get(i) / ((synthModel.getWaveHeight() / 2.0) + 0.5) :
                    Globals.waveOutput.get(i) / ((synthModel.getWaveHeight() / 2.0));
            {
                intBuffer[i+23] = (short) Math.round((tmp-1) * ((1 << (16-1))-1));
            }
        }
        FileOutputStream fs = new FileOutputStream(file);
        byte[] out = new byte[2 * intBuffer.length];
        for(int i = 0; i < out.length; i += 2)
        {
            // FUCK ENDIANESS! I HATE IT SO MUCH!
            out[i + 1] = (byte) (intBuffer[i / 2] >> 8);
            out[i] = (byte) (intBuffer[i / 2] & 0x00FF);
        }
        fs.write(out);
        fs.close();
        return true;
    }

    public boolean writeWav(File file, boolean macro) throws IOException {
        int bufLen = Globals.waveOutput.size();
        if(macro)
            bufLen = bufLen * synthModel.getMacLen();
        ArrayList<Short> intBuffer = new ArrayList<Short>();
        intBuffer.add((short) 0x4952); // "RI"
        intBuffer.add((short) 0x4646); // "FF"
        intBuffer.add((short) ((2*bufLen + 15) & 0x0000ffff));
        intBuffer.add((short) (((2*bufLen + 15) & 0xffff0000) >> 16));
        intBuffer.add((short) 0x4157); // "WA"
        intBuffer.add((short) 0x4556); // "VE"
        intBuffer.add((short) 0x6d66); // "fm"
        intBuffer.add((short) 0x2074); // "t "
        intBuffer.add((short) 0x0012); // fmt chunksize: 18
        intBuffer.add((short) 0x0000);
        intBuffer.add((short) 0x0001); // format tag : 1
        intBuffer.add((short) 0x0001); // channels : 1
        intBuffer.add((short) (getSampleRate() & 0x0000ffff)); // sample per sec
        intBuffer.add((short) ((getSampleRate() & 0xffff0000) >> 16)); // sample per sec
        intBuffer.add((short) ((2*0x0001*getSampleRate()) & 0x0000ffff)); // Byte per sec
        intBuffer.add((short) (((2*0x0001*getSampleRate()) & 0xffff0000) >> 16)); // Byte per sec
        intBuffer.add((short) 0x0004); // Block align
        intBuffer.add((short) (16 & 0xFFFF)); // Bit per sample
        intBuffer.add((short)0x0000); // cb size
        intBuffer.add((short) 0x6164); // "da"
        intBuffer.add((short) 0x6174); // "ta"
        intBuffer.add((short) ((2*bufLen) & 0x0000ffff)); // data size[byte]
        intBuffer.add((short) (((2*bufLen) & 0xffff0000) >> 16)); // data size[byte]

        if(!macro)
        {
            for (int i = 0; i < bufLen; i++) {
                double tmp = ((synthModel.getWaveHeight() & 0x0001) == 1) ? Globals.waveOutput.get(i) / ((synthModel.getWaveHeight() / 2.0) + 0.5) :
                        Globals.waveOutput.get(i) / ((synthModel.getWaveHeight() / 2.0));
                {
                    intBuffer.add((short) Math.round((tmp-1) * ((1 << (16-1))-1)));
                }
            }
        }
        else
        {
            int tmpMac = synthModel.getMacro();
            for(int i = 0; i < synthModel.getMacLen(); i++)
            {
                synthModel.setMacro(i);
                synthModel.synthesize();
                for (int j = 0; j < Globals.waveOutput.size(); j++) {
                    double tmp = ((synthModel.getWaveHeight() & 0x0001) == 1) ? Globals.waveOutput.get(j) / ((synthModel.getWaveHeight() / 2.0) + 0.5) :
                            Globals.waveOutput.get(j) / ((synthModel.getWaveHeight() / 2.0));
                    {
                        intBuffer.add((short) Math.round((tmp-1) * ((1 << (16-1))-1)));
                    }
                }
            }
        }
        FileOutputStream fs = new FileOutputStream(file);
        byte[] out = new byte[2 * intBuffer.size()];
        for(int i = 0; i < out.length; i += 2)
        {
            // FUCK ENDIANESS! I HATE IT SO MUCH!
            out[i + 1] = (byte) (intBuffer.get(i / 2) >> 8);
            out[i] = (byte) (intBuffer.get(i / 2) & 0x00FF);
        }
        fs.write(out);
        fs.close();
        return true;
    }

    private int getSampleRate() {
        return (int) Math.floor((440 * Globals.waveOutput.size()) /2.0);
    }

    public void resetOps() {
        for(int i = 0; i < 3; i++)
        {
            OperatorViewModel op = Globals.opVMs.get(i);
            op.isADSR().set(true);
            op.standardPhaseProp().set(true);
            op.phaseProp().set(0);
            op.tlVolumeProp().set(0);
            op.revPhaseProp().set(false);
            op.phaseModProp().set(false);
            op.attackProp().set(0);
            op.decayProp().set(0);
            op.susProp().set(0);
            op.detuneProp().set(1);
            op.feedbackProp().set(0);
            op.modProp().set(0);
            op.waveProp().set(0);
            op.waveStrProp().set("16 25 30 31 30 29 26 25 25 28 31 28 18 11 10 13 17 20 22 20 15 6 0 2 6 5 3 1 0 0 1 4 ");
            op.volStrProp().set("255 ");
            op.phaseStrProp().set("0 ");
        }
        OperatorViewModel op = Globals.opVMs.get(3);
        op.isADSR().set(true);
        op.standardPhaseProp().set(true);
        op.phaseProp().set(0);
        op.tlVolumeProp().set(1);
        op.multProp().set(1);
        op.interpolationProp().set(0);
        op.revPhaseProp().set(false);
        op.phaseModProp().set(false);
        op.attackProp().set(0);
        op.decayProp().set(0);
        op.susProp().set(1);
        op.detuneProp().set(1);
        op.feedbackProp().set(0);
        op.modProp().set(0);
        op.waveProp().set(0);
        op.waveStrProp().set("16 25 30 31 30 29 26 25 25 28 31 28 18 11 10 13 17 20 22 20 15 6 0 2 6 5 3 1 0 0 1 4 ");
        op.volStrProp().set("255 ");
        op.phaseStrProp().set("0 ");
        this.algProp.set(0);
        this.gainProp.set(1);
    }

    public boolean loadFile(File file) throws IOException {
        FileInputStream fs = new FileInputStream(file);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = fs.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        int index = 0;

        // Header check
        String header = "";
        for(; index < 4; index++)
            header += (char)data[index];
        if(!header.equals("KWTB"))
            return false;

        System.out.println(index);

        // Version check
        int versionLSB = (((int) data[index]) + 256) & 0xFF;
        int versionMSB = (((int) data[index+1]) + 256) & 0xFF;
        int myVersion = (versionMSB << 8) | (versionLSB);
        if(myVersion > VERSION) // If too new...
            return false;
        index += 2;

        // Algorithm
        this.algProp.set((((int) data[index]) + 256) & 0xFF);
        index++;

        // Filter
        this.smoothWinProp.set((((int) data[index]) + 256) & 0xFF);
        index++;

        // Gain
        int gainLSB = (((int) data[index]) + 256) & 0xFF;
        int gainMSB = (((int) data[index + 1]) + 256) & 0xFF;
        int myGain = (gainMSB << 8) | (gainLSB);
        this.gainProp.set(((float)myGain / 10000.0f));
        index += 2;

        // Loading operators data
        for(int i = 0; i < 4; i++)
        {
            OperatorViewModel opVM = Globals.opVMs.get(i);

            // TL
            int tlLSB = (((int) data[index]) + 256) & 0xFF;
            int tlMSB = (((int) data[index + 1]) + 256) & 0xFF;
            int myTL = (tlMSB << 8) | (tlLSB);
            opVM.tlVolumeProp().set((float) myTL / 10000.0f);
            index += 2;

            // Phase
            int phaseLSB = (((int) data[index]) + 256) & 0xFF;
            int phaseMSB = (((int) data[index + 1]) + 256) & 0xFF;
            int myPhase = (phaseMSB << 8) | (phaseLSB);
            opVM.phaseProp().set((float) myPhase / 10000.0f);
            index += 2;

            // Feedback
            int fbLSB = (((int) data[index]) + 256) & 0xFF;
            int fbMSB = (((int) data[index + 1]) + 256) & 0xFF;
            int myFB = (fbMSB << 8) | (fbLSB);
            opVM.feedbackProp().set((float) myFB / 10000.0f);
            System.out.println("FB LOAD : " +  opVM.feedbackProp().get());
            index += 2;

            // Mult
            opVM.multProp().set((((int) data[index]) + 256) & 0xFF);
            index++;

            // Detune
            opVM.detuneProp().set((((int) data[index]) + 256) & 0xFF);
            index++;

            // is PMod enabled?
            opVM.phaseModProp().set(((((int) data[index]) + 256) & 0xFF) >= 1);
            index++;

            // Is Phase Rev enabled?
            opVM.revPhaseProp().set(((((int) data[index]) + 256) & 0xFF) >= 1);
            index++;

            // Standard ADSR?
            opVM.isADSR().set(((((int) data[index]) + 256) & 0xFF) >= 1);
            index++;

            // Standard Phasing?
            opVM.standardPhaseProp().set(((((int) data[index]) + 256) & 0xFF) >= 1);
            index++;

            // Modulation mode
            opVM.modProp().set((((int) data[index]) + 256) & 0xFF);
            index++;

            // Waveform Type
            opVM.waveProp().set((((int) data[index]) + 256) & 0xFF);
            index++;

            // Interpolation type
            opVM.interpolationProp().set((((int) data[index]) + 256) & 0xFF);
            index++;

            // Attack
            opVM.attackProp().set((((int) data[index]) + 256) & 0xFF);
            index++;

            // Decay
            opVM.decayProp().set((((int) data[index]) + 256) & 0xFF);
            index++;

            // Sustain
            int susLSB = (int) (((int) data[index]) + 256) & 0xFF;
            int susMSB = (int) (((int) data[index + 1]) + 256) & 0xFF;
            int mySus = (susMSB << 8) | (susLSB);
            opVM.susProp().set((float) mySus / 10000.0f);
            index += 2;

            // Waveform data
            int wLen = (((int) data[index]) + 256) & 0xFF;
            wLen++;
            index++;
            int[] wt = new int[wLen];
            for (int j = 0; j < wLen; j++)
                wt[j] = (((int) data[index + j]) + 256) & 0xFF;
            opVM.getOpModel().setWavetable(wt);
            opVM.waveStrProp().set(opVM.getWTStr());
            index += wLen;

            // ADSR data
            int aLen = (((int) data[index]) + 256) & 0xFF;
            aLen++;
            index++;
            int[] adsr = new int[aLen];
            for (int j = 0; j < aLen; j++)
                adsr[j] = (((int) data[index + j]) + 256) & 0xFF;
            opVM.getOpModel().setCustEnv(adsr);
            opVM.volStrProp().set(opVM.getEnvStr());
            index += aLen;

            // Phase data
            int pLen = (((int) data[index]) + 256) & 0xFF;
            pLen++;
            index++;
            int[] ph = new int[pLen];
            for (int j = 0; j < pLen; j++)
                ph[j] = (((int) data[index + j]) + 256) & 0xFF;
            opVM.getOpModel().setPhaseEnv(ph);
            opVM.phaseStrProp().set(opVM.getPhaseStr());
            index += pLen;
        }

        return true;
    }

    private static final int VERSION = 1;
    public boolean saveToFile(File file) throws IOException {
        ArrayList<Byte> out = new ArrayList<>();
        // Header
        out.add((byte) 'K');
        out.add((byte) 'W');
        out.add((byte) 'T');
        out.add((byte) 'B');

        // Version
        out.add((byte) (VERSION & 0xFF));
        out.add((byte) (VERSION >>> 8));

        // Algorithm
        out.add((byte) synthModel.getAlgorithm());

        // Filter
        out.add((byte) synthModel.getSmoothWin());

        // gain
        int gain = (int) (synthModel.getGain() * 10000); // converts float to int
        out.add((byte) (gain & 0xFF)); // LSB
        out.add((byte) (gain >>> 8)); // MSB

        // Now, the operators
        for(int i = 0; i < 4; i++)
        {
            Operator op = Globals.synth.getOpIndex(i);
            // TL
            int tl = (int) (op.getTl() * 10000); // converts float to int
            out.add((byte) (tl & 0xFF)); // LSB
            out.add((byte) (tl >>> 8)); // MSB

            // Phase
            int phase = (int) (op.getPhase() * 10000); // converts float to int
            out.add((byte) (phase & 0xFF)); // LSB
            out.add((byte) (phase >>> 8)); // MSB

            // Feedback
            //System.out.println("FEEDBACK = " + op.getFeedback());
            int fb = (int) (op.getFeedback() * 10000); // converts float to int
            out.add((byte) (fb & 0xFF)); // LSB
            out.add((byte) (fb >>> 8)); // MSB

            // Mult
            out.add((byte) op.getMult());

            // Detune
            out.add((byte) op.getDetune());

            // Is PMod enabled?
            out.add((byte) (op.isPhaseMod() ? 1 : 0));

            // Is Phase Rev enabled?
            out.add((byte) (op.getPhaseRev() ? 1 : 0));

            // Is standard ADSR used?
            out.add((byte) (op.isUseADSR() ? 1 : 0));

            // Is standard phasing used?
            out.add((byte) (op.isStandardPhase() ? 1 : 0));

            // Modulation mode
            out.add((byte) (op.getModMode()));

            // Waveform type
            out.add((byte) (op.getWaveformId()));

            // Interpolation type
            out.add((byte) op.getInterpolation());

            // Attack
            out.add((byte) op.getAdsr().getAttack());

            // Decay
            out.add((byte) op.getAdsr().getDecay());

            // Sustain
            int sus = (int) (op.getAdsr().getSustain() * 10000); // converts float to int
            out.add((byte) (sus & 0xFF)); // LSB
            out.add((byte) (sus >>> 8)); // MSB

            // Custom waveform data
            int[] waveform = op.getWavetable();
            out.add((byte) (waveform.length - 1)); // Length of waveform
            for(int j = 0; j < waveform.length; j++) // The data
            {
                out.add((byte) (waveform[j] & 0xFF));
            }

            // Custom ADSR data
            int[] adsr = op.getCustEnv();
            out.add((byte) (adsr.length - 1)); // Length of the custom envelope
            for(int j = 0; j < adsr.length; j++) // The data
            {
                out.add((byte) (adsr[j] & 0xFF));
            }

            // Custom Phase data
            int[] ph = op.getPhaseEnv();
            out.add((byte) (ph.length - 1)); // Length of the custom phase env
            for(int j = 0; j < ph.length; j++) // The data
            {
                out.add((byte) (ph[j] & 0xFF));
            }
        }

        FileOutputStream fs = new FileOutputStream(file);
        byte[] out2 = new byte[out.size()];
        for(int i = 0; i < out.size(); i++)
            out2[i] = out.get(i);
        fs.write(out2);
        fs.close();
        return true;
    }
}
