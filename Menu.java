import java.util.ArrayList;
import javafx.scene.layout.Pane;

class Menu {
    public void embaralhar(ArrayList<String> cores, ArrayList<Integer> tipos_jogs) {
        int i, j, copia_tipo;
        String copia_cor;

        for (i = cores.size() - 1; i > 0; i--) {
            j = (int) (Math.random() * (i + 1));

            copia_cor = cores.get(i);
            cores.set(i, cores.get(j));
            cores.set(j, copia_cor);

            copia_tipo = tipos_jogs.get(i);
            tipos_jogs.set(i, tipos_jogs.get(j));
            tipos_jogs.set(j, copia_tipo);
        }
    }

    public ArrayList<Jogador> inicializarJogadores(ArrayList<String> cores, ArrayList<Integer> tipos_jogs,
            int y_caixa_jog, Pane root, int largura_tela, int altura_tela) {
        int indice = (int) (Math.random() * tipos_jogs.size()), copia = tipos_jogs.get(indice);
        boolean iguais = true;
        ArrayList<Jogador> jogs = new ArrayList<>();

        // ALGORITMO PARA EVITAR QUE O TIPO DE JOGADORES SEJAM TODOS O MESMO
        for (Integer tipo_jog : tipos_jogs) {
            if (tipo_jog != tipos_jogs.get(indice)) {
                iguais = false;
                break;
            }
        }

        if (iguais == true) {
            while (tipos_jogs.get(indice) == copia) {
                tipos_jogs.set(indice, (int) (Math.random() * 3));
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