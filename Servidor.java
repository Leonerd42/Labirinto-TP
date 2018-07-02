//Arquivo 2 do servidor 
import java.net.*;
import java.io.*;
import java.util.*;
import javax.sql.rowset.spi.SyncResolver;
import com.sun.media.jfxmedia.events.PlayerEvent;
import org.omg.PortableServer.IdAssignmentPolicyValue;

class Servidor {
  
    Servidor(){
    }

    public static void main(String[] args) {
        ServerSocket sc = null; 
        Player jog[] = new Player[4]; 
    
        try {
            sc = new ServerSocket(8080); 
        } catch (IOException e) {
            //TODO: handle exception
        }
        
        System.out.println("Aguardando jogadores..."); 

        for(int i=0; i<4; i++){  
            try {
                Socket s = sc.accept(); 
                if(jog[i] == null){
                    jog[i] = new Player(s,i);
                    System.out.println("Jogador " + (i+1) + " conectado!");
                }

            } catch (IOException e) {
                //TODO: handle exception
            }
            if(jog[0] != null && jog[1] != null && jog[2] == null){
                //Ação para começar o jogo... 
                Thread t = new Thread(){        //Essa thread conta o tempo antes de começar o jogo
                    int tempo = 5;              //para que todos os jogadores possam conectar
                    public void run(){      
                        while(tempo > 0){
                            try {
                                System.out.println(tempo + " segundos pra comecar o jogo!"); 
                                sleep(1000); 
                                tempo--; 
                            } catch (Exception e) {
                                //TODO: handle exception
                            }

                            if(jog[3] != null){
                                break; 
                            }

                            if(tempo == 0){
                                new Jogo(jog).start(); 
                                while(true) {}
                            }
                        }
                    }
                };
                t.start(); 
            }
        }
        new Jogo(jog).start(); 
    }

}

class Player extends Thread {
    public Socket socket; 
    public int id; 
    public int protocolo; 

    public int x; 
    public int y; 

    public PrintStream os; 
    public Scanner is; 
    
    Player(Socket socket, int n){
        this.socket = socket;
        this.id = n; 

        try{
            os = new PrintStream(socket.getOutputStream()); 
            is = new Scanner (socket.getInputStream()); 
        }catch (IOException e) {}
    }

    public void run (){
        //System.out.println("Classe do jogador rodando!!!"); 
        os.println(id); 
        //os.println("777"); 
        while(true){
           // System.out.println("teste"); 
            if(is.hasNextLine()){
                protocolo = Integer.parseInt(is.nextLine()); 
            }
        }
    }
}

class Jogo extends Thread {

    Player jog[]; 
    static int SCREEN_WIDTH = 1290; 
    static int SCREEN_HEIGHT = 689;
    public boolean EmExecucao;

    Jogo (Player jogadores[]){
        this.jog = jogadores; 
        System.out.println("Começou"); 

        for(int i=0; i<4; i++){
            if(jog[i] != null){
                //jog[i].os.println(i);
                jog[i].start();                 
            }
        }
    }

    public void run(){
        int vencedor; 
        IniciaJogo();
        do{
            Protocolos();
            vencedor = EndGame();
            //AtualizaDados();
            try {
                sleep(8);
            } catch (InterruptedException e) {} 
        }while(EnviaDados() && EmExecucao); 

        System.out.println("Game over! Vencedor foi o jogador " + (vencedor + 1)); 
        for(int i=0; i<4; i++){
            if(jog[i] != null)
                for(int j=0; j<3; j++)          //Envia 3 vezes para cada jogador
                    jog[i].os.println("999"); 
        }
    }

    void IniciaJogo(){
        jog[0].x = 0; 
        jog[0].y = 0; 
        jog[1].x = 1200; 
        jog[1].y = 30; 
        if(jog[2] != null){
            jog[2].x = 30; 
            jog[2].y = 620; 
        }
        if(jog[3] != null){
            jog[3].x = 1200; 
            jog[3].y = 620; 
        }
        
        EmExecucao = true; 
    }

    void Protocolos(){
        for(int i=0; i<4; i++)
            if(jog[i] != null)
                Move(jog[i].x, jog[i].y, jog[i].protocolo,i) ; 
    }

    void Move(int x, int y, int protocolo, int i){
        int passo = 2; 
        switch(protocolo){
            case 222: 
                if(jog[i].y < SCREEN_HEIGHT-50)
                    jog[i].y += passo; 
                break; 
            case 444: 
                if(jog[i].x > 0)
                    jog[i].x -= passo; 
                break; 
            case 666: 
                if(jog[i].x < SCREEN_WIDTH-50)
                    jog[i].x += passo; 
                break; 
            case 888: 
                if(jog[i].y > 0)
                    jog[i].y -= passo; 
                break;
            case 555: 
                //if()
                break;  
        }

    }

    int EndGame(){                  //Rotina para identificar o fim do jogo
        for(int i=0; i<4; i++){
            if(jog[i] != null){
                if(jog[i].x+25 >= 600 && jog[i].x+25 <= 660 && jog[i].y+25 >= 300 && jog[i].y+25 <= 360){
                    EmExecucao = false;
                    return i; 
                }                   
            }
        }
        return -1; 
    }

    public synchronized boolean EnviaDados(){   //Rotina para enviar os dados de todos os jogadores
                                                //para todos os jogadores
        for(int i=0; i<4; i++)
            if(jog[i] != null){
                for(int j=0; j<4; j++){
                    if(jog[j] != null){
                        jog[i].os.println(jog[j].id); 
                        jog[i].os.println(jog[j].x); 
                        jog[i].os.println(jog[j].y);
                    }
                }                
            }

        for(int i=0; i<4; i++)
            if(jog[i] != null)
                jog[i].os.flush();
        return true; 
    }

    boolean AtualizaDados(){
        for(int i=0; i<4; i++)
            if(jog[i] != null){
                jog[i].x = 42; 
                jog[i].y = 42; 
            }
        return true;
    }
}