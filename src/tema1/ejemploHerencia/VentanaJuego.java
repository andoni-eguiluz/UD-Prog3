package tema1.ejemploHerencia;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Ventana principal del juego de prueba
 * @author andoni
 */
public class VentanaJuego extends JFrame {
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
}

