import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public abstract class Jogador {
    protected int casa_atual, y_caixa_jog, num_rodadas;
    protected boolean jogada_liberada;
    protected String cor, cor_extenso, texto_classe;
    protected TextField caixa_jog;

    public Jogador(String cor, int y_caixa_jog, Pane root, int largura_tela, int altura_tela) {
        this.cor = cor;
        this.cor_extenso = this.definirCorExtenso();
        this.y_caixa_jog = y_caixa_jog;
        this.casa_atual = 0;
        this.num_rodadas = 0;
        this.jogada_liberada = true;
        this.texto_classe = this.definirTextoClasse();

        if (root != null)
            this.criarCaixaTexto(root, largura_tela, altura_tela);
    }

    public int getYCaixaJog() {
        return this.y_caixa_jog;
    }

    // ESSE MÉTODO SE ADERE AO POLIMORFISMO
    public String definirTextoClasse() {
        return null;
    }

    public String definirCorExtenso() {
        String cor_extenso;

        switch (this.cor) {
            case "#8B0000":
                cor_extenso = "VERMELHO";
                break;
            case "#0000FF":
                cor_extenso = "AZUL";
                break;
            case "#008B00":
                cor_extenso = "VERDE";
                break;
            case "#B8860B":
                cor_extenso = "AMARELO";
                break;
            case "#800080":
                cor_extenso = "ROXO";
                break;
            default:
                cor_extenso = "PRETO";
        }

        return cor_extenso;
    }

    public void criarCaixaTexto(Pane root, int largura_tela, int altura_tela) {
        String formato_texto = String.format("JOGADOR %s %s: %d", this.texto_classe, this.cor_extenso, this.casa_atual);
        String formato_estilo = String.format("-fx-control-inner-background: %s; -fx-text-fill: white;", this.cor);

        // Ajustando caixa de texto
        this.caixa_jog = new TextField(formato_texto);
        this.caixa_jog.setEditable(false);
        this.caixa_jog.setStyle(formato_estilo);
        this.caixa_jog.setPrefSize(largura_tela / 3, altura_tela / 20);
        this.caixa_jog.setLayoutX(largura_tela / 3);
        this.caixa_jog.setLayoutY(this.y_caixa_jog);

        root.getChildren().add(this.caixa_jog);
    }

    public void andarCasas(int qtd_casas) {
        this.casa_atual += qtd_casas;

        if (this.casa_atual > 40)
            this.casa_atual = 40;

        this.atualizarTextoCaixa();
    }

    public void atualizarTextoCaixa() {
        String formato_texto = String.format("JOGADOR %s %s: %d", this.texto_classe, this.cor_extenso, this.casa_atual);
        this.caixa_jog.setText(formato_texto);
    }

    public String getCor() {
        return this.cor;
    }

    public void setJogadaLiberada(boolean jogada_liberada) {
        this.jogada_liberada = jogada_liberada;
    }

    public boolean getJogadaLiberada() {
        return this.jogada_liberada;
    }

    public void setCasaAtual(int casa_atual) {
        this.casa_atual = casa_atual;
        this.atualizarTextoCaixa();
    }

    public int getCasaAtual() {
        return this.casa_atual;
    }

    public void addRodada() {
        ++this.num_rodadas;
    }

    public void setNumRodadas(int num_rodadas) {
        this.num_rodadas = num_rodadas;
    }

    public int getNumRodadas() {
        return this.num_rodadas;
    }

}
