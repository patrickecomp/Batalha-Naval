package br.uefs.ecomp.naval.cliente.gui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpringLayout;

import br.uefs.ecomp.naval.cliente.game.WaterCraft;

public class ChooseShip {

	private JFrame frame; 
	private JPanel jpMain; 
	private String player1;
	private String player2;
	private InetAddress ip;
	private int porta;
	private DatagramSocket socketCliente;
	
	public ChooseShip(String p1, String p2, InetAddress inetAddress, int i, DatagramSocket socketCliente) {
		this.player1 = p1;
		this.player2 = p2;
		this.ip = inetAddress;
		this.porta = i;
		this.socketCliente = socketCliente;
	}
	
	public void show(){
		createFrame();
		createJpMain();
		createForm();
		frame.pack();
		frame.setSize(400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void createFrame(){
		frame = new JFrame("Choose a orientation");
	}
	private void createJpMain(){
		SpringLayout layout = new SpringLayout();
		jpMain =  (JPanel)frame.getContentPane();
		jpMain.setLayout(layout);
	}
	
	private void createForm(){
		
		SpringLayout layout = (SpringLayout) jpMain.getLayout();
		final JLabel lblSubmarino = new JLabel("Submarino:");
		final JLabel lblFragata = new JLabel("Fragata:");
		final JLabel lblContratorpedeiros = new JLabel("Contratorpedeiros:");
		final JLabel lblDestroyer = new JLabel("Destroyer:");
		final JLabel lblPortaAvioes = new JLabel("Porta-Aviões:");
		
		final JRadioButton rbtHorizontal = new JRadioButton("Horizontal");
		final JRadioButton rbtVertical = new JRadioButton("Vertical");
		final ButtonGroup group = new ButtonGroup();
		group.add(rbtHorizontal);
		group.add(rbtVertical);
		
		
		final JRadioButton rbtHorizontal1 = new JRadioButton("Horizontal");
		final JRadioButton rbtVertical1 = new JRadioButton("Vertical");
		final ButtonGroup group1 = new ButtonGroup();
		group1.add(rbtHorizontal1);
		group1.add(rbtVertical1);
		
		
		final JRadioButton rbtHorizontal2 = new JRadioButton("Horizontal");
		final JRadioButton rbtVertical2 = new JRadioButton("Vertical");
		final ButtonGroup group2 = new ButtonGroup();
		group2.add(rbtHorizontal2);
		group2.add(rbtVertical2);
		
		
		final JRadioButton rbtHorizontal3 = new JRadioButton("Horizontal");
		final JRadioButton rbtVertical3 = new JRadioButton("Vertical");
		final ButtonGroup group3 = new ButtonGroup();
		group3.add(rbtHorizontal3);
		group3.add(rbtVertical3);
		
		
		final JRadioButton rbtHorizontal4 = new JRadioButton("Horizontal");
		final JRadioButton rbtVertical4 = new JRadioButton("Vertical");
		final ButtonGroup group4 = new ButtonGroup();
		group4.add(rbtHorizontal4);
		group4.add(rbtVertical4);
		
		final JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			
			private List<WaterCraft> ships = new ArrayList<WaterCraft>(); 
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rbtHorizontal.isSelected()){
					ships.add(new WaterCraft("submarino", 1, "submarino", 0));
				}else if(rbtVertical.isSelected()){
					ships.add(new WaterCraft("submarino", 1, "submarinoV", 1));
				}
				
				if(rbtHorizontal1.isSelected()){
					ships.add(new WaterCraft("fragata", 2, "fragata", 0));
				}else if(rbtVertical1.isSelected()){
					ships.add(new WaterCraft("fragata", 2, "fragataV", 1));
				}
				
				
				if(rbtHorizontal2.isSelected()){
					ships.add(new WaterCraft("contratorpedeiros", 3, "contrator", 0));
				}else if(rbtVertical2.isSelected()){
					ships.add(new WaterCraft("contratorpedeiros", 3, "contratorV", 1));
				}
				
				if(rbtHorizontal3.isSelected()){
					ships.add(new WaterCraft("destroyer", 4, "destroyer", 0));
				}else if(rbtVertical3.isSelected()){
					ships.add(new WaterCraft("destroyer", 4, "destroyerV", 1));
				}
				
				
				if(rbtHorizontal4.isSelected()){
					ships.add(new WaterCraft("portaAviões", 5, "porta", 0));
				}else if(rbtVertical4.isSelected()){
					ships.add(new WaterCraft("destroyer", 5, "portaV", 1));
				}
				
				BattleBoardGUI battle;
				try {
					battle = new BattleBoardGUI(player1, player2, ships, ip, porta, socketCliente);
					battle.show();
				} catch (SocketException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}finally{
					frame.dispose();
				}

				
				
			}
		});
		
		jpMain.add(lblSubmarino);
		jpMain.add(lblFragata);
		jpMain.add(lblContratorpedeiros);
		jpMain.add(lblDestroyer);
		jpMain.add(lblPortaAvioes);
		jpMain.add(rbtHorizontal);
		jpMain.add(rbtVertical);
		jpMain.add(rbtHorizontal1);
		jpMain.add(rbtVertical1);
		jpMain.add(rbtHorizontal2);
		jpMain.add(rbtVertical2);
		jpMain.add(rbtHorizontal3);
		jpMain.add(rbtVertical3);
		jpMain.add(rbtHorizontal4);
		jpMain.add(rbtVertical4);
		jpMain.add(btnOk);
		
		layout.putConstraint(SpringLayout.WEST, lblSubmarino, 25, SpringLayout.WEST, jpMain);
		layout.putConstraint(SpringLayout.NORTH, lblSubmarino, 30, SpringLayout.NORTH, jpMain);
		layout.putConstraint(SpringLayout.WEST, rbtHorizontal, 20, SpringLayout.EAST, lblSubmarino);
		layout.putConstraint(SpringLayout.NORTH, rbtHorizontal, 25, SpringLayout.NORTH, jpMain);
		layout.putConstraint(SpringLayout.WEST, rbtVertical, 20, SpringLayout.EAST, rbtHorizontal);
		layout.putConstraint(SpringLayout.NORTH, rbtVertical, 25, SpringLayout.NORTH, jpMain);
		
		
		layout.putConstraint(SpringLayout.WEST, lblFragata, 25, SpringLayout.WEST, jpMain);
		layout.putConstraint(SpringLayout.NORTH, lblFragata, 30, SpringLayout.SOUTH, lblSubmarino);
		layout.putConstraint(SpringLayout.WEST, rbtHorizontal1, 20, SpringLayout.EAST, lblFragata);
		layout.putConstraint(SpringLayout.NORTH, rbtHorizontal1, 25, SpringLayout.SOUTH, rbtHorizontal);
		layout.putConstraint(SpringLayout.WEST, rbtVertical1, 20, SpringLayout.EAST, rbtHorizontal1);
		layout.putConstraint(SpringLayout.NORTH, rbtVertical1, 25, SpringLayout.SOUTH, rbtVertical);
		
		
		layout.putConstraint(SpringLayout.WEST, lblContratorpedeiros, 25, SpringLayout.WEST, jpMain);
		layout.putConstraint(SpringLayout.NORTH, lblContratorpedeiros, 30, SpringLayout.SOUTH, lblFragata);
		layout.putConstraint(SpringLayout.WEST, rbtHorizontal2, 20, SpringLayout.EAST, lblContratorpedeiros);
		layout.putConstraint(SpringLayout.NORTH, rbtHorizontal2, 25, SpringLayout.SOUTH, rbtHorizontal1);
		layout.putConstraint(SpringLayout.WEST, rbtVertical2, 20, SpringLayout.EAST, rbtHorizontal2);
		layout.putConstraint(SpringLayout.NORTH, rbtVertical2, 25, SpringLayout.SOUTH, rbtVertical1);
		
		layout.putConstraint(SpringLayout.WEST, lblDestroyer, 25, SpringLayout.WEST, jpMain);
		layout.putConstraint(SpringLayout.NORTH, lblDestroyer, 40, SpringLayout.SOUTH, lblContratorpedeiros);
		layout.putConstraint(SpringLayout.WEST, rbtHorizontal3, 20, SpringLayout.EAST, lblDestroyer);
		layout.putConstraint(SpringLayout.NORTH, rbtHorizontal3, 25, SpringLayout.SOUTH, rbtHorizontal2);
		layout.putConstraint(SpringLayout.WEST, rbtVertical3, 20, SpringLayout.EAST, rbtHorizontal3);
		layout.putConstraint(SpringLayout.NORTH, rbtVertical3, 25, SpringLayout.SOUTH, rbtVertical2);
		
		layout.putConstraint(SpringLayout.WEST, lblPortaAvioes, 25, SpringLayout.WEST, jpMain);
		layout.putConstraint(SpringLayout.NORTH, lblPortaAvioes, 37, SpringLayout.SOUTH, lblDestroyer);
		layout.putConstraint(SpringLayout.WEST, rbtHorizontal4, 20, SpringLayout.EAST, lblPortaAvioes);
		layout.putConstraint(SpringLayout.NORTH, rbtHorizontal4, 25, SpringLayout.SOUTH, rbtHorizontal3);
		layout.putConstraint(SpringLayout.WEST, rbtVertical4, 20, SpringLayout.EAST, rbtHorizontal4);
		layout.putConstraint(SpringLayout.NORTH, rbtVertical4, 25, SpringLayout.SOUTH, rbtVertical3);
		
		layout.putConstraint(SpringLayout.EAST, btnOk, -40, SpringLayout.EAST, jpMain);
		layout.putConstraint(SpringLayout.SOUTH, btnOk, -40, SpringLayout.SOUTH, jpMain);
	}
	

}
