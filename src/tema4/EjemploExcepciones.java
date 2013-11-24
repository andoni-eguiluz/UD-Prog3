package tema4;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EjemploExcepciones {
	public static void main (String s[]) {
		Texto t = new Texto( "pedido.txt" );
		Texto t2 = t.traducir("inglés");
		t2.guardar("order.txt");
	}		
}

class Texto {
	java.util.Vector<String> lineas;
	public Texto( String nomFic ) {
		lineas = new java.util.Vector<String>();
	}
	public Texto traducir( String idioma ) {
		Texto nuevoTexto = new Texto( "" );
		for (String linea : lineas) {
			Frase f = new Frase( linea );
			Frase f2 = f.traducir( idioma );
			nuevoTexto.lineas.add( f2.getFrase() );
		}
		return nuevoTexto;
	}
	public void guardar( String nomFic ) {
		// Código guardar
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
	public Frase traducir( String idioma ) {
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
	public String traducir( String frase ) {
		String frase2 = "";
		// Código para conectarse a internet y traducir frase a frase2
		return frase2;
	}
}