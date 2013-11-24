package tema1.ejemploHerencia;

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
	
	public BichoConOtroTamanyo(String nom, int x, int y, float velX,
			float velY, JPanel panelJuego, String nombreImagenBicho,
			int anchura, int altura ) {
		super(nom, x, y, velX, velY, panelJuego, nombreImagenBicho);
		alturaObjeto = altura;
		anchuraObjeto = anchura;
		setBounds( new Rectangle( anchuraObjeto, alturaObjeto ));
        URL imgURL = getClass().getResource(nombreImagenBicho);
        try {
        	originalImage = ImageIO.read( imgURL );
        } catch (IOException e) {
        }
	}

	protected void paintComponent(Graphics g) {
		//super.paintComponent(g);  en vez de esto...
		Graphics2D g2 = (Graphics2D) g;  // El Graphics realmente es Graphics2D
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);	
        g2.drawImage(originalImage, 0, 0, anchuraObjeto, alturaObjeto, null);
	}
	
}

