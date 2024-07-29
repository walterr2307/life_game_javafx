import java.util.ArrayList;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Tabuleiro {
    private int turno_atual;
    private Circle circ_prox_jog;
    private TextField caixa_dado1, caixa_dado2;
    private Button botao_dados, caixa_avisos;

    public Tabuleiro(Stage stage, Pane root, int largura_tela, int altura_tela, ArrayList<Jogador> jogs) {
        this.caixa_dado1 = gerarCaixaDado(largura_tela, altura_tela, 1);
        this.caixa_dado2 = gerarCaixaDado(largura_tela, altura_tela, 2);
        this.botao_dados = gerarBotao(largura_tela, altura_tela);
        this.turno_atual = 0;

        // Adicionando o evento de clique ao botão
        botao_dados.setOnAction(event -> {
            interagirBotao(jogs, largura_tela, altura_tela, stage, root);
        });

        root.getChildren().addAll(caixa_dado1, caixa_dado2, botao_dados);
    }

    private TextField gerarCaixaDado(int largura_tela, int altura_tela, int num_dado) {
        float x_caixa_texto = largura_tela * (31f / 72f);
        TextField caixa_dado = new TextField("0");

        if (num_dado == 2)
            x_caixa_texto += largura_tela * (7f / 72f);

        caixa_dado.setEditable(false);
        caixa_dado.setPrefWidth(largura_tela / (72f / 5f));
        caixa_dado.setPrefHeight(altura_tela / (65f / 5f));
        caixa_dado.setLayoutX(x_caixa_texto);
        caixa_dado.setLayoutY(altura_tela * 0.75f);

        return caixa_dado;
    }

    private Button gerarBotao(int largura_tela, int altura_tela) {
        Button botao = new Button("Girar os dados");

        botao.setPrefWidth(largura_tela / 6f);
        botao.setPrefHeight(altura_tela / 20f);
        botao.setLayoutX(largura_tela * (5f / 11.6f));
        botao.setLayoutY(altura_tela * 0.85f);

        return botao;
    }

    private int[] girarDados() {
        int dado1 = (int) (Math.random() * 6 + 1), dado2 = (int) (Math.random() * 6 + 1);
        int dados[] = { dado1, dado2 };
        return dados;
    }

    public void interagirBotao(ArrayList<Jogador> jogs, int largura_tela, int altura_tela, Stage stage, Pane root) {
        int dados[] = this.girarDados(), prox_turno = (this.turno_atual + 1) % jogs.size();
        Jogador jog = jogs.get(this.turno_atual);
        TelaGameOver game_over = new TelaGameOver(stage);

        // Incrementando o número de rodadas
        jog.addRodada();

        // INÍCIO DO ALGORITMO PARA A ATUALIZAÇÃO DOS DADOS
        if (jog instanceof JogadorSortudo) {
            while (dados[0] + dados[1] < 7)
                dados = this.girarDados();
        }

        if (jog instanceof JogadorAzarado) {
            while (dados[0] + dados[1] > 6)
                dados = this.girarDados();
        }
        // FIM DO ALGORITMO PARA A ATUALIZAÇÃO DOS DADOS

        // Ajustando as caixas dos dados e de avisos
        this.setCaixasTexto(dados);
        this.alterarCorCaixasTexto(jog.getCor());
        this.padronizarCaixaAvisos();

        // INÍCIO DO ALGORITMO PARA A MOVIMENTAÇÃO DAS PEÇAS

        // Verificando se a jogada está liberada
        if (jog.getCasaAtual() == 10 || jog.getCasaAtual() == 25 || jog.getCasaAtual() == 38) {
            if (jog.getJogadaLiberada() == true) {
                this.alterarCaixaAvisos("Jogada Bloqueada", jog.getCor());
                jog.setJogadaLiberada(false);
                this.turno_atual = prox_turno;
                return;
            } else {
                jog.setJogadaLiberada(true);
            }
        }

        // Movendo a peça
        jog.andarCasas(dados[0] + dados[1]);

        // Mudando o tipo de jogador
        if (jog.getCasaAtual() == 13) {
            this.alterarCaixaAvisos("Cartas Surpresa", jog.getCor());
            this.mostrarCartas(jogs, this.turno_atual, jog, largura_tela, altura_tela, root);
        }

        // Lógica das casas da sorte
        if (jog.getCasaAtual() == 5 || jog.getCasaAtual() == 15 || jog.getCasaAtual() == 30) {
            if (!(jog instanceof JogadorAzarado)) {
                this.alterarCaixaAvisos("Casa da Sorte", jog.getCor());
                jog.andarCasas(3);
            }
        }

        // Escolhendo um jogador para voltar para o início
        if (jog.getCasaAtual() == 17 || jog.getCasaAtual() == 27) {
            this.alterarCaixaAvisos("Voltar ao Início", jog.getCor());
            this.voltarAoInicio(jogs, jog, largura_tela, altura_tela);
        }

        // Trocando de lugar com o jogador mais atrás
        if (jog.getCasaAtual() == 20 || jog.getCasaAtual() == 35) {
            this.alterarCaixaAvisos("Troca de Lugar", jog.getCor());
            this.trocarCasas(jogs, jog);
        }

        // FIM DO ALGORITMO PARA A MOVIMENTAÇÃO DAS PEÇAS

        // Finalizando o jogo
        if (jog.getCasaAtual() >= 40)
            game_over.exibirTela(jogs);

        // Alterando para o próximo jogador
        if (dados[0] != dados[1])
            this.turno_atual = prox_turno;

        this.atualizarCircProxJog(jogs.get(this.turno_atual), altura_tela);

        // Colorindo as caixas dos dados
        this.alterarCorCaixasTexto(jog.getCor());
    }

    private void setCaixasTexto(int[] dados) {
        this.caixa_dado1.setText(Integer.toString(dados[0]));
        this.caixa_dado2.setText(Integer.toString(dados[1]));
    }

    public void alterarCorCaixasTexto(String cor) {
        String formato_estilo = String.format("-fx-control-inner-background: %s; -fx-text-fill: white;", cor);
        this.caixa_dado1.setStyle(formato_estilo);
        this.caixa_dado2.setStyle(formato_estilo);
    }

    private void mostrarCartas(ArrayList<Jogador> jogs, int index, Jogador jog, int largura_tela, int altura_tela,
            Pane root) {
        int tipos_carta[] = new int[2], embaralhar = (int) (Math.random() * 2);
        int largura_janela = largura_tela / 2, altura_janela = altura_tela / 2;
        Button botao1 = new Button("Escolha"), botao2 = new Button("Escolha");
        Pane root_cartas = new Pane();
        Scene cena = new Scene(root_cartas, largura_janela, altura_janela);
        Stage janela = new Stage();
        PauseTransition pausa = new PauseTransition(Duration.seconds(1));

        if (jog instanceof JogadorAzarado) {
            tipos_carta[0] = 1;
            tipos_carta[1] = 2;
        } else if (jog instanceof JogadorComum) {
            tipos_carta[0] = 0;
            tipos_carta[1] = 2;
        } else {
            tipos_carta[0] = 0;
            tipos_carta[1] = 1;
        }

        if (embaralhar == 1) {
            embaralhar = tipos_carta[0];
            tipos_carta[0] = tipos_carta[1];
            tipos_carta[1] = embaralhar;
        }

        // Configurando os botões
        botao1.setPrefSize(largura_janela / 4f, largura_janela / 4f);
        botao1.setLayoutX(largura_janela / 5f);
        botao1.setLayoutY((altura_janela - largura_janela / 5f) / 2f);

        botao2.setPrefSize(largura_janela / 4f, largura_janela / 4f);
        botao2.setLayoutX(largura_janela * (11f / 20f));
        botao2.setLayoutY((altura_janela - largura_janela / 5f) / 2f);

        // Eventos para os botões
        botao1.setOnAction(event -> {
            this.configurarBotaoCartas(jogs, index, botao1, jog, tipos_carta[0], largura_tela, altura_tela, root);
            jog.atualizarTextoCaixa();
            root_cartas.setDisable(true);
            pausa.setOnFinished(e -> janela.close());
            pausa.play();
        });

        botao2.setOnAction(event -> {
            this.configurarBotaoCartas(jogs, index, botao2, jog, tipos_carta[1], largura_tela, altura_tela, root);
            jog.atualizarTextoCaixa();
            root_cartas.setDisable(true);
            pausa.setOnFinished(e -> janela.close());
            pausa.play();
        });

        // Configurando o root
        root_cartas.getChildren().addAll(botao1, botao2);
        root_cartas.setStyle("-fx-background-color: #B0C4DE;");

        // Configurando a janela
        janela.initModality(Modality.APPLICATION_MODAL);
        janela.setTitle("Escolha uma carta: ");
        janela.setScene(cena);
        janela.show();
    }

    private void configurarBotaoCartas(ArrayList<Jogador> jogs, int index, Button botao, Jogador jog, int indice,
            int largura, int altura, Pane root) {

        int casa_atual = jog.getCasaAtual(), num_rodadas = jog.getNumRodadas();
        Jogador novo_jog;

        if (indice == 0)
            novo_jog = new JogadorAzarado(jog.getCor(), jog.getYCaixaJog(), root, largura, altura);
        else if (indice == 1)
            novo_jog = new JogadorComum(jog.getCor(), jog.getYCaixaJog(), root, largura, altura);
        else
            novo_jog = new JogadorSortudo(jog.getCor(), jog.getYCaixaJog(), root, largura, altura);

        novo_jog.setCasaAtual(casa_atual);
        novo_jog.setNumRodadas(num_rodadas);
        botao.setText(novo_jog.definirTextoClasse());
        jogs.set(index, novo_jog);
    }

    private void trocarCasas(ArrayList<Jogador> jogs, Jogador jog) {
        int i, menor_casa = 41, qtd_passos;
        Jogador menor_jog = new JogadorComum(" ", 0, null, 0, 0);

        for (i = 0; i < jogs.size(); i++) {
            if (jogs.get(i).getCasaAtual() < menor_casa) {
                menor_casa = jogs.get(i).getCasaAtual();
                menor_jog = jogs.get(i);
            }
        }

        qtd_passos = jog.getCasaAtual() - menor_jog.getCasaAtual();
        menor_jog.andarCasas(qtd_passos);
        jog.andarCasas(-qtd_passos);
    }

    private void voltarAoInicio(ArrayList<Jogador> jogs, Jogador jog, int largura_tela, int altura_tela) {
        int largura_janela = largura_tela / 2, altura_janela = altura_tela / 2;
        int y_pos = (int) (altura_janela * 0.15f);
        String formato_estilo;
        Button botoes[] = new Button[jogs.size() - 1];
        Pane root_janela = new Pane();
        Scene cena = new Scene(root_janela, largura_janela, altura_janela);
        Stage janela = new Stage();
        PauseTransition pausa = new PauseTransition(Duration.seconds(1));
        ArrayList<Jogador> aux_jogs = new ArrayList<>();

        // Impedindo o jogador da vez de voltar ao início
        for (Jogador j : jogs) {
            if (!j.equals(jog)) {
                aux_jogs.add(j);
            }
        }

        // Criando os botões
        for (int i = 0; i < botoes.length; i++) {
            botoes[i] = new Button("Selecionar esse jogador");
            botoes[i].setPrefSize(largura_janela / 1.5f, altura_janela / 10);
            botoes[i].setLayoutX(largura_janela / 6f);
            botoes[i].setLayoutY(y_pos);
            y_pos += (altura_janela * 0.15f);
        }

        // Alterando a cor dos botões
        for (int i = 0; i < botoes.length; i++) {
            formato_estilo = String.format("-fx-background-color: %s; -fx-text-fill: white;", aux_jogs.get(i).getCor());
            botoes[i].setStyle(formato_estilo);
        }

        // Ajustando os eventos dos botões
        for (int i = 0; i < botoes.length; i++) {
            final int index = i; // Variável não modificável para o evento (função lambda)

            botoes[i].setOnAction(event -> {
                aux_jogs.get(index).setCasaAtual(0);
                root_janela.setDisable(true);
                pausa.setOnFinished(e -> janela.close());
                pausa.play();
            });
        }

        // Configurando o root
        root_janela.getChildren().addAll(botoes);
        root_janela.setStyle("-fx-background-color: #B0C4DE;");

        // Configurando a janela
        janela.initModality(Modality.APPLICATION_MODAL);
        janela.setTitle("Voltar ao início");
        janela.setScene(cena);
        janela.show();
    }

    public void criarCaixaAvisos(Pane root, int largura_tela, int altura_tela) {
        Rectangle borda_inferior = new Rectangle(largura_tela, 5);
        StackPane stack_pane = new StackPane();

        this.caixa_avisos = new Button("Game of Life");
        this.caixa_avisos.setFont(Font.font("Times New Roman", largura_tela * 0.08f));
        this.caixa_avisos.setPrefSize(largura_tela, altura_tela * 0.3f);
        this.caixa_avisos.setStyle("-fx-background-color: #000080; -fx-text-fill: white;");

        // Criar uma linha retangular para simular a borda na parte inferior
        borda_inferior.setFill(Color.WHITE);
        borda_inferior.setTranslateY(altura_tela * 0.15f); // Posiciona a borda na parte inferior do botão

        // Adicionar o botão e a borda ao root
        stack_pane.getChildren().addAll(caixa_avisos, borda_inferior);
        root.getChildren().add(stack_pane);
    }

    private void padronizarCaixaAvisos() {
        this.caixa_avisos.setStyle("-fx-background-color: #000080; -fx-text-fill: white;");
        this.caixa_avisos.setText("Game of Life");
    }

    private void alterarCaixaAvisos(String texto, String cor) {
        String formato_estilo = String.format("-fx-background-color: %s; -fx-text-fill: white;", cor);
        this.caixa_avisos.setStyle(formato_estilo);
        this.caixa_avisos.setText(texto);
    }

    public void gerarCircProxJog(Pane root, Jogador jog, int largura_tela, int altura_tela) {
        float raio = altura_tela / 40f;

        this.circ_prox_jog = new Circle(largura_tela * 0.285f, jog.getYCaixaJog() + raio, raio);
        this.circ_prox_jog.setFill(Color.TRANSPARENT);
        this.circ_prox_jog.setStroke(Color.WHITE);
        this.circ_prox_jog.setStrokeWidth(4); // Largura da borda

        root.getChildren().add(this.circ_prox_jog);
    }

    private void atualizarCircProxJog(Jogador jog, int altura_tela) {
        this.circ_prox_jog.setCenterY(jog.getYCaixaJog() + altura_tela / 40f);
    }

}
