package tests;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

public interface PruebaInterfaz {
	void importarFichero(LinkedList<Clientes> a) throws Exception;
	void CargarFichero(LinkedList<Clientes> a) throws Exception;
}


class Clientes implements Serializable, PruebaInterfaz
{


	private static final long serialVersionUID = -6144826749883490794L;

	
	private int dni;
	private String nombre;
	private String apellido;
	private int telefono;
	private int edad;  

	private boolean descuento;

	
	public Clientes(){}



public Clientes(int dni, String nombre, String apellido, int telefono,
			int edad) {
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.edad = edad;
		
		this.descuento = false;
	}



public void importarFichero(LinkedList<Clientes> a) throws Exception
{
FileInputStream fis = new FileInputStream("USER.DAT");
ObjectInputStream ffis = new ObjectInputStream(fis); 
a.clear();
boolean si =false;
while(si == false)

{
	try{
   
Clientes b =((Clientes) ffis.readObject());
a.add(b);
    
} catch (Exception e) {
        
        System.out.println("La lista se cargó correctamente");
        si=true;
    }
}
ffis.close();
}
public void CargarFichero(LinkedList<Clientes> a) throws Exception
{
FileOutputStream fis = new FileOutputStream("USER.DAT");
ObjectOutputStream ffis = new ObjectOutputStream(fis); 
int i=0;
boolean seguir = false;
try{
    while(seguir == false)

    {
Clientes b =((Clientes) a.get(i));
i++;
ffis.writeObject(b);
    }
} catch (Exception e) {

    	System.out.println("El fichero se cargó correctamente");
        seguir = true;
    }
ffis.close();
}


public String toString() {
return " dni = " + dni + "\n nombre = " + nombre + "\n apellido = "
+ apellido + "\n telefono = " + telefono + "\n edad = " + edad;
}

public static void main (String[] args) throws Exception 
{
LinkedList<Clientes> a = new LinkedList<Clientes>();
Clientes b = new Clientes(78959066,"Sergio","Santamaria",697606775,18);
a.add(b);
b.CargarFichero(a);
b.importarFichero(a);
System.out.println(a.get(0));
}

}



class Empleados implements PruebaInterfaz, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -918974718894291109L;
	public Empleados(int dni, String nombre, String apellido, int telefono,
			int edad) {
		super();
		
	}

	
	
	public void importarFichero(LinkedList<Clientes> a) throws Exception 
	{
	FileInputStream fis = new FileInputStream("empleado.DAT");
	ObjectInputStream ffis = new ObjectInputStream(fis); 
	a.clear();
	boolean si =false;
	while(si == false)

	{
		try{
	   
	Clientes b =((Clientes) ffis.readObject());
	a.add(b);
	    
	} catch (Exception e) {
	        
	        System.out.println("La lista se cargó correctamente");
	        si=true;
	    }
	}
	ffis.close();
	}
	public void CargarFichero(LinkedList<Clientes> a) throws Exception
	{
	FileOutputStream fis = new FileOutputStream("empleado.DAT");
	ObjectOutputStream ffis = new ObjectOutputStream(fis); 
	int i=0;
	boolean seguir = false;
	try{
	    while(seguir == false)

	    {
	Clientes b =((Clientes) a.get(i));
	i++;
	ffis.writeObject(b);
	    }
	} catch (Exception e) {

	    	System.out.println("El fichero se cargó correctamente");
	        seguir = true;
	    }
	ffis.close();
	}
	
	
	
}

