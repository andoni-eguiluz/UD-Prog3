package tema8;
// Modo 1: Heredando de thread

class ThreadHeredando extends Thread {

	private char miCaracter;

	public ThreadHeredando( char c ) {
		miCaracter = c;
	}
	
	public void run() { 
		// for( int i=0; i<100; i++ ) 
		while (true)
			System.out.print( miCaracter ); 
	}

	public static void main( String[] pars ) { 
		Thread t1 = new ThreadHeredando( '1' ); 
		Thread t2 = new ThreadHeredando( '2' ); 
		t1.start(); // Ejecuta t1.run() 
		t2.start(); // Ejecuta t2.run() 
		System.out.println("Ahora están los 2 en marcha"); 
		System.out.println(); 
		System.out.println( "Final del main" ); 
	}
	
}
