package aeSwingDemo;

import java.io.File;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

public class ReferenciaACodigo {
	private ElementoDemo miElementoDemo;
	private String nomClase;
	private File fileClase;
	private int offsetInicio;
	private int offsetFin;
	/**
	 * @param elemento	elemento principal de la referencia, no puede ser nulo
	 * @param numClase	número de clase en aeSwingDemo (0 <= numClase <= n-1)
	 * @param offsetInicio	offset de inicio en esa clase
	 * @param offsetFin	offset de fin en esa clase
	 * @throws ClassNotFoundException	Si el número de clase es incorrecto
	 * @throws NullPointerException	Si elemento es nulo
	 * @throws BadLocationException	Si hay un error en los offsets
	 */
	public ReferenciaACodigo( ElementoDemo elemento, int numClase, int offsetInicio, int offsetFin ) throws ClassNotFoundException, BadLocationException {
		if (numClase < 0  || numClase >= elemento.getListaClases().size())
			throw new ClassNotFoundException( "Referencia incorrecta: clase " + numClase + " no existe" );
		if (offsetInicio > offsetFin) throw new BadLocationException( "Offset incorrecto", offsetFin );
		miElementoDemo = elemento;
		nomClase = elemento.getListaClases().get( numClase );
		fileClase = elemento.getListaFilesClases().get( numClase );
		this.offsetInicio = offsetInicio;
		this.offsetFin = offsetFin;
	}
	public int getOffsetInicio() {
		return offsetInicio;
	}
	public int getOffsetFin() {
		return offsetFin;
	}
	public void setOffsets(int offsetInicio, int offsetFin) {
		this.offsetInicio = offsetInicio;
		this.offsetFin = offsetFin;
	}
	public String getNomClase() {
		return nomClase;
	}
	public File getFileClase() {
		return fileClase;
	}
}
