package sandbox;

/**
 * Kezeli az alá beosztott adatstruktúrát. Az alá rendelt objektum metódusait hívja, hozzáférést biztosít és elrejt más osztályoktól.
 * Lehetõvé teszi, hogy az ezt megvalósító osztályok sín osztályt, vagy abból leszármazó osztályokat látogassanak meg.
 */
public interface Visitor {
	
	/**
	 * A sima sín meglátogatására szolgáló függvény. Ennek segítségével a vonat rá tud lépni a sima sinekre.
	 * @param rail	A meglátogatandó sín
	 */
	public void visit(Rail rail);
	
	
	/**
	 * A váltók meglátogatására szolgáló függvény. Ennek segítségével a vonat rá tud lépni a váltókra.
	 * @param rail A meglátogatandó váltó
	 */
	public void visit(Switch rail);
	
	
	/**
	 * Az alagút száj meglátogatására szolgáló függvény. Ennek segítségével a vonat rá tud lépni az alagútszájakra.
	 * @param rail A meglátogatandó alagútszáj 
	 */
	public void visit(TunnelEntrance rail);
	
	
	/**
	 * Az alagút meglátogatására szolgáló függvény. Ennek segítségével a vonat rá tud lépni az alagútban lévõ sínre.
	 * @param rail A meglátogatandó alagút
	 */
	public void visit(Tunnel rail);
	
	
	/**
	 * Állomás meglátogatására szolgáló függvény. Ennek segítségével a vonat rá tud lépni az állomásokra.
	 * @param rail A meglátogatandó állomás
	 */
	public void visit(TrainStation rail);
}
