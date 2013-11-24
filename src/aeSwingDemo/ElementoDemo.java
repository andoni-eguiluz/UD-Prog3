package aeSwingDemo;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;

public class ElementoDemo {
	private AESwingDemo myAESwingDemo;
	private String nombreClasePrincipal;
	private Class clasePrincipal;
	private Method metodoPrincipal;
	private ArrayList<String> listaClases = new ArrayList<String>();
	private ArrayList<File> listaFilesClases = new ArrayList<File>();
	private ArrayList<Contenido> contenidos = new ArrayList<Contenido>();

		private void chequearYGuardarMetodoPrincipal( Class cl ) throws ClassNotFoundException {
			boolean encontrado = false;
			Method[] mets = cl.getMethods();
			for (Method m : mets) {
				if (m.getName().equals( "aeSwingDemo" )) {  // Está el nombre, veamos los pars y val. devuelto
					if (m.getReturnType() != JFrame.class) { break; } // no coincide tipo devuelto: error
					if (m.getParameterTypes().length > 0) { break; } // no coincide sin parámetros
					metodoPrincipal = m;
					encontrado = true;
				}
			}
			if (!encontrado) throw new ClassNotFoundException( "Método ae" );
		}
		
	/** Crea un nuevo elemento
	 * @param myAESwingDemo	No debe ser null (excepción NullPointerException)
	 * @param nombreClase	Nombre existente de la clase (debe estar compilada y en el classpath)
	 * @throws ClassNotFoundException	Si la clase compilada no se encuentra, o no se encuentra el fichero fuente, o no tiene el método estático aeSwingDemo()
	 * @throws NullPointerException	Si myAESwingDemo es null
	 */
	public ElementoDemo( AESwingDemo myAESwingDemo, String nombreClase ) throws ClassNotFoundException {
		Class clasePrincipal = ClassLoader.getSystemClassLoader().loadClass( nombreClase );  // excepción si no existe
		chequearYGuardarMetodoPrincipal( clasePrincipal );
		if (myAESwingDemo == null) throw new NullPointerException( "ElementoDemo: AESwingDemo nulo" );
		this.nombreClasePrincipal = nombreClase;
		this.myAESwingDemo = myAESwingDemo;
		addOtraClase( nombreClase );  // Añadimos la primera clase a la lista
	}
	public String getNombreClase() {
		return nombreClasePrincipal;
	}
	public void addOtraClase( String nomNuevaClase ) throws ClassNotFoundException {
		String nomFic = myAESwingDemo.getPathFuentes();
		nomNuevaClase = nomNuevaClase.replaceAll( "\\.", "/" );
		File f = new File( nomFic, nomNuevaClase + ".java" );
		// System.out.println( f.getAbsolutePath() );
		if (!f.exists() || !f.isFile()) throw new ClassNotFoundException( "AESwingDemo.addOtraClase(): Clase .java no encontrada: " + nomNuevaClase );
		listaClases.add( nomNuevaClase );
		listaFilesClases.add( f );
	}
	/** Elimina una de las clases de este elemento<p>
	 * Quita también todas las referencias internas a esa clase (deja los contenidos, quita las referencias)
	 * @param nomClaseAEliminar
	 */
	public void removeClase( String nomClaseAEliminar ) {
		int pos = listaClases.indexOf( nomClaseAEliminar );
		if (pos >= 0) {
			listaClases.remove( pos );
			listaFilesClases.remove( pos );
			for (Contenido c : contenidos) {
				ReferenciaACodigo r = c.getRefCodigo();
				if (r.getNomClase().equals(nomClaseAEliminar)) c.setRefCodigo( null );
			}
		}
	}
	public void addContenido( Contenido c ) {
		getContenidos().add( c );
	}
	public ArrayList<Contenido> getContenidos() {
		return contenidos;
	}
	public ArrayList<String> getListaClases() {
		return listaClases;
	}
	public ArrayList<File> getListaFilesClases() {
		return listaFilesClases;
	}

	public boolean equals( Object o ) {
		return (o instanceof ElementoDemo) && ((ElementoDemo)o).nombreClasePrincipal.equals(nombreClasePrincipal);
	}
	
	public JFrame activarVentanaPrincipal() {
		try {
			return (JFrame) metodoPrincipal.invoke( null );
		} catch (IllegalArgumentException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		}
	}
}
