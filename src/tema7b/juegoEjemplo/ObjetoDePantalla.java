package tema7b.juegoEjemplo;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
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
	protected float coordX; // Posici�n X en pixels
	protected float coordY; // Posici�n Y en pixels
	protected JPanel panelJuego;  // panel del juego donde se dibuja el objeto
	protected boolean estaDibujado; // Info de si se ha dibujado el objeto en el panel
	protected long horaUltimoDibujo; // Hora de �ltimo dibujo (para actualizaci�n de movimiento)
	protected int anchuraObjeto;  // Anchura del objeto en pixels (depende de la imagen)
	protected int alturaObjeto;  // Altura del objeto en pixels (depende de la imagen)
	protected ImageIcon icono;  // icono del objeto
	protected Envolvente envolvente;
	private static final long serialVersionUID = -3338791672055205072L;  // para serializar

	/** Crea un objeto nuevo de pantalla.<br>
	 * No se dibuja hasta que se llama al m�todo dibuja() o mueve()
	 * @param nom	Nombre del objeto
	 * @param x	Coordenada X de inicio
	 * @param y	Coordenada Y de inicio
	 * @param panelJuego	Panel en el que se debe dibujar el objeto
	 * @param nombreImagenObjeto	Path fichero donde est� la imagen del objeto
	 * Si existe fichero de envolvente lo carga tambi�n (el mismo nombre de fichero con extensi�n .env)
	 * Si no existe, la envolvente es el rect�ngulo externo del gr�fico
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
        cargaEnvolvente();
		// Esto se har�a para cargar directamente de un fichero:
        // (no vale para el fichero dentro del jar)
        // ImageIcon icono = new ImageIcon( nombreImagenObjeto );
		setIcon( icono );
		anchuraObjeto = icono.getIconWidth();
		alturaObjeto = icono.getIconHeight();
		setBounds( new Rectangle( anchuraObjeto, alturaObjeto ));
			// Marca el tama�o del label
			// Ojo, con un layout nulo si no se pone este tama�o el label no se ve
		estaDibujado = false;
			// Al principio no est� dibujado
	}
		/** M�todo privado de carga de fichero de envolvente (.env)
		 */
		private void cargaEnvolvente() {
			int posPunto = nombreImagenObjeto.lastIndexOf( "." );
			String fichEnvolvente = nombreImagenObjeto.substring( 0, posPunto ) + ".env";
	        URL envURL = getClass().getResource(fichEnvolvente);
	        if (envURL != null) {
				try {
					ObjectInputStream oi = new ObjectInputStream( envURL.openStream() );
					Object o = oi.readObject();
					envolvente = (Envolvente) o;
				} catch (Exception e) {  // Cualquier error se crea nueva envolvente
					envolvente = new Envolvente();
				}
			} else {
				envolvente = new Envolvente();
			}
		}
		
	/** Devuelve la envolvente del objeto de pantalla
	 * @return	envolvente sobre el origen (0,0), no sobre el punto de pantalla actual
	 */
	public Envolvente getEnvolvente() {
		return envolvente;
	}

	/** Devuelve el nombre del objeto
	 * @return	Nombre del objeto
	 */
	public String getNombre() {
		return nombre;
	}
	
	/** Dibuja el objeto en el panel definido para ello (en el constructor) 
	 */
	public void dibuja() {
		if (!estaDibujado) {  // Si no est� dibujado se a�ade al panel
			panelJuego.add( this );
			this.estaDibujado = true;
		}
		setLocation( (int)coordX, (int)coordY );
			// Pone las coordenadas x-y
		horaUltimoDibujo = System.currentTimeMillis();
			// Y guarda la hora del �ltimo dibujo
	}

	/** Comprueba si el objeto choca al moverse con alg�n muro u otro objeto chocable
	 * @param objetos	Array de muros/objetos chocables con los que se puede chocar
	 * @return	null si no hay choque, el objeto con el que choca en caso contrario
	 */
	public Chocable chocaCon( Chocable[] objetos ) {
		if (estaDibujado) {
			for (Chocable m : objetos) {
				if (m.estaDibujado()) {
					if (envolvente.isEmpty() && m.getEnvolvente().isEmpty()) {
						// Caso 1 - no hay envolventes. Choque de rect�ngulos
						float x1 = coordX; float x2 = coordX+anchuraObjeto;
						float x3 = m.getCoordX(); float x4 = m.getCoordX()+m.getAnchuraObjeto();
						float y1 = coordY; float y2 = coordY+alturaObjeto;
						float y3 = m.getCoordY(); float y4 = m.getCoordY()+m.getAlturaObjeto();
						if ( ((x3>x1 && x3<x2) || (x4>x1 && x4<x2)) &&
								((y3>y1 && y3<y2) || (y4>y1 && y4<y2)) ) {
							return m;
						}
					} else if (envolvente.isEmpty()) {
						// Caso 2 - m tiene envolvente, this no
						m.getEnvolvente().setOffset( m.getCoordX(), m.getCoordY() );
						if (m.getEnvolvente().intersects( getX(), getY(), getAnchuraObjeto(), getAlturaObjeto() )) {
							return m;
						}
					} else if (m.getEnvolvente().isEmpty()) {
						// Caso 3 - this tiene envolvente, m no
						envolvente.setOffset( getX(), getY() );
						if (envolvente.intersects( m.getCoordX(), m.getCoordY(),
								m.getAnchuraObjeto(), m.getAlturaObjeto() )) {
							return m;
						}
					} else {
						// Caso 4 - los dos tienen envolvente
						envolvente.setOffset( getX(), getY() );
						m.getEnvolvente().setOffset( m.getCoordX(), m.getCoordY() );
						if (envolvente.intersects( m.getEnvolvente() )) {
							return m;
						}
					}
				}
			}
		}
		return null;
	}
	
	/** Mueve el objeto de acuerdo a su velocidad y lo redibuja
	 * (si estaba ya dibujado. Si no, no lo dibuja) <br>
	 */
	abstract public void mueve();
	
	/** Indica si el objeto se ha salido del panel de juego
	 * @return	true si el objeto se ha salido, false en caso contrario
	 */
	public boolean seSale() {
		return (coordX < 0 || coordY < 0 || 
		        coordX > panelJuego.getWidth()-anchuraObjeto ||
		        coordY > panelJuego.getHeight()-alturaObjeto );
	}
	
	/** Indica si el objeto est� o no dibujado actualmente
	 * @return	true si est� en pantalla
	 */
	public boolean estaDibujado() {
		return estaDibujado;
	}

	/** Devuelve la coordenada X del objeto en pantalla
	 * @return	Coordenada horizontal
	 */
	public float getCoordX() {
		return coordX;
	}

	/** Devuelve la coordenada Y del objeto en pantalla
	 * @return	Coordenada vertical
	 */
	public float getCoordY() {
		return coordY;
	}

	/** Devuelve la anchura del rect�ngulo gr�fico del objeto
	 * @return	Anchura
	 */
	public int getAnchuraObjeto() {
		return anchuraObjeto;
	}
	
	/** Devuelve la altura del rect�ngulo gr�fico del objeto
	 * @return	Altura
	 */
	public int getAlturaObjeto() {
		return alturaObjeto;
	}

}

