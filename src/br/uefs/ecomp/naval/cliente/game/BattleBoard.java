package br.uefs.ecomp.naval.cliente.game;


import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleBoard {

	private WaterCraft[][] board;
	private List<WaterCraft> ships;
	private int length;

	public BattleBoard(int length, InetAddress ip, int porta) throws SocketException {
		this.length = length;
		board = new WaterCraft[length][length];
		this.ships = new ArrayList<>();
	}

	public void shuffle(List<WaterCraft> ships) {
		this.ships.addAll(ships);
		Random random = new Random();
		int value, index;
		for (int i = (ships.size() - 1); i >= 0; i = (ships.size() - 1)) {

			WaterCraft ship = ships.get(i);
			value = random.nextInt(length);
			if (ship.getOrientation() == 0
					&& (index = verifyPositionHorizontal(value,
							ship.getAmountOfSquares())) != -1) {
				for (int j = index; j <  length && j < (index + ship.getAmountOfSquares()); j++) {
					board[value][j] = ship;
					ship.addImage(value, j);
				}
				ships.remove(i);
			} else if (ship.getOrientation() == 1
					&& (index = verifyPositionVertical(value,
							ship.getAmountOfSquares())) != -1) {
				for (int row = index; row < length && row < (index + ship.getAmountOfSquares()); row++) {
					board[row][value] = ship;
					ship.addImage(row, value);
				}
				ships.remove(i);
			}
		}
	}

	private int verifyPositionHorizontal(int ind, int numberSquares) {
		int column = 0;
		boolean isValid = false;
		for (; column < this.length; column++) {
			for (int j = column; j < length && j < (column + numberSquares); j++) {
				if (verifyColumn(j, ind) && verifyRow(j, ind)) {
					isValid = true;
				} else {
					isValid = false;
					break;
				}
			}
			if (isValid)
				return column;
		}
		return -1;
	}

	private int verifyPositionVertical(int ind, int numberSquares) {
		int row = 0;
		boolean isValid = false;
		for (; row < this.length; row++) {
			for (int i = row; i < length && i < (row + numberSquares); i++) {
				if (verifyColumn(ind, i) && verifyRow(ind, i)) {
					isValid = true;
				} else {
					isValid = false;
					break;
				}
			}
			if (isValid)
				return row;
		}
		return -1;
	}

	private boolean verifyColumn(int column, int row) {
		if (column != 0 && column != (length - 1)) {
			if (board[row][column - 1] != null
					|| board[row][column + 1] != null) {
				return false;
			} else {
				return true;
			}
		} else if (column == 0) {
			if (board[row][column + 1] != null) {
				return false;
			} else {
				return true;
			}
		} else if (column == (length - 1)) {
			if (board[row][column - 1] != null) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	private boolean verifyRow(int column, int row) {
		if (row != 0 && row != (length - 1)) {
			if (board[row - 1][column] != null
					|| board[row + 1][column] != null) {
				return false;
			} else {
				return true;
			}
		} else if (row == 0) {
			if (board[row + 1][column] != null) {
				return false;
			} else {
				return true;
			}
		} else if (row == (length - 1)) {
			if (board[row - 1][column] != null) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	public int getWon(){
		for(WaterCraft s : ships){
			if(s.getStatus() != 1)
				return 0;
		}
		return 1;
	}
	public int attack(int row, int column) {
	
		if(board[row][column] != null ) {
			if(board[row][column].getStatus() != 1){
				board[row][column].addAttack();
				board[row][column].setImage(row, column);
			}
			return board[row][column].getStatus();
		}else{
			return -1;
		}
	}
	public String[][] getImages(){
		String [][] imagens = new String[length][length];
		for(int i = 0; i < length; i++){
			for(int j = 0;  j < length; j++){
				if(board[i][j] != null){
					imagens[i][j] = board[i][j].getImage(i, j)+".jpg"; 
				}else 
					imagens[i][j] = "agua.jpg";
			}
		}
		return imagens;
	}
	
	public String getSingleImage(int row, int column){
		if(board[row][column] != null)
			return board[row][column].getImage(row, column)+".jpg";
		else 
			return "agua.jpg";
	}
	
	public int getLenght(){
		return this.length;
	}




}
