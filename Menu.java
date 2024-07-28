import java.util.ArrayList;
import java.util.Collections;
import javafx.scene.layout.Pane;

class Menu {
    public void embaralharCores(ArrayList<String> cores) {
        Collections.shuffle(cores);
    }

    public ArrayList<Jogador> inicializarJogadores(ArrayList<String> cores, ArrayList<Integer> tipos_jogs,
            int y_caixa_jog, Pane root, int largura_tela, int altura_tela) {
        int copia = tipos_jogs.get(0);
        boolean iguais = true;
        ArrayList<Jogador> jogs = new ArrayList<>();

        // ALGORITMO PARA EVITAR QUE O TIPO DE JOGADORES SEJAM TODOS O MESMO
        for (Integer tipo_jog : tipos_jogs) {
            if (tipo_jog != tipos_jogs.get(0)) {
                iguais = false;
                break;
            }
        }

        if (iguais == true) {
            while (tipos_jogs.get(0) == copia) {
                tipos_jogs.set(0, (int) (Math.random() * 3));
            }
        }
        // FIM DO ALGORITMO

        for (int i = 0; i < cores.size(); i++) {
            Jogador jog;

            if (tipos_jogs.get(i) == 0) {
                jog = new JogadorAzarado(cores.get(i), y_caixa_jog, root, largura_tela, altura_tela);
            } else if (tipos_jogs.get(i) == 1) {
                jog = new JogadorComum(cores.get(i), y_caixa_jog, root, largura_tela, altura_tela);
            } else {
                jog = new JogadorSortudo(cores.get(i), y_caixa_jog, root, largura_tela, altura_tela);
            }
            jogs.add(jog);
            y_caixa_jog += (largura_tela / 25f);

        }

        return jogs;
    }
}