package tema6;

import javax.swing.*;
import java.awt.*;

public class MiPrimeraVentana extends JFrame {
	private JTextField miCuadroDeTexto;
	private JButton miBoton;
	private JButton botFinal;
	public void setCuadroDeTexto( String nuevoTexto ) {
		miCuadroDeTexto.setText( nuevoTexto );
	}
	public JButton getBoton1() { return miBoton; }
	public JButton getBotFinal() { return botFinal; }
	public MiPrimeraVentana( String titulo ) {
		// Contenedores
		JPanel miPanel = new JPanel();
		miPanel.setLayout( new BorderLayout() );
		// Componentes
		miCuadroDeTexto = new JTextField( "hola mundo", 15 );
		miBoton = new JButton( "pulsa aquí!");
		JLabel miExplicacion = new JLabel( "esto es yo que sé: ");
		botFinal = new JButton( "Finalizar" );
		// Añadir componentes y contenedores
		miPanel.add( miExplicacion, "North" );
		miPanel.add( miCuadroDeTexto, BorderLayout.CENTER );
		miPanel.add( miBoton, BorderLayout.SOUTH );
		miPanel.add( botFinal, "East" );
		this.add( miPanel );
		// Formato
		this.setSize( 320, 200 );
		this.setTitle( titulo );
	}
	public JTextField getCuadroTexto() {
		return miCuadroDeTexto;
	}

	public static void main(String[] args) {
		MiPrimeraVentana miVentana = new MiPrimeraVentana( "Título 1" );
		miVentana.setVisible( true );
		MiPrimeraVentana miVentana2 = new MiPrimeraVentana( "Ventana 2" );
		miVentana2.setVisible( true );
		
		//
		//
		System.out.println( miVentana2.getCuadroTexto().getText() );
	}

}
