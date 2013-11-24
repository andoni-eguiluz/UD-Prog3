package tests;

import java.util.StringTokenizer;

public class Prueba17 {

	public static void main(String s[]) {
		String miTexto = "esto es una frase separada. por espacios";
		StringTokenizer st = new StringTokenizer( miTexto, " \t." );
		System.out.println( st.countTokens() );
		while (st.hasMoreElements()) {
			System.out.println( st.nextToken() );
		}
	}
}
