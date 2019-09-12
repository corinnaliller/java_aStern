package aStern;
/**
 * Die Klasse Hindernis. Hat einen Konstruktor und eine x-/y-Position und kann nichts außer im Weg sein.
 * @author Die Pfadfinder (Liller, Machlis, Schaefer)
 *
 */
public class Hindernis {
	private int posX;
	private int posY;
	
	public Hindernis(int pPosX, int pPosY) {
		if (pPosX >= 0 && pPosX < 16) {
			posX = pPosX;
		}
		else {
			posX = 10;
		}
		if (pPosX >= 0 && pPosX < 12) {
			posY = pPosY;
		}
		else {
			posY = 0;
		}
		
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}
	

}
