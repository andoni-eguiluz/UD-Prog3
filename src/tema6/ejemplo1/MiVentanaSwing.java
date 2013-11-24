package tema6.ejemplo1;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.*;

public class MiVentanaSwing extends JFrame
{
	JTextField tfEmail;
	JTextArea taMensaje;
	JButton botonEnviar;
	JButton botonSalir;

	public MiVentanaSwing()
	{
		// Creación de componentes y contenedores
		JPanel panelCentral = new JPanel();
		JPanel panelSup = new JPanel();
		tfEmail = new JTextField(20);
		taMensaje = new JTextArea(5,35);
		JLabel etiqueta1 = new JLabel("Email:");
		botonEnviar = new JButton("Enviar");
		botonSalir = new JButton("Salir");
					
		// Añadir comp a contenedores
		panelSup.setLayout( new FlowLayout( FlowLayout.LEFT ) );
		panelSup.add(etiqueta1);
		panelSup.add(tfEmail);		
	
		panelCentral.add( new JLabel("Escribe aquí tu Mensaje") );
		panelCentral.add(taMensaje);

		JPanel panelInferior = new JPanel();
		panelInferior.add(botonEnviar);
		panelInferior.add(botonSalir);

		this.setLayout( new BorderLayout() );
		this.add(panelSup, BorderLayout.NORTH );
		this.add(panelCentral, BorderLayout.CENTER );
		this.add(panelInferior, BorderLayout.SOUTH );

		// Formato
		this.setSize(300,225);
		this.setTitle("Envío de Correo Electrónico");
	//	this.setVisible(true);
	}
	
	void hacerAlgo() {
		botonEnviar.setText( "hola");
	}

	public static void main(String[] args)
	{
		boolean pruebas = true;
		if (!(pruebas)) {
			MiVentanaSwing v = new MiVentanaSwing();
			// MiVentanaSwing v2 = new MiVentanaSwing();  ... etc etc
			v.setVisible(true);
			System.out.println( "Final" );
			// try { Thread.sleep( 10000 ); } catch (InterruptedException e) {}
			// v.dispose();
			// v2.dispose();
			// System.exit(0);
		} else {	
			JFrame f = new JFrame();
			f.setTitle( "Mi ventanita" );
			f.setSize( 300, 200 );
			JPanel p = new JPanel();
			JTextField miCuadro = new JTextField( "saluda", 15 );
			miCuadro.setPreferredSize( new java.awt.Dimension( 50, 50 ));
			System.out.println( miCuadro.getMinimumSize() );
			System.out.println( miCuadro.getMaximumSize() );
			JButton b2 = new JButton( "no saludes" );
			f.setVisible( true );
			p.add( miCuadro );
			p.add( b2 );
			
			f.add( p );
			System.out.println( "Se acabó");
			try { Thread.sleep( 10000 ); } catch (InterruptedException e) {}
			System.out.println( miCuadro.getText() );
			// f.dispose();
		}
	}
}
