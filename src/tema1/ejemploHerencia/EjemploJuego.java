package tema1.ejemploHerencia;

/**  Clase de ejemplo de un juego con dos bichos botando por pantalla
 * @author andoni
 *
 */
public class EjemploJuego {
	private static VentanaJuego ventanaJuego;
	/**  Método de prueba de ventana de juego
	 * Saca dos bichos en pantalla y los mueve:
	 * - Bicho 1 - en coordenada (100,50) con velocidad 85,37.5 pixels/seg
	 * - Bicho 2 - en coordenada (200,0) con vel. -20,82
	 * @param s
	 */
	static int contarMovimientos = 0;

	// main básico para entender la herencia y el polimorfismo
	public static void main (String s[]) {
		ventanaJuego = new VentanaJuego();
		ventanaJuego.pack();
		ventanaJuego.setLocationRelativeTo(null);
		ventanaJuego.setVisible( true );
		Bicho b1 = new Bicho( "Steve", 50, 50, 85, 37.5F, ventanaJuego.getPanelJuego(), "bicho.png" );
		Bicho b2 = new Bicho( "Martin", 100, 0, -20, 82, ventanaJuego.getPanelJuego(), "bicho.png" );
		Bicho b3 = new BichoConOtroTamanyo( "Mary", 450, 250, 5, 120, ventanaJuego.getPanelJuego(), "bicho.png", 200, 100 );
		Muro m1 = new Muro( "M1", 275, 150, 5, ventanaJuego.getPanelJuego(), "muro.png" );
		ObjetoDePantalla[] objetos = new ObjetoDePantalla[] { b1, b2, b3, m1 };
		Muro[] muros = new Muro[] { m1 };
		for (ObjetoDePantalla o : objetos) o.dibuja();
		// for (int i=0; i<objetos.length; i++) {
		// ObjetoDePantalla o = objetos[i];
		// 	o.dibuja();
		// }
		while (true) { 
			for (ObjetoDePantalla o : objetos) {
				if (o instanceof Bicho) {
					Bicho bTemp = (Bicho)o;
					Muro choque = bTemp.chocaConMuro( muros );
					if (choque != null) {
						choque.anyadeChoque();
						bTemp.invierteMarcha();
					}
				}
				o.mueve();
			}
		}
	}

	// Este es el main mejor
	public static void mainMejor (String s[]) {
		ventanaJuego = new VentanaJuego();
		ventanaJuego.pack();
		ventanaJuego.setLocationRelativeTo(null);
		ventanaJuego.setVisible( true );
		Bicho b1 = new Bicho( "Steve", 50, 50, 85, 37.5F, ventanaJuego.getPanelJuego(), "bicho.png" );
		Bicho b2 = new Bicho( "Martin", 100, 0, -20, 82, ventanaJuego.getPanelJuego(), "bicho.png" );
		Bicho b3 = new BichoConOtroTamanyo( "Mary", 450, 250, 5, 120, ventanaJuego.getPanelJuego(), "bicho.png", 200, 100 );
		Muro m1 = new Muro( "M1", 275, 150, 5, ventanaJuego.getPanelJuego(), "muro.png" );
		ObjetoDePantalla[] objetos = new ObjetoDePantalla[] { b1, b2, b3, m1 };
		Muro[] muros = new Muro[] { m1 };
		for (ObjetoDePantalla o : objetos) o.dibuja();
		while (true) { 
			// Bucle infinito (esto en un juego normal lo haría un thread de juego)
			// y además habría alguna manera de pararlo (aquí se cierra con la ventana)
			for (ObjetoDePantalla o : objetos) {
				if (o instanceof Bicho) {
					Muro choque = ((Bicho)o).chocaConMuro( muros );
					if (choque != null) {
						choque.anyadeChoque();
						((Bicho)o).invierteMarcha();
					}
				}
				o.mueve();
			}
			// Parada de unas centésimas: para no matar la CPU innecesariamente...
			try { Thread.sleep( 30 ); } catch (InterruptedException e) { }
				// Si se aumenta la parada se llegaría a ver la imagen
				// poco contínua (por ejemplo poner 100 en el sleep)
			// Contamos los movimientos que se hacen para ver las veces
			// que se dibujan los bichos en pantalla:
			contarMovimientos++;
			System.out.println( contarMovimientos );
		}
	}
}
