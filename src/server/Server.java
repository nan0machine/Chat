package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server implements Runnable{
	private ServerSocket serverSocket;
	private int port;
	private Socket socket;
	
	private Thread run;
	private boolean running;
	
	private static int ID = 0;
	
	
	static Vector<ClientHandler> clients = new Vector<>();
	
	
	public Server(int port) {
		this.port = port;
		
		try {
			serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		run = new Thread(this,"Server");
		run.start();
	}
	
	public void run() {
		running = true;
		
		while(running) {
			try {
				socket = serverSocket.accept();
				System.out.println("New client request received : " + socket); 
				
				ClientHandler new_client = new ClientHandler(socket,
												new DataInputStream(socket.getInputStream()),
												new DataOutputStream(socket.getOutputStream()),
												++ID);
				System.out.println(ID);
	            System.out.println("Adding this client to active client list"); 

				Thread u = new Thread(new_client);
				
				clients.add(new_client);
				u.start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
}
