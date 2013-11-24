package tests.alumnos;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class VentanaSit extends JFrame {
	
	
	
	
	public static void main ( String []args) throws Exception

	{
		
		Carga.cargaAmarres();
		VentanaSit vs = VentanaSit.getVentana();
		vs.setVisible(true);
		
	}
	
	
	// Declaracion de componentes

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public static Sprite[][] dibujosPantalanes = new Sprite[4][10];
	private MiComponente comp;
	private static VentanaSit vsit;

	public static VentanaSit getVentana() {

		if (vsit == null)
			vsit = new VentanaSit();
		return vsit;
	}

	private VentanaSit() {

		// Aspecto general de la ventana y formato de componentes y contenedores

		setBounds(100, 100, 205, 400);
		setResizable(false);
		setTitle("Situacion del puerto");
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		
		
		comp = new MiComponente();
		setContentPane(comp);

	
	}

	public void dibujarPantalanes(Graphics g)
	{
		for (int i = 0; i < JTableAmarres.aAmarres.length; i++) {
			for (int j = 0; j < JTableAmarres.aAmarres[i].length; j++) {
				dibujosPantalanes[i][j] = new Sprite(2);
				dibujosPantalanes[i][j].addFrame(1, "res/VERDE.png");
				dibujosPantalanes[i][j].addFrame(2, "res/ROJO.png");
			}
		}
		int espaciox = 5;
		int espacioy = 5;
		int ancho = dibujosPantalanes[1][1].getAnchoReal();
		int alto = dibujosPantalanes[1][1].getAltoReal();

		for (int i = 0; i < JTableAmarres.aAmarres.length; i++) {
			for (int j = 0; j < JTableAmarres.aAmarres[i].length; j++) {

				dibujosPantalanes[i][j].setX(i * ancho + 32 + espaciox * i);
				dibujosPantalanes[i][j].setY(j * alto + 42 + espacioy * j);

				if (JTableAmarres.aAmarres[i][j]!=null && JTableAmarres.aAmarres[i][j].isAlquilado()) {
					dibujosPantalanes[i][j].selFrame(2);
				}

				else {
					dibujosPantalanes[i][j].selFrame(1);
				}
				dibujosPantalanes[i][j].draw(g);

				if (JTableAmarres.aAmarres[i][j]!=null)
					g.drawString(JTableAmarres.aAmarres[i][j].getPlaza(), i * ancho + 36
						+ espaciox * i, j * alto + 60 + espacioy * j);

			}

		}
		System.out.println("dibujado");
	}


	public class MiComponente extends JPanel {
		private static final long serialVersionUID = 1L;
		public MiComponente(){
			super();
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			dibujarPantalanes(g);
		}
	}
	

}
