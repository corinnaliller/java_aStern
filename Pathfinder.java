package aStern;
import java.util.*;
/**
 * Die eigentliche Pfadberechnermethode
 * Start- und Zielknoten sowie Position der Hindernisse sind im Quelltext festgelegt. Können an dieser Stelle angepasst werden.
 * @author Die Pfadfinder (Liller, Machlis, Schaefer)
 *
 */


public class Pathfinder {
	/**
	 * Attribute
	 * Die Größe des Spielfeldes (x- und y-Spanne) werden definiert, der Startknoten wird definiert
	 * Offene, geschlossene und Pfad-Liste werden deklariert 
	 */
	Knoten start = new Knoten(0,0);
	Knoten ziel = new Knoten(15,11);
	private String[][] feld;
	private ArrayList <Knoten> openList;
	private ArrayList <Knoten> closedList;
	private ArrayList <Hindernis> hindernisse;
	private ArrayList <Knoten> pfad;
	private int feldX;
	private int feldY;
	private boolean zielErreicht = false;
	
	/**
	 * Konstruktor: Listen werden angelegt, Feldgröße werden definiert; Feld wird angelegt
	 * Der Startpunkt wird der offenen Liste hinzugefügt.
	 */
	public Pathfinder() {
		openList = new ArrayList <Knoten>();
		closedList = new ArrayList <Knoten>();
		pfad = new ArrayList <Knoten>();
		hindernisse = hindernisseHinzufuegen();
		feldX = 16;
		feldY = 12;
		feld = new String[feldY][feldX];
		openList.add(start);
	}
	
	/**
	 * Der eigentliche Programmablauf. Die Schleife bricht ab, wenn der Zielknoten in der offenen Liste auftaucht.
	 */
	public void wegfindung() {
		while(!zielErreicht) {
			lowestFValue();
		}
		zuEnde();
	}
	
	/**
	 * Wenn zielErreicht == true
	 */
	
	public void zuEnde() {
		System.out.println("Ziel erreicht!");
		System.out.println(closedList);
		pfadFinden();
		System.out.println(pfad);
		feldFuellen();
		pfadAusgeben();
	}
	
	/**
	 * Hindernisse werden gesetzt und in eine eigene Array-Liste gepackt
	 * @return die Liste mit den Hindernissen
	 */
	public ArrayList<Hindernis> hindernisseHinzufuegen() {
		ArrayList <Hindernis> hindernisListe = new ArrayList <Hindernis>();
		for (int i = 0; i < 6; i++) {
			Hindernis neu = new Hindernis(6,i+5);
			hindernisListe.add(neu);
		}
		
		return hindernisListe;
	}
	
	/**
	 * Diese Methode durchsucht die offene Liste nach dem niedrigsten F-Wert
	 * Der Knoten mit dem niedrigsten F-Wert wird untersucht.
	 * Diese Methode ruft die anderen Methoden auf.
	 */
	public void lowestFValue() {
		Knoten current = openList.get(0);
		
		int lowestF = openList.get(0).getfCost();
		for (int i = 0; i < openList.size(); i++) {
			if (openList.get(i).getfCost()<lowestF) {
				lowestF = openList.get(i).getfCost();
				current = openList.get(i);
			}
		}
		
		listenwechsel(current);
		nachbarn(current);
	}
	
	
	/**
	 * Der Knoten, der gerade untersucht wird, wird aus der offenen Liste entfernt und der geschlossenen hinzugefügt
	 * @param node
	 */
	public void listenwechsel(Knoten node) {
		closedList.add(node);
		openList.remove(node);
	}
	
	/**
	 * Diese Methode überprüft, ob es bereits ein Objekt mit der gleichen x- und y-Position in der geschlossenen Liste gibt
	 * @param das zu testende Objekt
	 * @return wahr oder falsch
	 */
	public boolean notInClosed(Knoten test) {
		boolean moeglich = true;
		for (int i = 0; i < closedList.size();i++) {
			if (closedList.get(i).getPosX() == test.getPosX() && closedList.get(i).getPosY() == test.getPosY()) {
				moeglich = false;
			}
		}
		return moeglich;
	}
	
	/**
	 * Diese Methode überprüft, ob es bereits ein Objekt an der gleichen x- und y-Position in der offenen Liste gibt.
	 * Wenn ja, wird überprüft, ob das gerade erzeugte einen besseren F-Wert hat als das vorhandene.
	 * Wenn ja, wird das alte entfernt und True zurückgegeben; wenn nein, wird False zurückgegeben.
	 * Wenn kein solches Objekt schon in der Liste ist, wird True zurückgegeben.
	 * @param test
	 * @return wahr oder falsch
	 */
	public boolean optimalInOpen(Knoten test) {
		boolean moeglich = true;
		for (int i = 0; i < openList.size();i++) {
			if (openList.get(i).getPosX() == test.getPosX() && openList.get(i).getPosY() == test.getPosY()) {
				if (test.getfCost()<openList.get(i).getfCost()) {
					openList.remove(i);
					moeglich = true;
				}
				else {
					moeglich = false;
				}
			}
			else {
				moeglich = true;
			}
		}
		return moeglich;
	}
	
	/**
	 * Methode, die alle acht potentiellen Nachbarknoten eines bestimmten Knoten anlegt.
	 * @param zu testender Knoten
	 */
	public void nachbarn(Knoten test) {
		/*
		 * Veränderungen der x- und y-Positionen
		 * nord = darüber, süd = darunter, ost = rechts, west = links
		 */
		int nordY = test.getPosY()-1;
		int suedY = test.getPosY()+1;
		int ostX = test.getPosX()+1;
		int westX = test.getPosX()-1;
		
		/*
		 * Wenn die Stelle auf dem Feld ist und nicht von einem Hindernis besetzt, wird ein Knoten angelegt.
		 * Wenn dieser Knoten nicht bereits besser in der offenen Liste vorhanden ist und noch nicht in der geschlossenen Liste
		 * abgelegt wurde, wird er der offenen Liste hinzugefügt.
		 * Wenn der neu angelegte Knoten an der gleichen Position ist wie der Zielknoten, wird die Variable Ziel überschrieben
		 * und zielErreicht auf wahr gesetzt, außerdem
		 */
		
		if (begehbar(test.getPosX(), nordY)) {
			Knoten nord = new Knoten(test.getPosX(), nordY, test.getPosX(), test.getPosY(), test.getgCost());
			if (optimalInOpen(nord)&&notInClosed(nord)) {
				openList.add(nord);
				if (nord.getPosX() == ziel.getPosX() && nord.getPosY() == ziel.getPosY()) {
					ziel = nord;
					zielErreicht = true;
				}
			}
		}
		if (begehbar(ostX, nordY)) {
			Knoten nordost = new Knoten(ostX, nordY, test.getPosX(), test.getPosY(), test.getgCost());
			if (optimalInOpen(nordost)&&notInClosed(nordost)) {
				openList.add(nordost);
				if (nordost.getPosX() == ziel.getPosX() && nordost.getPosY() == ziel.getPosY()) {
					ziel = nordost;
					zielErreicht = true;
				}
			}
		}
		if(begehbar(westX, nordY)) {
			Knoten nordwest = new Knoten(westX, nordY, test.getPosX(), test.getPosY(), test.getgCost());
			if (optimalInOpen(nordwest)&&notInClosed(nordwest)) {
				openList.add(nordwest);
				if (nordwest.getPosX() == ziel.getPosX() && nordwest.getPosY() == ziel.getPosY()) {
					ziel = nordwest;
					zielErreicht = true;
				}
			}
		}
		if(begehbar(ostX, test.getPosY())) {
			Knoten ost = new Knoten(ostX, test.getPosY(), test.getPosX(), test.getPosY(), test.getgCost());
			if (optimalInOpen(ost)&&notInClosed(ost)) {
				openList.add(ost);
				if (ost.getPosX() == ziel.getPosX() && ost.getPosY() == ziel.getPosY()) {
					ziel = ost;
					zielErreicht = true;
				}
			}
		}
		if(begehbar(westX, test.getPosY())) {
			Knoten west = new Knoten(westX, test.getPosY(), test.getPosX(), test.getPosY(), test.getgCost());
			if (optimalInOpen(west)&&notInClosed(west)) {
				openList.add(west);
				if (west.getPosX() == ziel.getPosX() && west.getPosY() == ziel.getPosY()) {
					ziel = west;
					zielErreicht = true;
				}
			}
		}
		if(begehbar(test.getPosX(), suedY)) {
			Knoten sued = new Knoten(test.getPosX(), suedY, test.getPosX(), test.getPosY(), test.getgCost());
			if (optimalInOpen(sued)&&notInClosed(sued)) {
				openList.add(sued);
				if (sued.getPosX() == ziel.getPosX() && sued.getPosY() == ziel.getPosY()) {
					ziel = sued;
					zielErreicht = true;
				}
			}
		}
		if(begehbar(ostX, suedY)) {
			Knoten suedost = new Knoten(ostX, suedY, test.getPosX(), test.getPosY(), test.getgCost());
			if (optimalInOpen(suedost)&&notInClosed(suedost)) {
				openList.add(suedost);
				if (suedost.getPosX() == ziel.getPosX() && suedost.getPosY() == ziel.getPosY()) {
					ziel = suedost;
					zielErreicht = true;
				}
			}
		}
		if(begehbar(westX, suedY)) {
			Knoten suedwest = new Knoten(westX, suedY, test.getPosX(), test.getPosY(), test.getgCost());
			if (optimalInOpen(suedwest)&&notInClosed(suedwest)) {
				openList.add(suedwest);
				if (suedwest.getPosX() == ziel.getPosX() && suedwest.getPosY() == ziel.getPosY()) {
					ziel = suedwest;
					zielErreicht = true;
				}
			}
		}	
	}
	
	/**
	 * Diese Methode überprüft, ob das gewünschte Feld überhaupt existiert und nicht von einem Hindernis belegt ist
	 * @param x
	 * @param y
	 * @return wahr oder falsch
	 */
	public boolean begehbar(int x, int y) {
		boolean moeglich = true;
		if (x < 0 || x >= feldX ||y < 0 || y >= feldY) {
			moeglich = false;
		}
		for (int i = 0; i < hindernisse.size();i++) {
			if(hindernisse.get(i).getPosX() == x && hindernisse.get(i).getPosY() == y) {
				moeglich = false;
			}
		}
		return moeglich;
	}
	
	/**
	 * Geht das Spielfeld und alle Listen durch und setzt Zeichen für die Vertreter der unterschiedlichen Listen
	 * S = Startknoten;
	 * Z = Zielknoten;
	 * # = Hindernis;
	 * o = offene Liste;
	 * c = geschlossene Liste;
	 * p = Pfad zum Ziel;
	 */
	public void feldFuellen() {
		for (int i = 0; i < feldY; i++) {
			for (int j = 0; j < feldX; j++) {
				for (int h = 0; h < hindernisse.size(); h++) {
					if (hindernisse.get(h).getPosX() == j && hindernisse.get(h).getPosY() == i) {
						feld[i][j] = "#";
					}
				}
			}
		}
		//offene Liste
		for (int i = 0; i < feldY; i++) {
			for (int j = 0; j < feldX; j++) {
				for (int h = 0; h < openList.size(); h++) {
					if (openList.get(h).getPosX() == j && openList.get(h).getPosY() == i) {
						feld[i][j] = "o";
					}
				}
			}
		}
		//geschlossene Liste
		for (int i = 0; i < feldY; i++) {
			for (int j = 0; j < feldX; j++) {
				for (int h = 0; h < closedList.size(); h++) {
					if (closedList.get(h).getPosX() == j && closedList.get(h).getPosY() == i) {
						feld[i][j] = "c";
					}
				}
			}
		}
		//Pfad
		for (int i = 0; i < feldY; i++) {
			for (int j = 0; j < feldX; j++) {
				for (int h = 0; h < pfad.size(); h++) {
					if (pfad.get(h).getPosX() == j && pfad.get(h).getPosY() == i) {
						feld[i][j] = "p";
					}
				}
			}
		}
		int zielX = ziel.getPosX();
		int zielY = ziel.getPosY();
		feld[zielY] [zielX] = "Z";
		int startX = start.getPosX();
		int startY = start.getPosY();
		feld[startY] [startX] = "S";
		
	}
	
	/**
	 * Das Feld in der Konsole anzeigen
	 */
	public void pfadAusgeben() {
		for (int i = 0; i < feldY; i++) {
			for (int j = 0; j < feldX; j++) {
				System.out.print(feld[i][j]+"\t");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * Durchsucht die geschlossene Liste nach dem Knoten, der an der gleichen Position liegt wie der Elter
	 * des untersuchten Knotens
	 * @param x-Wert des Elternknotens
	 * @param y-Wert des Elternknotens
	 * @return den Elternknoten
	 */
	public Knoten getParentInClosed(int x, int y) {
		Knoten parent = new Knoten(closedList.get(0).getPosX(),closedList.get(0).getPosY(),closedList.get(0).getparentX(), closedList.get(0).getparentY(),closedList.get(0).getparentG());;
		for (int i = 0; i < closedList.size(); i++) {
			if(closedList.get(i).getPosX() == x && closedList.get(i).getPosY() == y) {
				parent = closedList.get(i);
			}
		}
		return parent;
	}
	
	/**
	 * Geht vom Ziel aus rückwärts durch die geschlossene Liste und packt alle Knoten, die aufeinander zeigen in eine
	 * frische Liste
	 */
	public void pfadFinden() {
		for (int i = 0; i < openList.size(); i++) {
			if (openList.get(i).getPosX() == ziel.getPosX() && openList.get(i).getPosY() == ziel.getPosY()) {
				Knoten neuKnoten = getParentInClosed(openList.get(i).getparentX(),openList.get(i).getparentY());
				pfad.add(neuKnoten);
				System.out.println("Ziel hinzugefügt");
			}
		}
		
		for (int i = 0; i < pfad.size(); i++) {
			if (pfad.get(i).getparentX()==start.getPosX() && pfad.get(i).getparentY() == start.getPosY()) {
				System.out.println("Pfad fertig");
				i = pfad.size();
			}
			else {
				Knoten neuKnoten = getParentInClosed(pfad.get(i).getparentX(),pfad.get(i).getparentY());
				pfad.add(neuKnoten);
				System.out.println("Pfad hinzugefügt");
			}
		}
		
	}
	
	public static void main(String[] args) {
		Pathfinder aStern = new Pathfinder();
		
		aStern.wegfindung();
	}
	

}
