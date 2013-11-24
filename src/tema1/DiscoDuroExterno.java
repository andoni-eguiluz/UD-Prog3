package tema1;

public class DiscoDuroExterno extends DispositivoAlmacenamiento {
	private String tamanyo;
	// ...
	
	public DiscoDuroExterno( long capacidadEnBytes ) {
		memLibreEnBytes = capacidadEnBytes;
	}
	
	/** A�ade un fichero al MP3
	 * @param tamFichero	Tama�o del fichero en bytes
	 */
	public void guardaFichero( long tamFichero ) {
		memLibreEnBytes = memLibreEnBytes - tamFichero;
		memLibreEnBytes = memLibreEnBytes - 1024;
	}
	
	public String toString() {
		return "Disco duro con espacio libre = " + memLibreEnBytes;
	}
}
