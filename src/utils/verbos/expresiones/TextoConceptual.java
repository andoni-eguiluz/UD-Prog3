package utils.verbos.expresiones;

public interface TextoConceptual {
	public String getTextoOriginalCompleto();  // Devuelve la referencia a todo el texto original
	public String getTextoUnidad();  // Devuelve el texto original de la unidad conceptual gestionada
	public String getTextoMinusc();  // Devuelve esa unidad conceptual en minúsculas
	public String getTextoSinTildes();  // Devuelve la unidad conceptual en minúsculas sin tildes
	public int getOffsetIni();  // Devuelve el offset de inicio de la unidad conceptual
	public int getOffsetFin();  // Devuelve el offset de fin de la unidad conceptual
}
