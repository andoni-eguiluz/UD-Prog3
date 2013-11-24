package tema4.ejemplosResueltos;

public class EjemploExcepcionesDefinitivo {
	public static void main (String s[]) {
		try {
			Texto t = new Texto( "pedido.txt" );
			Texto t2 = t.traducir("inglés");
			t2.guardar("order.txt");
		} catch (RuntimeException e) {
			System.out.println( "Mi propia gestión de error");
			e.printStackTrace();
		}
	}		
}

class NoEncuentroAGoogleException extends Exception {
	public NoEncuentroAGoogleException( String mens ) {
		// TODO Auto-generated constructor stub
		super( mens );
	}
}

class Texto {
	java.util.Vector<String> lineas;
	public Texto() {
		lineas = new java.util.Vector<String>();
	}
	public Texto( String nomFic ) {
		this();
		// Ejemplo (aquí habría que cargar el fichero)
		lineas.add( "Frase de prueba 1" );
		lineas.add( "Frase de prueba 2" );
	}
	public Texto traducir( String idioma ) {
		// int a = 5 / 0;
		Texto nuevoTexto = new Texto();
		for (String linea : lineas) {
			Frase f = new Frase( linea );
			try {
				Frase f2 = f.traducir( idioma );
				nuevoTexto.lineas.add( f2.getFrase() );
			} catch (Exception miExcepcion) {
				System.out.println( "Error en traducción de tipo "
						+ miExcepcion.getMessage()
						+ " en la frase: " + f.miFrase );
			}
		}
		return nuevoTexto;
	}
	public void guardar( String nomFic ) {
		// Código guardar
		System.out.println( lineas );
	}
}

class Frase {
	String miFrase;
	public Frase( String frase ) {
		miFrase = frase;
	}
	public String getFrase() {
		return miFrase;
	}
	public Frase traducir( String idioma ) throws Exception {
		// Traduce this al idioma indicado y devuelve
		// una nueva frase traducida
		Traductor t = new Traductor( "castellano", idioma );
		String fraseTraducida = t.traducir( miFrase );
		return new Frase( fraseTraducida );
	}
}

class Traductor {
	public Traductor( String idioma1, String idioma2 ) {
		// Inicializa un traductor
	}
	/** Traduce la frase del idioma origen al destino y la devuelve
	 * @param frase	Frase a traducir (en el idioma origen)
	 * @return	Frase traducida
	 * @throws Exception	Error si no se puede conectar al traductor
	 */
	public String traducir( String frase ) throws Exception {
		// int a = 5 / 0;
		// Código para conectarse a internet y traducir frase a frase2
		if ( false /*no ha habido conexión*/ ) {
 			throw new NoEncuentroAGoogleException( "error de conexión http");
		}
		String frase2 = "*traducción de: " + frase;
		return frase2;
	}
}