package tema7b.juegoEjemplo;

import javax.swing.JPanel;

/** Clase del protagonista de juego, con su posici�n.<br>
 * @author andoni
 */
public class Prota extends ObjetoAnimado implements Chocable {
	protected boolean desintegrado = false;  // Si true, se ha roto por choques
	protected int choquesQueAguanta;   // Num. de choques que aguanta (0 = indestructible)
	protected float velocidadX = 0;
	private static final long serialVersionUID = 3287532921724034068L;  // para serializar
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

	/** Crea un prota nuevo que estar� est�tico en pantalla.<br>
	 * No se dibuja hasta que se llama al m�todo dibuja() o mueve()
	 * @param nom	Nombre del prota
	 * @param x	Coordenada X de inicio
	 * @param y	Coordenada Y de inicio
	 * @param numChoques	Choques que aguanta antes de romperse (0 para indestructible)
	 * @param panelJuego	Panel en el que se debe dibujar el bicho
	 * @param nombreImagenProta	Path fichero donde est� la imagen del prota
	 * @param prefijoImagenes	Path fichero prefijo donde est�n las im�genes del prota
	 */
	public Prota( String nom, int x, int y, int numChoques, JPanel panelJuego, String nombreImagenProta, String prefijoImagenes  ) {
		super( nom, x, y, panelJuego, nombreImagenProta, prefijoImagenes );
		this.choquesQueAguanta = numChoques;
	}

	/** El prota s�lo se dibuja si no est� desintegrado
	 */
	public void dibuja() {
		if (desintegrado) {
			if (estaDibujado) {
				// quita el prota del panel
				panelJuego.remove( this );  
				panelJuego.updateUI();
				estaDibujado = false;
			}
		} else {
			super.dibuja();
		}
	}

	/** El prota no se mueve solo, sino cuando lo mueven
	 * (atributo interno de velocidad)
	 */
	public void mueve() {
		if (estaDibujado) {
			float tiempoTranscurridoEnSegundos = 
				(System.currentTimeMillis() - horaUltimoDibujo) / 1000.0F;
			if (tiempoTranscurridoEnSegundos > 0.01F) {
				// Por debajo de una cent�sima no dibujamos (no merece la pena)
				float antCoordX = coordX;
				coordX += velocidadX * tiempoTranscurridoEnSegundos; 
				if (seSale()) {  // Si se sale de pantalla
					// se hace que rebote
					velocidadX = -velocidadX;
					// y se quita el movimiento que hab�amos hecho
					coordX = antCoordX; 
				}
				dibuja();  // Se dibuja en cualquier caso
			}
		}
	}	
	
	/** Activa velocidad del protagonista en horizontal
	 * @param velocidadHorizontal	Velocidad (negativo a la izqda, positivo a la dcha) en pixels/segundo
	 */
	public void activaMovimiento( float velocidadHorizontal ) {
		velocidadX = velocidadHorizontal;
		if (velocidadHorizontal == 0) {
			if (animacionActivada) animate( false, 0 );
		} else {
			if (!animacionActivada) animate( true, 250 );
		}
	}	
	
	/** Acelera/decelera la velocidad que tenga el protagonista si la ten�a manteniendo el lateral izqda. o dcha.
	 * @param velocidadHorizontal	Velocidad en pixels/segundo
	 */
	public void aceleraMovimiento( float velocidadHorizontal ) {
		if (velocidadX != 0) {
			velocidadHorizontal = Math.abs( velocidadHorizontal );
			if (velocidadX < 0) velocidadX = -velocidadHorizontal;
			else velocidadX = velocidadHorizontal;
		}
	}
	
	/** A�ade un nuevo choque al prota y lo desintegra si
	 * ha llegado a su l�mite de choques
	 */
	public void anyadeChoque() {
		choquesQueAguanta--;
		if (choquesQueAguanta <= 0) {
			System.out.println( "desintegrado el protagonista" );
			desintegrado = true;
			dibuja();
		}
	}
	
	
	
}

