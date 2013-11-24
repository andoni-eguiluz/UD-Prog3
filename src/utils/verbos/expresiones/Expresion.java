package utils.verbos.expresiones;

import utils.Debug;
import utils.verbos.*;
import java.util.Vector;

/** Palabra genérica sin tratamiento específico diferenciado
 */
public class Expresion implements TextoConceptual {
	protected String textoOrig;  //  Referencia al texto original completo
	protected Vector<TextoConceptual> expresion;
	/** Inicializa una expresión sobre el texto original, vacía
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
	// pre: e2 tiene el mismo texto origen que esta expresión
	public void add( Expresion e2 ) {
		expresion.add( e2 );
	}
	// pre: e2 tiene el mismo texto origen que esta expresión
	public void add( Expresion e2, boolean fundirLaExpresion ) {
		if (fundirLaExpresion)
			for (int i = 0; i < e2.numTCs(); i++) expresion.add( e2.getTC(i) );
		else add( e2 );
	}
	
	/** Devuelve el texto conceptual contenido en la expresión, indicado por su posición relativa en ella
	 * (el texto conceptual puede ser una unidad textual, u otra expresión)
	 * @param pos	Posición relativa (0 a numTCs-1)
	 * @return	TextoConceptual referenciado en esa posición, null si la posición es incorrecta
	 */
	public TextoConceptual getTC( int pos ) {
		if (pos < 0 || pos >= expresion.size()) return null;
		return expresion.get(pos);
	}
	public int numTCs() {  // Devuelve el número de textos conceptuales contenidos en la expresión
		return expresion.size();
	}
	
	/** Sustituye una serie de textos conceptuales por una expresión e2 que los engloba y caracteriza
	 * pre: e2 es una expresión que engloba a todos los textos conceptuales de indInicialTC a indFinalTC, ambos inclusive,
	 * 		o bien se trata de una sola UnidadTextual que reemplaza a otra/otras
	 * pre: e2 tiene el mismo texto origen que esta expresión
	 * Si hay un error en los parámetros, no hace nada
	 * @param indInicialTC	cumple 0 <= indInicialTC <= indFinalTC < numTCs()
	 * @param indFinalTC
	 * @param e2	Expresión construida que engloba a esos textos
	 */
	public void reemplazar( int indInicialTC, int indFinalTC, TextoConceptual e2 ) {
		if (indInicialTC < 0 || indFinalTC >= expresion.size() || indInicialTC > indFinalTC ) return;
		for (int i = indInicialTC; i <= indFinalTC; i++) expresion.remove( indInicialTC );
		expresion.add( indInicialTC, e2 );
	}
	
	/** Devuelve la información de si la expresión es o contiene un verbo complejo:
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
	
	/** Cuenta en la expresión las unidades que corresponden a un tipo determinado de objeto.<p>
	 * Este tipo puede ser tanto un derivado de TextoConceptual, como del interfaz VerboPrincipal.
	 * @param c	Clase modelo que se quiere contear (TextoConceptual.class para todas, VerboPrincipal.class para verbos...)
	 * @param valorAChequear	(minúsculas) si c es verbal, este valor indica el infinitivo del verbo principal que se quiere contar.
	 * 		Si no, indica el texto exacto con el que tiene que corresponder la unidad.
	 * 		Si es vacío, no se considera
	 * @param revisarExpAnidadas	true si se revisa todo el árbol, false revisa sólo el primer nivel 
	 * 		(y en ese caso no entra en las expresiones que haya anidadas).
	 * @return	Número de ocurrencias
	 * Ejemplo 1: conteoDe( VerboConjugado, "acabar" )   cuenta las ocurrencias del verbo principal "acabar"
	 * Ejemplo 2: conteoDe( UnidadTextual, "desde" ) cuenta todas las palabras "desde" en la expresión
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
						// if (tc instanceof VerboPrincipal) {  // Este chequeo está implícito en (1)
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
	
	/** Busca en la expresión las unidades que corresponden a un tipo determinado de objeto.<p>
	 * Este tipo puede ser tanto un derivado de TextoConceptual, como del interfaz VerboPrincipal.
	 * @param c	Clase modelo que se quiere buscar (TextoConceptual.class para todas, VerboPrincipal.class para verbos...)
	 * @param textoABuscar (minúsculas) si c es verbal, este valor indica el infinitivo del verbo principal que se quiere contar.
	 * 		Si no, indica el texto exacto con el que tiene que corresponder la unidad.
	 * 		(Por ello, si se quiere buscar una forma verbal determinada, poner c a TextoConceptual.class)
	 * 		Si es vacío, se busca la primera unidad que encaje con c (independientemente de su contenido)
	 * @param posIniBusqueda	Primera posición desde la que buscar (0 para buscar en toda la expresión)
	 * @param revisarExpAnidadas	true si se revisa todo el árbol, false revisa sólo el primer nivel 
	 * 		(y en ese caso no entra en las expresiones que haya anidadas).
	 * 		Si este parámetro es true, si se encuentra el elemento se devuelve la posición de toda la subexpresión
	 * 		donde se ha encontrado. Si se quiere trabajar con ese elemento específicamente deberá buscarse de nuevo en ella.
	 * @return	Posición de la primera ocurrencia encontrada, -1 si no se encuentra
	 * Ejemplo 1: conteoDe( VerboConjugado.class, "acabar", 0 )   devuelve la posición de la primera ocurrencia del verbo principal "acabar"
	 * Ejemplo 2: conteoDe( TextoConceptual.class, "acabado", 0 ) devuelve la posición de la primera ocurrencia de la forma verbal "acabado"
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
						// if (tc instanceof VerboPrincipal) {  // Este chequeo está implícito en (1)
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
	
	/** Busca en la expresión un patrón determinado y devuelve la(s) posición(es) en las que ocurre.
	 * El patrón es un string con elementos separados por espacios. Cada uno de los elementos debe corresponder a
	 * uno de los siguientes elementos patrón:<ul>
	 * <li> [XXXXX] - Cualquier conjugación del verbo principal XXXXX. Por ejemplo [ACABAR] encaja con "acabó", "acabar", "he acabado" o "se dió por acabado"<p>
	 * <li> [XXXXXSE] - Cualquier conjugación reflexiva del verbo principal XXXXX. Por ejemplo [ACABARSE] encaja con "se acabó", "acabose" o "se ha acabado"<p>
	 * <li> [infinitivo] - Cualquier verbo en forma de infinitivo simple o compuesto. Por ejemplo "acabar" o "haber perdido"<p>
	 * <li> [gerundio] - Cualquier verbo en forma de gerundio simple o compuesto. Por ejemplo "acabando" o "habiendo perdido"<p>
	 * <li> [participio] - Cualquier verbo en forma de participio. Por ejemplo "acabado"<p>
	 * <li> [verbo] - Cualquier conjugación de cualquier verbo<p>
	 * <li> [refl] - Cualquier partícula reflexiva (me, te, se...)
	 * <li> [artd] - Cualquier artículo determinado (la, lo, les...)
	 * </ul>
	 * Además, se pueden indicar cualquiera de los siguientes elementos no patrón:<ul>
	 * <li> xxxxx - Cualquier expresión textual exacta. Por ejemplo "de", "por" o "en"<p>
	 * <li> ![verbo] - Cualquier serie de expresiones textuales no verbales, excepto símbolos, de cualquier longitud<p>
	 * 		(este elemento no debe ser el primero ni el último del patrón, ni puede haber dos seguidos)
	 * </ul>
	 * Para el emparejamiento no se consideran significativos los elementos de tipo separador.<p>
	 * @param patron	String del patrón de emparejamiento, construido de acuerdo a la sintaxis indicada
	 * @param posIniBusqueda	Primera posición desde la que buscar (0 para buscar en toda la expresión)
	 * @param revisarExpAnidadas	true si se revisa todo el árbol, false revisa sólo el primer nivel 
	 * 		(y en ese caso no entra en las expresiones que haya anidadas).
	 * 		Si este parámetro es true, sólo se utiliza para elementos no patrón. Los elementos patrón deben encontrarse
	 * 		exclusivamente en el primer nivel.
	 * @return	Array de posiciones del primer emparejamiento encontrado. Cada elemento del array corresponde a uno de 
	 * los elementos patrón (no se indican las posiciones de los elementos no patrón). Si no se encuentran emparejamientos,
	 * se devuelve null.
	 * Ejemplos de string de emparejamiento: "[HABER] que ver lo que [verbo]", "[HARTARSE] ![verbo] de [infinitivo]"
	 * @throws ExpresionVerboException	Lanzada si el patrón es incorrecto
	 */
	public int[] buscaPatron( String patron, int posIniBusqueda, boolean revisarExpAnidadas ) throws ExpresionVerboException {
		return buscaPatron( patron, posIniBusqueda, revisarExpAnidadas, false, false );
	}

	/** Como buscaPatron(String patron, int posIniBusqueda, boolean revisarExpAnidadas) pero añadiendo dos parámetros:
	 * @param devPrimero	Si true, devuelve en el array (al inicio) la posición del primer elemento encontrado además del resto de  las posiciones de patrón
	 * @param devUltimo	Si true, devuelve en el array (al final) la posición del primer elemento encontrado además del resto de  las posiciones de patrón
	 */
	public int[] buscaPatron( String patron, int posIniBusqueda, boolean revisarExpAnidadas, boolean devPrimero, boolean devUltimo ) throws ExpresionVerboException {
		patron = patron.toLowerCase();
		String[] elPat = patron.split("[ ]+");  // Dividir patrón en elementos
		int[] posiciones = new int[ elPat.length ];
		int posPat = 0;  // Posición en el patrón
		int posExp = posIniBusqueda;  // Posición en la expresión
		int contNoPat = 0;  // Contador de elementos no patrón dentro del patrón
		int posPrimerMatch = 0;  // Posición del primer match (para avanzar a la siguiente en caso de no match)
		while (posExp < expresion.size() && posPat < elPat.length) {
			TextoConceptual tc = expresion.get(posExp);
			if (tc instanceof Simbolo) { // Un símbolo no es bienvenido dentro de un patrón
				posPat = 0; posExp++;
			} else if (tc instanceof Separador) {  // Se ignoran los separadores
				posExp++;
			} else {
				boolean hayMatch = false;
				boolean eltoEsPatron = (elPat[posPat].charAt(0) == '['); if (!eltoEsPatron) contNoPat++;
				if (elPat[posPat].equals("![verbo]") && (posPat == 0 || posPat == elPat.length-1)) throw new ExpresionVerboException( "Error en patrón: ![verbo] en inicio/final de patrón");  // Error de patrón
				posExpSgte = -1; // Especial para el caso de comodín
				hayMatch = matches( elPat, posPat, posExp, tc );  // Alternativa principal sobre el elemento del patrón (ver método matches)
				if (hayMatch) {  // Encontrado: seguimos avanzando 
					if (posPat == 0) posPrimerMatch = posExp;
					if (eltoEsPatron) posiciones[posPat] = posExp; else posiciones[posPat] = -1;
					posPat++; 
					if (posExpSgte == -1) posExp++; else posExp = posExpSgte;  // Posición del siguiente (caso especial: comodín)
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
		if (pat.charAt(0) == '[') {  // Elemento patrón
			VerboConjugado vc = null;
			if (pat.equals("[refl]")) {  // Cualquier partícula reflexiva
				return (tc instanceof ParticulaReflexiva);
			} else if (pat.equals("[artd]")) {  // Cualquier artículo determinado
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
				} else if ("#ar]#er]#ir]".contains(pat.substring(pat.length()-3))) {  // Cualquier conjugación de un verbo dado						
					return (vc.getInfinitivoVerboPrincipal().equals(pat.substring(1, pat.length()-1)));
				} else if (pat.length() < 6) {
					throw new ExpresionVerboException( "Patrón no reconocido: <" + pat + ">");  // Error de patrón
				} else if (pat.endsWith("se]")) {  // Cualquier conjugación reflexiva de un verbo dado						
					return ((vc.getInfinitivoVerboPrincipal().equals(pat.substring(1, pat.length()-3))) && vc.esReflexivo());
				} else 
					throw new ExpresionVerboException( "Patrón no reconocido: <" + pat + ">");  // Error de patrón
			} else 
				return false;	
		} else {  // Elemento no patrón
			if (pat.equals("![verbo]")) {  // Cualquier serie de expresiones no verbales ni símbolos (comodín)
				posExpSgte = posExp;  // Empezamos en el mismo elemento, que puede ser (la serie a localizar puede ser vacía)
				while (posExpSgte < expresion.size()) {
					TextoConceptual tcSgte = expresion.get(posExpSgte);
					if (tcSgte instanceof VerboPrincipal) return true;   // Un verbo acaba ya el comodín
					if (matches( elPat, posPat+1, posExpSgte, tcSgte )) return true;  // Si hay match en el siguiente acaba el comodín
					posExpSgte++;
				}
				return false;
			} else {  // xxxxx - Cualquier expresión textual exacta
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
			Debug.show( "Excepción en verbo compuesto: " + exc );
			exc.printStackTrace();
		}
		
		
		Debug.show( textoPrueba );  //  "hola soy el verbo ser,he sido";
		Debug.show( e.toString() );
		Debug.show( "Ocurrencias del verbo ser conjugado simple (2): " + e.conteoDe(VerboSimple.class, "ser", false) );
		Debug.show( "Ocurrencias del verbo ser conjugado simple en el árbol (3): " + e.conteoDe(VerboSimple.class, "ser", true) );
		Debug.show( "Ocurrencias del verbo principal ser (3): " + e.conteoDe(VerboPrincipal.class, "ser", false) );
		Debug.show( "Ocurrencias del verbo estar (0): " + e.conteoDe(VerboSimple.class, "estar", false) );
		Debug.show( "Ocurrencias del verbo haber (0): " + e.conteoDe(VerboSimple.class, "haber", false) );
		Debug.show( "Ocurrencias del verbo haber en el árbol (1): " + e.conteoDe(VerboSimple.class, "haber", true) );
		Debug.show( "Ocurrencias de cualquier verbo principal (3): " + e.conteoDe(VerboPrincipal.class, "", false) );
		Debug.show( "Ocurrencias de cualquier verbo simple en el árbol (4): " + e.conteoDe(VerboSimple.class, "", true) );
		Debug.show( "Ocurrencias de la palabra ser en el árbol (1): " + e.conteoDe(UnidadTextual.class, "ser", true) );
		Debug.show( "Ocurrencias de la palabra verbo (1): " + e.conteoDe(UnidadTextual.class, "verbo", true) );
		Debug.show( "El primer verbo ser está en la posición (2)... " + e.buscaTexto(VerboPrincipal.class, "ser", 0, false) );
		try {
			String patronPru = "[verbo] ![verbo] el ![verbo] [ser]";
			Debug.show( "Busca patrón '" + patronPru + "': " + verArray( 
					e.buscaPatron( patronPru, 0, false ) ) );
		} catch (ExpresionVerboException exx) {
			Debug.show( exx.getMessage() );
		}

	}
	
	// Utilitaria para depuración
	private static String verArray( int[] a ) { if (a==null) return "NULL"; String result = ""; for ( int i : a ) result += (i + " "); return result; }
	
}

