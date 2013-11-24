package utils.verbos;

import java.util.Comparator;

public class VerboIrregularConjugado implements Comparable {
	private String inf;
	private boolean refl;
	private String mod;
	VerboIrregularConjugado( String infinitivo, boolean reflexivo, String modelo ) {
		inf = infinitivo; refl = reflexivo; mod = modelo;
	}
	public int compareTo(Object arg0) {
		if (arg0 == null) return 1;
		if (!(arg0 instanceof VerboIrregularConjugado || arg0 instanceof String)) return 1;
		if (arg0 instanceof String) return inf.compareTo( (String) arg0 );
		return inf.compareTo( ((VerboIrregularConjugado)arg0).inf );
	}
	public boolean equals( Object obj ) {
		if (obj == null || !(obj instanceof VerboIrregularConjugado )) return false;
		return inf.equals( ((VerboIrregularConjugado)obj).inf );
	}
	public String getInfinitivo() { return inf; }
	public boolean getReflexivo() { return refl; }
	public String getModelo() { return mod; }
	public String toString() { return inf; }
}

