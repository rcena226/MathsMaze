package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Blocks {
    char type;
    Rectangle rectangle;

    Blocks(int x, int y, int pixelSize) {
        rectangle = new Rectangle(pixelSize, pixelSize);
        rectangle.setX(x);
        rectangle.setY(y);
        makeFloor();
    }

    public void setQuestion() {
        type = 'q';
        rectangle.setFill(Color.GOLD);
    }

    public void makeWalls() {
        type = 'w';
        rectangle.setFill(Color.RED);
    }

    public void makeFloor() {
        type = 'f';
        rectangle.setFill(Color.BLACK);
    }

    public void setCorrect() {
        type = 'c';
        rectangle.setFill(Color.LIMEGREEN);
    }

}

