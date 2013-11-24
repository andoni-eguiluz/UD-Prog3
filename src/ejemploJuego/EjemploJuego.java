package ejemploJuego;

import javax.swing.JComponent;

// Herencia:
//   ObjetoDePantalla
//      |
//      +-- Bicho
//      |     +-- BichoConOtroTamanyo
//      +-- Muro  --> implementa Chocable
//      +-- ObjetoAnimado
//            +-- Prota  --> implementa Chocable

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

	public static void main(String s[]) {
		ventanaJuego = new VentanaJuego();
		ventanaJuego.pack();
		ventanaJuego.setLocationRelativeTo(null);
		ventanaJuego.setVisible( true );
		Bicho b1 = new Bicho( "Steve", 50, 50, 45, 17.5F, ventanaJuego.getPanelJuego(), "resources/bicho.png" );
		Bicho b2 = new Bicho( "Martin", 100, 0, -10, 42, ventanaJuego.getPanelJuego(), "resources/bicho.png" );
		Bicho b3 = new Bicho( "Mary", 90, 250, 5, 60, ventanaJuego.getPanelJuego(), "resources/bicho.png" );
		int pixelsDistancia = 10;
		Bala[] balasAzules = new Bala [30];
		for (int i=0; i<balasAzules.length; i++) {
			balasAzules[i] = new Bala( "b" + i, 500, 10 + i*pixelsDistancia, ventanaJuego.getPanelJuego(), "resources/Burbuja01.png", "resources/Burbuja" );
			balasAzules[i].activaMovimiento( 70 );
		}
		Bala[] balasRojas = new Bala [30];
		for (int i=0; i<balasRojas.length; i++) {
			balasRojas[i] = new Bala( "b" + i, 550, 10 + i*pixelsDistancia, ventanaJuego.getPanelJuego(), "resources/BurbRoja01.png", "resources/BurbRoja" );
			balasRojas[i].activaMovimiento( 50 );
		}
		// Bicho b3 = new BichoConOtroTamanyo( "Mary", 490, 250, 5, 60, ventanaJuego.getPanelJuego(), "resources/bicho.png", 200, 100 );
		Muro m1 = new Muro( "M1", 275, 120, 5, ventanaJuego.getPanelJuego(), "resources/muro.png" );
		Prota p1 = new Prota( "Andoni", 350, 280, 10, ventanaJuego.getPanelJuego(), "resources/Monigote01.png", "resources/Monigote" );
		ObjetoDePantalla[] objetos = new ObjetoDePantalla[] { b1, b2, b3, m1, p1 };
		Chocable[] objetosQueChocan = new Chocable[] { m1, p1 };
		for (ObjetoDePantalla o : objetos) o.dibuja();
		for (ObjetoDePantalla o : balasAzules) o.dibuja();
		for (ObjetoDePantalla o : balasRojas) o.dibuja();
		GestorTeclado gestorTeclado = new GestorTeclado( new JComponent[]{ ventanaJuego.getPanelJuego() }, p1 );
		boolean seguir = true;
		while (seguir) {
			// Bucle infinito (esto en un juego normal lo haría un thread de juego)
			// y además habría alguna manera de pararlo (aquí se cierra con la ventana)
			for (ObjetoDePantalla o : objetos) {
				if (o instanceof Bicho) {
					Chocable choque = ((Bicho)o).chocaCon( objetosQueChocan );
					if (choque != null) {
						choque.anyadeChoque();
						((Bicho)o).invierteMarcha();
					}
				}
				o.mueve();
			}
			for (ObjetoDePantalla o : balasAzules) o.mueve();
			for (ObjetoDePantalla o : balasRojas) o.mueve();
			// Parada de unas centésimas: para no matar la CPU innecesariamente...
			try { Thread.sleep( 30 ); } catch (InterruptedException e) { }
				// Si se aumenta la parada se llegaría a ver la imagen
				// poco contínua (por ejemplo poner 100 en el sleep)
			// Contamos los movimientos que se hacen para ver las veces
			// que se dibujan los bichos en pantalla:
			contarMovimientos++;
			// System.out.println( contarMovimientos );
		}
		gestorTeclado.close();
	}

}
