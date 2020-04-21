package client;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.*;

public class Client{
	
	protected String nickname;
	
	protected Socket socket;
	private String address;
	private int port;
	
	protected Thread send;
	
	protected DataInputStream input;
	protected DataOutputStream output;
	
	public Client(String nickname, String address, int port) {
		this.nickname = nickname;
		this.address = address;
		this.port = port;	
	}
	
	
	public boolean openConnection() {
		try{
			socket = new Socket();
			socket.bind(new InetSocketAddress("localhost",0));
			socket.connect(new InetSocketAddress(this.address, this.port));
			
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
		}catch(Exception e ) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public String toString() { 
		return this.nickname + " " + this.address.toString() + " " + this.port;
	}

	
	protected String receive() throws IOException {
		String msg = null;
		msg = input.readUTF();
		return msg;
	}
		
	
	
	protected void send(String text) {
		send = new Thread("send") {
			public void run() {
				try {
					output.writeUTF(text);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	
	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {e.printStackTrace();}
	}


	
	
	
	
	
	
}
