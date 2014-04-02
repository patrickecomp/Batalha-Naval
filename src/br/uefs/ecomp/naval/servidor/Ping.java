package br.uefs.ecomp.naval.servidor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe que passa a lista de usuarios online para o cliente.
 * 
 * @author Patrick Vieira Brito e Silva e Lucas Almeida
 * 
 */
public class Ping extends Thread{
	
	private DatagramSocket socketServidor;//Segundo socket, responsavel pela transmissão de informações da lista de usuarios online
	private List<Player> listJogadores;
	private List<String> nickJogadores;
	private List<Player> listaAtualizada;
	
	public Ping(DatagramSocket socketServidor){
		this.socketServidor = socketServidor;
		listJogadores = new ArrayList<Player>();
		listaAtualizada = new ArrayList<Player>();
		nickJogadores = new ArrayList<String>();
	}
	
	@Override
	public void run(){
		byte[] dadosRetorno = new byte[1024];
		DatagramPacket pacoteRetorno = new DatagramPacket(dadosRetorno, dadosRetorno.length);
		try {
			socketServidor.receive(pacoteRetorno);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String frase = new String(pacoteRetorno.getData());
		System.out.println(frase);
		String[] palavras = frase.split(";");
		Player jogador = new Player(palavras[0], pacoteRetorno.getPort(), pacoteRetorno.getAddress(), palavras[1].trim());
		if(palavras[0].equals("Online")){
			listJogadores.add(jogador);
			nickJogadores.add(palavras[1].trim());
			System.out.println("Player foi adicionado: "+palavras[1].trim());
		}
		while (true) {
			for(Player obj : listaAtualizada){
				if(!listJogadores.contains(obj)){
					listJogadores.add(obj);
					System.out.println("Player foi atualizado: "+palavras[1].trim());
				}
			}
			if(listJogadores.isEmpty()){
				try {
					socketServidor.setSoTimeout(0);
					socketServidor.receive(pacoteRetorno);
					frase = new String(pacoteRetorno.getData());
					palavras = frase.split(";");
					jogador = new Player(palavras[0], pacoteRetorno.getPort(), pacoteRetorno.getAddress(), palavras[1]);
					listaAtualizada.add(jogador);
					nickJogadores.add(palavras[1].trim());
					System.out.println("Player foi adicionado: "+palavras[1].trim());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
            Iterator i = listJogadores.iterator();
            Player pl = null;
            while (i.hasNext()) {
				try{
					pl = (Player) i.next();
					ByteArrayOutputStream bStream = new ByteArrayOutputStream();
					ObjectOutput oo = new ObjectOutputStream(bStream);
					oo.writeObject(nickJogadores);
					byte [] buf=bStream.toByteArray();
					DatagramPacket pacoteEnvio = new DatagramPacket(buf, buf.length, pl.getIP(), pl.getPorta());
					socketServidor.send(pacoteEnvio);
					socketServidor.setSoTimeout(1000);
					dadosRetorno = new byte[1024];
					pacoteRetorno = new DatagramPacket(dadosRetorno, dadosRetorno.length);
					socketServidor.receive(pacoteRetorno);
					frase = new String(pacoteRetorno.getData());
					palavras = frase.split(";");
					jogador = new Player(palavras[0], pacoteRetorno.getPort(), pacoteRetorno.getAddress(), palavras[1]);
					if(palavras[0].equals("Online")){
						listaAtualizada.add(jogador);
						nickJogadores.add(palavras[1].trim());
						System.out.println("Player foi adicionado: "+palavras[1].trim());
					}
				} catch (SocketException e) {
					i.remove();
					listaAtualizada.remove(pl);
					nickJogadores.remove(pl.getNick().trim());
					System.out.println("Player foi removido2: "+pl.getNick().trim());
				} catch (SocketTimeoutException ex){
					i.remove();
					listaAtualizada.remove(pl);
					nickJogadores.remove(pl.getNick().trim());
					System.out.println("Player foi removido: "+pl.getNick().trim());
				} catch (IOException ex){
					System.out.println(ex.getMessage());
				}
			}
				
		}

	}

}
