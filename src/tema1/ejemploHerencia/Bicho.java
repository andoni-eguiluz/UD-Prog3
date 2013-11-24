package tema1.ejemploHerencia;

import javax.swing.JPanel;

/** Clase del bicho de juego, con su posición y velocidad.<br>
 * @author andoni
 */
public class Bicho extends ObjetoDePantalla {
	protected float velX;   // Velocidad X en pixels/segundo
	protected float velY;   // Velocidad Y en pixels/segundo
	private static final long serialVersionUID = 3287530621724014069L;  // para serializar
	/* Atributos heredados:
		protected String nombre; // Nombre del bicho
		protected String nombreImagenBicho; // Nombre de la imagen del bicho
		protected float coordX; // Posición X en pixels
		protected float coordY; // Posición Y en pixels
		protected JPanel panelJuego;  // panel del juego donde se dibuja el bicho
		protected boolean estaDibujado; // Info de si se ha dibujado el bicho en el panel
		protected long horaUltimoDibujo; // Hora de último dibujo (para actualización de movimiento)
		protected int anchuraBicho;  // Anchura del bicho en pixels (depende de la imagen)
		protected int alturaBicho;  // Altura del bicho en pixels (depende de la imagen)
		protected ImageIcon icono;  // icono del bicho
		*/

	/** Crea un bicho nuevo que se moverá de acuerdo a su velocidad.<br>
	 * No se dibuja hasta que se llama al método dibuja() o mueve()
	 * @param nom	Nombre del bicho
	 * @param x	Coordenada X de inicio
	 * @param y	Coordenada Y de inicio
	 * @param velX	Velocidad horizontal en pixels/segundo
	 * @param velY	Velocidad vertical en pixels/segundo
	 * @param panelJuego	Panel en el que se debe dibujar el bicho
	 * @param nombreImagenBicho	Path fichero donde está la imagen del bicho
	 */
	public Bicho( String nom, int x, int y, float velX, float velY, JPanel panelJuego, String nombreImagenBicho ) {
		super( nom, x, y, panelJuego, nombreImagenBicho );
		this.velX = velX;
		this.velY = velY;
	}

	/** Mueve el bicho de acuerdo a su velocidad y lo redibuja
	 * (si estaba ya dibujado. Si no, no lo dibuja) <br>
	 * Se utiliza el tiempo del sistema para asegurar la velocidad
	 * (es decir, los pixels que avanza no depende del número de 
	 * veces que se llame al método mover sino del tiempo transcurrido.
	 * El bicho se moverá independientemente del número de llamadas a este método.
	 * Si se llama pocas veces, sí puede ocurrir que en pantalla se note salto
	 * (muchos píxels entre dibujo y dibujo)
	 */
	public void mueve() {
		if (estaDibujado) {
			float tiempoTranscurridoEnSegundos = 
				(System.currentTimeMillis() - horaUltimoDibujo) / 1000.0F;
			if (tiempoTranscurridoEnSegundos > 0.01F) {
				// Por debajo de una centésima no dibujamos (no merece la pena)
				float antCoordX = coordX;
				float antCoordY = coordY;
				coordX += velX * tiempoTranscurridoEnSegundos; 
				coordY += velY * tiempoTranscurridoEnSegundos;
				if (seSale()) {  // Si se sale de pantalla 
					rebota(); // se hace que rebote
					// y se quita el movimiento que habíamos hecho
					coordX = antCoordX; 
					coordY = antCoordY;
				}
				dibuja();  // Se dibuja en cualquier caso
			}
		}
	}
	
	/** Comprueba si el bicho choca al moverse con algún muro
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

