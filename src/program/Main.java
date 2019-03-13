package program;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;

// IntelliJ IDEA 2019 project update -> SDK 1.8 Java -> code not changed
// for users of this editor -> A JavaFX application -> set the src folder as the source folder in the project structure setting

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    // all variables are listed here
    private ObservableList<Rectangle> list = FXCollections.observableArrayList();
    private Random rand = new Random();
    private Text text = new Text("Bubble or cocktail?");
    private Button b1 = new Button("Start");
    private Button b2 = new Button("Exit");
    private RadioButton rb1 = new RadioButton("Descending order");
    private RadioButton rb2 = new RadioButton("Cocktail sort");
    private Color blue = Color.rgb(33, 82, 218, 1.0);
    private Color red = Color.rgb(232, 30, 30, 1.0);
    private Color green = Color.rgb(54, 215, 51, 1.0);
    private Font smallFont = new Font("Helvetica", 20);
    private Font bigFont = new Font("Helvetica", 40);
    private Service thread = new Service() {
        @Override
        protected Task createTask() {
            if (!rb2.isSelected()) return bubbleSortTask();
            else return cocktailSortTask();
        }
    };
    private Group group = new Group();
    private FadeTransition ft = new FadeTransition(Duration.millis(800), text);
    boolean swapped = true;

    @Override
    public void start(Stage frame) {
        VBox vbox = new VBox(25);
        BorderPane pane = new BorderPane();
        HBox hbox = new HBox(20);
        HBox comboHBox = new HBox(180);
        b1.setPrefSize(140, 30);
        b2.setPrefSize(140, 30);
        b1.setFont(smallFont);
        b2.setFont(smallFont);
        text.setFont(bigFont);
        text.setFill(blue);
        list = listMaking();

        // exit
        b2.setOnAction(event -> System.exit(0));
        // start
        b1.setOnAction(event -> thread.start());

        group.getChildren().addAll(list);
        pane.setCenter(group);

        vbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(b1, b2);
        hbox.setAlignment(Pos.CENTER);
        comboHBox.setAlignment(Pos.CENTER);
        comboHBox.getChildren().addAll(rb1, rb2);
        vbox.getChildren().addAll(text, pane, comboHBox, hbox);

        Scene scene = new Scene(vbox, 800, 400);
        frame.setTitle("Sorting animations");
        frame.setScene(scene);
        frame.setResizable(true);
        frame.show();
    }

    private void swapAnimation(int one, int two) { // transitions for rectangles
        double x1 = list.get(one).getTranslateX();
        double x2 = list.get(two).getTranslateX();

        TranslateTransition translateTransitionFirst = new TranslateTransition();
        translateTransitionFirst.setNode(list.get(one));
        translateTransitionFirst.setDuration(Duration.millis(1000));
        translateTransitionFirst.setToX(x2);

        TranslateTransition translateTransitionSecond = new TranslateTransition();
        translateTransitionSecond.setNode(list.get(two));
        translateTransitionSecond.setDuration(Duration.millis(1000));
        translateTransitionSecond.setToX(x1);

        translateTransitionSecond.play();
        translateTransitionFirst.play();
    }

    private Task bubbleSortTask() { // bubble sort
        return new Task() {
            @Override
            protected Object call() throws Exception {
                b1.setDisable(true);
                rb1.setDisable(true);
                rb2.setDisable(true);
                int n = list.size();
                int k;
                for (int m = n; m >= 0; m--) {
                    for (int i = 0; i < n - 1; i++) {
                        k = i + 1;
                        bubbleAnimation(i, k);
                        if (rb1.isSelected()) {
                            if (list.get(i).getHeight() < list.get(k).getHeight()) {
                                swapAnimation(i, k);
                                manageBubbleSwap(i, k);
                            }

                        } else {
                            if (list.get(i).getHeight() > list.get(k).getHeight()) {
                                swapAnimation(i, k);
                                manageBubbleSwap(i, k);
                            }
                        }
                        list.get(i).setFill(Color.GRAY);
                        list.get(k).setFill(Color.GRAY);
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    Thread.sleep(600);
                    list.get(i).setFill(green);
                }
                ft.stop();
                text.setText("Sorted!");
                text.setFill(green);
                text.setOpacity(1.0);
                return true;
            }
        };
    }

    // bubble sort function
    private void manageBubbleSwap(int i, int k) throws Exception {
        Thread.sleep(1500);
        Rectangle temp;
        temp = list.get(i);
        list.set(i, list.get(k));
        list.set(k, temp);
    }

    private Task cocktailSortTask() { // cocktail sort
        return new Task() {
            @Override
            protected Object call() throws Exception {
                b1.setDisable(true);
                rb1.setDisable(true);
                rb2.setDisable(true);
                // boolean swapped = true;
                int i = 0;
                int j = list.size() - 1;
                while (i < j && swapped) {
                    swapped = false;
                    for (int k = i; k < j; k++) {
                        bubbleAnimation(k, k + 1);
                        if (rb1.isSelected()) {
                            if (list.get(k).getHeight() < list.get(k + 1).getHeight()) {
                                swapAnimation(k, k + 1);
                                swapped = manageFrontCoctailSwap(k);
                            }

                        } else {
                            if (list.get(k).getHeight() > list.get(k + 1).getHeight()) {
                                swapAnimation(k, k + 1);
                                swapped = manageFrontCoctailSwap(k);
                            }
                        }
                        list.get(k).setFill(Color.GRAY);
                        list.get(k + 1).setFill(Color.GRAY);
                    }
                    j--;
                    if (swapped) {
                        swapped = false;
                        for (int k = j; k > i; k--) {
                            bubbleAnimation(k, k - 1);
                            if (rb1.isSelected()) {
                                if (list.get(k).getHeight() > list.get(k - 1).getHeight()) {
                                    swapAnimation(k, k - 1);
                                    swapped = manageBackCoctailSwap(k);
                                }

                            } else {
                                if (list.get(k).getHeight() < list.get(k - 1).getHeight()) {
                                    swapAnimation(k, k - 1);
                                    swapped = manageBackCoctailSwap(k);
                                }
                            }
                            list.get(k).setFill(Color.GRAY);
                            list.get(k - 1).setFill(Color.GRAY);
                        }
                    }
                    i++;
                }
                for (int q = 0; q < list.size(); q++) {
                    Thread.sleep(600);
                    list.get(q).setFill(green);
                }
                ft.stop();
                text.setText("Sorted!");
                text.setFill(green);
                text.setOpacity(1.0);
                return true;
            }
        };
    }

    // cocktail swap function #1
    public boolean manageFrontCoctailSwap(int k) throws Exception {
        Thread.sleep(1500);
        Rectangle temp;
        temp = list.get(k);
        list.set(k, list.get(k + 1));
        list.set(k + 1, temp);
        return true;
    }

    // cocktail swap function #2
    public boolean manageBackCoctailSwap(int k) throws Exception {
        Thread.sleep(1500);
        Rectangle temp;
        temp = list.get(k);
        list.set(k, list.get(k - 1));
        list.set(k - 1, temp);
        return true;
    }

    private void bubbleAnimation(int one, int two) { // bubble movement throughout the list
        list.get(one).setFill(blue);
        list.get(two).setFill(red);
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
        text.setText("Sorting...");
    }

    private ObservableList<Rectangle> listMaking() { // making a random list
        for (int i = 1; i < 9; i++) {
            Rectangle r = new Rectangle();
            r.setWidth(40);
            r.setHeight((rand.nextInt(101) + 50));
            r.setTranslateX(60 * i);
            r.setTranslateY(200 - r.getHeight());
            r.setFill(Color.GRAY);
            list.add(r);
        }
        return list;
    }
}
