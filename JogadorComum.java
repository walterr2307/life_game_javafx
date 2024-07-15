import javafx.scene.layout.Pane;

public class JogadorComum extends Jogador {

    public JogadorComum(String cor, int y_botao, Pane root, int largura_tela, int altura_tela) {
        super(cor, y_botao, root, largura_tela, altura_tela);
    }

    public String definirTextoClasse() {
        return "COMUM";
    }

}
