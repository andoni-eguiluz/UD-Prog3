package aeSwingDemo;

public class Contenido {
	private String texto = "";
	private ReferenciaACodigo refCodigo = null;
	public Contenido(String texto, ReferenciaACodigo refCodigo) {
		super();
		this.texto = texto;
		this.refCodigo = refCodigo;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public ReferenciaACodigo getRefCodigo() {
		return refCodigo;
	}
	public void setRefCodigo(ReferenciaACodigo refCodigo) {
		this.refCodigo = refCodigo;
	}
}
