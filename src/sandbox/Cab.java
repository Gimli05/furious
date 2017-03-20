package sandbox;

import java.awt.Color;

/**
 * Egyedi színnel rendelkezõ vagon, mely utasokat szállít a számukra megfelelõ (azonos színû) állomásra a megfelelõ sorrendben. 
 * Egy vonatnak tetszõleges számú kabinja lehet. A játék megnyerésének a feltétele ezen vagonok kiürítése. 
 * Mikor a vonat visitel egy állomást (vagyis a mozdony az állomáshoz ér), megnézi, hogy melyik a legelsõ olyan vagon aminek még vannak utasai. 
 * Megnézi, hogy megegyezik-e ennek a vagonnak a színe a színe az állomáséval, és ha igen, akkor kiüríti a vagont (emptyCab).
 */
public class Cab extends TrainElement{
	private Boolean hasPassenger;/* Ebben a változóban tároljuk, hogy az adott vagonban vannak-e még utasok. True ha vannak benne utasok, false ha nincsenek. */
	
	
	/**
	 * A kabin konstruktora. 
	 * @param cabColor	Az adott kabin színe.
	 */
	public Cab(Color cabColor){
		System.out.println("Class: Cab\t Method: Constructor\t Param: cabColor");
		hasPassenger = true; /* Kezdetben minden kabint beállítunk, hogy utassal teli */
		color = cabColor; /* A kabin színét beállítjuk a megadott színre. */
	}
	
	
	/**
	 * Megadja, hogy vannak-e utasok az adott vagonban. Amennyiben vannak, úgy true-val amennyiben nincsenek, úgy false-al tér vissza.
	 * 
	 * @return	Van-e utas a kabinban.
	 */
	public Boolean isFull(){
		System.out.println("Class: Cab\t Method: isFull\t Param: -\t Van-e utasa a vagonnak?");
		return hasPassenger; 
	}
	
	
	/** 
	 * Kiüríti a kabint. A játék célja, hogy minden utas leugorjon a saját színû állomásán. 
	 * Ehhez ha egy kabin kiüríthetõ állapotban van - azaz a mozdonytól hátrafelé nézve õ az elsõ kabin ahol még vannak utasok, és a színe megegyezik  az állomás színével - 
	 * akkor ennek a függvények a meghívásával lehet kiüríteni az utasokat az adott megállónál. 
	 */
	public void emptyCab(){
		System.out.println("Class: Cab\t Method: emptyCab\t Param: -\t Kiürül a vagon.");
		hasPassenger = false;
	}
	
}
