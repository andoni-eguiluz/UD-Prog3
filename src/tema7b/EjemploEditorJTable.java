package tema7b;

// Basado en código de java2s:
// http://www.java2s.com/Code/JavaAPI/javax.swing.table/implementsTableCellEditor.htm

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class EjemploEditorJTable extends JFrame {

  public EjemploEditorJTable() {
    super("Customer Editor Test");
    setSize(600, 160);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    MixerModel test = new MixerModel();
    JTable jt = new JTable(test);
    jt.setDefaultRenderer(Volume.class, new VolumeRenderer());
    jt.setDefaultEditor(Volume.class, new VolumeEditor());
    JScrollPane jsp = new JScrollPane(jt);
    getContentPane().add(jsp, BorderLayout.CENTER);
  }

  public static void main(String args[]) {
	EjemploEditorJTable v = new EjemploEditorJTable();
    v.setVisible(true);
  }
}

class MixerModel extends AbstractTableModel {

  String headers[] = { "Track", "Start", "Stop", "Left Volume", "Right Volume" };

  Class columnClasses[] = { String.class, String.class, String.class, Volume.class, Volume.class };

  Object data[][] = { { "Bass", "0:00:000", "1:00:000", new Volume(56), new Volume(56) },
      { "Strings", "0:00:000", "0:52:010", new Volume(72), new Volume(52) },
      { "Brass", "0:08:000", "1:00:000", new Volume(99), new Volume(0) },
      { "Wind", "0:08:000", "1:00:000", new Volume(0), new Volume(99) }, };

  public int getRowCount() {
    return data.length;
  }

  public int getColumnCount() {
    return headers.length;
  }

  public Class getColumnClass(int c) {
    return columnClasses[c];
  }

  public String getColumnName(int c) {
    return headers[c];
  }

  public boolean isCellEditable(int r, int c) {
    return true;
  }

  public Object getValueAt(int r, int c) {
    return data[r][c];
  }

  public void setValueAt(Object value, int r, int c) {
    if (c >= 3) {
      ((Volume) data[r][c]).setVolume(value);
    } else {
      data[r][c] = value;
    }
  }
}

class Volume {
  private int volume;

  public Volume(int v) {
    setVolume(v);
  }

  public Volume() {
    this(50);
  }

  public void setVolume(int v) {
    volume = (v < 0 ? 0 : v > 100 ? 100 : v);
  }

  public void setVolume(Object v) {
    if (v instanceof String) {
      setVolume(Integer.parseInt((String) v));
    } else if (v instanceof Number) {
      setVolume(((Number) v).intValue());
    } else if (v instanceof Volume) {
      setVolume(((Volume) v).getVolume());
    }
  }

  public int getVolume() {
    return volume;
  }

  public String toString() {
    return String.valueOf(volume);
  }
}

class VolumeEditor extends JSlider implements TableCellEditor {

  public OkCancel helper = new OkCancel();
  protected transient Vector listeners;
  protected transient int originalValue;
  protected transient boolean editing;

  public VolumeEditor() {
    super(SwingConstants.HORIZONTAL);
    listeners = new Vector();
  }

  public class OkCancel extends JWindow {
    private JButton okB = new JButton("ok");
    private JButton cancelB = new JButton("no");
    private int w = 100;
    private int h = 24;
    public OkCancel() {
      setSize(w, h);
      setBackground(Color.yellow);
      JPanel p = new JPanel(new GridLayout(1,2));
      p.add(okB);
      p.add(cancelB);
      setContentPane(p);

      okB.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          stopCellEditing();
        }
      });

      cancelB.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          cancelCellEditing();
        }
      });
    }
  }

  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
      int row, int column) {
    if (value == null) {
      return this;
    }
    if (value instanceof Volume) {
      setValue(((Volume) value).getVolume());
    } else {
      setValue(0);
    }
    table.setRowSelectionInterval(row, row);
    table.setColumnSelectionInterval(column, column);
    originalValue = getValue();
    editing = true;
    Point p = table.getLocationOnScreen();
    Rectangle r = table.getCellRect(row, column, true);
    helper.setLocation(r.x + p.x + getWidth() - 50, r.y + p.y + getHeight());
    helper.setVisible(true);
    return this;
  }

  public void cancelCellEditing() {
    fireEditingCanceled();
    editing = false;
    helper.setVisible(false);
  }

  public Object getCellEditorValue() {
    return new Integer(getValue());
  }

  public boolean isCellEditable(EventObject eo) {
    return true;
  }

  public boolean shouldSelectCell(EventObject eo) {
    return true;
  }

  public boolean stopCellEditing() {
    fireEditingStopped();
    editing = false;
    helper.setVisible(false);
    return true;
  }

  public void addCellEditorListener(CellEditorListener cel) {
    listeners.addElement(cel);
  }

  public void removeCellEditorListener(CellEditorListener cel) {
    listeners.removeElement(cel);
  }

  protected void fireEditingCanceled() {
    setValue(originalValue);
    ChangeEvent ce = new ChangeEvent(this);
    for (int i = listeners.size() - 1; i >= 0; i--) {
      ((CellEditorListener) listeners.elementAt(i)).editingCanceled(ce);
    }
  }

  protected void fireEditingStopped() {
    ChangeEvent ce = new ChangeEvent(this);
    for (int i = listeners.size() - 1; i >= 0; i--) {
      ((CellEditorListener) listeners.elementAt(i)).editingStopped(ce);
    }
  }
}

class VolumeRenderer extends JSlider implements TableCellRenderer {

  public VolumeRenderer() {
    super(SwingConstants.HORIZONTAL);
    setSize(115, 15);
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
      boolean hasFocus, int row, int column) {
    if (value == null) {
      return this;
    }
    if (value instanceof Volume) {
      setValue(((Volume) value).getVolume());
    } else {
      setValue(0);
    }
    return this;
  }
}