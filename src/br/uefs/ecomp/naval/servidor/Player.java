package br.uefs.ecomp.naval.servidor;

import java.io.Serializable;
import java.net.InetAddress;


public class Player implements Serializable{
	
	private String nick;
	private int porta;
	private InetAddress iP;
	private String acao;
	
	public Player(String acao, int porta, InetAddress iP, String nick){
		this.acao = acao;
		this.porta = porta;
		this.iP = iP;
		this.nick = nick;
		
	}
	

	public Player(String string, int porta2, InetAddress directionIP) {
		this.porta = porta2;
		this.iP = directionIP;
		this.nick = string;
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
	public String getAcao(){
		return acao;
	}

}
