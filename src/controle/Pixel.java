package controle;

public class Pixel {
	
	public int x,y,r,g,b;
	
	public Pixel(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Pixel(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Pixel(int x, int y, int r, int g, int b) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Pixel pixelCima() {
		return new Pixel(x, y - 1);
	}
	
	public Pixel pixelBaixo() {
		return new Pixel(x, y + 1);
	}
	
	public Pixel pixelEsquerda( ) {
		return new Pixel(x - 1, y);
	}
	
	public Pixel pixelDireita( ) {
		return new Pixel(x + 1, y);
	}
	
	public Pixel pixelCimaDireita( ) {
		return new Pixel(x + 1, y - 1);
	}
	
	public Pixel pixelCimaEsquerda( ) {
		return new Pixel(x - 1, y - 1);
	}
	
	public Pixel pixelBaixoDireita( ) {
		return new Pixel(x + 1, y + 1);
	}
	
	public Pixel pixelBaixoEsquerda( ) {
		return new Pixel(x - 1, y + 1);
	}
	
	public int getCor(RGB rgb) {
		if (rgb.equals(RGB.R))
			return r;
		else if (rgb.equals(RGB.G))
			return g;
		else if (rgb.equals(RGB.B))
			return b;
		
		return -1;
	}
	
}
