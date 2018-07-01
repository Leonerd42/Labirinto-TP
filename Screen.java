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
    static BufferedImage parede; 
    static BufferedImage jogador[] = new BufferedImage[4]; 
    
    static int SCREEN_WIDTH = 1290; 
    static int SCREEN_HEIGHT = 689;

    int posX[] = new int[4]; 
    int posY[] = new int[4]; 

	Screen(){
        try{
            String pathname = "imgs/back.jpg";
            fundo = ImageIO.read(new File(pathname)); 
            parede = ImageIO.read(new File("imgs/branco.png")); 
            jogador[0] = ImageIO.read(new File("imgs/pokemon.png")); 
            jogador[1] = ImageIO.read(new File("imgs/mew.png"));
            jogador[2] = ImageIO.read(new File("imgs/psiduck.png"));
            jogador[3] = ImageIO.read(new File("imgs/warturtle.png"));
        }catch (IOException e){
            System.out.println("Não leu a imagem"); 
        }
        setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        IniciaValores();
    }

    void IniciaValores(){
        posX[0] = 20; posY[0] = 20; 
        posX[1] = 1100; posY[1] = 20;
        posX[2] = 20; posY[2] = 500;
        posX[3] = 1100; posY[3] = 500;
    }

    void Atualiza(int id, int x, int y){
        posX[id] = x; 
        posY[id] = y; 
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fundo, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
        g.drawImage(jogador[0], posX[0], posY[0], 50, 50, this);
        g.drawImage(jogador[1], posX[1], posY[1], 50, 50, this);
        g.drawImage(jogador[2], posX[2], posY[2], 50, 50, this);
        g.drawImage(jogador[3], posX[3], posY[3], 50, 50, this);
        g.drawRect(600, 300, 50, 50);
        //g.drawRect (645, 350, 700, 400);  
        //g.drawImage(parede, 800, 200, 100, 100, this);

    }

    /*public void FimMensagem(){
        super.paintComponent(g);
        g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));     
        g.setColor(Color.red);
        g.drawString("Game Over", 400, 250);

    }*/
}