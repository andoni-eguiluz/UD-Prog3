package ejemploJuego;

import java.util.Random;

import javax.swing.JPanel;

/** Clase del protagonista de juego, con su posici�n.<br>
 * @author andoni
 */
public class Bala extends ObjetoAnimado {
	protected float velocidadY = 5;
	private static final long serialVersionUID = 3287532921724032068L;  // para serializar
	/* Atributos heredados:
		protected String nombre; // Nombre del bicho
		protected String nombreImagenBicho; // Nombre de la imagen del bicho
		protected float coordX; // Posici�n X en pixels
		protected float coordY; // Posici�n Y en pixels
		protected JPanel panelJuego;  // panel del juego donde se dibuja el bicho
		protected boolean estaDibujado; // Info de si se ha dibujado el bicho en el panel
		protected long horaUltimoDibujo; // Hora de �ltimo dibujo (para actualizaci�n de movimiento)
		protected int anchuraBicho;  // Anchura del bicho en pixels (depende de la imagen)
		protected int alturaBicho;  // Altura del bicho en pixels (depende de la imagen)
		protected ImageIcon icono;  // icono del bicho
		*/

	/** Crea una bala nueva.<br>
	 * No se dibuja hasta que se llama al m�todo dibuja() o mueve()
	 * @param nom	Nombre de la bala
	 * @param x	Coordenada X de inicio
	 * @param y	Coordenada Y de inicio
	 * @param panelJuego	Panel en el que se debe dibujar el bicho
	 * @param nombreImagenBala	Path fichero donde est� la imagen del prota
	 * @param prefijoImagenes	Path fichero prefijo donde est�n las im�genes del prota
	 */
	public Bala( String nom, int x, int y, JPanel panelJuego, String nombreImagenProta, String prefijoImagenes  ) {
		super( nom, x, y, panelJuego, nombreImagenProta, prefijoImagenes );
	}

	/** La bala se mueve en vertical
	 * (atributo interno de velocidad)
	 */
	public void mueve() {
		if (estaDibujado) {
			float tiempoTranscurridoEnSegundos = 
				(System.currentTimeMillis() - horaUltimoDibujo) / 1000.0F;
			if (tiempoTranscurridoEnSegundos > 0.01F) {
				// Por debajo de una cent�sima no dibujamos (no merece la pena)
				float antCoordY = coordY;
				coordY += velocidadY * tiempoTranscurridoEnSegundos; 
				if (seSale()) {  // Si se sale de pantalla
					// se hace que rebote
					velocidadY = -velocidadY;
					// y se quita el movimiento que hab�amos hecho
					coordY = antCoordY; 
				}
				dibuja();  // Se dibuja en cualquier caso
			}
		}
	}	
	
	/** Activa velocidad del protagonista en horizontal
	 * @param velocidadVertical	Velocidad (negativo arriba, positivo abajo) en pixels/segundo
	 */
	public void activaMovimiento( float velocidadHorizontal ) {
		velocidadY = velocidadHorizontal;
		if (velocidadY == 0) {
			if (animacionActivada) animate( false, 0 );
		} else {
			if (!animacionActivada) animate( true, 250 );
		}
		horaInicioAnimacion = horaInicioAnimacion - r.nextInt(1000);
	}	
		private static Random r = new Random();
	
	/** Acelera/decelera la velocidad que tenga el protagonista si la ten�a manteniendo el lateral izqda. o dcha.
	 * @param velocidadVertical	Velocidad en pixels/segundo
	 */
	public void aceleraMovimiento( float velocidadVertical ) {
		if (velocidadY != 0) {
			velocidadVertical = Math.abs( velocidadVertical );
			if (velocidadY < 0) velocidadY = -velocidadVertical;
			else velocidadY = velocidadVertical;
		}
	}
	
}

