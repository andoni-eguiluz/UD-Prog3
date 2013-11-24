package tema1;

public class PruebaPosible {
	int vel = 0;
	static int i = 5;

	public int getVelocidad() {
		return vel;
	}
	
	public void incrementar( int kmh ) {
		vel += kmh;
	}
	
	private static void inicializar() {
		
	}
	
	public static void main (String st[]) {
		PruebaPosible.inicializar();
		double r = (double) i / 2;
		System.out.println( r );
		PruebaPosible pr = new PruebaPosible();
		System.out.println( pr.getVelocidad() );		
		pr.incrementar( 10 );
		System.out.println( pr.getVelocidad() );
		double s = r;
		s = s * 2;
		System.out.println( "s = " + s + " / r = " + r );
		PruebaPosible pr2 = new PruebaPosible();
		pr2.incrementar( 10 );
		if (pr == pr2) System.out.println( "Son iguales" );
		System.out.println( "Vel. pr = " + pr.getVelocidad() );
		System.out.println( "Vel. pr2 = " + pr2.getVelocidad() );
	}
	
}
