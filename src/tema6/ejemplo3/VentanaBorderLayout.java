package tema6.ejemplo3;

import java.awt.Frame;
import java.awt.Button;
import java.awt.BorderLayout;

public class VentanaBorderLayout extends Frame
{
	public VentanaBorderLayout()
	{
		Button boton1 = new Button("Norte");
		Button boton2 = new Button("Sur");
		Button boton3 = new Button("Este");
		Button boton4 = new Button("Oeste");
		Button boton5 = new Button("Centro");

		this.setLayout(new BorderLayout());

		this.add(boton1, "North");
		this.add(boton2, "South");
		//this.add(boton3, "East");
		this.add(boton4, "West");
		this.add(boton5, "Center");

		this.setSize(300,150);
		this.setVisible(true);
	}

	public static void main(String[] args)
	{
		new VentanaBorderLayout();
	}
}
