/*
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package digitalclock;

import digitalclock.imageshader.PuzzlePiece;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tools.ProgressIndicatorBar;

/**
 * A digital clock application that demonstrates JavaFX animation, images, and
 * effects.
 *
 * @see javafx.scene.effect.Glow
 * @see javafx.scene.shape.Polygon
 * @see javafx.scene.transform.Shear
 * @resource DigitalClock-background.png
 */
public class DigitalClock extends Application {

    private static final Logger logger = Logger.getLogger(DigitalClock.class.getName());
    
    private Clock clock;
    ImageView background;
    ImageView hiddenbackground;
    ImageView[] intermissionbackgrounds = new ImageView[7];
    String hiddenbackgroundName;
    List<ImageView> puzzlePieces = new ArrayList<ImageView>();
    String puzzleBackgroundFileName;
    ImageView puzzleBackground;
    
    Integer puzzleColumns = 8;
    Integer puzzleRows = 6;
    private static String VERSION = "v8beta";
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Digital Clock " + VERSION);
        Group root = new Group();
        
        root.getStylesheets().add(getClass().getResource("progress.css").toExternalForm());
        
        Scene scene = new Scene(root, 480, 412);
        // add background image
        background = new ImageView(new Image(getClass().getResourceAsStream("DigitalClock-background.png")));
        hiddenbackground = new ImageView(new Image(getClass().getResourceAsStream("finish.jpg"), 480, 412, false, true));
        // add digital clock
        clock = new Clock(Color.ORANGERED, Color.rgb(50,50,50), this);
        clock.setLayoutX(45);
        clock.setLayoutY(186);
        clock.getTransforms().add(new Scale(0.83f, 0.83f, 0, 0));
        // add background and clock to sample
        intermissionbackgrounds[0] = new ImageView(new Image(getClass().getResourceAsStream("intermission/ballon.jpeg")));
        intermissionbackgrounds[1] = new ImageView(new Image(getClass().getResourceAsStream("intermission/banana.jpg")));
        intermissionbackgrounds[2] = new ImageView(new Image(getClass().getResourceAsStream("intermission/fish.png")));
        intermissionbackgrounds[3] = new ImageView(new Image(getClass().getResourceAsStream("intermission/hug.jpg")));
        intermissionbackgrounds[4] = new ImageView(new Image(getClass().getResourceAsStream("intermission/lover.jpg")));
        intermissionbackgrounds[5] = new ImageView(new Image(getClass().getResourceAsStream("intermission/snakes.jpeg")));
        intermissionbackgrounds[6] = new ImageView(new Image(getClass().getResourceAsStream("intermission/turtle.gif")));

        
        
        hiddenbackground = new ImageView(new Image(getClass().getResourceAsStream(hiddenbackgroundName)));
        root.getChildren().addAll(
                hiddenbackground, 
                background, 
                intermissionbackgrounds[0], 
                intermissionbackgrounds[1], 
                intermissionbackgrounds[2], 
                intermissionbackgrounds[3], 
                intermissionbackgrounds[4], 
                intermissionbackgrounds[5], 
                intermissionbackgrounds[6], 
                clock);
        
        intermissionbackgrounds[0].setVisible(false);
        intermissionbackgrounds[1].setVisible(false);
        intermissionbackgrounds[2].setVisible(false);
        intermissionbackgrounds[3].setVisible(false);
        intermissionbackgrounds[4].setVisible(false);
        intermissionbackgrounds[5].setVisible(false);
        intermissionbackgrounds[6].setVisible(false);

        puzzleBackground = new ImageView(new Image(getClass().getResourceAsStream(puzzleBackgroundFileName), 480, 412, false, true));
        root.getChildren().add(puzzleBackground);
        
        for(int i = 0; i < puzzleColumns; i++) {
            for(int j = 0; j < puzzleRows; j++) {
                PuzzlePiece puzzlePiece = new PuzzlePiece(480, 412, puzzleColumns, puzzleRows, i, j);
                Image puzzleImage = SwingFXUtils.toFXImage(puzzlePiece, null);
                final ImageView puzzleImageView = new ImageView(puzzleImage);
/*
                if ((i+j)%2 == 0) {
                    puzzleImageView.setVisible(false);
                }
  */            
                puzzlePieces.add(puzzleImageView);
                root.getChildren().add(puzzleImageView);
            }
        }
        Collections.shuffle(puzzlePieces);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Clock made of 6 of the Digit classes for hours, minutes and seconds.
     */
    public static class Clock extends Parent {
        private final Calendar calendar = Calendar.getInstance();
        private final Digit[] digits;
        private Timeline delayTimeline, secondTimeline;
        final ProgressBar pb = new ProgressBar(0);
        final Tooltip tooltip = new Tooltip();
        final ReadOnlyDoubleWrapper workDone  = new ReadOnlyDoubleWrapper();
        final ProgressIndicatorBar pib = new ProgressIndicatorBar(workDone.getReadOnlyProperty(), 100.0, "%.4f%%");
        DigitalClock dc;

        private Date startDate;
        private Date endDate;
        private int intermissionDuration;

        public Clock(Color onColor, Color offColor, DigitalClock dc) {
            //try reading end-date from file
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Properties prop = new Properties();
            try {
//                prop.load(new FileInputStream("app.ini"));
                prop.load(this.getClass().getResourceAsStream("app.ini"));
                String strEndDate = prop.getProperty("strEndDate", "2016-07-21 07:45:00");
                endDate = sdf.parse(strEndDate);
                String strStartDate = prop.getProperty("strStartDate", "2016-06-28 17:00:00");
                startDate = sdf.parse(strStartDate);
                String finishImageName = prop.getProperty("finishImageName", "finish.jpg");
                dc.hiddenbackgroundName = finishImageName;
                
                String puzzleBackgroundName = prop.getProperty("puzzleBackground", "scale.jpeg");
                dc.puzzleBackgroundFileName = puzzleBackgroundName;
                dc.puzzleColumns = Integer.parseInt(prop.getProperty("puzzleColumns", "8"));
                dc.puzzleRows = Integer.parseInt(prop.getProperty("puzzleRows", "6"));

                String strIntermissionDuration = prop.getProperty("intermissionDuration", "5");
                intermissionDuration = Integer.parseInt(strIntermissionDuration);
                System.out.println("intermissionDuration:"+intermissionDuration);
            } catch (Exception e) {
                calendar.set(2016, Calendar.MARCH, 7, 8, 0, 0);
                endDate = calendar.getTime();
                calendar.set(2016, Calendar.FEBRUARY, 26, 17, 0, 0);
                startDate = calendar.getTime();
            }
                /*
            long longThen = calendar.getTimeInMillis();
            long longNow = System.currentTimeMillis();

            if (longThen < longNow) {
                //popup ask for endDate
                final TextField txUserName = new TextField();
final PasswordField txPassword = new PasswordField();
final Action actionLogin = new DialogAction("Login", ButtonType.OK_DONE){
    @Override public void handle(ActionEvent ae) {
        Dialog dlg = (Dialog) ae.getSource();
        // real login code here
        dlg.setResult(this);
    }
};
 
Dialog dlg = new Dialog(owner, "Login Dialog");
dlg.setMasthead("Login to ControlsFX");
 
InvalidationListener changeListener = o -> validate();
txUserName.textProperty().addListener(changeListener);
txPassword.textProperty().addListener(changeListener);
 
final GridPane content = new GridPane();
content.setHgap(10);
content.setVgap(10);
 
content.add(new Label("User name"), 0, 0);
content.add(txUserName, 1, 0);
GridPane.setHgrow(txUserName, Priority.ALWAYS);
content.add(new Label("Password"), 0, 1);
content.add(txPassword, 1, 1);
GridPane.setHgrow(txPassword, Priority.ALWAYS);
 
dlg.setResizable(false);
dlg.setIconifiable(false);
dlg.setGraphic(new ImageView(HelloDialog.class.getResource(
        "login.png").toString()));
dlg.setContent(content);
dlg.getActions().addAll(actionLogin, ACTION_CANCEL);
validate();
 
Action response = dlg.show();
System.out.println("response: " + response);
                System.out.println("*** past");
                Stage dialog = new Stage();
                dialog.initStyle(StageStyle.UTILITY);
                Scene scene = new Scene(new Group(new Text(25, 25, "Hello World!")));
                dialog.setScene(scene);
                dialog.show();
            } else {
                System.out.println("*** future");
            }
                */

            //lifeline
            this.dc = dc;
            // create effect for on LEDs
            Glow onEffect = new Glow(1.7f);
            onEffect.setInput(new InnerShadow());
            // create effect for on dot LEDs
            Glow onDotEffect = new Glow(1.7f);
            onDotEffect.setInput(new InnerShadow(5,Color.BLACK));
            // create effect for off LEDs
            InnerShadow offEffect = new InnerShadow();
            // create digits
            digits = new Digit[8];
            for (int i = -1; i < 6; i++) {
                Digit digit = new Digit(onColor, offColor, onEffect, offEffect);
                digit.setLayoutX(i * 76 + ((i + 1) % 2) * 20 +60);
                digit.setLayoutY(-40);
                digits[i+1] = digit;
                getChildren().add(digit);
            }
            // create dots
            Group dots = new Group(
                    new Circle(76 + 54 + 20+60, 4, 6, onColor),
                    new Circle(76 + 54 + 17+60, 24, 6, onColor),
                    new Circle((76 * 3) + 54 + 20+60, 4, 6, onColor),
                    new Circle((76 * 3) + 54 + 17+60, 24, 6, onColor));
            dots.setEffect(onDotEffect);
            getChildren().add(dots);
/*            
            pb.setPrefWidth(470);
            pb.setPrefHeight(40);
            pb.setLayoutY(100);
            getChildren().add(pb);
            Tooltip.install(pb, tooltip);
*/
            pib.setPrefWidth(470);
            pib.setPrefHeight(40);
            pib.setLayoutY(100);
            getChildren().add(pib);
            Tooltip.install(pib, tooltip);
            
            // update digits to current time and start timer to update every second
            refreshClocks();
            play();
        }

        private void refreshClocks() {
            calendar.setTimeInMillis(System.currentTimeMillis());
            long longNow = System.currentTimeMillis();
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            int seconds = calendar.get(Calendar.SECOND);

            calendar.setTime(endDate);
            long longThen = calendar.getTimeInMillis();

            calendar.setTime(startDate);
            long longFrom = calendar.getTimeInMillis();

            long longLapse = longThen - longFrom;
                    
            long time = longThen - longNow;
            float remainder = time/(1.0f*longLapse);
            float progress = 1.0f - remainder;
            pb.setProgress(progress);
            workDone.set(progress*100.0);
            tooltip.setText(String.valueOf(100.0f * progress)+"%");
            
            if(remainder < 0) {
                dc.solvePuzzle();
                stop();
                return;
            }
            
            System.out.println(""+time);
            hours = (int) (time / 60 / 60 / 1000);
            time -= hours * 60 * 60 * 1000;
            System.out.println(""+time);
            minutes = (int) (time / 60 / 1000);
            time -= minutes * 60 * 1000;
            System.out.println(""+time);
            seconds = (int) (time / 1000);

            System.out.println(""+hours+":"+minutes+":"+seconds);

            System.out.println("ten: " + hours%10 + " min: " + minutes);
            
            if(9 == hours%10 && minutes >= (60 - intermissionDuration)) {
                dc.startIntermission(hours);
            } else {
                dc.stopIntermission();
            }
            
            if(progress < 0.5f) {
                dc.reducePuzzle(progress);
            } else {
                dc.solvePuzzle();
            }
            
            digits[0].showNumber(hours / 100);
            hours %= 100;
            digits[0+1].showNumber(hours / 10);
            digits[1+1].showNumber(hours % 10);
            digits[2+1].showNumber(minutes / 10);
            digits[3+1].showNumber(minutes % 10);
            digits[4+1].showNumber(seconds / 10);
            digits[5+1].showNumber(seconds % 10);
        }

        public void play() {
            // wait till start of next second then start a timeline to call refreshClocks() every second
            delayTimeline = new Timeline();
            delayTimeline.getKeyFrames().add(
                    new KeyFrame(new Duration(1000 - (System.currentTimeMillis() % 1000)), new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent event) {
                            if (secondTimeline != null) {
                                secondTimeline.stop();
                            }
                            secondTimeline = new Timeline();
                            secondTimeline.setCycleCount(Timeline.INDEFINITE);
                            secondTimeline.getKeyFrames().add(
                                    new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent event) {
                                            refreshClocks();
                                        }
                                    }));
                            secondTimeline.play();
                        }
                    })
            );
            delayTimeline.play();
        }

        public void stop(){
            if(delayTimeline != null) {
                delayTimeline.stop();
            }
            if (secondTimeline != null) {
                secondTimeline.stop();
            }
            dc.finish();
        }

    }

    /**
     * Simple 7 segment LED style digit. It supports the numbers 0 through 9.
     */
    public static final class Digit extends Parent {
        private static final boolean[][] DIGIT_COMBINATIONS = new boolean[][]{
                new boolean[]{true, false, true, true, true, true, true},
                new boolean[]{false, false, false, false, true, false, true},
                new boolean[]{true, true, true, false, true, true, false},
                new boolean[]{true, true, true, false, true, false, true},
                new boolean[]{false, true, false, true, true, false, true},
                new boolean[]{true, true, true, true, false, false, true},
                new boolean[]{true, true, true, true, false, true, true},
                new boolean[]{true, false, false, false, true, false, true},
                new boolean[]{true, true, true, true, true, true, true},
                new boolean[]{true, true, true, true, true, false, true}};
        private final Polygon[] polygons = new Polygon[]{
                new Polygon(2, 0, 52, 0, 42, 10, 12, 10),
                new Polygon(12, 49, 42, 49, 52, 54, 42, 59, 12f, 59f, 2f, 54f),
                new Polygon(12, 98, 42, 98, 52, 108, 2, 108),
                new Polygon(0, 2, 10, 12, 10, 47, 0, 52),
                new Polygon(44, 12, 54, 2, 54, 52, 44, 47),
                new Polygon(0, 56, 10, 61, 10, 96, 0, 106),
                new Polygon(44, 61, 54, 56, 54, 106, 44, 96)};
        private final Color onColor;
        private final Color offColor;
        private final Effect onEffect;
        private final Effect offEffect;

        public Digit(Color onColor, Color offColor, Effect onEffect, Effect offEffect) {
            this.onColor = onColor;
            this.offColor = offColor;
            this.onEffect = onEffect;
            this.offEffect = offEffect;
            getChildren().addAll(polygons);
            getTransforms().add(new Shear(-0.1,0));
            showNumber(0);
        }

        public void showNumber(Integer num) {
            if (num < 0 || num > 9) num = 0; // default to 0 for non-valid numbers
            for (int i = 0; i < 7; i++) {
                polygons[i].setFill(DIGIT_COMBINATIONS[num][i] ? onColor : offColor);
                polygons[i].setEffect(DIGIT_COMBINATIONS[num][i] ? onEffect : offEffect);
            }
        }
    }

    void resetPuzzle() {
        for(ImageView pp : puzzlePieces) {
            pp.setVisible(true);
        }
        if(null != puzzleBackground)
            puzzleBackground.setVisible(true);
    }

    void solvePuzzle() {
        for(ImageView pp : puzzlePieces) {
            pp.setVisible(false);
        }
        if(null != puzzleBackground)
            puzzleBackground.setVisible(false);
    }

    void reducePuzzle(float progress) {
        int n = puzzlePieces.size();
        if (n > 0) {
            int k = (int) (n * progress * 2.0);
            logger.info("p%:"+progress+"; n:"+n+"; k:"+k);
            if (k >= 0 && k < n) {
                for(int i = 0; i < k; i++) {
                    puzzlePieces.get(i).setVisible(false);
                }
            }
            if(k >= n) {
                solvePuzzle();
            }
        }
    }

    void startIntermission(int hours) {
        System.out.println("**** "+hours);
        if(hiddenbackground != null)hiddenbackground.setVisible(false);
        if (null != background) background.setVisible(false);
        if(clock != null) clock.setVisible(false);
        int index = hours%7;
        System.out.println("**** "+index);

        if (null != intermissionbackgrounds[6]) {
            intermissionbackgrounds[0].setVisible(false);
            intermissionbackgrounds[1].setVisible(false);
            intermissionbackgrounds[2].setVisible(false);
            intermissionbackgrounds[3].setVisible(false);
            intermissionbackgrounds[4].setVisible(false);
            intermissionbackgrounds[5].setVisible(false);
            intermissionbackgrounds[6].setVisible(false);

            intermissionbackgrounds[index].setVisible(true);
        }
    }

    void stopIntermission() {
        if(hiddenbackground != null) hiddenbackground.setVisible(true);
        if (null != background) background.setVisible(true);
        if(clock != null) clock.setVisible(true);
        if (null != intermissionbackgrounds[6]) {
            intermissionbackgrounds[0].setVisible(false);
            intermissionbackgrounds[1].setVisible(false);
            intermissionbackgrounds[2].setVisible(false);
            intermissionbackgrounds[3].setVisible(false);
            intermissionbackgrounds[4].setVisible(false);
            intermissionbackgrounds[5].setVisible(false);
            intermissionbackgrounds[6].setVisible(false);
        }
    }


    void finish() {
        background.setVisible(false);
        if(clock != null) clock.setVisible(false);
    }
    
    /**
     * The main() method is ignored in correctly deployed JavaFX 
     * application. main() serves only as fallback in case the 
     * application can not be launched through deployment artifacts,
     * e.g., in IDEs with limited FX support. NetBeans ignores main().
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
