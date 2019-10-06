import java.lang.Number;
import java.lang.Integer;
import javafx.scene.layout.GridPane;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import java.util.Random;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;

/*
 * A Class to represent a game of match 3 or more with jewels
 * @author Eric Chang
 */
public class Jewels extends Application {
  
  private int rows = 8;
  private int columns = 10;
  
  //flag to check if the click is the first click or not
  private boolean click1 = false;
  
  //2D array to hold all the buttons
  private JewelsButton[][] jewelsButtonArray = new JewelsButton[rows][columns];
  //2D array to help mark all jewels
  private boolean[][] marks = new boolean[rows][columns];
  
  //location of the first button
  private int firstX;
  private int firstY;
  
  //location of second button
  private int secondX;
  private int secondY;
  
  //counts the number of legal moves taken
  private int numMoves = 0;
  
  @Override
  public void start(Stage primaryStage) {
    
    primaryStage.setTitle("Bejeweled");
    
    GridPane gridPane = new GridPane();
    gridPane.setMinSize(400, 200);
    gridPane.setPadding(new Insets(10,10,10,10));
    
    
    //when you get arguments from user, change the rows/columns values below
    rows = 8;
    columns = 10;
    
    //creating the grid of jewels
    for(int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        Color randomColor = randomColor();
        JewelsButton button = new JewelsButton(i, j, randomColor);
        jewelsButtonArray[i][j] = button;
        button.setPrefSize(50,50);
        button.setBackground(new Background(new BackgroundFill(randomColor, new CornerRadii(900.0), new Insets(1.0))));
        gridPane.add(button, i, j);
        
        button.setOnMousePressed(e -> {
          clicked(e);
        });
      }
    }
    
    
    
    Scene scene = new Scene(gridPane);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  /*
   * Method that returns a random color used for each gem
   * @return A random Color
   */
  private Color randomColor() {
    
    Random r = new Random();
    Color[] colorArray = new Color[4];
    colorArray[0] = Color.ORANGE;
    colorArray[1] = Color.CYAN;
    colorArray[2] = Color.RED;
    colorArray[3] = Color.YELLOW;
    
    int random = r.nextInt(colorArray.length);
    Color randomColor = colorArray[random];
    return randomColor;
  }
  
  //the action of clicking a jewel button
  public void clicked(MouseEvent x) {
    JewelsButton b = (JewelsButton)x.getSource();
    
    //first button is clicked
    if(click1 == false) {
      b.setBackground(new Background(new BackgroundFill(b.getColor().darker(), new CornerRadii(900.0),
                                                        new Insets(1.0))));
      click1 = true;
      firstX = b.getX();
      firstY = b.getY();
    }
    
    //first and second button are clicked
    else {
      
      secondX = b.getX();
      secondY = b.getY();
      Color originalColor = jewelsButtonArray[firstX][firstY].getColor();
      jewelsButtonArray[firstX][firstY].setBackground(new Background
                                                        (new BackgroundFill(originalColor,new CornerRadii(900.0), 
                                                                            new Insets(1.0))));
      int holdX = secondX;
      int holdY = secondY;
      helperSwitch(originalColor);
      int helperCheck = helperChecks(secondX,secondY);
      if (helperCheck < 2) {
        removeLine(helperCheck);
        numMoves++;
        mark();
        if (checkWin()){
          JOptionPane.showMessageDialog(null, "You've won in: " + numMoves + " moves!");
        }
      }else {
        int helperCheck2 = helperChecks(firstX,firstY);
        if (helperCheck2 < 2) {
          secondX = firstX;
          secondY = firstY;
          removeLine(helperChecks(secondX, secondY));
          numMoves++;
          mark();
          if (checkWin()){
            JOptionPane.showMessageDialog(null, "You've won in: " + numMoves + " moves!");
          }
        }
        switchBack(firstX, firstY, holdX, holdY);
      }
      click1 = false;
      
    }
  }
  
  /*
   * Helper method to help swap 2 gems that are clicked in succession
   * @param the color of the original gem
   */
  public void helperSwitch(Color originalColor) {
    jewelsButtonArray[firstX][firstY].setBackground(new Background(new BackgroundFill(jewelsButtonArray[secondX][secondY].getColor(),
                                                                                      new CornerRadii(900.0), new Insets(1.0))));
    jewelsButtonArray[firstX][firstY].setColor(jewelsButtonArray[secondX][secondY].getColor());
    
    jewelsButtonArray[secondX][secondY].setBackground(new Background(new BackgroundFill(originalColor, new CornerRadii(900.0),
                                                                                        new Insets(1.0))));
    jewelsButtonArray[secondX][secondY].setColor(originalColor);
  }
  
  /*
   * Helper method to check the left of the inputted x coordinate, the top of the y coordinate to check if jewels are in a row
   * @param x the x coordinate
   * @param y the y coordinate
   * @return a value to help determine whether there is a match or not
   */
  public int helperChecks(int x, int y) {
    int initX = x;
    int initY = y;
    JewelsButton initButton = jewelsButtonArray[x][y];
    while (x > 0 && initButton.getColor() == jewelsButtonArray[x-1][initY].getColor()) {
      x--;
    }
    while (y > 0 && initButton.getColor() == jewelsButtonArray[initX][y-1].getColor()) {
      y--;
    }
    
    int xCount = 0;
    int yCount = 0;
    initButton = jewelsButtonArray[x][initY];
    for (int i = x; i < rows; i ++) {
      JewelsButton testButton = jewelsButtonArray[i][initY];
      if (testButton.getColor() == initButton.getColor()) {
        xCount++;
        if (xCount > 2) {
          secondX = x;
          secondY = initY;
          return 0;
        }
      }else {
        break;
      }
    }
    initButton = jewelsButtonArray[initX][y];
    for (int i = y; i < columns; i ++) {
      JewelsButton testButton = jewelsButtonArray[initX][i];
      if (testButton.getColor() == initButton.getColor()) {
        yCount++;
        if (yCount > 2) {
          secondY = y;
          secondX = initX;
          return 1;
        }
      }else {
        break;
      }
    }
    return 2;
    
  }
  
  
  public void switchBack(int firstX, int firstY, int secondX, int secondY){
    Color tempColor = jewelsButtonArray[firstX][firstY].getColor();
    jewelsButtonArray[firstX][firstY].setBackground(new Background(new BackgroundFill(jewelsButtonArray[secondX][secondY].getColor(), new CornerRadii(900.0),
                                                                                      new Insets(1.0))));
    jewelsButtonArray[firstX][firstY].setColor(jewelsButtonArray[secondX][secondY].getColor());
    jewelsButtonArray[secondX][secondY].setBackground(new Background(new BackgroundFill(tempColor, new CornerRadii(900.0),
                                                                                        new Insets(1.0))));
    jewelsButtonArray[secondX][secondY].setColor(tempColor);
  }
  
  //THIS IS THE HELPER METHOD TO REMOVE THE LINE OF JEWELS 
  
  private void cascadeColors(JewelsButton bottom, JewelsButton initialButton) {
    for (int y = bottom.getY(); y >= 0; y--) {
      JewelsButton currButton = jewelsButtonArray[initialButton.getX()][y];
      Color desiredColor = Color.BLACK;
      if ((y-((bottom.getY()-initialButton.getY())+1)) >= 0) {
        desiredColor = jewelsButtonArray[initialButton.getX()][y-((bottom.getY()-initialButton.getY())+1)].getColor();
      }
      else {
        desiredColor = randomColor();
      }
      currButton.setColor(desiredColor);
      currButton.setBackground(new Background(new BackgroundFill(desiredColor, new CornerRadii(900.0),
                                                                 new Insets(1.0))));
    }
  }
  
  private JewelsButton getBottomJewel(JewelsButton[] JewelsButtons) {
    JewelsButton highestY = JewelsButtons[0];
    for (int i = 0; i < JewelsButtons.length; i++) {
      if (JewelsButtons[i].getY() > highestY.getY()) {
        highestY = JewelsButtons[i];
      }
    }
    return highestY;
  }
  
  private JewelsButton getTopJewel(JewelsButton[] JewelsButtons) {
    JewelsButton lowestY = JewelsButtons[0];
    for (int i = 0; i < JewelsButtons.length; i++) {
      if (JewelsButtons[i].getY() < lowestY.getY()) {
        lowestY = JewelsButtons[i];
      }
    }
    return lowestY;
  }
  
  public void removeLine(int checkType){   
    if (checkType == 1) {
      JewelsButton[] verticalCheck = verticalLine();
      if (verticalCheck!=null) {
        cascadeColors(getBottomJewel(verticalCheck), getTopJewel(verticalCheck));
      }
    }else if (checkType == 0) {
      JewelsButton[] horizontalCheck = horizontalLine();
      if (horizontalCheck!=null && horizontalCheck[0] != null) {
        for (int l = 0; l < horizontalCheck.length; l++) {
          cascadeColors(horizontalCheck[l], horizontalCheck[l]);
        }
      }
    }
  }
  
  private JewelsButton[] fillArray (JewelsButton[] initArray, int count) {
    JewelsButton[] tempArray = new JewelsButton[count];
    for (int i = 0; i < count; i++) {
      tempArray[i] = initArray[i];
    }
    return tempArray;
  }
  
  public JewelsButton[] verticalLine() {
    int count = 0;
    JewelsButton[] tempJewels = new JewelsButton[20];
    if (secondY < columns && secondY+1 < columns && jewelsButtonArray[secondX][secondY + 1].getColor() == jewelsButtonArray[secondX][secondY].getColor()){
      Color c1 = jewelsButtonArray[secondX][secondY].getColor();
      JewelsButton tempJewel = jewelsButtonArray[secondX][secondY];
      while (secondY < columns && c1 == jewelsButtonArray[secondX][secondY].getColor()){
        marks[secondX][secondY] = true;
        tempJewels[count] = tempJewel;
        if(secondY + 1 < columns) {
          tempJewel = jewelsButtonArray[secondX][secondY + 1];
        }
        count++;
        secondY++;
      }
      if (count >= 3){
        JewelsButton[] returnArray = fillArray(tempJewels, count);
        return returnArray;
      } else {
        resetMarks();
        return null;
      }
    } else if (secondY > 0 && jewelsButtonArray[secondX][secondY - 1].getColor() == jewelsButtonArray[secondX][secondY].getColor()){
      Color c1= jewelsButtonArray[secondX][secondY].getColor();
      JewelsButton tempJewel = jewelsButtonArray[secondX][secondY];
      while (secondY>0 && c1 == jewelsButtonArray[secondX][secondY].getColor()){
        marks[secondX][secondY] = true;
        tempJewels[count] = tempJewel;
        tempJewel = jewelsButtonArray[secondX][secondY - 1];
        count++;
        secondY--;
      }
      if (count >= 3){
        JewelsButton[] returnArray = fillArray(tempJewels, count);
        return returnArray;
      } else {
        resetMarks();
        return null;
      }
    }
    else
      return null;
  }
  
  public JewelsButton[] horizontalLine() {
    int count = 0;
    JewelsButton[] tempJewels = new JewelsButton[20];
    if (secondX < rows - 1 &&  jewelsButtonArray[secondX + 1][secondY].getColor() == jewelsButtonArray[secondX][secondY].getColor()){
      Color c1 = jewelsButtonArray[secondX][secondY].getColor();
      JewelsButton tempJewel = jewelsButtonArray[secondX][secondY];
      while (secondX < rows && c1 == jewelsButtonArray[secondX][secondY].getColor()){
        marks[secondX][secondY] = true;
        tempJewels[count] = tempJewel;
        count++;
        if (secondX + 1 < rows) {
          tempJewel = jewelsButtonArray[secondX+1][secondY];
        }
        secondX++;
      }
      if (count >= 3){
        JewelsButton[] returnArray = fillArray(tempJewels, count);
        return returnArray;
      } else {
        resetMarks();
        return null;
      }
    } else if (secondX > 0 && jewelsButtonArray[secondX - 1][secondY].getColor() == jewelsButtonArray[secondX][secondY].getColor()){
      Color c1= jewelsButtonArray[secondX][secondY].getColor();
      JewelsButton tempJewel = jewelsButtonArray[secondX][secondY];
      while (secondX>0 && c1 == jewelsButtonArray[secondX][secondY].getColor()){
        marks[secondX][secondY] = true;
        tempJewels[count] = tempJewel;
        count++;
        tempJewel = jewelsButtonArray[secondX-1][secondY];
        secondX--;
      }
      if (count >= 3){
        JewelsButton[] returnArray = fillArray(tempJewels, count);
        return returnArray;
      } else {
        resetMarks();
        return null;
      }
    }
    else
      return null;
  }
  
  public void mark() {
    for(int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if(marks[i][j] == true){
          jewelsButtonArray[i][j].setText("*");
        }
      }
    }
  }
  
  public boolean checkWin() {
    for(int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if (jewelsButtonArray[i][j].getText().equals("*")){
        }
        else{
          return false;
        }
      }
    }
    return true;
  }
  
  public void resetMarks() {
    for(int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        marks[i][j] = false;
      }
    }
  }
  
  public static void main(String args[]) {
    launch(args);
  }
}