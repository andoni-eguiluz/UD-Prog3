package ejemploJuego;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BichoConOtroTamanyo extends Bicho {
	
	BufferedImage originalImage;
	private static final long serialVersionUID = -4980419934523336406L;
	
	/** Construye un bicho con tamaño proporcional al bitmap de referencia
	 * que se mueve como un bicho normal
	 * @param nom	Nombre del bicho
	 * @param x	Coordenada X de inicio
	 * @param y	Coordenada Y de inicio
	 * @param velX	Velocidad horizontal en pixels/segundo
	 * @param velY	Velocidad vertical en pixels/segundo
	 * @param panelJuego	Panel en el que se debe dibujar el bicho
	 * @param nombreImagenBicho	Path fichero donde está la imagen del bicho
	 * @param anchura	Anchura modificada del bicho
	 * @param altura	Altura modificada del bicho
	 */
	public BichoConOtroTamanyo(String nom, int x, int y, float velX,
			float velY, JPanel panelJuego, String nombreImagenBicho,
			int anchura, int altura ) {
		super(nom, x, y, velX, velY, panelJuego, nombreImagenBicho);
		envolvente.transformaEnvolvente( 1.0*anchura/anchuraObjeto, 1.0*altura/alturaObjeto);  // Hace la envolvente proporcional
		alturaObjeto = altura;
		anchuraObjeto = anchura;
		setBounds( new Rectangle( anchuraObjeto, alturaObjeto ));
        URL imgURL = getClass().getResource(nombreImagenBicho);
        try {
        	originalImage = ImageIO.read( imgURL );
        } catch (IOException e) {
        }
	}

	// Dibuja este componente de una forma no habitual (al ser proporcional)
	protected void paintComponent(Graphics g) {
		//super.paintComponent(g);  en vez de esto...
		Graphics2D g2 = (Graphics2D) g;  // El Graphics realmente es Graphics2D
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);	
        g2.drawImage(originalImage, 0, 0, anchuraObjeto, alturaObjeto, null);
	}
	
}

