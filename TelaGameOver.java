import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TelaGameOver {
    private Stage stage;

    public TelaGameOver(Stage stage) {
        this.stage = stage;
    }

    public void exibirTela(ArrayList<Jogador> jogs) {
        Jogador jog = encontrarVencedor(jogs);
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: black;");

        Label titulo = new Label("GAME OVER");
        titulo.setStyle("-fx-font-size: 40px; -fx-text-fill: red;");

        Label vencedor;
        if (jog != null) {
            vencedor = new Label("Jogador Vencedor: " + jog.getCorExtenso());
        } else {
            vencedor = new Label("Nenhum vencedor encontrado");
        }
        vencedor.setStyle("-fx-text-fill: white;");

        Label estatisticas = criarEstatisticas(jogs);
        estatisticas.setStyle("-fx-text-fill: white;");

        root.getChildren().addAll(titulo, vencedor, estatisticas);

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Tela de Game Over");
        stage.setScene(scene);
        stage.show();
    }

    private Jogador encontrarVencedor(ArrayList<Jogador> jogs) {
        for (Jogador jogador : jogs) {
            if (jogador.getCasaAtual() >= 40) {
                return jogador;
            }
        }
        return null; // Caso não haja vencedor, mas isso não deve acontecer
    }

    private Label criarEstatisticas(ArrayList<Jogador> jogs) {
        StringBuilder estatisticas = new StringBuilder("Estatísticas Finais:\n");
        for (Jogador jogador : jogs) {
            estatisticas.append(String.format("%s - Posição: %d, Jogadas: %d%n",
                    jogador.getCorExtenso(), jogador.getCasaAtual(), jogador.getNumRodadas()));
        }
        Label estatisticasLabel = new Label(estatisticas.toString());
        estatisticasLabel.setStyle("-fx-text-fill: white;");
        return estatisticasLabel;
    }
}