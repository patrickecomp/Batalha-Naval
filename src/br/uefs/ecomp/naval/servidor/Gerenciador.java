package br.uefs.ecomp.naval.servidor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Thread criada para gerenciar as ações dos clientes.
 * 
 * @author Patrick Vieira Brito e Silva e Lucas Almeida
 * 
 */
public class Gerenciador extends Thread{
	
	private PacoteSincronizado pac;//Classe que sincroniza informações com a thread ouvinte.
	private DatagramSocket socketServidor;//primeiro socket, responsavel por receber informações de login e desafio.
	private List<Player> listJogadores;//Lista de jogadores.

	public Gerenciador(PacoteSincronizado pac, DatagramSocket socketServidor) {
		this.pac = pac;
		this.socketServidor = socketServidor;
		listJogadores = new ArrayList<Player>();
		
	}
	
	@Override
	public void run(){
		
		while (!isInterrupted()) {
			Player usuario = pac.entregarPacote();//Uso da classe pacoteSincronizado para passar o player do ouvinte para o gerenciador
			FileReader arq;
			System.out.println(usuario.getAcao()+" Olha aqui");
			if(usuario.getAcao().equals("Logar")){//Caso o usuario deseje logar
				try {
					arq = new FileReader("usuarios" + File.separator + usuario.getNick().trim() + ".ser");
					BufferedReader lerArq = new BufferedReader(arq);//Busca usuario no disco rigido
				} catch (FileNotFoundException e) {//Caso o usuario não tenha registro
					try{
					    FileOutputStream fo = new FileOutputStream("usuarios" + File.separator + usuario.getNick() + ".ser");
					    ObjectOutputStream oo = new ObjectOutputStream(fo);
					    oo.writeObject(usuario);//Cria um arquivo com o nome do usuario
					    oo.close();
					}catch(IOException ex){
						System.err.println("Erro na serialização do arquivo." + usuario.getNick());
					}
				}
				listJogadores.add(usuario);//Adiciona o usuario em uma lista de jogadores.
				String frase = "Logado";
				try {
					enviaPacote(usuario, frase);//Avisa ao usuario que ele conseguiu logar.
				} catch (IOException e) {
					e.printStackTrace();
				}
		      
			}else{
				if(usuario.getAcao().equals("Desafiar")){//Caso o usuario busque o ip do adversario
					InetAddress ipAdversario = null;
					int porta = 0;
					String adversario = null;
					for (Player obj : listJogadores) {  //Busca na lista de jogadores o ip e porta do adversario
					    if(usuario.getNick().equals(obj.getNick())){
					    	ipAdversario = obj.getIP();
					    	porta = obj.getPorta();
					    	adversario = obj.getNick();
					    	System.out.println(porta);
					    }
					}
					try {
						String frase ="Adversario" + ";" + ipAdversario.getHostAddress() + ";" + new Integer(porta).toString() + ";" + adversario;
						enviaPacote(usuario, frase);//Envia para o usuario os dados do adversario.
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
			

		}
	}
	
	private void enviaPacote(Player usuario, String frase) throws IOException{
		byte [] dadosEnvio = new byte[1024];
		dadosEnvio = frase.getBytes();//Avisa ao usuario que ele conseguiu logar.
		DatagramPacket pacoteEnvio = new DatagramPacket(dadosEnvio, dadosEnvio.length, usuario.getIP(), usuario.getPorta());
		socketServidor.send(pacoteEnvio);
	}

}
