package tests;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

/** 
 * Ejemplo de JTable con modelo de datos propio
 */
public class DemoJTable {

	// JTable
	private static JTable table;	
	// Dato de prueba con el modelo de datos propio
	private static MyTableModel datos;

	// [01] Renderers para alinear distinto que a la izquierda
	private static DefaultTableCellRenderer rendererDerecha = new DefaultTableCellRenderer();
	private static DefaultTableCellRenderer rendererCentrado = new DefaultTableCellRenderer();
	static {
		rendererDerecha.setHorizontalAlignment( JLabel.RIGHT );
		rendererCentrado.setHorizontalAlignment( JLabel.CENTER );
	}
	
	
	/**
     * Crear la ventana y mostrarla. Para seguridad de hilos,
     * este método debería invocarse desde el hilo de gestión de eventos
     */
    private static void crearYMostrarGUI() {
    	// Crear datos de prueba
    	datos = new MyTableModel( Deportista.nombresAtributos, Deportista.atributosEditables );
    	datos.add( new Deportista( "Mary", "Campione",
    	         "Snowboard", new Integer(5), new Boolean(false) ) );
    	datos.add( new Deportista( "Alison", "Huml",
    	         "Remo", new Integer(3), new Boolean(true) ) );
    	datos.add( new Deportista( "Kathy", "Walrath",
    	         "Esgrima", new Integer(2), new Boolean(false) ) );
    	datos.add( new Deportista( "Sharon", "Zakhour",
    	         "Natación", new Integer(20), new Boolean(true) ) );
    	datos.add( new Deportista( "Philip", "Milne",
    	         "Baloncesto", new Integer(10), new Boolean(false) ) );

    	// Crear JTable partiendo de modelo
        table = new JTable( datos );
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        // [01] Asignar renderers de alineación horizontal
        table.getColumn("Nombre").setCellRenderer( rendererDerecha );
        table.getColumn("Deporte").setCellRenderer( rendererCentrado );

        //Crear el scroll pane y añadirle la tabla
        JScrollPane scrollPane = new JScrollPane(table);

        //Crear e inicializar la ventana con la tabla central
        JFrame frame = new JFrame("Demo de JTable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add( scrollPane, "Center" );
        
        // Crear e inicializar la botonera
        JPanel botonera = new JPanel();
        JButton botonInsertar = new JButton( "Insertar nueva fila" );
        JButton botonBorrar = new JButton( "Borrar fila" );
        botonera.add( botonInsertar );
        botonera.add( botonBorrar );
        frame.add(botonera, "South");

        // Acciones de botones
        botonInsertar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		    	datos.add( new Deportista( "Nuevo", "Dato",
		    	         "?????", new Integer(0), new Boolean(true) ) );
				table.updateUI();
			}
        });
        botonBorrar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() >= 0 && table.getSelectedRow() < datos.getRowCount() ) {
					datos.remove( table.getSelectedRow() );
					table.updateUI();
				} else {
					JOptionPane.showMessageDialog( null, 
							"Selecciona una fila antes de borrarla", "Error en borrado", 
							JOptionPane.INFORMATION_MESSAGE );
				}
			}
        });
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Mandar trabajo a Swing
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                crearYMostrarGUI();
            }
        });
    }
}

class Deportista implements DatoParaTabla {
	public static String[] nombresAtributos = new String[] {
		"Nombre", "Apellidos", "Deporte", "Años", "Vegetariano" };
	public static boolean[] atributosEditables = new boolean[] {
		true, true, false, true, true };
	private String nombre;
	private String apellidos;
	private String deporte;
	private int anyos;
	private boolean vegetariano;
	public Deportista( String nom, String apes, String dep, int numAnyos, boolean esVegetariano ) {
		nombre = nom;
		apellidos = apes;
		deporte = dep;
		anyos = numAnyos;
		vegetariano = esVegetariano;
	}
    public int getValueCount() {
    	return 5;
    }
    public Object getValueAt(int col) {
    	switch (col) {
	    	case 0: { return nombre; }
	    	case 1: { return apellidos; }
	    	case 2: { return deporte; }
	    	case 3: { return anyos; }
	    	case 4: { return vegetariano; }
    	}
    	return null;
    }
    public void setValueAt(Object value, int col) {
    	try {
	    	switch (col) {
		    	case 0: { nombre = (String) value; }
		    	case 1: { apellidos = (String) value; }
		    	case 2: { deporte = (String) value; }
		    	case 3: { anyos = (Integer) value; }
		    	case 4: { vegetariano = (Boolean) value; }
	    	}
    	} catch (Exception e) {
    		// Error en conversión. No hacer nada
    	}
    }
}

interface DatoParaTabla {
    public int getValueCount();
    public Object getValueAt(int col);
    public void setValueAt(Object value, int col);
}

class MyTableModel extends AbstractTableModel {
    private boolean DEBUG = false;
    private ArrayList<String> columnNames;  // Nombres de columnas
    private ArrayList<DatoParaTabla> data = new ArrayList<DatoParaTabla>();
    private boolean[] editableColumns;

    public MyTableModel( String[] nombresColumnas, boolean[] colsEditables ) {
    	columnNames = new ArrayList<String>( 
    			Arrays.asList( nombresColumnas ) );
    	editableColumns = colsEditables;
    }
    
    public void add( DatoParaTabla dato ) {
    	data.add( dato );
    }

    public void remove( DatoParaTabla dato ) {
    	data.remove( dato );
    }

    public void remove( int index ) {
    	data.remove( index );
    }

    public int getColumnCount() {
        return columnNames.size();
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames.get( col );
    }

    public Object getValueAt(int row, int col) {
        return data.get(row).getValueAt(col);
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
    	if (col >= 0 && col < editableColumns.length)
    		return editableColumns[col];
    	else return false;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        if (DEBUG) {
            System.out.println("Setting value at " + row + "," + col
                               + " to " + value
                               + " (an instance of "
                               + value.getClass() + ")");
        }

        data.get(row).setValueAt( value, col );
        fireTableCellUpdated(row, col);

        if (DEBUG) {
            System.out.println("New value of data:");
            printDebugData();
        }
    }

    private void printDebugData() {
        int numRows = getRowCount();
        int numCols = getColumnCount();

        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + data.get(i).getValueAt(j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }
}

