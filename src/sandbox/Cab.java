package sandbox;

import java.awt.Color;

/**
 * Egyedi színnel rendelkezõ vagon, mely utasokat szállít a számukra megfelelõ (azonos színû) állomásra a megfelelõ sorrendben. 
 * Egy vonatnak tetszõleges számú kabinja lehet. A játék megnyerésének a feltétele ezen vagonok kiürítése. 
 * Mikor a vonat visitel egy állomást (vagyis a mozdony az állomáshoz ér), megnézi, hogy melyik a legelsõ olyan vagon aminek még vannak utasai. 
 * Megnézi, hogy megegyezik-e ennek a vagonnak a színe a színe az állomáséval, és ha igen, akkor kiüríti a vagont (emptyCab).
 */
public class Cab extends TrainElement{
	protected Boolean hasPassenger;/* Ebben a változóban tároljuk, hogy az adott vagonban vannak-e még utasok. True ha vannak benne utasok, false ha nincsenek. */
	
	
	/**
	 * A kabin konstruktora. 
	 * 
	 * @param cabColor	Az adott kabin színe.
	 */
	public Cab(Color cabColor){
		System.out.println("Class: Cab\t\t Object: "+this+"\t\t\t Method: Constructor\t Param: "+cabColor); /* Kiíratás a Szkeleton vezérlésének */
		hasPassenger = true; /* Kezdetben minden kabint beállítunk, hogy utassal teli */
		color = cabColor; /* A kabin színét beállítjuk a megadott színre. */
	}
	
	
	/**
	 * Megadja, hogy vannak-e utasok az adott vagonban. Amennyiben vannak, úgy true-val amennyiben nincsenek, úgy false-al tér vissza.
	 * 
	 * @return	Van-e utas a kabinban.
	 */
	public Boolean isFull(){
		System.out.println("Class: Cab\t\t Object: "+this+"\t\t\t Method: isFull\t Van-e utasa a vagonnak?"); /* Kiíratás a Szkeleton vezérlésének */
		System.out.println("Class: Cab\t\t Object: "+this+"\t\t\t Returned: "+hasPassenger); /* Kiíratás a Szkeleton vezérlésének */
		return hasPassenger; 
	}
	
	
	/** 
	 * Kiüríti a kabint. A játék célja, hogy minden utas leugorjon a saját színû állomásán. 
	 * Ehhez ha egy kabin kiüríthetõ állapotban van - azaz a mozdonytól hátrafelé nézve õ az elsõ kabin ahol még vannak utasok, és a színe megegyezik  az állomás színével - 
	 * akkor ennek a függvények a meghívásával lehet kiüríteni az utasokat az adott megállónál. 
	 */
	public void emptyCab(){
		System.out.println("Class: Cab\t\t Object: "+this+"\t\t\t Method: emptyCab\t Kiürül a vagon."); /* Kiíratás a Szkeleton vezérlésének */
		hasPassenger = false;
	}
	
	/**
	 * Ha a kocsink üres lehetöségünk van felszállítani rá utasokat.
	 * A függvény feladatoa, hogy amennyiben fel tudja rá tenni az utasokat felrakja.
	 * Ez akkor lehetséges ha a vagon üres és az utasok színe megegyezik a vonatével.
	 */
	
	public boolean addPassenger(Color passengersColor){
		if(passengersColor==color && !hasPassenger){ /*Ha a szín egyezik és nincs utas*/
			hasPassenger=true;  					/*Felültetjük*/
			return true;							/*Jelezzük hogy sikerült*/
		}
		return false;								/*Jelezzük hogy nem sikerült*/
	}
	
}
