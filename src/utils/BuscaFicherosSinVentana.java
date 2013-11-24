package utils;

import java.io.File;
import java.util.regex.Pattern;

public class BuscaFicherosSinVentana {
	public interface Operador {
		void ficheroEncontrado( File f );
		void finBusqueda( boolean haCanceladoElUsuario );
	}
	
	private static final long serialVersionUID = -1245237602454798003L;
	private boolean seguirBusqueda = true;
	private Operador miOperador = null;
	private Pattern miPatron = null;
	private int miFrecMensajes = 0;
	private long tiempoUltimoMens; 
	private File inicioBusqueda;
	private Thread miThread = null;
	private Runnable miHiloDeBusqueda = new Runnable() {  // Proceso a hacer en el hilo de búsqueda
		@Override
		public void run() {
			tiempoUltimoMens = System.currentTimeMillis();
			recorreDisco( inicioBusqueda, 0, 1 );
    		if (miOperador != null) miOperador.finBusqueda( !seguirBusqueda );
		}
	};

		private void recorreDisco( File fuente, double porcProgresoInicio, double porcProgresoFin ) {
	        if (seguirBusqueda) { // Corta cuando se pulsa el botón de cancelar
	        	if (fuente.isDirectory()) {
		        	// System.out.println( fuente.getAbsolutePath() + "\\" + fuente.getName() );
		        	// numDirs++;
		        	String cont[] = fuente.list();
		        	if (cont != null) {
		        		double incrPorIteracion = (porcProgresoFin-porcProgresoInicio)/cont.length;
		        		int paso = 0;
			        	for (String fic : cont) {
			        		File f = new File( fuente.getAbsolutePath() + "\\" + fic );
			        		// soyYo.actualizarProgreso( porcProgresoInicio + paso*incrPorIteracion );
			    			if (miFrecMensajes>0 && (System.currentTimeMillis() - tiempoUltimoMens) > miFrecMensajes*1000 )
			    				sacarMensaje( "Procesando carpeta " + f.getAbsolutePath() + f.getName() + "..." );
			        		recorreDisco( f, porcProgresoInicio + paso*incrPorIteracion, 
			        				porcProgresoInicio + (paso+1)*incrPorIteracion );
			        		paso++;
			        	}
		        	}
		        } else {
		        	// numFics++;
		        	if (miPatron!=null && miPatron.matcher(fuente.getName()).matches()) {
		        		//System.out.println( fuente.getName() );
		        		if (miOperador != null) miOperador.ficheroEncontrado( fuente );
		        	}
		        }
	        }
		}
		
		private void sacarMensaje( String mens ) {
			tiempoUltimoMens = System.currentTimeMillis();
			System.out.println( mens );
		}
		
	
	/** Crea un objeto no visual que busca ficheros a partir del disco o directorio
	 * indicados, de forma recursiva en todas las carpetas.
	 * @param carpetaInicio	Inicio de la búsqueda, debe ser una carpeta válida (por ejemplo "C:\\") 
	 * @param patron	Patrón de los ficheros que se buscan.<br>  
	 * 			Por ejemplo ".*\\.(jpg|jpeg|gif|png|bmp)|.*\\.doc"
	 * @param operador	Operador de acción. Sobre cada fichero encontrado se llama a su método ficheroEncontrado( File f ).
	 * @param empiezaYa	Si true empieza ya, si false requiere llamar al método iniciaBusqueda().
	 * @param sacaMensajes	Número de segundos. Si es un valor mayor que cero, saca mensaje de directorio en proceso cada esos segundos
	 */
	public BuscaFicherosSinVentana( String carpetaInicio, Pattern patron, BuscaFicherosSinVentana.Operador operador, boolean empiezaYa, int sacaMensajes ) {
		inicioBusqueda = new File( carpetaInicio );
		miOperador = operador;
		miPatron = patron;
		miFrecMensajes = sacaMensajes;
		if (empiezaYa) iniciaBusqueda();
	}

	public void iniciaBusqueda() {
		if (!inicioBusqueda.exists() || !inicioBusqueda.isDirectory()) return;
		miThread = new Thread( miHiloDeBusqueda );
		miThread.start();
	}
	
	public void esperaAQueAcabeBusqueda() {
		try {
			if (miThread!=null) miThread.join();
		} catch (InterruptedException e) {
		}
	}
	
	/** Método main de prueba
	 * @param s	No utilizado
	 */
	public static void main( String[] s ) {
		BuscaFicherosSinVentana v = new BuscaFicherosSinVentana( "d:\\t\\", 
				Pattern.compile(".*\\.(jpg|jpeg|gif|png|bmp)|.*\\.doc"), miOp, 
				false, 5 );
		v.iniciaBusqueda();
	}
	
	private static Operador miOp = new Operador() {
		@Override
		public void ficheroEncontrado( File f ) {
			System.out.println( f.getAbsolutePath() );
		}
		public void finBusqueda( boolean cancel ) {
			if (cancel)
				System.out.println( "CANCELADO" );
			else
				System.out.println( "SE ACABO" );
		}
	};
	
}


