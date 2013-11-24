package tema6.ejemplo4;

import java.awt.Frame;
import java.awt.Button;
import java.awt.GridLayout;

public class VentanaGridLayout extends Frame
{
	public VentanaGridLayout()
	{
		Button boton1 = new Button("Celda1");
		Button boton2 = new Button("Celda2");
		Button boton3 = new Button("Celda3");
		Button boton4 = new Button("Celda4");
		Button boton5 = new Button("Celda5");
		Button boton6 = new Button("Celda6");

		this.setLayout(new GridLayout(3,2));

		this.add(boton1);
		this.add(boton2);
		this.add(boton3);
		this.add(boton4);
		this.add(boton5);
		this.add(boton6);

		this.setSize(300,150);
		this.setVisible(true);
	}
	public static void main(String[] args)
	{
		new VentanaGridLayout();
	}
}
