package gestion;

import java.awt.Button;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.io.font.constants.StandardFonts;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ConsultaTiendas implements WindowListener, ActionListener {

	private static final String CABECERA = "id\t\tNombre\t\tDirección";

	private static final String CONSULTA = "listadoTiendas.pdf";

	private static final String DEST_EXCEL = "listadoTiendas.xlsx";

	Frame ventana = new Frame("Listado de Tiendas");
	TextArea txaListado = new TextArea(10,67);
	Button btnPdf = new Button("PDF");
	Button btnExcel = new Button("Excel");
	ConexionVapers bd = new ConexionVapers();

	int tipoUsuario;

	public ConsultaTiendas(int tipoUsuario){

		this.tipoUsuario=tipoUsuario;
		//Listener para dar funcionalidad
		ventana.addWindowListener(this);
		btnPdf.addActionListener(this);
		btnExcel.addActionListener(this);

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

		ventana.add(btnExcel);
		btnExcel.setBackground(new Color(12, 128, 128));

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
	public void actionPerformed(ActionEvent evento) {

		//Para exportar datos a pdf
		if (evento.getSource() == btnPdf){
			try{
				//Initialize PDF writer
				PdfWriter writer = new PdfWriter(CONSULTA);
				//Initialize PDF document
				PdfDocument pdf = new PdfDocument(writer);
				// Initialize document
				Document document = new Document(pdf, PageSize.A4.rotate());
				document.setMargins(30, 30, 30, 30);//Da márgenes
				//Fuente de letra
				PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
				PdfFont bold = PdfFontFactory.createFont(StandardFonts.COURIER_BOLD);

				//Add a Paragraph
				document.add(new Paragraph("Listado de tiendas").setFont(bold).setTextAlignment(TextAlignment.CENTER).setFontSize(22f));
				Table table = new Table(UnitValue.createPercentArray(new float[]{1,3,3})).useAllAvailableWidth();

				//Reading Headers
				String line = CABECERA;
				process(table, line, bold, true);

				//Extraemos datos usando el método
				bd.conectar();
				String[] listado =bd.consultarTiendasPDF().split("\n");
				bd.desconectar();

				//Rellenamos tabla con los datos usando el método process
				for (String lineN : listado)
				{
					process(table, lineN, font, false);
				}

				//Añadimos la tabla al documento
				document.add(table);

				bd.guardarLog( tipoUsuario, "Documento PDF Consulta tiendas creado" );

				//Close document
				document.close();

				//Open the new PDF document just created
				Desktop.getDesktop().open(new File(CONSULTA));
			}
			catch (IOException ioe) {

			}
		}
		//Botón excell
		else if (evento.getSource().equals(btnExcel)) {
			try {
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("Listado de tiendas");

				// Crear la fila de encabezado
				XSSFRow headerRow = sheet.createRow(0);
				String[] headers = CABECERA.split("\t\t");
				for (int i = 0; i < headers.length; i++) {
					XSSFCell cell = headerRow.createCell(i);
					cell.setCellValue(headers[i]);
					CellStyle style = workbook.createCellStyle();
					XSSFFont font = workbook.createFont();
					font.setBold(true);
					style.setFont(font);
					cell.setCellStyle(style);
				}

				bd.conectar();
				String[] listado = bd.consultarTiendasPDF().split("\n");
				bd.desconectar();

				// Agregar los datos al archivo de Excel
				int rowNum = 1;
				for (String lineN : listado) {
					XSSFRow row = sheet.createRow(rowNum++);
					StringTokenizer tokenizer = new StringTokenizer(lineN, "\t\t");
					int colNum = 0;
					while (tokenizer.hasMoreTokens()) {
						String value = tokenizer.nextToken();
						XSSFCell cell = row.createCell(colNum++);
						cell.setCellValue(value);
					}
				}
				// Ajustar el ancho de las columnas
				for (int i = 0; i < headers.length; i++) {
					sheet.autoSizeColumn(i);
				}
				//Guardar el archivo de Excel
				FileOutputStream outputStream = new FileOutputStream(DEST_EXCEL);
				workbook.write(outputStream);
				workbook.close();
				outputStream.close();

				//Abrir el nuevo archivo de Excel
				Desktop.getDesktop().open(new File(DEST_EXCEL));
				bd.guardarLog( tipoUsuario, "Listado excel tiendas" );	
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void process(Table table, String line, PdfFont font, boolean isHeader)
	{
		StringTokenizer tokenizer = new StringTokenizer(line, "\t\t");
		while (tokenizer.hasMoreTokens())
		{
			if (isHeader)
			{
				table.addHeaderCell(new Cell().add(new Paragraph(tokenizer.nextToken()).setFont(font)));
			} else
			{
				table.addCell(new Cell().add(new Paragraph(tokenizer.nextToken()).setFont(font)));
			}
		}
	}

}
