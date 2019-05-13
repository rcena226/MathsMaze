package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {
//declaring global variables and javaFx properties that will be used in the program
    int playerX = 10;
    int playerY = 10;

    int pixelSize=20;
    int speed = 20;

    int xPos,yPos;

    int userAnswer;

    int result=0;

    VBox questionScreen;
    TextField answerBox;
    Button b2;

    Circle player = new Circle(pixelSize/2,Color.DARKGRAY);

    Scene menu, question, game;

    Blocks[][] maze = new Blocks[40][40];

    Group gameHolder;

    Random rand = new Random();

    private int baseWidth = 3;

    private int baseHeight = 3;



    @Override

    public void start(Stage primaryStage) {
        //setting up main menu screen
        Label welcome = new Label("Welcome to Maths Maze");
        welcome.setTextFill(Color.web("#e5e5e5"));
        welcome.setFont(new Font("Algerian", 30));
        Button b1 = new Button("Start Game");
        b1.setPrefSize(100, 100);
        b1.setOnAction(new EventHandler<ActionEvent>() { //when button is clicked
            @Override
            public void handle(ActionEvent e) {
                primaryStage.setScene(game); //sets game scene
                generate(0, 0, maze[0].length, maze.length,maze.length,chooseOrientation(maze[0].length, maze.length)); //first recursive call
                generateQuestionsPlaceHolder(); //calls function
            }
        });
        HBox menuLayout = new HBox(100);
        menuLayout.setAlignment(Pos.CENTER);
        Image img = new Image(getClass().getResourceAsStream("MathsBlackboard.jpeg")); // background image used
        ImageView iv = new ImageView(img);
        iv.setFitHeight(1000);
        iv.setFitWidth(1000);
        StackPane stack = new StackPane();
        menuLayout.getChildren().addAll(b1, welcome);
        stack.getChildren().addAll(iv, menuLayout);
        menu = new Scene(stack);

        //setting up the game screen
        VBox gameScreen = new VBox();
        game = new Scene(gameScreen);
        gameHolder = new Group();

        //setting up question screen
        questionScreen = new VBox(10);
        question = new Scene(questionScreen,100,100);
        answerBox = new TextField(); // adding text box for answer input

        // giving text box property to only accept integers
        answerBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    answerBox.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //submit answer button used get users answer
        b2 = new Button("Submit");
        b2.setPrefSize(100, 100);
        b2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                userAnswer = Integer.parseInt(answerBox.getText()); //typed answer to be compared with actual answer
                primaryStage.setScene(game);
                if(userAnswer == result){ //comparing the answers
                    System.out.println("set correct");
                    maze[yPos][xPos].setCorrect(); //set question block to correct
                    questionScreen.getChildren().clear(); //reset of question gen
                    answerBox.clear();
                }else{
                    questionScreen.getChildren().clear(); //reset of question gen
                    answerBox.clear();
                }
            }
        });

        //setting player coordinates
        player.setCenterX(10);
        player.setCenterY(10);

    game.setOnKeyPressed(new EventHandler<KeyEvent>() { //player movement
     @Override
     public void handle(KeyEvent event) {
         //player positions at beginning
         xPos = playerX/pixelSize;
         yPos = playerY/pixelSize;
         switch(event.getCode()) {
             case LEFT:
                 //player movement to the left
                 if (playerX < pixelSize / 2 || maze[yPos][xPos - 1].type != 'w') { //make sure player does not go through walls or of screen
                     playerX = playerX - speed;
                     player.setCenterX(playerX);
                     event.consume();
                 }
                 break;
             case RIGHT:
                 //player movement to the right
                 if (playerX < pixelSize / 2 || maze[yPos][xPos + 1].type != 'w') { //make sure player does not go through walls or of screen
                     playerX = playerX + speed;
                     player.setCenterX(playerX);
                     event.consume();
                 }
                 break;
             case DOWN:
                 //player movement downwards
                 if (playerY < pixelSize / 2 || maze[yPos + 1][xPos].type != 'w') { //make sure player does not go through walls or of screen
                     playerY = playerY + speed;
                     player.setCenterY(playerY);
                     event.consume();
                 }
                 break;
             case UP:
                 //player movement upwards
                 if (playerY < pixelSize / 2 || maze[yPos - 1][xPos].type != 'w') { //make sure player does not go through walls or of screen
                     playerY = playerY - speed;
                     player.setCenterY(playerY);
                     event.consume();
                 }
                 break;
         }
            //recalculate new position
         xPos = playerX/pixelSize;
         yPos = playerY/pixelSize;

         if(maze[yPos][xPos].type=='q'){ //if player steps on question block
             generateQuestions(); //run function
             primaryStage.setScene(question); //change scene
             }
         }

 });

        defaultMaze(); //default tile maze created
        gameHolder.getChildren().add(player); //player added to screen

        gameScreen.getChildren().add(gameHolder); //game screen set up

        primaryStage.setScene(menu); //starting scene chosen

        primaryStage.show(); //program is run scene will show

    }

    private void generateQuestions() {
        //two random numbers generated
        int num1 = (int)(Math.random()*100);
        int num2 = (int)(Math.random()*100);
        Label l;

        int chooseOper = rand.nextInt(3); //random number to decide operator

        switch (chooseOper){ //switch case opened
            case 0: //chooseOper == 0 chosen operator is +
                result = num1+num2;
                 l= new Label(num1 + "+" + num2); //outputs question on question screen
                questionScreen.getChildren().addAll(l,answerBox,b2);
                break;
            case 1: //chooseOper == 1 chosen operator is -
                //if statement to switch number positions to avoid negatives
                if (num1<num2){
                    result = num2-num1;
                    l = new Label(num2 + "-" + num1);  //outputs question on question screen
                    questionScreen.getChildren().addAll(l,answerBox,b2);
                }else{
                    result = num1-num2;
                    l = new Label(num1 + "-" + num2);  //outputs question on question screen
                    questionScreen.getChildren().addAll(l,answerBox,b2);
                }
                break;
            case 2: //chooseOper == 2 chosen operator is *
                //random numbers regenerated as smaller numbers to make question simpler
                num1 =(int)(Math.random()*20);
                num2 =(int)(Math.random()*20);
                result=num1*num2;
                l = new Label(num1 + "*" + num2);
                questionScreen.getChildren().addAll(l,answerBox,b2);  //outputs question on question screen
                break;
        }

    }


    /**

     * @param args the command line arguments

     */

    public static void main(String[] args) {

        launch(args);

    }

    private void defaultMaze() { //creates default empty maze of just floors
// nested for loop as we must loop through 2d array
        for (int i = 0; i < maze.length; i++) {

            for (int j = 0; j < maze[0].length; j++) {

                maze[i][j] = new Blocks(j * pixelSize, i * pixelSize, pixelSize);

                gameHolder.getChildren().add(maze[i][j].rectangle);// maze added to game screen

            }

        }

    }

    private void generate(int x, int y, int width, int height, int door, boolean horizontal){ //generate function made with starting parameters

        if(width<=baseWidth+2 || height <=baseHeight+2){ //base case set so generation knows when to do new recursive call
            System.out.println("base case");
        }
        else{
            if(horizontal){ //horizontal call
                int wallY; // where random split will occur
                do{
                    wallY= y+rand.nextInt(height-baseHeight)+1; //random position on maze chosen for split
                }while(wallY==door);

                int doorX; //door created
                doorX= x + rand.nextInt(width-baseWidth); //random place on wall made into a door
                System.out.println("New call");
                for (int wallX = x; wallX < x+width; wallX++) {
                    maze[wallY][wallX].makeWalls(); // wall made
                    System.out.println("WallX " + wallX + " WallY " + wallY);
                }
                System.out.println("");
                maze[wallY][doorX].makeFloor(); //door made
                int newHeight = y+height-wallY;
                generate(x, y, width, wallY-y,doorX, false); //recursive call
                generate(x, wallY+1, width, newHeight-1, doorX, false);  //recursive call
            }
            else{ //vertical call
                int wallX; //where random split will occur
                do{
                    wallX= x + rand.nextInt(width-baseWidth)+1; //random position on maze chosen for split
                }while(wallX==door);

                int doorY;
                doorY= y+ rand.nextInt(height-baseHeight); //random place on wall made into a door
                System.out.println("New Call");
                for (int wallY = y; wallY < y+height; wallY++) {
                    maze[wallY][wallX].makeWalls(); //wall made
                    System.out.println("WallX " + wallX + " WallY " + wallY);
                }
                System.out.println("");
                maze[doorY][wallX].makeFloor(); //door made

                int newWidth = x+width-wallX;
                generate(x, y, wallX-x, height, doorY, true);  //recursive call
                generate(wallX+1, y, newWidth-1, height, doorY, true);  //recursive call
            }

        }
    }
    private boolean chooseOrientation(int width,int height){
        if (width < height){
            return true; //HORIZONTAL
        }
        else if (height < width){
            return false; //VERTICAL
        }
        else{
            if(rand.nextInt(2)==0){
                return true; //HORIZONTAL
            }
            else{
                return false; //VERTICAL
            }
        }
    }

    private void generateQuestionsPlaceHolder() { //chooses 20 random floor blocks to change into question blocks
        int i = 0;
        while (i < 20) {
            int j = rand.nextInt(maze.length - 10) + 5;
            int k = rand.nextInt(maze[0].length - 10) + 5;
            if (maze[j][k].type != 'w') {
                maze[j][k].setQuestion();
                i++;
            }
        }
    }
}






