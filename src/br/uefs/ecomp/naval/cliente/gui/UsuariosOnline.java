package br.uefs.ecomp.naval.cliente.gui;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JTable;


public class UsuariosOnline extends Thread{
	
	private String nome;
	private JTable tabela;
	private DatagramSocket SocketCliente2;
	private ArrayList<String> tabelaAtual;
	private boolean trabalhaThread = true;
	
	public UsuariosOnline(String nome, JTable tabela, DatagramSocket SocketCliente) {
		this.nome = nome;
		this.tabela = tabela;
		tabelaAtual = new ArrayList<String>();
		SocketCliente2 = SocketCliente;
	}

	@Override
	public void run(){
		try {
			byte[] dadosEnvio = new byte[1024];
			InetAddress DirectionIP = InetAddress.getByName("localhost");
			String frase = "Online;"+nome;
			dadosEnvio = frase.getBytes();
			DatagramPacket pacoteEnvio = new DatagramPacket(dadosEnvio, dadosEnvio.length, DirectionIP, 6789);
			System.out.println("passei 2");
			SocketCliente2.send(pacoteEnvio);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while(trabalhaThread){
			try {
				byte[] dadosRetorno = new byte[1024];
				DatagramPacket pacoteRetorno = new DatagramPacket(dadosRetorno, dadosRetorno.length);
				SocketCliente2.receive(pacoteRetorno);
	            ByteArrayInputStream baos = new ByteArrayInputStream(pacoteRetorno.getData());
	            ObjectInputStream oos = new ObjectInputStream(baos);
	            ArrayList<String> jogadoresOnline = (ArrayList<String>)oos.readObject();
	            if(!tabelaAtual.equals(jogadoresOnline)){
	            	tabelaAtual = jogadoresOnline;
	            	PlayerTableModel tabelaJogadores = new PlayerTableModel(jogadoresOnline);
					tabela.setModel(tabelaJogadores);
					tabelaJogadores.addTableModelListener(tabela);
	            }
				byte[] dadosEnvio = new byte[1024];
				InetAddress DirectionIP = InetAddress.getByName("localhost");
				String frase = "Ok;";
				dadosEnvio = frase.getBytes();
				DatagramPacket pacoteEnvio = new DatagramPacket(dadosEnvio, dadosEnvio.length, DirectionIP, 6789);
				SocketCliente2.send(pacoteEnvio);
			} catch (SocketException e) {
				trabalhaThread = false;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void matarThread(){
		trabalhaThread = false;
	}
	
		
}
