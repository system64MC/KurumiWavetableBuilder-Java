package com.system64.kurumisynth.view;

import com.system64.kurumisynth.model.Globals;
import com.system64.kurumisynth.viewmodel.OperatorViewModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class OperatorView {
    @FXML
    private AnchorPane morphBox;
    @FXML
    private CheckBox morphCheck;
    @FXML
    private TextField morphField;
    @FXML
    private Slider morphSlider;
    @FXML
    private Label morphLabel;
    @FXML
    private CheckBox custPhaseCheck;
    @FXML
    private Canvas phaseCanvas;
    @FXML
    private TextField phaseField;
    @FXML
    private VBox envBox;
    @FXML
    private CheckBox customVolEnvCheck;
    @FXML
    private TextField volField;
    @FXML
    private Label detuneLabel;
    @FXML
    private CheckBox revPhaseCheck;
    @FXML
    private Slider detuneSlider;
    @FXML
    private Canvas adsrCanvas;
    @FXML
    private Label attackLabel;
    @FXML
    private Label decayLabel;
    @FXML
    private Label sustainLabel;
    @FXML
    private Slider attackSlider;
    @FXML
    private Slider decaySlider;
    @FXML
    private Slider sustainSlider;
    @FXML
    private CheckBox phaseCheck;
    @FXML
    private TextField waveField;
    @FXML
    private ComboBox modSelect;
    @FXML
    private Canvas waveCanvas;
    @FXML
    private Label opIdLabel;
    @FXML
    private ComboBox interpolationSelect;
    @FXML
    private ComboBox waveSelect;
    @FXML
    private Slider feedbackSlider;
    @FXML
    private Label fbLabel;
    @FXML
    private Label phaseLabel;
    @FXML
    private Label waveLabel;
    @FXML
    private Label multLabel;
    @FXML
    private Label tlLabel;
    @FXML
    private Slider tlSlider;

    @FXML
    private Slider phaseSlider;

    //@FXML
    //private Slider waveSlider;

    @FXML
    private VBox vBox;

    @FXML
    private Slider multSlider;
    private OperatorViewModel opVM;

    @FXML
    void initialize() {
        System.out.println();
        opVM = new OperatorViewModel();
        opVM.isADSR().set(true);
        opVM.standardPhaseProp().set(true);
        //waveSelect = new ComboBox<String>(FXCollections.observableList(Arrays.asList(Globals.waves)));
        waveSelect.setItems(FXCollections.observableList(Globals.wavesList));
        waveSelect.setValue("Sine");
        interpolationSelect.setItems(FXCollections.observableList(Globals.interpolationsList));
        interpolationSelect.setValue("None");
        modSelect.setItems(FXCollections.observableList(Globals.modList));
        modSelect.setValue("FM");
        opIdLabel.setText("Operator " + (Globals.opVMs.indexOf(opVM) + 1) + " :");
        //tlSlider.setValue(opVM.tlVolumeProp().getValue());
        tlLabel.setText("TL : " + opVM.tlVolumeProp().getValue());
        sustainLabel.setText("Sustain : " + opVM.susProp().getValue());
        doBindings();
        addListeners();
        drawWave();
        drawADSR();
        drawPhaseEnv();
        interpolationSelect.setDisable(true);
        Globals.operatorViews.add(this);
        //tlSlider = new Slider(10, 10, 10);
    }

    void drawWave() {
        GraphicsContext gc = waveCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, waveCanvas.getWidth(), waveCanvas.getHeight());
        Color c = Color.rgb(255, 255, 255);
        gc.setFill(c);
        for(int x = 0; x < 256; x++)
        {
            float sample = (-opVM.getOpModel().oscillate((float) x/256) + 1) * 31;
            float sample1 = (-opVM.getOpModel().oscillate((float) (x + 1)/256) + 1) * 31;
            //gc.fillRect(x, sample, 2, 2);
            gc.setStroke(c);
            gc.setLineWidth(2);
            gc.strokeLine(x, sample, x + 1, sample1);
            //gc.strokeLine(x, sample-1, x + 1, sample1);
        }
        //gc.rect(10, 10, 10, 10);
    }

    void drawADSR() {
        GraphicsContext gc = adsrCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, adsrCanvas.getWidth(), adsrCanvas.getHeight());
        Color c = Color.rgb(255, 255, 255);
        gc.setFill(c);
        gc.setStroke(c);
        gc.setLineWidth(2);
        // Attack
        gc.strokeLine(0, 64, opVM.attackProp().get(), 64 - opVM.tlVolumeProp().get() * 16);
        // Decay
        gc.strokeLine(opVM.attackProp().get(), 64 - opVM.tlVolumeProp().get() * 16, opVM.attackProp().get() + opVM.decayProp().get(), 64 - opVM.susProp().get() * 16);
        // Sustain
        gc.strokeLine(opVM.attackProp().get() + opVM.decayProp().get(), 64 - opVM.susProp().get() * 16, 512, 64 - opVM.susProp().get() * 16);
    }

    void drawCustVolEnv() {
        GraphicsContext gc = adsrCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, adsrCanvas.getWidth(), adsrCanvas.getHeight());
        Color c = Color.rgb(255, 255, 255);
        gc.setFill(c);
        for(int x = 0; x < 256; x++)
        {
            double sample = (opVM.getOpModel().getCustEnv()[Math.min(x, opVM.getOpModel().getCustEnv().length-1)] / 4.0);
            double sample1 = (opVM.getOpModel().getCustEnv()[Math.min(x + 1, opVM.getOpModel().getCustEnv().length-1)] / 4.0);
            //gc.fillRect(x, sample, 2, 2);
            gc.setStroke(c);
            gc.setLineWidth(2);
            gc.strokeLine(x,64 - sample, x + 1, 64 - sample1);
            //gc.strokeLine(x, sample-1, x + 1, sample1);
        }
    }

    void drawPhaseEnv() {
        GraphicsContext gc = phaseCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, phaseCanvas.getWidth(), phaseCanvas.getHeight());
        Color c = Color.rgb(255, 255, 255);
        gc.setFill(c);
        for(int x = 0; x < 256; x++)
        {
            double sample = (opVM.getOpModel().getPhaseEnv()[Math.min(x, opVM.getOpModel().getPhaseEnv().length-1)] / 4.0);
            double sample1 = (opVM.getOpModel().getPhaseEnv()[Math.min(x + 1, opVM.getOpModel().getPhaseEnv().length-1)] / 4.0);
            //gc.fillRect(x, sample, 2, 2);
            gc.setStroke(c);
            gc.setLineWidth(2);
            gc.strokeLine(x,64 - sample, x + 1, 64 - sample1);
            //gc.strokeLine(x, sample-1, x + 1, sample1);
        }
    }

    void drawVolEnv() {
        if(opVM.isADSR().get())
        {
            drawADSR();
            return;
        }
        drawCustVolEnv();
    }

    void doBindings() {
        tlSlider.valueProperty().bindBidirectional(opVM.tlVolumeProp());
        phaseSlider.valueProperty().bindBidirectional(opVM.phaseProp());
        //waveSlider.valueProperty().bindBidirectional(opVM.waveProp());
        multSlider.valueProperty().bindBidirectional(opVM.multProp());
        feedbackSlider.valueProperty().bindBidirectional(opVM.feedbackProp());
        waveField.textProperty().bindBidirectional(opVM.waveStrProp());
        volField.textProperty().bindBidirectional(opVM.volStrProp());
        phaseCheck.selectedProperty().bindBidirectional(opVM.phaseModProp());
        customVolEnvCheck.selectedProperty().bindBidirectional(opVM.isADSR());
        //customVolEnvCheck.selectedProperty().bindBidirectional(volField.disableProperty());
        volField.disableProperty().bindBidirectional(customVolEnvCheck.selectedProperty());
        attackSlider.valueProperty().bindBidirectional(opVM.attackProp());
        decaySlider.valueProperty().bindBidirectional(opVM.decayProp());
        sustainSlider.valueProperty().bindBidirectional(opVM.susProp());
        revPhaseCheck.selectedProperty().bindBidirectional(opVM.revPhaseProp());
        detuneSlider.valueProperty().bindBidirectional(opVM.detuneProp());
        envBox.visibleProperty().bindBidirectional(customVolEnvCheck.selectedProperty());
        phaseField.textProperty().bindBidirectional(opVM.phaseStrProp());
        custPhaseCheck.selectedProperty().bindBidirectional(opVM.standardPhaseProp());
        phaseField.disableProperty().bindBidirectional(custPhaseCheck.selectedProperty());

        morphCheck.selectedProperty().bindBidirectional(opVM.isMorphEnabledProp());
        morphField.disableProperty().bindBidirectional(morphCheck.selectedProperty());
        //morphBox.visibleProperty().bindBidirectional(morphCheck.selectedProperty());
        morphBox.disableProperty().bindBidirectional(opVM.isMorphEnabledProp());
        morphField.textProperty().bindBidirectional(opVM.morphStrProp());
        morphSlider.valueProperty().bindBidirectional(opVM.morphFramesProp());
    }

    void addListeners() {
        /*waveSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            waveSlider.setValue(newVal.intValue());
            Globals.setStringTextField();
        });*/

        multSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            multSlider.setValue(newVal.intValue());
            multLabel.setText("Mult : " + opVM.multProp().get());
            Globals.setStringTextField();
            drawWave();
        });

        tlSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            tlLabel.setText("TL : " + opVM.tlVolumeProp().get());
            Globals.setStringTextField();
            drawWave();
            drawVolEnv();
        });

        phaseSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            phaseLabel.setText("Phase : " + opVM.phaseProp().get());
            Globals.setStringTextField();
            drawWave();
        });

        feedbackSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            fbLabel.setText("Feedback : " + opVM.feedbackProp().get());
            Globals.setStringTextField();
            drawWave();
        });

        waveSelect.valueProperty().addListener((obs, oldVal, newVal) -> {
            opVM.waveProp().set(Globals.wavesList.indexOf(waveSelect.getValue()));
            Globals.setStringTextField();
            drawWave();
            interpolationSelect.setDisable(opVM.waveProp().get() != 31);
            waveField.setDisable(opVM.waveProp().get() != 31);
        });

        opVM.waveProp().addListener((obs, oldVal, newVal) -> {
            waveSelect.setValue(Globals.wavesList.get(newVal.intValue()));
            Globals.setStringTextField();
            drawWave();
        });

        interpolationSelect.valueProperty().addListener((obs, oldVal, newVal) -> {
            opVM.interpolationProp().set(Globals.interpolationsList.indexOf(interpolationSelect.getValue()));
            Globals.setStringTextField();
            drawWave();
        });

        opVM.interpolationProp().addListener((obs, oldVal, newVal) -> {
            interpolationSelect.setValue(Globals.interpolationsList.get(newVal.intValue()));
            Globals.setStringTextField();
            drawWave();
        });

        modSelect.valueProperty().addListener((obs, oldVal, newVal) -> {
            opVM.modProp().set(Globals.modList.indexOf(modSelect.getValue()));
            Globals.setStringTextField();
            drawWave();
        });

        opVM.modProp().addListener((obs, oldVal, newVal) -> {
            modSelect.setValue(Globals.modList.get(newVal.intValue()));
            Globals.setStringTextField();
            drawWave();
        });

        waveField.textProperty().addListener((obs, oldVal, newVal) -> {
            Globals.setStringTextField();
            drawWave();
        });

        volField.textProperty().addListener((obs, oldVal, newVal) -> {
            Globals.setStringTextField();
            drawWave();
        });

        phaseCheck.onActionProperty().addListener((obs, oldVal, newVal) -> {
            Globals.setStringTextField();
            drawWave();
        });

        attackSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            attackSlider.setValue(newVal.intValue());
            attackLabel.setText("Attack : " + opVM.attackProp().get());
            Globals.setStringTextField();
            drawWave();
            drawVolEnv();
        });

        decaySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            decaySlider.setValue(newVal.intValue());
            decayLabel.setText("Decay : " + opVM.decayProp().get());
            Globals.setStringTextField();
            drawWave();
            drawVolEnv();
        });

        sustainSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            sustainLabel.setText("Sustain : " + opVM.susProp().get());
            Globals.setStringTextField();
            drawWave();
            drawVolEnv();
        });

        detuneSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            detuneSlider.setValue(newVal.intValue());
            detuneLabel.setText("Detune : " + opVM.detuneProp().get());
            Globals.setStringTextField();
            drawWave();
            drawVolEnv();
        });

        customVolEnvCheck.selectedProperty().addListener((obs, oldVal, newVal) -> {
            Globals.setStringTextField();
            drawWave();
            drawVolEnv();
        });

        volField.textProperty().addListener((obs, oldVal, newVal) -> {
            Globals.setStringTextField();
            drawWave();
            drawVolEnv();
        });

        phaseField.textProperty().addListener((obs, oldVal, newVal) -> {
            Globals.setStringTextField();
            drawWave();
            drawPhaseEnv();
        });

        morphSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            morphSlider.setValue(newVal.intValue());
            morphLabel.setText("Morph time len. : " + opVM.morphFramesProp().get());
            Globals.setStringTextField();
            drawWave();
            drawPhaseEnv();
        });

        morphField.textProperty().addListener((obs, oldVal, newVal) -> {
            Globals.setStringTextField();
            drawWave();
            drawPhaseEnv();
        });

        morphCheck.selectedProperty().addListener((obs, oldVal, newVal) -> {
            Globals.setStringTextField();
            drawWave();
            drawPhaseEnv();
        });
    }

}
