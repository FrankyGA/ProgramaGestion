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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConsultaStock implements WindowListener, ActionListener {
	
	Frame ventana= new Frame ("Stock de las tiendas");
	TextArea txaListado= new TextArea(10,67);
	Button btnPdf= new Button("Exportar a PDF");
	
	ConexionVapers bd= new ConexionVapers();
	
	Connection connection = null;
	Statement statement = null;
	ResultSet resultSet = null;
	int tipoUsuario;
	
	public ConsultaStock(int tipoUsuario)
	{
		
		this.tipoUsuario=tipoUsuario;
		//btnPdf.addActionListener(this);
		
		//Listener
		ventana.addWindowListener(this);
		//Pantalla
		ventana.setSize(530, 260); // Ancho y altura
		ventana.setResizable(false);// no permitir redimensionar
		
		ventana.setLayout(new FlowLayout());
		//Rellenar el TextArea con la información de la base de datos
		ventana.setBackground(Color.orange);
		
		//Conectar
		bd.conectar();
		//Sacar la información y meterla en el TextArea
		txaListado.setText(bd.consultaStock(tipoUsuario));
		//Desconectar
		bd.desconectar();
		
		ventana.add(txaListado);
		txaListado.setEditable(false);
		//txaListado.setPreferredSize(new Dimension(250, 20));
		ventana.add(btnPdf);
		btnPdf.setBackground(new Color(12, 128, 128));
		//txaListado.setColumns(0);
		
		ventana.setLocationRelativeTo(null);//fijar que la ventana salga
		ventana.setVisible(true);//mostrar la ventana
	}
	@Override
	public void actionPerformed(ActionEvent e) {}
	@Override
	public void windowOpened(WindowEvent e) {}
	@Override
	public void windowClosing(WindowEvent e) {
		ventana.setVisible(false);	
	}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}
}
