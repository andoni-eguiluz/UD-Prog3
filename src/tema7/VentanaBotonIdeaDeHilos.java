package tema7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;

class EscuchadorBotonVentanaHilos implements ActionListener
{
	private JTextArea miTA = null;
	public EscuchadorBotonVentanaHilos (JTextArea ta ) {
		miTA = ta;
	}
	public void actionPerformed(ActionEvent e) {
		// Y lo saca al textArea
		miTA.append( "Botón Pulsado: " + new Date() + "\n" );
		
		// Y qué pasa si tarda mucho tiempo algo...?
		// try { Thread.sleep( 100000 ); } catch (InterruptedException e2) {}
		
		// Swing no se corta con las excepciones. Las muestra y sigue pintando
		// throw new RuntimeException();  
	}
}

public class VentanaBotonIdeaDeHilos extends JFrame
{
	private static VentanaBotonIdeaDeHilos miVentana = null;
	public static VentanaBotonIdeaDeHilos getVentana() { return miVentana; }
	
	JPanel panelBoton;
	JTextArea area;
	JButton boton;

	public VentanaBotonIdeaDeHilos()
	{
		miVentana = this;
		area = new JTextArea( 20, 50 );
		panelBoton = new JPanel();
		boton = new JButton("Pulsa Aquí");

		panelBoton.add(boton);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(new JScrollPane(area),"Center");
		this.getContentPane().add(panelBoton,"South");

		EscuchadorBotonVentanaHilos escuchador = 
				new EscuchadorBotonVentanaHilos(area);
		boton.addActionListener( escuchador );
		
		this.setSize(600,400);
		this.setTitle("Ejemplo Sencillo para entender hilos");
	}
	
	private double procesoIntenso() {
		double r = 3.0;
			// Hago algo que tarda mucho tiempo
			try { Thread.sleep(3000); } catch (InterruptedException e) {}
//			for (int i = 0; i<8000000; i++) {
//				for (int j = 1; i<2000000; i++) {
//					for (int k = 0; i<100000000; i++) {
//						r = r + i / j * k; // r * r * Math.PI * i * i / j * k * Math.sin( k );
//					}
//				}				
//			}
		return r;
	}
	
	public static void main(String[] args)
	{
		VentanaBotonIdeaDeHilos v = new VentanaBotonIdeaDeHilos();
		v.setVisible(true);
		while (true) {
			double r = v.procesoIntenso();
			// Y visualizo el resultado
			v.area.append( r + "\n" );
		}
	}
}
