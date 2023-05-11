package gestion;

import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;



public class ConsultaTiendas implements WindowListener, ActionListener 
{
	
	Frame ventana = new Frame("Listado de Tiendas");
	TextArea txaListado = new TextArea(10,67);
	Button btnPdf = new Button("PDF");
	ConexionVapers bd = new ConexionVapers();

	int tipoUsuario;
	
	public ConsultaTiendas(int tipoUsuario){
		
		this.tipoUsuario=tipoUsuario;
		//Listener para dar funcionalidad
		ventana.addWindowListener(this);
		btnPdf.addActionListener(this);
		
		//Parámetros de la pantalla
		ventana.setSize(530, 260);
		//No Permitir redimensionar
		ventana.setResizable(false);

		ventana.setLayout(new FlowLayout());
		ventana.setBackground(Color.yellow);
		//Rellenar TextArea con la información de la BD
		//Conectar
		bd.conectar();
		//Sacar la información y meterla en el TextArea
		txaListado.setText(bd.obtenerTiendas(tipoUsuario));
		//Desconectar
		bd.desconectar();

		ventana.add(txaListado);
		txaListado.setEditable(false);
		//txaListado.setPreferredSize(new Dimension(250, 20));
		ventana.add(btnPdf);
		btnPdf.setBackground(new Color(12, 128, 128));
		
		//Fijar que la ventana salga siempre en el medio
		ventana.setLocationRelativeTo(null); 
		ventana.setVisible(true); //Mostrarla
	}

	public void windowActivated(WindowEvent we) {}
	public void windowClosed(WindowEvent we) {}
	public void windowClosing(WindowEvent we)
	{
		ventana.setVisible(false);
	}
	public void windowDeactivated(WindowEvent we) {}
	public void windowDeiconified(WindowEvent we) {}
	public void windowIconified(WindowEvent we) {}
	public void windowOpened(WindowEvent we) {}

	@Override
	public void actionPerformed(ActionEvent evento) 
	{
	//Para funcionalidad de botones
	//Para exportar datos a pdf
	}
}
