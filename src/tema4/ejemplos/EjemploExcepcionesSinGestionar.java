package tema4.ejemplos;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class EjemploExcepcionesSinGestionar {
	public static void main (String s[]) {
		UnTexto t = new UnTexto( "sentences.txt" );
		System.out.println( "Líneas originales:");
		t.visualizar();
		System.out.println();
		UnTexto t2 = t.traducir("en", "es");
		System.out.println( "Líneas traducidas:");
		t2.guardar("order.txt");
	}		
}

class UnTexto {
	java.util.Vector<String> lineas;
	public UnTexto() {
		lineas = new java.util.Vector<String>();
	}
	public UnTexto( String nomFic ) {
		this();
		// Ejemplo (aquí habría que cargar el fichero)
		lineas.add( "This is the first test sentence" );
		lineas.add( "Here, another simple english words" );
	}
	public UnTexto traducir( String idiomaOrigen, String idiomaDestino ) {
		UnTexto nuevoTexto = new UnTexto();
		for (String linea : lineas) {
			UnaFrase f = new UnaFrase( linea );
			UnaFrase f2 = f.traducir( idiomaOrigen, idiomaDestino );
			nuevoTexto.lineas.add( f2.getFrase() );
		}
		return nuevoTexto;
	}
	public void visualizar() {
		// Código guardar
		for (String s : lineas) {
			System.out.println( s );
		}
	}
	public void guardar( String nomFic ) {
		// Código guardar
		for (String s : lineas) {
			System.out.println( s );
		}
	}
}

class UnaFrase {
	String miFrase;
	public UnaFrase( String frase ) {
		miFrase = frase;
	}
	public String getFrase() {
		return miFrase;
	}
	public UnaFrase traducir( String idiomaOrigen, String idiomaDestino ) {
		// Traduce this al idioma indicado y devuelve
		// una nueva frase traducida
		UnTraductor t = new UnTraductor( idiomaOrigen, idiomaDestino );
		String fraseTraducida = t.traducir( miFrase );
		return new UnaFrase( fraseTraducida );
	}
}

class UnTraductor {
	
	public UnTraductor( String idioma1, String idioma2 ) {
		inicMicrosoftTranslator( idioma1, idioma2 );
	}
	
	public String traducir( String frase ) {
		// Código para conectarse a internet y traducir frase a frase2
		String frase2 = "";
		frase2 = traducirConMicrosoftTranslator( frase );
		// Fin código de traducción
		return frase2;
	}

	
	// Servicio de traducción online de Microsoft
	// Inicialización. Ver
	// https://code.google.com/p/microsoft-translator-java-api/	
		private Language idiomaOrigen;  // Atributos para traductor de Microsoft
		private Language idiomaDestino;
	private void inicMicrosoftTranslator( String idioma1, String idioma2 ) {
		// Inicializa el traductor de Microsoft 
		try {
			// Estas dos claves tienen que sacarse en la plataforma de
			// Microsoft de desarrollo para poder usar el servicio
			// (ver http://msdn.microsoft.com/en-us/library/hh454950.aspx)
		    Translate.setClientId("-PON-TU-ID-AQUI-");
		    Translate.setClientSecret("-PON-TU-CLAVE-AQUI-");
		} catch (Exception e) {
			throw new RuntimeException( "Error de conexión" );
		}
		// Almacenar los idiomas de traducción
	    idiomaOrigen = Language.fromString( idioma1 );  // p ej. "en" -> Language.ENGLISH;
	    idiomaDestino = Language.fromString( idioma2 );  // p ej. "es" -> Language.SPANISH;
	    if (idiomaOrigen == null) {  // Excepción si idioma 1 incorrecto
	    	throw new NullPointerException( "idioma incorrecto: " + idioma1 );
	    }
	    if (idiomaDestino == null) {  // Excepción si idioma 2 incorrecto
	    	throw new NullPointerException( "idioma incorrecto: " + idioma2 );
	    }
	}
	
	// Servicio de traducción online de Microsoft
	// Acceso a la API. Ver
	// https://code.google.com/p/microsoft-translator-java-api/	
	private String traducirConMicrosoftTranslator( String frase ) {
		try {
		    String translatedText = Translate.execute( frase, idiomaOrigen, idiomaDestino );
		    return translatedText;
		} catch (Exception e) {
			throw new RuntimeException( "Error de conexión" );
		}
	}

}