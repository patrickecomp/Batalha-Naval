package br.uefs.ecomp.naval.cliente.gui;



import java.io.Serializable;
import java.net.InetAddress;


public class Player implements Serializable{
	
	private String nick;
	private int porta;
	private InetAddress iP;
	
	public Player(String palavras, int porta, InetAddress iP){
		nick = palavras;
		this.porta = porta;
		this.iP = iP;
		
	}
	
	public Player(String string) {
		string = nick;
	}

	public String getNick() {
		return nick;
	}
	
	public int getPorta(){
		return porta;
	}
	
	public InetAddress getIP(){
		return iP;
	}

}