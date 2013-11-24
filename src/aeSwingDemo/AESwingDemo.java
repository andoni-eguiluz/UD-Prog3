package aeSwingDemo;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JFrame;

import aeSwingDemo.windows.AESwingDemoPlayer;

public class AESwingDemo {
	public String getPathFuentes() {
		return pathFuentes;
	}
	public ArrayList<ElementoDemo> getElementos() {
		return elementos;
	}
	private String pathFuentes;  // Carpeta inicio de .java (src por defecto)
	private ArrayList<ElementoDemo> elementos = new ArrayList<ElementoDemo>();
	public AESwingDemo( String pathFuentes ) throws IOException {
		File fFuentes = new File(pathFuentes);
		if (!fFuentes.isDirectory()) throw new IOException( "AESwingDemo(): Error en path de inicio" );
		this.pathFuentes = pathFuentes;
	}
	public AESwingDemo() throws IOException {
		this( "src" );
	}
	public void add( ElementoDemo ed ) {
		if (ed != null) elementos.add(ed);
	}
	public static void main (String[] s) throws Exception {
		AESwingDemo aesd = new AESwingDemo();  // Path src
		ElementoDemo el = new ElementoDemo(aesd, "aeSwingDemo.component.SpinnerDemo" );
		el.addOtraClase( "aeSwingDemo.component.CyclingSpinnerListModel" );
		el.addOtraClase( "aeSwingDemo.component.SpringUtilities" );
		AESwingDemoPlayer v = new AESwingDemoPlayer();
		v.setVisible( true );
		v.setFuenteToFile( el.getListaFilesClases().get(0) );
		// Contenido c1 = new Contenido( "prueba", new ReferenciaACodigo( el, 0, 2099, 2233 ));
		Contenido c1 = new Contenido( "prueba", new ReferenciaACodigo( el, 0, 30, 50 ));
		el.addContenido( c1 );
		v.setFuenteSel( c1.getRefCodigo() );
		JFrame f = el.activarVentanaPrincipal();
		f.setSize( 400, 300 );
		f.setLocation( new Point( 500, 300 ));
		f.setVisible( true );
	}
}
