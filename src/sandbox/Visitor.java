package sandbox;

/**
 * Kezeli az al� beosztott adatstrukt�r�t. Az al� rendelt objektum met�dusait h�vja, hozz�f�r�st biztos�t �s elrejt m�s oszt�lyokt�l.
 * Lehet�v� teszi, hogy az ezt megval�s�t� oszt�lyok s�n oszt�lyt, vagy abb�l lesz�rmaz� oszt�lyokat l�togassanak meg.
 */
public interface Visitor {
	
	/**
	 * A sima s�n megl�togat�s�ra szolg�l� f�ggv�ny. Ennek seg�ts�g�vel a vonat r� tud l�pni a sima sinekre.
	 * @param rail	A megl�togatand� s�n
	 */
	public void visit(Rail rail);
	
	
	/**
	 * A v�lt�k megl�togat�s�ra szolg�l� f�ggv�ny. Ennek seg�ts�g�vel a vonat r� tud l�pni a v�lt�kra.
	 * @param rail A megl�togatand� v�lt�
	 */
	public void visit(Switch rail);
	
	
	/**
	 * Az alag�t sz�j megl�togat�s�ra szolg�l� f�ggv�ny. Ennek seg�ts�g�vel a vonat r� tud l�pni az alag�tsz�jakra.
	 * @param rail A megl�togatand� alag�tsz�j 
	 */
	public void visit(TunnelEntrance rail);
	
	
	/**
	 * Az alag�t megl�togat�s�ra szolg�l� f�ggv�ny. Ennek seg�ts�g�vel a vonat r� tud l�pni az alag�tban l�v� s�nre.
	 * @param rail A megl�togatand� alag�t
	 */
	public void visit(Tunnel rail);
	
	
	/**
	 * �llom�s megl�togat�s�ra szolg�l� f�ggv�ny. Ennek seg�ts�g�vel a vonat r� tud l�pni az �llom�sokra.
	 * @param rail A megl�togatand� �llom�s
	 */
	public void visit(TrainStation rail);
}
