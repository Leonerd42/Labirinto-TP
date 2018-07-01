import java.awt.*;
import javax.sound.midi.SysexMessage;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

class Client extends JFrame implements KeyListener{
	
	static int SCREEN_WIDTH = 1290; 
    static int SCREEN_HEIGHT = 689;
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

   //Valores para teste; 
    int t, k;

    Client(){
        super("   Labirinto   "); 
        String ip = "127.0.0.1";       
        screen = new Screen(); 
        setResizable(false);        
        add(screen); 
        setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);        
        setVisible(true);
        
        try{   //Conexão com o servidor
            socket = new Socket(ip, porta);            
            os = new PrintStream(socket.getOutputStream()); //Saida de dados
            is = new Scanner(socket.getInputStream());  //Entrada de dados
            //impressão do funcionamento 
            id = Integer.parseInt(is.nextLine(),10);
            //System.out.println("Impressão do valor do id " + status.id); 
            //while(true) {}

        } catch (UnknownHostException e){
            System.err.println("Servidor desconhecido"); 
        } catch (IOException ex){
            System.err.println("Não pode se conectar ao servidor!"); 
        }
        addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                estado = PARADO; 
                //System.out.println("Key released"); 
            }

            public void keyPressed(KeyEvent e){
                switch (e.getKeyCode()){
                    case KeyEvent.VK_W:         //Cima
                    case KeyEvent.VK_UP:
                        estado = CIMA; 
                        //System.out.println("Subindo"); 
                        break; 
                    case KeyEvent.VK_A:         //Esquerda
                    case KeyEvent.VK_LEFT:
                        estado = ESQUERDA; 
                        //System.out.println("Esquerda"); 
                        break;
                    case KeyEvent.VK_S:         //Baixo 
                    case KeyEvent.VK_DOWN:
                        estado = DESCE; 
                        //System.out.println("Descendo"); 
                        break; 
                    case KeyEvent.VK_D:         //Direita
                    case KeyEvent.VK_RIGHT:
                        estado = DIREITA; 
                        //System.out.println("Direita"); 
                        break; 
                    case KeyEvent.VK_SPACE:
                        estado = ACAO; 
                        //System.out.println("Ação"); 
                        break; 
                }
            }
        });       

        Thread enviar = new Thread(){
            public void run(){
                String buf = new String();   
                boolean mudanca = false;                 
                while(true){
                    try {
                        sleep(20); 
                    } catch (InterruptedException e) {
                        //TODO: handle exception
                    }
                    switch(estado){
                        case PARADO:    
                            buf = "111"; 
                            //os.println("111");   
                            //System.out.println("Personagem PARADO!");
                        break; 
                        case ESQUERDA:  
                            buf = "444";    
                            //mudanca = true;
                        // os.println("444");   
                            //System.out.println("Personagem andando para a ESQUERDA!");
                        break; 
                        case DIREITA:   
                            buf = "666";  
                            //mudanca = true;
                            //os.println("666");    
                            //System.out.println("Personagem andando para a DIREITA!");
                        break; 
                        case CIMA:     
                            buf = "888"; 
                            //mudanca = true;
                            //os.println("888");   
                            //System.out.println("Personagem andando para CIMA!");
                        break; 
                        case DESCE:     
                            buf = "222"; 
                            //mudanca = true;
                            //os.println("222");
                            //System.out.println("Personagem andando para BAIXO!");
                        break; 
                        case ACAO:   
                            buf = "555"; 
                            //mudanca = true;
                            //os.println("555");  
                            //System.out.println("Personagem APERTANDO UM BOTÃO!");
                        break; 
                    }
                    if(estado != PARADO)
                        os.println(buf); 
                } 
            }
        };

        enviar.start();
        
        Thread receber = new Thread(){
            String ident, posx, posy; 
            String[] teste ; 
            public void run(){
                while(true){
                //if(is.hasNextLine()){

                    ident = is.nextLine(); 
                    posx = is.nextLine(); 
                    posy = is.nextLine(); 

                    //System.out.println(ident + " " + posx + " " + posy); 

                    aid = Integer.parseInt(ident); 
                    ax = Integer.parseInt(posx); 
                    ay = Integer.parseInt(posy); 

                    if(aid == 999 || ax == 999 || ay == 999)
                        EndGame(); 

                    AtualizaTela(aid, ax, ay);
                          
                 //}
                    screen.repaint();
                }

            }
        };
        receber.start();
    }

    void EndGame(){
        //screen.FimMensagem(g);
    }

    void AtualizaTela(int id, int x, int y){
        screen.posX[id] = x; 
        screen.posY[id] = y;         
    }

    int getValue(String texto){
        return Integer.parseInt(texto); 
    }
   
    /*@Override
    public void run() {
        
    }*/

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