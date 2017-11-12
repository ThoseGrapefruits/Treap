package io.tmoore.trees.renderer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import io.tmoore.trees.treap.Treap;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

public class TreeRenderer extends Application {
    private static final Random random = new Random();
    private static final int RANDOM_DATA_SIZE = 64;
    private static final Set<Integer> randomData = new HashSet<>(RANDOM_DATA_SIZE);
    private static final Treap<Integer> treap = new Treap<>();

    private static void generateRandomData() {
        while (randomData.size() < RANDOM_DATA_SIZE) {
            randomData.add(random.nextInt());
        }
        treap.addAll(randomData);
    }

    public static void main(String[] args) {
        generateRandomData();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        final int width = 600;
        final int height = 500;
        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        moveCanvas(canvas, width / 2, 0);

        final Iterator<Integer> iterator = treap.iterator();
        // Handler for Layer 2
        canvas.addEventHandler(
                MouseEvent.MOUSE_PRESSED,
                event -> {
                    iterator.next();
                    iterator.remove();

                    reset(canvas, Color.WHITE);
                    gc.setFill(Color.BLACK);
                    gc.setStroke(Color.GRAY);

                    treap.draw(canvas, width, 0, 0);
                });
    }

    private void reset(Canvas canvas, Color color) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void moveCanvas(Canvas canvas, int x, int y) {
        canvas.setTranslateX(x);
        canvas.setTranslateY(y);
    }

    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        gc.fillPolygon(new double[]{10, 40, 10, 40},
                       new double[]{210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90},
                         new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140},
                          new double[]{210, 210, 240, 240}, 4);
    }
}

