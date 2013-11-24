package utils.verbos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import utils.Debug;

public class VerbosIrregulares {
	private static boolean DEBUG_ERROR = true;   // Muestra en consola posibles errores de ficheros
	private static boolean DEBUG_LOAD = false;   // Muestra en consola el contador de verbos tras la carga
	
	private static TreeSet<Conjugacion> conjs = init();
	public static Iterator<Conjugacion> iterator() {
		return conjs.iterator();
	}

	private static final float CONFIANZA_EXACTA = 1.0F;
	private static final float CONFIANZA_SIN_TILDES = 0.75F;
	private static TreeSet<Conjugacion> init() {
		TreeSet<Conjugacion> result = new TreeSet<Conjugacion>();

		// It would be like...
		// result.add( new Conjugacion( "haber", 1, false, "presente", false, "" , "he" ) );
		// ...
		// ...but we load from file
		
		String fileName = "verbos\\irregularesModelos.dat";
        File irrFile = new File(fileName);
    	if (!irrFile.exists()) {
    		// Something to advise user
    		if (DEBUG_ERROR) utils.Debug.show( "File not found: verbos/irregularesModelos.dat" );
    	} else {
    	    try {
		        BufferedReader input =  new BufferedReader(new FileReader(irrFile));
		        String line = null;
		        int conjCount = 0; int addCount = 0;
		        while ((line = input.readLine()) != null) {
		        	// Ini line process
		        	// System.out.println( line );
		        	line.trim();
					if (line.startsWith("//") || line.trim().equals("")) {   // Comment
					} else {
						String[] data = line.split(",");
						if (data.length < 8) {  // Line data error
						} else {
							// data[1] from String to int
							int i = 0;
							try { Integer ii = new Integer( data[2] ); i = ii.intValue(); } catch (Exception e) { }
							Conjugacion c = new Conjugacion( data[0], data[1], i, (data[3].equalsIgnoreCase("P")),
									data[4], (data[5].equalsIgnoreCase("subjuntivo")), data[6], data[7], 1.0F );
					        conjCount++;
							if (!result.add( c )) {   // Ya existe en set
								if (DEBUG_ERROR) utils.Debug.show( "L�nea con conjugaci�n duplicada o no existente: $" + line +"$" );
								addCount++;
							}
						}
					}
		        }
		        if (DEBUG_LOAD) utils.Debug.show( "Contador de verbos irregulares: " + conjCount + " " + addCount );
    	    } 
    	    catch (IOException ex){
    	    	if (DEBUG_ERROR) utils.Debug.show( "file irregularesModelos.dat not found");
    	    }
    	}
		return result;
	}
	
	static Conjugacion conjTest = new Conjugacion( "", "", 0, false, "", false, "", "", 0.0F );
	/** Devuelve la informaci�n de si una palabra es una forma verbal irregular consultando en la BD de formas irregulares
	 * pre: pal es s�lo una palabra (no tiene espacios), en min�sculas (puede tener tildes)
	 * post: tras llamar a esta funci�n se pueden chequear los m�todos getConjugacion(), variasConjugaciones()
	 * @param pal	Palabra a chequear
	 * @param exactMatch	true si la palabra tiene que ser exacta incluyendo tildes, false las tildes no se consideran
	 * @return	true si es una forma verbal irregular, false si no lo es
	 */
	public static boolean esFormaVerbal( String pal, boolean exactMatch ) {
		lastConjugacion = null;
		calcVariasConjugaciones(pal, exactMatch);
		if (lastVectorConjs.size() > 0) {
			// Si hay varios buscamos el que m�s se acerque (por tildes)
			int i = 0;
			for (Conjugacion c : lastVectorConjs) {
				if (lastConjugacion == null || c.getConfianza() > lastConjugacion.getConfianza()) {  // && lastConjugacion.getConfianza() < CONFIANZA_EXACTA) {   // Comparaci�n exacta (con tildes)
					lastConjugacion = c; 
					if (c.getConfianza() >= CONFIANZA_EXACTA) break;  // Es ya la m�xima confianza, no hace falta buscar m�s  (salvo que se diera info de verbo, de tiempo, de persona...
				}
				i++;
			}
			return (lastConjugacion != null);
		} else {
			return false;
		}
	}
	
	private static Conjugacion lastConjugacion = null;
	/** Devuelve la conjugaci�n de la �ltima forma verbal buscada y encontrada
	 * @return	Ultima forma verbal encontrada, null si no se encontr� (esFormaVerbal = false)
	 */
	public static Conjugacion getConjugacion() {
		return lastConjugacion;
	}

//  DEPRECATED
//	private static float lastConfianza = 0.0F;
//	/** La confianza de la detecci�n es de un 100% si es seguro que s�, y de un 75% si coincide sin tildes pero no con tildes
//	 * @return  Confianza de la detecci�n (0.0 ninguna a 1.0 m�xima)
//	 */
//	public static float confianzaDeteccion() {
//		return lastConfianza;
//	}

	private static Vector<Conjugacion> lastVectorConjs = null; 
	/** Devuelve todas las conjugaciones posibles de la forma verbal pasada a esFormaVerbal
	 * @return	Vector de todas las formas verbales encontradas que encajan con la palabra indicada, vac�o si no se encontr� ninguna
	 */
	public static Vector<Conjugacion> variasConjugaciones() {
		return lastVectorConjs;
	}

//  DEPRECATED
//	private static Vector<Float> lastVectorConfianzas = null; 
//	/** Devuelve las confianzas de las conjugaciones posibles indicadas por variasConjugaciones()
//	 * @return	Vector de confianzas (de 0.0 a 1.0) de cada forma verbal, en el mismo orden del vector de variasConjugaciones(),
//	 * 		vac�o si no se encontr� ninguna conjugaci�n
//	 */
//	public static Vector<Float> variasConfianzas() {
//		return lastVectorConfianzas;
//	}
//
	/** M�todo usado por esFormaVerbal para calcular el vector de conjugaciones y confianzas
	 */
	private static void calcVariasConjugaciones( String pal, boolean exactMatch ) {
		Vector<Conjugacion> v = new Vector<Conjugacion>();
		String palST = UtilsVerbos.quitarTildes(pal);
		conjTest.initConjugBusqueda( palST );
		if (conjs.contains(conjTest)) {
			// Si hay varias devolvemos todas. Primero por detr�s...
			Iterator<Conjugacion> grupo = conjs.headSet( conjTest, true ).descendingIterator();  // Cogemos las menores o iguales en descendente
			while (grupo.hasNext()) {
				Conjugacion c = grupo.next();
				if (!palST.equals(c.getConjugacionSinTildes())) break;  // Si no es el mismo verbo, se para el bucle (s�lo se recorren iguales)
				if (!exactMatch || pal.equals(c.getConjugacion())) {  // Si la comparaci�n es exacta y no es el mismo verbo, no se a�ade (s�lo se recorren iguales)
					if (pal.equals(c.getConjugacion())) c.setConfianza(CONFIANZA_EXACTA); else c.setConfianza(CONFIANZA_SIN_TILDES);  // se a�ade la confianza
					v.add(c);  // se a�ade
				}
			}
			// Y luego por delante:
			grupo = conjs.tailSet( conjTest, true ).iterator();  // Cogemos las mayores o iguales
			grupo.next();  // Y despreciamos la primera (se ha procesado en el bucle de "detr�s"
			while (grupo.hasNext()) {
				Conjugacion c = grupo.next();
				if (!palST.equals(c.getConjugacionSinTildes())) break;  // Si no es el mismo verbo, se para el bucle (s�lo se recorren iguales)
				if (!exactMatch || pal.equals(c.getConjugacion())) {  // Si la comparaci�n es exacta y no es el mismo verbo, no se a�ade (s�lo se recorren iguales)
					if (pal.equals(c.getConjugacion())) c.setConfianza(CONFIANZA_EXACTA); else c.setConfianza(CONFIANZA_SIN_TILDES);  // se a�ade la confianza
					v.add(c);  // se a�ade la conjugaci�n
				}
			}
		}
		lastVectorConjs = v;
	}
	
	/** Chequea un infinitivo verbal en min�sculas y sin tildes para ver si es un verbo irregular
	 * @param pal	Verbo a chequear (min�sculas, sin tildes)
	 * @return	true si es irregular, false si no est� en la lista de verbos irregulares soportados, o no es un verbo
	 */
	public static boolean esVerboIrregular(String pal) {
		return (esFormaVerbal(pal, false) && getConjugacion().getFormaVerbal().equals("infinitivo"));
	}		

	public static void main (String[] s) {
		Iterator<Conjugacion> i = iterator();
		int j = 0;
		while (i.hasNext()) { utils.Debug.show( j++ + "  " + i.next().show() ); }
		Debug.showWindow(false);
		Debug.setPrefix("* ");
		Debug.show( "tener " + (esVerboIrregular("tener")?"ES":"NO ES") + " un verbo irregular" );
		Debug.show( "cantar " + (esVerboIrregular("cantar")?"ES":"NO ES") + " un verbo irregular" );
		Debug.show( "yendo: " + esFormaVerbal("yendo", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "cant�: " + esFormaVerbal("cant�", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "dispondr�: " + esFormaVerbal("dispondr�", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "dispondra: " + esFormaVerbal("dispondra", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "dispondra (forzando tildes): " + esFormaVerbal("dispondra", true) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "ir: " + esFormaVerbal("ir", true) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "vuelva: " + esFormaVerbal("vuelva", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
			for (int k = 0; k < variasConjugaciones().size(); k++ )
				Debug.show( "  " + variasConjugaciones().get(k).show( true ) );
		utils.Debug.close();
	}

}
