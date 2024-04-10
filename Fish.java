//package csc;
import java.awt.Color;
import java.util.Random;

public class Fish {
	Random rand = new Random();
    private int size;
    private String name;
    private Color color;
    private int speed;
    private int pointValue;
    private int x;  // X position
    private int y;  // Y position
    private int speedX;  // Speed in the X direction
    private int speedY;  // Speed in the Y direction

    public Fish(int size, String name, Color color, int speed, int pointValue) {
        this.size = size;
        this.name = name;
        this.color = color;
        this.speed = speed;
        this.pointValue = pointValue;
        this.x = rand.nextInt(800);
        this.y = rand.nextInt(800);
        this.speedX = speed;
        this.speedY = speed;
    }

    public int getPointValue() {
    	return pointValue;
    }
    // Getter and setter for X position
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    // Getter and setter for Y position
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    // Getter for size, name, color, and speed
    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getSpeed() {
        return speed;
    }
    
    public int getSpeedX() {
    	return speedX;
    }
    
    public int getSpeedY() {
    	return speedY;
    }
    
    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }
    
    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }
}