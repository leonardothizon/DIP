package view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import controle.PDI;
import controle.Pixel;
import controle.RegiaoSegmento;
import controle.TipoDilatacao;
import controle.TipoFiltroRuido;
import controle.TipoVizinhanca;

public class Aplicacao extends Shell {
	private CLabel cLblImagem1;
	private CLabel cLblImagem2;
	private CLabel cLblImagemResultado;
	private Button btnImage1;
	private Button btnImage2;
	private Composite compositeCor;
	private Label lblPontoMouse;
	private Label lblCorG;
	private Label lblCorR;
	private Label lblCorB;

	private FileDialog fileDialog;
	private Image image1, image2, image3;
	private String pathImage1, pathImage2;
	private String hex;
	private Label lblHex;
	private Text txtCinzaR;
	private Text txtCinzaG;
	private Text txtCinzaB;
	private Text txtNumFaixasCinza;
	private Button btnGerarNegativa;
	private Button btnQuadrado;
	private Button btnCruz;
	private Button btnX;
	private Button btnEliminarRuido;
	private Button btnMedia;
	private Button btnMediana;
	
	private Pixel inicioDragImage1, fimDragImage1;
	private Button btnAumentarImagem;
	private Button btnRotacionarImagem;
	private Spinner spinnerImg1;
	private Spinner spinnerImg2;
	private Button btnAdicao;
	private Button btnSubtracao;
	private Button btnGerarHistograma;
	private Spinner spinnerTransparencia;
	private Button btnEqualizao;
	private Button btnDilatacao;
	private Button btnDilCrculo;
	private Button btnDilLosango;
	private Button btnDilCruz;
	private Spinner spinnerLimiarErosao;
	private Label labelMedia;
	private Text textThresh;
	private Text textQuad1;
	private Text textQuad2;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			Aplicacao shell = new Aplicacao(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public Aplicacao(Display display) {
		super(display, SWT.SHELL_TRIM);

		Group grpImagem = new Group(this, SWT.NONE);
		grpImagem.setText("Imagem 1");
		grpImagem.setBounds(142, 150, 232, 283);

		ScrolledComposite scrolledComposite = new ScrolledComposite(grpImagem, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(10, 60, 212, 215);

		cLblImagem1 = new CLabel(scrolledComposite, SWT.NONE);
		cLblImagem1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				lblHex.setText(hex);
				inicioDragImage1 = encontrarPixel(e.x, e.y, image1.getImageData());				
			}
		});
		cLblImagem1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					fimDragImage1 = encontrarPixel(e.x, e.y, image1.getImageData());
					if(inicioDragImage1 != null) {
						BufferedImage cinzaSimples = new PDI().desenharRetangulo(ImageIO.read(new File(pathImage1)), inicioDragImage1, fimDragImage1);
						ImageIO.write(cinzaSimples, "jpg", new File("imgs/_retangulo.jpg"));
						image3 = new Image(null, "imgs/_retangulo.jpg");
						exibeImagem(cLblImagemResultado, image3);
						inicioDragImage1 = null;
						fimDragImage1 = null;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		cLblImagem1.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (image1 != null)
					encontrarPixel(e.x, e.y, image1.getImageData());
			}
		});
		cLblImagem1.setText("");
		cLblImagem1.setBounds(0, 0, 40, 40);
		scrolledComposite.setContent(cLblImagem1);

		btnImage1 = new Button(grpImagem, SWT.NONE);
		btnImage1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				pathImage1 = selecionarCaminhoImagem();
				image1 = abrirImagem(pathImage1);
				exibeImagem(cLblImagem1, image1);

			}
		});
		btnImage1.setBounds(10, 23, 212, 25);
		btnImage1.setText("Adicionar Imagem");

		Group grpImagem_1 = new Group(this, SWT.NONE);
		grpImagem_1.setText("Imagem 2");
		grpImagem_1.setBounds(380, 150, 232, 283);

		ScrolledComposite scrolledComposite_1 = new ScrolledComposite(grpImagem_1, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1.setBounds(10, 60, 212, 215);

		cLblImagem2 = new CLabel(scrolledComposite_1, SWT.NONE);
		cLblImagem2.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (image2 != null)
					encontrarPixel(e.x, e.y, image2.getImageData());
			}
		});
		cLblImagem2.setText("");
		cLblImagem2.setBounds(0, 0, 40, 40);
		scrolledComposite_1.setContent(cLblImagem2);

		btnImage2 = new Button(grpImagem_1, SWT.NONE);
		btnImage2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				pathImage2 = selecionarCaminhoImagem();
				image2 = abrirImagem(pathImage2);
				exibeImagem(cLblImagem2, image2);

			}
		});
		btnImage2.setText("Adicionar Imagem");
		btnImage2.setBounds(10, 23, 212, 25);

		Group grpResultado = new Group(this, SWT.NONE);
		grpResultado.setText("Resultado");
		grpResultado.setBounds(618, 150, 506, 455);

		ScrolledComposite scrolledComposite_2 = new ScrolledComposite(grpResultado, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_2.setBounds(10, 24, 486, 421);

		cLblImagemResultado = new CLabel(scrolledComposite_2, SWT.NONE);
		cLblImagemResultado.setText("");
		cLblImagemResultado.setBounds(0, 0, 40, 40);
		scrolledComposite_2.setContent(cLblImagemResultado);

		Label lblR = new Label(this, SWT.NONE);
		lblR.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblR.setAlignment(SWT.CENTER);
		lblR.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		lblR.setBounds(10, 260, 126, 25);
		lblR.setText("R");

		Label lblG = new Label(this, SWT.NONE);
		lblG.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		lblG.setText("G");
		lblG.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		lblG.setAlignment(SWT.CENTER);
		lblG.setBounds(10, 310, 126, 25);

		Label lblB = new Label(this, SWT.NONE);
		lblB.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		lblB.setText("B");
		lblB.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		lblB.setAlignment(SWT.CENTER);
		lblB.setBounds(10, 360, 126, 25);

		compositeCor = new Composite(this, SWT.BORDER);
		compositeCor.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		compositeCor.setBounds(50, 167, 43, 30);

		lblPontoMouse = new Label(this, SWT.NONE);
		lblPontoMouse.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		lblPontoMouse.setAlignment(SWT.CENTER);
		lblPontoMouse.setBounds(10, 204, 126, 25);
		lblPontoMouse.setText("320 x 598");

		lblCorR = new Label(this, SWT.NONE);
		lblCorR.setAlignment(SWT.CENTER);
		lblCorR.setBounds(10, 289, 126, 25);

		lblCorG = new Label(this, SWT.NONE);
		lblCorG.setAlignment(SWT.CENTER);
		lblCorG.setBounds(10, 339, 126, 25);

		lblCorB = new Label(this, SWT.NONE);
		lblCorB.setAlignment(SWT.CENTER);
		lblCorB.setBounds(10, 387, 126, 25);

		lblHex = new Label(this, SWT.NONE);
		lblHex.setAlignment(SWT.CENTER);
		lblHex.setBounds(10, 235, 126, 15);

		CTabFolder tabFolder = new CTabFolder(this, SWT.BORDER);
		tabFolder.setBounds(43, 10, 1081, 134);
		tabFolder.setSelectionBackground(
				Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem tbtmEscalaDeCinza = new CTabItem(tabFolder, SWT.NONE);
		tbtmEscalaDeCinza.setText("Escala de Cinza");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmEscalaDeCinza.setControl(composite);

		Button btnMediaAritmetica = new Button(composite, SWT.NONE);
		btnMediaAritmetica.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					BufferedImage cinzaSimples = new PDI().cinzaSimples(ImageIO.read(new File(pathImage1)), null);
					ImageIO.write(cinzaSimples, "jpg", new File("imgs/_cinzaSimples.jpg"));
					image3 = new Image(null, "imgs/_cinzaSimples.jpg");
					exibeImagem(cLblImagemResultado, image3);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnMediaAritmetica.setBounds(30, 26, 173, 62);
		btnMediaAritmetica.setText("M\u00E9dia Aritm\u00E9tica");

		txtCinzaR = new Text(composite, SWT.BORDER);
		txtCinzaR.setBounds(226, 26, 33, 21);

		txtCinzaG = new Text(composite, SWT.BORDER);
		txtCinzaG.setBounds(282, 26, 33, 21);

		txtCinzaB = new Text(composite, SWT.BORDER);
		txtCinzaB.setBounds(338, 26, 33, 21);

		Button btnMediaPonderada = new Button(composite, SWT.NONE);
		btnMediaPonderada.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					Double R = Double.parseDouble(txtCinzaR.getText());
					Double G = Double.parseDouble(txtCinzaG.getText());
					Double B = Double.parseDouble(txtCinzaB.getText());

					if ((R + G + B) != 100) {
						throw new Exception("A soma dos valores deve ser 100");
					}

					BufferedImage cinzaSimples = new PDI().cinzaSimples(ImageIO.read(new File(pathImage1)),
							new Double[] { R, G, B });
					ImageIO.write(cinzaSimples, "jpg", new File("imgs/_cinzaSimples.jpg"));
					image3 = new Image(null, "imgs/_cinzaSimples.jpg");
					exibeImagem(cLblImagemResultado, image3);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}

			}
		});
		btnMediaPonderada.setBounds(226, 53, 145, 35);
		btnMediaPonderada.setText("M\u00E9dia Ponderada");

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					BufferedImage cinzaSimples = new PDI().cinzaFaixasSimples(ImageIO.read(new File(pathImage1)),
							Integer.parseInt(txtNumFaixasCinza.getText()));
					ImageIO.write(cinzaSimples, "jpg", new File("imgs/_cinzaSimples.jpg"));
					image3 = new Image(null, "imgs/_cinzaSimples.jpg");
					exibeImagem(cLblImagemResultado, image3);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		btnNewButton.setBounds(397, 53, 102, 35);
		btnNewButton.setText("Faixas");

		txtNumFaixasCinza = new Text(composite, SWT.BORDER);
		txtNumFaixasCinza.setBounds(397, 26, 102, 21);

		Scale scalePontoLimiar = new Scale(composite, SWT.NONE);
		scalePontoLimiar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				try {
					int valorLimiar = scalePontoLimiar.getSelection();
					BufferedImage pretoBranco = new PDI().pretoBranco(ImageIO.read(new File(pathImage1)), valorLimiar);
					ImageIO.write(pretoBranco, "jpg", new File("imgs/_pretoBranco.jpg"));
					image3 = new Image(null, "imgs/_pretoBranco.jpg");
					exibeImagem(cLblImagemResultado, image3);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		scalePontoLimiar.setMaximum(255);
		scalePontoLimiar.setBounds(528, 53, 145, 35);

		Label lblPontoDeCorte = new Label(composite, SWT.NONE);
		lblPontoDeCorte.setBounds(528, 26, 102, 18);
		lblPontoDeCorte.setText("Ponto de Corte");
		
		CTabItem tbtmNegativa = new CTabItem(tabFolder, SWT.NONE);
		tbtmNegativa.setText("Negativa");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmNegativa.setControl(composite_1);
		
		btnGerarNegativa = new Button(composite_1, SWT.NONE);
		btnGerarNegativa.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					BufferedImage negativa = new PDI().negativa(ImageIO.read(new File(pathImage1)));
					ImageIO.write(negativa, "jpg", new File("imgs/_negativa.jpg"));
					image3 = new Image(null, "imgs/_negativa.jpg");
					exibeImagem(cLblImagemResultado, image3);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnGerarNegativa.setBounds(10, 20, 142, 66);
		btnGerarNegativa.setText("Gerar negativa");
		
		CTabItem tbtmRudos = new CTabItem(tabFolder, SWT.NONE);
		tbtmRudos.setText("Ru\u00EDdos");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmRudos.setControl(composite_2);
		
		Group grpVizinhana = new Group(composite_2, SWT.NONE);
		grpVizinhana.setText("Vizinhan\u00E7a");
		grpVizinhana.setBounds(10, 22, 222, 65);
		
		btnQuadrado = new Button(grpVizinhana, SWT.RADIO);
		btnQuadrado.setBounds(10, 27, 94, 16);
		btnQuadrado.setText("Quadrado");
		
		btnCruz = new Button(grpVizinhana, SWT.RADIO);
		btnCruz.setBounds(110, 27, 53, 16);
		btnCruz.setText("Cruz");
		
		btnX = new Button(grpVizinhana, SWT.RADIO);
		btnX.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnX.setBounds(169, 27, 31, 16);
		btnX.setText("X");
		
		btnEliminarRuido = new Button(composite_2, SWT.NONE);
		btnEliminarRuido.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				try {
					
					TipoVizinhanca tipo = null;
					TipoFiltroRuido filtro = null;
					
					if(btnQuadrado.getSelection())
						tipo = TipoVizinhanca.QUADRADO;
					else if(btnCruz.getSelection())
						tipo = TipoVizinhanca.CRUZ;
					else if(btnX.getSelection())
						tipo = TipoVizinhanca.XIS;
					
					if(btnMedia.getSelection())
						filtro = TipoFiltroRuido.MEDIA;
					else if(btnMediana.getSelection())
						filtro = TipoFiltroRuido.MEDIANA;
					
					BufferedImage semRuidos = new PDI().eliminarRuido(ImageIO.read(new File(pathImage1)), tipo, filtro);
					ImageIO.write(semRuidos, "jpg", new File("imgs/_ruido.jpg"));
					image3 = new Image(null, "imgs/_ruido.jpg");
					exibeImagem(cLblImagemResultado, image3);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btnEliminarRuido.setBounds(398, 31, 115, 56);
		btnEliminarRuido.setText("Eliminar ru\u00EDdo");
		
		Group grpFiltro = new Group(composite_2, SWT.NONE);
		grpFiltro.setText("Filtro");
		grpFiltro.setBounds(238, 22, 154, 65);
		
		btnMedia = new Button(grpFiltro, SWT.RADIO);
		btnMedia.setBounds(10, 27, 54, 16);
		btnMedia.setText("M\u00E9dia");
		
		btnMediana = new Button(grpFiltro, SWT.RADIO);
		btnMediana.setBounds(70, 27, 67, 16);
		btnMediana.setText("Mediana");
		
		CTabItem tbtmOperaes = new CTabItem(tabFolder, SWT.NONE);
		tbtmOperaes.setText("Opera\u00E7\u00F5es");
		
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmOperaes.setControl(composite_3);
		
		btnAumentarImagem = new Button(composite_3, SWT.NONE);
		btnAumentarImagem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					BufferedImage aumentar = new PDI().aumentarImagem(ImageIO.read(new File(pathImage1)));
					ImageIO.write(aumentar, "jpg", new File("imgs/_aumentar.jpg"));
					image3 = new Image(null, "imgs/_aumentar.jpg");
					exibeImagem(cLblImagemResultado, image3);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnAumentarImagem.setBounds(21, 29, 141, 30);
		btnAumentarImagem.setText("Aumentar Imagem");
		
		btnRotacionarImagem = new Button(composite_3, SWT.NONE);
		btnRotacionarImagem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					BufferedImage aumentar = new PDI().rotacionarDireita(ImageIO.read(new File(pathImage1)));
					ImageIO.write(aumentar, "jpg", new File("imgs/_rotacionar.jpg"));
					image3 = new Image(null, "imgs/_rotacionar.jpg");
					exibeImagem(cLblImagemResultado, image3);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnRotacionarImagem.setBounds(191, 29, 162, 30);
		btnRotacionarImagem.setText("Rotacionar Imagem");
		
		CTabItem tbtmAdioSubtrao = new CTabItem(tabFolder, SWT.NONE);
		tbtmAdioSubtrao.setText("Adi\u00E7\u00E3o / Subtra\u00E7\u00E3o");
		
		Composite composite_4 = new Composite(tabFolder, SWT.NONE);
		tbtmAdioSubtrao.setControl(composite_4);
		
		btnAdicao = new Button(composite_4, SWT.NONE);
		btnAdicao.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					BufferedImage adicao = new PDI().adicao(ImageIO.read(new File(pathImage1)), ImageIO.read(new File(pathImage2)),
							Integer.parseInt(spinnerImg1.getText()), Integer.parseInt(spinnerImg2.getText()));
					ImageIO.write(adicao, "jpg", new File("imgs/_adicao.jpg"));
					image3 = new Image(null, "imgs/_adicao.jpg");
					exibeImagem(cLblImagemResultado, image3);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnAdicao.setBounds(231, 20, 90, 30);
		btnAdicao.setText("Adi\u00E7\u00E3o");
		
		btnSubtracao = new Button(composite_4, SWT.NONE);
		btnSubtracao.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					BufferedImage adicao = new PDI().subtracao(ImageIO.read(new File(pathImage1)), ImageIO.read(new File(pathImage2)),
							Integer.parseInt(spinnerImg1.getText()), Integer.parseInt(spinnerImg2.getText()));
					ImageIO.write(adicao, "jpg", new File("imgs/_subtracao.jpg"));
					image3 = new Image(null, "imgs/_subtracao.jpg");
					exibeImagem(cLblImagemResultado, image3);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnSubtracao.setBounds(231, 56, 90, 30);
		btnSubtracao.setText("Subtra\u00E7\u00E3o");
		
		Group grpProporo = new Group(composite_4, SWT.NONE);
		grpProporo.setText("Propor\u00E7\u00E3o");
		grpProporo.setBounds(10, 7, 215, 87);
		
		Label lblImagem = new Label(grpProporo, SWT.NONE);
		lblImagem.setBounds(20, 27, 70, 20);
		lblImagem.setText("Imagem 1");
		
		spinnerImg1 = new Spinner(grpProporo, SWT.BORDER);
		spinnerImg1.setTextLimit(3);
		spinnerImg1.setBounds(20, 53, 59, 26);
		
		Label lblImagem_1 = new Label(grpProporo, SWT.NONE);
		lblImagem_1.setText("Imagem 2");
		lblImagem_1.setBounds(121, 27, 70, 20);
		
		spinnerImg2 = new Spinner(grpProporo, SWT.BORDER);
		spinnerImg2.setTextLimit(3);
		spinnerImg2.setBounds(121, 53, 59, 26);
		
		spinnerTransparencia = new Spinner(composite_4, SWT.BORDER);
		spinnerTransparencia.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				
				try {
					Integer fator = Integer.parseInt(spinnerTransparencia.getText());
					BufferedImage transparencia = new PDI().transparencia(ImageIO.read(new File(pathImage1)), fator);
					ImageIO.write(transparencia, "jpg", new File("imgs/_transparencia.jpg"));
					image3 = new Image(null, "imgs/_transparencia.jpg");
					exibeImagem(cLblImagemResultado, image3);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
					
			}
		});
		spinnerTransparencia.setIncrement(5);
		spinnerTransparencia.setBounds(360, 42, 59, 26);
		
		Label lblNewLabel = new Label(composite_4, SWT.NONE);
		lblNewLabel.setBounds(425, 45, 105, 20);
		lblNewLabel.setText("Transpar\u00EAncia");
		
		CTabItem tbtmHistograma = new CTabItem(tabFolder, SWT.NONE);
		tbtmHistograma.setText("Histograma");
		
		Composite composite_5 = new Composite(tabFolder, SWT.NONE);
		tbtmHistograma.setControl(composite_5);
		
		btnGerarHistograma = new Button(composite_5, SWT.NONE);
		btnGerarHistograma.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				try {
					new PDI().criarGraficoHistograma(ImageIO.read(new File(pathImage1)));
					image3 = new Image(null, "imgs/_histograma.jpg");
					exibeImagem(cLblImagemResultado, image3);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		btnGerarHistograma.setBounds(35, 10, 174, 84);
		btnGerarHistograma.setText("Gerar histograma");
		
		btnEqualizao = new Button(composite_5, SWT.NONE);
		btnEqualizao.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				try {
					BufferedImage equalizado = new PDI().equalizar(ImageIO.read(new File(pathImage1)));
					ImageIO.write(equalizado, "jpg", new File("imgs/_equalizado.jpg"));
					image3 = new Image(null, "imgs/_equalizado.jpg");
					exibeImagem(cLblImagemResultado, image3);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		btnEqualizao.setBounds(239, 10, 138, 84);
		btnEqualizao.setText("Equaliza\u00E7\u00E3o");
		
		CTabItem tbtmDilataoEroso = new CTabItem(tabFolder, SWT.NONE);
		tbtmDilataoEroso.setText("Dilata\u00E7\u00E3o / Eros\u00E3o");
		
		Composite composite_6 = new Composite(tabFolder, SWT.NONE);
		tbtmDilataoEroso.setControl(composite_6);
		
		Group grpMtodo = new Group(composite_6, SWT.NONE);
		grpMtodo.setText("M\u00E9todo");
		grpMtodo.setToolTipText("M\u00E9todo");
		grpMtodo.setBounds(27, 10, 283, 87);
		
		btnDilCruz = new Button(grpMtodo, SWT.RADIO);
		btnDilCruz.setBounds(10, 40, 58, 20);
		btnDilCruz.setText("Cruz");
		
		btnDilCrculo = new Button(grpMtodo, SWT.RADIO);
		btnDilCrculo.setBounds(81, 40, 70, 20);
		btnDilCrculo.setText("C\u00EDrculo");
		
		btnDilLosango = new Button(grpMtodo, SWT.RADIO);
		btnDilLosango.setBounds(162, 40, 111, 20);
		btnDilLosango.setText("Losango");
		
		btnDilatacao = new Button(composite_6, SWT.NONE);
		btnDilatacao.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				try {
					
					TipoDilatacao tipo = null;
					
					if(btnDilCruz.getSelection())
						tipo = TipoDilatacao.CRUZ;
					else if(btnDilCrculo.getSelection())
						tipo = TipoDilatacao.CIRCULO;
					else if(btnDilLosango.getSelection())
						tipo = TipoDilatacao.LOSANGO;
					
					BufferedImage dilatado = new PDI().dilatacao(ImageIO.read(new File(pathImage1)), tipo);
					ImageIO.write(dilatado, "jpg", new File("imgs/_dilatacao.jpg"));
					image3 = new Image(null, "imgs/_dilatacao.jpg");
					exibeImagem(cLblImagemResultado, image3);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		btnDilatacao.setBounds(418, 24, 90, 30);
		btnDilatacao.setText("Dilata\u00E7\u00E3o");
		
		Button btnErosao = new Button(composite_6, SWT.NONE);
		btnErosao.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				try {
					
					TipoDilatacao tipo = null;
					
					if(btnDilCruz.getSelection())
						tipo = TipoDilatacao.CRUZ;
					else if(btnDilCrculo.getSelection())
						tipo = TipoDilatacao.CIRCULO;
					else if(btnDilLosango.getSelection())
						tipo = TipoDilatacao.LOSANGO;
					
					int limiar = Integer.parseInt(spinnerLimiarErosao.getText());
					
					BufferedImage erosao = new PDI().erosao(ImageIO.read(new File(pathImage1)), tipo, limiar);
					ImageIO.write(erosao, "jpg", new File("imgs/_erosao.jpg"));
					image3 = new Image(null, "imgs/_erosao.jpg");
					exibeImagem(cLblImagemResultado, image3);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		btnErosao.setBounds(418, 60, 90, 30);
		btnErosao.setText("Eros\u00E3o");
		
		spinnerLimiarErosao = new Spinner(composite_6, SWT.BORDER);
		spinnerLimiarErosao.setIncrement(5);
		spinnerLimiarErosao.setBounds(316, 64, 59, 26);
		
		Label lblLimiarEroso = new Label(composite_6, SWT.NONE);
		lblLimiarEroso.setBounds(316, 33, 96, 20);
		lblLimiarEroso.setText("Limiar Eros\u00E3o");
		
		CTabItem tbtmSegmentao = new CTabItem(tabFolder, SWT.NONE);
		tbtmSegmentao.setText("Segmenta\u00E7\u00E3o");
		
		Composite composite_7 = new Composite(tabFolder, SWT.NONE);
		tbtmSegmentao.setControl(composite_7);
		
		Button btnSegmentar = new Button(composite_7, SWT.NONE);
		btnSegmentar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				try {
				
					List<RegiaoSegmento> regioes = new ArrayList<>();
					regioes.add(new RegiaoSegmento(0, 85, new int[]{255,0,0,0}));
					regioes.add(new RegiaoSegmento(86, 170, new int[]{0,255,0,0}));
					regioes.add(new RegiaoSegmento(171, 255, new int[]{0,0,255,0}));
					
					BufferedImage segmentacao = new PDI().segmentacao(ImageIO.read(new File(pathImage1)), regioes);
					ImageIO.write(segmentacao, "jpg", new File("imgs/_segmentacao.jpg"));
					image3 = new Image(null, "imgs/_segmentacao.jpg");
					exibeImagem(cLblImagemResultado, image3);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		btnSegmentar.setBounds(21, 20, 119, 74);
		btnSegmentar.setText("Segmentar");
		
		Button btnSegmentarOpencv = new Button(composite_7, SWT.NONE);
		btnSegmentarOpencv.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				try {
					
					System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
			        Mat source = Imgcodecs.imread(pathImage1,  Imgcodecs.CV_LOAD_IMAGE_COLOR);
			        Mat destination = new Mat(source.rows(),source.cols(),source.type());

			        destination = source;
			        Imgproc.threshold(source,destination,Integer.parseInt(textThresh.getText()),255,Imgproc.THRESH_BINARY);
			        Imgcodecs.imwrite("imgs/_segmentacao.jpg", destination);
					
					image3 = new Image(null, "imgs/_segmentacao.jpg");
					exibeImagem(cLblImagemResultado, image3);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		btnSegmentarOpencv.setText("Segmentar OpenCV");
		btnSegmentarOpencv.setBounds(175, 54, 158, 40);
		
		textThresh = new Text(composite_7, SWT.BORDER);
		textThresh.setBounds(255, 20, 78, 26);
		
		Label lblThrash = new Label(composite_7, SWT.NONE);
		lblThrash.setBounds(192, 20, 50, 28);
		lblThrash.setText("Thrash");
		
		CTabItem tbtmProva = new CTabItem(tabFolder, SWT.NONE);
		tbtmProva.setText("Prova 1");
		
		Composite composite_8 = new Composite(tabFolder, SWT.NONE);
		tbtmProva.setControl(composite_8);
		
		Group grpQuesto = new Group(composite_8, SWT.NONE);
		grpQuesto.setText("Quest\u00E3o 1");
		grpQuesto.setBounds(24, 10, 311, 87);
		
		textQuad1 = new Text(grpQuesto, SWT.BORDER);
		textQuad1.setBounds(10, 51, 56, 26);
		
		Label lblQuadrantesInverter = new Label(grpQuesto, SWT.NONE);
		lblQuadrantesInverter.setBounds(10, 22, 150, 20);
		lblQuadrantesInverter.setText("Quadrantes inverter");
		
		textQuad2 = new Text(grpQuesto, SWT.BORDER);
		textQuad2.setBounds(86, 51, 56, 26);
		
		Button btnExecutarQuestao1 = new Button(grpQuesto, SWT.NONE);
		btnExecutarQuestao1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				try {
					Byte quad1 = Byte.parseByte(textQuad1.getText());
					Byte quad2 = Byte.parseByte(textQuad2.getText());
					
					if(quad1 < 1 || quad1 > 4 || quad2 < 1 || quad2 > 4)
						throw new Exception();
					
					BufferedImage inverterQuadrantes = new PDI().inverterDoisQuadrantes(ImageIO.read(new File(pathImage1)), quad1, quad2);
					ImageIO.write(inverterQuadrantes, "jpg", new File("imgs/_invquad.jpg"));
					image3 = new Image(null, "imgs/_invquad.jpg");
					exibeImagem(cLblImagemResultado, image3);

				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Por favor, insira apenas números entre 1 e 4 nos campos de entrada.");
				}
				
			}
		});
		btnExecutarQuestao1.setBounds(166, 22, 118, 55);
		btnExecutarQuestao1.setText("Executar");
		
		Group grpQuesto_1 = new Group(composite_8, SWT.NONE);
		grpQuesto_1.setText("Quest\u00E3o 2");
		grpQuesto_1.setBounds(363, 10, 188, 87);
		
		Button btnExecutarQuestao2 = new Button(grpQuesto_1, SWT.NONE);
		btnExecutarQuestao2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				try {
					
					BufferedImage dividirDiagonalEEqualizar = new PDI().dividirDiagonalEEqualizar(ImageIO.read(new File(pathImage1)));
					ImageIO.write(dividirDiagonalEEqualizar, "jpg", new File("imgs/_diagEq.jpg"));
					image3 = new Image(null, "imgs/_diagEq.jpg");
					exibeImagem(cLblImagemResultado, image3);

				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Erro ao processar.");
				}
				
			}
		});
		btnExecutarQuestao2.setBounds(44, 33, 90, 30);
		btnExecutarQuestao2.setText("Executar");
		
		Group grpQuesto_2 = new Group(composite_8, SWT.NONE);
		grpQuesto_2.setText("Quest\u00E3o 3");
		grpQuesto_2.setBounds(577, 10, 188, 87);
		
		Button btnExecutarQuestao3 = new Button(grpQuesto_2, SWT.NONE);
		btnExecutarQuestao3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					boolean fechado = new PDI().identificarQuadradoFechado(ImageIO.read(new File(pathImage1)));
					if(fechado) 
						JOptionPane.showMessageDialog(null, "Quadrado fechado!");
					else
						JOptionPane.showMessageDialog(null, "Quadrado aberto!");
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Erro ao processar.");
				}
			}
		});
		btnExecutarQuestao3.setBounds(50, 35, 90, 30);
		btnExecutarQuestao3.setText("Executar");
		
		Label lblMdia = new Label(this, SWT.NONE);
		lblMdia.setAlignment(SWT.CENTER);
		lblMdia.setBounds(34, 429, 70, 20);
		lblMdia.setText("M\u00E9dia");
		
		labelMedia = new Label(this, SWT.NONE);
		labelMedia.setAlignment(SWT.CENTER);
		labelMedia.setBounds(34, 461, 70, 20);
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(1301, 698);

	}

	private Image abrirImagem(String path) {
		if (path != null && !path.equals(""))
			return new Image(null, path);

		return null;
	}

	private void exibeImagem(CLabel label, Image img) {

		label.setBackground(img);
		label.setBounds(label.getBounds().x, label.getBounds().y, img.getImageData().width, img.getImageData().height);

	}

	private String selecionarCaminhoImagem() {
		fileDialog = new FileDialog(this, SWT.OPEN);
		fileDialog.setText("Abrir imagem");
		fileDialog.setFilterPath("imgs");
		String[] filterExt = { "*.*", "*.png", "*.bmp", "*.jpg", "*.jpeg" };
		fileDialog.setFilterExtensions(filterExt);
		return fileDialog.open();
	}

	private Pixel encontrarPixel(int x, int y, ImageData imageData) {

		PaletteData palette = null;

		if (imageData != null)
			palette = imageData.palette;

		if (palette != null && x > -1 && y > -1) {

			int pixel = imageData.getPixel(x, y);
			RGB rgb = palette.getRGB(pixel);
			lblCorR.setText(String.valueOf(rgb.red));
			lblCorG.setText(String.valueOf(rgb.green));
			lblCorB.setText(String.valueOf(rgb.blue));
			labelMedia.setText(String.valueOf(Math.round((rgb.red + rgb.green + rgb.blue) / 3)));

			hex = String.format("#%02X%02X%02X", rgb.red, rgb.green, rgb.blue);

			compositeCor.setBackground(new Color(null, rgb.red, rgb.green, rgb.blue));
			lblPontoMouse.setText(x + "  x  " + y);

		}
		
		return new Pixel(x, y);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
