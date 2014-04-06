package br.uefs.ecomp.naval.cliente.gui;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class Login {
	
	private JFrame frame;
	private JPanel jpMain; 
	private JTextField txtLogin;
	private DatagramSocket socketCliente;
	
	public Login(){
		
	}
	
	public void show() throws SocketException{
		
		socketCliente = new DatagramSocket();
		createFrame();
		createJpMain();
		createFormLogin();
		frame.pack();
		frame.setSize(500, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void createFrame(){
		frame = new JFrame("Login");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
				socketCliente.close();
		    }
		});
	}
	
	private void createJpMain(){
		SpringLayout layout = new SpringLayout();
		jpMain = (JPanel)frame.getContentPane();
		jpMain.setLayout(layout);
	}
	
	private void createFormLogin(){
		
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
		SpringLayout layout = (SpringLayout)jpMain.getLayout();
		
		JLabel lblLogin = new JLabel("Login:");
		JButton  btnLogin = new JButton("Sing in");
		txtLogin = new JTextField(25);
		lblLogin.setFont(font);
		btnLogin.setBackground(Color.blue);
		btnLogin.setForeground(Color.white);
		btnLogin.setFont(font);
		txtLogin.setFont(font);
		
		
		SingIn listener = new SingIn();
		txtLogin.addActionListener(listener);
		btnLogin.addActionListener(listener);
		
		jpMain.add(lblLogin);
		jpMain.add(txtLogin);
		jpMain.add(btnLogin);
		
		layout.putConstraint(SpringLayout.WEST, lblLogin, 25, SpringLayout.WEST, jpMain);
		layout.putConstraint(SpringLayout.NORTH, lblLogin, 30, SpringLayout.NORTH, jpMain);
		layout.putConstraint(SpringLayout.WEST, txtLogin, 15, SpringLayout.EAST, lblLogin);
		layout.putConstraint(SpringLayout.NORTH, txtLogin, 30, SpringLayout.NORTH, jpMain);
		layout.putConstraint(SpringLayout.EAST, btnLogin, -38, SpringLayout.EAST, jpMain);
		layout.putConstraint(SpringLayout.SOUTH, btnLogin, -25, SpringLayout.SOUTH, jpMain);
	
	}	
	private class SingIn implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			String nomeCliente = txtLogin.getText().trim();
			System.out.println("nomeCliente = "+nomeCliente);
        	if(nomeCliente.length() == 0){
        		JOptionPane.showMessageDialog(null, "O campo esta em branco!");
        	}else{
        		if(nomeCliente.length() <=8){
        			try{
        				InetAddress DirectionIP = InetAddress.getByName("localhost");
        				byte[] dadosEnvio = new byte[1024];
        				byte[] dadosRetorno = new byte[1024];
        				String frase = "Logar;" + nomeCliente;
        				dadosEnvio = frase.getBytes();
        				DatagramPacket pacoteEnvio = new DatagramPacket(dadosEnvio, dadosEnvio.length, DirectionIP, 7654);
        				socketCliente.send(pacoteEnvio);
        				socketCliente.setSoTimeout(1000);
        				DatagramPacket pacoteRetorno = new DatagramPacket(dadosRetorno, dadosRetorno.length);
        				socketCliente.receive(pacoteRetorno);
        				System.out.println("trava aqui");
        				frase = new String(pacoteRetorno.getData());
        				
        				if(frase.trim().equals("Logado")){
        					String player = txtLogin.getText().trim();
        					MainView view = new MainView(player, socketCliente);
        					view.show();
        					frame.dispose();
        				}
        			}catch(SocketTimeoutException ex){
        				//Reenvia pacote
        				try{
        					InetAddress DirectionIP = InetAddress.getByName("localhost");
        					byte[] dadosRetorno = new byte[1024];
        					String frase = "Logar;" + nomeCliente;
            				byte[] dadosEnvio = new byte[1024];
            				dadosEnvio = frase.getBytes();
            				DatagramPacket pacoteEnvio = new DatagramPacket(dadosEnvio, dadosEnvio.length, DirectionIP, 7654);
            				socketCliente.send(pacoteEnvio);
        					DatagramPacket pacoteRetorno = new DatagramPacket(dadosRetorno, dadosRetorno.length);
        					socketCliente.setSoTimeout(1000);
        					socketCliente.receive(pacoteRetorno);
        				}catch(SocketTimeoutException exp){
        					JOptionPane.showMessageDialog(null, "O Servidor esta offline!");//Tratamento de erro
        				} catch (UnknownHostException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
        			}catch(Exception ex){
        				System.out.println("Erro: " + ex.getMessage());
        			}
        		}else{
        			JOptionPane.showMessageDialog(null, "O Login deve ter no maximo 8 caracteres!");
        		}
        		
        	}
		}
	}
	
	
	public static void main(String [] args) throws SocketException{
		Login log = new Login(); 
		log.show();
	}
}
