package utils.verbos;

/** Clase que representa una conjugación verbal
 * Se ordena por valor completo, pero su igualdad se mira con la conjugación (sin tildes)
 * @author andoni
 *
 */
public class Conjugacion extends SufijoVerbal {
	// en Conjugacion el modelo genérico pasa a ser ya el infinitivo concreto
	private String conjugacion = "";
	private String conjugacionSinTildes = "";
	private String sufijoEnclitico = "";
	private float confianza = 0.0F;  // Confianza de detección automática del verbo (si procede)
	
	/** Constructor por defecto de un sufijo de conjugación no reflexivo
	 * @param pInfinitivo	Infinitivo verbal, p ej "amar", "deber", "vivir" ...
	 * @param pTerminacion	Terminación verbal (la que se sustituye por el sufijo de conjugación), p ej "ar" "er" "ir" para esos 3 modelos
	 * @param pPersona	Persona (1-3) de la conjugación, 0 si no procede
	 * @param pNumeroPlural	Singular (false) o plural (true)
	 * @param pForma	Forma verbal ("presente", "futuro"..., "participio", "gerundio" ...)
	 * @param pModoSubjuntivo	Indicativo (false) o subjuntivo (true)
	 * @param pSufijo	Sufijo verbal en minúsculas (con tildes, si procede)  [si solo se usan las conjugaciones, puede ser ""]
	 * @param pConjugacion	Conjugación verbal en minúsculas (con tildes, si procede) [si solo se usan los sufijos, puede ser ""]
	 * @param pConfianza	Confianza (0.0F a 1.0F) de detección automática del verbo (si procede)
	 */
	public Conjugacion( String pInfinitivo, String pTerminacion, int pPersona, boolean pNumeroPlural, String pForma, boolean pModoSubjuntivo, String pSufijo, String pConjugacion, float pConfianza ) {
		super( pInfinitivo, pTerminacion, pPersona, pNumeroPlural, pForma, pModoSubjuntivo, pSufijo );
		conjugacion = pConjugacion; conjugacionSinTildes = UtilsVerbos.quitarTildes( pConjugacion );
		confianza = pConfianza;
	}

	public float getConfianza() {
		return confianza;
	}
	
	public String getInfinitivo() {   // = getModelo()
		return modelo;
	}
	
	public String getConjugacion() {
		return conjugacion;
	}
	
	public String getConjugacionSinTildes() {
		return conjugacionSinTildes;
	}

	public String getSufijoEnclitico( ) {
		return sufijoEnclitico;
	}
	
	public void setConjugacion( String conj ) {
		conjugacion = conj;
		conjugacionSinTildes = UtilsVerbos.quitarTildes( conj );
	}
	
	public void setSufijoEnclitico( String se ) {
		sufijoEnclitico = se;
	}
	
	public void setConfianza( float conf ) {
		confianza = conf;
	}
	
	/** Pone la forma verbal de la conjugación, eliminando el resto de los datos (dejándolos a blanco),
	 * preparando este elemento para la búsqueda.
	 * @param pConjugacion	forma verbal
	 */

	/* Activa internamente el flag de búsqueda para poder encontrar la primera ocurrencia en la estructura
	 */
	public void initConjugBusqueda( String pConjugacion ) {
		conjugacion = pConjugacion; 
		conjugacionSinTildes = UtilsVerbos.quitarTildes( pConjugacion );
		flagBusqueda = true;  // Estamos buscando este elemento en el Tree
		modelo = ""; persona = 0; numeroPlural = false; forma = ""; modoSubjuntivo = false; sufijo = ""; sufijoSinTildes = "";
	}

	
	public int compareTo( SufijoVerbal c2 ) {
		// Si todo igual se devuelve 0
		if (c2 instanceof Conjugacion) {
			return compareTo( (Conjugacion)c2 );
		} return -1;
	}
	public int compareTo( Conjugacion c2 ) {
		// Si todo igual se devuelve 0
		if (modelo.equals(c2.modelo) && terminacion==c2.terminacion && persona==c2.persona && numeroPlural==c2.numeroPlural && forma.equals(c2.forma) && 
				modoSubjuntivo==c2.modoSubjuntivo && sufijo.equals(c2.sufijo) && conjugacion.equals(c2.conjugacion))
			return 0;
		// Si alguno está en modo de búsqueda y coinciden las claves se devuelve 0
		if (conjugacionSinTildes.equals(c2.conjugacionSinTildes) && (flagBusqueda || c2.flagBusqueda)) return 0;
		// Si no, -1 o +1
		if (conjugacionSinTildes.compareTo( c2.conjugacionSinTildes )<0) return -1; else return +1;
	}
	public String showReduc() {
		return "<" + modelo + ">" + persona + (numeroPlural?"P":"S") + 
			forma.substring(0,4) + (modoSubjuntivo?"Sub":"") + (reflexivo?"reflexivo":"");
	}	
	public String show( boolean mostrarConfianza ) {
		return conjugacion + " (<" + modelo + "> (-" + terminacion + ") " + persona + (numeroPlural?"P ":"S ") + 
			forma + (modoSubjuntivo?" subju":" indic") + (reflexivo?" reflexivo":"") +
			(sufijo.equals("")?"":" -"+sufijo) + 
			( (sufijoEnclitico.equals(""))?"":(" -" + sufijoEnclitico) ) + 
			(mostrarConfianza?" [confi " + confianza + "]":"") +
			")";
	}	
	public String toString() {
		return conjugacionSinTildes;
	}
}

