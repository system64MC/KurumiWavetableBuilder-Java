package com.system64.kurumisynth.viewmodel;

import com.sun.media.sound.WaveFileWriter;
import com.system64.kurumisynth.model.Globals;
import com.system64.kurumisynth.model.Synth;
import javafx.beans.property.*;

import javax.sound.sampled.AudioFileFormat;
import java.io.*;
import java.util.ArrayList;

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
            synthModel.synthesize();
            for(int j = 0; j < Globals.waveOutput.size(); j++)
            {
                out += Globals.waveOutput.get(j) + " ";
            }
            out += ";\n";
        }
        synthModel.setMacro(tmp);
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
}
