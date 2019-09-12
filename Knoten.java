package aStern;

/**
 * Die Klasse Knoten hat eigene x- und y-Positionen, weiß an welcher x- und y-Position sein Elternknoten sitzt,
 * berechnet seinen g-Wert (Abstand vom Start), h-Wert (ungefährer Abstand vom Ziel) und f-Wert (beides zusammengerechnet)
 * @author Die Pfadfinder (Liller, Machlis, Schaefer)
 *
 */

public class Knoten {
	private int posX;
	private int posY;
	private int gCost;
	private int hCost;
	private int fCost;
	private int parentX;
	private int parentY;
	private int parentG;
	
	/**
	 * Konstruktor, der auch den Zeiger auf den Elter (x- und y-Position und G-Wert) beinhaltet
	 * @param pPosX
	 * @param pPosY
	 * @param pParentX
	 * @param pParentY
	 * @param pParentG
	 * Die Werte werden sofort berechnet
	 */
	public Knoten(int pPosX, int pPosY, int pParentX, int pParentY, int pParentG) {
		posX = pPosX;
		posY = pPosY;
		parentX = pParentX;
		parentY = pParentY;
		parentG = pParentG;
		calculateG();
		calculateH();
		calculateF();
	}
	/**
	 * Anderer Konstruktor, der nur am Anfang für Start und Ziel genutzt wird.
	 * @param pPosX
	 * @param pPosY
	 */
	public Knoten(int pPosX, int pPosY) {
		posX = pPosX;
		posY = pPosY;
		parentG = 0;
	}
	
	/**
	 * Berechnet den G-Wert; dafür nimmt er den Wert des Elter und addiert 10 (gerade) oder 14 (diagonal)
	 * @return G-Wert
	 */
	public int calculateG() {
		if (posX == parentX || posY == parentY) {
			gCost = parentG + 10;
			//gCost = 10;
		}
		else {
			gCost = parentG + 14;
			//gCost = 14;
		}
		return gCost;
	}
	
	/**
	 * Berechnet den H-Wert grob Anhand der Euklidischen Methode
	 * @return H-Wert
	 */
	public int calculateH() {
		Pathfinder problem = new Pathfinder();
		int zielX = problem.ziel.getPosX();
		int zielY = problem.ziel.getPosY();
		int xWert = Math.abs(posX - zielX)*10;
		int yWert = Math.abs(posY - zielY)*10;
		
		hCost = (int) Math.sqrt((xWert*xWert)+(yWert*yWert));
		return hCost;
	}
	
	/**
	 * Addiert G- und H-Werte zum F-Wert
	 * @return F-Wert
	 */
	public int calculateF() {
		fCost = gCost+hCost;
		return fCost;
	}
	
	public String toString() {
		return "X: "+posX+", Y: "+posY+"; G: "+gCost+", H: "+hCost+", F: "+fCost+"; ParentX: "+parentX+"; ParentY: "+parentY+"\n";
	}
	
	/*
	 * Getters und Setters - automatisch generiert
	 */
	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getgCost() {
		return gCost;
	}

	public void setgCost(int gCost) {
		this.gCost = gCost;
	}

	public int gethCost() {
		return hCost;
	}

	public void sethCost(int hCost) {
		this.hCost = hCost;
	}

	public int getfCost() {
		return fCost;
	}

	public void setfCost(int fCost) {
		this.fCost = fCost;
	}

	public int getparentX() {
		return parentX;
	}

	public void setparentX(int parentX) {
		this.parentX = parentX;
	}

	public int getparentY() {
		return parentY;
	}

	public void setparentY(int parentY) {
		this.parentY = parentY;
	}
	
	public int getparentG() {
		return parentG;
	}

}
