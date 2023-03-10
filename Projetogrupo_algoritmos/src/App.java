
public class App {

   
    public static void main(String[] args) {
           
            
        MeuConversorImagem minhaImagem = new MeuConversorImagem("../imagens/teste.jpg");
        minhaImagem.BlurEffect("../imagens/testeBlur.jpg");
        minhaImagem.ScrambleEffect("../imagens/scramble.jpg");
          
    }
    
}
