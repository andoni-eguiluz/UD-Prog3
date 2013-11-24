package tests;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class VentanaPruebaWB extends JFrame {

	private JPanel contentPane;
	JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPruebaWB frame = new VentanaPruebaWB();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaPruebaWB() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panelSur = new JPanel();
		panelSur.setBackground(Color.RED);
		contentPane.add(panelSur, BorderLayout.SOUTH);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.append( " sdfsdfs df");
			}
		});
		panelSur.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setForeground(Color.MAGENTA);
		panelSur.add(btnCancelar);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setIcon(new ImageIcon(VentanaPruebaWB.class.getResource("/javax/swing/plaf/metal/icons/Error.gif")));
		panelSur.add(btnNewButton);
		
		textArea = new JTextArea();
		contentPane.add(textArea, BorderLayout.CENTER);
	}

}
