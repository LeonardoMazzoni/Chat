package com.mycompany.chat;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Client {
    
    private final static int porta = 6789;
    private Socket s;
    private BufferedReader tastiera;
    private BufferedReader inDalServer;
    private DataOutputStream outVersoServer;
    
    public void connetti()
    {
        
        tastiera = new BufferedReader(new InputStreamReader(System.in));
        
        try 
        {
            InetAddress ip = InetAddress.getByName("localhost");
            s = new Socket(ip, porta);
            inDalServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
            outVersoServer = new DataOutputStream(s.getOutputStream());
            
            ThreadInvio threadInvio = new ThreadInvio();
            ThreadLettura threadLettura = new ThreadLettura();
        } catch (Exception ex) {
            ex.toString();
        }
    }
    
    public class ThreadInvio extends Thread
    {
        public ThreadInvio()
        {
            start();
        }
        
        @Override
        public void run()
        {
            try
            {
                for(;;)
                {
                    String messaggio = tastiera.readLine();
                    if(messaggio.equals("FINE"))
                    {
                        outVersoServer.writeBytes("Chiusura connessione." + '\n');
                        inDalServer.close();
                        outVersoServer.close();
                        s.close();
                        System.exit(0);
                        break;
                    }
                    outVersoServer.writeBytes(messaggio + '\n');
                }
            }
            catch (Exception ex)
            {
                ex.toString();
                System.exit(1);
            }
        }
    }
    
    public class ThreadLettura extends Thread
    {
        public ThreadLettura()
        {
            start();
        }
        
        @Override
        public void run()
        {
            try
            {
                for(;;)
                {
                    String messaggio = inDalServer.readLine();
                    if(messaggio != null) System.out.println(messaggio);
                }
            }catch (Exception ex)
            {
                ex.toString();
                System.exit(1);
            }
        }
    }
}
