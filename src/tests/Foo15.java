package tests;


import java.io.*;

public class Foo15 {

	public static void leer() throws Exception {
	    Zatiki z1;
		FileInputStream fis = new FileInputStream("zatikiak.dat");
		ObjectInputStream ois = new ObjectInputStream (fis);
		while (true) {
			z1 = (Zatiki)ois.readObject();
			z1.pantailanErakutsi();
		}
	}
	
	public static void escribir() {
		Zatiki z1;
		z1 = new Zatiki(); //POR QUÉ TENGO QUE CREARLO CADA VEZ??


		try{
			FileOutputStream fos = new FileOutputStream("zatikiak.dat");
			ObjectOutputStream oos = new ObjectOutputStream (fos);
			for (int i=0; i < 5;i++){

				z1.teklatutikEskatu();
				oos.writeObject(z1);
				oos.reset();
				z1.pantailanErakutsi();
			}
			oos.close();
		}catch (Exception e){System.out.println(e.toString());}
	}
	
	public static void main(String[] args) throws Exception {
		escribir();
		leer();
	}
}

class Zatiki implements Serializable {
	//ZATIK: NUMERADOR
	//ZATIT: DENOMINADOR
	private int zatik, zatit;
	
	//KONSTRUKTOREAK - CONSTRUCTORES, CON Y SIN PARÁMETROS
	public Zatiki(){
		zatik = 1;
		zatit = 1;
	}
	public Zatiki(int zk, int zt){
		zatik = zk;
		if (zt != 0) //VALIDAMOS EN TODO MOMENTO QUE EL DENOMINADOR NO TENGA VALOR CERO 
			zatit = zt;
		else
			zatit = 1;
	}
	//GET ETA SET METODOAK
	public int getZatik() {
		return zatik;
	}
	public void setZatik(int zk) {
		zatik = zk;
	}
	public int getZatit() {
		return zatit;
	}
	public void setZatit(int zt) {
		if (zt != 0)
			zatit = zt;
	}
	//S/I METODOAK - MÉTODOS DE ENTRADA/SALIDA
	public void pantailanErakutsi(){//Mostrar en pantalla
		System.out.println(zatik + "/" + zatit);
	}
	public void teklatutikEskatu(){//Leer de teclado
		System.out.print("Idatzi zatikizuna: ");
		zatik = Utilidades.leerEntero();
		do{
			System.out.print("Idatzi zatitzailea (<>0): ");
			zatit = Utilidades.leerEntero();
		}while (zatit == 0);
	}
	//ERAGIKETA MATEMATIKOAK
	public int zkh(){ //Máximo común divisor
		int zkh = Math.min(zatik, zatit);
		while ((zatik%zkh!=0) || (zatit%zkh!=0))
			zkh = zkh-1;
		return zkh;
	}
	public void sinplifikatu(){//Sinplificar la fracción
		int zkh = zkh();
		zatik = zatik / zkh;
		zatit = zatit / zkh;
	}
	public void batuketa(Zatiki z2){// this += z2 - this guarda también el resultado de la operación
		//this = this + z2
		zatik = zatik*z2.zatit + zatit*z2.zatik;
		zatit = zatit * z2.zatit;
		sinplifikatu();
	}
	public Zatiki kenketa(Zatiki z2){ //En este caso se crea una nueva fracción que almacena el resultado
		//emaitza = this + z2
		Zatiki emaitza = new Zatiki(zatik*z2.zatit - zatit*z2.zatik, zatit * z2.zatit);
		emaitza.sinplifikatu();
		return emaitza;
	}
	public Zatiki biderketa(int zk, int zt){//Aquí, de la segunda fracción, se reciben los atributos por separado
		//no es una opción recomendable. El resultado se devuelve en una nueva fracción.
		Zatiki emaitza = new Zatiki(zatik*zk, zatit*zt);
		emaitza.sinplifikatu();
		return emaitza;	
	}
	public void zatiketa(int zk, int zt){//Igual que la anterior, pero es this quien almacena también el 
		//resultado. Tampoco es recomendable.
		if (zk != 0){
			zatik = zatik * zt;
			zatit = zatit * zk;
			sinplifikatu();
		}
	}
}
