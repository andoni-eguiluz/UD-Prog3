package tema6.ejemplo2;

import java.awt.Frame;
import java.awt.Button;
import java.awt.FlowLayout;

public class VentanaFlowLayout extends Frame
{
	public VentanaFlowLayout()
	{
		Button boton1 = new Button("Boton 1");
		Button boton2 = new Button("Boton 2");
		Button boton3 = new Button("Boton 3");
		Button boton4 = new Button("Boton 4");

		this.setLayout(new FlowLayout());

		this.add(boton1);
		this.add(boton2);
		this.add(boton3);
		this.add(boton4);

		this.setSize(300,150);
		this.setVisible(true);
	}

	public static void main(String[] args)
	{
		new VentanaFlowLayout();
	}
}
