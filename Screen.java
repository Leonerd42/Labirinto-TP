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
    //Imagens do jogo 
    static BufferedImage fundo; 
    static BufferedImage centro; 
    static BufferedImage parede[] = new BufferedImage[12]; 
    static BufferedImage jogador[] = new BufferedImage[4];
    static BufferedImage portas[] = new BufferedImage[4];
    static BufferedImage botao[] = new BufferedImage[2]; 
    
    //Tamanho da tela 
    static int SCREEN_WIDTH = 1311; 
    static int SCREEN_HEIGHT = 745;

    //Variaveis para desenhar os botoes pressionados
    public boolean b1 = false; 
    public boolean b2 = false; 
    public boolean b3 = false; 
    public boolean b4 = false; 
    //Portas
    public boolean p1 = false; 
    public boolean p2 = false; 
    public boolean p3 = false; 
    public boolean p4 = false; 

    boolean gameOver = false; 
    Wall[] par; 

    //Posições x,y para cada jogador 
    int posX[] = new int[4]; 
    int posY[] = new int[4]; 

	Screen(Wall[] paredes){
        try{
            String pathname = "imgs/back3.jpg";
            fundo = ImageIO.read(new File(pathname)); 
            for(int i=0; i<12; i++)
                parede[i] = ImageIO.read(new File("imgs/parede.jpg")); 
            for(int i=0; i<4; i++)
                portas[i] = ImageIO.read(new File("imgs/preto.png")); 
            botao[0] = ImageIO.read(new File("imgs/not-pressed.png")); 
            botao[1] = ImageIO.read(new File("imgs/pressed.png")); 
            centro = ImageIO.read(new File("imgs/stairs.png")); 
            jogador[0] = ImageIO.read(new File("imgs/pokemon.png")); 
            jogador[1] = ImageIO.read(new File("imgs/mew.png"));
            jogador[2] = ImageIO.read(new File("imgs/psiduck.png"));
            jogador[3] = ImageIO.read(new File("imgs/warturtle.png"));
        }catch (IOException e){
            System.out.println("Não leu a imagem"); 
        }
        this.par = paredes; 
        IniciaValores();
    }

    //Essa função coloca os personagens no meio da tela
    //Para quando começar o jogo o jogador perceber que começou
    void IniciaValores(){
        posX[0] = 500; posY[0] = 250; 
        posX[1] = 700; posY[1] = 250;
        posX[2] = 500; posY[2] = 600;
        posX[3] = 700; posY[3] = 600;
    }

    //Atualiza os valores de x,y para mostrar na tela
    void Atualiza(int id, int x, int y){
        posX[id] = x; 
        posY[id] = y; 
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fundo, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
        g.drawImage(centro, 600,300,60,60,this); 
        for(int i=0; i<12; i++){
            g.drawImage(parede[i], par[i].x, par[i].y, par[i].Wall_Width, par[i].Wall_Height, this);
        }
        if(b1 == false)
            g.drawImage(botao[0],SCREEN_WIDTH/2-100,150,50,50,this); 
        else g.drawImage(botao[1],SCREEN_WIDTH/2-100,150,50,50,this); 
        if(b2 == false)
            g.drawImage(botao[0],SCREEN_WIDTH/2,150,50,50,this); 
        else g.drawImage(botao[1],SCREEN_WIDTH/2,150,50,50,this); 
        if(b3 == false)
            g.drawImage(botao[0],SCREEN_WIDTH/2-100,SCREEN_HEIGHT-250,50,50,this); 
        else g.drawImage(botao[1],SCREEN_WIDTH/2-100,SCREEN_HEIGHT-250,50,50,this); 
        if(b4 == false)
            g.drawImage(botao[0],SCREEN_WIDTH/2,SCREEN_HEIGHT-250,50,50,this); 
        else g.drawImage(botao[1],SCREEN_WIDTH/2,SCREEN_HEIGHT-250,50,50,this); 
        if(p1 == true){
            g.drawImage(portas[0],SCREEN_WIDTH/8,220,100,30,this); 
        }
        if(p2 == true){
            g.drawImage(portas[1],SCREEN_WIDTH/8,SCREEN_HEIGHT-300,100,30,this); 
        }
        if(p3 == true){
            g.drawImage(portas[2],SCREEN_WIDTH-2*SCREEN_WIDTH/8,SCREEN_HEIGHT-300,100,30,this); 
        }
        if(p4 == true){
            g.drawImage(portas[3],SCREEN_WIDTH-2*SCREEN_WIDTH/8,220,100,30,this); 
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

    void Botoes(int i){
        switch(i){
            case 0:
                b1 = true;
                break; 
            case 1: 
                b2 = true; 
                break; 
            case 2: 
                b3 = true; 
                break; 
            case 3:
                b4 = true; 
                break; 
        }
    }

    void ZeraBotao(int i){
        switch(i){
            case 0:
                b1 = false;
                break; 
            case 1: 
                b2 = false; 
                break; 
            case 2: 
                b3 = false; 
                break; 
            case 3:
                b4 = false; 
                break; 
        }
    }

    void Portas(int i){
        switch(i){
            case 0: 
            case 2:
                p1 = false; 
                p2 = true; 
                p3 = false; 
                p4 = true; 
                break; 
            case 1:
            case 3: 
                p1 = true; 
                p2 = false; 
                p3 = true; 
                p4 = false; 
                break; 
        }
    }
}