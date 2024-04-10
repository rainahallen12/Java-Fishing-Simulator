//package csc;

import java.awt.*;

   public class Fisherman {
       private int x;
       private int y;
       private Color color;
       private static final int SIZE = 150;
      public Fisherman(int x, int y, Color color) {
   	  
   	   this.x = x;
   	   this.y = y;
   	   this.color = color;
   			
      }
     
      public int getX() {
   	   return this.x;
      }
     
      public int  getY() {
   	   return this.y;
      }
     
      public Color getColor() {
   	   return this.color;
      }
     
      public void setX(int x) {
   	   this.x = x;
   	  
      }
      public void setY(int y) {
   	   this.y = y;
   	  
      }
     
}
