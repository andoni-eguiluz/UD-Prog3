package ejemploJuego;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class JPanelEdicion extends JPanel {
	private ArrayList<Envolvente> envolventes = 
		new ArrayList<Envolvente>();
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Envolvente e : envolventes) {
			if (e!=null && e.getGPath() != null) {
				((java.awt.Graphics2D) g).draw( e );  // dibuja líneas de la forma
				Point2D[] puntos = e.getPuntosDe( false );
				for (Point2D p : puntos) {  // dibuja puntos "gordos" de la forma
					((java.awt.Graphics2D) g).drawOval( (int)p.getX()-4, (int)p.getY()-4, 8, 8 );
				}
			}
		}
	}
	public void addEnvolvente( Envolvente e ) {
		envolventes.add( e );
		repaint();
	}
	public void removeEnvolventes() {
		envolventes.clear();
		repaint();
	}
	public void refreshEnvolventes() {
		repaint();
	}
}
