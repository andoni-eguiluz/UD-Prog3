/**
 * 
 */
package utils.verbos;

import java.util.ArrayList;
import java.util.Arrays;

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
		 // Grupo de test de frases con un sólo verbo, 
		 // chequeando el infinitivo del verbo principal
		 suite.addTest( new TestVerbos( "Lo habría sabido", "saber" ));
		 suite.addTest( new TestVerbos( "Luis siguió allí", "seguir" ));
		 suite.addTest( new TestVerbos( "Luis fue allí", "ser" ));
		 return suite;	
	}
	
}
