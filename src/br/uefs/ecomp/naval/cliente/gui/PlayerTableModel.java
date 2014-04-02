package br.uefs.ecomp.naval.cliente.gui;



import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class PlayerTableModel extends AbstractTableModel {

	private List<String> players;

	public PlayerTableModel(List<String> newPlayers) {
		this.players = new ArrayList<>();
		if (newPlayers != null)
			this.players.addAll(newPlayers);
	}

	public int getRowCount() {
		return players.size();
	}

	public int getColumnCount() {
		return 1;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= 0 && rowIndex < players.size() && columnIndex == 0) {
			return players.get(rowIndex);
		} else {
			return null;
		}
	}

	public void addPlayers(List<String> newPlayers) {
		this.players.addAll(newPlayers);
	}

	public void removePlayers(List<String> oldPlayers) {
		this.players.removeAll(oldPlayers);
	}
}
