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

public class ConsultaLiquidos implements WindowListener, ActionListener 
{
	
	Frame ventana = new Frame("Listado de Líquidos");
	TextArea txaListado = new TextArea(10,40);
	Button btnPdf = new Button("PDF");
	ConexionVapers bd = new ConexionVapers();

	public ConsultaLiquidos()
	{
		//Listener para dar funcionalidad
		ventana.addWindowListener(this);
		//Parámetros de la pantalla
		ventana.setSize(340, 260); 
		//No Permitir redimensionar
		ventana.setResizable(false);

		ventana.setLayout(new FlowLayout());
		ventana.setBackground(Color.green);
		//Rellenar TextArea con la información de la BD
		//Conectar
		bd.conectar();
		//Sacar la información y meterla en el TextArea
		txaListado.setText(bd.obtenerLiquidos());
		//Desconectar
		bd.desconectar();
		txaListado.setEditable(false);
		ventana.add(txaListado);
		ventana.add(btnPdf);
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
