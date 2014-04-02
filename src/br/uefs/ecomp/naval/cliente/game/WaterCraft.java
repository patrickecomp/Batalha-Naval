package br.uefs.ecomp.naval.cliente.game;



import java.util.HashMap;

public class WaterCraft {

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	private final int amountOfSquares;
	private int attack;
	private final String name;
	private final String nameImage;
	private HashMap<String, String> images;
	private int orientation;

	public WaterCraft(String name, int amountOfSquares, String nameImage,
			int orientation) {
		this.name = name;
		this.amountOfSquares = (amountOfSquares < 1) ? 1 : amountOfSquares;
		this.attack = 0;
		this.orientation = orientation;
		this.nameImage = nameImage;
		this.images = new HashMap<>();
	}

	public String getName() {
		return name;
	}

	public int getAmountOfSquares() {
		return amountOfSquares;
	}

	public void addAttack() {
		if (attack < amountOfSquares){
			this.attack++;
		}
	}

	public int getStatus() {
		if (attack > 0 && attack < amountOfSquares) {
			return 0;
		} else if (attack == amountOfSquares) {
			return 1;
		} else {
			return -1;
		}
	}

	public int getOrientation() {
		return this.orientation;
	}

	public void addImage(int row, int column) {
		String key = row+";"+column;
		int part = images.size();
		String namePart = nameImage+part;
		images.put(key, namePart);
	}

	public String getImage(int row, int column){
		String key = row+";"+column;
		if(images.containsKey(key))
			return images.get(key);
		else 
			return "SEM";
	}
	
	public void setImage(int row, int column){
		String key = row+";"+column;
		if(images.containsKey(key)){
			String oldImage = images.get(key); 
			String image = "fogo";
			System.out.println("Old => "+oldImage+ " Image = "+image);
		}
	}
}
