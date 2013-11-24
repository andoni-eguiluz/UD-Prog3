/**
 * 
 */
package tema9;

import utils.verbos.UtilsVerbos;  // CLASE CHEQUEADA

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author andoni
 *
 */
public class TestVerbos extends TestCase {

	private String formaVerbal;
	private String infinitivo;
	
	/** Chequea si la frase indicada tiene el verbo infinitivo indicado
	 * @param fraseConUnaFormaVerbal
	 * @param infinitivo
	 */
	public TestVerbos(String fraseConUnaFormaVerbal, String infinitivo) {
		super( "testDevolverInfinitivosVerbos" );  // La forma la ponemos tb como nombre de test
		this.formaVerbal = fraseConUnaFormaVerbal;
		this.infinitivo = infinitivo;
	}

	public void testDevolverInfinitivosVerbos() {
		ArrayList<String> verbos = UtilsVerbos.devolverInfinitivosVerbos( formaVerbal );
		assertTrue( verbos != null );
		assertTrue( verbos.size() == 1 );
		assertEquals( verbos.get(0), infinitivo );
	}

	public static Test suite() {
		 TestSuite suite = new TestSuite();
		 // Grupo de test de frases con un s�lo verbo, 
		 // chequeando el infinitivo del verbo principal
		 suite.addTest( new TestVerbos( "Lo habr�a sabido", "saber" ));
		 suite.addTest( new TestVerbos( "Luis sigui� all�", "seguir" ));
		   // Problema aqu�: ambig�edad entre ir y ser en la forma "fue"
		 suite.addTest( new TestVerbos( "Luis fue all�", "ir" ));
		 suite.addTest( new TestVerbos( "Luis fue un gran se�or", "ser" ));
		 return suite;	
	}
	
}
