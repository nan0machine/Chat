package client;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client {
	
	private String nickname;
	private InetAddress address;
	private int port;
	
	public Client(String nickname, String address, int port) {
		this.nickname = nickname;
		try {
			this.address =  InetAddress.getByName(address);
		}catch(UnknownHostException e) {
			e.printStackTrace();
		}
		this.port = port;
	}
	
	public String toString() { 
		return this.nickname + " " + this.address.toString() + " " + this.port;
	}
}
