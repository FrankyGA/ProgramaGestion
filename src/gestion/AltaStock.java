package gestion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/*La clase "AltaStock" representa una interfaz gráfica de usuario para agregar información 
 * de stock relacionada con tiendas y líquidos a una base de datos. 
 * Proporciona interacciones con el usuario a través de componentes gráficos y realiza consultas
 *  y actualizaciones en la base de datos según las acciones del usuario.*/

public class AltaStock implements WindowListener, ActionListener
{
	Frame ventana = new Frame("Nuevo Stock");
	
	Label lblStock = new Label("Stock");
	TextField txtStock = new TextField(30);
	
	Choice choTiendas = new Choice();
	Choice choLiquidos = new Choice();
	
	Label lblChoiceTienda = new Label("Elige una tienda");
	Label lblChoiceLiquido = new Label("Elige un líquido");
	
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	
	Dialog dlgMensaje = new Dialog(ventana, "Mensaje", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXXX");
	
	ConexionVapers bd = new ConexionVapers();
	ResultSet rs = null;
	
	int idTiendaFk;
	int idTipoLiquidoFk;
	int stockLiquido;
	
	int tipoUsuario;

	/*Este constructor inicializa los componentes de la interfaz gráfica, 
	 * establece sus propiedades y agrega controladores de eventos.*/
	public AltaStock(int tipoUsuario){
		
		this.tipoUsuario=tipoUsuario;
		
		ventana.setLayout(new FlowLayout());
		ventana.setBackground(Color.orange);
		ventana.addWindowListener(this);
		
		choTiendas.setPreferredSize(new Dimension(250, 20));
		choLiquidos.setPreferredSize(new Dimension(250, 20));
		
		ventana.add(lblStock);
		ventana.add(txtStock);
		
		bd.conectar();
		
		//-------------------------Sacar los datos de la tabla tiendas----------------------------//
		ventana.add(lblChoiceTienda);
		ventana.add(choTiendas);
		
		rs = bd.rellenarTienda();
		
		//Meter los datos en el Choice separados con guiones
		try{
			while(rs.next()){
				choTiendas.add(rs.getInt("idTienda")
				+ "-" + rs.getString("nombreTienda")
				+ "-" + rs.getString("direccionTienda"));
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		//-------------------------Sacar los datos de la tabla líquidos----------------------------//
		ventana.add(lblChoiceLiquido);
		ventana.add(choLiquidos);
		
		rs = bd.rellenarLiquido();
		//Meter los datos en el Choice separados con guiones
		try{
			while(rs.next()){
				choLiquidos.add(rs.getInt("idTipoLiquido")
				+ "-" + rs.getString("marcaLiquido")
				+ "-" + rs.getString("modeloLiquido")
				+ "-" + rs.getString("capacidadLiquido"));
			}
		} 
		
		catch (SQLException e){
			e.printStackTrace();
		}
		
		bd.desconectar();
		
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		
		ventana.add(btnAceptar);
		ventana.add(btnLimpiar);
		
		ventana.setSize(300,300);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
		ventana.setResizable(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent evento){
		
		//Si pulsamos en limpiar, limpiamos campos
		if(evento.getSource().equals(btnLimpiar)){
			limpiar();
		}
		//Si pulsamos en aceptar
		else if(evento.getSource().equals(btnAceptar)){
			bd.conectar();
				//Metemos los datos en un array sin el guión
				String[] liquidos = choLiquidos.getSelectedItem().split("-");
				//Sacamos el índice
				idTipoLiquidoFk = Integer.parseInt(liquidos[0]);
				//Metemos los datos en un array sin el guión
				String[] tiendas = choTiendas.getSelectedItem().split("-");
				//Sacamos el índice
				idTiendaFk = Integer.parseInt(tiendas[0]);
				
				//Sacamos datos de número de stock introducido
				String stockLiquidoString = txtStock.getText();
				stockLiquido = Integer.parseInt(stockLiquidoString);
				//Aplicamos método de insertar con los datos
				int resultado = bd.insertarStock(stockLiquido, idTipoLiquidoFk, idTiendaFk, tipoUsuario);
				
			if(txtStock.getText().length()==0) {
				lblMensaje.setText("El campo está vacio");
			}
			else if(resultado == 0)
			{
				limpiar();
				lblMensaje.setText("Alta correcta");
				mostrarDialogo();
			}
			else
			{
				lblMensaje.setText("Error en Alta");
			}
			mostrarDialogo();
			bd.desconectar();
		}
	}
	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		if(dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
			limpiar();
		}
		else
		{
			ventana.setVisible(false);
		}
	}
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
	
	//Método para lanzar diálogo
	public void mostrarDialogo()
	{
		dlgMensaje.setLayout(new FlowLayout());
		dlgMensaje.addWindowListener(this);
		dlgMensaje.add(lblMensaje);
		dlgMensaje.setSize(150,80);
		dlgMensaje.setResizable(false);
		dlgMensaje.setLocationRelativeTo(null);
		dlgMensaje.setVisible(true);
	}
	
	//Método para limpiar campos
	public void limpiar()
	{
		txtStock.setText("");
		txtStock.requestFocus();
	}
}

/*String stockLiquidoString = txtStock.getText();
int stockLiquido = Integer.parseInt(stockLiquidoString);

String sentencia = "INSERT INTO tipoliquidotienda VALUES(null, " 
          + stockLiquido + ", "
          + idTipoLiquidoFk + ", "
          + idTiendaFk + ");";
System.out.println(sentencia);

int resultado = bd.insertarStock(sentencia,  tipoUsuario);*/
