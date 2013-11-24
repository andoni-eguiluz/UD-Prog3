package ejemploJuego;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;

public class GestorTeclado implements KeyListener {
	private static float VEL_LENTA = 25f; 
	private static float VEL_RAPIDA = 200f; 
	protected Prota p;
	protected JComponent[] componentesTeclas;
	public GestorTeclado( JComponent[] listaComponentes, Prota p ) {
        for (JComponent c : listaComponentes) {
            c.addKeyListener( this );
        	c.setFocusable( true );
        }
        this.p = p;
        componentesTeclas = listaComponentes;
    }

	public void close() {
        for (JComponent c : componentesTeclas) {
            c.removeKeyListener( this );
        }
	}
	
    /** This listener is called for character keys. */
    public void keyTyped(KeyEvent kevt) {
        // char c = kevt.getKeyChar();
    }

    /** This listener is called for both character and non-character keys. */
    public void keyPressed(KeyEvent e) {
    	float speed;
        // Chequea la tecla shift y mueve más rápido con esa tecla
        if (e.isShiftDown()) {
            speed = VEL_RAPIDA;
        } else {
            speed = VEL_LENTA;
        }        
        // Procesa las teclas de cursor
        if (p!=null) {
	        switch (e.getKeyCode()) {
	        	case KeyEvent.VK_SHIFT: p.aceleraMovimiento( VEL_RAPIDA ); break;
	            case KeyEvent.VK_LEFT : p.activaMovimiento( -speed ); break;
	            case KeyEvent.VK_RIGHT: p.activaMovimiento( speed ); break;
	            // case KeyEvent.VK_UP   : break;
	            // case KeyEvent.VK_DOWN : break;
	        }
        }        
    }

    public void keyReleased(KeyEvent ke) {
        if (p!=null) {
	        switch (ke.getKeyCode()) {
	        	case KeyEvent.VK_SHIFT: p.aceleraMovimiento( VEL_LENTA ); break;
	            case KeyEvent.VK_LEFT : p.activaMovimiento( 0 ); break;
	            case KeyEvent.VK_RIGHT: p.activaMovimiento( 0 ); break;
	        }
        }
    }
}