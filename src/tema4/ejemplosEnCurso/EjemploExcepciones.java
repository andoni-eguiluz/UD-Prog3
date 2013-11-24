package tema4.ejemplosEnCurso;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.management.RuntimeErrorException;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class EjemploExcepciones {
	public static void main (String s[]) {
		Texto t = new Texto( "sentences.txt" );
		System.out.println( "Líneas originales:");
		t.visualizar();
		System.out.println();
		Texto t2 = null;
		try {
				t2 = t.traducir("en", "sp");
				System.out.println( "Líneas traducidas:");
				t2.guardar("order.txt");
		} catch (Exception e) {
			System.out.println( "Error de conexión: traductor no accesible");
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
		lineas.add( "This is the first test sentence" );
		lineas.add( "Here, another simple english words" );
	}
	public Texto traducir( String idiomaOrigen, String idiomaDestino ) 
	throws Exception {
		Texto nuevoTexto = new Texto();
		for (String linea : lineas) {
			Frase f = new Frase( linea );
			try {
				Frase f2 = f.traducir( idiomaOrigen, idiomaDestino );
				nuevoTexto.lineas.add( f2.getFrase() );
			} catch (NullPointerException e) {
				System.out.println( "Frase incorrecta bla bla bla");
			}
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

class Frase {
	String miFrase;
	public Frase( String frase ) {
		miFrase = frase;
	}
	public String getFrase() {
		return miFrase;
	}
	public Frase traducir( String idiomaOrigen, String idiomaDestino ) 
	throws Exception {
		// Traduce this al idioma indicado y devuelve
		// una nueva frase traducida
		Traductor t = new Traductor( idiomaOrigen, idiomaDestino );
		String fraseTraducida = t.traducir( miFrase );
		return new Frase( fraseTraducida );
	}
}

class Traductor {
	private String idiomaOrigen;
	private String idiomaDestino;
	public Traductor( String idioma1, String idioma2 ) {
		// Inicializa un traductor 
		// ...
		idiomaOrigen = idioma1;
		idiomaDestino = idioma2;
	}
	static int i = 0;
	public String traducir ( String frase ) throws Exception  {
		// Código para conectarse a internet y traducir frase a frase2
		String frase2 = null;
		if (frase2 == null)
			throw new Exception( "Info de prueba" );
		frase2 = Traductor.traducirConMicrosoftTranslator( frase, idiomaOrigen, idiomaDestino );
		// Fin código de traducción
		return frase2;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// Traductor de Microsoft
	// Acceso a la API. Ver
	// https://code.google.com/p/microsoft-translator-java-api/	
	public static String traducirConMicrosoftTranslator( String frase,
			String idiomaDesde, String idiomaHasta ) {
		try {
		    Translate.setClientId("andonieguiluz");
		    Translate.setClientSecret("kNENzcvGd1Hz6wFtVKTzTa/hvg+xkmRbB+u//S+rXRY=");
		    
//			System.out.println( Language.getLanguageCodesForTranslation().indexOf("en") );

//			System.out.println( idiomaDesde + " - " + idiomaHasta );
//			Language lDesde = Language. valueOf( idiomaDesde ); 
//			Language lHasta = Language.valueOf( idiomaHasta );
//			System.out.println( lDesde + " - " + lHasta );
//			System.out.println( Language.getLanguageCodesForTranslation() );
//		    String translatedText = Translate.execute(frase, lDesde, lHasta );
		    String translatedText = Translate.execute(frase, Language.ENGLISH, Language.SPANISH );
		    return translatedText;
		} catch (Exception e) {
			throw new RuntimeException( "Error de conexión" );
		}
	}

}