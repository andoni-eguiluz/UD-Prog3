package ejemploJuego;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import utils.BuscaFicherosSinVentana;

/** Ventana principal del juego de prueba
 * @author andoni
 */
public class VentanaEdicionEnvolvente extends JFrame {
	private static Dimension TAM_MINIMO_VENTANA = new Dimension( 700, 500 );  // ventana mínimo
	private JPanelEdicion espacioEdicion;  // Panel principal del juego
	private JLabel mensajeJuego;  // Barra de mensaje inferior
	private JList lista;
	private JComboBox combo;
	private JButton botonCerrar;
	private JButton botonGuardar;
	private JButton botonReiniciar;
	private DefaultListModel modeloLista;
	private static final long serialVersionUID = 1294475242982151023L;
	private Envolvente miEnvolvente;
	private Bicho miBicho = null;
	private ArrayList<String> ficheros = new ArrayList<String>();
	private ArrayList<String> nombresComplFicheros = new ArrayList<String>();
	
	/** Construye una ventana con su panel de juego
	 */
	VentanaEdicionEnvolvente( ArrayList<String> imagenes, ArrayList<String> nomsFicImagenes ) {
		ficheros = imagenes;
		nombresComplFicheros = nomsFicImagenes;
		combo = new JComboBox( imagenes.toArray(new String[0]));
		botonCerrar = new JButton( "Cerrar env." );
		botonGuardar = new JButton( "Guardar env." );
		botonReiniciar = new JButton( "Reiniciar env." );
		modeloLista = new DefaultListModel();
		lista = new JList();
		lista.setMinimumSize( new Dimension(100, 100));
		lista.setPreferredSize( new Dimension(100, 100));
		lista.setModel(modeloLista);
		mensajeJuego = new JLabel( " " );
		mensajeJuego.setHorizontalAlignment( JLabel.CENTER );
		mensajeJuego.setOpaque( true );
		mensajeJuego.setBackground( Color.black );
		mensajeJuego.setForeground( Color.white );
		espacioEdicion = new JPanelEdicion();
		espacioEdicion.setLayout( null );  // Pone layout nulo para dibujar por coordenadas en él
		espacioEdicion.addEnvolvente( miEnvolvente );
		setMinimumSize( TAM_MINIMO_VENTANA );
		setPreferredSize( TAM_MINIMO_VENTANA );
		getContentPane().setLayout( new BorderLayout() );
		getContentPane().add( espacioEdicion, "Center" );
		getContentPane().add( mensajeJuego, "South" );
		getContentPane().add( lista, "West" );
		JPanel sup = new JPanel();
		sup.add( combo );
		sup.add( botonCerrar );
		sup.add( botonGuardar );
		sup.add( botonReiniciar );
		getContentPane().add( sup, "North" );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		cargaDeCombo();
			// EXIT porque esta ventana es la única forma de parar al main
			// Normalmente sería DISPOSE_ON_CLOSE
		// Escuchador de ratón para mostrar qué bicho hay:
		espacioEdicion.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				miEnvolvente.lineTo( new Point2D.Double( e.getX(), e.getY() ));
				modeloLista.addElement( "(" + e.getX() + "," + e.getY() + ")" );
				espacioEdicion.refreshEnvolventes();
			}
		});
		botonCerrar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (miEnvolvente.getCurrentPoint() != null) {
					miEnvolvente.closePath();
					espacioEdicion.refreshEnvolventes();
				}
			}
		} );
		botonGuardar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				salvarEnvolvente();
			}
		} );
		botonReiniciar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				miEnvolvente = new Envolvente();
				getPanelEdicion().removeEnvolventes();
				getPanelEdicion().addEnvolvente( miEnvolvente );
				modeloLista.removeAllElements();
			}
		} );
		combo.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cargaDeCombo();
			}
		});
	}

		private String fichEnvolvente = "";
	private void cargaDeCombo() {
		int pos = combo.getSelectedIndex();
		if (pos>=0 && pos<ficheros.size()) {
			// 1. Carga de gráfico de referencia
			String fichero = ficheros.get(pos);
			String fichCompleto = nombresComplFicheros.get(pos);
			getPanelEdicion().removeAll();
			miBicho = new Bicho( "Edición", 50, 50, 85, 37.5F, getPanelEdicion(), fichero );
			getPanelEdicion().add( miBicho );
			getPanelEdicion().repaint();
			// System.out.println( fichCompleto );
			// 2. Carga de fichero envolvente (.env)
			int posPunto = fichCompleto.lastIndexOf( "." );
			fichEnvolvente = fichCompleto.substring( 0, posPunto ) + ".env";
			System.out.println( fichEnvolvente );
			cargarEnvolvente();
		}
		
	}
	
		private void cargarEnvolvente() {
//			File f = new File( fichEnvolvente );
//			if (f.exists()) {
//				try {
//					ObjectInputStream oi = new ObjectInputStream( new FileInputStream(f));
//					Object o = oi.readObject();
//					miEnvolvente = (Envolvente) o;
//				} catch (Exception e) {  // Cualquier error se crea nueva envolvente
//					miEnvolvente = new Envolvente();
//				}
//			} else {
//				miEnvolvente = new Envolvente();
//			}
			if (miBicho!=null) {
				espacioEdicion.removeEnvolventes();
				espacioEdicion.addEnvolvente( miBicho.getEnvolvente() );
				miEnvolvente = miBicho.getEnvolvente();
			}
			modeloLista.removeAllElements();
		}
		private void salvarEnvolvente() {
			File f = new File( fichEnvolvente );
			try {
				ObjectOutputStream oo = new ObjectOutputStream( new FileOutputStream(f));
				oo.writeObject( miEnvolvente );
			} catch (Exception e) {
			}
		}
	
	
	
	/** Devuelve el panel de juego de la ventana
	 * @return	Panel principal donde se visualiza el juego
	 */
	public JPanelEdicion getPanelEdicion() {
		return espacioEdicion;
	}
	
			private static ArrayList<String> fics = new ArrayList<String>();
			private static ArrayList<String> noms = new ArrayList<String>();
		private static BuscaFicherosSinVentana.Operador miOp = 
			new BuscaFicherosSinVentana.Operador() {
			@Override
			public void ficheroEncontrado( File f ) {
				// System.out.println( f.getAbsolutePath() );
				if (f.getAbsolutePath().contains( carpetas )) {
					int pos = f.getAbsolutePath().indexOf( carpetas ) + carpetas.length();
					String path = f.getAbsolutePath().substring( pos, f.getAbsolutePath().length() );
					if (!fics.contains(path)) {
						// System.out.println( path );
						fics.add( path );
						noms.add( f.getAbsolutePath() );
					}
				}
			}
			public void finBusqueda( boolean cancel ) {
//				if (cancel)
//					System.out.println( "CANCELADO" );
//				else
//					System.out.println( "SE ACABO" );
			}
		};
		private static String carpetas;
		
	public static void main( String[] s ) {
		String paquete = VentanaEdicionEnvolvente.class.getPackage().getName();
		carpetas = paquete.replaceAll( "\\.", "\\\\" ) + "\\";
        // System.out.println( paquete );
        // System.out.println( carpetas );
		BuscaFicherosSinVentana b = new BuscaFicherosSinVentana( "./", 
				Pattern.compile(".*\\.(jpg|jpeg|png)|.*\\.gif"), miOp, 
				false, 5 );
		b.iniciaBusqueda();
		b.esperaAQueAcabeBusqueda();
		System.out.println( fics );
		VentanaEdicionEnvolvente v = new VentanaEdicionEnvolvente( fics, noms );
		v.setVisible( true );
	}
	
	
}

