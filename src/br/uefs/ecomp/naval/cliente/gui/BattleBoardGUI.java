package br.uefs.ecomp.naval.cliente.gui;



import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.uefs.ecomp.naval.cliente.game.BattleBoard;
import br.uefs.ecomp.naval.cliente.game.WaterCraft;

public class BattleBoardGUI {

	private JFrame frame; 
	private JPanel jpMain; 
	private String player1; 
	private String player2;
	private BattleBoard board;
	private InetAddress ip;
	private int porta;

	
	public BattleBoardGUI(String p1, String p2, List<WaterCraft> craft, InetAddress ip, int porta, DatagramSocket socketCliente) throws IOException {
		this.player1 = p1;
		this.player2 = p2;
		this.board = new BattleBoard(10, ip, porta);
		this.ip = ip;
		this.porta = porta;
		init(craft);
		Thread t = new Thread(new ListenerInfo(ip, porta, socketCliente));
		t.start();
	}

	public void show(){
		createFrame();
		createJpMain();
		createJpPlayer1();
		createJpPlayer2();
		frame.pack();
		frame.setSize(1200, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void init(List<WaterCraft> craft){
		board.shuffle(craft);
	}
	
	private void createFrame(){
		frame = new JFrame("Battle Players");
	}
	
	private void createJpMain(){
		jpMain = (JPanel)frame.getContentPane();
		GridLayout layout = new GridLayout(1, 2);
		jpMain.setLayout(layout);
	}
	
	private void createJpPlayer1(){
		JPanel play1 = new JPanel();
		BoxLayout box  = new BoxLayout(play1, BoxLayout.Y_AXIS);
		play1.setLayout(box);
		GridLayout layout = new GridLayout(10,10);
		JPanel board = new JPanel(layout);
		String [][] imagens = this.board.getImages();
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				System.out.println(imagens[i][j]);
				Icon icone = new ImageIcon(System.getProperty("user.dir") + File.separator+"images"+File.separator+imagens[i][j]);
				JLabel lbl = new JLabel();
				lbl.setIcon(icone);
				lbl.setSize(59, 56);
				lbl.setVisible(true);
				board.add(lbl);
			}
		}
		JLabel lblPlayer1 = new JLabel(this.player1);
		play1.add(lblPlayer1);
		play1.add(board);
		jpMain.add(play1);
	}
	
	private void createJpPlayer2(){
		JPanel play2 = new JPanel();
		BoxLayout box  = new BoxLayout(play2, BoxLayout.Y_AXIS);
		play2.setLayout(box);
		GridLayout layout = new GridLayout(10,10);
		JPanel board = new JPanel(layout);
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				SquareOfTheBoard square = new SquareOfTheBoard(i, j, ip, porta);
				board.add(square.getPanel());
			}
		}
		JLabel lblPlayer1 = new JLabel(this.player2);
		play2.add(lblPlayer1);
		play2.add(board);
		jpMain.add(play2);
	}
	
	private void updateSquare(int row, int column, int status, String image){
		
		JPanel otherPlayer = (JPanel)jpMain.getComponent(1);
		JPanel boardOpponent = (JPanel)otherPlayer.getComponent(1);
		Component []  pos = boardOpponent.getComponents();
		JPanel obj = (JPanel)pos[row*this.board.getLenght()+column];
		
		String URL = System.getProperty("user.dir") + File.separator + "images" + File.separator + image;
		System.out.println(URL);
		Icon icon = new ImageIcon(URL);
		JLabel result = new JLabel(icon); 
		result.setSize(59, 56);
		result.setVisible(true);
		
		
		if(status == 0 || status == 1){
			obj.add(result);
			obj.setBackground(Color.red);
		}else if(status == -1){ 
			obj.add(result);
			obj.setBackground(Color.blue);
		}
	}
	
	private class ListenerInfo implements Runnable {
		
		private DatagramSocket socket;
		private InetAddress ip;
		private int porta;
		private boolean controle;
		private int tiros;
		
		public ListenerInfo(InetAddress ip, int porta, DatagramSocket socketCliente) throws IOException{
			socket = new DatagramSocket();
			this.ip = ip;
			this.porta = porta;
			this.socket = socketCliente;
			tiros = 0;
			controle = true;
			try{
				byte[] info = new byte[2048];
				String message = "comeca";
				info = message.getBytes();
				DatagramPacket send = new DatagramPacket(info, info.length, ip, porta); 
				socket.send(send);
				socket.setSoTimeout(1000);
				byte[] info1 = new byte[2048];
				DatagramPacket receive = new DatagramPacket(info1,
					info1.length);
				socket.receive(receive);
				JOptionPane.showMessageDialog(null, "Comece a partida!");
			} catch(SocketTimeoutException exp){
				controle = false;
				JOptionPane.showMessageDialog(null, "Aguarde o outro jogador escolher as embarcações!");
			}
			
			
		}
		@Override
		public void run() {

			while (true) {
				try {
					byte[] info = new byte[2048];
					DatagramPacket receive = new DatagramPacket(info,
							info.length);
					socket.setSoTimeout(0);
					socket.receive(receive);
					String message = new String(receive.getData());
					message = message.trim();
					String[] value = message.split(";");
					if(message.equals("SYN")){
						System.out.println("recebo SYN");
						message = "SYN_ACK";
						info = message.getBytes();
						DatagramPacket send = new DatagramPacket(info, info.length, ip, porta); 
						socket.send(send);
						System.out.println("Mando SYN_ACK");
						
						
						byte [] answer = new byte[2048]; 
						receive = new DatagramPacket(answer, answer.length); 
						System.out.println("Morro antes ?");
						socket.receive(receive);
						System.out.println("Ou depois ?");
						String value1 = new String(receive.getData());
						value1 = value1.trim(); 
						String [] datas = value1.split(";");
						int row = Integer.parseInt(datas[1]);
						int column = Integer.parseInt(datas[2]);
						int status = Integer.parseInt(datas[3]);
						String image = datas[4];
						int won = Integer.parseInt(datas[5]);
						updateSquare(row, column, status, image);
						tiros++;
						controle = false;
						if(won == 1){
							JOptionPane.showMessageDialog(null, "Voce ganhou");
							FimPartida fim = new FimPartida(tiros, socket, player2);
							fim.show();
							frame.dispose();
							info = "Fim".getBytes();
							send = new DatagramPacket(info, info.length, ip, porta);
							socket.send(send);
						}
					}
					if (value[0].equals("ATTACK") && !controle) {
						System.out.println("Recebi a mensagem");
						byte [] synMessage = "SYN".getBytes();
						DatagramPacket syn = new DatagramPacket(synMessage,synMessage.length, ip, porta);
						socket.send(syn);
						System.out.println("Mando SYN");
						
						socket.setSoTimeout(2000);
						byte [] backMessage = new byte[2048]; 
						DatagramPacket back = new DatagramPacket(backMessage, backMessage.length); 
						socket.receive(back);
						
						String synFinal = new String(back.getData());
						synFinal = synFinal.trim();
						if(synFinal.equals("SYN_ACK")){
							System.out.println("Recebo SYN_ACK");
							
							int row = Integer.parseInt(value[1].trim());
							int column = Integer.parseInt(value[2].trim());
							int status = board.attack(row, column);
							String image = board.getSingleImage(row, column);
							int won = board.getWon();
							String dados = "ANSWER;"+row+";"+column+";"+status+";"+image+";"+won;
							System.out.println(dados);
							backMessage = dados.getBytes(); 
							back = new DatagramPacket(backMessage, backMessage.length, ip, porta);
							socket.send(back);
							controle = true;
						}
					}else{
						if(value[0].equals("ATTACK") && controle){
							byte [] synMessage = "aguarde".getBytes();
							DatagramPacket syn = new DatagramPacket(synMessage,synMessage.length, ip, porta);
							socket.send(syn);
						}
					}
					if(message.equals("comeca")){
						byte [] synMessage = "ok".getBytes();
						DatagramPacket syn = new DatagramPacket(synMessage,synMessage.length, ip, porta);
						socket.send(syn);
					}else{
						if(message.equals("aguarde")){
							JOptionPane.showMessageDialog(null, "Aguarde a vez do adversario!");
						}else{
							if(message.equals("Fim")){
								JOptionPane.showMessageDialog(null, "Voce perdeu!");
								FimPartida fim = new FimPartida(tiros, socket, player2);
								fim.show();
								frame.dispose();
							}
						}
					}
				} catch (IOException e) {
					System.out.println("ERRO NO SQUARE");
				}
			}
		}
	}
	

	
}
