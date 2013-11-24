package utils.verbos;

import java.net.*;
import java.io.*;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import utils.Debug;

public class ConjugacionesVerbales {
	private static boolean DEBUG_ERROR = true;   // Muestra en consola posibles errores de ficheros
	private static boolean DEBUG_LOAD = false;   // Muestra en consola el contador de verbos tras la carga
	private static boolean DEBUG_VERBOS_EN_FICHERO = false;
	private static int longMaxSufijos = 0;
	private static int longMaxSufijosIrregulares = 0;
	private static TreeSet<VerboModeloConjugacion> conjugadosRegulares = null;
	private static TreeSet<SufijoVerbal> sufijos = initSufijos();
	private static TreeSet<VerboModeloConjugacion> conjugadosIrregulares = null;
	private static TreeSet<SufijoVerbal> sufijosIrregulares = initSufijosIrregulares();
	private static TreeSet<String> verbosRegulares = initVerbosRegulares( DEBUG_VERBOS_EN_FICHERO );
	private static TreeSet<VerboIrregularConjugado> verbosIrregularesConjugados = initVerbosIrregularesConjugados( DEBUG_VERBOS_EN_FICHERO );
	
	private static final float CONFIANZA_EXACTA = 1.0F;
	private static final float CONFIANZA_SIN_TILDES = 0.75F;
	// private static final float CONFIANZA_MODELOMAL = 0.25F;
	
	public static Iterator<String> iteratorVerbosRegulares() {
		return verbosRegulares.iterator();
	}
	
	private static TreeSet<String> initVerbosRegulares( boolean chequarModeloOkEnWeb ) {
		TreeSet<String> vr = new TreeSet<String>();
		String fileName = "verbos\\regulares.dat";
        File regFile = new File(fileName);
    	if (!regFile.exists()) {
    		// Something to advise user
    		if (DEBUG_ERROR) utils.Debug.show( "File not found: verbos/regulares.dat" );
    	} else {
    		int regVCount = 0;
    	    try {
		        BufferedReader input =  new BufferedReader(new FileReader(regFile));
		        String line = null;
		        while ((line = input.readLine()) != null) {
		        	// Ini line process
		        	// System.out.println( line );
		        	line.trim();
					if (line.startsWith("//") || line.trim().equals("")) {   // Comment
					} else {
						if (line.contains(" ")) {  // Line data error (space between verb chars)
						} else {
							if (vr.add( line )) {
								regVCount++;
								if (chequarModeloOkEnWeb)
									ChequeoWeb.comprobarVerbos( line, "", false, 20 );
							}
						}
					}
		        }
		        if (DEBUG_LOAD) utils.Debug.show( "Contador de verbos regulares: " + regVCount );
    	    } 
    	    catch (IOException ex){
    	    	if (DEBUG_ERROR) utils.Debug.show( "file regulares.dat not found or error in file");
    	    }
    	}
		return vr;
	}		

	
	private static TreeSet<VerboIrregularConjugado> initVerbosIrregularesConjugados( boolean chequarModeloOkEnWeb ) {
		TreeSet<VerboIrregularConjugado> vi = new TreeSet<VerboIrregularConjugado>();
		String fileName = "verbos\\irregulares.dat";
        File regFile = new File(fileName);
    	if (!regFile.exists()) {
    		// Something to advise user
    		if (DEBUG_ERROR) utils.Debug.show( "File not found: verbos/irregulares.dat" );
    	} else {
    		int regVCount = 0;
    	    try {
		        BufferedReader input =  new BufferedReader(new FileReader(regFile));
		        String line = null;
		        while ((line = input.readLine()) != null) {
		        	line.trim();
					if (line.startsWith("//") || line.trim().equals("")) {   // Comment
					} else {
						String[] data = line.split( "[,]" );
						if (data.length == 3) {  // Must be 3 in data file <infinitivo>,<reflexivo>,<modelo>
							if (vi.add( new VerboIrregularConjugado( data[0], (data[1].toLowerCase().equals("reflexivo")), data[2]) )) {
								regVCount++;
								if (chequarModeloOkEnWeb)
									ChequeoWeb.comprobarVerbos( data[0], data[2], true, 20 );
							}
						}
					}
		        }
		        if (DEBUG_LOAD) utils.Debug.show( "Contador de verbos irregulares conjugados: " + regVCount );
    	    } 
    	    catch (IOException ex){
    	    	if (DEBUG_ERROR) utils.Debug.show( "file irregulares.dat not found or error in file");
    	    }
    	}
		return vi;
	}		
	
		// M�todo utilitario. A�ade un sufijo a dos lugares: el ordenado por sufijos (aSV) con todos,
		// y al verbomodelo que se a�adir� al �rbol de conjugaciones vMC
		private static void anyadirSufijoADosArboles( TreeSet<SufijoVerbal> aSV, VerboModeloConjugacion vMC, SufijoVerbal sv ) {
			aSV.add( sv );
			vMC.getConjugaciones().add( sv );
		}
		
	private static TreeSet<SufijoVerbal> initSufijosIrregulares() {
		TreeSet<SufijoVerbal> result = new TreeSet<SufijoVerbal>();
		conjugadosIrregulares = new TreeSet<VerboModeloConjugacion>();
		String fileName = "verbos\\irregularesModelos.dat";
        File irrFile = new File(fileName);
    	if (!irrFile.exists()) {
    		// Something to advise user
    		if (DEBUG_ERROR) utils.Debug.show( "File not found: verbos/irregularesModelos.dat" );
    	} else {
    	    try {
		        BufferedReader input =  new BufferedReader(new FileReader(irrFile));
		        String line = null;
		        VerboModeloConjugacion verboIrrEnCurso = new VerboModeloConjugacion( "" );
		        int conjCount = 0; int verbCount = 0;
		        while ((line = input.readLine()) != null) {
		        	// Ini line process
		        	// System.out.println( line );
		        	line.trim();
					if (line.startsWith("//") || line.trim().equals("")) {   // Comment
					} else {
						String[] data = line.split(",");
						if (data.length < 8) {  // Line data error
						} else {
							// infinitivo,terminaci�n,persona,numero(S/P),forma,tiempo,sufijo,conjugaci�n
							// data[2] from String to int
							int i = 0;
							try { Integer ii = new Integer( data[2] ); i = ii.intValue(); } catch (Exception e) { }
							if (!verboIrrEnCurso.getInfinitivo().equals(data[0])) {  // Actualizar verbo en curso
								conjugadosIrregulares.add( verboIrrEnCurso );  // A�adir anterior verbo
								verbCount++;
								verboIrrEnCurso = new VerboModeloConjugacion( data[0] );   // y empezar uno nuevo
							}
							anyadirSufijoADosArboles( result, verboIrrEnCurso, new SufijoVerbal( data[0], data[1], i, (data[3].equalsIgnoreCase("P")),
									data[4], (data[5].equalsIgnoreCase("subjuntivo")), data[6] ) );
							conjCount++;
							// Calc max length of suffix
							int numCars = data[6].length(); 
							if (numCars > longMaxSufijosIrregulares) longMaxSufijosIrregulares = numCars; 
						}
					}
		        }
				conjugadosIrregulares.add( verboIrrEnCurso );  // A�adir el �ltimo verbo irregular
				verbCount++;
				if (DEBUG_LOAD) utils.Debug.show( "Contador de verbos irregulares: " + verbCount );
				if (DEBUG_LOAD) utils.Debug.show( "Contador de sufijos irregulares: " + conjCount );
    	    } 
    	    catch (IOException ex){
    	    	if (DEBUG_ERROR) utils.Debug.show( "file irregularesModelos.dat not found");
    	    }
    	}
    	return result;
	}
	
	private static TreeSet<SufijoVerbal> initSufijos() {
		TreeSet<SufijoVerbal> result = new TreeSet<SufijoVerbal>();
		conjugadosRegulares = new TreeSet<VerboModeloConjugacion>();
		VerboModeloConjugacion verboR = new VerboModeloConjugacion( "deber" );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, false, "presente", false, "o" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, false, "presente", false, "es" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, false, "presente", false, "e" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, true, "presente", false, "emos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, true, "presente", false, "�is" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, true, "presente", false, "en" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, false, "pret�rito imperfecto", false, "�a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, false, "pret�rito imperfecto", false, "�as" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, false, "pret�rito imperfecto", false, "�a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, true, "pret�rito imperfecto", false, "�amos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, true, "pret�rito imperfecto", false, "�ais" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, true, "pret�rito imperfecto", false, "�an" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, false, "futuro", false, "er�" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, false, "futuro", false, "er�s" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, false, "futuro", false, "er�" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, true, "futuro", false, "eremos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, true, "futuro", false, "er�is" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, true, "futuro", false, "er�n" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, false, "condicional", false, "er�a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, false, "condicional", false, "er�as" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, false, "condicional", false, "er�a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, true, "condicional", false, "er�amos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, true, "condicional", false, "er�ais" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, true, "condicional", false, "er�an" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, false, "pret�rito perfecto simple", false, "�" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, false, "pret�rito perfecto simple", false, "iste" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, false, "pret�rito perfecto simple", false, "i�" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, true, "pret�rito perfecto simple", false, "imos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, true, "pret�rito perfecto simple", false, "isteis" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, true, "pret�rito perfecto simple", false, "ieron" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, false, "presente", true, "a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, false, "presente", true, "as" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, false, "presente", true, "a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, true, "presente", true, "amos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, true, "presente", true, "�is" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, true, "presente", true, "an" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, false, "pret�rito perfecto", true, "iera" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, false, "pret�rito perfecto", true, "ieras" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, false, "pret�rito perfecto", true, "iera" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, true, "pret�rito perfecto", true, "i�ramos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, true, "pret�rito perfecto", true, "ierais" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, true, "pret�rito perfecto", true, "ieran" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, false, "pret�rito perfecto", true, "iese" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, false, "pret�rito perfecto", true, "ieses" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, false, "pret�rito perfecto", true, "iese" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, true, "pret�rito perfecto", true, "i�semos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, true, "pret�rito perfecto", true, "ieseis" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, true, "pret�rito perfecto", true, "iesen" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, false, "futuro", true, "iere" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, false, "futuro", true, "ieres" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, false, "futuro", true, "iere" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, true, "futuro", true, "i�remos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, true, "futuro", true, "iereis" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, true, "futuro", true, "ieren" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, false, "imperativo", false, "e" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, false, "imperativo", false, "a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 1, true, "imperativo", false, "amos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 2, true, "imperativo", false, "ed" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 3, true, "imperativo", false, "an" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 0, true, "gerundio", false, "iendo" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "deber", "er", 0, true, "participio pasivo", false, "ido" ) );
		conjugadosRegulares.add( verboR );
	
		verboR = new VerboModeloConjugacion( "amar" );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, false, "presente", false, "o" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, false, "presente", false, "as" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, false, "presente", false, "a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, true, "presente", false, "amos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, true, "presente", false, "�is" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, true, "presente", false, "an" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, false, "pret�rito imperfecto", false, "aba" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, false, "pret�rito imperfecto", false, "abas" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, false, "pret�rito imperfecto", false, "aba" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, true, "pret�rito imperfecto", false, "�bamos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, true, "pret�rito imperfecto", false, "abais" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, true, "pret�rito imperfecto", false, "aban" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, false, "futuro", false, "ar�" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, false, "futuro", false, "ar�s" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, false, "futuro", false, "ar�" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, true, "futuro", false, "aremos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, true, "futuro", false, "ar�is" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, true, "futuro", false, "ar�n" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, false, "condicional", false, "ar�a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, false, "condicional", false, "ar�as" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, false, "condicional", false, "ar�a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, true, "condicional", false, "ar�amos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, true, "condicional", false, "ar�ais" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, true, "condicional", false, "ar�an" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, false, "pret�rito perfecto simple", false, "�" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, false, "pret�rito perfecto simple", false, "aste" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, false, "pret�rito perfecto simple", false, "�" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, true, "pret�rito perfecto simple", false, "amos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, true, "pret�rito perfecto simple", false, "asteis" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, true, "pret�rito perfecto simple", false, "aron" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, false, "presente", true, "e" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, false, "presente", true, "es" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, false, "presente", true, "e" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, true, "presente", true, "emos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, true, "presente", true, "�is" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, true, "presente", true, "en" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, false, "pret�rito perfecto", true, "ara" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, false, "pret�rito perfecto", true, "aras" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, false, "pret�rito perfecto", true, "ara" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, true, "pret�rito perfecto", true, "�ramos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, true, "pret�rito perfecto", true, "arais" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, true, "pret�rito perfecto", true, "aran" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, false, "pret�rito perfecto", true, "ase" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, false, "pret�rito perfecto", true, "ases" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, false, "pret�rito perfecto", true, "ase" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, true, "pret�rito perfecto", true, "�semos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, true, "pret�rito perfecto", true, "aseis" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, true, "pret�rito perfecto", true, "asen" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, false, "futuro", true, "are" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, false, "futuro", true, "ares" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, false, "futuro", true, "are" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, true, "futuro", true, "�remos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, true, "futuro", true, "areis" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, true, "futuro", true, "aren" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, false, "imperativo", false, "a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, false, "imperativo", false, "e" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 1, true, "imperativo", false, "emos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 2, true, "imperativo", false, "ad" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 3, true, "imperativo", false, "en" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 0, true, "gerundio", false, "ando" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "amar", "ar", 0, true, "participio pasivo", false, "ado" ) );
		conjugadosRegulares.add( verboR );
		
		verboR = new VerboModeloConjugacion( "vivir" );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, false, "presente", false, "o" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, false, "presente", false, "es" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, false, "presente", false, "e" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, true, "presente", false, "imos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, true, "presente", false, "�s" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, true, "presente", false, "en" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, false, "pret�rito imperfecto", false, "�a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, false, "pret�rito imperfecto", false, "�as" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, false, "pret�rito imperfecto", false, "�a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, true, "pret�rito imperfecto", false, "�amos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, true, "pret�rito imperfecto", false, "�ais" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, true, "pret�rito imperfecto", false, "�an" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, false, "futuro", false, "ir�" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, false, "futuro", false, "ir�s" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, false, "futuro", false, "ir�" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, true, "futuro", false, "iremos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, true, "futuro", false, "ir�is" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, true, "futuro", false, "ir�n" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, false, "condicional", false, "ir�a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, false, "condicional", false, "ir�as" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, false, "condicional", false, "ir�a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, true, "condicional", false, "ir�amos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, true, "condicional", false, "ir�ais" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, true, "condicional", false, "ir�an" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, false, "pret�rito perfecto simple", false, "�" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, false, "pret�rito perfecto simple", false, "iste" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, false, "pret�rito perfecto simple", false, "i�" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, true, "pret�rito perfecto simple", false, "imos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, true, "pret�rito perfecto simple", false, "isteis" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, true, "pret�rito perfecto simple", false, "ieron" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, false, "presente", true, "a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, false, "presente", true, "as" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, false, "presente", true, "a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, true, "presente", true, "amos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, true, "presente", true, "�is" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, true, "presente", true, "an" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, false, "pret�rito perfecto", true, "iera" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, false, "pret�rito perfecto", true, "ieras" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, false, "pret�rito perfecto", true, "iera" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, true, "pret�rito perfecto", true, "i�ramos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, true, "pret�rito perfecto", true, "ierais" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, true, "pret�rito perfecto", true, "ieran" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, false, "pret�rito perfecto", true, "iese" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, false, "pret�rito perfecto", true, "ieses" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, false, "pret�rito perfecto", true, "iese" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, true, "pret�rito perfecto", true, "i�semos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, true, "pret�rito perfecto", true, "ieseis" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, true, "pret�rito perfecto", true, "iesen" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, false, "futuro", true, "iere" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, false, "futuro", true, "ieres" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, false, "futuro", true, "iere" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, true, "futuro", true, "i�remos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, true, "futuro", true, "iereis" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, true, "futuro", true, "ieren" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, false, "imperativo", false, "e" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, false, "imperativo", false, "a" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 1, true, "imperativo", false, "amos" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 2, true, "imperativo", false, "id" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 3, true, "imperativo", false, "an" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 0, true, "gerundio", false, "iendo" ) );
			anyadirSufijoADosArboles( result, verboR, new SufijoVerbal( "vivir", "ir", 0, true, "participio pasivo", false, "ido" ) );
		conjugadosRegulares.add( verboR );

		// Initialize max length of suffix
		Iterator<SufijoVerbal> i = result.iterator();
		while (i.hasNext()) { 
			int numCars = i.next().getSufijo().length(); 
			if (numCars > longMaxSufijos) longMaxSufijos = numCars; 
		}
		
		return result;
	}
	
	public static int getLongMaxSufijos() { return longMaxSufijos; }

	/** Chequea un infinitivo verbal en min�sculas y sin tildes para ver si es un verbo regular
	 * @param pal	Verbo a chequear (min�sculas, sin tildes)
	 * @return	true si es regular, false si es irregular o no es un verbo
	 */
	public static boolean esVerboRegular(String pal) {
		return (verbosRegulares.contains(pal));
	}	

	/** Chequea un infinitivo verbal en min�sculas y sin tildes para ver si es un verbo irregular 
	 * de la lista de no conjugados
	 * @param pal	Verbo a chequear en infinitivo (min�sculas, sin tildes)
	 * @return	true si es irregular de la lista, false si no est� en la lista o no es un verbo
	 */
	public static boolean esVerboIrregularSinConjugaciones(String pal) {
		return (verbosIrregularesConjugados.contains( new VerboIrregularConjugado( pal, false, "" ) ));
	}	

	/** Chequea si una palabra �nica es una forma verbal correcta (sin sufijos)
	 * @param pal	Palabra (�nica) a chequear
	 * @param exactMatch	true si se quiere que el encaje sea exacto con tildes, false devuelve resultados a�n con tildes incorrectas
	 * @param checkOnlyRegularVerbs	true si se quiere chequear s�lo verbos regulares; con false chequea irregulares conjugados
	 * @return	true si se encuentra al menos una forma verbal equivalente a pal
	 */
	public static boolean esFormaVerbal( String pal, boolean exactMatch, boolean checkOnlyRegularVerbs ) {
		lastConjugacion = null;
		lastVectorConjs = new Vector<Conjugacion>();
		String palST = UtilsVerbos.quitarTildes(pal);
		// Chequear si es infinitivo (de regular o de irregular)
		if (verbosRegulares.contains(palST)) {
			// TODO: Sustituir el par�metro 2 y el 7 por la terminaci�n correcta sacada del modelo del verboConjugado
			float confianza = CONFIANZA_EXACTA;
			if (exactMatch) if (!verbosRegulares.contains(pal)) confianza = CONFIANZA_SIN_TILDES;
			Conjugacion c = new Conjugacion( pal, pal.substring(pal.length()-2), 0, false, "infinitivo", false, pal.substring(pal.length()-2), pal, confianza );
			lastVectorConjs.add( c );
			lastConjugacion = c;
			return true;
		} else if (!checkOnlyRegularVerbs && verbosIrregularesConjugados.contains( new VerboIrregularConjugado(palST,false,"") )) {
			float confianza = CONFIANZA_EXACTA;
			if (exactMatch) if (!verbosIrregularesConjugados.contains( new VerboIrregularConjugado( pal, false, "" ) )) confianza = CONFIANZA_SIN_TILDES;
			Conjugacion c = new Conjugacion( pal, pal.substring(pal.length()-2), 0, false, "infinitivo", false, pal.substring(pal.length()-2), pal, confianza );
			lastVectorConjs.add( c );
			lastConjugacion = c;
			return true;
		}
		// Chequear todos los sufijos posibles de pal, partiendo de un car�cter hasta la longitud m�xima de la palabra (o la del sufijo m�s largo)
		float confianzaDeVerbo = CONFIANZA_EXACTA;
		for (int longSufijo = 1; longSufijo <= Math.min(getLongMaxSufijos(), pal.length()); longSufijo++) {
			String suf = pal.substring( pal.length() - longSufijo );
			String raizST = palST.substring( 0, pal.length() - longSufijo );
			boolean verboValido = false;
			if (esSufijoVerbal(suf, exactMatch, false)) { // Primero chequeamos si es regular
				// El sufijo regular es posible. Comprobar si la ra�z es un verbo regular v�lido
				for (String terminacion : listaTerminacionesVerbales()) {
					String posibleVerbo = raizST + terminacion;
					if (verbosRegulares.contains(posibleVerbo)) { 
						// TODO: REVISAR SI HAY QUE HACER ESTO O YA ESTA BIEN
						// Si no es as�, seguir en la siguiente iteraci�n sin poner verboValido a true
						// Explicaci�n: este caso es que el texto que estamos procesando tiene un error de conjugaci�n
						//   (por ejemplo "perda" (modelo "deba" de deber) en lugar de "pierda" para el verbo perder)
						//   con lo cual debemos despreciarlo para evitar reconocimientos incorrectos de textos
						// O bien es una conjugaci�n com�n entre modelos (p ej "perd�a" (modelo "deb�a" de deber))
						//   con lo cual este modelo en particular se puede despreciar (deber� haber otro sufijo en el modelo correcto)
						verboValido = true; confianzaDeVerbo = CONFIANZA_EXACTA; break; } // Es posible
				}
				if (verboValido) {
					// Si es un verbo v�lido, procesar todos los sufijos y sus confianzas
					int i = 0;
					for (SufijoVerbal s : variosSufijos()) {
						float confianza = variasConfianzasSufijos().get(i).floatValue() * confianzaDeVerbo; i++;
						String posibleVerbo = raizST + s.getTerminacion();
						if (verbosRegulares.contains(posibleVerbo)) {
							Conjugacion c = new Conjugacion( posibleVerbo, s.getTerminacion(), s.persona, s.numeroPlural, s.forma, s.modoSubjuntivo, s.sufijo, pal, confianza );
							lastVectorConjs.add( c );
							if (lastConjugacion == null || confianza > lastConjugacion.getConfianza()) {
								lastConjugacion = c;
							}
						}
//						if (!checkOnlyRegularVerbs && verbosIrregularesConjugados.contains( new VerboIrregularConjugado(posibleVerbo,false,"") )) {
//							Conjugacion c = new Conjugacion( posibleVerbo, s.getTerminacion(), s.persona, s.numeroPlural, s.forma, s.modoSubjuntivo, s.sufijo, pal, confianza );
//							lastVectorConjs.add( c );
//							if (lastConjugacion == null || confianza > lastConjugacion.getConfianza()) {
//								lastConjugacion = c;
//							}
//						}
					}
				}
			}
			if (!verboValido && !checkOnlyRegularVerbs && esSufijoVerbal(suf, exactMatch, true)) {  // Y si no es regular, a ver si es irregular
				// El sufijo irregular es posible. Comprobar si la ra�z es un verbo irregular v�lido
				for (String terminacion : listaTerminacionesVerbales()) {
					String posibleVerbo = raizST + terminacion;
					if (verbosIrregularesConjugados.contains( new VerboIrregularConjugado(posibleVerbo,false,"") )) { 
						// Chequeamos si el verbo irregular tiene modelo, y despu�s veremos si ese modelo es el correspondiente al sufijo encontrado
						verboValido = true; confianzaDeVerbo = CONFIANZA_EXACTA; break; } // Es posible
				}
				if (verboValido) {
					// Si es un verbo v�lido, procesar todos los sufijos y sus confianzas
					int i = 0;
					for (SufijoVerbal s : variosSufijos()) {
						float confianza = variasConfianzasSufijos().get(i).floatValue() * confianzaDeVerbo; i++;
						String posibleVerbo = raizST + s.getTerminacion();
						if (verbosIrregularesConjugados.contains( new VerboIrregularConjugado(posibleVerbo,false,"") )) {
							// Chequear el modelo, y si ese modelo es el correspondiente al sufijo encontrado
							VerboIrregularConjugado vi = verbosIrregularesConjugados.ceiling( new VerboIrregularConjugado(posibleVerbo,false,"") );
							if (vi.getModelo().equals( s.getModelo() )) { 
								Conjugacion c = new Conjugacion( posibleVerbo, s.getTerminacion(), s.persona, s.numeroPlural, s.forma, s.modoSubjuntivo, s.sufijo, pal, confianza );
								lastVectorConjs.add( c );
								if (lastConjugacion == null || confianza > lastConjugacion.getConfianza()) {
									lastConjugacion = c;
								}
//							} else {  // Ya est�n todos los verbos irregulares modelo, esto sobra
//								Conjugacion c = new Conjugacion( posibleVerbo, s.getTerminacion(), s.persona, s.numeroPlural, s.forma, s.modoSubjuntivo, s.sufijo, pal, confianza * CONFIANZA_MODELOMAL );
//								lastVectorConjs.add( c );
//								if (lastConjugacion == null || c.getConfianza() > lastConjugacion.getConfianza()) {
//									lastConjugacion = c;
//								}
							}
						}
					}
				}
			}
		}
		return (lastConjugacion != null);
	}


	private static Conjugacion lastConjugacion = null;
	/** Devuelve la conjugaci�n de la �ltima forma verbal buscada y encontrada
	 * @return	Ultima forma verbal encontrada, null si no se encontr� (esFormaVerbal = false)
	 */
	public static Conjugacion getConjugacion() {
		return lastConjugacion;
	}

//  DEPRECATED
//	private static float lastConfianza = 0.0F;
//	/** La confianza de la detecci�n es de un 100% si es seguro que s�, y de un 75% si coincide sin tildes pero no con tildes
//	 * @return  Confianza de la detecci�n (0.0 ninguna a 1.0 m�xima)
//	 */
//	public static float confianzaDeteccion() {
//		return lastConfianza;
//	}

	private static Vector<Conjugacion> lastVectorConjs = null; 
	/** Devuelve todas las conjugaciones posibles de la forma verbal pasada a esFormaVerbal
	 * @return	Vector de todas las formas verbales encontradas que encajan con la palabra indicada, vac�o si no se encontr� ninguna
	 */
	public static Vector<Conjugacion> variasConjugaciones() {
		return lastVectorConjs;
	}

// DEPRECATED
//	private static Vector<Float> lastVectorConfianzas = null; 
//	/** Devuelve las confianzas de las conjugaciones posibles indicadas por variasConjugaciones()
//	 * @return	Vector de confianzas (de 0.0 a 1.0) de cada forma verbal, en el mismo orden del vector de variasConjugaciones(),
//	 * 		vac�o si no se encontr� ninguna conjugaci�n
//	 */
//	public static Vector<Float> variasConfianzas() {
//		return lastVectorConfianzas;
//	}

			
	static SufijoVerbal sufTest = new SufijoVerbal( "", "", 0, false, "", false, "" );
	/** Devuelve la informaci�n de si un sufijo es un posible sufijo verbal consultando en la BD de sufijos
	 * pre: suf es un sufijo sin espacios, en min�sculas (puede tener tildes)
	 * post: tras llamar a esta funci�n se pueden chequear los m�todos getConjugacion(), variasConjugaciones()
	 * @param suf	Sufijo a chequear
	 * @param exactMatch	true si el sufijo tiene que ser exacto incluyendo tildes, con false las tildes no se consideran
	 * @param irregular		false si se quiere chequear en formas regulares, true para sufijos irregulares
	 * @return	true si es un sufijo correcto, false si no lo es
	 */
	// Actualiza las listas internas de modelos y terminaciones verbales posibles para el sufijo suf
	public static boolean esSufijoVerbal( String suf, boolean exactMatch, boolean irregular ) {
		lastVectorDeModelosVerbales = new Vector<String>();
		lastVectorDeTerminacionesVerbales = new Vector<String>();
		calcVariosSufijos(suf, exactMatch, irregular);
		if (lastVectorSufijos.size() > 0) {
			// Calculamos los modelos verbales encontrados en los sufijos
			for (SufijoVerbal s : lastVectorSufijos) {
				String modelo = s.getModelo();
				String terminacion = s.getTerminacion();
				if (!lastVectorDeModelosVerbales.contains(modelo)) {
					lastVectorDeModelosVerbales.add( modelo );
					lastVectorDeTerminacionesVerbales.add( terminacion );
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private static Vector<SufijoVerbal> lastVectorSufijos = null; 
	/** Devuelve todos los sufijos posibles del sufijo pasado a esSufijoVerbal
	 * @return	Vector de todos los sufijos encontrados que encajan con la palabra indicada, vac�o si no se encontr� ninguno
	 */
	public static Vector<SufijoVerbal> variosSufijos() {
		return lastVectorSufijos;
	}

	private static Vector<Float> lastVectorConfianzasSufijos = null; 
	/** Devuelve las confianzas de los sufijos posibles indicadas por variosSufijos()
	 * @return	Vector de confianzas (de 0.0 a 1.0) de cada sufijo verbal, en el mismo orden del vector de variosSufijos(),
	 * 		vac�o si no se encontr� ning�n sufijo
	 */
	public static Vector<Float> variasConfianzasSufijos() {
		return lastVectorConfianzasSufijos;
	}

	private static Vector<String> lastVectorDeModelosVerbales = null;
	/** Devuelve los modelos verbales incluidos en los sufijos calculados por esSufijoVerbal
	 * Si varios sufijos posibles afectan al mismo modelo, en este vector s�lo se indica una vez
	 * @return	Vector de todos los modelos verbales ("amar", "deber", "vivir" ... ) encontrados que encajan en los sufijos, vac�o si no se encontr� ninguno
	 */
	public static Vector<String> listaModelosVerbales() {
		return lastVectorDeModelosVerbales;
	}
	
	private static Vector<String> lastVectorDeTerminacionesVerbales = null;
	/** Devuelve las terminaciones de los modelos verbales incluidos en los sufijos calculados por esSufijoVerbal
	 * Si varios sufijos posibles afectan al mismo modelo, en este vector s�lo se indica una vez
	 * (pero puede haber terminaciones repetidas, para distintos modelos. P ej amar/-ar y andar/-ar)
	 * @return	Vector de todas las terminaciones verbales ("ar", "er", "ir") encontrados que encajan en los sufijos, vac�o si no se encontr� ninguno
	 * post: El vector de terminaciones tiene relaci�n uno a uno con el de modelos
	 */
	public static Vector<String> listaTerminacionesVerbales() {
		return lastVectorDeTerminacionesVerbales;
	}
	
	
	/** M�todo usado por esFormaVerbal para calcular el vector de conjugaciones
	 */
	private static void calcVariosSufijos( String suf, boolean exactMatch, boolean irregular ) {
		Vector<SufijoVerbal> v = new Vector<SufijoVerbal>();
		Vector<Float> vc = new Vector<Float>();
		String sufST = UtilsVerbos.quitarTildes(suf);
		sufTest.initSufijoBusqueda( sufST );
		TreeSet<SufijoVerbal> misSufijos = (irregular)?sufijosIrregulares:sufijos;
		if (misSufijos.contains(sufTest)) {
			// Si hay varias devolvemos todas. Primero por detr�s...
			Iterator<SufijoVerbal> grupo = misSufijos.headSet( sufTest, true ).descendingIterator();  // Cogemos las menores o iguales en descendente
			while (grupo.hasNext()) {
				SufijoVerbal s = grupo.next();
				if (!sufST.equals(s.getSufijoSinTildes())) break;  // Si no es el mismo sufijo, se para el bucle (s�lo se recorren iguales)
				if (!exactMatch || suf.equals(s.getSufijo())) {  // Si la comparaci�n es exacta y no es el mismo sufijo, no se a�ade (s�lo se recorren iguales)
					v.add(s);  // se a�ade
					if (suf.equals(s.getSufijo())) vc.add(CONFIANZA_EXACTA); else vc.add(CONFIANZA_SIN_TILDES);  // se a�ade la confianza
				}
			}
			// Y luego por delante:
			grupo = misSufijos.tailSet( sufTest, true ).iterator();  // Cogemos los mayores o iguales
			grupo.next();  // Y despreciamos el primero (se ha procesado en el bucle de "detr�s"
			while (grupo.hasNext()) {
				SufijoVerbal s = grupo.next();
				if (!sufST.equals(s.getSufijoSinTildes())) break;  // Si no es el mismo sufijo, se para el bucle (s�lo se recorren iguales)
				if (!exactMatch || suf.equals(s.getSufijo())) {  // Si la comparaci�n es exacta y no es el mismo sufijo, no se a�ade (s�lo se recorren iguales)
					v.add(s);  // se a�ade
					if (suf.equals(s.getSufijo())) vc.add(CONFIANZA_EXACTA); else vc.add(CONFIANZA_SIN_TILDES);  // se a�ade la confianza
				}
			}
		}
		lastVectorSufijos = v;
		lastVectorConfianzasSufijos = vc;
	}
	
	/** Devuelve la conjugaci�n total de un verbo dado. Si el verbo no est� en la lista de verbos existentes, devuelve el string vac�o
	 * @param verbo	Verbo a conjugar (en infinitivo)
	 * @return	String con las conjugaciones. Primera l�nea indica el nombre del verbo, si es o no irregular, y el modelo de conjugaci�n verbal usado.
	 */
	public static String conjugarVerbo( String verbo ) {
		String res = "";
		verbo = verbo.toLowerCase();
		String verboModelo = "amar";
		VerboModeloConjugacion vmc = null;
		if (verbosRegulares.contains( verbo )) { // Es regular
			res = "Conjugaci�n de verbo " + verbo + ". Regular.\n";
			if (verbo.endsWith("er")) verboModelo = "deber";
			else if (verbo.endsWith("ir")) verboModelo = "vivir";
			vmc = conjugadosRegulares.ceiling( new VerboModeloConjugacion( verboModelo ));
		} else if (verbosIrregularesConjugados.contains( new VerboIrregularConjugado( verbo, false, "" ))) { // Es irregular
			verboModelo = verbosIrregularesConjugados.ceiling( new VerboIrregularConjugado( verbo, false, "" ) ).getModelo();
			vmc = conjugadosIrregulares.ceiling( new VerboModeloConjugacion( verboModelo ));
			res = "Conjugaci�n de verbo " + verbo + ". Irregular. Modelo " + verboModelo + ".\n";
		} else return res; // no existe (devuelve "")
		boolean first = true;
		String formaVerbal = "";
		for (SufijoVerbal sv : vmc.getConjugaciones()) {
			if (first) {
				verboModelo = verbo.substring( 0, verbo.length() - sv.getTerminacion().length() );  // Raiz de verbo
				first = false;
			}
			if (!formaVerbal.equals( sv.getFormaVerbal() + (sv.getModoVerbal()?" subjuntivo":" indicativo") )) {
				if (sv.getPersona()==0) {  // Forma verbal impersonal (infinitivo, gerundio, participio)
					formaVerbal = sv.getFormaVerbal();
				} else
					formaVerbal = sv.getFormaVerbal() + (sv.getModoVerbal()?" subjuntivo":" indicativo");
				res = res + formaVerbal + "\n";
			}
			String persona = " yo ";
			if (sv.getNumeroPlural()) { if (sv.getPersona()==1) persona = " nosotr@s "; else if (sv.getPersona()==2) persona = " vosotr@s "; else persona = " ell@s "; }
			else { if (sv.getPersona()==2) persona = " t� "; else if (sv.getPersona()==3) persona = " �l/ella "; }
			if (sv.getPersona()==0) {  // Formas verbales impersonales
				res = res + " " + verboModelo + sv.getSufijo() + "\n";
			} else if (formaVerbal.equals("imperativo indicativo")) {  // Forma imperativo (sujeto despu�s del verbo)
				res = res + " " + verboModelo + sv.getSufijo() + " " + persona.trim() + "\n";
			} else {  // Conjugaci�n normal
				res = res + persona + verboModelo + sv.getSufijo() + "\n";
			}
				// + sv.getPersona() + (sv.getNumeroPlural()?"P":"S");
		}
		return res;
	}

	/** Devuelve una conjugaci�n particular total de un verbo dado. Si el verbo no est� en la lista de verbos existentes
	 * o hay alg�n error en los par�metros, devuelve el string vac�o
	 * @param verbo	Verbo a conjugar (en infinitivo)
	 * @param tiempo	Tiempo verbal ("presente", "participio" ...)
	 * @param modoSubjuntivo	Modo verbal (true para subjuntivo, false para indicativo)
	 * @param persona	Persona (0 - impersonal, 1-3 - primera a tercera persona)
	 * @param numeroPlural	N�mero (true para plural, false para singular)
	 * @return	String con las conjugaciones. Primera l�nea indica el nombre del verbo, si es o no irregular, y el modelo de conjugaci�n verbal usado.
	 */
	public static String conjugarVerbo( String verbo, String tiempo, boolean modoSubjuntivo, int persona, boolean numeroPlural ) {
		verbo = verbo.toLowerCase();
		String verboModelo = "amar";
		VerboModeloConjugacion vmc = null;
		if (verbosRegulares.contains( verbo )) { // Es regular
			if (verbo.endsWith("er")) verboModelo = "deber";
			else if (verbo.endsWith("ir")) verboModelo = "vivir";
			vmc = conjugadosRegulares.ceiling( new VerboModeloConjugacion( verboModelo ));
		} else if (verbosIrregularesConjugados.contains( new VerboIrregularConjugado( verbo, false, "" ))) { // Es irregular
			verboModelo = verbosIrregularesConjugados.ceiling( new VerboIrregularConjugado( verbo, false, "" ) ).getModelo();
			vmc = conjugadosIrregulares.ceiling( new VerboModeloConjugacion( verboModelo ));
		} else return ""; // no existe (devuelve "")
		boolean first = true;
		for (SufijoVerbal sv : vmc.getConjugaciones()) {
			if (first) {
				verboModelo = verbo.substring( 0, verbo.length() - sv.getTerminacion().length() );  // Raiz de verbo
				first = false;
			}
			if (sv.getFormaVerbal().equals(tiempo) && sv.getModoVerbal()==(modoSubjuntivo) && 
				(sv.getPersona()==0 || (sv.getNumeroPlural()==numeroPlural)) && sv.getPersona()==persona) {
				return verboModelo + sv.getSufijo();
			}
		}
		return "";
	}

	public static void main (String[] s) {
		
//		Debug.show( "bru�ir = " + ChequeoWeb.comprobarVerbo( "bru�ir" ) );
//		Debug.show( "beate�r = " + ChequeoWeb.comprobarVerbo( "beate�r" ) );
//		Debug.show( "conservar = " + ChequeoWeb.comprobarVerbo( "conservar" ) );
//		Debug.show( "te�ir = " + ChequeoWeb.comprobarVerbo( "te�ir" ) );

		Debug.setPrefix("* ");
		Debug.show( "Sufijos verbales regulares:"); int cont = 0;
		Iterator<SufijoVerbal> i = sufijos.iterator();
		while (i.hasNext()) { utils.Debug.show( i.next().show() ); cont++; }
		Debug.show( "Contador de sufijos: " + cont );
		Debug.show( "Sufijos verbales irregulares:"); int cont2 = 0;
		Iterator<SufijoVerbal> i2 = sufijosIrregulares.iterator();
		while (i2.hasNext()) { utils.Debug.show( i2.next().show() ); cont2++; }
		Debug.show( "Contador de sufijos: " + cont2 );
		Debug.show( "Long. m�xima de sufijo: " + getLongMaxSufijos() + " caracteres." );
		Debug.show( "" );
		Debug.show( "Sufijo regular -�: " + esSufijoVerbal("�", false, false) + " " + (variosSufijos().size()==0?"vac�o":variosSufijos()) );
		Debug.show( "Sufijo regular -� (forzando tildes): " + esSufijoVerbal("�", true, false) + " " + (variosSufijos().size()==0?"vac�o":variosSufijos()) );
		Debug.show( "Sufijo regular -�: " + esSufijoVerbal("�", false, false) + " " + (variosSufijos().size()==0?"vac�o":variosSufijos()) );
			for (int j = 0; j < variosSufijos().size(); j++ )
				Debug.show( "  " + variosSufijos().get(j).show() + " [confianza " + variasConfianzasSufijos().get(j) + "]" );
		Debug.show( "Sufijo irregular -�: " + esSufijoVerbal("�", false, true) + " " + (variosSufijos().size()==0?"vac�o":variosSufijos()) );
		Debug.show( "Sufijo irregular -� (forzando tildes): " + esSufijoVerbal("�", true, true) + " " + (variosSufijos().size()==0?"vac�o":variosSufijos()) );
		Debug.show( "Sufijo irregular -�: " + esSufijoVerbal("�", false, true) + " " + (variosSufijos().size()==0?"vac�o":variosSufijos()) );
			for (int j = 0; j < variosSufijos().size(); j++ )
				Debug.show( "  " + variosSufijos().get(j).show() + " [confianza " + variasConfianzasSufijos().get(j) + "]" );
		boolean comprobarTambienIrregulares = true;
		Debug.show( "cantar: " + esFormaVerbal("cantar", false, !comprobarTambienIrregulares) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "cantar�a: " + esFormaVerbal("cantar�a", false, !comprobarTambienIrregulares) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "cantar�a (forzando tildes): " + esFormaVerbal("cantar�a", true, !comprobarTambienIrregulares) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "cantaria: " + esFormaVerbal("cantaria", false, !comprobarTambienIrregulares) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "cantaria (forzando tildes): " + esFormaVerbal("cantaria", true, !comprobarTambienIrregulares) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "am�: " + esFormaVerbal("am�", false, !comprobarTambienIrregulares) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
			for (int k = 0; k < variasConjugaciones().size(); k++ )
				Debug.show( "  " + variasConjugaciones().get(k).show( true ) );
		Debug.show( "aborreciste: " + esFormaVerbal("aborreciste", false, !comprobarTambienIrregulares) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		for (int k = 0; k < variasConjugaciones().size(); k++ )
			Debug.show( "  " + variasConjugaciones().get(k).show( true ) );
		Debug.show( "acosto: " + esFormaVerbal("acosto", false, !comprobarTambienIrregulares) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		for (int k = 0; k < variasConjugaciones().size(); k++ )
			Debug.show( "  " + variasConjugaciones().get(k).show( true ) );
		Debug.show( "aborrec�: " + esFormaVerbal("aborrec�", false, !comprobarTambienIrregulares) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "detener: " + esFormaVerbal("detener", false, !comprobarTambienIrregulares) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		Debug.show( "detengo: " + esFormaVerbal("detengo", false, !comprobarTambienIrregulares) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) );
		muestraPrueba( "le�do", comprobarTambienIrregulares ); 
		muestraPrueba( "leido", comprobarTambienIrregulares ); 
		Debug.setPrefix("");
		Debug.show( conjugarVerbo("denunciar") + "\n");
		Debug.show( conjugarVerbo("guiar") + "\n");
		Debug.setPrefix("* ");
		Debug.show( "conj. denuncio nosotros = " + conjugarVerbo("denunciar","presente",false,1,true) );
		Debug.show( "participio denuncio = " + conjugarVerbo("denunciar","participio pasivo",false,0,false) );
		
//		Debug.show( "Verbos regulares:"); cont = 0;
//		Iterator<String> vr = iteratorVerbosRegulares();
//		while (vr.hasNext()) { utils.Debug.show( vr.next() ); cont++; }
//		Debug.show( "Contador de verbos: " + cont );
//		utils.Debug.close();
	}
	
	private static void muestraPrueba( String prueba, boolean cti ) { Debug.show( prueba + ": " + esFormaVerbal(prueba, false, !cti) + " " + (getConjugacion()==null?"nulo":lastConjugacion.show( true )) ); }

}

class VerboModeloConjugacion implements Comparable<VerboModeloConjugacion> {
	private String infin = "";
	private Vector<SufijoVerbal> conjugaciones;
	VerboModeloConjugacion( String infinitivo ) {
		infin = infinitivo;
		conjugaciones = new Vector<SufijoVerbal>();
	}
	public String getInfinitivo() {
		return infin;
	}
	public Vector<SufijoVerbal> getConjugaciones() {
		return conjugaciones;
	}
	public int compareTo(VerboModeloConjugacion arg0) {
		if (arg0 == null) return 1;
		return infin.compareTo( arg0.infin );
	}
}

class ChequeoWeb2 extends Thread {
	private static int TRIES_NUM = 10;  // A veces la web da error, que no se puede diferenciar de que el verbo no exista. Probamos "n" veces
	private static long SLEEP_TIME = 10000;  // Tiempo de sue�o entre errores
	private String miVerbo;
	String elModelo = "[NO-ACABADO]";
	ChequeoWeb2( String v ) {
		miVerbo = v;
	}
	public void run() {
		// Devuelve en <elModelo> el modelo de la web conjugador.reverso.net  ("" si no existe o no hay conexi�n a la web)
		// Ponemos �s y posibles tildes para formato URL
		String verbo = miVerbo.toLowerCase();
		verbo = verbo.replace("�","%C3%B1");
		verbo = verbo.replace("�","%C3%A1");
		verbo = verbo.replace("�","%C3%AD");
		String res = "";
		int intentos = 0;
		do {
			try {
		        URL url = new URL("http://conjugador.reverso.net/conjugacion-espanol-verbo-"+verbo+".html");  // � = %C3%B1  ñ  � = %C3%AD í  � = %C3%A1  
		        URLConnection yc = url.openConnection();
		        BufferedReader in = new BufferedReader( new InputStreamReader( yc.getInputStream() ) );
		        String inputLine;
		        while ((inputLine = in.readLine()) != null) {
	        		// System.out.println(inputLine);
		        	if (inputLine.contains("modelo-espanol/modelo-")) {
		        		int pos = inputLine.indexOf( "r.html'>" );
		        		int pos2 = inputLine.indexOf( "</a>", pos );
		        		if (pos > -1 && pos2 > -1) { // Si encontramos el modelo en el texto HTML
		        			res = inputLine.substring( pos+"r.html'>".length(), pos2 );
		        			// Convertir posible tilde de modelo (s�lo "�" o "�")
		        			res = res.replace( "ñ", "�" );
		        			res = res.replace( "í", "�" );
		        		}
		        		break;
		        	} else if (inputLine.contains("El verbo buscado no corresponde a ninguna tabla de conjugac")) {  // Verbo incorrecto
		        		res = "[NO]";
		        		break;
		        	} else if (inputLine.contains("Este verbo es desconocido. Si igualmente desea conjugarlo")) {  // Verbo incorrecto
		        		res = "[NO]";
		        		break;
		        	}
		        }
		        in.close();
				if (res.equals("")) Thread.sleep( SLEEP_TIME );
			} catch (Exception e) {
				try { Thread.sleep( SLEEP_TIME ); } catch (InterruptedException e2) {}
			}	
	        intentos++;
		} while (res.equals("") && intentos < TRIES_NUM);
		elModelo = res;
	}
}

class ChequeoWeb extends Thread {
	private static int activeThreadsNum = 0;
	private String miVerbo;
	private String miModelo;
	private boolean esIrregular;
	private boolean miDebug;
	private static int TRIES_NUM = 5;
	private static long MAX_TIME_LIMIT = 60000;  // Max. time to wait for web response in each try (milliseconds)
	private static long SLEEP_TIME = 1000;  // Min. time to wait for web response (milliseconds)
	String elModelo = "";
	ChequeoWeb( String v, String m, boolean i, boolean debugLog ) {
		miVerbo = v; miModelo = m; esIrregular = i; miDebug = debugLog;
	}
	synchronized static void decThreads() {
		activeThreadsNum--;
	}
	synchronized static void incThreads() {
		activeThreadsNum++;
	}
	synchronized static int getThreads() {
		return activeThreadsNum;
	}
	public void run() {
		incThreads();
		try {
			for (int i = 0; i < TRIES_NUM; i++) {
				ChequeoWeb2 c2 = new ChequeoWeb2( miVerbo );
				Thread t = new Thread( c2 );
				t.start();
				long startTime = System.currentTimeMillis();
				do {
					Thread.sleep( SLEEP_TIME );
					// Debug.show( "wait de " + miVerbo + "..." );
				} while ( c2.elModelo.equals("[NO-ACABADO]") && System.currentTimeMillis() < startTime + MAX_TIME_LIMIT );
				if (!c2.elModelo.equals("[NO-ACABADO]")) {
					elModelo = c2.elModelo;
					break;
				} else {  // Too much time waiting
					t.interrupt();
				}
			}
		} catch (InterruptedException e) {
			elModelo = "";
		}
		if (miDebug) {
			if (elModelo.equals("")) {  // Verbo no existente
				Debug.show( "ERROR EN VERBO\t" + miVerbo + "\t: verbo no encontrado o web no activa" );
			} else if (elModelo.equals("[NO]")) {  // Verbo no encontrado
				Debug.show( "ERROR EN VERBO\t" + miVerbo + "\t: verbo no existente" );
			} else if (esIrregular) {
				if (!miModelo.equals(elModelo))   // Verbo irregular con modelo incorrecto
					if ("#amar#deber#vivir#".contains(elModelo))   // Verbo irregular con modelo regular
						Debug.show( "ERROR EN VERBO\t" + miVerbo + "\t: modelo\t" + elModelo + "\ten lugar de " + miModelo + "\t(regular)" );
					else   // Verbo irregular con modelo irregular diferente
						Debug.show( "ERROR EN VERBO\t" + miVerbo + "\t: modelo\t" + elModelo + "\ten lugar de\t" + miModelo );
//				else
//					Debug.show( "Correcto verbo\t" + miVerbo );
			} else {
				if (!"#amar#deber#vivir#".contains(elModelo))   // Verbo regular con modelo irregular
					Debug.show( "ERROR EN VERBO\t" + miVerbo + "\t: modelo\t" + elModelo + "\ten lugar de regular" );
//				else
//					Debug.show( "Correcto verbo\t" + miVerbo );
			}
		}
		decThreads();
	}
	
	static void comprobarVerbos( String verbo, String modelo, boolean irregular, int numMaxThreadsActivos ) {
		try {
			while (getThreads() >= numMaxThreadsActivos) Thread.sleep( SLEEP_TIME );
		} catch (InterruptedException e) { }
		// Debug.show( "comprobando " + verbo + "... (" + getThreads() + " threads activos)" );
		ChequeoWeb c = new ChequeoWeb( verbo, modelo, irregular, true );			
		Thread t = new Thread( c );
		t.start();
	}

	static String comprobarVerbo( String verbo ) {
		try {
			ChequeoWeb c = new ChequeoWeb( verbo, "", false, false );			
			Thread t = new Thread( c );
			t.start();
			t.join( TRIES_NUM * MAX_TIME_LIMIT );
			return c.elModelo;
		} catch (InterruptedException e) {
			return "";
		}
	}
	
}

