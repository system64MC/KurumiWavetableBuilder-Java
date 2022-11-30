package com.system64.kurumisynth;

import com.system64.kurumisynth.model.Globals;

import javax.sound.sampled.*;

public class SoundPlayer implements Runnable {
    public static int sampleRate = 48000; // 48 kbit/s sampling rate
    public static int sampleSizeInBits = 16; // bits
    public boolean playing = false;
    private byte[] buffer;
    private final AudioFormat AF = new AudioFormat(sampleRate, sampleSizeInBits, 1, true, true);
    private final DataLine.Info INFO = new DataLine.Info(SourceDataLine.class, AF);
    public SourceDataLine line;
    private double RAD = 2.0 * Math.PI;
    private Thread player = null;
    public static int lineBufferSize = 4096;

    public void play() {
        player = new Thread(this,"Player");
        player.start();
    }

    public void stop() {
        player.stop();
    }

    public synchronized static int getSampleSizeInBits()
    {
        return sampleSizeInBits;
    }


    public SoundPlayer()
    {
        //setSampleSizeInBytes();
        buffer = new byte[getSampleSizeInBits()/8];

        try
        {
            line = (SourceDataLine) AudioSystem.getLine(INFO);
            line.open(AF, lineBufferSize);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        try
        {
            line.start();	//begins the stream... starts playing it
            while (true)
            {
                    for (int i = 0; i < buffer.length; i++)
                    {
                        //int wave = calculateWaveSample(i);
                        //int wave = Globals.waveOutput.get(i) * 100;
                        int ind = (int)phaseAcc(Globals.waveOutput.size());
                        int wave = Globals.waveOutput.get(ind) * (31000 / Globals.synth.getWaveHeight());
                        byte msb = (byte)(wave >>> 8);
                        byte lsb = (byte) wave;
                        if(playing)
                        {
                            buffer[0] = msb;
                            buffer[1] = lsb;
                        }
                        else
                        {
                            buffer[0] = 0;
                            buffer[1] = 0;
                        }
                        line.write( buffer, 0, buffer.length );

                }

                /*if (!playing)
                    break;*/
            }//end while
        }//end try
        catch (Exception e)
        {
            e.printStackTrace();
        }//end catch
        line.stop();
        line.flush();
    }
    private float phase = 0;

    private float phaseAcc(int waveLen)
    {
        float freqTable = 48000 / (float)waveLen;
        float playFreq = 220 / freqTable;
        phase = (phase + playFreq) % waveLen;
        return phase % waveLen;
    }
}
