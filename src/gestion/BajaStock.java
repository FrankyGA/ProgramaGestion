package gestion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BajaStock implements  WindowListener, ActionListener {
	
	Frame ventana = new Frame ("Baja de stock");
	TextArea txaListado= new TextArea(10,67);
	
	//Creamos Choose para listado stocks
	Label lblCabecera = new Label ("Elegir el stock a Borrar:");
	Choice choStocks = new Choice();
	Button btnBorrar = new Button("Borrar");

	//Ventana para confirmar el borrado
	Dialog dlgConfirmacion = new Dialog(ventana,"Confirmación", true);
	Label lblConfirmacion = new Label("XXXXXXXXXXXXXXXXXXXXXXX");
	Button btnSi = new Button("Sí");
	Button btnNo = new Button("No");

	Dialog dlgMensaje = new Dialog(ventana, "Mensaje", true);
	Label lblMensaje = new Label("Baja correcta");

	ConexionVapers bd = new ConexionVapers();
	
	//Variable para guardar los datos
	ResultSet rs = null;
	
	int tipoUsuario;

	//Constructor de la clase
	public BajaStock(int tipoUsuario){
		
		this.tipoUsuario=tipoUsuario;
		//Listener
		ventana.addWindowListener(this);
		btnBorrar.addActionListener(this);

		//Pantalla
		ventana.setLayout(new FlowLayout());
		ventana.setBackground(Color.orange);
		
		//Parámetros de la ventana
		ventana.setSize(530,360);
		//No Permitir redimensionar
		ventana.setResizable(false);
		
		ventana.add(lblCabecera);
		
		//Conectar
		bd.conectar();
		//Sacar la información y meterla en el TextArea
		txaListado.setText(bd.consultaStock(tipoUsuario));
		//Desconectar
		bd.desconectar();
				
		ventana.add(txaListado);
		txaListado.setEditable(false);
				
		//Rellenar el Choice cabecera
		choStocks.add("Seleccionar un stock por índice...");
		// Conectar BD
		bd.conectar();
		//Sacar los datos de la tabla stocks y rellenar choice
		rs=bd.rellenarStock();
		//Registro a registro, meterlos en el Choice con formato
		try{
			//Mientras haya registros...
			while (rs.next()){
				//Añadimos al choice
				choStocks.add(rs.getInt("idTipoLiquidoTienda") + "-" + 
				"-" + rs.getString("stockLiquido") + "-" +
				"-" + rs.getInt("idTiendaFk") + "-" +
				"-" + rs.getInt("idTipoLiquidoFk"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		//Registro a registro, meteros en el choice
		//Desconectar la base de datos
		bd.desconectar();
		ventana.add(choStocks);
		choStocks.setPreferredSize(new Dimension(280, 20));
		ventana.add(btnBorrar);

		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	}
	public void actionPerformed(ActionEvent evento){
		
		//Si el usuario elige registro y pulsa en botón borrar
		if (evento.getSource().equals(btnBorrar)){
			//Si la elección del choice es igual a...
			if ((choStocks.getSelectedItem().equals("Seleccionar un stock por índice..."))){
				lblMensaje.setText("Debes seleccionar un stock");
				mostrarMensaje();
			}
			else{
				mostrarDialogoConfirmacion();
			}
		}
		//Si el usuario deniega el borrado
		else if (evento.getSource().equals(btnNo)){
			
			dlgConfirmacion.setVisible(false);
		}
		//Si el usuario confirma el borrado
		else if (evento.getSource().equals(btnSi)){
			
			bd.conectar();
			String[] array = choStocks.getSelectedItem().split("-");
			int resultado= bd.borrarStock(Integer.parseInt(array[0]), tipoUsuario);
			
			//Borrado correcto
			if (resultado == 0){
				
				lblMensaje.setText("Borrado con éxito");
				dlgConfirmacion.setVisible(false);
				mostrarMensaje();
			}
			//Error en borrado
			else{
				
				lblMensaje.setText("Error en borrado");
				mostrarMensaje();
			}
			bd.desconectar();
		}
		//Desconectar de la base

	}
	//Creamos método de borrado correcto
	private void mostrarMensaje(){
		
		dlgMensaje.setLayout(new FlowLayout());
		dlgMensaje.setSize(350,100);
		dlgMensaje.addWindowListener(this);
		dlgMensaje.add(lblMensaje);

		dlgMensaje.setLocationRelativeTo(null);
		dlgMensaje.setVisible(true);
		//lblMensaje.setText("Error");
		//dlgConfirmacion.setVisible(false);
	}
	
	//Creamos método para preguntar al usuario
	private void mostrarDialogoConfirmacion(){
		
		//Mostrar el diálogo de confirmación
		//Listeners
		dlgConfirmacion.addWindowListener(this);
		btnSi.addActionListener(this);
		btnNo.addActionListener(this);
		
		//Pantalla
		dlgConfirmacion.setLayout(new FlowLayout());
		dlgConfirmacion.setSize(450,100);
		dlgConfirmacion.add(lblConfirmacion);
		lblConfirmacion.setText("¿Estas seguro de borrar a " + choStocks.getSelectedItem()+ "?");
		dlgConfirmacion.add(btnSi);
		dlgConfirmacion.add(btnNo);

		dlgConfirmacion.setLocationRelativeTo(null);
		dlgConfirmacion.setVisible(true);
	}
	
	@Override
	public void windowClosing(WindowEvent e){

		//Si el diálogo confirmación esta activo, ese es el que hay que ocultar
		if(dlgMensaje.isActive()){
			
			dlgMensaje.setVisible(false);
		}
		//Si el diálogo mensaje esta activo, ese es el que hay que ocultar
		else if(dlgConfirmacion.isActive()){
			
			dlgConfirmacion.setVisible(false);
		}
		//Si ninguno esta activo, se oculta la ventana
		else{
			
			ventana.setVisible(false);
		}
	}
	
	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosed(WindowEvent e){}
	@Override
	public void windowIconified(WindowEvent e){}
	@Override
	public void windowDeiconified(WindowEvent e){}
	@Override
	public void windowActivated(WindowEvent e){}
	@Override
	public void windowDeactivated(WindowEvent e){}
}

