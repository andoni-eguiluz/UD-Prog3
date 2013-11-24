package utils.verbos.expresiones;

import utils.Debug;
import utils.verbos.*;
import java.util.Vector;

/** Palabra gen�rica sin tratamiento espec�fico diferenciado
 */
public class Expresion implements TextoConceptual {
	protected String textoOrig;  //  Referencia al texto original completo
	protected Vector<TextoConceptual> expresion;
	/** Inicializa una expresi�n sobre el texto original, vac�a
	 * (sin asignarle ninguna unidad textual de contenido)
	 * @param textoOriginal
	 */
	public Expresion( String textoOriginal ) {
		textoOrig = textoOriginal;
		expresion = new Vector<TextoConceptual>();
	}
	public void add( TextoConceptual tc ) {
		expresion.add( tc );
	}
	// pre: e2 tiene el mismo texto origen que esta expresi�n
	public void add( Expresion e2 ) {
		expresion.add( e2 );
	}
	// pre: e2 tiene el mismo texto origen que esta expresi�n
	public void add( Expresion e2, boolean fundirLaExpresion ) {
		if (fundirLaExpresion)
			for (int i = 0; i < e2.numTCs(); i++) expresion.add( e2.getTC(i) );
		else add( e2 );
	}
	
	/** Devuelve el texto conceptual contenido en la expresi�n, indicado por su posici�n relativa en ella
	 * (el texto conceptual puede ser una unidad textual, u otra expresi�n)
	 * @param pos	Posici�n relativa (0 a numTCs-1)
	 * @return	TextoConceptual referenciado en esa posici�n, null si la posici�n es incorrecta
	 */
	public TextoConceptual getTC( int pos ) {
		if (pos < 0 || pos >= expresion.size()) return null;
		return expresion.get(pos);
	}
	public int numTCs() {  // Devuelve el n�mero de textos conceptuales contenidos en la expresi�n
		return expresion.size();
	}
	
	/** Sustituye una serie de textos conceptuales por una expresi�n e2 que los engloba y caracteriza
	 * pre: e2 es una expresi�n que engloba a todos los textos conceptuales de indInicialTC a indFinalTC, ambos inclusive,
	 * 		o bien se trata de una sola UnidadTextual que reemplaza a otra/otras
	 * pre: e2 tiene el mismo texto origen que esta expresi�n
	 * Si hay un error en los par�metros, no hace nada
	 * @param indInicialTC	cumple 0 <= indInicialTC <= indFinalTC < numTCs()
	 * @param indFinalTC
	 * @param e2	Expresi�n construida que engloba a esos textos
	 */
	public void reemplazar( int indInicialTC, int indFinalTC, TextoConceptual e2 ) {
		if (indInicialTC < 0 || indFinalTC >= expresion.size() || indInicialTC > indFinalTC ) return;
		for (int i = indInicialTC; i <= indFinalTC; i++) expresion.remove( indInicialTC );
		expresion.add( indInicialTC, e2 );
	}
	
	/** Devuelve la informaci�n de si la expresi�n es o contiene un verbo complejo:
	 * ExpresionVerbal, PerifrasisVerbal o VerboCompuesto
	 * @return	true si lo contiene, false en caso contrario
	 */
	public boolean contieneVerboComplejo() { 
		for (int i = 0; i < expresion.size(); i++) {
			TextoConceptual tc = expresion.get(i);
			if (tc instanceof ExpresionVerbal || tc instanceof VerboCompuesto || tc instanceof PerifrasisVerbal)
				return true;
			else if (tc instanceof Expresion)
				if (((Expresion)tc).contieneVerboComplejo()) return true;
		}
		return false;
	}
	
	/** Cuenta en la expresi�n las unidades que corresponden a un tipo determinado de objeto.<p>
	 * Este tipo puede ser tanto un derivado de TextoConceptual, como del interfaz VerboPrincipal.
	 * @param c	Clase modelo que se quiere contear (TextoConceptual.class para todas, VerboPrincipal.class para verbos...)
	 * @param valorAChequear	(min�sculas) si c es verbal, este valor indica el infinitivo del verbo principal que se quiere contar.
	 * 		Si no, indica el texto exacto con el que tiene que corresponder la unidad.
	 * 		Si es vac�o, no se considera
	 * @param revisarExpAnidadas	true si se revisa todo el �rbol, false revisa s�lo el primer nivel 
	 * 		(y en ese caso no entra en las expresiones que haya anidadas).
	 * @return	N�mero de ocurrencias
	 * Ejemplo 1: conteoDe( VerboConjugado, "acabar" )   cuenta las ocurrencias del verbo principal "acabar"
	 * Ejemplo 2: conteoDe( UnidadTextual, "desde" ) cuenta todas las palabras "desde" en la expresi�n
	 */
	public int conteoDe( Class<?> c, String valorAChequear, boolean revisarExpAnidadas ) {
		int conteo = 0;
		boolean claseVerbal = VerboPrincipal.class.isAssignableFrom( c );
		for (TextoConceptual tc : expresion) {
			if (c.isInstance(tc)) {  // (1)
				if (valorAChequear == null || valorAChequear.equals("")) {
					conteo++;
				} else {
					if (claseVerbal) {  // Si estamos buscando verbos chequear infinitivo principal
						// if (tc instanceof VerboPrincipal) {  // Este chequeo est� impl�cito en (1)
							String verboP = ((VerboPrincipal)tc).getInfinitivoVerboPrincipal();
							if (valorAChequear.equals( verboP )) conteo++;
							// else nada
						// }
					} else {  // Si no estamos buscando verbos, chequear palabra
							if (valorAChequear.equals( tc.getTextoMinusc() )) conteo++;
					}
				}
			} else if (revisarExpAnidadas && (tc instanceof Expresion)) {
				conteo += ((Expresion)tc).conteoDe(c, valorAChequear, revisarExpAnidadas);
			}
		}
		return conteo;
	}
	
	/** Busca en la expresi�n las unidades que corresponden a un tipo determinado de objeto.<p>
	 * Este tipo puede ser tanto un derivado de TextoConceptual, como del interfaz VerboPrincipal.
	 * @param c	Clase modelo que se quiere buscar (TextoConceptual.class para todas, VerboPrincipal.class para verbos...)
	 * @param textoABuscar (min�sculas) si c es verbal, este valor indica el infinitivo del verbo principal que se quiere contar.
	 * 		Si no, indica el texto exacto con el que tiene que corresponder la unidad.
	 * 		(Por ello, si se quiere buscar una forma verbal determinada, poner c a TextoConceptual.class)
	 * 		Si es vac�o, se busca la primera unidad que encaje con c (independientemente de su contenido)
	 * @param posIniBusqueda	Primera posici�n desde la que buscar (0 para buscar en toda la expresi�n)
	 * @param revisarExpAnidadas	true si se revisa todo el �rbol, false revisa s�lo el primer nivel 
	 * 		(y en ese caso no entra en las expresiones que haya anidadas).
	 * 		Si este par�metro es true, si se encuentra el elemento se devuelve la posici�n de toda la subexpresi�n
	 * 		donde se ha encontrado. Si se quiere trabajar con ese elemento espec�ficamente deber� buscarse de nuevo en ella.
	 * @return	Posici�n de la primera ocurrencia encontrada, -1 si no se encuentra
	 * Ejemplo 1: conteoDe( VerboConjugado.class, "acabar", 0 )   devuelve la posici�n de la primera ocurrencia del verbo principal "acabar"
	 * Ejemplo 2: conteoDe( TextoConceptual.class, "acabado", 0 ) devuelve la posici�n de la primera ocurrencia de la forma verbal "acabado"
	 * @return
	 */
	
	public int buscaTexto( Class<?> c, String textoABuscar, int posIniBusqueda, boolean revisarExpAnidadas ) {
		boolean claseVerbal = VerboPrincipal.class.isAssignableFrom( c );
		int i = posIniBusqueda; if (i<0) i = 0;
		while (i < expresion.size()) {
			TextoConceptual tc = expresion.get(i);
			if (c.isInstance(tc)) {  // (1)
				if (textoABuscar == null || textoABuscar.equals("")) {
					return i;
				} else {
					if (claseVerbal) {  // Si estamos buscando verbos chequear infinitivo principal
						// if (tc instanceof VerboPrincipal) {  // Este chequeo est� impl�cito en (1)
							String verboP = ((VerboPrincipal)tc).getInfinitivoVerboPrincipal();
							if (textoABuscar.equals( verboP )) return i;
							// else nada
						// }
					} else {  // Si no estamos buscando verbos, chequear palabra
							if (textoABuscar.equals( tc.getTextoMinusc() )) return i;
					}
				}
			} else if (revisarExpAnidadas && (tc instanceof Expresion)) {
				int posAnidada = ((Expresion)tc).buscaTexto(c, textoABuscar, 0, revisarExpAnidadas);
				if ((posAnidada) == -1) return i;
			}
			i++;
		}
		return -1;
	}	
	
	/** Busca en la expresi�n un patr�n determinado y devuelve la(s) posici�n(es) en las que ocurre.
	 * El patr�n es un string con elementos separados por espacios. Cada uno de los elementos debe corresponder a
	 * uno de los siguientes elementos patr�n:<ul>
	 * <li> [XXXXX] - Cualquier conjugaci�n del verbo principal XXXXX. Por ejemplo [ACABAR] encaja con "acab�", "acabar", "he acabado" o "se di� por acabado"<p>
	 * <li> [XXXXXSE] - Cualquier conjugaci�n reflexiva del verbo principal XXXXX. Por ejemplo [ACABARSE] encaja con "se acab�", "acabose" o "se ha acabado"<p>
	 * <li> [infinitivo] - Cualquier verbo en forma de infinitivo simple o compuesto. Por ejemplo "acabar" o "haber perdido"<p>
	 * <li> [gerundio] - Cualquier verbo en forma de gerundio simple o compuesto. Por ejemplo "acabando" o "habiendo perdido"<p>
	 * <li> [participio] - Cualquier verbo en forma de participio. Por ejemplo "acabado"<p>
	 * <li> [verbo] - Cualquier conjugaci�n de cualquier verbo<p>
	 * <li> [refl] - Cualquier part�cula reflexiva (me, te, se...)
	 * <li> [artd] - Cualquier art�culo determinado (la, lo, les...)
	 * </ul>
	 * Adem�s, se pueden indicar cualquiera de los siguientes elementos no patr�n:<ul>
	 * <li> xxxxx - Cualquier expresi�n textual exacta. Por ejemplo "de", "por" o "en"<p>
	 * <li> ![verbo] - Cualquier serie de expresiones textuales no verbales, excepto s�mbolos, de cualquier longitud<p>
	 * 		(este elemento no debe ser el primero ni el �ltimo del patr�n, ni puede haber dos seguidos)
	 * </ul>
	 * Para el emparejamiento no se consideran significativos los elementos de tipo separador.<p>
	 * @param patron	String del patr�n de emparejamiento, construido de acuerdo a la sintaxis indicada
	 * @param posIniBusqueda	Primera posici�n desde la que buscar (0 para buscar en toda la expresi�n)
	 * @param revisarExpAnidadas	true si se revisa todo el �rbol, false revisa s�lo el primer nivel 
	 * 		(y en ese caso no entra en las expresiones que haya anidadas).
	 * 		Si este par�metro es true, s�lo se utiliza para elementos no patr�n. Los elementos patr�n deben encontrarse
	 * 		exclusivamente en el primer nivel.
	 * @return	Array de posiciones del primer emparejamiento encontrado. Cada elemento del array corresponde a uno de 
	 * los elementos patr�n (no se indican las posiciones de los elementos no patr�n). Si no se encuentran emparejamientos,
	 * se devuelve null.
	 * Ejemplos de string de emparejamiento: "[HABER] que ver lo que [verbo]", "[HARTARSE] ![verbo] de [infinitivo]"
	 * @throws ExpresionVerboException	Lanzada si el patr�n es incorrecto
	 */
	public int[] buscaPatron( String patron, int posIniBusqueda, boolean revisarExpAnidadas ) throws ExpresionVerboException {
		return buscaPatron( patron, posIniBusqueda, revisarExpAnidadas, false, false );
	}

	/** Como buscaPatron(String patron, int posIniBusqueda, boolean revisarExpAnidadas) pero a�adiendo dos par�metros:
	 * @param devPrimero	Si true, devuelve en el array (al inicio) la posici�n del primer elemento encontrado adem�s del resto de  las posiciones de patr�n
	 * @param devUltimo	Si true, devuelve en el array (al final) la posici�n del primer elemento encontrado adem�s del resto de  las posiciones de patr�n
	 */
	public int[] buscaPatron( String patron, int posIniBusqueda, boolean revisarExpAnidadas, boolean devPrimero, boolean devUltimo ) throws ExpresionVerboException {
		patron = patron.toLowerCase();
		String[] elPat = patron.split("[ ]+");  // Dividir patr�n en elementos
		int[] posiciones = new int[ elPat.length ];
		int posPat = 0;  // Posici�n en el patr�n
		int posExp = posIniBusqueda;  // Posici�n en la expresi�n
		int contNoPat = 0;  // Contador de elementos no patr�n dentro del patr�n
		int posPrimerMatch = 0;  // Posici�n del primer match (para avanzar a la siguiente en caso de no match)
		while (posExp < expresion.size() && posPat < elPat.length) {
			TextoConceptual tc = expresion.get(posExp);
			if (tc instanceof Simbolo) { // Un s�mbolo no es bienvenido dentro de un patr�n
				posPat = 0; posExp++;
			} else if (tc instanceof Separador) {  // Se ignoran los separadores
				posExp++;
			} else {
				boolean hayMatch = false;
				boolean eltoEsPatron = (elPat[posPat].charAt(0) == '['); if (!eltoEsPatron) contNoPat++;
				if (elPat[posPat].equals("![verbo]") && (posPat == 0 || posPat == elPat.length-1)) throw new ExpresionVerboException( "Error en patr�n: ![verbo] en inicio/final de patr�n");  // Error de patr�n
				posExpSgte = -1; // Especial para el caso de comod�n
				hayMatch = matches( elPat, posPat, posExp, tc );  // Alternativa principal sobre el elemento del patr�n (ver m�todo matches)
				if (hayMatch) {  // Encontrado: seguimos avanzando 
					if (posPat == 0) posPrimerMatch = posExp;
					if (eltoEsPatron) posiciones[posPat] = posExp; else posiciones[posPat] = -1;
					posPat++; 
					if (posExpSgte == -1) posExp++; else posExp = posExpSgte;  // Posici�n del siguiente (caso especial: comod�n)
				} else { // No encontrado: vuelta a empezar 
					posExp = (posPat==0)?posExp+1:posPrimerMatch+1; 
					posPat=0; contNoPat = 0;
				}  
			}
		}
		if (posPat == elPat.length && elPat.length > 0) {  // Conseguido!
			if (devPrimero) contNoPat--; if (devUltimo) contNoPat--;
			int[] lasPosiciones = new int[ posiciones.length - contNoPat ];
			int i = 0;
			if (devPrimero) { lasPosiciones[0] = posPrimerMatch; i++; }
			for (int posi : posiciones) if (posi >= 0) { lasPosiciones[i] = posi; i++; }
			if (devUltimo) { lasPosiciones[posiciones.length - contNoPat - 1] = posExp-1; i++; }
			return lasPosiciones;
		} // else
		return null;
	}

	private int posExpSgte = -1;
	private boolean matches( String[] elPat, int posPat, int posExp, TextoConceptual tc ) throws ExpresionVerboException {
		String pat = elPat[posPat];
		if (pat.charAt(0) == '[') {  // Elemento patr�n
			VerboConjugado vc = null;
			if (pat.equals("[refl]")) {  // Cualquier part�cula reflexiva
				return (tc instanceof ParticulaReflexiva);
			} else if (pat.equals("[artd]")) {  // Cualquier art�culo determinado
				return (tc instanceof ArticuloDeterminado);
			} else if (tc instanceof VerboConjugado) { 
				vc = (VerboConjugado) tc;
				if (pat.equals("[verbo]")) {  // Cualquier conjug de cualquier verbo
					return (vc != null);
				} else if (pat.equals("[infinitivo]")) {  // Cualquier infinitivo
					return (vc.esInfinitivo());
				} else if (pat.equals("[gerundio]")) {  // Cualquier gerundio
					return (vc.esGerundio());
				} else if (pat.equals("[participio]")) {  // Cualquier participio
					return (vc.esParticipio());
				} else if ("#ar]#er]#ir]".contains(pat.substring(pat.length()-3))) {  // Cualquier conjugaci�n de un verbo dado						
					return (vc.getInfinitivoVerboPrincipal().equals(pat.substring(1, pat.length()-1)));
				} else if (pat.length() < 6) {
					throw new ExpresionVerboException( "Patr�n no reconocido: <" + pat + ">");  // Error de patr�n
				} else if (pat.endsWith("se]")) {  // Cualquier conjugaci�n reflexiva de un verbo dado						
					return ((vc.getInfinitivoVerboPrincipal().equals(pat.substring(1, pat.length()-3))) && vc.esReflexivo());
				} else 
					throw new ExpresionVerboException( "Patr�n no reconocido: <" + pat + ">");  // Error de patr�n
			} else 
				return false;	
		} else {  // Elemento no patr�n
			if (pat.equals("![verbo]")) {  // Cualquier serie de expresiones no verbales ni s�mbolos (comod�n)
				posExpSgte = posExp;  // Empezamos en el mismo elemento, que puede ser (la serie a localizar puede ser vac�a)
				while (posExpSgte < expresion.size()) {
					TextoConceptual tcSgte = expresion.get(posExpSgte);
					if (tcSgte instanceof VerboPrincipal) return true;   // Un verbo acaba ya el comod�n
					if (matches( elPat, posPat+1, posExpSgte, tcSgte )) return true;  // Si hay match en el siguiente acaba el comod�n
					posExpSgte++;
				}
				return false;
			} else {  // xxxxx - Cualquier expresi�n textual exacta
				return tc.getTextoMinusc().equals(pat);
			}
		}
	}
	
	public String getTextoOriginalCompleto() { 
		return textoOrig; 
	}

	public String getTextoUnidad() {
		String text = "";
		for (int i = 0; i < expresion.size(); i++) text += expresion.get(i).getTextoUnidad();
		return text;
	}
	public String getTextoMinusc() { 
		String text = "";
		for (int i = 0; i < expresion.size(); i++) text += expresion.get(i).getTextoMinusc();
		return text;
	}
	public String getTextoSinTildes() { 
		String text = "";
		for (int i = 0; i < expresion.size(); i++) text += expresion.get(i).getTextoSinTildes();
		return text;
	}	

	public int getOffsetIni() {
		if (expresion.size() == 0) return 0;
		return expresion.get(0).getOffsetIni();
	}
	public int getOffsetFin() {
		if (expresion.size() == 0) return 0;
		return expresion.get( expresion.size()-1 ).getOffsetFin();
	}
	
	public String toString() {
		StringBuffer res = new StringBuffer("{");
		for (TextoConceptual t : expresion) { res.append( t.toString()); }
		res.append("}");
		return res.toString().trim();
	}
	
	public static void main( String[] s ) {
		String textoPrueba = "hola soy el verbo ser,he sido";
		Expresion e = new Expresion( textoPrueba );
		e.add( new Palabra(textoPrueba,0,4));  // hola
		e.add( new Separador(textoPrueba,4,5));
		if (VerbosIrregulares.esFormaVerbal(textoPrueba.substring(5,8), false) && (VerbosIrregulares.getConjugacion().getConfianza() > 0.4F))   // Posible verbo? 						
			e.add( new VerboSimple( textoPrueba, 5, 8, VerbosIrregulares.getConjugacion() ));   // soy
		e.add( new Separador(textoPrueba,8,9));
		e.add( new Palabra(textoPrueba,9,11));  // el
		e.add( new Separador(textoPrueba,11,12));
		e.add( new Palabra(textoPrueba,12,17));  // verbo
		e.add( new Separador(textoPrueba,17,18));
		if (VerbosIrregulares.esFormaVerbal(textoPrueba.substring(18,21), false) && (VerbosIrregulares.getConjugacion().getConfianza() > 0.4F))   // Posible verbo? 						
			e.add( new VerboSimple( textoPrueba, 18, 21, VerbosIrregulares.getConjugacion() ));   // ser
		e.add( new Simbolo(textoPrueba,21,22));  // ,
		if (VerbosIrregulares.esFormaVerbal(textoPrueba.substring(22,24), false) && (VerbosIrregulares.getConjugacion().getConfianza() > 0.4F))   // Posible verbo? 						
			e.add( new VerboSimple( textoPrueba, 22, 24, VerbosIrregulares.getConjugacion() ));   // he
		e.add( new Separador(textoPrueba,24,25));
		if (VerbosIrregulares.esFormaVerbal(textoPrueba.substring(25,29), false) && (VerbosIrregulares.getConjugacion().getConfianza() > 0.4F))   // Posible verbo? 						
			e.add( new VerboSimple( textoPrueba, 25, 29, VerbosIrregulares.getConjugacion() ));   // sido
		
		// Crear verbo compuesto
		try {
			VerboCompuesto vc = new VerboCompuesto( e, 10, 12 );
			// Sustituirlo en e
			e.reemplazar(10, 12, vc);
		} catch (ExpresionVerboException exc) {
			Debug.show( "Excepci�n en verbo compuesto: " + exc );
			exc.printStackTrace();
		}
		
		
		Debug.show( textoPrueba );  //  "hola soy el verbo ser,he sido";
		Debug.show( e.toString() );
		Debug.show( "Ocurrencias del verbo ser conjugado simple (2): " + e.conteoDe(VerboSimple.class, "ser", false) );
		Debug.show( "Ocurrencias del verbo ser conjugado simple en el �rbol (3): " + e.conteoDe(VerboSimple.class, "ser", true) );
		Debug.show( "Ocurrencias del verbo principal ser (3): " + e.conteoDe(VerboPrincipal.class, "ser", false) );
		Debug.show( "Ocurrencias del verbo estar (0): " + e.conteoDe(VerboSimple.class, "estar", false) );
		Debug.show( "Ocurrencias del verbo haber (0): " + e.conteoDe(VerboSimple.class, "haber", false) );
		Debug.show( "Ocurrencias del verbo haber en el �rbol (1): " + e.conteoDe(VerboSimple.class, "haber", true) );
		Debug.show( "Ocurrencias de cualquier verbo principal (3): " + e.conteoDe(VerboPrincipal.class, "", false) );
		Debug.show( "Ocurrencias de cualquier verbo simple en el �rbol (4): " + e.conteoDe(VerboSimple.class, "", true) );
		Debug.show( "Ocurrencias de la palabra ser en el �rbol (1): " + e.conteoDe(UnidadTextual.class, "ser", true) );
		Debug.show( "Ocurrencias de la palabra verbo (1): " + e.conteoDe(UnidadTextual.class, "verbo", true) );
		Debug.show( "El primer verbo ser est� en la posici�n (2)... " + e.buscaTexto(VerboPrincipal.class, "ser", 0, false) );
		try {
			String patronPru = "[verbo] ![verbo] el ![verbo] [ser]";
			Debug.show( "Busca patr�n '" + patronPru + "': " + verArray( 
					e.buscaPatron( patronPru, 0, false ) ) );
		} catch (ExpresionVerboException exx) {
			Debug.show( exx.getMessage() );
		}

	}
	
	// Utilitaria para depuraci�n
	private static String verArray( int[] a ) { if (a==null) return "NULL"; String result = ""; for ( int i : a ) result += (i + " "); return result; }
	
}

