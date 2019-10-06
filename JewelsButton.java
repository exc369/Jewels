import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class JewelsButton extends Button {
  private int x;
  private int y;
  private Color color;
  
  public JewelsButton(int i, int j, Color color) {
    this.x = i;
    this.y = j;
    this.color = color;
  }
  
  public int getX() {
    return x;
  }
  
  public void setX(int x) {
    this.x = x;
  }
  
  public int getY() {
    return y;
  }
  
  public void setY(int y) {
    this.y = y;
  }
  
  public Color getColor() {
    return color;
  }
  
  public void setColor(Color color) {
    this.color = color;
  }
}
  