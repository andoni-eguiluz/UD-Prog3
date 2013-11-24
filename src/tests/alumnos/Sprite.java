package tests.alumnos;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URL;
import javax.imageio.ImageIO;


class Sprite implements ImageObserver{
    protected int posx;
	protected int posy;
	protected int alto;
	protected int ancho;
    protected boolean active;
    protected int frame;
	private int nFrames;
    private Image[] sprites;
    
    /**
     * Metodo de creacion del objeto
     * @param nFrames Numero de imagenes que compone el Sprite
     */

    public Sprite (int nFrames)
    {
        active = false;
        frame=1;
        this.nFrames = nFrames;
        sprites = new BufferedImage[nFrames+1];
    }
    
    public void setX (int x)
    {
        posx = x;
    }
    
    public void setY (int y)
    {
        posy = y;
    }
    
    int getX ()
    {
        return posx;
    }  
    
    int getY ()
    {
        return posy;
    }
    
    public int getAlto() {
		return alto;
	}
    
    public int getAltoReal() {
		return getImagen(1).getHeight(this);
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public int getAncho() {
		return ancho;
	}
	
	public int getAnchoReal() {
		return getImagen(1).getWidth(this);
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}
    
    public void  on ()
    {
        active = true;
    }
    
    public void  off ()
    {
        active = false;
    }
    
    public boolean isActive ()
    {
        return active;
    }
    
    public void selFrame (int frameno)
    {
        frame = frameno;
    }
    
    public int getFrame ()
    {
	    return frame;
    }
    
    public Image getImagen (int x)
    {
	    return sprites[x];
    }
    
    public int frames ()
    {
        return nFrames;
    }
    
    public void addFrame (int frameno, String path)
    {
        sprites[frameno] = loadImage(path);
    }
    
    /**
     * Metodo encargado de la carga de imagenes
     * @param nombre Direccion en la que se encuentra la imagen
     * @return Devuelve la imagen
     */
    
    public BufferedImage loadImage(String nombre) {

    	URL url=null;
    	try {
    	    url = getClass().getResource(nombre);
    	    return ImageIO.read(url);
    	} catch (Exception e) {
    	    System.out.println("No se pudo cargar la imagen " + nombre +" de "+url);
    	    System.out.println("El error fue : "+e.getClass().getName()+" "+e.getMessage());
    	    System.exit(0);
    	    return null;
    	}
    }
    
    /**
     *  Metodo encargado de dibujar el objeto
     *  @param g Objeto grafico
     */
    
    public void draw(Graphics g)
    {
    	g.drawImage(sprites[frame], posx, posy, this);
    }

	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}

}                         

