package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;

public class ClientHandler implements Runnable{
	
	public Socket socket;
	public final DataInputStream input;
	public final DataOutputStream output;
	
	private String name;
	public final int ID;
	
	private String received_msg;
	private boolean running;
	
	public ClientHandler(Socket socket,
						 DataInputStream input,
						 DataOutputStream output,
						 int ID) 
	{
		this.socket = socket;
		this.input = input;
		this.output = output;
		this.ID = ID;
	}
	
	
	public void run() {
		running = true;
		while(running) {
			try {
				received_msg = input.readUTF();
			
				System.out.println(received_msg);
				process(received_msg);
			
			}catch(Exception e) {
				e.printStackTrace();
				disconnect();
			}
			}
	}
	
	
	private void process(String str) {
		if(str.equals("/d/")) {
			disconnect();
			return;
		}
		if(str.startsWith("/n/")) {
			this.name = str.replace("/n/", "");
			return;
		}
		Send(str);
	}
	
	
	private void Send(String message) {
		for(ClientHandler client : Server.clients) {
			try {
				client.output.writeUTF(this.name + ": " + message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void disconnect() {
		try {
			//input.close();
			//output.close();
			socket.close();
			running = false;
			Server.clients.remove(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return "Name: " + this.name + " ID: " + this.ID;
	}
	
}

