

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;

import java.io.FileWriter;
import java.security.PrivilegedActionException;
import java.util.Arrays;
import java.util.Random;
// TODO: Filtros simples (Saturação etc) / Inserção de imagens(Watermarks) / Scramble / Média das cores com análise de "mood"
public class MeuConversorImagem extends ConversorImagemAED{
    private BufferedImage imagem;
    
    public MeuConversorImagem(String urlImagemOrigem) {
        super(urlImagemOrigem);
        imagem = super.getImagem();
    }
    
    public void PostWaterMark(String urlImagemDestino, String urlImagemWatermark){}   
    public void SaturationFilter(String urlImagemDestino){}
    
    
    public void BlurEffect(String urlImagemDestino){
        
        try {
        
            BufferedImage imagemBlur = new BufferedImage(imagem.getWidth(), imagem.getHeight(), imagem.getType() );
            int height = imagem.getHeight();
            int width = imagem.getWidth();
        
            for (int x = 1; x < width-1; x++)
            for (int y = 1; y < height-1; y++) {
    
                int redAmount = 0;
                int greenAmount = 0;
                int blueAmount = 0;
    
                int pixel1 = imagem.getRGB(x - 1, y - 1);
                redAmount += (pixel1 >> 16) & 0xff;
                greenAmount += (pixel1 >> 8) & 0xff;
                blueAmount += (pixel1 >> 0) & 0xff;
    
                int pixel2 = imagem.getRGB(x, y - 1);
                redAmount += (pixel2 >> 16) & 0xff;
                greenAmount += (pixel2 >> 8) & 0xff;
                blueAmount += (pixel2 >> 0) & 0xff;
    
                int pixel3 = imagem.getRGB(x + 1, y - 1);
                redAmount += (pixel3 >> 16) & 0xff;
                greenAmount += (pixel3 >> 8) & 0xff;
                blueAmount += (pixel3 >> 0) & 0xff;
    
                int pixel4 = imagem.getRGB(x - 1, y);
                redAmount += (pixel4 >> 16) & 0xff;
                greenAmount += (pixel4 >> 8) & 0xff;
                blueAmount += (pixel4 >> 0) & 0xff;
    
                int pixel5 = imagem.getRGB(x, y);
                redAmount += (pixel5 >> 16) & 0xff;
                greenAmount += (pixel5 >> 8) & 0xff;
                blueAmount += (pixel5 >> 0) & 0xff;
    
                int pixel6 = imagem.getRGB(x + 1, y);
                redAmount += (pixel6 >> 16) & 0xff;
                greenAmount += (pixel6 >> 8) & 0xff;
                blueAmount += (pixel6 >> 0) & 0xff;
    
                int pixel7 = imagem.getRGB(x - 1, y + 1);
                redAmount += (pixel7 >> 16) & 0xff;
                greenAmount += (pixel7 >> 8) & 0xff;
                blueAmount += (pixel7 >> 0) & 0xff;
    
                int pixel8 = imagem.getRGB(x, y + 1);
                redAmount += (pixel8 >> 16) & 0xff;
                greenAmount += (pixel8 >> 8) & 0xff;
                blueAmount += (pixel8 >> 0) & 0xff;
    
                int pixel9 = imagem.getRGB(x + 1, y + 1);
                redAmount += (pixel9 >> 16) & 0xff;
                greenAmount += (pixel9 >> 8) & 0xff;
                blueAmount += (pixel9 >> 0) & 0xff;
    
                redAmount /= 9;
                greenAmount /= 9;
                blueAmount /= 9;
    
                int newPixel = (redAmount << 16) | (greenAmount << 8) | blueAmount;
                imagemBlur.setRGB(x, y, newPixel);
            }


            ImageIO.write(imagemBlur, "jpg", new File(urlImagemDestino));
            System.out.println("Imagem convertida para Blur");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ScrambleEffect(String urlImagemDestino) {
        Random r = new Random();
        try {
            int imageWidth = imagem.getWidth();
            int imageHeight = imagem.getHeight();
            BufferedImage finalImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
            
            boolean[][] placedArray = new boolean[imageHeight][imageWidth]; // isso daqui vai guardar a posição de todos os pixeis
            // ou seja
            // nós podemos verificar se a posição está true, e caso esteja, já sabemos que botamos um pixel lá já
            // assim não precisamos colocar uma imagem branca ou nada assim, porque já temos a informação necessária a evitar repetições
            int x, y;
            //Arrays.fill(placedArray, false);
            for (int i = 0; i < imageHeight; i++) { // save colors
                for (int j = 0; j < imageWidth; j++) {
                    placedArray[i][j] = false;
                }
            }
            
            for (int i = 0; i < imageHeight; i++) { // save colors
                for (int j = 0; j < imageWidth; j++) {
                  int cor = imagem.getRGB(i, j);
                  do{
                    x = r.nextInt(imageWidth);
                    y = r.nextInt(imageHeight);
                    System.out.println(x+" "+y);
                    if (placedArray[y][x] == false) {
                        System.out.println("added");
                        finalImage.setRGB(x, y, cor);
                        placedArray[y][x] = true;
                    }
                  } while(placedArray[y][x] == true);
                }
            }

            ImageIO.write(finalImage, "jpg", new File(urlImagemDestino));
            System.out.println("Imagem mexida! woOOooOOOoooOoooOoo");

        } catch (Exception e) {
            e.printStackTrace();
        }





    }
    public void GetMoodColor(String urlurlImagemDestino){}
    

    


}