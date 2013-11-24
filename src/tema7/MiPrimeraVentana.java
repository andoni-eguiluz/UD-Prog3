package tema7;

import javax.swing.*;

import tema6.ejemplo1.MiVentana;

import java.awt.*;
import java.awt.event.*;

public class MiPrimeraVentana extends JFrame {
	private JTextField miCuadroDeTexto;
	private JButton miBoton;
	private JButton botFinal;
	public void setCuadroDeTexto( String nuevoTexto ) {
		miCuadroDeTexto.setText( nuevoTexto );
	}
	public JButton getBoton1() { return miBoton; }
	public JButton getBotFinal() { return botFinal; }

	private static MiPrimeraVentana miVentana = null;
	public static MiPrimeraVentana getInstance() {
		if (miVentana == null) {
			miVentana = new MiPrimeraVentana( "" );
		}
		return miVentana;
	}
	
	private MiPrimeraVentana( String titulo ) {
		// Contenedores
		JPanel miPanel = new JPanel();
		miPanel.setLayout( new BorderLayout() );
		// Componentes
		miCuadroDeTexto = new JTextField( "hola mundo", 15 );
		miBoton = new JButton( "pulsa aquí!");
		miBoton.setToolTipText( "Botón para lo que sea");
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
		// Escuchadores
		miBoton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						setCuadroDeTexto( "" );
					}
				} );
		botFinal.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				} );
		miCuadroDeTexto.addMouseListener(
				new MouseListener() {
					@Override
					public void mouseReleased(MouseEvent arg0) {
						System.out.println( "Release!" );
					}
					
					@Override
					public void mousePressed(MouseEvent arg0) {
						System.out.println( "Press!" );
					}
					
					@Override
					public void mouseExited(MouseEvent arg0) {
						System.out.println( "Exit!" );
					}
					
					@Override
					public void mouseEntered(MouseEvent arg0) {
						System.out.println( "Enter!" );
					}
					
					@Override
					public void mouseClicked(MouseEvent arg0) {
						System.out.println( "Click!" );
					}
				} );
	}
	
	
	
	public JTextField getCuadroTexto() {
		return miCuadroDeTexto;
	}

	public static void main(String[] args) {
		MiPrimeraVentana.getInstance().setVisible( true );
	}

	
}

