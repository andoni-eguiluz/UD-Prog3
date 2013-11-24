package tema7;

import javax.swing.JFrame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;

class EscuchadorBoton implements ActionListener
{
	JLabel etiquetaDelEscuchador;

	public EscuchadorBoton(JLabel etiq)
	{
		this.etiquetaDelEscuchador = etiq;
	}

	public void actionPerformed(ActionEvent e)
	{
		etiquetaDelEscuchador.setText(
				"Botón Pulsado: " + new Date());		
	}
}

public class VentanaBoton extends JFrame  implements ActionListener,
	MouseListener, WindowListener {
	
	private JLabel etiqueta;
	private JButton boton;
	private JButton boton2;

	public VentanaBoton()
	{
		JPanel panelBoton = new JPanel();
		etiqueta = new JLabel();
		boton = new JButton("Pulsa Aquí");
		boton2 = new JButton("O aquí");

		panelBoton.add(boton);
		panelBoton.add(boton2);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(etiqueta,"North");
		this.getContentPane().add(panelBoton,"South");

//		EscuchadorBoton escuchador = 
//			new EscuchadorBoton(etiqueta);
		boton.addActionListener( this );
		boton2.addActionListener( this );
		boton.addMouseListener( this );
		addWindowListener( this );
		
		this.setSize(300,100);
		this.setTitle("Ejemplo Sencillo");
		this.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new VentanaBoton();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == boton) {
			etiqueta.setText(
					"Botón Pulsado: " + new Date());		
		} else if (e.getSource() == boton2) {
			etiqueta.setText( "Otra cosa");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println( "CLICKED");
		System.out.println( e.getX() + "," + e.getY() );
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println( "PRESSED");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println( "RELEASED");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		boton.setText( "PULSA AQUÍ");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		boton.setText( "Pulsa Aquí" );
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		dispose();
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
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

}
