package br.com.senac.aula06.visao;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.senac.aula06.config.VisaoProperties;

/** Escolhe o provedor pelo nome; se vier vazio usa visao.provedor-padrao. */
@Component
public class VisaoProviderFactory {

    private final Map<String, VisaoProvider> porNome;
    private final String padrao;

    public VisaoProviderFactory(List<VisaoProvider> provedores, VisaoProperties props) {
        this.porNome = provedores.stream().collect(Collectors.toMap(VisaoProvider::nome, Function.identity()));
        this.padrao = props.provedorPadrao();
    }

    public VisaoProvider obter(String nome) {
        String chave = (nome == null || nome.isBlank()) ? padrao : nome.toLowerCase().trim();
        VisaoProvider p = porNome.get(chave);
        if (p == null) {
            throw new VisaoException(400, "Provedor desconhecido: '" + nome + "'. Use um destes: " + porNome.keySet());
        }
        return p;
    }

    public List<String> nomes() {
        return porNome.keySet().stream().sorted().toList();
    }
}
