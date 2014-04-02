package br.uefs.ecomp.naval.servidor;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Servidor extends JFrame{
	
	private JTextArea area;//Crio uma area para ver as ações do servidor em uma interface
	
	public Servidor(){
		area = new JTextArea("Servidor iniciado. \n");
		area.setEnabled(false);
		
		getContentPane().add(area, BorderLayout.CENTER);
		setResizable(false);
		setSize(600, 600);
		setVisible(true);
	}
	public static void main(String args[]) throws Exception       {
		
		PacoteSincronizado pac = new PacoteSincronizado(); 
		DatagramSocket SocketServidor2 = new DatagramSocket(7654);
		DatagramSocket SocketServidor = new DatagramSocket(6789);
		Servidor server = new Servidor();
		Ping listaOnline = new Ping(SocketServidor);
		final Ouvinte carteiro = new Ouvinte(pac, SocketServidor2);
		final Gerenciador buscaCadastra = new Gerenciador(pac, SocketServidor2);
		carteiro.start();
		buscaCadastra.start();
		listaOnline.start();
		server.addWindowFocusListener(new WindowAdapter(){
			public void windowClosing(WindowEvent evento){
				carteiro.interrupt();
				buscaCadastra.interrupt();
				System.exit(0);
			}
		});

	}
	
}
