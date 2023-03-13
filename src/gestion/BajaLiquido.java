package gestion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BajaLiquido implements WindowListener, ActionListener  
{
	//Componentes de la ventana
	Frame ventana = new Frame("Baja de l�quido");
	
	Label lblCabecera = new Label("Elegir el l�quido a Borrar:");
	
	//Creamos Choose para listado l�quido
	Choice choLiquidos = new Choice();
	Button btnBorrar = new Button("Borrar");
	Button btnCancelar = new Button("Cancelar");
	
	//Ventana para confirmar el borrado
	Dialog dlgConfirmacion = new Dialog(ventana, "Confirmaci�n", true);
	Label lblConfirmacion = new Label("XXXXXXXXXXXXXXXXXXXXXXX");
	Button btnSi = new Button("S�");
	Button btnNo = new Button("No");

	Dialog dlgMensaje = new Dialog(ventana, "Mensaje", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXX");

	ConexionVapers bd = new ConexionVapers();
	//variable para guardar los datos
	ResultSet rs = null;

	//Constructor de la clase
	public BajaLiquido()
	{
		//Listener para dar funcionalidad
		ventana.addWindowListener(this);
		btnBorrar.addActionListener(this);
		btnCancelar.addActionListener(this);

		//Par�metros de la ventana
		ventana.setSize(350, 200); 
		//No Permitir redimensionar
		ventana.setResizable(false); 

		ventana.setLayout(new FlowLayout());
		ventana.setBackground(Color.green);
		
		ventana.add(lblCabecera);
		//Rellenar Choice cabecera
		choLiquidos.add("Elegir l�quido...");
		//Conectar BD
		bd.conectar();
		//Sacar los datos de la tabla l�quidos y rellenar choice
		rs=bd.rellenarLiquido();
		//Registro a registro, meterlos en el Choice con formato
		try 
		{
			while(rs.next())
			{
				choLiquidos.add(rs.getInt("idTipoLiquido")+"-"+
				"-" + rs.getString("marcaLiquido")+ "-" + 
				"-" + rs.getString("modeloLiquido")+ "-" +
				"-" + rs.getString("capacidadLiquido"));
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Desconectar BD
		bd.desconectar();
		ventana.add(choLiquidos);
		ventana.add(btnBorrar);
		ventana.add(btnCancelar);
		//Fijar que la ventana salga siempre en el medio
		ventana.setLocationRelativeTo(null); 
		ventana.setVisible(true); // Mostrarla
	}
	public void windowActivated(WindowEvent we) {}
	public void windowClosed(WindowEvent we) {}
	//Funcionalidad de cerrar ventana
	public void windowClosing(WindowEvent we)
	{
		//Si el di�logo confirmaci�n esta activo, ese es el que hay que ocultar
		if(dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
		}
		//Si el di�logo mensaje esta activo, ese es el que hay que ocultar
		else if(dlgConfirmacion.isActive())
		{
			dlgConfirmacion.setVisible(false);
		}
		//Si ninguno esta activo, se oculta la ventana
		else
		{
			ventana.setVisible(false);
		}
	}
	public void windowDeactivated(WindowEvent we) {}
	public void windowDeiconified(WindowEvent we) {}
	public void windowIconified(WindowEvent we) {}
	public void windowOpened(WindowEvent we) {}

	@Override
	public void actionPerformed(ActionEvent evento) 
	{
		//Si hemos pulsado el bot�n borrar...
		if(evento.getSource().equals(btnBorrar))
		{
			//Mostrar el di�logo de confirmaci�n
			//Listener para dar funcionalidad
			dlgConfirmacion.addWindowListener(this);
			btnSi.addActionListener(this);
			btnNo.addActionListener(this);

			//Par�metros de la pantalla
			dlgConfirmacion.setSize(450, 100); 
			//No Permitir redimensionar
			dlgConfirmacion.setResizable(false); 

			dlgConfirmacion.setLayout(new FlowLayout());
			
			//Pregunta de confirmaci�n del l�quido seleccionado
			lblConfirmacion.setText("�Borrar el l�quido "+
			choLiquidos.getSelectedItem()+"?");
			
			dlgConfirmacion.add(lblConfirmacion);
			dlgConfirmacion.add(btnSi);
			dlgConfirmacion.add(btnNo);
			//Fijar que la ventana salga siempre en el medio
			dlgConfirmacion.setLocationRelativeTo(null); 
			dlgConfirmacion.setVisible(true);
		}
		if(evento.getSource().equals(btnCancelar))
		{
			ventana.setVisible(false);
		}
		//Si pulsamos en no...
		else if(evento.getSource().equals(btnNo))
		{
			dlgConfirmacion.setVisible(false);
		}
		//Si pulsamos en si...
		else if(evento.getSource().equals(btnSi))
		{
			//Conectamos a la BD
			bd.conectar();
			//Hacer el DELETE en la tabla guardando los datos en el array
			String[] registros = choLiquidos.getSelectedItem().split("-");
			//Borrar los datos de ese registro por posici�n(id) usando el m�todo
			int resultado = bd.borrarLiquido(Integer.parseInt(registros[0]));
			//Si el resultado es 0, borrado con �xito
			if(resultado==0)
			{
				lblMensaje.setText("Borrado con �xito");
			}
			//Sino, error
			else
			{
				lblMensaje.setText("Error en borrado");
			}
			// Desconectar
			bd.desconectar();

			dlgMensaje.addWindowListener(this);

			//Par�metros de la pantalla
			dlgMensaje.setSize(350, 100);
			//No Permitir redimensionar
			dlgMensaje.setResizable(false);

			dlgMensaje.setLayout(new FlowLayout());
			dlgMensaje.add(lblMensaje);
			//Fijar que la ventana salga siempre en el medio
			dlgMensaje.setLocationRelativeTo(null);
			dlgMensaje.setVisible(true);
			dlgConfirmacion.setVisible(false);
		}
	}
}
