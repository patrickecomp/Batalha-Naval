package br.uefs.ecomp.naval.servidor;

public class PacoteSincronizado {
	
	private Player jogador;
	private boolean receber = true;
	
	public synchronized void receberPacote(Player jogador){
		while(!receber){
			try{
				wait();
			}catch(InterruptedException excecao){
				excecao.printStackTrace();
			}
		}
		this.jogador = jogador;
		System.out.println("Recipiente recebeu");
		receber = false;
		notify();
	}
	
	public synchronized Player entregarPacote(){
		while(receber){
			try{
				wait();
			}catch(InterruptedException excecao){
				excecao.printStackTrace();
			}
		}
		notifyAll();
		receber = true;
		return jogador;
	}
}
