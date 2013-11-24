package tema1;
import java.util.Vector;

/**
 * @author andoni
 *
 */
/**
 * @author andoni
 *
 */
public class MP3 extends DispositivoAlmacenamiento
                 implements ConPantalla
{
	protected String color;
	protected Vector titulosCanciones;

	/**  Construye un nuevo objeto MP3 dada su memoria disponible
	 * @param pMemLibre	Memoria disponible del MP3 en Mbytes
	 */
	public MP3( double pMemLibre ) {
		// (1) - Crear el objeto
		// this -> referencia a ese objeto
		// (2) - Programador: ASIGNAR VALORES A LOS ATRIBUTOS
		memLibreEnBytes = (long) (pMemLibre * 1024 * 1024);
		color = "";
		titulosCanciones = new Vector();
		// (3) - Devuelve la dirección (referencia) a ese nuevo objeto
	}
	
	/** Crea y devuelve un nuevo MP3 partiendo de los bytes disponibles
	 * @param bytesLibres	Memoria del MP3 en bytes
	 * @return	MP3 nuevo
	 */
	public static MP3 creaNuevoMP3EnBytes( long bytesLibres ) {
		return new MP3( bytesLibres*1024*1024 );
	}
	
	/**
	 * @return
	 */
	public static MP3 creaNuevoMP31Gb( ) {
		return new MP3( 1024 );
	}
	
	
	
	public boolean equals( Object obj ) {
		if (!(obj instanceof MP3)) return false;
		MP3 segundoMP3 = ((MP3)obj);
		return memLibreEnBytes == segundoMP3.memLibreEnBytes;
	}
	
	
	
	/** Añade un fichero al MP3
	 * @param tamFichero	Tamaño del fichero en bytes
	 */
	public void guardaFichero( long tamFichero ) {
		memLibreEnBytes = memLibreEnBytes - tamFichero;
	}
	
	public void guardaCancion( long tamCancion, String nomCancion ) {
		memLibreEnBytes -= tamCancion;
		titulosCanciones.add( nomCancion );
	}
	
		public String toString() {
		return "" + memLibreEnBytes;
	}
	public double getMemLibre() {
		return memLibreEnBytes;
	}
	/** Actualiza la memoria libre del MP3 a los Mb indicados
	 * @param memLibreEnBytes	Memoria libre en Mb
	 */
	public void setMemLibre(double memLibreEnMb) {
		if (memLibreEnMb < 0) System.out.println( "ERROR!" );
		this.memLibreEnBytes = (long) (memLibreEnMb*1024*1024);
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public void reproduceTodas() {
		// ...
	}
	public static void main (String s[]) {
		double memoria1 = 1000;
		double memoria2 = memoria1;
		if (memoria1 == memoria2) System.out.println( "Memorias iguales" );
		memoria2 = memoria2 - 2;
//		System.out.println( "Mem 1 = " + memoria1 + " / Mem 2 = " + memoria2 );
		MP3 player0 = new MP3( 1024 );
		MP3 player1 = creaNuevoMP31Gb();
		player1.guardaCancion( 50, "Yesterday" );
		MP3 player2 = creaNuevoMP31Gb(); // new MP3( 1000 );  // player1.clone();
//		if (player1 == player2) System.out.println( "Players iguales" );
		if (player1.equals(player2)) System.out.println( "Players iguales" );
		player2.guardaFichero( 2 );
		System.out.println( "Mem player1 = " + player1.getMemLibre() );
		System.out.println( "Mem player2 = " + player2.memLibreEnBytes );
		
	}

	public void mostrarEnPantalla( String s ) {
		System.out.println( s );
	}

	@Override
	public String leerPantalla() {
		// TODO Auto-generated method stub
		return "prueba";
	}

	@Override
	public void escribirEnPantalla(String s) {
		// TODO Auto-generated method stub
		// bla bla bla
	}


}





// Importancia de la ocultación (get,set)

//
// Proyecto 1
//
class ProyEjemplo1 {
	public static void main( String s[] ) {
		// ...
		MP3 miMP3 = new MP3( 1024 );
		// MP3 miMP3 = MP3.creaNuevoMP31Gb();
		miMP3.memLibreEnBytes = 1020;    // opc. 1
		// miMP3.setMemLibre( 1020 ); // opc. 2
		// ...
	}
}


//
//  Proyecto 2
//
class ProyEjemplo2 {
	public static void main( String s[] ) {
		// ...
		MP3 tuMP3 = MP3.creaNuevoMP31Gb();
		// tuMP3.memLibre = 1000;    // opc.1
		tuMP3.setMemLibre( 1000 ); //opc. 2
		// ...
	}
	
}












