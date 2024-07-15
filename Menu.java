import java.util.ArrayList;

public class Menu {

    public void embaralharCores(String[] cores) {
        int i, j;
        String copia;

        for (i = cores.length - 1; i > 0; i--) {
            j = (int) (Math.random() * (i + 1));
            copia = cores[i];
            cores[i] = cores[j];
            cores[j] = copia;
        }
    }

    // METODO PARA SER AJUSTADO
    public void configurarJogadores(ArrayList<Jogador> jogs, String[] cores) {

    }
}
