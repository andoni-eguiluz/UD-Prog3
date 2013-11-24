package tema1.ejemploHerencia;

import javax.swing.JPanel;

/** Clase del bicho de juego, con su posici�n y velocidad.<br>
 * @author andoni
 */
public class Bicho extends ObjetoDePantalla {
	protected float velX;   // Velocidad X en pixels/segundo
	protected float velY;   // Velocidad Y en pixels/segundo
	private static final long serialVersionUID = 3287530621724014069L;  // para serializar
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

	/** Crea un bicho nuevo que se mover� de acuerdo a su velocidad.<br>
	 * No se dibuja hasta que se llama al m�todo dibuja() o mueve()
	 * @param nom	Nombre del bicho
	 * @param x	Coordenada X de inicio
	 * @param y	Coordenada Y de inicio
	 * @param velX	Velocidad horizontal en pixels/segundo
	 * @param velY	Velocidad vertical en pixels/segundo
	 * @param panelJuego	Panel en el que se debe dibujar el bicho
	 * @param nombreImagenBicho	Path fichero donde est� la imagen del bicho
	 */
	public Bicho( String nom, int x, int y, float velX, float velY, JPanel panelJuego, String nombreImagenBicho ) {
		super( nom, x, y, panelJuego, nombreImagenBicho );
		this.velX = velX;
		this.velY = velY;
	}

	/** Mueve el bicho de acuerdo a su velocidad y lo redibuja
	 * (si estaba ya dibujado. Si no, no lo dibuja) <br>
	 * Se utiliza el tiempo del sistema para asegurar la velocidad
	 * (es decir, los pixels que avanza no depende del n�mero de 
	 * veces que se llame al m�todo mover sino del tiempo transcurrido.
	 * El bicho se mover� independientemente del n�mero de llamadas a este m�todo.
	 * Si se llama pocas veces, s� puede ocurrir que en pantalla se note salto
	 * (muchos p�xels entre dibujo y dibujo)
	 */
	public void mueve() {
		if (estaDibujado) {
			float tiempoTranscurridoEnSegundos = 
				(System.currentTimeMillis() - horaUltimoDibujo) / 1000.0F;
			if (tiempoTranscurridoEnSegundos > 0.01F) {
				// Por debajo de una cent�sima no dibujamos (no merece la pena)
				float antCoordX = coordX;
				float antCoordY = coordY;
				coordX += velX * tiempoTranscurridoEnSegundos; 
				coordY += velY * tiempoTranscurridoEnSegundos;
				if (seSale()) {  // Si se sale de pantalla 
					rebota(); // se hace que rebote
					// y se quita el movimiento que hab�amos hecho
					coordX = antCoordX; 
					coordY = antCoordY;
				}
				dibuja();  // Se dibuja en cualquier caso
			}
		}
	}
	
	/** Comprueba si el bicho choca al moverse con alg�n muro
	 * @param muros	Array de muros con los que se puede chocar
	 * @return	null si no hay choque, el muro con el que choca en caso contrario
	 */
	public Muro chocaConMuro( Muro[] muros ) {
		if (estaDibujado) {
			float tiempoTranscurridoEnSegundos = 
				(System.currentTimeMillis() - horaUltimoDibujo) / 1000.0F;
			float nuevaCoordX = coordX + velX * tiempoTranscurridoEnSegundos;
			float nuevaCoordY = coordY + velY * tiempoTranscurridoEnSegundos;
			for (Muro m : muros) {
				if (m.estaDibujado) {
					float x1 = nuevaCoordX; float x2 = nuevaCoordX+anchuraObjeto;
					float x3 = m.coordX; float x4 = m.coordX+m.anchuraObjeto;
					float y1 = nuevaCoordY; float y2 = nuevaCoordY+alturaObjeto;
					float y3 = m.coordY; float y4 = m.coordY+m.alturaObjeto;
					if ( ((x3>x1 && x3<x2) || (x4>x1 && x4<x2)) &&
							((y3>y1 && y3<y2) || (y4>y1 && y4<y2)) ) {
						return m;
					}
				}
			}
		}
		return null;
	}
	
	/** Indica si el bicho se ha salido del panel de juego
	 * @return	true si el bicho se ha salido, false en caso contrario
	 */
	public boolean seSale() {
		return (coordX < 0 || coordY < 0 || 
		        coordX > panelJuego.getWidth()-anchuraObjeto ||
		        coordY > panelJuego.getHeight()-alturaObjeto );
	}
	
	/** Invierte la velocidad del bicho si se ha salido de pantalla
	 * (dependiendo del lugar por el que se sale, invierte la velocidad correspondiente:
	 * horizontal (X) si se sale por izquierda o derecha
	 * vertical (Y) si se sale por arriba o abajo)
	 */
	public void rebota() {
		if (coordX < 0) // rebota por la izquierda
			velX = Math.abs(velX);
		else if (coordX > panelJuego.getWidth()-anchuraObjeto) // rebota por la derecha
			velX = -Math.abs(velX);
		if (coordY < 0) // rebota por arriba
			velY = Math.abs(velY);
		else if (coordY > panelJuego.getHeight()-alturaObjeto) // rebota por abajo
			velY = -Math.abs(velY);
	}
	
	/** Invierte la velocidad del bicho si ha chocado
	 */
	public void invierteMarcha() {
		velX = -velX;
		velY = -velY;
	}
	
}

