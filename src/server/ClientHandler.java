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
		String received_msg = null;
			try {
				received_msg = input.readUTF();
				process(received_msg);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void process(String str) {
		
		if(str.startsWith("/m/")) {
			str = str.substring(3, str.length());
			Send(str);
			
		}else if(str.startsWith("/c/")) {
			str = str.substring(3, str.length());
	
			if(str.equals("/d/")) {
				disconnect();
			}
			else if(str.startsWith("/n/")) {
				str = str.substring(3, str.length());
				setName(str);
			}
		}	
	}
	
	private void setName(String name) {
		this.name = name;
	}
	
	private String getName() {
		return this.name;
	}
	
	private void Send(String message) {
		for(ClientHandler client : Server.clients) {
			try {
				System.out.println(message);
				client.output.writeUTF(getName() + ": " + message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void disconnect() {
		this.running = false;
		try {
			this.socket.close();
		} catch (IOException e) {e.printStackTrace();}
		Server.clients.remove(this);
		System.out.println(this.toString() + " removed from list");
	}
	
	public String toString() {
		return "Name: " + getName() + " ID: " + this.ID;
	}
	
}

