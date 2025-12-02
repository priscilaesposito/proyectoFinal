package recursos;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;

/**
 * Clase para manejar la carga de recursos (imágenes, iconos, etc.)
 * de la aplicación de streaming
 */
public class GestionRecursos {
    
    // Ruta base para los recursos
    private static final String RESOURCE_PATH = "/recursos/";
    
    /**
     * Cargar una imagen desde los recursos
     * @param fileName nombre del archivo de imagen
     * @return ImageIcon con la imagen cargada, o null si no se encontró
     */
    public static ImageIcon loadImage(String fileName) {
        try {
            URL imageURL = GestionRecursos.class.getResource(RESOURCE_PATH + fileName);
            if (imageURL != null) {
                return new ImageIcon(imageURL);
            } else {
                System.err.println("No se pudo encontrar la imagen: " + RESOURCE_PATH + fileName);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen " + fileName + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cargar una imagen redimensionada desde los recursos
     * @param fileName nombre del archivo de imagen
     * @param width ancho deseado
     * @param height alto deseado
     * @return ImageIcon con la imagen redimensionada, o null si no se encontró
     */
    public static ImageIcon loadScaledImage(String fileName, int width, int height) {
        ImageIcon originalIcon = loadImage(fileName);
        if (originalIcon != null) {
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return null;
    }
    
    /**
     * Cargar imagen con fallback a una imagen por defecto dibujada
     * @param fileName nombre del archivo de imagen
     * @param width ancho para la imagen de fallback
     * @param height alto para la imagen de fallback
     * @return ImageIcon con la imagen o una imagen generada por código
     */
    public static ImageIcon loadImageWithFallback(String fileName, int width, int height) {
        // Intentar cargar la imagen desde archivo
        ImageIcon loadedImage = loadScaledImage(fileName, width, height);
        
        if (loadedImage != null) {
            return loadedImage;
        }
        
        // Si no se pudo cargar, crear imagen por defecto
        return createDefaultDogStreamingImage(width, height);
    }
    
    /**
     * Crear una imagen por defecto del perrito viendo streaming
     * (imagen generada por código como fallback)
     */
    private static ImageIcon createDefaultDogStreamingImage(int width, int height) {
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fondo beige
        g2d.setColor(new java.awt.Color(255, 223, 186));
        g2d.fillRect(0, 0, width, height);
        
        // Dibujar el sofá (naranja)
        g2d.setColor(new java.awt.Color(255, 140, 0));
        g2d.fillRoundRect(width/6, height*2/3, width*2/3, height/4, 20, 20);
        
        // Dibujar el perrito (marrón claro)
        g2d.setColor(new java.awt.Color(205, 133, 63));
        // Cuerpo
        g2d.fillOval(width/3, height/2, width/4, height/5);
        // Cabeza
        g2d.fillOval(width/3 + width/8, height*2/5, width/6, width/6);
        // Orejas
        g2d.fillOval(width/3 + width/12, height*2/5 - width/15, width/12, width/8);
        g2d.fillOval(width/3 + width/6, height*2/5 - width/15, width/12, width/8);
        
        // Ojos (negros)
        g2d.setColor(java.awt.Color.BLACK);
        g2d.fillOval(width/3 + width/7, height*2/5 + width/20, 4, 4);
        g2d.fillOval(width/3 + width/6, height*2/5 + width/20, 4, 4);
        
        // TV (gris oscuro)
        g2d.setColor(new java.awt.Color(64, 64, 64));
        g2d.fillRect(width*2/3, height/4, width/4, height/6);
        
        // Pantalla TV (negro con logos simulados)
        g2d.setColor(java.awt.Color.BLACK);
        g2d.fillRect(width*2/3 + 10, height/4 + 10, width/4 - 20, height/6 - 20);
        
        // Logos en la TV
        g2d.setColor(java.awt.Color.RED);
        g2d.fillRect(width*2/3 + 15, height/4 + 15, 25, 15);
        g2d.setColor(new java.awt.Color(0, 150, 250));
        g2d.fillRect(width*2/3 + 45, height/4 + 15, 25, 15);
        g2d.setColor(new java.awt.Color(0, 200, 0));
        g2d.fillRect(width*2/3 + 15, height/4 + 35, 25, 15);
        g2d.setColor(new java.awt.Color(150, 0, 200));
        g2d.fillRect(width*2/3 + 45, height/4 + 35, 25, 15);
        
        g2d.dispose();
        
        return new ImageIcon(image);
    }
}