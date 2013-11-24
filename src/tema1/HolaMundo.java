package tema1;

import java.util.Vector;

public class HolaMundo {
	public static void main( String s[] ) {
		System.out.println( Planeta.getNumPlanetas() );
		Planeta p = new Planeta( "Tierra", 6371000, 2870972200000L );
		Planeta p2 = new Planeta( "Urano", 25659000, 34234234234234234L );
		System.out.println( "Hola " + p + "!" );
		System.out.println( "Hola " + p2 + "!" );
		Satelite l = new Satelite( "Luna", 23423, p );
		DeathStar ds = new DeathStar( "estrella de la muerte 1", 342434, 99999999 );
		System.out.println( "Hola " + l + "!" );
		System.out.println( Planeta.getNumPlanetas() );
		System.out.println( ObjetoEstelar.getNumObjetosCreados() );
		Vector<ObjetoEstelar> miGrupo = new Vector<ObjetoEstelar>();
		miGrupo.add( p );
		miGrupo.add( p2 );
		miGrupo.add( l );
		miGrupo.add( ds );
		for (int indArrayOE=0; indArrayOE<miGrupo.size(); indArrayOE++) {
			System.out.print( miGrupo.get( indArrayOE ) );
			System.out.println( "  " + miGrupo.get( indArrayOE ).calculaOrbita() );
			if (miGrupo.get(indArrayOE) instanceof Satelite) {
				java.lang.System.out.println( "  soy del planeta " + ((Satelite)(miGrupo.get(indArrayOE))).getPlanetaDelQueOrbito() );
			}
			ObjetoEstelar o = miGrupo.get( indArrayOE );
			if (o instanceof Habitable) {
				System.out.println(  ((Habitable) o).getNumHabitantes() );
			}
		}
	}
}
