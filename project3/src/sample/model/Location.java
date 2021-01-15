package sample.model;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Location {
    public int ID;//标号
    public Circle circle;
    public Label label;
    public boolean busStation = false;
    public int X;
    public int Y;

    public Location(int ID,String name,boolean busStation,int X,int Y,Circle circle){
        this.ID = ID;
        this.busStation = busStation;
        this.X = X;
        this.Y = Y;
        this.label.setText(name);
        this.circle = circle;
    }
    public Location(int ID,String name,int X,int Y,Circle circle){
        this.ID = ID;
        this.X = X;
        this.Y = Y;
        this.label.setText(name);
        this.circle = circle;
    }
    public Location(Circle circle,Label label){
        this.circle = circle;
        this.label = label;
    }
    public Location(String name,int X,int Y,boolean busStation){
        this.label = new Label(name);
        this.circle = new Circle(X,Y,3);
        label.setLayoutX(X);
        label.setLayoutY(Y);
        this.X = X;
        this.Y = Y;
        this.busStation = busStation;
        if(busStation){
            circle.setStroke(Color.YELLOW);
        }else {
            circle.setStroke(Color.BLUE);
        }
    }
    public Location(){

    }
}
