package tema4;

class FueraDeListaException extends IndexOutOfBoundsException
{
	FueraDeListaException() {}
	FueraDeListaException(String s)
	{
		super( s ); // Llama al constructor del padre
	}
}

public class ListaEnlazada
{
	NodoLista l;
	public void insertarPrimero(Object e) 
	{
		NodoLista nuevoNodo = new NodoLista(e, null);
		if (l==null)
			l = nuevoNodo;
		else {		
			nuevoNodo.siguiente=l;
			l = nuevoNodo;
		}
	}

	public void insertar( Object o, int posicion ) throws FueraDeListaException
	{
		NodoLista aux = l;
		for (int i = 1; aux != null && i < posicion-1; i++ )
		{
			aux = aux.siguiente;
		}
		if (posicion == 1)
			l = new NodoLista( o, l );
			else
			{
				if (aux == null)
					throw new FueraDeListaException("intentando insertar elemento más allá del fin de la lista" );
				else
					aux.siguiente = new NodoLista( o, aux.siguiente );
			}
	}

	public void borrarPrimero() throws FueraDeListaException
	{
		if (l == null)
			throw new FueraDeListaException("intento de borrar primero en lista vacía" );
		else
			l = l.siguiente;
	}

	public String toString()
	{
		String str = "( ";
		NodoLista aux =l;
		while (aux != null){
			str += aux.elemento.toString() + " ";
			aux = aux.siguiente;
		}
		str += " )";
		return str;
	}

	public void destruir(){
		l=null;
	}	

	public static void main( String[] a )
	{
		ListaEnlazada l = new ListaEnlazada();
		try {
			l.borrarPrimero();
		} catch (FueraDeListaException e) {
			System.out.println( "Error: " + e.getMessage() + ". Continuamos..." );
		}
		l.insertarPrimero( new Integer( 1 ) );
		l.insertarPrimero( new Integer( 2 ) );
		l.insertar( new Integer( 4 ), 1 );
		l.insertar( new Integer( 3 ), 2 );
		// no obligatorio recoger el error
		// pq es RuntimeException
		l.insertar( new Double( 2.5 ), 3 );
		try {
			l.insertar( new Integer( 18 ), 10 );
		} catch (FueraDeListaException e) {
			System.out.println( "Error: " + e.getMessage() + ". Continuamos..." );
		} finally {
			l.insertarPrimero( new Integer(18) );
		}
		System.out.println( l.toString());
		l.destruir();
		l.borrarPrimero();
		// de este error no se recupera el programa
		System.out.println( "Aquí no se llega... error en ejecución" );
	}
}

class NodoLista
{
	Object elemento;
	NodoLista siguiente;
	
	NodoLista(){}
	NodoLista(Object o){
		elemento = o;
	}	
	NodoLista(Object d, NodoLista s) {
		elemento = d;
		siguiente = s;
	}
	public void insertarSig(Object o){
		NodoLista nuevoNodo = new NodoLista(o);
		nuevoNodo.siguiente = siguiente;
		siguiente = nuevoNodo;
	}
}
