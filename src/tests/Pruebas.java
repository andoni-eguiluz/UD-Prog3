package tests;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Pruebas {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ObjectInputStream map = 
				new ObjectInputStream(
						new FileInputStream("MAPA.DAT")
						); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
