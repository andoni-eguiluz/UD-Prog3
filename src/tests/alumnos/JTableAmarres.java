package tests.alumnos;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class JTableAmarres extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	// Modelo de datos propio
	static MiTableModel datos;
	static Amarre [][] aAmarres = new Amarre [4][10];

	// [02] Renderers para alinear distinto que a la izquierda
	private static DefaultTableCellRenderer rendererDerecha = new DefaultTableCellRenderer();
	private static DefaultTableCellRenderer rendererCentrado = new DefaultTableCellRenderer();
	static {
		rendererDerecha.setHorizontalAlignment( JLabel.RIGHT );
		rendererCentrado.setHorizontalAlignment( JLabel.CENTER );
	}

	public JTableAmarres( MiTableModel modelo ) {
		super(modelo);
		
		
    	// Fijar tamano preferido de la tabla

		
        setPreferredScrollableViewportSize(new Dimension(1200, 250));
        
        // [02] Asignar renderers de alineacion horizontal
        
        getColumn("Plaza").setCellRenderer( rendererCentrado );
        getColumn("Precio").setCellRenderer( rendererCentrado );
        // [03] Asignar anchuras iniciales
        
        
		getColumn( "Pantalan" ).setMinWidth( 50 );
		getColumn( "Plaza" ).setMinWidth( 50 );
		
		getColumn( "Situacion" ).setPreferredWidth( 50 );
		
	}
	
	public void setModel( TableModel t ) {
		if (t instanceof MiTableModel)
			super.setModel( t );
		else
			throw new IncompatibleClassChangeError( 
				"No se puede asignar cualquier modelo de datos a esta tabla" );
	}
	
	public MiTableModel getMiTableModel() {
		return (MiTableModel) getModel();
	}
	


}

	

interface DatoParaTabla {
    public int getNumColumnas();  // Numero de columnas (campos del dato)
    public Object getValor(int col);  // Devolver valor de columna col
    public void setValor(Object value, int col);  // Asignar valor a columna col
}

// [01] Clase que implementa un modelo de datos para usar en JTables
class MiTableModel extends AbstractTableModel {
    // Lista principal de datos del modelo:
   ArrayList<DatoParaTabla> datos = new ArrayList<DatoParaTabla>();
    private String[] nombresColumnas;  // Nombres de columnas
    private boolean[] columnasEditables;  // Columnas editables o no
	private static final long serialVersionUID = 7026825539532562011L;
	private boolean DEBUG = true;

    /** Constructor de modelo de tabla
     * @param nombresColumnas	Nombres de las columnas
     * @param colsEditables	Array de valores logicos si las columnas son editables (true) o no (false)
     */
    public MiTableModel( String[] nombresColumnas, boolean[] colsEditables ) {
    	this.nombresColumnas = nombresColumnas;
    	this.columnasEditables = colsEditables;
    }
    
    /** Anade un dato al final del modelo
     * @param dato	Dato a anadir
     */
    public void insertar( int index ,DatoParaTabla dato ) {
    	datos.add( index,dato );
    }
    
    
    public void insertarInicial(DatoParaTabla dato ) {
    	datos.add(dato );
    }
    

    
    public DatoParaTabla get (int ind)
    {
    	
    return datos.get(ind);	
    	
    }

    /** Elimina un dato del modelo
     * @param dato	Dato a borrar
     */
    public void borrar( DatoParaTabla dato ) {
    	datos.remove( dato );
    }

    /** Elimina un dato del modelo, indicado por su posicion
     * @param ind	Posicion del dato a borrar
     */
    public void borrar( int ind ) {
    	datos.remove( ind );
    }

    /* [01] MODELO: Devuelve el numero de columnas
     */
    public int getColumnCount() {
        return nombresColumnas.length;
    }

    /* [01] MODELO: Devuelve el numero de filas
     */
    public int getRowCount() {
        return datos.size();
    }

    /* [01] MODELO: Devuelve el nombre de la columna
     */
    public String getColumnName(int col) {
        return nombresColumnas[col];
    }

    /* [01] MODELO: Devuelve el valor de la celda indicada
     */
    public Object getValueAt(int row, int col) {
        return datos.get(row).getValor(col);
    }

    /* [01] MODELO: Este metodo devuelve el renderer/editor por defecto
     * para cada celda, identificado por la columna. Si no cambiaramos
     * este metodo la ultima columna se veria como un String en lugar
     * de un checkbox (renderer/editor por defecto para Boolean)
     */
    public Class getColumnClass(int c) {
    	if (datos.size()==0) return String.class;  // por defecto String
    	if (datos.get(0)==null) return String.class;
        return datos.get(0).getValor(c).getClass();  // Si hay datos, la clase correspondiente
    }

    /* [01] MODELO: Si la tabla es editable, este metodo identifica
     * que celdas pueden editarse
     */
    public boolean isCellEditable(int row, int col) {
    	if (col >= 0 && col < columnasEditables.length)
    		return columnasEditables[col];
    	else return false;
    }

    /* [01] MODELO: Metodo que solo hay que implementar si la tabla
     * puede cambiar (editar) valores de sus celdas
     */
    public void setValueAt(Object value, int row, int col) {
        if (DEBUG) {
            System.out.println("Poniendo valor en (" + row + "," + col
                               + ") = " + value + " (instancia de "
                               + value.getClass() + ")");
        }
        datos.get(row).setValor( value, col );
        fireTableCellUpdated(row, col);  // Notifica a escuchadores de cambio de celda
    }

}



