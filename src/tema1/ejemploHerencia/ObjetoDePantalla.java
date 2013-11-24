package tema1.ejemploHerencia;

import java.awt.Rectangle;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Clase abstracta de objeto visible en pantalla
 * @author andoni
 */
public abstract class ObjetoDePantalla extends JLabel {
	protected String nombre; // Nombre del objeto
	protected String nombreImagenObjeto; // Nombre del fichero de imagen del objeto
	protected float coordX; // Posición X en pixels
	protected float coordY; // Posición Y en pixels
	protected JPanel panelJuego;  // panel del juego donde se dibuja el objeto
	protected boolean estaDibujado; // Info de si se ha dibujado el objeto en el panel
	protected long horaUltimoDibujo; // Hora de último dibujo (para actualización de movimiento)
	protected int anchuraObjeto;  // Anchura del bicho en pixels (depende de la imagen)
	protected int alturaObjeto;  // Altura del bicho en pixels (depende de la imagen)
	protected ImageIcon icono;  // icono del bicho
	private static final long serialVersionUID = -3338791672055205072L;  // para serializar

	/** Crea un objeto nuevo de pantalla.<br>
	 * No se dibuja hasta que se llama al método dibuja() o mueve()
	 * @param nom	Nombre del bicho
	 * @param x	Coordenada X de inicio
	 * @param y	Coordenada Y de inicio
	 * @param panelJuego	Panel en el que se debe dibujar el bicho
	 * @param nombreImagenObjeto	Path fichero donde está la imagen del bicho
	 */
	public ObjetoDePantalla( String nombre, int x, int y, JPanel panelJuego, String nombreImagenObjeto ) {
		this.panelJuego = panelJuego;
		this.nombre = nombre;
		this.coordX = x;
		coordY = y;
		// Cargamos el icono (como un recurso - vale tb del .jar)
		this.nombreImagenObjeto = nombreImagenObjeto;
        URL imgURL = getClass().getResource(nombreImagenObjeto);
        icono = new ImageIcon(imgURL);
		// Esto se haría para cargar directamente de un fichero:
        // (no vale para el fichero dentro del jar)
        // ImageIcon icono = new ImageIcon( nombreImagenBicho );
		setIcon( icono );
		anchuraObjeto = icono.getIconWidth();
		alturaObjeto = icono.getIconHeight();
		setBounds( new Rectangle( anchuraObjeto, alturaObjeto ));
			// Marca el tamaño del label
			// Ojo, con un layout nulo si no se pone este tamaño el label no se ve
		estaDibujado = false;
			// Al principio no está dibujado
	}

	/** Devuelve el nombre del objeto
	 * @return	Nombre del objeto
	 */
	public String getNombre() {
		return nombre;
	}
	
	public void dibuja() {
		if (!estaDibujado) {  // Si no está dibujado se añade al panel
			panelJuego.add( this );
			this.estaDibujado = true;
		}
		setLocation( (int)coordX, (int)coordY );
			// Pone las coordenadas x-y
		horaUltimoDibujo = System.currentTimeMillis();
			// Y guarda la hora del último dibujo
	}
	
	/** Mueve el bicho de acuerdo a su velocidad y lo redibuja
	 * (si estaba ya dibujado. Si no, no lo dibuja) <br>
	 */
	abstract public void mueve();
	
	/** Indica si el bicho se ha salido del panel de juego
	 * @return	true si el bicho se ha salido, false en caso contrario
	 */
	public boolean seSale() {
		return (coordX < 0 || coordY < 0 || 
		        coordX > panelJuego.getWidth()-anchuraObjeto ||
		        coordY > panelJuego.getHeight()-alturaObjeto );
	}
	
}

