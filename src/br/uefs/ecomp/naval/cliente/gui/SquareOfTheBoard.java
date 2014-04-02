package br.uefs.ecomp.naval.cliente.gui;



import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SquareOfTheBoard {

	private JPanel panel;
	private int row;
	private int column;
	private InetAddress ip;
	private int porta;

	public SquareOfTheBoard(int i, int j, InetAddress ip, int porta) {
		this.row = i;
		this.column = j;
		this.ip = ip;
		this.porta = porta;
		createSquare();
	}

	public JPanel getPanel() {
		return panel;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	private void createSquare() {
		panel = new JPanel();
		panel.setBackground(Color.gray);
		panel.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
		panel.addMouseListener(new SquareClicked());
	}

	private class SquareClicked extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {

			String message = "ATTACK;" + row + ";" + column;
			try {
				DatagramSocket socket = new DatagramSocket();
				byte[] data = message.getBytes();
				DatagramPacket sendData = new DatagramPacket(data, data.length,
						ip, porta);
				System.out.println("MANDEI ATTACK");
				System.out.println(row+" "+column);
				socket.send(sendData);
			} catch (SocketException e1) {
				JOptionPane.showMessageDialog(null,
						"There is a problem in connection. Please try again",
						"ERROR: Connection failed", JOptionPane.ERROR_MESSAGE);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	}

}
