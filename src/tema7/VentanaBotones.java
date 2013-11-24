package tema7;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;

public class VentanaBotones extends JFrame implements ActionListener
{	
	JPanel panelBotones;
	JPanel panelEtiqueta;
	JLabel etiqueta;
	JButton boton1;
	JButton boton2;
	JButton boton3;
	public VentanaBotones()
	{			
		etiqueta = new JLabel();
		panelBotones = new JPanel();
		panelEtiqueta = new JPanel();
		boton1 = new JButton("Boton1");
		boton2 = new JButton("Boton2");
		boton3 = new JButton("Boton3");
		panelEtiqueta.add(etiqueta);
		panelBotones.add(boton1);
		panelBotones.add(boton2);
		panelBotones.add(boton3);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panelEtiqueta,"North");
		this.getContentPane().add(panelBotones,"South");
		boton1.addActionListener(this);
		boton2.addActionListener(this);
		boton3.addActionListener(this);
		
		this.setSize(300,100);
		this.setTitle("Ejemplo 3 Botones");
		this.setVisible(true);
	}
	public void actionPerformed(ActionEvent e)
	{
		JButton botonPulsado = (JButton)e.getSource();
		if (botonPulsado == boton1) 
			etiqueta.setText("Se ha pulsado el botón 1");		
		if (botonPulsado == boton2) 
			etiqueta.setText("Se ha pulsado el botón 2");		
		if (botonPulsado == boton3) 
			etiqueta.setText("Se ha pulsado el botón 3");
	}
		
	public static void main(String[] args)
	{
		new VentanaBotones();
	}
}
