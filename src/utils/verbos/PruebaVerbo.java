package utils.verbos;

import utils.verbos.expresiones.Expresion;
import utils.verbos.expresiones.Separador;
import utils.verbos.expresiones.TextoConceptual;
import utils.verbos.expresiones.VerboPrincipal;

public class PruebaVerbo {
	
	public static void main( String[] s ) {
		// Este código busca la PRIMERA forma verbal del texto y muestra su verbo principal
		String prueba = "Miguel, acostúmbrate a mirar a la ventana saliendo de casa";
		Expresion exp = UtilsVerbos.analizarTextoConFormasVerbales( prueba, true, true );
		for (int i = 0; i < exp.numTCs(); i++ ) {
			TextoConceptual tc = exp.getTC(i);
			if (tc instanceof VerboPrincipal) {
				VerboPrincipal vp = (VerboPrincipal) tc;
				System.out.println( "La forma verbal analizada es: '" + tc.getTextoUnidad() + "'  ---  " + tc.toString());
				System.out.println( "El verbo principal es: " + vp.getInfinitivoVerboPrincipal() );
				break; // No seguir buscando más formas verbales
			}
		}
		
		// Este código simplemente muestra el verbo principal de la PRIMERA forma verbal del texto
		System.out.println( "Que sí, que es el verbo " + UtilsVerbos.getVerboPrincipal( prueba ));
		
		exp = UtilsVerbos.analizaFrase( prueba, 0, true, true );
		for (int i = 0; i < exp.numTCs(); i++ ) {
			TextoConceptual tc = exp.getTC(i);
			if (!(tc instanceof Separador)) System.out.println( " + " + tc + " " + tc.getClass().getName() );
		}
		
	}
	
}
