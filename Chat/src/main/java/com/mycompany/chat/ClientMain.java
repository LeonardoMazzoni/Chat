/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chat;

import java.io.*;
import java.net.*;

public class ClientMain {

    public static void main(String[] args) throws UnknownHostException, IOException{
        Client client = new Client();
        client.connetti();
    }
}
