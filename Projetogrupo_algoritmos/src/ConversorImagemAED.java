

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;

import java.io.FileWriter;

/**
 *
 * @author alexandrebarao
 */
public class ConversorImagemAED {

    private BufferedImage imagem;

    public ConversorImagemAED(String urlImagemOrigem) {

        try {
            imagem = ImageIO.read(new File(urlImagemOrigem));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void converteBW(String urlImagemDestino) {

        try {

            BufferedImage imagemBW = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < imagem.getHeight(); i++) {
                for (int j = 0; j < imagem.getWidth(); j++) {

                    Color cor = new Color(imagem.getRGB(j, i));
                    imagemBW.setRGB(j, i, getCinza(cor).getRGB());
                }
            }

            ImageIO.write(imagemBW, "jpg", new File(urlImagemDestino));
            System.out.println("Imagem convertida para B&W");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Color getCinza(Color cor) {

        int r = (int) (cor.getRed() * 0.3);
        int g = (int) (cor.getGreen() * 0.59);
        int b = (int) (cor.getBlue() * 0.11);

        return new Color(r + g + b, r + g + b, r + g + b);
    }

    public void histogramaCsvBW(String urlFicheiroDestino) {

        try {

            int v[] = new int[256];

            System.out.println("A processar...");

            for (int i = 0; i < imagem.getHeight(); i++) {
                for (int j = 0; j < imagem.getWidth(); j++) {

                    Color cor = new Color(imagem.getRGB(j, i));

                    ++v[cor.getRed()];

                }
            }

            FileWriter ficheiro = new FileWriter(urlFicheiroDestino);

            for (int i = 0; i <= 255; i++) {
                ficheiro.write(i + ";" + v[i] + "\n");
            }

            ficheiro.close();
            System.out.println("Histograma gerado no ficheiro CSV: " + urlFicheiroDestino);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void binarizarBW(String urlImagemDestino, int limiteRGB) {

        try {

            BufferedImage imagemBW = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < imagem.getHeight(); i++) {
                for (int j = 0; j < imagem.getWidth(); j++) {

                    Color cor = new Color(imagem.getRGB(j, i));

                    if (cor.getRed() < limiteRGB) {
                        imagemBW.setRGB(j, i, new Color(0, 0, 0).getRGB());
                        //imagemBW.setRGB(j, i, new Color(255, 255, 255).getRGB());
                    } else {
                        imagemBW.setRGB(j, i, new Color(255, 255, 255).getRGB());
                        // imagemBW.setRGB(j, i, new Color(0, 0, 0).getRGB());
                    }

                }
            }

            ImageIO.write(imagemBW, "jpg", new File(urlImagemDestino));
            System.out.println("Imagem binarizada (limite: " + limiteRGB + ")");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    
    
    
    public void negativoBW(String urlImagemDestino) {

        try {

            BufferedImage imagemBW = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < imagem.getHeight(); i++) {
                for (int j = 0; j < imagem.getWidth(); j++) {

                    Color cor = new Color(imagem.getRGB(j, i));

                    int novoRGB = 255 - cor.getRed();

                    imagemBW.setRGB(j, i, new Color(novoRGB, novoRGB, novoRGB).getRGB());

                }
            }

            ImageIO.write(imagemBW, "jpg", new File(urlImagemDestino));
            System.out.println("Negativo gerado");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // https://aryamansharda.medium.com/how-image-edge-detection-works-b759baac01e2 
    public void detetarContornosBW(String urlImagemDestino) {

        try {

            BufferedImage imagemBW = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_INT_RGB);

            int Gx[][] = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
            int Gy[][] = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};

            int Mx[][] = new int[imagem.getHeight()][imagem.getWidth()];
            int My[][] = new int[imagem.getHeight()][imagem.getWidth()];

            int M[][] = new int[imagem.getHeight()][imagem.getWidth()];

            int maiorGradiente = 0;

            for (int l = 0 + 1; l < (imagem.getHeight() - 1); l++) {
                for (int c = 0 + 1; c < (imagem.getWidth() - 1); c++) {

                    // Mx
                    Mx[l - 1][c - 1] = new Color(imagem.getRGB(c - 1, l - 1)).getRed();
                    Mx[l - 1][c] = new Color(imagem.getRGB(c, l - 1)).getRed();
                    Mx[l - 1][c + 1] = new Color(imagem.getRGB(c + 1, l - 1)).getRed();

                    Mx[l][c - 1] = new Color(imagem.getRGB(c - 1, l)).getRed();
                    Mx[l][c] = new Color(imagem.getRGB(c, l)).getRed();
                    Mx[l][c + 1] = new Color(imagem.getRGB(c + 1, l)).getRed();

                    Mx[l + 1][c - 1] = new Color(imagem.getRGB(c - 1, l + 1)).getRed();
                    Mx[l + 1][c] = new Color(imagem.getRGB(c, l + 1)).getRed();
                    Mx[l + 1][c + 1] = new Color(imagem.getRGB(c + 1, l + 1)).getRed();

                    Mx[l][c] = (Gx[0][0] * Mx[l - 1][c - 1])
                            + (Gx[0][1] * Mx[l - 1][c])
                            + (Gx[0][2] * Mx[l - 1][c + 1])
                            + (Gx[1][0] * Mx[l][c - 1])
                            + (Gx[1][1] * Mx[l][c])
                            + (Gx[1][2] * Mx[l][c + 1])
                            + (Gx[2][0] * Mx[l + 1][c - 1])
                            + (Gx[2][1] * Mx[l + 1][c])
                            + (Gx[2][2] * Mx[l + 1][c + 1]);

                    // My
                    My[l - 1][c - 1] = new Color(imagem.getRGB(c - 1, l - 1)).getRed();
                    My[l - 1][c] = new Color(imagem.getRGB(c, l - 1)).getRed();
                    My[l - 1][c + 1] = new Color(imagem.getRGB(c + 1, l - 1)).getRed();

                    My[l][c - 1] = new Color(imagem.getRGB(c - 1, l)).getRed();
                    My[l][c] = new Color(imagem.getRGB(c, l)).getRed();
                    My[l][c + 1] = new Color(imagem.getRGB(c + 1, l)).getRed();

                    My[l + 1][c - 1] = new Color(imagem.getRGB(c - 1, l + 1)).getRed();
                    My[l + 1][c] = new Color(imagem.getRGB(c, l + 1)).getRed();
                    My[l + 1][c + 1] = new Color(imagem.getRGB(c + 1, l + 1)).getRed();

                    My[l][c] = (Gy[0][0] * My[l - 1][c - 1])
                            + (Gy[0][1] * My[l - 1][c])
                            + (Gy[0][2] * My[l - 1][c + 1])
                            + (Gy[1][0] * My[l][c - 1])
                            + (Gy[1][1] * My[l][c])
                            + (Gy[1][2] * My[l][c + 1])
                            + (Gy[2][0] * My[l + 1][c - 1])
                            + (Gy[2][1] * My[l + 1][c])
                            + (Gy[2][2] * My[l + 1][c + 1]);

                    M[l][c] = (int) (Math.sqrt(Math.pow(Mx[l][c], 2) + Math.pow(My[l][c], 2)));

                    if (M[l][c] > maiorGradiente) {
                        maiorGradiente = M[l][c];
                    }
                    
                    
                    //System.out.println(M[l][c] + " " + maiorGradiente);
                }
            }

            for (int i = 0; i < imagem.getHeight(); i++) {
                for (int j = 0; j < imagem.getWidth(); j++) {
                    
                    // normalização... 
                    
                    M[i][j] = M[i][j] * 255 / maiorGradiente;
                    
                  
                    // System.out.println(M[i][j] + " " + maiorGradiente);

                    if (M[i][j] >= 0 && M[i][j] <= 255) {
                        
                        Color cor = new Color(M[i][j], M[i][j], M[i][j]);
                        imagemBW.setRGB(j, i, cor.getRGB());
                        
                    } else {
                        System.out.println(M[i][j] + " " + maiorGradiente);
                    }
                }

            }

            ImageIO.write(imagemBW, "jpg", new File(urlImagemDestino));

            System.out.println("Deteção de contornos efetuada");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //
    // 0 - Red 
    // 1 - Green 
    // 2 - Blue
    //
    public void converteUmaCorRGB(String urlImagemDestino, int rgb) {

        try {

            BufferedImage imagemBW = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < imagem.getHeight(); i++) {
                for (int j = 0; j < imagem.getWidth(); j++) {

                    Color cor = new Color(imagem.getRGB(j, i));
                    imagemBW.setRGB(j, i, getUmaCorRGB(cor, rgb).getRGB());
                }
            }

            ImageIO.write(imagemBW, "jpg", new File(urlImagemDestino));
            System.out.println("Imagem convertida para uma cor RGB");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Color getUmaCorRGB(Color cor, int rgb) {

        Color c = new Color(cor.getRGB());

        switch (rgb) {
            case 0:

                c = new Color(cor.getRed(), 0, 0);
                break;

            case 1:
                c = new Color(0, cor.getGreen(), 0);
                break;

            case 2:
                c = new Color(0, 0, cor.getBlue());
                ;
                break;

        }

        return c;
    }

    public void rodarEsquerda(String urlImagemDestino) {

        try {

            BufferedImage imagemBW = new BufferedImage(imagem.getHeight(), imagem.getWidth(), BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < imagem.getHeight(); i++) {
                for (int j = 0; j < imagem.getWidth(); j++) {

                    Color cor = new Color(imagem.getRGB(j, i));

                    int li = (imagem.getWidth() - 1) - j;
                    int c = i;
                    imagemBW.setRGB(c, li, cor.getRGB()); // x, y
                }
            }

            ImageIO.write(imagemBW, "jpg", new File(urlImagemDestino));
            System.out.println("Imagem rodada para a esquerda");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void espelhar(String urlImagemDestino) {

        try {

            BufferedImage imagemBW = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < imagem.getHeight(); i++) {
                for (int j = 0; j < imagem.getWidth(); j++) {

                    Color cor = new Color(imagem.getRGB(j, i));

                    // imagemBW.setRGB(j, i, cor.getRGB());
                    int li = i;
                    int c = (imagem.getWidth() - 1) - j;;
                    imagemBW.setRGB(c, li, cor.getRGB()); // x, y
                }
            }

            ImageIO.write(imagemBW, "jpg", new File(urlImagemDestino));
            System.out.println("Imagem espelhada");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void criarMargem(int margem, String urlImagemDestino) {

        try {

            BufferedImage imagemBW = new BufferedImage(imagem.getWidth() + 2 * margem, imagem.getHeight() + 2 * margem, BufferedImage.TYPE_INT_RGB);

            // varrimento para gerar margem
            for (int i = 0; i < margem; i++) {
                for (int j = 0; j < imagemBW.getWidth(); j++) {
                    Color cor = new Color(255, 255, 255);
                    imagemBW.setRGB(j, i, cor.getRGB());
                }
            }

            for (int i = margem; i < margem + imagem.getHeight(); i++) {
                for (int j = 0; j < imagemBW.getWidth(); j++) {
                    if (j < margem || j > margem + imagem.getWidth()) {
                        Color cor = new Color(255, 255, 255);
                        imagemBW.setRGB(j, i, cor.getRGB());
                    }
                }
            }

            for (int i = imagemBW.getHeight() - margem; i < imagemBW.getHeight(); i++) {
                for (int j = 0; j < imagemBW.getWidth(); j++) {
                    Color cor = new Color(255, 255, 255);
                    imagemBW.setRGB(j, i, cor.getRGB());
                }
            }

            for (int i = 0; i < imagem.getHeight(); i++) {
                for (int j = 0; j < imagem.getWidth(); j++) {

                    Color cor = new Color(imagem.getRGB(j, i));
                    imagemBW.setRGB(margem + j, margem + i, cor.getRGB());
                }
            }

            ImageIO.write(imagemBW, "jpg", new File(urlImagemDestino));
            System.out.println("Imagem convertida com margem de " + margem + " pixeis.");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public BufferedImage getImagem() {
        //System.out.println("oi");
        return imagem;
    }

}
