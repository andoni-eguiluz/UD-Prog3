package tema1.ejemploHerencia;

import javax.swing.JPanel;

/** Clase del muro de juego, con su posici�n.<br>
 * @author andoni
 */
public class Muro extends ObjetoDePantalla {
	protected boolean desintegrado = false;  // Si true, se ha roto por choques
	protected int choquesQueAguanta;   // Num. de choques que aguanta (0 = indestructible)
	private static final long serialVersionUID = 3287530621724034068L;  // para serializar
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

	/** Crea un muro nuevo que estar� est�tico en pantalla.<br>
	 * No se dibuja hasta que se llama al m�todo dibuja() o mueve()
	 * @param nom	Nombre del bicho
	 * @param x	Coordenada X de inicio
	 * @param y	Coordenada Y de inicio
	 * @param numChoques	Choques que aguanta antes de romperse (0 para indestructible)
	 * @param panelJuego	Panel en el que se debe dibujar el bicho
	 * @param nombreImagenMuro	Path fichero donde est� la imagen del bicho
	 */
	public Muro( String nom, int x, int y, int numChoques, JPanel panelJuego, String nombreImagenMuro ) {
		super( nom, x, y, panelJuego, nombreImagenMuro );
		this.choquesQueAguanta = numChoques;
	}

	/** El muro s�lo se dibuja si no est� desintegrado
	 */
	public void dibuja() {
		if (desintegrado) {
			if (estaDibujado) {
				// quita el muro del panel
				panelJuego.remove( this );  
				panelJuego.updateUI();
				estaDibujado = false;
			}
		} else {
			super.dibuja();
		}
	}

	/** El muro no se mueve
	 */
	public void mueve() {
	}	
	
	/** A�ade un nuevo choque al muro y lo desintegra si�
	 * ha llegado a su l�mite de choques
	 */
	public void anyadeChoque() {
		choquesQueAguanta--;
		if (choquesQueAguanta == 0) {
			System.out.println( "desintegrado" );
			desintegrado = true;
			dibuja();
		}
	}	
	
}

