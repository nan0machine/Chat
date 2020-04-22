package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
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
			toSend(str);
			
		}else if(str.startsWith("/c/")) {
			str = str.substring(3, str.length());
	
			if(str.equals("/d/")) {
				broadcast("is disconnected!" );
				disconnect();
			}
			else if(str.startsWith("/n/")) {
				str = str.substring(3, str.length());
				setName(str);
			}
		}	
	}
	
	private void toSend(String message) {
		Pattern pattern = Pattern.compile("@");
		Matcher matcher = pattern.matcher(message);
		
		if(matcher.find()) {
			pattern = Pattern.compile("@[\\w]*");
			matcher = pattern.matcher(message);
		
			while(matcher.find()) {
				send(message, matcher.group(0).substring(1) );
			}
		}else {
			broadcast(message);
		}
	}
	
	
	private void send(String message, String receiver) {
		for(ClientHandler client : Server.clients) {
			if(client.name.equals(receiver)) {
				try {
					client.output.writeUTF(getName() + ": " + message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	private void broadcast(String message) {
		for(ClientHandler client : Server.clients) {
			if(!client.name.equals(this.name)) {
				try {
					client.output.writeUTF(getName() + ": " + message);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}	}
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
	
	private void setName(String name) {
		this.name = name;
	}
	
	private String getName() {
		return this.name;
	}
	
	
}

