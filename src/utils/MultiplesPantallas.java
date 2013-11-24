package utils;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import javax.swing.JFrame;

public class MultiplesPantallas {
	
	/** Devuelve la pantalla en la que est� la ventana indicada.
	 * Para consultar en qu� pantalla est� considera el punto CENTRAL de la ventana
	 * @param frame	Ventana a consultar
	 * @return	N�mero de pantalla en la que est�, -1 si el punto central no est� en ninguna
	 */
	public static int enQuePantallaEsta( JFrame frame ) {
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice[] gs = ge.getScreenDevices();
		Point miCentro = frame.getLocation();
		miCentro.move( miCentro.x+frame.getWidth()/2, miCentro.y+frame.getHeight()/2 );
    	for (int pant=0; pant<gs.length; pant++) {
    		if (gs[pant].getDefaultConfiguration().getBounds().contains( miCentro )) {
    	    	return pant;  // frame est� en esta pantalla
    		}
    	}
    	return -1;  // No est� en ninguna pantalla
	}

	/** Pasa la ventana desde donde est� a la pantalla indicada.
	 * @param screen	N�mero de pantalla (de 0 a n-1). Si no existe, 
	 * 					se mover� a la ventana principal
	 * @param frame	Ventana a mover
	 */
	public static void pasaAPantalla( int screen, JFrame frame )
	{
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice[] gs = ge.getScreenDevices();
	    if( screen>-1 && screen<gs.length ) {  // Si el num de pantalla es correcto
	    	if ((frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
	    		// Si estaba maximizada, maximizarlo en la pantalla objetivo
		        // gs[screen].setFullScreenWindow( frame );  // No hace exactamente lo que queremos
	    		frame.setLocation( gs[screen].getDefaultConfiguration().getBounds().getLocation() );
	    		frame.setExtendedState( frame.getExtendedState() | JFrame.MAXIMIZED_BOTH );
	    	} else {
	    		// Si no estaba maximizada, colocarla en la pantalla objetivo:
	    		int dondeEstoy = enQuePantallaEsta(frame);
		    	if (dondeEstoy >= 0) {  // Si la encuentra (si no es que est� fuera del escritorio: no se hace nada)
		    		Point origen = gs[dondeEstoy].getDefaultConfiguration().getBounds().getLocation(); 
		    		int distAOrigenX = frame.getX() - origen.x;
		    		int distAOrigenY = frame.getY() - origen.y;
		    		Point destino = gs[screen].getDefaultConfiguration().getBounds().getLocation();
		    		destino.move( destino.x+distAOrigenX, destino.y+distAOrigenY );
		    		frame.setLocation( destino );
		    	}
	    	}
	    }
	}	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame vent = new JFrame();
		vent.setSize( 300, 300 );
		vent.setLocation( 1500, 400 );
		vent.setVisible( true );
		vent.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		System.out.println( "  " + vent.getLocation() );
		System.out.println( "Est� en pantalla " + enQuePantallaEsta(vent) );
		pasaAPantalla( 0, vent );
		System.out.println( "  " + vent.getLocation() );
		System.out.println( "Ahora est� en pantalla " + enQuePantallaEsta(vent) );
		try { Thread.sleep( 5000 ); } catch (InterruptedException e) {}

		vent.setLocation( 1500, 200 );
		vent.setVisible( true );
		System.out.println( "  " + vent.getBounds() );
		System.out.println( "Cambio. Ahora est� en pantalla " + enQuePantallaEsta(vent) );
		try { Thread.sleep( 2000 ); } catch (InterruptedException e) {}
		vent.setExtendedState( vent.getExtendedState() | JFrame.MAXIMIZED_BOTH );
		vent.setVisible( true );
		System.out.println( "  " + vent.getBounds() );
		System.out.println( "Est� maximizada en pantalla " + enQuePantallaEsta(vent) );
		try { Thread.sleep( 2000 ); } catch (InterruptedException e) {}
		pasaAPantalla( 0, vent );
		System.out.println( "  " + vent.getBounds() );
		System.out.println( "Ahora est� maximizada en pantalla " + enQuePantallaEsta(vent) );
	}

}
