import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    public void start(Stage primaryStage) {

        // Variáveis e objetos
        int i, largura_tela = 800, altura_tela = (int) (largura_tela * 0.75f);
        int y_caixa_jog = (int) (largura_tela * 0.3f);
        String cores[] = { "#8B0000", "#0000FF", "#008B00", "#B8860B", "#800080", "#000000" };
        Pane root = new Pane();
        Scene scene = new Scene(root, largura_tela, altura_tela);
        Menu menu = new Menu();
        ArrayList<Jogador> jogs = new ArrayList<Jogador>();
        Tabuleiro tabuleiro = new Tabuleiro(root, largura_tela, altura_tela, jogs);

        // Ajustando o tabuleiro
        tabuleiro.criarCaixaAvisos(root, largura_tela, altura_tela);

        // Configurando o menu
        menu.embaralharCores(cores);
        menu.configurarJogadores(jogs, cores);

        // ESSE BLOCO EH PARA SER SUBSTITUIDO POR UM METODO DA CLASSE MENU
        // Iniciando o jogo
        for (i = 0; i < cores.length; i++) {
            int teste = (int) (Math.random() * 3);
            Jogador jog;

            if (teste == 0)
                jog = new JogadorAzarado(cores[i], y_caixa_jog, root, largura_tela, altura_tela);
            else if (teste == 1)
                jog = new JogadorComum(cores[i], y_caixa_jog, root, largura_tela, altura_tela);
            else
                jog = new JogadorSortudo(cores[i], y_caixa_jog, root, largura_tela, altura_tela);

            jogs.add(jog);
            y_caixa_jog += (largura_tela / 25f);
        }
        // FIM DO BLOCO

        // Gerando o círculo que apontará para o próximo jogador
        tabuleiro.gerarCircProxJog(root, jogs.get(0), largura_tela, altura_tela);

        // Configurando a cena
        root.setStyle("-fx-background-color: #B0C4DE;");
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}