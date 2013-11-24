package tema1;
// PRUEBA métodos de clase y de instancia
// atributos de clase y de instancia

public class Prueba {
	int elDatoQueTengoEnEstaClaseDePrueba = 5;
	public int getElDatoQueTengoEnEstaClaseDePrueba() {
		return elDatoQueTengoEnEstaClaseDePrueba;
	}
	public void setElDatoQueTengoEnEstaClaseDePrueba(
			int elDatoQueTengoEnEstaClaseDePrueba) {
		this.elDatoQueTengoEnEstaClaseDePrueba = elDatoQueTengoEnEstaClaseDePrueba;
	}
	public static int getMiCuenta() {
		return miCuenta;
	}
	public static void setMiCuenta(int miCuenta) {
		Prueba.miCuenta = miCuenta;
	}
	// contador de objetos que creamos
	static int miCuenta = 0;
	// contador de llamadas a métodos
	static int misLlamadas = 0;
	public Prueba() {
		elDatoQueTengoEnEstaClaseDePrueba = 3;
		miCuenta++;
	}
	void incrementar() {
		elDatoQueTengoEnEstaClaseDePrueba++;
		misLlamadas++;
	}
	static void saludar() {
		System.out.print( "Hola!  " );
		System.out.println( "Mi contador es:" + miCuenta );
	}
	public static void main( String s[] ) {
		Prueba.saludar();
		Prueba p = new Prueba();
		p.incrementar();		
		Prueba.saludar();
		System.out.println( Prueba.miCuenta );
		
		// Conversión implícita/explícita
		double f = 5F / 2;   /* (float) 5 / 2; */  /* 5F/2 */
		System.out.println( f );
	}
}
