package utils.verbos;

/** Clase que representa un sufijo verbal
 * Se ordena por valor completo, pero su igualdad se mira con el sufijo (sin tildes)
 * @author andoni
 *
 */
public class SufijoVerbal implements Comparable<SufijoVerbal> {
	protected String modelo;
	protected String terminacion;
	protected int persona;
	protected boolean numeroPlural;  // false = singular
	protected String forma;
	protected boolean modoSubjuntivo;  // false = indicativo
	protected String sufijo;
	protected String sufijoSinTildes;
	protected boolean reflexivo = false;  // false = no reflexivo
	protected boolean flagBusqueda = false;
	
	/** Constructor por defecto de un sufijo de conjugación no reflexivo
	 * @param pModelo	Modelo verbal, p ej "amar" "deber" "vivir" para los 3 modelos de verbos regulares
	 * @param pTerminacion	Terminación verbal (la que se sustituye por el sufijo de conjugación), p ej "ar" "er" "ir" para esos 3 modelos
	 * @param pPersona	Persona (1-3) de la conjugación, 0 si no procede
	 * @param pNumeroPlural	Singular (false) o plural (true)
	 * @param pForma	Forma verbal ("presente", "futuro" ...)
	 * @param pmodoSubjuntivo	Indicativo (false) o subjuntivo (true)
	 * @param pSufijo	Sufijo verbal en minúsculas (con tildes, si procede)  [si solo se usan las conjugaciones, puede ser ""]
	 * @param pConjugacion	Conjugación verbal en minúsculas (con tildes, si procede) [si solo se usan los sufijos, puede ser ""]
	 */
	public SufijoVerbal( String pModelo, String pTerminacion, int pPersona, boolean pNumeroPlural, String pForma, boolean pModoSubjuntivo, String pSufijo ) {
		modelo = pModelo; terminacion = pTerminacion; persona = pPersona; numeroPlural = pNumeroPlural; forma = pForma; 
		modoSubjuntivo = pModoSubjuntivo; sufijo = pSufijo; sufijoSinTildes = UtilsVerbos.quitarTildes( pSufijo );
	}
	
	public void setReflexivo( boolean pReflexivo ) {
		reflexivo = pReflexivo;
	}
	
	public boolean getReflexivo() {
		return reflexivo;
	}
	
	public String getModelo() {
		return modelo;
	}
	
	public String getTerminacion() {
		return terminacion;
	}
	
	public int getPersona() {
		return persona;
	}
	
	public boolean getNumeroPlural() {
		return numeroPlural;
	}
	
	public String getFormaVerbal() {
		return forma;
	}

		public boolean esParticipio() { return ("participio".equals(forma) || "participio pasivo".equals(forma)); }
		public boolean esGerundio() { return "gerundio".equals(forma); }
		public boolean esInfinitivo() { return "infinitivo".equals(forma); }
	
	public boolean getModoVerbal() {
		return modoSubjuntivo;
	}
	
	public String getSufijo() {
		return sufijo;
	}	
	
	public String getSufijoSinTildes() {
		return sufijoSinTildes;
	}	
	
	/* Activa internamente el flag de búsqueda para poder encontrar la primera ocurrencia en la estructura
	 */
	public void initSufijoBusqueda( String pSufijo ) {
		sufijo = pSufijo; 
		sufijoSinTildes = UtilsVerbos.quitarTildes( pSufijo );
		flagBusqueda = true;  // Estamos buscando este elemento en el Tree
		modelo = ""; persona = 0; numeroPlural = false; forma = ""; modoSubjuntivo = false;
	}
	
	public int compareTo( SufijoVerbal s2 ) {
		// Si todo igual se devuelve 0
		if (modelo.equals(s2.modelo) && terminacion.equals(s2.terminacion) && persona==s2.persona && numeroPlural==s2.numeroPlural && 
				forma.equals(s2.forma) && modoSubjuntivo==s2.modoSubjuntivo && sufijo.equals(s2.sufijo))
			return 0;
		// Si alguno está en modo de búsqueda y coinciden las claves se devuelve 0
		if (sufijoSinTildes.equals(s2.sufijoSinTildes) && (flagBusqueda || s2.flagBusqueda)) return 0;
		// Si no, -1 o +1
		if (sufijoSinTildes.compareTo( s2.sufijoSinTildes )<0) return -1; else return +1;
	}
	
	public String show() {
		String result = "-" + sufijo;
		return result + " (<" + modelo + "> (-" + terminacion + ") " + persona + (numeroPlural?"P ":"S ") + forma + 
			(modoSubjuntivo?" subju":" indic") + (" " + (reflexivo?"reflexivo":"")) + ")";
	}
	
	public String toString() {
		return sufijoSinTildes;
	}
}

