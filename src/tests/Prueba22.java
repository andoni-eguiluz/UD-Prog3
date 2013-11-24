package tests;

import javax.swing.*;
import java.awt.*;

public class Prueba22 extends JFrame {
	JPanel JPanel3Central;
	JLabel etiq2;
	public Prueba22() {
		JPanel3Central = new JPanel();
		JPanel3Central.setLayout(new GridLayout(1,2));
		JPanel3Central.add(new JLabel("label 1"));
		etiq2 = new JLabel("label 2");
		JPanel3Central.add(etiq2);
		getContentPane().add(JPanel3Central);
	}
	void sustituyePanelDerecho( JComponent c ) {
		JPanel3Central.remove( etiq2 );
		JPanel3Central.add( c );
		pack();
	}
	public static void main( String s[] ) {
		Prueba22 v = new Prueba22();
		v.pack();
		v.setVisible(true);
		try { Thread.sleep(2000); } catch (InterruptedException e) {}
		v.sustituyePanelDerecho( new JLabel("label nueva") );
	}
}
