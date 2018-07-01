import java.awt.*;
import javax.sound.midi.SysexMessage;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;

class Wall {
    public int x, y; 
    public int Wall_Width, Wall_Height;
    public Rectangle par; 

    Wall(int x, int y, int width, int height){
        this.x = x; 
        this.y = y; 
        this.Wall_Width = width; 
        this.Wall_Height = height; 
        this.par = new Rectangle(x,y,width,height); 
    }
}