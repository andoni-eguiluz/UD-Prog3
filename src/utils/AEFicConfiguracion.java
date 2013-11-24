package utils;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AEFicConfiguracion extends JDialog {

	private ArrayList<JTextField> tfPropiedad = new ArrayList<JTextField>();
	private ArrayList<String> propiedad = new ArrayList<String>();
	private ArrayList<String> valorDefecto = new ArrayList<String>();
	private String nomFic;
	private Properties misProps = null;
	private boolean hayCambios = false;
	
	/** Construye un diálogo con definición particular de configuración de propiedades
	 * @param nomFic	Nombre del fichero en el que se guardarán/cargarán las propiedades
	 * 		Cada vez que se abra el diálogo interactivo, se guardarán el fichero si hay cualquier cambio
	 * @param props	Array de strings de nombres de propiedades
	 * @param mensajes	Array de mensajes a mostrar al usuario con cada propiedad
	 * @param defecto	Array de strings de valores por defecto de las propiedades
	 * @param tipos	Array de Strings con los siguientes valores: null o "" si es un texto normal, 
	 *      "FILE" si corresponde a un path de fichero, "DIR" si corresponde a un path de carpeta 
	 * @throws IndexOutOfBoundsException	Los cuatro arrays props, mensajes, defecto, carpetas deben tener la misma longitud, con
	 * 		correspondencia uno a uno. Si alguno tiene longitud diferente no se crea el objeto y se lanza esta excepción
	 */
	public AEFicConfiguracion( String nomFic, String[] props, String[] mensajes, String[] defecto, String[] tipos ) throws IndexOutOfBoundsException {
		if (props.length != defecto.length || defecto.length != tipos.length || tipos.length != mensajes.length) 
			throw new IndexOutOfBoundsException( "AEFicConfiguracion: Inicialización incorrecta");
		this.nomFic = nomFic;
		setModal(true);
		setTitle( "(AE) Configuración" );
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel();
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		for (int i = 0; i < props.length; i++ ) {
			propiedad.add( props[i] );
			valorDefecto.add( (defecto[i]==null)?"":defecto[i] );
			JPanel panel1 = new JPanel();
			((FlowLayout) panel1.getLayout()).setAlignment(FlowLayout.LEFT);  // El Layout de JPanel ya es Flow por defecto
			contentPanel.add(panel1);
			JLabel lbl = new JLabel( mensajes[i]);
				panel1.add(lbl);
			JTextField tf = new JTextField();
			tfPropiedad.add( tf );
				panel1.add( tf );
				if (tipos[i]==null || tipos[i].equals("")) {  // TextField normal
					tf.setColumns(20);
					// tf.setEditable( true );
					tf.addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
							hayCambios = true;
						}
					});
				} else {
					tf.setColumns(30);
					tf.setEditable( false );
					JButton btn = new JButton("Cambiar");
					if (tipos[i].equals("DIR")) {
						btn.addActionListener( new BotonCambiarPath( tf, true ) );
					} else { // FIC
						btn.addActionListener( new BotonCambiarPath( tf, false ) );
					}
					panel1.add(btn);
				}
			contentPanel.add( panel1 );
		}
		JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Confirmar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						botonAceptar();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						botonCancelar();
					}
				});
				buttonPane.add(cancelButton);
			}
		getProps();  // Inicializa las propiedades desde el fichero
		pack();
	}

	private void botonAceptar() {
		if (hayCambios) {
			for (int i=0; i < propiedad.size(); i++) {
				misProps.setProperty( propiedad.get(i), tfPropiedad.get(i).getText() );
			}
			saveProps();
		}
		hayCambios = false;
		setVisible(false);
	}
	
	private void botonCancelar() {
		hayCambios = false;
		setVisible(false);
	}
	
	private class BotonCambiarPath implements ActionListener {
		private JTextField tf;
		private boolean soloDirs;
		BotonCambiarPath( JTextField tf, boolean soloDirs ) {
			this.tf = tf;
			this.soloDirs = soloDirs;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser( tf.getText() );
			String mens = "Elige nuevo fichero";
			if (soloDirs) { 
				fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
				mens = "Elige nueva carpeta";
			}
			int valor = fc.showDialog( AEFicConfiguracion.this, mens );
			if (valor == JFileChooser.APPROVE_OPTION) {
				tf.setText( fc.getSelectedFile().getPath() );
				hayCambios = true;
			}
		}
	}

	/** Devuelve el objeto que guarda las propiedades de configuración
	 * @return	objeto Properties	
	 */
	public Properties getProps() {
		if (misProps == null) initProps();
		return misProps;
	}
	
	/** Devuelve el valor de la propiedad indicada. Funciona como una llamada a getProperty sobre getProps().
	 * @param propName	Nombre de la propiedad que se busca
	 * @return	Valor de la propiedad, null si esta propiedad no existe
	 */
	public String getProp( String propName ) {
		if (misProps == null) initProps();
		return misProps.getProperty( propName );
	}
	
	private void initProps() {
		misProps = new Properties();
		try {
			misProps.loadFromXML( new FileInputStream( nomFic ) );
		} catch (Exception e) { // Valores por defecto
			for (int i=0; i < propiedad.size(); i++) {
				misProps.setProperty( propiedad.get(i), valorDefecto.get(i) );
			}
		}
		refrescaPropsVentana();
	}
	
	private void refrescaPropsVentana() {
		for (int i=0; i < propiedad.size(); i++) {
			tfPropiedad.get(i).setText( misProps.getProperty( propiedad.get(i) ));
		}
	}
	
	/** Guarda el fichero de propiedades con los valores que estén actualmente definidos
	 */
	public void saveProps() {
		try {
			misProps.storeToXML( new PrintStream( nomFic ), "Propiedades de AEFicConfiguracion" );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	/** Main de prueba, genera un fichero de configuración con unas pocas propiedades
	 */
	public static void main(String[] args) {
		String[] props = { "UltimoUsuario", "UltimaFecha", "UltimoFic", "DirDatos" };
		String[] mensajes = { "Último usuario que ha accedido: ", "Fecha de último acceso: ", "Último fichero abierto: ", "Directorio de datos: " };
		String[] defecto = { "admin", null, ".", "c:\\Users\\" };
		String[] carpetas = { "", "", "FILE", "DIR" };
		try {
			AEFicConfiguracion dialog = new AEFicConfiguracion( "prueba.prop.xml", props, mensajes, defecto, carpetas );
			dialog.setVisible(true);  // Edición interactiva de configuración
			dialog.dispose();  // Cierra la ventana para que swing acabe
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
