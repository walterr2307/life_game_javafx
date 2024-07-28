import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    // Variáveis de instância para armazenar o número de jogadores, cores disponíveis, tipos de jogadores, etc.
    private int numPlayers = 1;
    private String cores_disponiveis[] = { "#8B0000", "#0000FF", "#008B00", "#B8860B", "#800080", "#000000" };
    private String cores_extenso[] = { "Vermelho", "Azul", "Verde", "Amarelo", "Roxo", "Preto" };
    private ArrayList<Integer> tipos_jogs = new ArrayList<Integer>();
    private ArrayList<String> cores = new ArrayList<>();
    private ArrayList<Jogador> jogs;
    private List<String> playerTypes = new ArrayList<>();

    @Override
    public void start(Stage primary_stage) {
        // Define o título da janela principal
        primary_stage.setTitle("Jogo de Tabuleiro");

        // Cria um diálogo para entrada do número de jogadores, com valor padrão "2"
        TextInputDialog dialog = new TextInputDialog("2");
        dialog.setTitle("Número de Jogadores");
        dialog.setHeaderText("Entre com o número de jogadores (2-6):");
        dialog.setContentText("Número de Jogadores:");

        // Mostra o diálogo e espera pelo resultado
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(num -> {
            // Converte a entrada para um número inteiro
            numPlayers = Integer.parseInt(num);
            // Verifica se o número de jogadores está no intervalo permitido (2-6)
            if (numPlayers < 2 || numPlayers > 6) {
                // Mostra um alerta se o número de jogadores for inválido e define o número de jogadores para 2
                showAlert("Número inválido de jogadores. Configuração para 2 jogadores.");
                numPlayers = 2;
            }
        });

        // Cria uma lista de cores disponíveis para os jogadores
        List<String> availableColorsExtenso = new ArrayList<>(Arrays.asList(cores_extenso));
        List<String> availableColors = new ArrayList<>(Arrays.asList(cores_disponiveis));
        // Inicializa a lista de jogadores
        jogs = new ArrayList<>();

        // Loop para configurar cada jogador
        for (int i = 0; i < numPlayers; i++) {
            // Cria um diálogo para selecionar o tipo de jogador
            ChoiceDialog<String> typeDialog = new ChoiceDialog<>("Comum", "Sortudo", "Azarado", "Comum");
            typeDialog.setTitle("Tipo de jogador");
            typeDialog.setHeaderText("Selecione o tipo de jogador " + (i + 1));
            typeDialog.setContentText("Tipo:");

            // Mostra o diálogo e espera pelo resultado
            Optional<String> typeResult = typeDialog.showAndWait();
            // Pega o tipo selecionado ou define para "Comum" se não selecionado
            String type = typeResult.orElse("Comum");
            playerTypes.add(type);

            // Adiciona o tipo de jogador à lista tipos_jogs com base na seleção do usuário
            if (type.equals("Azarado"))
                tipos_jogs.add(0);
            else if (type.equals("Comum"))
                tipos_jogs.add(1);
            else
                tipos_jogs.add(2);

            // Cria um diálogo para selecionar a cor do jogador
            ChoiceDialog<String> colorDialog = new ChoiceDialog<>(availableColorsExtenso.get(0), availableColorsExtenso);
            colorDialog.setTitle("Cor do jogador");
            colorDialog.setHeaderText("Selecione a cor do jogador " + (i + 1));
            colorDialog.setContentText("Cor:");

            // Mostra o diálogo e espera pelo resultado
            Optional<String> colorResult = colorDialog.showAndWait();
            colorResult.ifPresent(color -> {
                // Remove a cor selecionada das cores disponíveis em extenso
                int colorIndex = availableColorsExtenso.indexOf(color);
                availableColorsExtenso.remove(color);
                // Remove a cor correspondente das cores disponíveis em hexadecimal
                cores.add(availableColors.remove(colorIndex));
            });
        }

        // Mostra um alerta indicando que a configuração dos jogadores está completa
        showAlert("Jogadores e tipos selecionados. Iniciando configuração do jogo...");
        // Chama o método para começar o jogo
        comecarJogo();
    }

    // Método para mostrar alertas com mensagens
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação do Jogo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método para iniciar o jogo
    private void comecarJogo() {
        int largura_tela = 640, altura_tela = (int) (largura_tela * 0.75f);
        int y_caixa_jog = (int) (largura_tela * 0.3f);
        Pane root = new Pane();
        Scene scene = new Scene(root, largura_tela, altura_tela);
        Stage stage = new Stage();
        Menu menu = new Menu();
        Tabuleiro tabuleiro;

        // Configurando o menu
        menu.embaralharCores(cores);
        
        // Iniciando o jogo através de um método da classe Menu
        jogs = menu.inicializarJogadores(cores, tipos_jogs, y_caixa_jog, root, largura_tela, altura_tela);

        // Ajustando o tabuleiro
        tabuleiro = new Tabuleiro(stage, root, largura_tela, altura_tela, jogs);
        tabuleiro.criarCaixaAvisos(root, largura_tela, altura_tela);

        // Gerando o círculo que apontará para o próximo jogador
        tabuleiro.gerarCircProxJog(root, jogs.get(0), largura_tela, altura_tela);

        // Configurando a cena
        root.setStyle("-fx-background-color: #B0C4DE;");
        stage.setTitle("Game of Life");
        stage.setScene(scene);
        stage.show();
    }

    // Método main para iniciar a aplicação
    public static void main(String[] args) {
        launch(args);
    }
}