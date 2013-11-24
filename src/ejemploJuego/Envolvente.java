package ejemploJuego;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Envolvente implements Shape, Serializable {
	private static final long serialVersionUID = -5545603745832948838L;
	private GeneralPath miPath;
	private double offX = 0.0;
	private double offY = 0.0;
	
	/** Constructor de envolvente vacía con offset 0,0
	 */
	public Envolvente() {
		miPath = new GeneralPath();
	}
	
	/** Constructor de envolvente partiendo de otra dada, con el desplazamiento
	 * de todos sus puntos según el offset indicado
	 * @param e	Envolvente original
	 * @param offX	Offset horizontal
	 * @param offY	Offset vertical
	 */
	public Envolvente( Envolvente e, double offX, double offY ) {
		miPath = new GeneralPath();
		Point2D[] puntos = e.getPuntosDe( false );
		for (Point2D p : puntos) {
			lineTo( new Point2D.Double( p.getX()+offX, p.getY()+offY ) );
		}
		closePath();
	}
	
	/** Transforma la envolvente de acuerdo al ratio de aspecto proporcional indicado
	 * (objeto que se achata o ensancha)
	 * @param relAncho	Relación de ancho: 1.0 sigue igual, < 1.0 disminuye, > 1.0 aumenta
	 * @param relAlto	Relación de alto (como el ancho)
	 */
	public void transformaEnvolvente( double relAncho, double relAlto ) {
		Point2D[] puntos = getPuntosDe( false );
		miPath = new GeneralPath();
		for (Point2D p : puntos) {
			lineTo( new Point2D.Double( p.getX()*relAncho, p.getY()*relAlto ) );
		}
		closePath();
	}
	/** Actualiza el offset de desplazamiento de la envolvente
	 * @param offX	Offset horizontal
	 * @param offY	Offset vertical
	 */
	public void setOffset( double offX, double offY ) {
		this.offX = offX;
		this.offY = offY;
	}
	
	/** Devuelve el offset horizontal de la envolvente
	 * @return	Offset horizontal
	 */
	public double getOffsetX() {
		return offX;
	}
	
	/** Devuelve el offset vertical de la envolvente
	 * @return	Offset vertical
	 */
	public double getOffsetY() {
		return offY;
	}
	
	@Override
	public Rectangle getBounds() {
		Envolvente transformada = new Envolvente( this, getOffsetX(), getOffsetY() );
		return transformada.miPath.getBounds();
	}
	@Override
	public Rectangle2D getBounds2D() {
		Envolvente transformada = new Envolvente( this, getOffsetX(), getOffsetY() );
		return transformada.miPath.getBounds2D();
	}
	@Override
	public boolean contains(double x, double y) {
		Envolvente transformada = new Envolvente( this, getOffsetX(), getOffsetY() );
		return transformada.miPath.contains(x,y);
	}
	@Override
	public boolean contains(Point2D p) {
		Envolvente transformada = new Envolvente( this, getOffsetX(), getOffsetY() );
		return transformada.miPath.contains(p);
	}
	@Override
	public boolean intersects(double x, double y, double w, double h) {
		Envolvente transformada = new Envolvente( this, getOffsetX(), getOffsetY() );
		return transformada.miPath.intersects(x,y,w,h);
	}
	@Override
	public boolean intersects(Rectangle2D r) {
		Envolvente transformada = new Envolvente( this, getOffsetX(), getOffsetY() );
		return transformada.miPath.intersects(r);
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		Envolvente transformada = new Envolvente( this, getOffsetX(), getOffsetY() );
		return transformada.miPath.contains(x, y, w, h);
	}
	@Override
	public boolean contains(Rectangle2D r) {
		Envolvente transformada = new Envolvente( this, getOffsetX(), getOffsetY() );
		return transformada.miPath.contains(r);
	}
	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		return miPath.getPathIterator(at);
	}
	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return miPath.getPathIterator(at, flatness);
	}
	
	/** Devuelve el último punto añadido a la envolvente
	 * @return	Ultimo punto, null si está vacía
	 */
	public Point2D getCurrentPoint() {
		return miPath.getCurrentPoint();
	}
	
	/**	Devuelve el objeto de path asociado a la envolvente
	 * @return	path asociado
	 */
	public GeneralPath getGPath() {
		return miPath;
	}
	
		private	static Point2D getPuntoDeArray6( double[] ar ) {
			return new Point2D.Double( ar[0], ar[1] );
		}

	/**	Devuelve un array de todos los puntos de la envolvente
	 * sobre el origen 0,0
	 * @param incluirCierre	Si true, se repite el primer punto al final
	 * @return	Array de puntos
	 */
	public Point2D[] getPuntosDe( boolean incluirCierre ) {
		PathIterator puntos = miPath.getPathIterator(new AffineTransform());
		double[] punto6 = new double[6];
		Point2D inicio = null;
		Point2D punto = null;
		ArrayList<Point2D> listaPuntos = new ArrayList<Point2D>(); 
		while (!puntos.isDone()) {
			int tipo = puntos.currentSegment( punto6 );
			if (tipo == PathIterator.SEG_MOVETO) {
				inicio = getPuntoDeArray6(punto6);
				punto = inicio;
			} else if (tipo == PathIterator.SEG_CLOSE) {
				punto = inicio;
				if (!incluirCierre) break;
				// si se quiere procesar, quitar el break 
			} else {
				punto = getPuntoDeArray6(punto6);
				listaPuntos.add( punto );
			}
			listaPuntos.add( punto );
			// System.out.println( tipo + " ... " + punto );
			puntos.next();
		}
		Point2D[] result = new Point2D[listaPuntos.size()];
		int i=0;
		for (Point2D p : listaPuntos) { 
			result[i] = p;
			i++;
		}
		return result;
	}

	/** Chequea la intersección entre dos envolventes
	 * @param e2	Segunda envolvente
	 * @return	true si tocan en cualquier punto o una es completamente interior a la otra
	 */
	public boolean intersects( Envolvente e2 ) {
		Point2D[] puntos = e2.getPuntosDe(false);
		for (Point2D p : puntos) {
			Point2D.Double p2 = new Point2D.Double( p.getX()+e2.getOffsetX(), p.getY()+e2.getOffsetY() );
			if (contains(p2)) {
				// System.out.println( "CHOCAN! Chequeo de " + toString() + " y " + e2.toString() );
				return true;
			}
		}
		return false;
	}
	
	/** Añade el primer punto de la envolvente sobre offset 0,0
	 * @param p	Punto a añadir
	 */
	public void moveTo( Point2D p ) {
		miPath.moveTo(p.getX(), p.getY());
	}
	
	/** Añade el siguiente punto de la envolvente sobre offset 0,0
	 * @param p	Punto a añadir
	 */
	public void lineTo( Point2D p ) {
		if (miPath.getCurrentPoint()==null)
			moveTo( p );
		else
			miPath.lineTo(p.getX(), p.getY());
	}
	
	/** Cierra la envolvente
	 * Hacerlo después de añadir el último punto
	 */
	public void closePath() {
		miPath.closePath();
	}
	
	/** Chequea si la envolvente tiene puntos o no
	 * @return	true si está vacía, false en caso contrario
	 */
	public boolean isEmpty() {
		return (getPuntosDe( false ).length==0);
	}
	
	@Override
	public String toString() {
		Collection<Point2D> puntos = Arrays.asList( getPuntosDe(false) );
		String st = "";
		for (Point2D p : puntos) {
			st += "(" + p.getX() + "," + p.getY() + ")";
		}
		return st;
	}
	
	// MAIN DE PRUEBA
	public static void main( String[] s) {
		Envolvente e = new Envolvente();
		e.moveTo( new Point2D.Double( 220, 10 ) );
		e.lineTo( new Point2D.Double( 260, 40 ) );
		e.lineTo( new Point2D.Double( 240, 70 ) );
		e.lineTo( new Point2D.Double( 210, 35 ) );
		e.closePath();
		Envolvente e2 = new Envolvente();
		e2.moveTo( new Point2D.Double( 240, 110 ) );
		e2.lineTo( new Point2D.Double( 300, 140 ) );
		e2.lineTo( new Point2D.Double( 340, 170 ) );
		e2.lineTo( new Point2D.Double( 225, 135 ) );
		e2.closePath();
		Envolvente e3 = new Envolvente();
		e3.moveTo( new Point2D.Double( 320, 10 ) );
		e3.lineTo( new Point2D.Double( 360, 40 ) );
		e3.lineTo( new Point2D.Double( 340, 70 ) );
		e3.lineTo( new Point2D.Double( 250, 35 ) );
		e3.closePath();
		if (e.intersects(e2)) System.out.println( "CHOCAN!!!");
		if (e.intersects(e3)) System.out.println( "CHOCAN!!!");
		ArrayList<String> fics = new ArrayList<String>();
		fics.add( "resources\\bicho.png" );
		VentanaEdicionEnvolvente v = new VentanaEdicionEnvolvente( fics, fics );
		v.setVisible( true );
		v.getPanelEdicion().addEnvolvente( e );
		v.getPanelEdicion().addEnvolvente( e2 );
		v.getPanelEdicion().addEnvolvente( e3 );
	}
}
