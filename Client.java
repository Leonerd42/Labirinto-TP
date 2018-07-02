import java.awt.*;
import javax.sound.midi.SysexMessage;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;

class Client extends JFrame implements KeyListener{
	
	static int SCREEN_WIDTH = 1311; 
    static int SCREEN_HEIGHT = 745;
    StringTokenizer st = null;
    Screen screen; 
    String rec; 
    String split[];

    //ESTADOS DE MOVIMENTAÇÃO DO JOGADOR 
    final int PARADO = 0; 
    final int ESQUERDA = 1; 
    final int DIREITA = 2; 
    final int CIMA = 3; 
    final int DESCE = 4; 
    final int ACAO = 5; 
    int estado = PARADO;
    Rectangle jogador = null; 
    public int mudanca;

    //Paredes 
    Wall par[] = new Wall[12]; 
    Wall botao[] = new Wall[4]; 

    //Parte de conexão  
    Socket socket = null; 
    PrintStream os = null; 
    Scanner is = null; 
    final int porta = 8080; 
    public int id; 
    public int aux;
    public int aid;
    public int ax; 
    public int ay; 

    void CriaParedes(){
        //Todas as paredes do jogo
        par[0] = new Wall(0,100,300,30);                    //Parede do jogador 1
        par[1] = new Wall(SCREEN_WIDTH-300,100,300,30);     //Parede do jogador 2
        par[2] = new Wall(0,SCREEN_HEIGHT-170,300,30);      //Parede do jogador 3
        par[3] = new Wall(SCREEN_WIDTH-300,SCREEN_HEIGHT-170,300,30);   //parede do jogador 4
        par[4] = new Wall(SCREEN_WIDTH/2-40,0,30,250);      //Paredes centrais verticais
        par[5] = new Wall(SCREEN_WIDTH/2-40,SCREEN_HEIGHT-300,30,300);  
        par[6] = new Wall(SCREEN_WIDTH/6,220,800,30);   //Paredes horizontais centrais
        par[7] = new Wall(SCREEN_WIDTH/6,SCREEN_HEIGHT-300,800,30);
        par[8] = new Wall(0,SCREEN_HEIGHT/2-40,200,30);    
        par[9] = new Wall(SCREEN_WIDTH-200,SCREEN_HEIGHT/2-40,200,30);
        par[10] = new Wall(SCREEN_WIDTH/2-40-SCREEN_WIDTH/8,220,30,150);
        par[11] = new Wall(SCREEN_WIDTH/2+SCREEN_WIDTH/8,330,30,140);

        //Criação dos botoes 
        botao[0] = new Wall(SCREEN_WIDTH/2-100,150,50,50);
        botao[1] = new Wall(SCREEN_WIDTH/2,150,50,50);
        botao[2] = new Wall(SCREEN_WIDTH/2-100,SCREEN_HEIGHT-250,50,50);
        botao[3] = new Wall(SCREEN_WIDTH/2,SCREEN_HEIGHT-250,50,50);

    }

    Client(){
        super("   Labirinto   "); 
        CriaParedes(); 
        String ip = "127.0.0.1";       
        screen = new Screen(par); 
        setResizable(false);        
        add(screen); 
        setSize(1311,745);
        setDefaultCloseOperation(EXIT_ON_CLOSE);        
        setVisible(true);
        
        try{   //Conexão com o servidor
            socket = new Socket(ip, porta);            
            os = new PrintStream(socket.getOutputStream()); //Saida de dados
            is = new Scanner(socket.getInputStream());  //Entrada de dados
            id = Integer.parseInt(is.nextLine(),10);
        } catch (UnknownHostException e){
            System.err.println("Servidor desconhecido"); 
        } catch (IOException ex){
            System.err.println("Não pode se conectar ao servidor!"); 
        }

        setTitle("Labirinto - Jogador "+(id+1));

        addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                estado = PARADO; 
            }

            public void keyPressed(KeyEvent e){
                switch (e.getKeyCode()){
                    case KeyEvent.VK_W:         //Cima
                    case KeyEvent.VK_UP:
                        estado = CIMA; 
                        break; 
                    case KeyEvent.VK_A:         //Esquerda
                    case KeyEvent.VK_LEFT:
                        estado = ESQUERDA; 
                        break;
                    case KeyEvent.VK_S:         //Baixo 
                    case KeyEvent.VK_DOWN:
                        estado = DESCE; 
                        break; 
                    case KeyEvent.VK_D:         //Direita
                    case KeyEvent.VK_RIGHT:
                        estado = DIREITA; 
                        break; 
                    case KeyEvent.VK_SPACE:
                        estado = ACAO; 
                        break; 
                }
            }
        });       

        Thread enviar = new Thread(){
            public void run(){
                String buf = new String();   
                                
                while(true){
                    try {
                        sleep(20); 
                    } catch (InterruptedException e) {}

                    //Colide(screen.jogador[id], screen.parede);
                    switch(estado){
                        case PARADO:    
                        //if(Colide(jogador) || estado == PARADO)
                            buf = "111"; 
                        break; 
                        case ESQUERDA:  
                        if(!Colide(jogador) || mudanca != ESQUERDA){
                            buf = "444";    
                            mudanca = ESQUERDA; 
                        }
                        else {
                            mudanca = ESQUERDA; 
                            buf = "111"; 
                        }
                        break; 
                        case DIREITA:   
                        if(!Colide(jogador) || mudanca != DIREITA){
                            buf = "666";  
                            mudanca = DIREITA; 
                        }
                        else {
                            mudanca = DIREITA; 
                            buf = "111"; 
                        }
                        break; 
                        case CIMA:     
                        if(!Colide(jogador) || mudanca != CIMA){
                            buf = "888"; 
                            mudanca = CIMA; 
                        }
                        else {
                            buf = "111"; 
                            mudanca = CIMA; 
                        }
                        break; 
                        case DESCE:  
                        if(!Colide(jogador) || mudanca != DESCE)   {
                            buf = "222"; 
                            mudanca = DESCE; 
                        }
                        else {
                            buf = "111"; 
                            mudanca = DESCE; 
                        }
                        break; 
                        case ACAO:   
                            buf = "555"; 
                        break; 
                    }
                    //if(estado != PARADO)
                        os.println(buf); 
                } 
            }
        };

        enviar.start();
        
        Thread receber = new Thread(){
            String ident, posx, posy; 
            String[] teste; 
            public void run(){
                while(true){
                    if(is.hasNextLine()){
                        ident = is.nextLine(); 
                        posx = is.nextLine(); 
                        posy = is.nextLine(); 
                        aid = Integer.parseInt(ident); 
                        ax = Integer.parseInt(posx); 
                        ay = Integer.parseInt(posy); 
                    }

                    if(aid == 999 || ax == 999 || ay == 999)
                        EndGame(); 

                    MudaBotoes(jogador);
                    AtualizaTela(aid, ax, ay);
                    screen.repaint();
                }

            }
        };
        receber.start();
    }

    void EndGame(){
        screen.EndGame();
    }

    boolean Colide(Rectangle i1){
        i1 = new Rectangle(screen.posX[id],screen.posY[id],50,50); 
        for(int i=0; i<12; i++){
            if(i1.intersects(par[i].par))
                return true;
        }
        return false; 
    }

    void MudaBotoes(Rectangle j1){/*
        Rectangle v[] = new Rectangle[4]; 
        for(int i=0; i<4; i++)
            v[i] = new Rectangle(screen.posX[i],screen.posY[i],50,50); 
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                if(v[i].intersects(botao[j].par)){
                    screen.Botoes(j);
                }//else screen.ZeraBotao(j); 
            }
        }*/
        j1 = new Rectangle(screen.posX[id],screen.posY[id],50,50); 
        if(j1.intersects(botao[id].par) && estado == ACAO){
            //alteração das paredes
            screen.Botoes(id);
            screen.Portas(id);
        }else screen.ZeraBotao(id); 
    }

    void AtualizaTela(int id, int x, int y){
        screen.posX[id] = x; 
        screen.posY[id] = y;         
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
	public void keyTyped(KeyEvent arg0) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    public static void main(String[] args) {
        new Client();
    }
}