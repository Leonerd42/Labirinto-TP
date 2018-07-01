import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.*;

public class Screen extends JPanel {
    JPanel painel; 
    static BufferedImage fundo; 
    static BufferedImage parede[] = new BufferedImage[12]; 
    static BufferedImage centro; 
    static BufferedImage jogador[] = new BufferedImage[4]; 
    
    static int SCREEN_WIDTH = 1290; 
    static int SCREEN_HEIGHT = 689;

    boolean gameOver = false; 
    Wall[] par; 

    int posX[] = new int[4]; 
    int posY[] = new int[4]; 

	Screen(Wall[] paredes){
        try{
            String pathname = "imgs/back3.jpg";
            fundo = ImageIO.read(new File(pathname)); 
            for(int i=0; i<12; i++)
                parede[i] = ImageIO.read(new File("imgs/parede.jpg")); 
            centro = ImageIO.read(new File("imgs/stairs.png")); 
            jogador[0] = ImageIO.read(new File("imgs/pokemon.png")); 
            jogador[1] = ImageIO.read(new File("imgs/mew.png"));
            jogador[2] = ImageIO.read(new File("imgs/psiduck.png"));
            jogador[3] = ImageIO.read(new File("imgs/warturtle.png"));
        }catch (IOException e){
            System.out.println("Não leu a imagem"); 
        }
        //setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        this.par = paredes; 
        IniciaValores();
    }

    void IniciaValores(){
        posX[0] = 30; posY[0] = 30; 
        posX[1] = 1200; posY[1] = 30;
        posX[2] = 30; posY[2] = 620;
        posX[3] = 1200; posY[3] = 620;
    }

    void Atualiza(int id, int x, int y){
        posX[id] = x; 
        posY[id] = y; 
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fundo, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
        g.drawImage(centro, 600,300,60,60,this); 
        //g.drawImage(parede, 200, 100, 30, 500, this);
        for(int i=0; i<12; i++){
            g.drawImage(parede[i], par[i].x, par[i].y, par[i].Wall_Width, par[i].Wall_Height, this);
        }
        g.drawImage(jogador[0], posX[0], posY[0], 50, 50, this);
        g.drawImage(jogador[1], posX[1], posY[1], 50, 50, this);
        g.drawImage(jogador[2], posX[2], posY[2], 50, 50, this);
        g.drawImage(jogador[3], posX[3], posY[3], 50, 50, this);
        g.drawRect(600, 300, 60, 60);
        if(gameOver == true){
            g.setFont(new Font("TimesRoman", Font.PLAIN, 100));     
            g.setColor(Color.yellow);
            g.drawString("Game Over", 400, 300);
        }
        //g.drawRect (645, 350, 700, 400);  
       
    }

    void EndGame(){
        gameOver = true; 
        repaint(); 
    }
}