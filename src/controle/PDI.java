package controle;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.wb.swt.Util;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class PDI {
	
	public BufferedImage cinzaSimples(BufferedImage img, Double[] ponderado) {
		
		WritableRaster raster = img.getRaster();
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		int pixels[] = new int[4];
		for (int i = 0; i < img.getWidth() - 1; i++) {
			for (int j = 0; j < img.getHeight() - 1; j++) {
				
				raster.getPixel(i, j, pixels);
				double media = 0;
				if(ponderado != null) 
					media = ((pixels[0] * (ponderado[0] / 100)) + (pixels[1] * (double)(ponderado[1] / 100)) + (pixels[2] * (double)(ponderado[2] / 100))) / 3;
				else
					media = (pixels[0] + pixels[1] + pixels[2]) / 3;
				
				pixels[0] = (int)media;
				pixels[1] = (int)media;
				pixels[2] = (int)media;
				raster.setPixel(i, j, pixels);
				
			}			
		}
		
		try {
			newImg.setData(raster);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}
	
	public BufferedImage cinzaFaixasSimples(BufferedImage img, int numFaixas) {
		
		WritableRaster raster = img.getRaster();
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		int pixels[] = new int[4];
		
		Double larguraFaixa = (double)img.getWidth() / numFaixas;
		boolean cinza = true;
		Double limite = larguraFaixa;
		
		for (int i = 0; i < img.getWidth() - 1; i++) {
			if(cinza) {
				if(i >= limite) {
					cinza = !cinza;
					limite += larguraFaixa;
				}
				for (int j = 0; j < img.getHeight() - 1; j++) {
					
					raster.getPixel(i, j, pixels);
					double media = 0;
					media = (pixels[0] + pixels[1] + pixels[2]) / 3;
					
					pixels[0] = (int)media;
					pixels[1] = (int)media;
					pixels[2] = (int)media;
					raster.setPixel(i, j, pixels);
					
				}			
			} else {
				if(i >= limite) {
					cinza = !cinza;
					limite += larguraFaixa;
				}
			}
		}
		
		try {
			newImg.setData(raster);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}
	
	private int valorPixelLimiar(int valorPixel, int valorLimiar) {
		if(valorPixel >= valorLimiar)
			return 255;
		else
			return 0;
	}
	
	public BufferedImage pretoBranco(BufferedImage img, int pontoLimiar) {
		
		WritableRaster raster = img.getRaster();
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		int pixels[] = new int[4];
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				
				raster.getPixel(i, j, pixels);
				pixels[0] = valorPixelLimiar(pixels[0], pontoLimiar);
				pixels[1] = valorPixelLimiar(pixels[1], pontoLimiar);
				pixels[2] = valorPixelLimiar(pixels[2], pontoLimiar);
				pixels[3] = 0;
				raster.setPixel(i, j, pixels);

			}			
		}
		
		try {
			newImg.setData(raster);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}
	
	public BufferedImage negativa(BufferedImage img) {
		
		WritableRaster raster = img.getRaster();
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		int pixels[] = new int[4];
		for (int i = 0; i < img.getWidth() - 1; i++) {
			for (int j = 0; j < img.getHeight() - 1; j++) {
				
				raster.getPixel(i, j, pixels);
				pixels[0] = 255 - pixels[0];
				pixels[1] = 255 - pixels[1];
				pixels[2] = 255 - pixels[2];
				raster.setPixel(i, j, pixels);
				
			}			
		}
		
		try {
			newImg.setData(raster);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}
	
	public BufferedImage eliminarRuido(BufferedImage img, TipoVizinhanca tipoVizinhanca, TipoFiltroRuido tipoFiltro) {
		
		WritableRaster raster = img.getRaster();
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		int pixels[] = new int[4];
		for (int i = 1; i < img.getWidth() - 2; i++) {
			for (int j = 1; j < img.getHeight() - 2; j++) {
				
				raster.getPixel(i, j, pixels);
				
				Pixel px = new Pixel(i, j);
				px.r = pixels[0];
				px.g = pixels[1];
				px.b = pixels[2];
						
				List<Pixel> pixelsAnalisar = Util.vizinhos(px, tipoVizinhanca);
				for(Pixel p : pixelsAnalisar) {
					Util.definirRGB(p, img);
				}
				pixelsAnalisar.add(px);
				
				pixels[0] = tipoFiltro.equals(TipoFiltroRuido.MEDIANA) ? (int)Util.mediana(pixelsAnalisar, RGB.R) : (int)Util.media(pixelsAnalisar, RGB.R);
				pixels[1] = tipoFiltro.equals(TipoFiltroRuido.MEDIANA) ? (int)Util.mediana(pixelsAnalisar, RGB.G) : (int)Util.media(pixelsAnalisar, RGB.G);
				pixels[2] = tipoFiltro.equals(TipoFiltroRuido.MEDIANA) ? (int)Util.mediana(pixelsAnalisar, RGB.B) : (int)Util.media(pixelsAnalisar, RGB.B);
				
				raster.setPixel(i, j, pixels);
				
			}			
		}
		
		try {
			newImg.setData(raster);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}

	public BufferedImage desenharRetangulo(BufferedImage img, Pixel pixel1, Pixel pixel2) {
		
		WritableRaster raster = img.getRaster();
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		int pixels[] = new int[4];
		
		Pixel pixelInicioX = null;
		Pixel pixelFimX = null;
		Pixel pixelInicioY = null;
		Pixel pixelFimY = null;
		
		if(pixel1.x < pixel2.x) {
			pixelInicioX = pixel1;
			pixelFimX = pixel2;
		} else {
			pixelInicioX = pixel2;
			pixelFimX = pixel1;
			
		}
		if(pixel1.y < pixel2.y) {
			pixelInicioY = pixel1;
			pixelFimY = pixel2;
		} else {
			pixelInicioY = pixel2;
			pixelFimY = pixel1;
			
		}
		
		for(int i = pixelInicioX.x; i <= pixelFimX.x; i++) {
			
			pixels[0] = 0;
			pixels[1] = 0;
			pixels[2] = 0;
			
			raster.setPixel(i, pixelInicioX.y, pixels);
			raster.setPixel(i, pixelFimX.y, pixels);
			
		}
		
		for(int i = pixelInicioY.y; i <= pixelFimY.y; i++) {
			
			pixels[0] = 0;
			pixels[1] = 0;
			pixels[2] = 0;
			
			raster.setPixel(pixelInicioY.x, i, pixels);
			raster.setPixel(pixelFimY.x, i, pixels);
			
		};
		
		try {
			newImg.setData(raster);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}
	
	public BufferedImage aumentarImagem(BufferedImage img) {
		
		WritableRaster rasterOrigin = img.getRaster();
		BufferedImage newImg = new BufferedImage(img.getWidth() * 2, img.getHeight() * 2, BufferedImage.TYPE_INT_RGB);
		WritableRaster rasterTarget = newImg.getRaster();
		int pixels[] = new int[4];
		for (int i = 1; i < img.getWidth() - 2; i++) {
			for (int j = 1; j < img.getHeight() - 2; j++) {
				
				rasterOrigin.getPixel(i, j, pixels);
				
				int x = i - 1;
				int y = j - 1;
				//1 , 0
				try {
					rasterTarget.setPixel(i + x, j + y, pixels);
					rasterTarget.setPixel(i + x +1, j + y, pixels);
					rasterTarget.setPixel(i + x, j + y + 1, pixels);
					rasterTarget.setPixel(i + x +1, j + y+1, pixels);
				} catch (Exception e) {
					System.out.println(i);
					System.out.println(j);
				}
				
			}			
		}
		
		try {
			newImg.setData(rasterTarget);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}
	
	public BufferedImage rotacionarDireita(BufferedImage img) {
		
		WritableRaster rasterOrigin = img.getRaster();
		BufferedImage newImg = new BufferedImage(img.getHeight(), img.getWidth(), BufferedImage.TYPE_INT_RGB);
		WritableRaster rasterTarget = newImg.getRaster();
		int pixels[] = new int[4];
		for (int i = 1; i < img.getWidth() - 2; i++) {
			for (int j = 1; j < img.getHeight() - 2; j++) {
				
				rasterOrigin.getPixel(i, j, pixels);
				rasterTarget.setPixel(img.getHeight() - j, img.getWidth() - i, pixels);
				
			}			
		}
		
		try {
			newImg.setData(rasterTarget);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}

	public BufferedImage adicao(BufferedImage img1, BufferedImage img2, int proporcaoImg1, int proporcaoImg2) {
		
		int larguraMenor = Math.max(img1.getWidth(), img2.getWidth());
		int alturaMenor = Math.max(img1.getHeight(), img2.getHeight());
		
		BufferedImage newImg = new BufferedImage(larguraMenor, alturaMenor, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster1 = img1.getRaster();
		WritableRaster raster2 = img2.getRaster();
		WritableRaster rasterTarget = newImg.getRaster();
		int pixelsImg1[] = new int[4];
		int pixelsImg2[] = new int[4];
		int pixelsTarget[] = new int[4];
		for (int i = 1; i < larguraMenor; i++) {
			for (int j = 1; j < alturaMenor; j++) {
				
				raster1.getPixel(i, j, pixelsImg1);
				raster2.getPixel(i, j, pixelsImg2);
				
				float valor1 = (pixelsImg1[0] * ((float)proporcaoImg1 / 100)) + (pixelsImg2[0] * ((float)proporcaoImg2 / 100));
				float valor2 = (pixelsImg1[1] * ((float)proporcaoImg1 / 100)) + (pixelsImg2[1] * ((float)proporcaoImg2 / 100));
				float valor3 = (pixelsImg1[2] * ((float)proporcaoImg1 / 100)) + (pixelsImg2[2] * ((float)proporcaoImg2 / 100));
				
				pixelsTarget[0] = Math.round(valor1);
				pixelsTarget[1] = Math.round(valor2);
				pixelsTarget[2] = Math.round(valor3);
				
				rasterTarget.setPixel(i, j, pixelsTarget);
				
			}			
		}
		
		try {
			newImg.setData(rasterTarget);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}
	
	public BufferedImage subtracao(BufferedImage img1, BufferedImage img2, int proporcaoImg1, int proporcaoImg2) {
		
		int larguraMenor = Math.max(img1.getWidth(), img2.getWidth());
		int alturaMenor = Math.max(img1.getHeight(), img2.getHeight());
		
		BufferedImage newImg = new BufferedImage(larguraMenor, alturaMenor, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster1 = img1.getRaster();
		WritableRaster raster2 = img2.getRaster();
		WritableRaster rasterTarget = newImg.getRaster();
		int pixelsImg1[] = new int[4];
		int pixelsImg2[] = new int[4];
		int pixelsTarget[] = new int[4];
		for (int i = 1; i < larguraMenor; i++) {
			for (int j = 1; j < alturaMenor; j++) {
				
				raster1.getPixel(i, j, pixelsImg1);
				raster2.getPixel(i, j, pixelsImg2);
				
				float valor1 = (pixelsImg1[0] * ((float)proporcaoImg1 / 100)) - (pixelsImg2[0] * ((float)proporcaoImg2 / 100));
				float valor2 = (pixelsImg1[1] * ((float)proporcaoImg1 / 100)) - (pixelsImg2[1] * ((float)proporcaoImg2 / 100));
				float valor3 = (pixelsImg1[2] * ((float)proporcaoImg1 / 100)) - (pixelsImg2[2] * ((float)proporcaoImg2 / 100));
				
				pixelsTarget[0] = valor1 > 0 ? Math.round(valor1) : 0;
				pixelsTarget[1] = valor2 > 0 ? Math.round(valor2) : 0;
				pixelsTarget[2] = valor3 > 0 ? Math.round(valor3) : 0;
				
				rasterTarget.setPixel(i, j, pixelsTarget);
				
			}			
		}
		
		try {
			newImg.setData(rasterTarget);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}
	
	public BufferedImage transparencia(BufferedImage img1, int fator) {
		
		int proporcaoImg1 = 100 - fator;
		
		BufferedImage newImg = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableRaster raster1 = img1.getRaster();
		WritableRaster rasterTarget = newImg.getRaster();
		int pixelsImg1[] = new int[4];
		int pixelsTarget[] = new int[4];
		for (int i = 1; i < img1.getWidth(); i++) {
			for (int j = 1; j < img1.getHeight(); j++) {
				
				raster1.getPixel(i, j, pixelsImg1);
				
				float valor1 = (pixelsImg1[0] * ((float)proporcaoImg1 / 100)) + (255 * ((float)fator / 100));
				float valor2 = (pixelsImg1[1] * ((float)proporcaoImg1 / 100)) + (255 * ((float)fator / 100));
				float valor3 = (pixelsImg1[2] * ((float)proporcaoImg1 / 100)) + (255 * ((float)fator / 100));
				
				pixelsTarget[0] = Math.round(valor1);
				pixelsTarget[1] = Math.round(valor2);
				pixelsTarget[2] = Math.round(valor3);
				
				rasterTarget.setPixel(i, j, pixelsTarget);
				
			}			
		}
		
		try {
			newImg.setData(rasterTarget);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}
	
	private int[] vetorHistograma(BufferedImage img) {
		
		int[] total = new int[256];
		WritableRaster rasterOrigin = img.getRaster();
		int[] pixels = new int[4];
		for (int i = 1; i < img.getWidth() - 2; i++) {
			for (int j = 1; j < img.getHeight() - 2; j++) {
				
				rasterOrigin.getPixel(i, j, pixels);
				total[pixels[0]]++;
				total[pixels[1]]++;
				total[pixels[2]]++;
				
			}			
		}
		
		return total;
		
	}
	
	public void criarGraficoHistograma(BufferedImage img) {
		
		int[] vetor = vetorHistograma(img);
		DefaultCategoryDataset ds = new DefaultCategoryDataset();
		
		for (int i = 0; i < 256; i++) {
			ds.addValue(vetor[i], "", i+"");
		}
		
		JFreeChart grafico = ChartFactory.createBarChart("Histograma", "", "", ds);
		
		try {
			OutputStream arquivo = new FileOutputStream("imgs/_histograma.jpg");
			ChartUtilities.writeChartAsJPEG(arquivo, grafico, 6000, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public BufferedImage equalizar(BufferedImage img) {
		
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		int[] acumulado = new int[256];
		int[] vetor = vetorHistograma(img);
		int L = 0;
		int N = Math.round(img.getWidth() * img.getHeight() * 3);
		
		for (int i = 0; i < vetor.length; i++) {
			if(i > 0)
				acumulado[i] = vetor[i] + acumulado[i-1];
			else
				acumulado[i] = vetor[i];
			
			if(vetor[i] > 0)
				L++;
				
		}
		int tons = L - 1;
		
		WritableRaster rasterOrigin = img.getRaster();
		int[] pixels = new int[4];
		for (int i = 1; i < img.getWidth() - 2; i++) {
			for (int j = 1; j < img.getHeight() - 2; j++) {
				
				rasterOrigin.getPixel(i, j, pixels);
				
				
				pixels[0] = Math.round((((float)tons / N)) * acumulado[ pixels[0] ]);
				pixels[1] = Math.round((((float)tons / N)) * acumulado[ pixels[1] ]);
				pixels[2] = Math.round((((float)tons / N)) * acumulado[ pixels[2] ]);
				
				rasterOrigin.setPixel(i, j, pixels);
				
			}			
		}
		
		try {
			newImg.setData(rasterOrigin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}
	
	public BufferedImage dilatacao(BufferedImage img, TipoDilatacao tipo) {
		
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		WritableRaster rasterOrigin = img.getRaster();
		WritableRaster rasterFinal = newImg.getRaster();
		int[] pixels = new int[4];
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				
				rasterOrigin.getPixel(i, j, pixels);
				
				pixels[0] = pixels[0];
				pixels[1] = pixels[1];
				pixels[2] = pixels[2];
				
				Pixel px = new Pixel(i, j, pixels[0], pixels[1], pixels[2]);
				
				if(pixels[0] == 255 && pixels[1] == 255 && pixels[2] == 255)
					dilatarPixel(rasterFinal, px, tipo);
				
			}
		}
		
		try {
			newImg.setData(rasterFinal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}
	
	private void dilatarPixel(WritableRaster raster, Pixel px, TipoDilatacao tipo) {
		
		int filtro[][] = null;
		int inicio = -1;
		
		if(tipo == TipoDilatacao.CRUZ) {
			filtro = new int [][] {
				{0,1,0},
				{1,1,1},
				{0,1,0}
			};
			inicio = -1;
		}
		
		if(tipo == TipoDilatacao.CIRCULO) {
		
			filtro = new int [][] {
				{0,1,1,1,0},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{0,1,1,1,0}
			};
			inicio = -2;
			
		}
		
		if(tipo == TipoDilatacao.LOSANGO) {
			
			filtro = new int [][] {
				{0,0,0,1,0,0,0},
				{0,1,1,1,1,1,0},
				{0,1,1,1,1,1,1},
				{0,1,1,1,1,1,1},
				{0,1,1,1,1,1,1},
				{0,1,1,1,1,1,0},
				{0,0,0,1,0,0,0}
			};
			inicio = -3;
			
		}
		
		int[] pixels = new int[]{255,255,255,0};		
		
		for (int i = 0; i < filtro.length; i++) {
			
			int x = inicio;
			for (int j = 0; j < filtro.length; j++) {
				
				if(filtro[i][j] == 1) {
					try {
						raster.setPixel(px.x + x, px.y + inicio, pixels);
					} catch (Exception e) {
//						e.printStackTrace();
					}
				}
				x++;
				
			}
			inicio++;
			
		}		
		
	}
	
	public BufferedImage erosao(BufferedImage img, TipoDilatacao tipo, int limiar) {
		
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		WritableRaster rasterOrigin = img.getRaster();
		WritableRaster rasterFinal = newImg.getRaster();
		int[] pixels = new int[4];
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				
				rasterOrigin.getPixel(i, j, pixels);
				
				Pixel px = new Pixel(i, j, pixels[0], pixels[1], pixels[2]);
				
				if(pixels[0] > 10 && pixels[1] > 10 && pixels[2] > 10) {
					if(numPixelsBrancosVizinhos(rasterOrigin, px, tipo) < limiar) {
						pixels[0] = 0;
						pixels[1] = 0;
						pixels[2] = 0;
						rasterFinal.setPixel(i, j, pixels);
					} else {
						pixels[0] = 255;
						pixels[1] = 255;
						pixels[2] = 255;
						rasterFinal.setPixel(i, j, pixels);						
					}
				}
					
			}
		}
		
		try {
			newImg.setData(rasterFinal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}
	
	private int numPixelsBrancosVizinhos(WritableRaster raster, Pixel px, TipoDilatacao tipo) {
		
		int filtro[][] = null;
		int inicio = -1;
		
		if(tipo == TipoDilatacao.CRUZ) {
			filtro = new int [][] {
				{0,1,0},
				{1,1,1},
				{0,1,0}
			};
			inicio = -1;
		}
		
		if(tipo == TipoDilatacao.CIRCULO) {
		
			filtro = new int [][] {
				{0,1,1,1,0},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{0,1,1,1,0}
			};
			inicio = -2;
			
		}
		
		if(tipo == TipoDilatacao.LOSANGO) {
			
			filtro = new int [][] {
				{0,0,0,1,0,0,0},
				{0,1,1,1,1,1,0},
				{0,1,1,1,1,1,1},
				{0,1,1,1,1,1,1},
				{0,1,1,1,1,1,1},
				{0,1,1,1,1,1,0},
				{0,0,0,1,0,0,0}
			};
			inicio = -3;
			
		}
		
		int numBrancos = 0;
		
		int[] pixels = new int[4];		
		
		for (int i = 0; i < filtro.length; i++) {
			
			int x = inicio;
			for (int j = 0; j < filtro.length; j++) {
				
				try {
					raster.getPixel(px.x + x, px.y + inicio, pixels);
					if(pixels[0] > 10 && pixels[1] > 10 && pixels[2] > 10)
						numBrancos++;
					
				} catch (Exception e) {
//					e.printStackTrace();
				}
				x++;
				
			}
			inicio++;
			
		}
		
		return numBrancos;
		
	}
	
	public BufferedImage segmentacao(BufferedImage img, List<RegiaoSegmento> regioes) {
		
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		Map<String, int[]> coresPadroes = new HashMap<>();
		coresPadroes.put("000", new int[]{0,0,0});
		coresPadroes.put("111", new int[]{255,255,255});
		coresPadroes.put("100", new int[]{255,0,0});
		coresPadroes.put("010", new int[]{0,255,0});
		coresPadroes.put("001", new int[]{0,0,255});
		coresPadroes.put("110", new int[]{255,255,0});
		coresPadroes.put("011", new int[]{0,255,255});
		coresPadroes.put("101", new int[]{255,0,255});
		
		WritableRaster rasterOrigin = img.getRaster();
		WritableRaster rasterFinal = newImg.getRaster();
		int[] pixels = new int[4];
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				
				rasterOrigin.getPixel(i, j, pixels);
				
				Pixel px = new Pixel(i, j, pixels[0], pixels[1], pixels[2]);
				
				String padrao = mediaPadraoCanaisVizinhanca(rasterOrigin, px);
				rasterFinal.setPixel(i, j, coresPadroes.get(padrao));
				
			}
		}
		
		try {
			newImg.setData(rasterFinal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}
	
	private int mediaVizinhanca(WritableRaster raster, Pixel px) {
		
		int filtro[][] = null;
		int inicio = -1;
		
		filtro = new int [][] {
			{0,0,0,1,0,0,0},
			{0,1,1,1,1,1,0},
			{0,1,1,1,1,1,1},
			{0,1,1,1,1,1,1},
			{0,1,1,1,1,1,1},
			{0,1,1,1,1,1,0},
			{0,0,0,1,0,0,0}
		};
		inicio = -3;
		
		double total = 0;
		
		int[] pixels = new int[4];		
		
		for (int i = 0; i < filtro.length; i++) {
			
			int x = inicio;
			for (int j = 0; j < filtro.length; j++) {
				
				try {
					
					raster.getPixel(px.x + x, px.y + inicio, pixels);
					total += (pixels[0] + pixels[1] + pixels[2]) / 3;
					
				} catch (Exception e) {
//					e.printStackTrace();
				}
				x++;
				
			}
			inicio++;
			
		}
		
		return (int) Math.round(total/49);
		
	}
	
	private String mediaPadraoCanaisVizinhanca(WritableRaster raster, Pixel px) {
		
		int filtro[][] = null;
		int inicio = -1;
		
		Map<String, Integer> padroes = new HashMap<>();
		
		filtro = new int [][] {
			{0,1,1,1,0},
			{1,1,1,1,1},
			{1,1,1,1,1},
			{1,1,1,1,1},
			{0,1,1,1,0}
		};
		inicio = -2;
		
		int[] pixels = new int[4];		
		
		for (int i = 0; i < filtro.length; i++) {
			
			int x = inicio;
			for (int j = 0; j < filtro.length; j++) {
				
				try {
					
					raster.getPixel(px.x + x, px.y + inicio, pixels);
					Pixel pixel = new Pixel(pixels[0], pixels[1], pixels[2]);
					String padrao = padraoCanais(pixel);
					if(padroes.containsKey(padrao)) {
						padroes.put(padrao, padroes.get(padrao) + 1);
					} else {
						padroes.put(padrao, 1);
					}
					
				} catch (Exception e) {
//					e.printStackTrace();
				}
				x++;
				
			}
			inicio++;
			
		}
		
		
		String padrao = null;
		int maiorPadrao = 0;
		
		for (Map.Entry<String, Integer> entry : padroes.entrySet()) {
		    String key = entry.getKey();
		    Integer value = entry.getValue();

		    if(value > maiorPadrao) {
		    	padrao = key;
		    	maiorPadrao = value;
		    }
		    
		}
		
		return padrao;
		
		
	}
	
	private String padraoCanais(Pixel px) {
		
		byte[] padrao = new byte[]{0,0,0};
		
		int media = Math.round((px.r + px.g + px.b) / 3);

		int rg = px.r - px.g; 
		int gr = px.g - px.r; 
		int rb = px.r - px.b; 
		int br = px.b - px.r; 
		int gb = px.g - px.b; 
		int bg = px.b - px.g; 
		
		if(rg > 30 || rb > 30) {
			padrao[0] = 1;
		}
		
		if(gr > 30 || gb > 30) {
			padrao[1] = 1;
		}
		
		if(br > 30 || bg > 30) {
			padrao[2] = 1;
		}
		
		if( padrao[0] == 0 && padrao[1] == 0 && padrao[2] == 0 ) {
			
			if(media > 127)
				return "111";
			else
				return "000";
			
		}
		
		return String.valueOf(padrao[0]) + String.valueOf(padrao[1]) + String.valueOf(padrao[2]);
		
	}
	
	public BufferedImage inverterDoisQuadrantes(BufferedImage img, byte quadrante1, byte quadrante2) {
		
		WritableRaster rasterOrigin = img.getRaster();
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableRaster rasterTarget = newImg.getRaster();
		
		int pixels[] = new int[4];
		
		// cria a imagem
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				rasterOrigin.getPixel(i, j, pixels);
				rasterTarget.setPixel(i, j, pixels);
			}
		}
		
		// inverte primeiro quadrante
		int x = fimXQuadrante(img, quadrante1) -1;
		for (int i = inicioXQuadrante(img, quadrante1); i < fimXQuadrante(img, quadrante1) - 1; i++) {
			int y = fimYQuadrante(img, quadrante1) - 1;
			for (int j = inicioYQuadrante(img, quadrante1); j < fimYQuadrante(img, quadrante1) - 1; j++) {
				
				rasterOrigin.getPixel(i, j, pixels);
				rasterTarget.setPixel(x, y, pixels);
				y--;
			}			
			x--;
		}
		
		// inverte segundo quadrante
		x = fimXQuadrante(img, quadrante2) -1;
		for (int i = inicioXQuadrante(img, quadrante2); i < fimXQuadrante(img, quadrante2) - 1; i++) {
			int y = fimYQuadrante(img, quadrante2) - 1;
			for (int j = inicioYQuadrante(img, quadrante2); j < fimYQuadrante(img, quadrante2) - 1; j++) {
				
				rasterOrigin.getPixel(i, j, pixels);
				rasterTarget.setPixel(x, y, pixels);
				y--;
			}		
			x--;
		}
		
		try {
			newImg.setData(rasterTarget);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
		
	}
	
	private int inicioXQuadrante(BufferedImage img, byte numQuadrante) {
		
		if(numQuadrante == 1 || numQuadrante == 3) {
			return 1;
		} else {
			return Math.round(img.getWidth() / 2) -1;
		} 
		
	}
	
	private int fimXQuadrante(BufferedImage img, byte numQuadrante) {
		
		if(numQuadrante == 2 || numQuadrante == 4) {
			return img.getWidth();
		} else {
			return Math.round(img.getWidth() / 2) ;
		} 
		
	}
	
	private int inicioYQuadrante(BufferedImage img, byte numQuadrante) {
		
		if(numQuadrante == 1 || numQuadrante == 2) {
			return 1;
		} else {
			return Math.round(img.getHeight() / 2);
		} 
		
	}
	
	private int fimYQuadrante(BufferedImage img, byte numQuadrante) {
		
		if(numQuadrante == 3 || numQuadrante == 4) {
			return img.getHeight() -1;
		} else {
			return Math.round(img.getHeight() / 2);
		} 
		
	}
	
	public BufferedImage dividirDiagonalEEqualizar(BufferedImage img) {
		
		WritableRaster rasterOrigin = img.getRaster();
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableRaster rasterTarget = newImg.getRaster();
		
		int[] acumulado = new int[256];
		int[] vetor = vetorHistograma(img);
		int L = 0;
		int N = Math.round(img.getWidth() * img.getHeight() * 3);
		
		for (int i = 0; i < vetor.length; i++) {
			if(i > 0)
				acumulado[i] = vetor[i] + acumulado[i-1];
			else
				acumulado[i] = vetor[i];
			
			if(vetor[i] > 0)
				L++;
			
		}
		int tons = L - 1;
		
		Pixel pxDiagonal = new Pixel(0, 0);
		
		int[] pixels = new int[4];
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				
				rasterOrigin.getPixel(i, j, pixels);
				if(i == pxDiagonal.x && j == pxDiagonal.y) {
					pixels = new int[]{0,0,0,0};
				} else if(i >= pxDiagonal.x && j < pxDiagonal.y) {
					
					pixels[0] = Math.round((((float)tons / N)) * acumulado[ pixels[0] ]);
					pixels[1] = Math.round((((float)tons / N)) * acumulado[ pixels[1] ]);
					pixels[2] = Math.round((((float)tons / N)) * acumulado[ pixels[2] ]);
				}
				
				rasterTarget.setPixel(i, j, pixels);
				
			}	
			pxDiagonal.x++;
			pxDiagonal.y++;
		}
		
		try {
			newImg.setData(rasterTarget);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newImg;
	}
	
	public boolean identificarQuadradoFechado(BufferedImage img) {
		
		WritableRaster rasterOrigin = img.getRaster();
		
		boolean retanguloFechado = true;
		
		int[] pixels = new int[4];
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				
				rasterOrigin.getPixel(i, j, pixels);
				
				Pixel px = new Pixel(i, j, pixels[0], pixels[1], pixels[2]);
				if(pixelPreto(img, px)) {
					
					int vizinhosPretos = 0;
					
					if(pixelPreto(img, px.pixelCima()))
						vizinhosPretos++;
					if(pixelPreto(img, px.pixelDireita()))
						vizinhosPretos++;
					if(pixelPreto(img, px.pixelBaixo()))
						vizinhosPretos++;
					if(pixelPreto(img, px.pixelEsquerda()))
						vizinhosPretos++;
					
					if(vizinhosPretos < 2) {
						retanguloFechado = false;
						break;
					}						
					
				}
				
			}
			
			if(!retanguloFechado) {
				break;
			}
			
		}
		
		return retanguloFechado;
	}
	
	private boolean pixelPreto(BufferedImage img, Pixel px) {
		
		if(px.y < 0 || px.x < 0 || px.x > img.getWidth() - 1 || px.y > img.getHeight() - 1)
			return false;
		
		WritableRaster rasterOrigin = img.getRaster();
		int[] pixels = new int[4];
		rasterOrigin.getPixel(px.x, px.y, pixels);
		return pixels[0] < 200 && pixels[1] < 200 && pixels[2] < 200;
		
	}
	
	
}
