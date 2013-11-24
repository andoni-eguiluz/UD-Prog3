package ejemploJuego;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.image.*;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tests.DrawingPanel;

/** Ventana principal del juego de prueba
 * @author andoni
 */
public class VentanaJuego extends JFrame implements WindowListener {
	private static Dimension TAM_MINIMO_VENTANA = new Dimension( 700, 500 );  // ventana m�nimo
	private JPanel espacioJuego;  // Panel principal del juego
	private JLabel mensajeJuego;  // Barra de mensaje inferior
	private static final long serialVersionUID = 1294475242982151023L;
	
	/** Construye una ventana con su panel de juego
	 */
	VentanaJuego() {
		mensajeJuego = new JLabel( " " );
		mensajeJuego.setHorizontalAlignment( JLabel.CENTER );
		mensajeJuego.setOpaque( true );
		mensajeJuego.setBackground( Color.black );
		mensajeJuego.setForeground( Color.white );
		espacioJuego = new JPanel();
		espacioJuego.setLayout( null );  // Pone layout nulo para dibujar por coordenadas en �l
		setMinimumSize( TAM_MINIMO_VENTANA );
		setPreferredSize( TAM_MINIMO_VENTANA );
		getContentPane().add( espacioJuego, "Center" );
		getContentPane().add( mensajeJuego, "South" );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		addWindowListener( this );
			// EXIT porque esta ventana es la �nica forma de parar al main
			// Normalmente ser�a DISPOSE_ON_CLOSE
		// Escuchador de rat�n para mostrar qu� bicho hay:
		espacioJuego.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Component c = espacioJuego.findComponentAt( e.getX(), e.getY() );
				if (c instanceof Bicho) {
					mensajeJuego.setText( "Click en: " + ((Bicho) c).getNombre() );
				} else {
					mensajeJuego.setText( " " );
				}
			}
		});
	}
	
	
	
	/** Devuelve el panel de juego de la ventana
	 * @return	Panel principal donde se visualiza el juego
	 */
	public JPanel getPanelJuego() {
		return espacioJuego;
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// if (getPanelJuego() != null) getPanelJuego().requestFocus();
	}
	@Override
	public void windowClosing(WindowEvent e) {
	}
	@Override
	public void windowClosed(WindowEvent e) {
	}
	@Override
	public void windowIconified(WindowEvent e) {
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
	}
	@Override
	public void windowActivated(WindowEvent e) {
		if (getPanelJuego() != null) getPanelJuego().requestFocus();
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
