import java.net.*;
import java.io.*;
import java.util.*;
import javax.sql.rowset.spi.SyncResolver;
import org.omg.PortableServer.IdAssignmentPolicyValue;

class Server {//implements Runnable {    
    ServerSocket serversocket = null; 
    
    Server(){
        final int porta = 73; 
        try{
            serversocket = new ServerSocket(porta); 
        }catch (IOException e){
            System.err.println("O porto " + porta + " não pode ser usado.\n" + e);
        }

        System.out.println("Servidor aguardando cliente..."); 

        while(true){
            Dados dados = new Dados();
            Scanner is[] = new Scanner[Dados.NUM_MAX_JOGADORES];
            PrintStream os[] = new PrintStream[Dados.NUM_MAX_JOGADORES]; 
            //Cliente 1 
            ConectaCliente(Dados.CLIENTE_UM, is, os); 
            new Recebe(Dados.CLIENTE_UM, is, dados).start(); 
            new Envia(os, dados).start();
            //Cliente dois
            ConectaCliente(Dados.CLIENTE_DOIS, is, os); 
            new Recebe(Dados.CLIENTE_DOIS, is, dados).start();             
            //Cliente três
            ConectaCliente(Dados.CLIENTE_TRES, is, os); 
            new Recebe(Dados.CLIENTE_TRES, is, dados).start(); 
            //Cliente quatro
            ConectaCliente(Dados.CLIENTE_QUATRO, is, os); 
            new Recebe(Dados.CLIENTE_QUATRO, is, dados).start(); 
        }        
   }

   boolean ConectaCliente(int id, Scanner is[], PrintStream os[]){
        Socket clientSocket = null; 
        try {
            clientSocket = serversocket.accept();  //Servidor aceita o cliente 
            System.out.println ("Cliente " + id + " entrou no server!");  //Manda mensagem avisando que tal cliente conectou
            is[id] = new Scanner(clientSocket.getInputStream()); 
            os[id] = new PrintStream(clientSocket.getOutputStream()); 
        } catch (Exception e) {     //Retorna um erro 
            System.err.println("Não foi possível conectar com o cliente.\n" + e);
            return false;
        }
        os[id].println(id); //manda o primeiro dado para o cliente sendo o ID dele no servidor
        return true; 
    }

    public static void main(String[] args) {
        new Server(); 
        //servidor.start();
    }
}

class Dados{
    static final int NUM_MAX_JOGADORES = 4;
    static final int CLIENTE_UM = 0;
    static final int CLIENTE_DOIS = 1;
    static final int CLIENTE_TRES = 2;
    static final int CLIENTE_QUATRO = 3;
    static final int LARG_CLIENTE = 600;
    static final int ALTU_CLIENTE = 450;

    class EstadoCliente {
        String dir; 
        String x, y; 
    }
    EstadoCliente estado[] = new EstadoCliente[NUM_MAX_JOGADORES];
    Dados(){
        for(int i=0; i<NUM_MAX_JOGADORES; i++){
            estado[i] = new EstadoCliente(); 
            switch(i){
                case 0: 
                    estado[i].x = "100"; 
                    estado[i].y = "100"; 
                break; 
                case 1: 
                    estado[i].x = "300"; 
                    estado[i].y = "300"; 
                break; 
                case 2: 
                    estado[i].x = "300"; 
                    estado[i].y = "100"; 
                break; 
                case 3: 
                    estado[i].x = "100"; 
                    estado[i].y = "300"; 
                break;                 
            }
        }
    }
    synchronized boolean EnviaDados(PrintStream os[]){
           try{
               sleep(37); 
           }catch (InterruptedException e) {}

            for(int i=0; i<NUM_MAX_JOGADORES; i++){
                if(os[i] != null){
                    os[i].println(CLIENTE_UM + ". " + estado[CLIENTE_UM].x + ". " + estado[CLIENTE_UM].y);
                    os[i].println(CLIENTE_DOIS + ". " + estado[CLIENTE_DOIS].x + ". " + estado[CLIENTE_DOIS].y); 
                    os[i].println(CLIENTE_TRES + ". " + estado[CLIENTE_TRES].x + ". " + estado[CLIENTE_TRES].y); 
                    os[i].println(CLIENTE_QUATRO + ". " + estado[CLIENTE_QUATRO].x + ". " + estado[CLIENTE_QUATRO].y); 
                }else continue; 
            }

            for(int i=0; i<NUM_MAX_JOGADORES; i++){
                if(os[i] != null)
                    os[i].flush();
                else continue; 
            }
        return true; 
    }

    synchronized void AlteraDados(){    //Sobrecarga
    }

    synchronized void AlteraDados(int x){
    }

   
}
class Recebe extends Thread {
    int IdCliente; 
    Dados dados; 
    Scanner is[]; 
    int aux_x, aux_y; 
    String c; 

    Recebe(int id, Scanner is[], Dados d){
       IdCliente = id; 
       dados = d; 
       this.is = is; 
    }

    public void run(){
        while(true){    
            
            if(is[IdCliente].hasNextLine()){
                c = is[IdCliente].nextLine();
                //System.out.println(c); 
                switch(c.intern()){
                    case "Cima":
                            System.out.println("Cima");
                            aux_y = Integer.parseInt(dados.estado[IdCliente].y,10); 
                            if(aux_y > 0){
                                aux_y -= 5; 
                                dados.estado[IdCliente].y = Integer.toString(aux_y); 
                            }
                    break; 
                    case "Desce":
                            System.out.println("Desce");
                            aux_y = Integer.parseInt(dados.estado[IdCliente].y,10); 
                            if(aux_y < 689-50){
                                aux_y += 5; 
                                dados.estado[IdCliente].y = Integer.toString(aux_y);
                            }
                    break; 
                    case "Esquerda":
                            System.out.println("Esquerda");
                            aux_x = Integer.parseInt(dados.estado[IdCliente].x,10); 
                            if(aux_x > 0){                        
                                aux_x -= 5; 
                                dados.estado[IdCliente].x = Integer.toString(aux_x);
                            }
                    break; 
                    case "Direita":
                            System.out.println("Direita");
                            aux_x = Integer.parseInt(dados.estado[IdCliente].x,10); 
                            if(aux_x < 1290-50){
                                aux_x += 5; 
                                dados.estado[IdCliente].x = Integer.toString(aux_x);
                            }                    
                    break; 
                    } 
            }          
        }
    }
};

class Envia extends Thread {

    PrintStream os[]; 
    Dados dados; 
    int IdCliente; 

    Envia(PrintStream os[], Dados d){
        this.os = os; 
        dados = d;
    }
    public void run(){
       while(true){
            if(!dados.EnviaDados(os))
                break;
            //try {
             //   sleep(33); 
            //} catch (InterruptedException e) {
                //TODO: handle exception
            //}     
        }
    }
};

