package com.system64.kurumisynth.model;

import com.system64.kurumisynth.viewmodel.OperatorViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

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

    public static List<String> wavesList = Arrays.asList(waves);
    public static List<String> interpolationsList = Arrays.asList(interpolations);
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
                gc.fillRect(i * 2, 128, 2, Math.abs(-sample-128));
            }
            else
            {
                Color c = Color.rgb(70, 200, 255);
                gc.setFill(c);
                //sample = -sample;
                gc.fillRect(i * 2, sample+256, 2, Math.abs(-sample-128));
            }
            //fillRect(i * 4, 20, 3, waveOutput.get(i))

        }
        System.out.println(waveOutput);
        //waveDrawCvs.getGraphicsContext2D().fillRect(10, 10, 20, 20);
    }

    public static String getWaveOutStr() {
        StringBuilder str = new StringBuilder();
        for (int i : waveOutput) {
            str.append(i).append(" ");
        }
        str.append(";");
        return str.toString();
    }
}
