package utils.verbos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import utils.Debug;
import utils.verbos.expresiones.*;

// TODO: Completar lista de verbos irregulares conjugables y cambiar conjugacionesverbales para que los coja de fichero

public class Prueba {

	private static boolean DEBUG_ERROR = true;
	private static boolean DEBUG_IDENTIFICACION = false;
	private static boolean DEBUG_HEURISTICAS = false;
	private static String[] sufijosEncliticosNoReflexivos = { "la", "las", "le", "les", "lo", "los" };
	private static String[] sufijosEncliticosReflexivosDirectos = { "me", "te", "se", "nos", "os" };
	private static String[] sufijosEncliticosReflexivosCompuestos[] = 
		{ { "mela", "mele", "melo", "osla", "osle", "oslo", "osme", "sela", "sele", "selo", 
			"seme", "seos", "sete", "tela", "tele", "telo", "teme" },  // índice 0 - longitud 4
		{ "melas", "meles", "melos", "nosla", "nosle", "noslo", "oslas", "osles", "oslos", 
			"osnos", "selas", "seles", "selos", "senos", "telas", "teles", "telos", "tenos" },  // índice 1 - longitud 5
		{ "noslas", "nosles", "noslos", "osmela", "osmele", "osmelo", "semela", "semele", "semelo", 
			"seosla", "seosle", "seoslo", "seosme", "setela", "setele", "setelo", "seteme", "temela", 
			"temele", "temelo" },  // índice 2 - longitud 6
		{ "osmelas", "osmeles", "osmelos", "osnosla", "osnosle", "osnoslo", "semelas", "semeles", "semelos", 
			"senosla", "senosle", "senoslo", "seoslas", "seosles", "seoslos", "seosnos", "setelas", "seteles", "setelos",
			"setenos", "temelas", "temeles", "temelos", "tenosla", "tenosle", "tenoslo" },  // índice 3 - longitud 7
		{ "osnoslas", "osnosles", "osnoslos", "senoslas", "senosles", "senoslos", 
			"tenoslas", "tenosles", "tenoslos" }  // índice 4 - longitud 8
	 };				
	private static final int SUFIJO_ENCLITICO_MENOR = 4;
	private static final int SUFIJO_ENCLITICO_MAYOR = 8;
	
	private static String prePalabrasReflexivas = "#me#te#se#nos#os#";
	private static String articulosDeterminados = "#el#la#las#le#les#lo#los#";
	private static String articulosIndeterminados = "#un#unos#una#unas#esa#ese#eso#esas#esos#esas#esto#esta#este#estos#estas#";
	private static String pronombresPersonales = "#yo#tú#él#ella#nosotros#vosotros#ellos#nosotras#vosotras#ellas#usted#ustedes#ello#"+
		"#mí#ti#conmigo#contigo#consigo#";   // Tb "sí" que es ambiguo
	private static String pronombresPosesivos = "#mi#tu#su#nuestro#nuestra#vuestro#vuestra#mis#tus#sus#nuestros#nuestras#vuestros#vuestras"+
		"#mío#míos#mía#mías#tuyo#tuya#tuyos#tuyas#suyo#suya#suyos#suyas#";	
    private static String pronombresRelativos = "#que#quien#cual#cuyo#cuya#";
    private static String pronombres = pronombresPersonales + pronombresPosesivos + pronombresRelativos;
	private static String preposiciones = "#a#ante#bajo#con#contra#de#desde#durante#en#entre#hacia#hasta#mediante#para#por#pro#según#sin#sobre#tras#vía#";
	private static String adverbios = "#aquí#allí#ahí#allá#acá#arriba#abajo#cerca#lejos#delante#detrás#encima#debajo#enfrente#atrás#alrededor"+ // De lugar
    	"#pronto#tarde#temprano#todavía#aún#ya#ayer#hoy#mañana#siempre#nunca#jamás#próximamente#prontamente#anoche#enseguida#ahora#mientras"+ // De tiempo absoluto
    	"#antes#después#posteriormente#primeramente#primero#respectivamente"+ // De tiempo relativo
    	"#bien#mal#regular#despacio#deprisa#así#aprisa#adrede#peor#mejor#fielmente#estupendamente#fácilmente"+ // De modo
    	"#poco#mucho#bastante#más#menos#algo#demasiado#casi#sólo#solamente#tan#tanto#todo#nada#aproximadamente"+ // De cantidad  (+ #cada vez más#)
    	"#sí#también#cierto#ciertamente#efectivamente#claro#exacto#verdaderamente"+ // De afirmación
    	"#no#jamás#nunca#tampoco#negativamente"+ // De negación 
        "#quizá#quizás#acaso#probablemente#posiblemente#seguramente"+ // De duda
		"#tal#como"+ // De comparación (+ #mejor que#peor que#) 
		"#muy"+
		"#dentro"+  // Adverbio que faltaba
		"#";
	// Más adverbios = acabados en "mente" excepto los que sean conjugaciones verbales (como "aumente") - comprobar después que los verbos
    private static String conjunciones = "#luego#y#e#o#u#sino#pero#ni#que"+
    	"#empero#excepto#salvo#menos#sino#mas#tanto#si#cuando#mientras#apenas#luego#cuando"+
    	"#donde#adonde#como#según#conforme#manera#suerte#tan#más#menos"+
    	"#porque#pues#conque#manera#aunque#como#siempre#";
    
    private static String interrogativos = "#qué#cuál#cuáles#cómo#cuántos#cuánto#cuántas#cuánta#cuándo#dónde#quién#quiénes#";
    private static String interjecciones = "#adiós#ah#arre#aúpa#ay#bah#chas#chist#chitón#ea#eh#hala#hale#hey#hola#huy"+
    	"#miau#oh#ojalá#ole#olé#plaf#psche#pst#puf#pum#quia#so#uf#uh#zas";

    private static String otrasParticulas = "#al#del#";

    private static String[] particulasCompuestas = { "cada vez más", "mejor que", "peor que", "tal vez" // adverbios compuestos
    	, "cada vez más", "mejor que", "peor que", "tal vez", "con que", "así que", // conjunciones compuestas
		"sin embargo", "con todo", "a pesar de", "no obstante", "más bien", "sino que", "antes bien", "al contrario",
		"o sea", "esto es", "es decir", "mejor dicho", "es más", "de que", "después de que", "antes de que", "no bien", "así que", "de que", "en cuanto", "luego que",
		"antes que", "después que", "mientras que", "en tanto", "siempre que", "ahora que", "desde que", "hasta que", "una vez que", "por donde", "en donde", "desde donde",
		"como si", "de la forma", "de modo que", "ya que", "así que", "por tanto", "así pues", "de forma",
		"modo que", "suerte que", "por más que", "a pesar de que", "mismo que", "con todo", "antes bien", "bien que", "pese a que",
		"para que", "a fin de que", "con el cometido", "intención de que", "propósito de que",
		"caso que", "en el caso de que", "con tal que", "puesto caso que", "supuesto que", "a menos que"
		, "a lo largo" };  // Otras combinaciones de partículas compuestas
	
// TODO: Otros aspectos idiomáticos a resolver, por ejemplo ambigüedad locución conjutiva "junto a", frases hechas como "acto seguido"  ...

    
    
	// Todas las siguientes son ambiguedades con verbos, y suponemos que son verbos solo si: 1) no hay otro verbo en la frase
	// private static String ambiguedadesArt = "#una#uno#"; 
	// private static String ambiguedadesPrep = "#entre#para#";
	// private static String ambiguedadesAdv = "#cerca#encima#enfrente#tarde#nada#";
	// private static String ambiguedadesInt = "#vaya#";
	private static String[] ambiguedadesPrevias = { "para", "nada", "entre", "uno", "vaya", "tarde", "una", "enfrente", "cerca", "encima" };
		// Palabras que se han detectado como adverbios, conjunciones, etc. y podrían ser verbos
	private static String[] ambiguedadesPosteriores = { "agua", "aire", "pie", "pies", "bata", "batas", "pueblo", "tienda", "tiendas",
		"oído" };
		// Palabras que han podido ser detectadas como verbos y que es probable que no lo sean (sólo se quedan como verbos si son verbo único)
	
	// Suposición: Las perífrasis siempre acaban en el verbo principal
	private static Vector<String> perifrasisVerbales = initVector( "verbos\\perifrasisVerbales.dat"); 	
	private static Vector<String> expresionesVerbales = initVector( "verbos\\expresionesVerbales.dat"); 	
	private static Vector<String> initVector( String fileName ) {
		Vector<String> result = new Vector<String>();
        File vFile = new File(fileName);
    	if (!vFile.exists()) {
    		// Something to advise user
    		if (DEBUG_ERROR) utils.Debug.show( "File not found: " + fileName );
    	} else {
    	    try {
		        BufferedReader input =  new BufferedReader(new FileReader(vFile));
		        String line = null;
		        while ((line = input.readLine()) != null) {
		        	line.trim();
					if (line.startsWith("//") || line.trim().equals("")) {   // Comment
					} else {
						if (!result.contains( line ))  // Si ya existe en result, sólo lo guardamos una vez
							result.add( line );
					}
		        }
    	    } catch (IOException ex){
    	    	if (DEBUG_ERROR) utils.Debug.show( "Error en fichero " + fileName );
    	    }
    	}
		return result;
	}

	
	// En los patrones de perífrasis o tiempos compuestos...
	// [verbo] indica verbo conjugado; [gerundio], [infinitivo], [participio] indican la forma verbal precisa. 
	//     Este verbo funciona como el principal de la perífrasis (es el que da el sentido)
	// El otro verbo, en mayúsculas, (p ej [TOMAR]) es el auxiliar de la perífrasis. Se conjuga en la frase de CUALQUIER MODO (incluido reflexivo). 
	//     Si aparece en reflexivo (-SE) significa que tiene que conjugarse en reflexivo obligatoriamente
	
	// Los verbos conjugados admiten tanto sufijos como prefijos
	// Los infinitivos y los gerundios admiten solo sufijos
	// Los participios no admiten prefijos ni sufijos
		
	
	private static final float CONFIANZA_VERBO_OK = 0.35F;
	private static final float CONFIANZA_VERBO_SIMPLE = 0.8F;
	private static final float CONFIANZA_ADD_TILDE_SUFIJO = 0.15F;  // A añadir a la confianza si falla por una tilde en sufijo enclítico	
	
	public static String quitarTildes(String texto)
	{
		String result = texto.replace('á','a');
		result = result.replace ('é','e');
		result = result.replace ('í','i');
		result = result.replace ('ó','o');
		result = result.replace ('ú','u'); 
		return result;
	}

	/** Analiza si la palabra indicada tiene alguno de los sufijos del array.
	 * post: Si es true, se puede consultar la raíz (atributo raizDePalabra) y el sufijo (atributo sufijoDePalabra)
	 * @param pal	Palabra (en minúsculas) a analizar
	 * @param exactMatch	true si la palabra tiene que ser exacta incluyendo tildes, false las tildes no se consideran
	 * @param arraySufijos	Sufijos a analizar
	 * @return	true si la palabra tiene alguno de los sufijos
	 */
	private static boolean puedeTenerSufijo( String pal, boolean exactMatch, String[] arraySufijos ) {
		raizDePalabra = "";
		sufijoDePalabra = "";
		String palST = quitarTildes(pal);
		if (exactMatch) palST = pal;
		for (String suf : arraySufijos) {
			if (palST.endsWith( suf )) {
				raizDePalabra = pal.substring( 0, pal.length() - suf.length() );
				sufijoDePalabra = suf;
				return true;
			}
		}
		return false;
	}
	
	private static String raizDePalabra = "";
	private static String sufijoDePalabra = "";
	
	/** Devuelve la información de si una palabra es una forma verbal, chequeando todos los posibles sufijos
	 * pre: pal es sólo una palabra (no tiene espacios), en minúsculas (puede tener tildes)
	 * post: tras llamar a esta función se pueden chequear los métodos getConjugacion(), variasConjugaciones()
	 * @param pal	Palabra a chequear
	 * @param exactMatch	true si la palabra tiene que ser exacta incluyendo tildes, false las tildes no se consideran
	 * @return	true si es una forma verbal, false si no lo es
	 */
	public static boolean esFormaVerbal( String pal, boolean exactMatch ) {
		lastConjugacion = null;
		lastVectorConjs = new Vector<Conjugacion>();
		if (pal.length()<2) return false;
		return esFormaVerbal( pal, exactMatch, true );
	}
	
	/** Devuelve la información de si una palabra es una forma verbal
	 * @param pal
	 * @param exactMatch	true si se quiere chequear las tildes de forma exacta
	 * @param checkSuffixes	true si se quieren chequear posibles sufijos verbales enclíticos (me, la, mela...) 
	 * @return
	 */
	private static boolean esFormaVerbal( String pal, boolean exactMatch, boolean checkSuffixes ) {
		pal = pal.toLowerCase();
		// Algorithm...
		// 1. Search direct form in irregular verbs
		// TODO: TO ELIMINATE THIS
		boolean esF = VerbosIrregulares.esFormaVerbal( pal, exactMatch );
		if (esF) {
			if (lastConjugacion == null || VerbosIrregulares.getConjugacion().getConfianza() > lastConjugacion.getConfianza()) {
				lastConjugacion = VerbosIrregulares.getConjugacion();
			}
			lastVectorConjs.addAll( VerbosIrregulares.variasConjugaciones() );
		} else {
			// 2. Search direct form in regular & another irregular verbs
			esF = ConjugacionesVerbales.esFormaVerbal( pal, exactMatch, false );
			if (esF) {
				if (lastConjugacion == null || ConjugacionesVerbales.getConjugacion().getConfianza() > lastConjugacion.getConfianza()) {
					lastConjugacion = ConjugacionesVerbales.getConjugacion();
				}
				lastVectorConjs.addAll( ConjugacionesVerbales.variasConjugaciones() );
			} else if (checkSuffixes) {
				// 3. Analize possible suffixes
				// exactmatch goes to false anyway (suffixes of this kind can change the accent possition in spanish. Ex: cantaba = cantábamela)
				int longSufijo = SUFIJO_ENCLITICO_MENOR;
				while (pal.length() >= longSufijo+2 && longSufijo <= SUFIJO_ENCLITICO_MAYOR) {
					esF = checkSufijosEncliticos( pal, false, sufijosEncliticosReflexivosCompuestos[longSufijo-4], true ) || esF;
					longSufijo++;
				}
				esF = checkSufijosEncliticos( pal, false, sufijosEncliticosReflexivosDirectos, true ) || esF;
				esF = checkSufijosEncliticos( pal, false, sufijosEncliticosNoReflexivos, false ) || esF;
			}
		}
		return esF;
	}

	private static boolean checkSufijosEncliticos( String pal, boolean exactMatch, String[] sufs, boolean setReflexivoOn ) {
		if (puedeTenerSufijo(pal, exactMatch, sufs)) {
			Conjugacion templC = lastConjugacion;
			int tempLVC = lastVectorConjs.size();
			String formaVerbal = raizDePalabra;
			String sufijoEnclitico = sufijoDePalabra;
			if (esFormaVerbal(formaVerbal, exactMatch, false)) {  // Inner call for second ("recursive") process
				for (int i = lastVectorConjs.size()-1; i >= tempLVC; i--) {  // Recorrer y marcar las nuevas incorporaciones
					if (lastVectorConjs.get(i).esParticipio()) { //Atención: los participios no admiten enclíticos. Quitar
						lastVectorConjs.remove(i);
					} else { // Normal (no participio) - admite sufijos. Marcar como enclítico y como reflexivo si procede
						lastVectorConjs.get(i).setSufijoEnclitico( sufijoEnclitico );
						if (setReflexivoOn) lastVectorConjs.get(i).setReflexivo(true);
						lastVectorConjs.get(i).setConjugacion( pal );
						if (lastVectorConjs.get(i).getConfianza() < CONFIANZA_VERBO_SIMPLE) 
							lastVectorConjs.get(i).setConfianza( lastVectorConjs.get(i).getConfianza() + CONFIANZA_ADD_TILDE_SUFIJO );
					}
				}
				if (lastConjugacion != templC) {  // Marcar la principal seleccionada
					if (lastConjugacion.esParticipio()) { //Atención: los participios no admiten enclíticos. Quitar
						lastConjugacion = templC;
						return false;
					} else {  // Normal (no participio) - admite sufijos. Marcar la principal como enclítica y reflexivo si procede
						lastConjugacion.setSufijoEnclitico( sufijoEnclitico );
						if (setReflexivoOn) lastConjugacion.setReflexivo(true);
						lastConjugacion.setConjugacion( pal );
						if (lastConjugacion.getConfianza() < CONFIANZA_VERBO_SIMPLE) 
							lastConjugacion.setConfianza( lastConjugacion.getConfianza() + CONFIANZA_ADD_TILDE_SUFIJO );
					}
					return true;
				}
			}
		}
		return false;
	}
	
	
	private static Conjugacion lastConjugacion = null;
	/** Devuelve la conjugación de la última forma verbal buscada y encontrada
	 * @return	Ultima forma verbal encontrada, null si no se encontró (esFormaVerbal = false)
	 */
	public static Conjugacion getConjugacion() {
		return lastConjugacion;
	}

	private static Vector<Conjugacion> lastVectorConjs = null; 
	/** Devuelve todas las conjugaciones posibles de la forma verbal pasada a esFormaVerbal
	 * @return	Vector de todas las formas verbales encontradas que encajan con la palabra indicada, vacío si no se encontró ninguna
	 */
	public static Vector<Conjugacion> variasConjugaciones() {
		return lastVectorConjs;
	}
		
	/** Chequea un infinitivo verbal en minúsculas y sin tildes para ver si es un verbo regular
	 * @param pal	Verbo a chequear (minúsculas, sin tildes)
	 * @return	true si es regular, false si es irregular o no es un verbo
	 */
	public static boolean esVerboRegular(String pal) {
		return ConjugacionesVerbales.esVerboRegular(pal);
	}	
	
	/** Chequea un infinitivo verbal en minúsculas y sin tildes para ver si es un verbo irregular
	 * @param pal	Verbo a chequear (minúsculas, sin tildes)
	 * @return	true si es irregular, false si no está en la lista de verbos irregulares soportados, o no es un verbo
	 */
	public static boolean esVerboIrregular(String pal) {
		return VerbosIrregulares.esVerboIrregular(pal);
	}	

	
	/** Conjuga un verbo con todos los tiempos. Si el verbo no existe devuelve string vacío
	 * @param verbo	Verbo a conjugar (en infinitivo)
	 * @return	Conjugaciones del verbo en líneas separadas por \n
	 */
	public static String conjugarVerbo( String verbo ) {
		return ConjugacionesVerbales.conjugarVerbo( verbo );
	}

	/** Devuelve una conjugación particular total de un verbo dado. Si el verbo no está en la lista de verbos existentes
	 * o hay algún error en los parámetros, devuelve el string vacío
	 * @param verbo	Verbo a conjugar (en infinitivo)
	 * @param tiempo	Tiempo verbal ("presente", "participio" ...)
	 * @param modoSubjuntivo	Modo verbal (true para subjuntivo, false para indicativo)
	 * @param persona	Persona (0 - impersonal, 1-3 - primera a tercera persona)
	 * @param numeroPlural	Número (true para plural, false para singular)
	 * @return	String con las conjugaciones. Primera línea indica el nombre del verbo, si es o no irregular, y el modelo de conjugación verbal usado.
	 */
	public static String conjugarVerbo( String verbo, String tiempo, boolean modoSubjuntivo, int persona, boolean numeroPlural ) {
		return ConjugacionesVerbales.conjugarVerbo(verbo, tiempo, modoSubjuntivo, persona, numeroPlural );
	}
	
	/** Analiza un texto en busca de formas verbales
	 * @param texto	Texto a analizar
	 * @param revisarPerifrasis	indica si deben o no buscarse perífrasis (si no es así, quedarán los verbos separados)
	 * @param revisarExpVerbales	indica si deben o no revisarse las expresiones verbales (ej: "dar vueltas")
	 * @return	Expresión formada con todos los verbos encontrados en el texto
	 */
	public static Expresion analizarTextoConFormasVerbales( String texto, boolean revisarPerifrasis, boolean revisarExpVerbales ) {
		Expresion resul = new Expresion(texto);
		// 1. Separamos el texto en frases (puntos) ...
		int numFrase = 0;
		int offsetInicio = 0;  // offset de inicio de la frase
		while (offsetInicio < texto.length()) {
			if (DEBUG_IDENTIFICACION) Debug.show("Frase " + numFrase+1 + ": ");
			Expresion exp = analizaFrase( texto, offsetInicio, revisarPerifrasis, revisarExpVerbales );
			// 2. Añadimos a una expresión que solo tenga los verbos
			int posi = exp.buscaTexto( VerboConjugado.class, "", 0, false);
			while (posi >= 0) {
				resul.add( exp.getTC(posi) );
				posi = exp.buscaTexto( VerboConjugado.class, "", posi+1, false);
			}
			// 3. Avanzamos a la siguiente frase
			offsetInicio = exp.getOffsetFin();
			numFrase++;
		}
		// 4. Hasta acabar
		return resul;
	}

	/** Revisa la siguiente frase de un texto
	 * @param texto	Referencia al texto original a revisar
	 * @param offsetInicio	Offset en el que empezar la revisión
	 * @param revisarPerifrasis	indica si se quieren identificar o no las perífrasis verbales
	 * @param revisarExpVerbales	indica si se quieren identificar o no las expresiones verbales
	 * @return	Expresión completa de la siguiente frase
	 */
	public static Expresion analizaFrase( String texto, int offsetInicio, boolean revisarPerifrasis, boolean revisarExpVerbales  ) {
		// 1. Creamos expresión a devolver
		Expresion exp = new Expresion(texto);
		// 2. Separamos cada frase en palabras
		int offEnFrase = 0;
		// palabras, números y símbolos (esp,tab,ret). Las palabras agruparán luego en formas verbales y perífrasis verbales
		// Los símbolos son los ortográficos () y el resto.
		while (offsetInicio+offEnFrase < texto.length() && texto.charAt(offsetInicio+offEnFrase)!='.') {
			// switch (fraseTxt.charAt(offsetInicio+offEnFrase))
			if (esAlfabetico(texto, offsetInicio+offEnFrase)) {   // Procesar palabra
				int off2 = 1; while (esAlfabetico(texto, offsetInicio+offEnFrase+off2)) off2++;
				String palabra = texto.substring( offsetInicio+offEnFrase, offsetInicio+offEnFrase+off2 ).toLowerCase();
				if (prePalabrasReflexivas.contains("#"+palabra+"#")) 
					exp.add( new ParticulaReflexiva( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + off2 ));
				else if (articulosDeterminados.contains("#"+palabra+"#"))
					exp.add( new ArticuloDeterminado( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + off2 ));
				else if (articulosIndeterminados.contains("#"+palabra+"#"))
					exp.add( new ArticuloIndeterminado( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + off2 ));
				else if (pronombres.contains("#"+palabra+"#"))
					exp.add( new Pronombre( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + off2 ));
				else if (preposiciones.contains("#"+palabra+"#"))
					exp.add( new Preposicion( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + off2 ));
				else if (adverbios.contains("#"+palabra+"#"))
					exp.add( new Adverbio( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + off2 ));
				else if (conjunciones.contains("#"+palabra+"#"))
					exp.add( new Conjuncion( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + off2 ));
				else if (interrogativos.contains("#"+palabra+"#"))
					exp.add( new Interrogativo( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + off2 ));
				else if (interjecciones.contains("#"+palabra+"#"))
					exp.add( new Otro( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + off2 ));
				else if (otrasParticulas.contains("#"+palabra+"#"))
					exp.add( new Otro( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + off2 ));
				else if (palabra.length()>7 && palabra.substring( palabra.length()-5 ).equals("mente"))  // Adverbios acabados en mente
						exp.add( new Adverbio( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + off2 ));
				else if (esFormaVerbal(palabra, false) && (getConjugacion().getConfianza() > CONFIANZA_VERBO_SIMPLE))   // Posible verbo? 						
					exp.add( new VerboSimple( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + off2, getConjugacion() ));
				else { // Si no, palabra "normal" (el resto de casos: sustantivo, adjetivo, partículas mal detectadas, etc.
					exp.add( new Palabra( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + off2 ));
				}
				offEnFrase += off2;
			} else if (esNumerico(texto, offsetInicio+offEnFrase)) {   // Procesar número
				int off2 = 1; 
				while (esNumerico(texto, offsetInicio+offEnFrase+off2) || (esPuntoOComa(texto, offsetInicio+offEnFrase+off2) && esNumerico(texto, offsetInicio+offEnFrase+off2+1))) 
					off2++;
				exp.add( new Numero( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + off2 ));
				offEnFrase += off2;
			} else if (esSeparador(texto, offsetInicio+offEnFrase)) {   // Procesar separador
				int off2 = 1; while (esSeparador(texto, offsetInicio+offEnFrase+off2)) off2++;
				exp.add( new Separador( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + off2 ));    // No hace falta gestionar separadores
				offEnFrase += off2;
			} else if (esSimbolo(texto, offsetInicio+offEnFrase)) {  // Procesar símbolo
				exp.add( new Simbolo( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + 1 ));
				offEnFrase++;
			} else {  // Procesar otro
				exp.add( new UnidadTextual( texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + 1 ));
				offEnFrase++;
			}
		}
		// 3. Añadimos el punto al final de la frase (excepto si es la última)
		if (offsetInicio + offEnFrase < texto.length()) {
			exp.add( new Simbolo(texto, offsetInicio + offEnFrase, offsetInicio + offEnFrase + 1) );
			offEnFrase++;
		}
		// 4. Resolvemos algunas ambigüedades verbales (posibles verbos)
		int cuantosVerbosHay = exp.conteoDe( VerboPrincipal.class, "", false ); // Ver cuántos verbos hay en la frase
		if (cuantosVerbosHay == 0) {  //  Si no hay ningún verbo en la frase, buscar partículas ambiguas que pueden promocionar a verbo 
			for (String a : ambiguedadesPrevias) {  
				int pos = exp.buscaTexto( UnidadTextual.class, a, 0, false );  // Busca el texto exacto en minúsculas de la ambigüedad en una unidad de exp y devuelve su posición
				while (pos >= 0) {
					// Encontrada una ambigüedad: si se pilla el verbo, reemplazarla...
					// Posible verbo?  (lo será, si no no sería ambigüedad 						
					if (esFormaVerbal(a, false) && (getConjugacion().getConfianza() > CONFIANZA_VERBO_OK)) {  // Posible verbo? 						
						exp.reemplazar( pos, pos, new VerboSimple( texto, exp.getTC(pos).getOffsetIni(), exp.getTC(pos).getOffsetFin(), getConjugacion() ));
						break;  // Solo cambiamos una, la primera (a suertes)  :-)   - se podría hacer if en vez de while...
					} else {  // Caso que no debería darse
						if (DEBUG_ERROR) Debug.show( "Error: Verbo ambiguo no encontrado");
					}
					pos = exp.buscaTexto( UnidadTextual.class, a, pos+1, false );
				}
			}
		} else if (cuantosVerbosHay > 1) {  //  Si hay más de un verbo en la frase, buscar partículas ambiguas que pueden reducirse a no-verbos
			for (String a : ambiguedadesPosteriores) {
				int pos = exp.buscaTexto( TextoConceptual.class, a, 0, false );  // Busca el texto exacto en minúsculas y verbo, de la ambigüedad en una unidad de exp y devuelve su posición
				while (pos >= 0) {
					// Encontrada una ambigüedad: si es verbo, reemplazarla por palabra (genérica) ... 
					// TODO: En vez de palabras se podrían estructurar las ambiguedades por tipos léxicos
					if (exp.getTC(pos) instanceof VerboSimple) {
						exp.reemplazar( pos, pos, new Palabra( texto, exp.getTC(pos).getOffsetIni(), exp.getTC(pos).getOffsetFin() ));
						cuantosVerbosHay--;
						if (cuantosVerbosHay == 1) break;  // Cambiamos las que haya pero dejando siempre algún verbo... empezando por la izquierda (a suertes)  :-)  // 
					}
					pos = exp.buscaTexto( TextoConceptual.class, a, pos+1, false );
				}
			}
		}
		if (DEBUG_IDENTIFICACION) {
			Debug.show( "*** FRASE: <" + texto.substring( offsetInicio, offsetInicio + offEnFrase ) + ">" );
			Debug.show( "  Inic.: " + exp );
		}
		// 5.- Si hay partículas compuestas (como "a lo largo", "sin embargo"...) y ambiguamente se ha interpretado como verbo,
		// se quita de ser verbo
		try {
			for (String particula : particulasCompuestas) {
				int[] poss = exp.buscaPatron( particula, 0, false, true, true );
				while (poss != null) {
					verbosAPalabras( exp, poss[0], poss[1] );  // primero - último
					poss = exp.buscaPatron( particula, poss[1]+1, false, true, true );
				}
			}
		} catch (ExpresionVerboException ex) {  // No ocurre (sólo patrón mal formado)
		}
		// 6. Buscar posibles tiempos compuestos
		String tiempoCompuesto = "[HABER] [participio]";
		try {
			int[] poss = exp.buscaPatron( tiempoCompuesto, 0, false );  // patrón = [HABER] [participio]
			while (poss != null) {
				VerboCompuesto verboComp = new VerboCompuesto( exp, poss[0], poss[1] );
				exp.reemplazar( poss[0], poss[1], verboComp );   
					// introduce una nueva expresión donde antes había dos unidades (con posibles separadores en medio)
				poss = exp.buscaPatron( tiempoCompuesto, poss[1]+1, false );
			}
		} catch (ExpresionVerboException ex) {  // No ocurre (sólo patrón mal formado)
		}
		if (DEBUG_IDENTIFICACION) Debug.show("  Comp.: " + exp);
		// 7. Buscar posibles formas verbales de prefijo ("me lo dijiste", "me lo has dicho", "se cayó", "la coge"...)
		// prePalabrasReflexivas = "#me#te#se#nos#os#";
		// articulosDeterminados = "#el#la#las#le#les#lo#los#";
		String[] formasVerbales = { "[refl] [artd] [verbo]", "[refl] [verbo]", "[artd] [verbo]" };
		try {
			for ( String formaVerbal : formasVerbales ) {
				int[] poss = exp.buscaPatron( formaVerbal, 0, false );
				while (poss != null) {
					int posF = (poss.length==2)?poss[1]:poss[2];
					FormaVerbal fv = new FormaVerbal( exp, poss[0], posF );
					// Excepción: "el" no vale
					if (fv.getArticulo() == null || !(fv.getArticulo().getTextoMinusc().equals("el")))
						exp.reemplazar( poss[0], posF, fv );
						// introduce una nueva expresión donde antes había varias unidades (con posibles separadores en medio)
					poss = exp.buscaPatron( formaVerbal, posF+1, false );
				}
			}
		} catch (ExpresionVerboException ex) {  // No ocurre (sólo patrón mal formado)
		}
		if (DEBUG_IDENTIFICACION) Debug.show("  F.Ver: " + exp);
		// 8. Aplica heurísticas para quitar formas verbales ambiguas que pueden ser sustantivos o adjetivos
		//  (ej: un eructo, en la sala...)
		filtraHeuristicas( exp );
		if (DEBUG_IDENTIFICACION) Debug.show( "  Heur.: " + exp );
		// 9. Buscar posibles perífrasis verbales ("acabó entendiéndolo", "anda tambaleando", "no le basta con mirar"...)
		if (revisarPerifrasis) {
			try {
				for ( String perifrasis : perifrasisVerbales ) {
					int[] poss = exp.buscaPatron( perifrasis, 0, false, true, true );
					while (poss != null) {
						int posPrinc = poss.length-2;  // posición del verbo principal [ primero ... verboAux ... verboPrinc ultimo ]
						// Entendemos que el último verbo patrón de la perífrasis es el del verbo principal
						// Buscamos el verbo auxiliar de la perífrasis
						int posAux = 1;
						while (posAux < posPrinc && !(exp.getTC(poss[posAux]) instanceof VerboConjugado)) {
							posAux++;
						}
						if (posAux < posPrinc && exp.getTC(poss[posPrinc]) instanceof VerboConjugado) {   // Si no, error indeterminado en búsqueda de perífrasis
							PerifrasisVerbal pv = new PerifrasisVerbal( exp, poss[0], poss[poss.length-1], poss[posAux], poss[posPrinc] );
							exp.reemplazar( poss[0], poss[poss.length-1], pv );
							// introduce una nueva expresión donde antes había varias unidades (con posibles separadores en medio)
						}
						poss = exp.buscaPatron( perifrasis, poss[posPrinc]+1, false, true, true );
					}
				}
			} catch (ExpresionVerboException ex) {  // No ocurre (sólo patrón mal formado)
			}
			if (DEBUG_IDENTIFICACION) Debug.show("  P.Ver: " + exp);
		}
		// 10. Buscar posibles expresiones verbales de interés ("[dar] por [adjetivo]", "[hacer] de vientre"...)
		// prePalabrasReflexivas = "#me#te#se#nos#os#";
		// articulosDeterminados = "#el#la#las#le#les#lo#los#";
		if (revisarExpVerbales) {
			try {
				for ( String eV : expresionesVerbales ) {
					int[] poss = exp.buscaPatron( eV, 0, false, true, true );
					while (poss != null) {
						if (exp.getTC(poss[1]) instanceof VerboConjugado) {   // Si no, error indeterminado en búsqueda de perífrasis
							ExpresionVerbal ev = new ExpresionVerbal( exp, poss[0], poss[poss.length-1], poss[1] );
							exp.reemplazar( poss[0], poss[poss.length-1], ev );
							// introduce una nueva expresión donde antes había varias unidades (con posibles separadores en medio)
						}
						poss = exp.buscaPatron( eV, poss[poss.length-1]+1, false, true, true );
					}
				}
			} catch (ExpresionVerboException ex) {  // No ocurre (sólo patrón mal formado)
			}
			if (DEBUG_IDENTIFICACION) Debug.show("  E.Ver: " + exp);
		}
		return exp;
	}

	
	private static boolean esAlfabetico(String f, int off) { return (off < f.length() && Character.isLetter(f.charAt(off))); }
	private static boolean esNumerico(String f, int off) { return (off < f.length() && Character.isDigit(f.charAt(off))); } 
	private static boolean esSeparador(String f, int off) { return (off < f.length() && Character.isWhitespace(f.charAt(off))); } 
	private static boolean esPuntoOComa(String f, int off) { return (off < f.length() && (f.charAt(off)=='.' || f.charAt(off) == ',')); }    	
	// esSimbolo: Símbolos reconocidos: , ; ¿ ? ¡ ! ( ) \ ' "
	private static boolean esSimbolo(String f, int off) { return (off < f.length() && (f.substring(off,off+1).matches("[,;¿?¡!()'\"]")));  }    	

	private static void filtraHeuristicas( Expresion exp ) {
		TextoConceptual tc = null;
		String listaUnOPosesivo = "#un#el#al#del#mi#tu#su#mis#tus#sus#";
		String listaPronPers = "#yo#tú#él#ella#ello#usted#nosotros#nosotras#vosotras#vosotros#ellos#ellas#ustedes#";
		boolean esPreposicion = false; // excepto "según"
		boolean esUnOPosesivo = false; // "#un" + "#mi#tu#su#mis#tus#sus#" 
		boolean esPronombrePersonal = false; // #yo#tú#él#ella#ello#usted#nosotros#nosotras#vosotras#vosotros#ellos#ellas#ustedes#
			// Si además no es precedido de preposición (en ese caso puede cerrar frase relativa, p ej "el amor que hay [en tí] hará milagros"
		String lastTC = "";
		for (int i = 0; i < exp.numTCs(); i++) {
			tc = exp.getTC(i);
			if (tc instanceof Preposicion && !tc.getTextoMinusc().equals("según")) { esPreposicion = true; esUnOPosesivo = false; esPronombrePersonal = false; }
			else if (listaUnOPosesivo.contains("#"+tc.getTextoMinusc()+"#")) { esUnOPosesivo = true; esPreposicion = false; esPronombrePersonal = false; }
			else if (listaPronPers.contains("#"+tc.getTextoMinusc()+"#") && !esPreposicion) { esPronombrePersonal = true; esPreposicion = false; esUnOPosesivo = false; } 
			else {
				VerboConjugado vc = null;
				boolean noEsVerbo = false;
				if (tc instanceof VerboConjugado) {
					vc = (VerboConjugado) tc;
					// 1.- Después de una preposición, excepto "según", no puede venir un verbo conjugado (sí en infinitivo-participio). 
					//     Ejemplos:  NO VERBOS: "mediante canto" "por nada"  //  SI VERBOS: "Por nadar le amonestaron" "todo consiste en parar quieto"
					if (esPreposicion && !vc.esInfinitivo() && !vc.esParticipio()) noEsVerbo = true;
					// 2.- Después de "un" o cualquier artículo o pronombre posesivo de la siguiente lista no puede venir un verbo conjugado (sí inf/par). Ejemplos:  "un eructo"
					//		"#un" + "#el#al#del#"+ "#mi#tu#su#mis#tus#sus#"
					if (esUnOPosesivo && !vc.esInfinitivo() && !vc.esParticipio()) noEsVerbo = true;
					// 3.- Si después de pronombre personal (A) que no venga precedido de preposición 
					// hay un verbo, tiene que ser gerundio, participio, o concordar en persona y número
					//     (A) #yo#tú#él#ella#ello#usted#nosotros#nosotras#vosotras#vosotros#ellos#ellas#ustedes#"+
					if (esPronombrePersonal && !vc.esGerundio() && !vc.esParticipio()) {
						if (vc.getConjugacion() != null) {
							int persona = 3;
							boolean numeroPlural = true;  // Por omisión [ellos] (3a plural)
								if (lastTC.equals("yo")) { persona = 1; numeroPlural = false; }
								else if (lastTC.equals("tú")) { persona = 2; numeroPlural = false; }
								else if ("#él#ella#ello#usted#".contains("#"+lastTC+"#"))
									{ persona = 3; numeroPlural = false; }
								else if ("#nosotros#nosotras#".contains("#"+lastTC+"#"))
									{ persona = 1; }
								else if ("#vosotros#vosotras#".contains("#"+lastTC+"#"))
									{ persona = 2; }
							if (persona != vc.getConjugacion().getPersona() || 
								numeroPlural != vc.getConjugacion().getNumeroPlural())
								noEsVerbo = true;
						}
					}
					if (noEsVerbo) {  // Se ha dado algún caso 1-2-3. Quitamos el verbo
						if (vc instanceof Expresion) {  // Tratamiento si es verbo compuesto, expresión verbal...
							Expresion nuevaE = verbosAPalabras( (Expresion) vc );
							exp.reemplazar( i, i, nuevaE );
						} else {  // Tratamiento si es verbo simple
							Palabra p = new Palabra(exp.getTextoOriginalCompleto(), vc.getOffsetIni(), vc.getOffsetFin());
							exp.reemplazar( i, i, p );
						}
					}
				} 
				if (!(tc instanceof Separador)) {
					esPreposicion = false;
					esUnOPosesivo = false;
					esPronombrePersonal = false;
				}
			}
			if (!(tc instanceof Separador)) lastTC = tc.getTextoMinusc();
		}
		// 4.- Si hay dos verbos SEGUIDOS y uno es conjugado y tiene prefijo artículo "la", "el", "las", "los" es que es ambiguo (sustantivo),
		// y se elimina de ser verbo: p ej. "<tira> <los botes>" o "<mira> <la danza>" o "<la danza> <es> su vida"
		// No pasa en "<le da> <fuerzas>"
		// Si ninguno tiene prefijo artículo "<das> <fuerzas>" o "<fuerzas> <das>" no se puede resolver automáticamente la ambigüedad,
		// pero como es raro que ocurra suponemos que el correcto es el primero, y quitamos el verbo del segundo
		// salvo que alguno tenga verbo complejo, y entonces quitamos el otro
		String dosConjugados = "[verbo] [verbo]";
		String articulosDets = "#la#el#las#los#";
		try {
			int[] poss = exp.buscaPatron( dosConjugados, 0, false );
			while (poss != null) {
				int pos1 = poss[0];
				int pos2 = poss[1];
				VerboConjugado v1 = (VerboConjugado) (exp.getTC(pos1));
				VerboConjugado v2 = (VerboConjugado) (exp.getTC(pos2));
				String debugDosVerbos = "";
				if (v1.esConjugado() && v2.esConjugado()) {
					debugDosVerbos = debugDosVerbos + "Dos verbos conjugados:\t" + v1.getTextoUnidad() + "\t" + v2.getTextoUnidad();
					if (v1 instanceof FormaVerbal && ((FormaVerbal)v1).getArticulo()!=null && 
							articulosDets.contains( ((FormaVerbal)v1).getArticulo().getTextoMinusc() )) {  // Quitar la forma verbal 1
						Expresion nuevaE = verbosAPalabras( (Expresion) v1 );
						exp.reemplazar( pos1, pos1, nuevaE );
						if (DEBUG_HEURISTICAS) debugDosVerbos = debugDosVerbos + "\tQUITADO EL PRIMERO";
//					} else if (v2 instanceof FormaVerbal && ((FormaVerbal)v2).getArticulo()!=null &&
//							articulosDets.contains( ((FormaVerbal)v2).getArticulo().getTextoMinusc() )) {  // Quitar la forma verbal 2
					} else {  // Por omisión quitamos el segundo salvo si contiene verbo compuesto, expresión verbal o perífrasis
						if (v2 instanceof VerboSimple) {
							verbosAPalabras( exp, pos2, pos2 );
							if (DEBUG_HEURISTICAS) debugDosVerbos = debugDosVerbos + "\tQUITADO EL SEGUNDO";
						} else if (((Expresion)v2).contieneVerboComplejo()) {
							if (v1 instanceof VerboSimple || !((Expresion)v1).contieneVerboComplejo()) {
								verbosAPalabras( exp, pos1, pos1 );
								if (DEBUG_HEURISTICAS) debugDosVerbos = debugDosVerbos + "\tQUITADO EL PRIMERO";
							}
						} else {
							if (v2 instanceof VerboCompuesto || (v2.esReflexivo() && v2 instanceof FormaVerbal) || (v2 instanceof FormaVerbal && "#le#les#".contains(((FormaVerbal)v2).getArticulo().getTextoMinusc()))) {
								if (v1 instanceof VerboCompuesto || (v1.esReflexivo() && v1 instanceof FormaVerbal) || (v1 instanceof FormaVerbal && "#le#les#".contains(((FormaVerbal)v1).getArticulo().getTextoMinusc()))) {
									// Nada (los dos tienen partículas)
									if (DEBUG_HEURISTICAS) debugDosVerbos = debugDosVerbos + "\tDEJADOS LOS DOS";
								} else {
									verbosAPalabras( exp, pos1, pos1 );
									if (DEBUG_HEURISTICAS) debugDosVerbos = debugDosVerbos + "\tQUITADO EL PRIMERO";
								}
							} else {
								verbosAPalabras( exp, pos2, pos2 );
								if (DEBUG_HEURISTICAS) debugDosVerbos = debugDosVerbos + "\tQUITADO EL SEGUNDO";
							}
						}
						if (DEBUG_HEURISTICAS) {
							Debug.setDebug( true );
							debugDosVerbos = debugDosVerbos + "\tEn expresión:\t" + exp.toString();
							Debug.show( debugDosVerbos );
							Debug.setDebug( false );
						}
					}
				}
				poss = exp.buscaPatron( dosConjugados, pos2+1, false );
			}
		} catch (ExpresionVerboException ex) {  // No ocurre (sólo patrón mal formado)
		}
		
		// TODO:
		// 5.- Lo mismo con los artículos indeterminados excepto "un"
		//     "#un#unos#una#unas#esa#ese#eso#esas#esos#esas#esto#esta#este#estos#estas#";
		// 6.- La confianza del subjuntivo debería ser pequeña
		
	}
		
		// Devuelve otra expresión equivalente a e pero quitando todos los verbos y poniéndolos como palabras
		private static Expresion verbosAPalabras( Expresion e ) {
			Expresion nuevaE = new Expresion( e.getTextoOriginalCompleto() );
			for (int j = 0; j < e.numTCs(); j++) {
				TextoConceptual tc2 = e.getTC( j );
				if (tc2 instanceof Expresion) {
					nuevaE.add( verbosAPalabras( (Expresion) tc2 ) );
				} else {
					if (tc2 instanceof VerboSimple) {
						nuevaE.add( new Palabra(e.getTextoOriginalCompleto(), tc2.getOffsetIni(), tc2.getOffsetFin() ) );
					} else {
						nuevaE.add( tc2 );
					}
				}
			}		
			return nuevaE;
		}

		// Modifica la expresión quitando todos los verbos simples que haya en el intervalo indicado
		// (desde desdePos hasta hastaPos, ambos inclusive)
		private static void verbosAPalabras( Expresion e, int desdePos, int hastaPos ) {
			for (int j = desdePos; j <= hastaPos; j++) {
				TextoConceptual tc2 = e.getTC( j );
				if (tc2 instanceof VerboSimple) {
					e.reemplazar( j, j, new Palabra(e.getTextoOriginalCompleto(), tc2.getOffsetIni(), tc2.getOffsetFin() ) );
				} else if (tc2 instanceof Expresion && tc2 instanceof VerboConjugado) {  // Si es expresión verbo se quita cambiando a expresión normal
					e.reemplazar( j, j, verbosAPalabras( ((Expresion)tc2) ) );
				} else if (tc2 instanceof Expresion) {  // Si es expresión no verbo se quita sin cambiar la expr
					verbosAPalabras( ((Expresion)tc2), 0, ((Expresion)tc2).numTCs()-1 );
				}
			}		
		}

	

	// JERARQUIA DE CLASES
	// (paquete utils.verbos.expresiones)
	// CLASES (Object)
	//  +-- UnidadTextual (imp TextoConceptual)   [una palabra única]
	//      +-- VerboSimple (imp VerboConjugado)
	//      +-- ParticulaReflexiva, ArticuloDeterminado
	//      +-- Palabra  (genérico)
	//      +-- Adverbio, ArticuloIndeterminado, Conjuncion, Numero, Preposicion, Pronombre, Interrogativo
	//      +-- Simbolo, Otro
	//      +-- Separador
	//  +-- Expresion  (imp TextoConceptual)   [cualquier conjunto de varias palabras. Por ejemplo una frase]
	//      +-- FormaVerbal  (imp VerboConjugado)  [por ejemplo "me lo dijo"]
	//      +-- VerboCompuesto  (imp VerboConjugado)  [por ejemplo "ha estado"]
	//      +-- PerifrasisVerbal  (imp VerboPrincipal)  [por ejemplo "aciertas a vivirla"]
	//      +-- ExpresionVerbal  (imp VerboConjugado)  [por ejemplo "doy crédito a"]
	// INTERFACES
	//  +-- TextoConceptual
	//  +-- VerboPrincipal
	//      +-- VerboConjugado
	// EXCEPCIONES (Exception)
	//  +-- ExpresionVerboException
	
	// TODO: quitar verbos irregulares repes (los gestionados ad-hoc) del fichero de datos de irregulares sin conjug específica

		private static void testDebug( String s ) {
			Expresion e = analizarTextoConFormasVerbales( s, true, true );
			Debug.show( e.toString() );
		}
		
		
	/** Devuelve el verbo principal de un texto en infinitivo. Si hay varios verbos devuelve el primero. Si no hay ninguno devuelve ""
	 * @param texto	Texto en el que buscar verbos
	 * @return	Verbo principal del texto en infinitivo
	 */
	public static String getVerboPrincipal( String texto ) {
		Expresion exp = UtilsVerbos.analizarTextoConFormasVerbales( texto, true, true );
		for (int i = 0; i < exp.numTCs(); i++ ) {
			TextoConceptual tc = exp.getTC(i);
			if (tc instanceof VerboPrincipal) {
				VerboPrincipal vp = (VerboPrincipal) tc;
				return ( vp.getInfinitivoVerboPrincipal() );
			}
		}
		return "";
	}
	

	private static void prueba1() {
		Debug.setPrefix("* ");

		testDebug( "Devolver los prismáticos después de leer para entender nada."+
				"Se mueve en su tumba y se remueve después de ver el agua y él se dispone a aguar todo para él."+
				"Agua la fiesta." );
		testDebug( "Jorge vuelve a mirar" );
		testDebug( "él según canta sí es verbo, mediante canto no lo es, en nada tampoco. Por nadar es verbo. Consiste en parar también" );
		testDebug(  "No son verbos: un eructo, mi nada, tu tira. Sí lo son: un cantar, mi sentido");
		testDebug( "Sí son verbos: yo cantando, tú mirado. Él nada. No es: yo nada, yo mirar, usted miro" );
		testDebug( "Prueban un cohete. Lleva una bata" );
		testDebug( "empapándose" );
		testDebug( "y entorna la puerta. Acto seguido va al teléfono y marca." );
		testDebug( "Dentro de la estancia, un hombre yace dentro de la cama." );
		testDebug( "La bola cae de su mano, rueda al suelo y se rompe." );
		testDebug( "desliza por el suelo a lo largo del pasillo" );
		testDebug( "Lance se lo ha dibujado en el pecho,");
//		System.exit(0);

		Debug.setDebug( false );

		Debug.show( "tener " + (esVerboIrregular("tener")?"ES":"NO ES") + " un verbo irregular" );
		Debug.show( "cantar " + (esVerboIrregular("cantar")?"ES":"NO ES") + " un verbo irregular" );
		Debug.show( "esconder " + (esVerboRegular("esconder")?"ES":"NO ES") + " un verbo regular" );
		Debug.show( "hacer " + (esVerboRegular("hacer")?"ES":"NO ES") + " un verbo regular" );

		Debug.show( "cantar: " + esFormaVerbal("cantar", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "cantaría: " + esFormaVerbal("cantaría", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "cantaría (forzando tildes): " + esFormaVerbal("cantaría", true) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "cantaria: " + esFormaVerbal("cantaria", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "cantaria (forzando tildes): " + esFormaVerbal("cantaria", true) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "amó: " + esFormaVerbal("amó", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
			for (int k = 0; k < variasConjugaciones().size(); k++ )
				Debug.show( "  " + variasConjugaciones().get(k).show( true ) );

		Debug.show( "yendo: " + esFormaVerbal("yendo", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "cantó: " + esFormaVerbal("cantó", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "dispondrá: " + esFormaVerbal("dispondrá", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "dispondra: " + esFormaVerbal("dispondra", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "dispondra (forzando tildes): " + esFormaVerbal("dispondra", true) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "ir: " + esFormaVerbal("ir", true) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "vuelva: " + esFormaVerbal("vuelva", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
			for (int i = 0; i < variasConjugaciones().size(); i++ )
				Debug.show( "  " + variasConjugaciones().get(i).show( true ) );
		Debug.show( "cantábanoslo (forzando tildes): " + esFormaVerbal("cantábanoslo", true) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "cantábanoslo: " + esFormaVerbal("cantábanoslo", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
			for (int i = 0; i < variasConjugaciones().size(); i++ )
				Debug.show( "  " + variasConjugaciones().get(i).show( true ) );
		Debug.show( "teníasenoslas: " + esFormaVerbal("teníasenoslas", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
			for (int i = 0; i < variasConjugaciones().size(); i++ )
				Debug.show( "  " + variasConjugaciones().get(i).show( true ) );
		Debug.show( "fabricabastelo: " + esFormaVerbal("fabricabastelo", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "dalo: " + esFormaVerbal("dalo", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		for (int i = 0; i < variasConjugaciones().size(); i++ )
			Debug.show( "  " + variasConjugaciones().get(i).show( true ) );
		Debug.show( "sé: " + esFormaVerbal("sé", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "se: " + esFormaVerbal("se", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "haciéndomelo: " + esFormaVerbal("haciéndomelo", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "hacérmelo: " + esFormaVerbal("hacérmelo", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "hechomelo: " + esFormaVerbal("hechomelo", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "si: " + esFormaVerbal("si", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "aborreciste: " + esFormaVerbal("aborreciste", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		for (int k = 0; k < variasConjugaciones().size(); k++ )
			Debug.show( "  " + variasConjugaciones().get(k).show( true ) );
		Debug.show( "acosto: " + esFormaVerbal("acosto", false) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		for (int k = 0; k < variasConjugaciones().size(); k++ )
			Debug.show( "  " + variasConjugaciones().get(k).show( true ) );

		// Procesar verbos de us:
		String fileName = "d:\\t\\US-tracce.txt";
		String fileOut = "d:\\t\\Verbos-tracce.txt";
		String fileOut2 = "d:\\t\\Entrada-tracce.txt";
		String fileOut3 = "d:\\t\\Verbos-desc-tracce.txt";
        File regFile = new File(fileName);
    	PrintStream logFile = null;
    	PrintStream logFile2 = null;
    	PrintStream logFile3 = null;
		try {
			logFile = new PrintStream( new FileOutputStream( fileOut, false /* append */ ) );
			logFile2 = new PrintStream( new FileOutputStream( fileOut2, false /* append */ ) );
			logFile3 = new PrintStream( new FileOutputStream( fileOut3, false /* append */ ) );
		} catch (FileNotFoundException e2) {
		}
    	if (regFile.exists()) {
    	    try {
    	    	String line = "";
		        BufferedReader input =  new BufferedReader(new FileReader(regFile));
		        int numLin = 0;
		        while ((line = input.readLine()) != null) {
		        	line = line.trim();
		        	if (!line.equals("")) {
		        		Expresion e = analizarTextoConFormasVerbales( line, true, true );  // ACCION!!!
		        		StringBuffer b1 = new StringBuffer();
		        		StringBuffer b2 = new StringBuffer();
		        		for (int posi = 0; posi < e.numTCs(); posi++) {
		    				b1.append( e.getTC(posi).getTextoUnidad() + "\t" );
		    				b2.append( ((VerboConjugado)e.getTC(posi)).getConjugacion().getConfianza() + " " + e.getTC(posi).toString() + "\t" );
		    			}
		        		logFile2.println( line.replaceAll( "[\n\r\f\"]", " ") );
		    			logFile.println( b1 );
		    			logFile3.println( b2 );
		        	}
		        	numLin++;
		    		Debug.setDebug( true );
			        Debug.show( "Procesada línea " + numLin + ": " + line );
					Debug.setDebug( false );
					// if (numLin > 100) break;
		        }
		        input.close();
    	    } 
    	    catch (IOException ex){
    	    }
    	}
    	logFile.close();
		logFile2.close();
		logFile3.close();
		utils.Debug.close();
	}

	private static void verbosTotales() {
		Debug.setPrefix("* ");
		Debug.setDebug( false );
		// Procesar verbos de us:
		String fileName = "d:\\t\\VerbosDeUS-201004.txt";
		String fileOut = "d:\\t\\Verbos-tracce-201004.txt";
		String fileOut2 = "d:\\t\\Entrada-tracce-201004.txt";
		String fileOut3 = "d:\\t\\Verbos-desc-tracce-201004.txt";
        File regFile = new File(fileName);
    	PrintStream logFile = null;
    	PrintStream logFile2 = null;
    	PrintStream logFile3 = null;
		try {
			logFile = new PrintStream( new FileOutputStream( fileOut, false /* append */ ) );
			logFile2 = new PrintStream( new FileOutputStream( fileOut2, false /* append */ ) );
			logFile3 = new PrintStream( new FileOutputStream( fileOut3, false /* append */ ) );
		} catch (FileNotFoundException e2) {
		}
    	if (regFile.exists()) {
    	    try {
    	    	String line = "";
		        BufferedReader input =  new BufferedReader(new FileReader(regFile));
		        int numLin = 0;
		        while ((line = input.readLine()) != null) {
		        	line = line.trim();
		        	if (!line.equals("")) {
		        		Expresion e = analizarTextoConFormasVerbales( line, true, true );  // ACCION!!!
		        		StringBuffer b1 = new StringBuffer();
		        		StringBuffer b2 = new StringBuffer();
		        		for (int posi = 0; posi < e.numTCs(); posi++) {
		    				b1.append( e.getTC(posi).getTextoUnidad() + "\t" );
		    				b2.append( ((VerboConjugado)e.getTC(posi)).getConjugacion().getConfianza() + " " + e.getTC(posi).toString() + "\t" );
		    			}
		        		logFile2.println( line.replaceAll( "[\n\r\f\"]", " ") );
		    			logFile.println( b1 );
		    			logFile3.println( b2 );
		        	}
		        	numLin++;
		    		Debug.setDebug( true );
			        Debug.show( "Procesada línea " + numLin + ": " + line );
					Debug.setDebug( false );
					// if (numLin > 100) break;
		        }
		        input.close();
    	    } 
    	    catch (IOException ex){
    	    }
    	}
    	logFile.close();
		logFile2.close();
		logFile3.close();
		utils.Debug.close();
	}		

	public static void main( String[] s ) {
		// prueba1();
		verbosTotales();
	}
}
