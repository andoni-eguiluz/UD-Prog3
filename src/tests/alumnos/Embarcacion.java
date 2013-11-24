package tests.alumnos;

import java.io.*;



public class Embarcacion implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -961269971284559830L;
	/**
	 * 
	 */
	public String matricula;
	/**
	 * nombre del barco
	 */
	public String nombre;
	/**
	 * DNI del propietario
	 */
	public int DNIPropietario;
	/**
	 * Metros que tiene el barco de longitud
	 */
	public double eslora;
	
	/**
	 * Metros que tiene el barco de manga
	 */
	public double manga;
	
	
	
	/**
	 * constructor por defecto
	 * @param matricula del barco
	 * @param nombre del barco
	 * @param propietario del barco
	 * @param eslora del barco
	 * @param manga del barco
	 */
	public Embarcacion(String matricula,String nombre,int DNIPropietario,double eslora,double manga)	{
		this.matricula=matricula;
		this.nombre=nombre;
		this.DNIPropietario=DNIPropietario;
		this.eslora=eslora;
		this.manga=manga;
		}
	public Embarcacion(){
		this.matricula = "";
		this.nombre = "";
		this.DNIPropietario = 0;
		this.eslora = 0;
		this.manga=0;
	}
	
	/**
	 * @return the manga
	 */
	
	public double getManga() {
		return manga;
	}

	/**
	 * @param manga the manga to set
	 */
	public void setManga(double manga) {
		this.manga = manga;
	}
	
	/**
	 * @return the matricula
	 */
	public String getMatricula() {
		return matricula;
	}

	/**
	 * @param matricula the matricula to set
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	/**
	 * @return the nombre
	 */
	public int getDNIPropietario() {
		return DNIPropietario;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setDNIPropietario(int DNIPropietario) {
		this.DNIPropietario = DNIPropietario;
	}

	/**
	 * @return the propietario
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param propietario the propietario to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the eslora
	 */
	public double getEslora() {
		return eslora;
	}

	/**
	 * @param eslora the eslora to set
	 */
	public void setEslora(double eslora) {
		this.eslora = eslora;
	}




	

	/**
	 * visualiza los atributos de un objeto barco
	 */
	public void mostrarE()
	{
		
		System.out.println("Matricula de la embarcacion: " + this.matricula);
		System.out.println("Nombre: "+ nombre);
		System.out.println("Propietario: " + DNIPropietario);
		System.out.println("Eslora: "+ eslora);
	}
	
}
