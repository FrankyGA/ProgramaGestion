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
	
	Dialog dlgFeedback = new Dialog(ventana, "Mensaje", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXXX");
	
	ConexionVapers bd = new ConexionVapers();
	ResultSet rs = null;
	
	int idTiendaFk;
	int idTipoLiquidoFk;
	int stockLiquido;
	
	int tipoUsuario;

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
		
		ventana.add(lblChoiceTienda);
		
		ventana.add(choTiendas);
		//Sacar los datos de la tabla tiendas
		rs = bd.rellenarTienda();
		
		//Meterl en el Choice
		try
		{
			while(rs.next())
			{
				choTiendas.add(rs.getInt("idTienda")
						+ "-" + rs.getString("nombreTienda")
						+ "-" + rs.getString("direccionTienda"));

			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		ventana.add(lblChoiceLiquido);
		ventana.add(choLiquidos);
		//Sacar los datos de la tabla líquidos
		rs = bd.rellenarLiquido();
		//Meterlo en el Choice
		try
		{
			while(rs.next())
			{
				choLiquidos.add(rs.getInt("idTipoLiquido")
						+ "-" + rs.getString("marcaLiquido")
						+ "-" + rs.getString("modeloLiquido")
						+ "-" + rs.getString("capacidadLiquido"));

			}
		} catch (SQLException e)
		{
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
	public void actionPerformed(ActionEvent evento)
	{
		if(evento.getSource().equals(btnLimpiar))
		{
			limpiar();
		}
		else if(evento.getSource().equals(btnAceptar))
		{
			bd.conectar();
			
			
				String[] liquidos = choLiquidos.getSelectedItem().split("-");
				idTipoLiquidoFk = Integer.parseInt(liquidos[0]);
				String[] tiendas = choTiendas.getSelectedItem().split("-");
				idTiendaFk = Integer.parseInt(tiendas[0]);
			
				
				String stockLiquidoString = txtStock.getText();
				stockLiquido = Integer.parseInt(stockLiquidoString);
				int resultado = bd.insertarStock(stockLiquido, idTipoLiquidoFk, idTiendaFk, tipoUsuario);
				
				/*String stockLiquidoString = txtStock.getText();
				int stockLiquido = Integer.parseInt(stockLiquidoString);
			
				String sentencia = "INSERT INTO tipoliquidotienda VALUES(null, " 
		                  + stockLiquido + ", "
		                  + idTipoLiquidoFk + ", "
		                  + idTiendaFk + ");";
				System.out.println(sentencia);
			
				int resultado = bd.insertarStock(sentencia,  tipoUsuario);*/
			
					
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
	public void windowOpened(WindowEvent e)
	{
		// TODO Auto-generated method stub
	}
	@Override
	public void windowClosing(WindowEvent e)
	{
		if(dlgFeedback.isActive())
		{
			dlgFeedback.setVisible(false);
		}
		else
		{
			ventana.setVisible(false);
		}
	}
	@Override
	public void windowClosed(WindowEvent e)
	{
		// TODO Auto-generated method stub
	}
	@Override
	public void windowIconified(WindowEvent e)
	{
		// TODO Auto-generated method stub
	}
	@Override
	public void windowDeiconified(WindowEvent e)
	{
		// TODO Auto-generated method stub
	}
	@Override
	public void windowActivated(WindowEvent e)
	{
		// TODO Auto-generated method stub
	}
	@Override

	public void windowDeactivated(WindowEvent e)
	{
		// TODO Auto-generated method stub
	}
	public void mostrarDialogo()
	{
		dlgFeedback.setLayout(new FlowLayout());
		dlgFeedback.addWindowListener(this);
		dlgFeedback.add(lblMensaje);
		dlgFeedback.setSize(150,80);
		dlgFeedback.setResizable(false);
		dlgFeedback.setLocationRelativeTo(null);
		dlgFeedback.setVisible(true);
	}
	public void limpiar()
	{
		txtStock.setText("");
		txtStock.requestFocus();
	}

}
