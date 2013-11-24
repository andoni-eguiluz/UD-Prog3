package tema8;

class Contador {
    private int c = 0;
    public void inc() {  
    	// Haciendo c++ podría pasar pero se ve más claro en pasos:
    	int d = c;
    	d++;
        c = d;
    }

    public void dec() {
    	// Haciendo c-- podría pasar pero se ve más claro en pasos:
    	int d = c;
    	d--;
        c = d;
    }
    public int getContador() {
        return c;
    }
}

public class EjemploProblemasSincronizacion implements Runnable {
	private static Contador miContador = new Contador();
	@Override
	public void run() {
		for (int i = 0; i<100000000; i++) {
			miContador.inc();
			miContador.dec();
		}
	}
	public static void main( String[] s ) {
		 Thread t1 = new Thread( new EjemploProblemasSincronizacion() );
		 Thread t2 = new Thread( new EjemploProblemasSincronizacion() );
		 Thread t3 = new Thread( new EjemploProblemasSincronizacion() );
		 System.out.println( "Contador = " + miContador.getContador() );  // Contador = 0
		 long tiempo = System.currentTimeMillis();
		 t1.start();
		 t2.start();
		 t3.start();
		 try {
			 t1.join();
			 t2.join();
			 t3.join();
		 } catch (InterruptedException e) {
		 }
		 System.out.println( "Contador = " + miContador.getContador() );  // Debería ser cero pero... 
		 System.out.println( "Tiempo transcurrido: " + (System.currentTimeMillis() - tiempo) + " msgs." );
	}
}
