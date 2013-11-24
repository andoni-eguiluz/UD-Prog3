package tema7;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;

public class VentanaBotonEscuchador extends JFrame implements ActionListener
{
	JPanel panelBoton;
	JLabel etiqueta;
	JButton boton;
	public VentanaBotonEscuchador()
	{
		etiqueta = new JLabel();
		panelBoton = new JPanel();
		boton = new JButton("Pulsa Aquí");
		panelBoton.add(boton);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(etiqueta,"North");
		this.getContentPane().add(panelBoton,"South");
		boton.addActionListener(this);
			
		this.setSize(300,100);
		this.setTitle("Ejemplo Sencillo");
		this.setVisible(true);
	}
	public void actionPerformed(ActionEvent e)
	{
		etiqueta.setText("Botón Pulsado: " + new Date());
	}
	
	public static void main(String[] args)
	{
		new VentanaBoton();
	}
}