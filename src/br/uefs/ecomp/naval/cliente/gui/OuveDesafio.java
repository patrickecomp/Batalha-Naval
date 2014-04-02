package br.uefs.ecomp.naval.cliente.gui;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import javax.swing.JOptionPane;

public class OuveDesafio extends Thread {

	private DatagramSocket socketCliente;
	private boolean controleThread = true;

	public OuveDesafio(DatagramSocket socketCliente) {
		this.socketCliente = socketCliente;
	}

	@Override
	public void run() {
		while (controleThread) {
			byte[] dadosRetorno = new byte[1024];
			try {
				socketCliente.setSoTimeout(0);
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
			DatagramPacket pacoteRetorno = new DatagramPacket(dadosRetorno,
					dadosRetorno.length);
			try {
				socketCliente.receive(pacoteRetorno);
				String dadosAdversario = new String(pacoteRetorno.getData());
				String[] palavras = dadosAdversario.split(";");
				System.out.println("sd" + palavras[1]);
				if (palavras[0].equals("Adversario")) {
					InetAddress ip = InetAddress.getByName(palavras[1]);
					int porta = new Integer(palavras[2].trim());
					String frase = "Desafio;" + palavras[3].trim();
					byte[] dadosEnvio = frase.getBytes();
					DatagramPacket pacoteEnvio = new DatagramPacket(dadosEnvio,
							dadosEnvio.length, ip, porta);
					socketCliente.send(pacoteEnvio);
				} else {
					if (palavras[0].trim().equals("Desafio")) {
						int reply = JOptionPane.showConfirmDialog(null,
								"Jogador " + palavras[1].trim()
										+ " lhe desafiou. Aceitar desafio ?",
								"Desafio", JOptionPane.YES_NO_OPTION);
						if (reply == JOptionPane.YES_OPTION) {
							String frase = "Aceito;";
							byte[] dadosEnvio = frase.getBytes();
							DatagramPacket pacoteEnvio = new DatagramPacket(
									dadosEnvio, dadosEnvio.length,
									pacoteRetorno.getAddress(),
									pacoteRetorno.getPort());
							socketCliente.send(pacoteEnvio);

							ChooseShip choose = new ChooseShip("eu", palavras[1].trim(), pacoteRetorno.getAddress(), pacoteRetorno.getPort(), socketCliente);
							choose.show();
							controleThread = false;
						} else {
							String frase = "naoAceito;";
							byte[] dadosEnvio = frase.getBytes();
							DatagramPacket pacoteEnvio = new DatagramPacket(
									dadosEnvio, dadosEnvio.length,
									pacoteRetorno.getAddress(),
									pacoteRetorno.getPort());
							socketCliente.send(pacoteEnvio);
						}
					} else {
						if (palavras[0].trim().equals("Aceito")) {
							JOptionPane.showMessageDialog(null,
									"Desafio aceito!");
							ChooseShip choose = new ChooseShip("eu", palavras[1].trim(), pacoteRetorno.getAddress(), pacoteRetorno.getPort(), socketCliente);
							choose.show();
							controleThread = false;} else {
							JOptionPane.showMessageDialog(null,
									"Desafio nao foi aceito!");

						}
					}
				}
			} catch (SocketException e) {
				controleThread = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void matarThread() {
		controleThread = false;
	}

}