package tema1;

/** Clase de prueba sobre herencia
 * @author andoni
 * 
 */
abstract public class DispositivoAlmacenamiento {
	protected long memLibreEnBytes;
	
	public long getMemLibreEnBytes() {
		return memLibreEnBytes;
	}

	public void setMemLibreEnBytes(long memLibreEnBytes) {
		this.memLibreEnBytes = memLibreEnBytes;
	}

	/** Añade un fichero al dispositivo de almacenamiento
	 * @param tamFichero	Tamaño del fichero en bytes
	 */
	abstract public void guardaFichero( long tamFichero );
	
	public static void main (String s[]) {
		DispositivoAlmacenamiento[] a = 
			new DispositivoAlmacenamiento[10];
		a[0] = new MP3(1024);
		a[1] = new DiscoDuroExterno(  1024L*1024L*1024L );
		System.out.println( a[0].toString() );
		System.out.println( a[1].toString() );
		a[0].guardaFichero( 1000 );
		if (a[0] instanceof MP3) {
			MP3 temp = (MP3) a[0];
			temp.reproduceTodas();
		}
		a[1].guardaFichero( 1000 );
		System.out.println( a[0] );
		System.out.println( a[1] );
	}
}
