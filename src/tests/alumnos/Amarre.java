package tests.alumnos;

import java.io.*;


public class Amarre implements Serializable, DatoParaTabla {


	public static String[] nombresAtributos = new String[] {
		"Pantalan","Plaza","Situacion", "Matricula","Nombre","DNI","Eslora","Manga","Precio","Pago"  };
	
	public static boolean[] atributosEditables = new boolean[] {
		false,false,true,true,true,true,true,true,true,true};
	

	
    public int getNumColumnas()
    
    {
    	return 10;
    }
    
    
    public Object getValor(int col) {
    	switch (col) {
	    	case 0: { return pantalan; }
	    	case 1: { return plaza; }
	    	case 2: { return alquilado; }
	    	case 3: { return embarcacion.matricula; }
	    	case 4: { return embarcacion.nombre; }
	    	case 5: { return embarcacion.DNIPropietario; }
	    	case 6: { return embarcacion.eslora; }
	    	case 7: { return embarcacion.manga; }
	    	case 8: { return precio; }
	    	case 9: { return pagado; }
	    	
    	}
    	return null;
    }
    
    
    public void setValor(Object value, int col) {
    	try {
	    	switch (col) {
	    	
	    	
	    	
		    	case 0: { pantalan = (Character) value; break; }
		    	case 1: { plaza = (String) value; break; }
		    	case 2: { alquilado = (Boolean) value; break; }
		    	case 3: { embarcacion.matricula = (String) value; break; }
		    	case 4: { embarcacion.nombre = (String) value; break; }
		    	case 5: { embarcacion.DNIPropietario = (Integer) value; break; }
		    	case 6: { embarcacion.eslora = (Double) value; break; }
		    	case 7: { embarcacion.manga = (Double) value; break; }
		    	case 8: { precio = (Double) value; break; }
		    	case 9: { pagado = (Boolean) value; break; }
		    	
		    	
		    	
		    
	    	}
	    	
    	} catch (Exception e) {
    		// Error en conversion. Intentando asignar un tipo incorrecto
    		e.printStackTrace();
    	}
    }

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * numero de plaza del pantalan: : 1 a 10, hay 10 en cada pantalan
	 */
	private String plaza="";
	/**
	 * precio base de la plaza
	 */
	
	/**
	 * pantalan en el que esta: A,B,C,D (4 Posibles)
	 */
	private char pantalan=' ';
	/**
	 * barco que la ocupa
	 */
	private boolean pagado=false;
	
	private Embarcacion embarcacion=null;
	
	private double precio=0;
	
	
	/**
	 * situacion del amarre
	 */
	private boolean alquilado=false;
	
	

	/**
	 * constructor por defecto
	 * @param plaza del amarre
	 * @param precio del amarre
	 * @param pantalan del amarre
	 * @param estado del amarre
	 * @param embarcacion situada en el amarre
	 * @param pagado o no pagado
	 */
	public Amarre(String plaza, double precio, char pantalan,boolean alquilado,boolean pagado, Embarcacion embarcacion){
		this.plaza=plaza;
		this.precio=embarcacion.manga*embarcacion.eslora;
		this.pantalan=pantalan;
		this.alquilado=alquilado;
		this.pagado=pagado;
		this.embarcacion=embarcacion;	
	}
	public Amarre(){
		this.plaza  = "";
		this.precio = 0;
		this.pantalan =' ';
		this.alquilado = false;
		this.pagado=false;
		this.embarcacion = new Embarcacion();
	}
	
	/**
	 * @return the plaza
	 */
	public String getPlaza() {
		return plaza;
	}


	/**
	 * @param plaza the plaza to set
	 */
	public void setPlaza(String plaza) {
		this.plaza = plaza;
	}


	/**
	 * @return the precio
	 */
	public double getPrecio() {
		return precio;
	}

	/**
	 * @param precio the precio to set
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}


	/**
	 * @return the pantalan
	 */
	public char getPantalan() {
		return pantalan;
	}


	/**
	 * @param pantalan the pantalan to set
	 */
	public void setPantalan(char pantalan) {
		this.pantalan = pantalan;
	}
	/**
	 * @return the alquilado
	 */
	
	public boolean isAlquilado() {
		return alquilado;
	}

	/**
	 * @param alquilado the alquilado to set
	 */
	public void setAlquilado(boolean alquilado) {
		this.alquilado = alquilado;
	}
	
	/**
	 * @return the embarcacion
	 */
	public Embarcacion getEmbarcacion() {
		return embarcacion;
	}

	/**
	 * @param embarcacion the embarcacion to set
	 */
	public void setEmbarcacion(Embarcacion embarcacion) {
		this.embarcacion = embarcacion;
	}

	
	/**
	 *
	 * @return pagado o no
	 */
	public boolean isPagado() {
		return pagado;
	}
	
	
	/**
	 * @param to set pagado
	 */
	public void setPagado(boolean pagado) {
		this.pagado = pagado;
	}

	
	
	
		
	/**
	 * visualiza en consola los atributos de un objeto amarre
	 */
	
	public void mostrarenConsola()
	{
		
		
		
			
			
		System.out.println("Plaza no: " + plaza);
	
		
		
	
		
		System.out.println("Pantalan: " +pantalan);
		System.out.println("Disponibilidad:");
		
		if(alquilado ==true)
		{
			
        System.out.println("Ocupado");
        
		embarcacion.mostrarE();
		
		System.out.println("Precio base diario del amarre: " + precio);
		
		
		if (pagado==false)
			System.out.println("NO PAGADO");
		else 
			System.out.println("PAGADO");
		}
		else
		System.out.println("Disponible");
		
	}
	
	/**
	 * actualiza el fichero de amarres
	 */
	
	
	public static void grabarFicheroActualizado (Amarre[][]aAmarres) throws Exception
	{
		
		FileOutputStream fFAmarres= new FileOutputStream("AMARRES.DAT");
		ObjectOutputStream FAmarres= new ObjectOutputStream(fFAmarres);
		
		
		FAmarres.writeObject( aAmarres );

		FAmarres.close();
	
	}
	
	
	
	

	
	/**
	 * Se encarga de crear ya el fichero de amarres si es que no se ha creado previamente
	 */
	
	public static void procesarExcepcion()  {
		try{	
		
			FileOutputStream fFAmarres= new FileOutputStream("AMARRES.DAT");
			ObjectOutputStream fAmarres= new ObjectOutputStream(fFAmarres);
			Amarre [][] a = new Amarre[4][10];
			cargarAmarrePorDefecto();
			
			fAmarres.writeObject(a);
			fAmarres.writeObject(null);
			fAmarres.close();
	
		}
		catch(FileNotFoundException e){		
			e.printStackTrace();
		}
	
	catch(IOException e2){
		e2.printStackTrace();
	}
}


	private static void cargarAmarrePorDefecto() {
		for (int i = 0; i<=3;i++){
			for (int j = 0; j<=9;j++){
				
			char p=' ';
				switch (i)
				{
				case 0:
					p='A';
					break;
				case 1:
					p='B';
					break;
				case 2:
					p='C';
					break;
				case 3:
					p='D';
					break;
				
				}
				String plaza="" + p+j + "";
				double precio=0;
				char Pant=p;
				boolean alquilado=false;
				boolean pagado=false;
			Embarcacion e=new Embarcacion();
				Amarre temp = new Amarre(plaza,precio,Pant,alquilado,pagado,e);
				JTableAmarres.aAmarres [i][j] = temp;
			}
		}

	}

	
	public static void nuevoAmarrista(Amarre[][]aAmarres) throws Exception
	{
		System.out.println("Introduce el pantalan en el que debe ir introducida la embarcacion");
		
		char p= Utilidades.leerCaracter();
		
		int i =0;
		
		if (p=='A')
			i=0;
		else if (p=='B')
			i=1;
		else if (p=='C')
			i=2;
		else if (p=='D')
			i=3;
		
		boolean enc = false;
		
		int j=0; 
		
		 while (j<aAmarres[i].length && enc ==false)
		 {
			 if (aAmarres[i][j]==null||aAmarres[i][j].isAlquilado()==false)
			 {
				enc=true;
				
			 }
			 else 
				 j++;
				 
		 }
		
		if (enc==true){
			System.out.println("Introduce los datos de la embarcacion: ");
			System.out.println("Introduce la matricula: ");
			String mat= Utilidades.leerCadena();
			System.out.println("Introduce el nombre: ");
			String nm=Utilidades.leerCadena();
			System.out.println("Introduce el DNI del propietario: ");
			int dni= Utilidades.leerEntero();
			System.out.println("Introduce la eslora: ");
			double esl= Utilidades.leerReal();
			
			Embarcacion e = new Embarcacion(mat,nm,dni,esl,0);
			Amarre a=new Amarre("",2,' ',false,false,e);
			
			aAmarres[i][j]=a;	
		}
			else{	
				System.out.println("No hay sitio para su embarcacion, por favor, vuelva a intentarlo mas adelante");
			}


	}
}
