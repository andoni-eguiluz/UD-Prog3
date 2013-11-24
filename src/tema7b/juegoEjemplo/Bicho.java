package tema7b.juegoEjemplo;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/** Clase del bicho de juego, con su posici�n y velocidad.<br>
 * @author andoni
 */
public class Bicho extends ObjetoDePantalla {
	protected float velX;   // Velocidad X en pixels/segundo
	protected float velY;   // Velocidad Y en pixels/segundo
	private static final long serialVersionUID = 3287530621724014069L;  // para serializar

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
	 * @param objetos	Array de muros con los que se puede chocar
	 * @return	null si no hay choque, el muro con el que choca en caso contrario
	 */
	public Chocable chocaCon( Chocable[] objetos ) {
		if (estaDibujado) {
			float tiempoTranscurridoEnSegundos = 
				(System.currentTimeMillis() - horaUltimoDibujo) / 1000.0F;
			coordX = coordX + velX * tiempoTranscurridoEnSegundos;
			coordY = coordY + velY * tiempoTranscurridoEnSegundos;
			Chocable choque = super.chocaCon( objetos );
			if (choque != null) {
				coordX = coordX - 2 * velX * tiempoTranscurridoEnSegundos;
				coordY = coordY - 2 * velY * tiempoTranscurridoEnSegundos;
				return choque;
			}
		}
		return null;
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

