package com.mycompany.chat;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
public class Server {

    private ArrayList<MyThread> sockets = new ArrayList();
    private Socket socket = new Socket();
    private ServerSocket s;
    public void start()
    {
        try
        {
            System.out.println("Server in attesa di una connessione...");
            s = new ServerSocket(6789);

            for (int i = 0; i < 2; i++) {
                sockets.add(new MyThread(s.accept()));
                sockets.get(i).start();
            }
            s.close();
        } catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            System.out.println("Errore durante l'istanza del server!");
            System.exit(1);
        }
    }
    
    public class MyThread extends Thread
    {
        private Socket clients = null;
        private BufferedReader inDalClient;
        private DataOutputStream outVersoClient;
        private String nomeUtente;
        
        public MyThread (Socket socket)
        {
            this.clients = socket;
        }
        
        @Override
        public void run()
        {
            try
            {
                comunica();
            }
            catch(Exception ex)
            {
                ex.toString();
            }
        }
        
        public void comunica() throws Exception
        {
            inDalClient = new BufferedReader(new InputStreamReader(clients.getInputStream()));
            outVersoClient = new DataOutputStream(clients.getOutputStream());
            
            outVersoClient.writeBytes("Inserisci nome" + '\n');
            nomeUtente = inDalClient.readLine();
            outVersoClient.writeBytes(nomeUtente + " si è connesso alla chat." + '\n');
            
            for(int i = 0; i > -1; i++)
            {
                String messaggio = inDalClient.readLine();
                if(messaggio.equals("FINE"))
                {
                    System.out.println(nomeUtente + "si è disconnesso." + '\n');
                    sockets.remove(this);
                    break;
                }
                else if (sockets.size()>1)
                {
                    for(MyThread s: sockets)
                    {
                        if(s != this) s.outVersoClient.writeBytes(nomeUtente+": "+messaggio+'\n');
                    }
                }
                else outVersoClient.writeBytes("Non ci sono utenti connessi"+'\n');
            }
            outVersoClient.close();
            inDalClient.close();
            clients.close();
        }
    }
}
