package tests;

public class PruebaTonta {

	private static boolean esPrimo( int num ) {
		boolean esPrimo = true;
		for (int divisor=2; divisor<num/2; divisor++) {
			if (num % divisor == 0) {
				esPrimo = false;
				break;
			}
		}
		return esPrimo;
	}
	
	static public void main( String s[] ) {
		int[] numeros = new int[1000000];
		long tiempo = System.currentTimeMillis();
		for( int i=0; i<1000000; i++ ) {
			if (esPrimo(i))
				numeros[i] = i;
			else 
				numeros[i] = 0;
		}
		for( int i=0; i<1000000; i++ ) {
			if (numeros[i] != 0)
				System.out.println( numeros[i] + "  " );
		}
		System.out.println( System.currentTimeMillis() - tiempo );
	}
}
