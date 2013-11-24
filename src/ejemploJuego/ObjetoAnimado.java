package ejemploJuego;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/** Clase de objeto de pantalla con animación (varios gráficos)
 * @author andoni
 */
public abstract class ObjetoAnimado extends ObjetoDePantalla {
	protected String[] imagenesObjeto; // Nombres de los ficheros de imagen del objeto
	protected ImageIcon[] iconosObjeto;  // iconos del objeto
	protected int numImagenActual;  // Número de imagen en animación
	protected long horaInicioAnimacion; // Tiempo de inicio de la animación
	protected long milisegundosFotogramas;  // Tiempo de cambio de fotogramas
	protected boolean animacionActivada = false;  // Indicación de si la animación está activada
	private static final long serialVersionUID = 3287530621724032068L;  // para serializar
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

	/** Crea un objeto nuevo que estará estático en pantalla.<br>
	 * No se dibuja hasta que se llama al método dibuja() o mueve()
	 * @param nom	Nombre del objeto
	 * @param x	Coordenada X de inicio
	 * @param y	Coordenada Y de inicio
	 * @param numChoques	Choques que aguanta antes de romperse (0 para indestructible)
	 * @param panelJuego	Panel en el que se debe dibujar el bicho
	 * @param nombreImagObj	Path fichero donde está la PRIMERA imagen del objeto
	 * @param nombreImagenesObj	Path fichero con el inicio del nombre de LAS IMAGENES del objeto
	 */
	public ObjetoAnimado( String nom, int x, int y, JPanel panelJuego, 
			String nombreImagObj, String nombreImagenesObj ) {
		super( nom, x, y, panelJuego, nombreImagObj );
		// Cargamos los iconos múltiples
		if (nombreImagenesObj.endsWith("/") || nombreImagenesObj.endsWith("\\"))
			nombreImagenesObj = nombreImagenesObj.substring( 0, nombreImagenesObj.length()-1 );
		int posContraBarra = Math.max( nombreImagenesObj.lastIndexOf( '/' ), nombreImagenesObj.lastIndexOf( '\\' ) );
		posContraBarra = (posContraBarra<0) ? 0 : posContraBarra;
		final String path = nombreImagenesObj.substring( 0, posContraBarra );
		final String prefijo = nombreImagenesObj.substring( posContraBarra+1 );
	    URL referenciaURL = getClass().getResource( path );
	    try {
		    if (referenciaURL != null && referenciaURL.getProtocol().equals("file")) {
				FilenameFilter filter = new FilenameFilter() {
				    public boolean accept(File dir, String name) {
				        return name.startsWith( prefijo ) && 
				        	(name.endsWith( ".png") || name.endsWith( ".jpg") ||
				        	 name.endsWith( ".jpeg") || name.endsWith( ".gif"));
				    }
				};
				imagenesObjeto = new File(referenciaURL.toURI()).list( filter );
		    } else {
				imagenesObjeto = new String[0];
		    }
	    } catch (URISyntaxException e) {
			imagenesObjeto = new String[0];
	    }
		int i = 0;
		iconosObjeto = new ImageIcon[ imagenesObjeto.length ];
		for (String fic : imagenesObjeto ) {
			// System.out.println( path + "\\" + fic );
			URL imgURL = getClass().getResource( path + "\\" + fic );
	        iconosObjeto[i] = new ImageIcon(imgURL);
	        i++;
		}
        numImagenActual = 0;
	}

	/** El objeto se dibuja con posible animación
	 */
	public void dibuja() {
		if (animacionActivada) {  // Calcular fotograma que toca y ponerlo
			double numFotogramas = (System.currentTimeMillis()-horaInicioAnimacion) * 1.0 / milisegundosFotogramas; 
			int numIcono = ((int)numFotogramas) % iconosObjeto.length;
			setIcon( iconosObjeto[numIcono] );
		}
		super.dibuja();
	}

	/** Activa o desactiva la animación gráfica del objeto
	 * @param activarAnimacion	true para activarla, false para desactivarla
	 * @param milisegundosAnimacion	Número de milisegundos para cambio de fotograma
	 */
	public void animate( boolean activarAnimacion, long milisegundosAnimacion ) {
		if (activarAnimacion) {  // Activar animación
			animacionActivada = true;
			horaInicioAnimacion = System.currentTimeMillis();
			milisegundosFotogramas = milisegundosAnimacion;
		} else {
			animacionActivada = false;
		}
	}
	
}

