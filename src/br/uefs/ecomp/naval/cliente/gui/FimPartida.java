package br.uefs.ecomp.naval.cliente.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;


public class FimPartida {
	private JFrame janela;
	private JPanel principal; 
	private int tiros;
	private DatagramSocket socket;
	private String player;
	
	public FimPartida(int tiros, String player1){
		this.tiros = tiros;
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.player = player1;
	}
	
	public void show() throws SocketException{
		
		createFrame();
		createPrincipal();
		createFormLogin();
		janela.pack();
		janela.setSize(500, 150);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setLocationRelativeTo(null);
		janela.setVisible(true);
	}
	
	private void createFrame(){
		janela = new JFrame("Fim da partida!");
		janela.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
		    }
		});
	}
	
	private void createPrincipal(){
		SpringLayout layout = new SpringLayout();
		principal = (JPanel)janela.getContentPane();
		principal.setLayout(layout);
	}
	
	private void createFormLogin(){
		
		SpringLayout layout = (SpringLayout)principal.getLayout();
		
		JLabel status = new JLabel("Vitoria do jogador: "+player);
		JLabel lblLogin = new JLabel("Quantidade de tiros necessarios para afundar as embarcações: "+tiros);
		JButton  btnLogin = new JButton("Ok");
		
		
		SingIn listener = new SingIn();
		btnLogin.addActionListener(listener);
		
		principal.add(status);
		principal.add(lblLogin);
		principal.add(btnLogin);
		
		layout.putConstraint(SpringLayout.WEST, lblLogin, 25, SpringLayout.WEST, principal);
		layout.putConstraint(SpringLayout.NORTH, lblLogin, 30, SpringLayout.NORTH, principal);
		layout.putConstraint(SpringLayout.EAST, btnLogin, -38, SpringLayout.EAST, principal);
		layout.putConstraint(SpringLayout.SOUTH, btnLogin, -25, SpringLayout.SOUTH, principal);
	
	}	
	private class SingIn implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			try{
			InetAddress DirectionIP = InetAddress.getByName("localhost");
			byte[] dadosEnvio = new byte[1024];
			byte[] dadosRetorno = new byte[1024];
			String frase = "Logar;" + player;
			dadosEnvio = frase.getBytes();
			DatagramPacket pacoteEnvio = new DatagramPacket(dadosEnvio, dadosEnvio.length, DirectionIP, 7654);
			socket.send(pacoteEnvio);
			DatagramPacket pacoteRetorno = new DatagramPacket(dadosRetorno, dadosRetorno.length);
			socket.receive(pacoteRetorno);
			System.out.println("trava aqui");
			frase = new String(pacoteRetorno.getData());
			
			if(frase.trim().equals("Logado")){
				MainView view = new MainView(player, socket);
				view.show();
				janela.dispose();
			}
			} catch(IOException exp){
				System.out.println("Erro na conexão!");
			}
		}
		
	}
	
	
}
