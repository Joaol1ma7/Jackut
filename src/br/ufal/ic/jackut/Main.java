package br.ufal.ic.jackut;

import easyaccept.EasyAccept;

/**
 * Classe que executa as suítes de testes (EasyAccept) definidas em /tests.
 */
public class Main {
    /**
     * Ponto de entrada simples que executa os arquivos de especificação.
     *
     * @param args argumentos de linha de comando (não usados)
     */
    public static void main(String[] args) {
        EasyAccept.main(new String[] {"br.ufal.ic.jackut.Facade", "tests/us1_1.txt"});
        EasyAccept.main(new String[] {"br.ufal.ic.jackut.Facade", "tests/us1_2.txt"});
        EasyAccept.main(new String[] {"br.ufal.ic.jackut.Facade", "tests/us2_1.txt"});
        EasyAccept.main(new String[] {"br.ufal.ic.jackut.Facade", "tests/us2_2.txt"});
        EasyAccept.main(new String[] {"br.ufal.ic.jackut.Facade", "tests/us3_1.txt"});
        EasyAccept.main(new String[] {"br.ufal.ic.jackut.Facade", "tests/us3_2.txt"});
        EasyAccept.main(new String[] {"br.ufal.ic.jackut.Facade", "tests/us4_1.txt"});
        EasyAccept.main(new String[] {"br.ufal.ic.jackut.Facade", "tests/us4_2.txt"});
    }
}
