package tests.alumnos;

import java.io.*;




public class Carga {


	/**
	 * metodo encargado de realizar la carga de los amarres en el array bidimensional
	 */
	public static void cargaAmarres() throws Exception
	{
		
		
		try {

		FileInputStream fFAmarres= new FileInputStream("AMARRES.DAT");
		ObjectInputStream FAmarres= new ObjectInputStream(fFAmarres);
		
		JTableAmarres.aAmarres = (Amarre[][])FAmarres.readObject();
		
		FAmarres.close();
		
		} catch (FileNotFoundException e) {
			Amarre.procesarExcepcion();
		}catch (IOException e) {
			Amarre.procesarExcepcion();
		}	catch(ClassNotFoundException e)
		
		{
			
		}
		
	}	
	
	public static void main (String[]args) throws Exception{
		
		Amarre [][] aAmarres = new Amarre [4][10];
	

		
       cargaAmarres();
		
		
			
		
			
				Amarre.nuevoAmarrista(aAmarres);
				Amarre.grabarFicheroActualizado(aAmarres);
			
				
		
		
	}
	
		
}
