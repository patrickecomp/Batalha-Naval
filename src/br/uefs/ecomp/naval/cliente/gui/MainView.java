package br.uefs.ecomp.naval.cliente.gui;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;


public class MainView {
	
	private JFrame frame; 
	private JPanel jpMain; 
	private JPanel jpList; 
	private JTable tbPlayers;
	private JScrollPane scroll;
	private String player;
	private DatagramSocket socketListPlayers;
	private DatagramSocket socketSend;
	private UsuariosOnline users;
	private OuveDesafio challenge;
	
	public MainView(String namePlayer, DatagramSocket socketCliente){
		this.player = namePlayer;
		socketSend = socketCliente;
	}
	public void show() throws SocketException{
		socketListPlayers = new DatagramSocket();
		createFrame();
		createJpMain();
		createJpList();
		createList();
		init();
		frame.pack();
		frame.setSize(700, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void init(){
		users = new UsuariosOnline(this.player, tbPlayers, socketListPlayers);
    	challenge = new OuveDesafio(socketSend, this);
    	users.start();
    	challenge.start();
	}
	private void createFrame(){
		frame = new JFrame("Naval Battle ["+this.player+"]");
	}
	
	private void createJpMain(){
		BorderLayout layout = new BorderLayout(); 
		jpMain = (JPanel)frame.getContentPane(); 
		jpMain.setLayout(layout);
	}
	
	private void createJpList(){
		jpList = new JPanel();
		BoxLayout layout = new BoxLayout(jpList, BoxLayout.Y_AXIS);
		jpList.setLayout(layout);
		jpMain.add(jpList, BorderLayout.WEST);
	}
	
	private void createList(){
		JLabel lblList = new JLabel("Players List");
		tbPlayers = new JTable(); 
		scroll = new JScrollPane();
		createTableList();
		createScrollPane();
		jpList.add(lblList); 
		jpList.add(Box.createRigidArea(new Dimension(20, 20)));
		jpList.add(scroll);
	}

	private void createScrollPane(){
		scroll.setSize(100, 100);
		scroll.getViewport().setBorder(null);
		scroll.getViewport().setSize(100, 100);
		scroll.setPreferredSize(new Dimension(100, 100));
		scroll.setVisible(true);
	}
	
	private void createTableList(){
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
	/*	PlayerTableModel model = new PlayerTableModel(novo); 
		tbPlayers.setModel(model);
		model.addTableModelListener(tbPlayers);*/
		tbPlayers.setBorder(new LineBorder(Color.black));
		tbPlayers.setGridColor(Color.black);
		tbPlayers.setShowGrid(true);
		tbPlayers.setFont(font);
		tbPlayers.setRowHeight(30);
		tbPlayers.setSize(100, 100);
		tbPlayers.addMouseListener(new CallToPlay());
		scroll.add(tbPlayers);
		scroll.setViewportView(tbPlayers);
		
	}
	

	private class CallToPlay extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e){
		try{
			if  (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
				int [] selectedRow = tbPlayers.getSelectedRows(); 
				PlayerTableModel model = (PlayerTableModel) tbPlayers.getModel();
				String selectedPlayer = (String) model.getValueAt(selectedRow[0], 0);
				InetAddress DirectionIP = InetAddress.getByName("localhost");
    			String frase = "Desafiar" + ";" + selectedPlayer;
    			byte[] dadosEnvio = new byte[1024];
    			dadosEnvio = frase.getBytes();
    			DatagramPacket pacoteEnvio = new DatagramPacket(dadosEnvio, dadosEnvio.length, DirectionIP, 7654);
				socketSend.send(pacoteEnvio);
			}
		}catch(IOException erro){
			JOptionPane.showMessageDialog(null, "User Address not found or sent item didn't arrive.", "ERRO: Connection", JOptionPane.ERROR_MESSAGE);
		}
		}
	}


	public void fechar() {
		challenge.matarThread();
		users.matarThread();
		frame.dispose();
	}
	
}
