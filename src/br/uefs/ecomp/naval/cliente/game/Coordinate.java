package br.uefs.ecomp.naval.cliente.game;


import java.io.Serializable;

public class Coordinate implements Serializable{

	private int row;
	private int column;

	public Coordinate(int r, int c) {
		this.row = r;
		this.column = c;
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
}
