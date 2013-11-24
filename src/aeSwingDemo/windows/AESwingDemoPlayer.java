package aeSwingDemo.windows;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import aeSwingDemo.AESwingDemo;
import aeSwingDemo.Contenido;
import aeSwingDemo.ElementoDemo;
import aeSwingDemo.ReferenciaACodigo;

import java.awt.*;
import java.awt.event.*;
import java.io.EOFException;
import java.io.File;

public class AESwingDemoPlayer extends JFrame {
	JPanel panelBoton;
	JLabel etiqueta;
	JList lista = new JList( new String[] { "A", "B", "C", "Otros" } );
	JFrame ventanaEnCurso = null;
	// private JTextArea taCodFuente = new JTextArea();
	private JTextPane taCodFuente = new JTextPane();
	private File actFuenteFile = null;
	public AESwingDemoPlayer()
	{
		taCodFuente.setSelectedTextColor( Color.green );
		taCodFuente.setSelectionColor( Color.red );
		this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		etiqueta = new JLabel();
		panelBoton = new JPanel();
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(etiqueta,"West");
		this.getContentPane().add(lista,"North");
		this.getContentPane().add(panelBoton,"South");
		getContentPane().add( new JScrollPane(taCodFuente), "Center" );

		lista.setCellRenderer( new MiListCellRenderer() );

		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if (ventanaEnCurso != null) ventanaEnCurso.dispose();
			}
		});
		
		this.pack();
		this.setTitle("Ejemplo Sencillo");
		this.setVisible(true);
	}
	
	/** Carga un fichero en el fuente. Devuelve true si la carga ha sido correcta y false en caso contrario
	 * @param f
	 * @return	true si la carga es correcta
	 */
	public boolean setFuenteToFile( File f ) {
		try {
			java.io.FileInputStream fis = new java.io.FileInputStream( f );
			java.io.BufferedReader brF = new java.io.BufferedReader( new java.io.InputStreamReader(fis) );
			// HTMLDocument doc = new HTMLDocument();
			// taCodFuente.setDocument(doc);
			taCodFuente.setText( "" );
			try {
				String lin = brF.readLine();
				while (lin != null) {
					taCodFuente.getDocument().insertString( taCodFuente.getDocument().getLength(), lin+"\n", null);
					// taCodFuente.append( lin+"\n" );
					lin = brF.readLine();
				}
			} catch (EOFException e) {
				// Se acabó el fichero
				fis.close();
			}
		} catch (Exception e) {
			return false;
		}
		actFuenteFile = f;
		return true;
	}
	static SimpleAttributeSet fondoVerde  = new SimpleAttributeSet();
	public void setFuenteSel( ReferenciaACodigo r ) {
		if (r.getFileClase() != actFuenteFile) setFuenteToFile( r.getFileClase() );
		// taCodFuente.setSelectionStart( r.getOffsetInicio() );
		// taCodFuente.setSelectionEnd( r.getOffsetFin() );
	       fondoVerde.addAttribute( StyleConstants.Background, Color.green );
	       Document doc = taCodFuente.getDocument();
	       try {
	           String text = doc.getText( r.getOffsetInicio(), r.getOffsetFin()-r.getOffsetInicio());
	           doc.remove( r.getOffsetInicio(), r.getOffsetFin()-r.getOffsetInicio() );
	           doc.insertString( r.getOffsetInicio(), text, fondoVerde );
	       }
	       catch( Exception exc )
	       {
	           exc.printStackTrace();
	       }		
		
	}
	public JTextComponent getTextAreaFuente() {
		return taCodFuente;
	}
	
	public JFrame getVentanaEnCurso() {
		return ventanaEnCurso;
	}
	public void setVentanaEnCurso( JFrame f ) {
		if (ventanaEnCurso != null) ventanaEnCurso.dispose();
		ventanaEnCurso = f;
	}
	public static void main (String[] s) throws Exception {
		AESwingDemo aesd = new AESwingDemo();  // Path src
		ElementoDemo el = new ElementoDemo(aesd, "aeSwingDemo.component.SpinnerDemo" );
		el.addOtraClase( "aeSwingDemo.component.CyclingSpinnerListModel" );
		el.addOtraClase( "aeSwingDemo.component.SpringUtilities" );
		AESwingDemoPlayer v = new AESwingDemoPlayer();
		v.setVisible( true );
		v.setFuenteToFile( el.getListaFilesClases().get(0) );
		// Contenido c1 = new Contenido( "prueba", new ReferenciaACodigo( el, 0, 2099, 2233 ));
		Contenido c1 = new Contenido( "prueba", new ReferenciaACodigo( el, 0, 30, 50 ));
		el.addContenido( c1 );
		v.setFuenteSel( c1.getRefCodigo() );
		AESDUtils.cambiarFontUI( new javax.swing.plaf.FontUIResource("Arial",Font.BOLD,20) );
    	// AESDUtils.cambiarFontUI( "Label.font", new javax.swing.plaf.FontUIResource("Arial",Font.BOLD,30) );

		v.setVentanaEnCurso( el.activarVentanaPrincipal() );
		v.getVentanaEnCurso().setSize( 400, 300 );
		v.getVentanaEnCurso().setLocation( new Point( 500, 300 ));
		v.getVentanaEnCurso().setAlwaysOnTop( true );
		v.getVentanaEnCurso().setVisible( true );
	}
	
}

class MiListCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1725136079822919719L;
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	public Component getListCellRendererComponent(JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
		if (index == 2) {
			Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus );
			c.setBackground( Color.green );
			return c;
		} else {
			return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus );
		}
	}
}

class AESDUtils {
	/** Cambia todos los fonts por defecto del interfaz de usuario<p>
	 * Ejemplo: cambiarFontUI( new javax.swing.plaf.FontUIResource
	 *                         ("Arial", Font.BOLD, 16 ));
	 * @param font	Font a poner por defecto
	 */
	public static void cambiarFontUI(javax.swing.plaf.FontUIResource font){
		java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get (key);
			if (value instanceof javax.swing.plaf.FontUIResource) {
				UIManager.put (key, font);
			}
			System.out.println( key + "\t" + ((value==null)?"NULO":value.getClass().toString()) );
		}
		System.out.println( );
		System.out.println( "L&F");
		System.out.println( );
		keys = UIManager.getLookAndFeelDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get (key);
			if (value instanceof javax.swing.plaf.FontUIResource) {
				UIManager.put (key, font);
			}
			System.out.println( key + "\t" + ((value==null)?"NULO":value.getClass().toString()) );
		}
	}    
	
	/** Cambia el font por defecto del interfaz de usuario de 
	 * un componente en particular<p>
	 * Ejemplo: cambiarFontUI( "Label.font",
	 * 		new javax.swing.plaf.FontUIResource("Arial", Font.BOLD, 16 ));
	 * @param componentFontKey	Nombre de la clave del font de componente en el UI
	 * @param font	Font a poner por defecto
	 */
	public static void cambiarFontUI( String componentFontKey, javax.swing.plaf.FontUIResource font ) {
		UIManager.put(componentFontKey,font);
	}
}
