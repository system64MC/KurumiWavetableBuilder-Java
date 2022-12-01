package com.system64.kurumisynth.model;

import com.system64.kurumisynth.SoundPlayer;
import com.system64.kurumisynth.view.OperatorView;
import com.system64.kurumisynth.viewmodel.OperatorViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

public class Globals {


    public static Synth synth;

    public static ArrayList<Integer> waveOutput = new ArrayList<Integer>();

    public static ArrayList<OperatorViewModel> opVMs = new ArrayList<>();

    public static TextField txtField;

    public static void setStringTextField() {
        txtField.setText(Globals.getWaveOutStr());
    }
    //public static ObservableList<Integer> waveOutput2 =

    public static Canvas waveDrawCvs;

    public static String[] waves = {
            "Sine",
            "Rect. Sine",
            "Abs. Sine",
            "Quarter Sine",
            "Squished Sine",
            "Squished Abs. Sine",
            "Square",
            "Saw",
            "Rect. Saw",
            "Abs. Saw",
            "Cubed Saw",
            "Rect. Cubed Saw",
            "Abs. Cubed Saw",
            "Cubed Sine",
            "Rect. Cubed Sine",
            "Abs. Cubed Sine",
            "Quarter Cubed Sine",
            "Squished Cubed Sine",
            "Squi. Abs. Cubed Sine",
            "Triangle",
            "Rect. Triange",
            "Abs. Triangle",
            "Quarter Triangle",
            "Squished Triangle",
            "Abs. Squished Triangle",
            "Cubed Triangle",
            "Rect. Cubed Triangle",
            "Abs. Cubed Triangle",
            "Quarter Cubed Triangle",
            "Squi. Cubed Triangle",
            "Squi. Abs. Cubed Triangle",
            "Custom"
    };

    public static String[] interpolations = {
            "None",
            "Linear",
            "Cosine",
            "Cubic"
    };

    public static String[] modModes = {
            "FM",
            "OR",
            "XOR",
            "AND",
            "NAND",
            "ADD",
            "SUB",
            "MUL"
    };

    public static List<String> wavesList = Arrays.asList(waves);
    public static List<String> interpolationsList = Arrays.asList(interpolations);

    public static List<String> modList = Arrays.asList(modModes);

    public static final boolean fullWave = true;

    public static Stage stage;

    public static final double[] volROM = {0.0, 0.0078125, 0.015625, 0.0234375, 0.03125, 0.0390625, 0.046875, 0.0546875, 0.0625, 0.0703125, 0.078125, 0.0859375, 0.09375, 0.1015625, 0.109375, 0.1171875, 0.125, 0.1328125, 0.140625, 0.1484375, 0.15625, 0.1640625, 0.171875, 0.1796875, 0.1875, 0.1953125, 0.203125, 0.2109375, 0.21875, 0.2265625, 0.234375, 0.2421875, 0.25, 0.2578125, 0.265625, 0.2734375, 0.28125, 0.2890625, 0.296875, 0.3046875, 0.3125, 0.3203125, 0.328125, 0.3359375, 0.34375, 0.3515625, 0.359375, 0.3671875, 0.375, 0.3828125, 0.390625, 0.3984375, 0.40625, 0.4140625, 0.421875, 0.4296875, 0.4375, 0.4453125, 0.453125, 0.4609375, 0.46875, 0.4765625, 0.484375, 0.4921875, 0.5, 0.5078125, 0.515625, 0.5234375, 0.53125, 0.5390625, 0.546875, 0.5546875, 0.5625, 0.5703125, 0.578125, 0.5859375, 0.59375, 0.6015625, 0.609375, 0.6171875, 0.625, 0.6328125, 0.640625, 0.6484375, 0.65625, 0.6640625, 0.671875, 0.6796875, 0.6875, 0.6953125, 0.703125, 0.7109375, 0.71875, 0.7265625, 0.734375, 0.7421875, 0.75, 0.7578125, 0.765625, 0.7734375, 0.78125, 0.7890625, 0.796875, 0.8046875, 0.8125, 0.8203125, 0.828125, 0.8359375, 0.84375, 0.8515625, 0.859375, 0.8671875, 0.875, 0.8828125, 0.890625, 0.8984375, 0.90625, 0.9140625, 0.921875, 0.9296875, 0.9375, 0.9453125, 0.953125, 0.9609375, 0.96875, 0.9765625, 0.984375, 1.0};

    private static int noInterpolation(int x) {
        double t = x;
        int idx = (int) Math.floor(t);
        int len = 512;
        //double max = (double)Arrays.stream(wavetable).max().getAsInt() / 2;
        int s0 = (waveOutput.get((x % len)));
        return s0;
    }
    public static void drawWaveOut() {
        int len = waveOutput.size();
        //System.out.println(waveOutput);
        GraphicsContext gc = waveDrawCvs.getGraphicsContext2D();
        gc.clearRect(0, 0, waveDrawCvs.getWidth(), waveDrawCvs.getHeight());
        for(int i = 0; i < len; i++)
        {
            int sample = -waveOutput.get(i) * 255 / synth.getWaveHeight();
            if(sample >= -128)
            {

                Color c = Color.rgb(53, 112, 181);
                gc.setFill(c);
                if(fullWave)
                    gc.fillRect(i * (512.0 / waveOutput.size()), 128, (512.0 / waveOutput.size()), Math.abs(-sample-128));
                else
                    gc.fillRect(i * 2, 128, 2, Math.abs(-sample-128));
            }
            else
            {
                Color c = Color.rgb(70, 200, 255);
                gc.setFill(c);
                //sample = -sample;
                if(fullWave)
                    gc.fillRect(i * (512.0 / waveOutput.size()), sample+256, (512.0 / waveOutput.size()), Math.abs(-sample-128));
                else
                    gc.fillRect(i * 2, sample+256, 2, Math.abs(-sample-128));
            }
            //fillRect(i * 4, 20, 3, waveOutput.get(i))

        }
        //System.out.println(waveOutput);
        //waveDrawCvs.getGraphicsContext2D().fillRect(10, 10, 20, 20);
    }

    public static ArrayList<OperatorView> operatorViews = new ArrayList<>();

    public static String getWaveOutStr() {
        StringBuilder str = new StringBuilder();
        for (int i : waveOutput) {
            str.append(i).append(" ");
        }
        str.append(";");
        return str.toString();
    }
    public static SoundPlayer sp;
}
