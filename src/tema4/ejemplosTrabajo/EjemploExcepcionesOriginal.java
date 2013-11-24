package tema4.ejemplosTrabajo;

public class EjemploExcepcionesOriginal {
	public static void main (String s[]) {
		try {
			Texto t = new Texto( "pedido.txt" );
			Texto t2 = t.traducir("inglés");
			t2.guardar("order.txt");
		} catch (ArithmeticException e2) {
			
		} catch (NullPointerException e3) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		Texto nuevoTexto = new Texto();
			for (String linea : lineas) {
				Frase f = new Frase( linea );
				try {
					Frase f2 = f.traducir( idioma );
					nuevoTexto.lineas.add( f2.getFrase() );
				} catch (ArithmeticException e2) {
					System.out.println( "Error numérico");
				} catch (Exception e) {
					e.printStackTrace();
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
	static int i = 0;
	public Traductor( String idioma1, String idioma2 ) {
		// Inicializa un traductor
	}
	public String traducir( String frase ) throws NullPointerException {
		// Código para conectarse a internet y traducir frase a frase2
		i++;
		if (i == 1)
			throw new NullPointerException();
		int a = 5 / 0;
		String frase2 = "*traducción de: " + frase;
		return frase2;
	}
}