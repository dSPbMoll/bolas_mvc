package helpers.ss_animation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SpriteLoader {
    private HashMap<String, SpriteSheet> animations;
    private ArrayList<IndependentAnimationReproducer> reproducers;

    public SpriteLoader() {
        this.reproducers = new ArrayList<>();
        this.animations = new HashMap<>();
    }

    public Sprite loadImage(String imagePath) {
        try {
            return new Sprite(ImageIO.read(new File(imagePath)));
        } catch (IOException e) {
            System.out.println("Failed loading image \"" + imagePath +"\"\n" + e);
        }
        return null;
    }

    public SpriteSheet loadSpriteSheet(String sheetPath, int rows, int cols, int delay) {
        try {
            BufferedImage sheet = ImageIO.read(new File(sheetPath));
            if (sheet == null) throw new IOException("Imagen no encontrada: " + sheetPath);

            BufferedImage[] frames = new BufferedImage[rows * cols];
            int frameWidth = sheet.getWidth() / cols;
            int frameHeight = sheet.getHeight() / rows;

            // Limpieza de path para el nombre del archivo debug
            String debugName = new File(sheetPath).getName().replace(".", "_");

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    // 1. Obtener subimagen
                    BufferedImage subImage = sheet.getSubimage(j * frameWidth, i * frameHeight, frameWidth, frameHeight);

                    // 2. Crear copia limpia (ARGB)
                    BufferedImage cleanFrame = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = cleanFrame.createGraphics();
                    g.drawImage(subImage, 0, 0, null);
                    g.dispose();

                    frames[i * cols + j] = cleanFrame;

                    // ================== BLOQUE DE PRUEBA ==================
                    // Guardamos solo el primer frame de cada hoja para verificar
                    if (i == 0 && j == 0) {
                        File debugFile = new File("DEBUG_" + debugName + "_frame0.png");
                        ImageIO.write(cleanFrame, "png", debugFile);
                        System.out.println("--> PRUEBA GUARDADA: " + debugFile.getAbsolutePath());
                    }
                    // ======================================================
                }
            }
            return new SpriteSheet(sheet, frames, delay);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Añade esto a SpriteLoader.java

    public void debugGrid(String sheetPath, int rows, int cols) {
        try {
            BufferedImage sheet = ImageIO.read(new File(sheetPath));
            if (sheet == null) {
                System.out.println("No se pudo cargar: " + sheetPath);
                return;
            }

            // Creamos una copia para pintar encima
            BufferedImage debugImage = new BufferedImage(sheet.getWidth(), sheet.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = debugImage.createGraphics();
            g.drawImage(sheet, 0, 0, null);

            // Configuración del pincel rojo
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(3)); // Línea gruesa

            int frameWidth = sheet.getWidth() / cols;
            int frameHeight = sheet.getHeight() / rows;

            System.out.println("DEBUG GRID: " + sheetPath);
            System.out.println(" -> Rows: " + rows + ", Cols: " + cols);
            System.out.println(" -> Cell Size: " + frameWidth + "x" + frameHeight);

            // Pintamos la cuadrícula
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int x = j * frameWidth;
                    int y = i * frameHeight;

                    // Dibujar rectángulo de la celda
                    g.drawRect(x, y, frameWidth, frameHeight);

                    // Escribir el número de frame en la esquina
                    g.drawString("F" + (i * cols + j), x + 10, y + 20);
                }
            }
            g.dispose();

            // Guardamos la imagen con la rejilla pintada
            String debugName = "GRID_CHECK_" + new File(sheetPath).getName();
            File outputFile = new File(debugName);
            ImageIO.write(debugImage, "png", outputFile);

            System.out.println(" -> Imagen de comprobación guardada en: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}