package tests;

import java.awt.BorderLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;

public class PruebasVarias {

	public static void main( String[] s ) {
		// No hace falta porque se crea con ellos:  c.setTimeInMillis( System.currentTimeMillis() ); 
		JFrame f = new JFrame();
		JTextField fechaI = new JTextField(20);
		// No (manera antigua) fechaI.setText( d.getDay() + "/" + d.getMonth() + "/" + d.getYear() );
		// Construida a mano:
		// fechaI.setText( c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR) );
		// Pero mejor todavía con DateFormat:
		DateFormat df = new SimpleDateFormat( "d/M/y h:m" ); // DateFormat.getDateInstance( DateFormat.SHORT );
		fechaI.setText( df.format( new Date() ));
		Date d = null;
		try {
			d = df.parse( "31/5/2010 22:15" );
		} catch (ParseException e) {
		}
		fechaI.setText( df.format( d ));
		f.add( fechaI );
		f.getContentPane().add( fechaI, BorderLayout.NORTH );
		JList l = new JList( new String [] { "aaaa", "bbbb", "cccc"} );
		f.getContentPane().add( l, BorderLayout.CENTER );
		f.pack();
		// f.setEnabled( false );
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		f.setVisible( true );
		try { Thread.sleep( 2000 ); } catch (Exception e) {}
		l.setSelectedIndex(0);
		try { Thread.sleep( 2000 ); } catch (Exception e) {}
		// l.setSelectedValue( null, false );
		l.setSelectedIndices( new int[] {} );
	}
}
