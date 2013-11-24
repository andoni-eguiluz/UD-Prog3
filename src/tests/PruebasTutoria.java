package tests;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class PruebasTutoria {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame miv = new JFrame();
		miv.setVisible( true );
		miv.setSize( 320, 200 );
		miv.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		String[] valores = new String[] { "valor 1", "valor 2" }; 
		JList lista = new JList(valores);
		JTextField texto = new JTextField( 20 );
		miv.getContentPane().add( new JScrollPane(lista), BorderLayout.CENTER );
		miv.getContentPane().add( texto, BorderLayout.SOUTH );
		MiKeyListener met = new MiKeyListener();
		lista.addKeyListener( met );
		KeyListener miesc2 = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar()<'0' || e.getKeyChar()>'9') e.consume();
			}
		};
		texto.addKeyListener( miesc2 );
	}
	

}

class MiKeyListener implements KeyListener {
	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println( "TYPED=" + e.getKeyChar() );
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println( "TYPED=" + e.getKeyCode() + " -- modif " + e.getModifiers() );
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println( "RELEASED=" + e.getKeyCode() );
	}
	
}
