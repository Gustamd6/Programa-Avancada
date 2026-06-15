package br.com.unicesumar.diagnostico.util;

import br.com.unicesumar.diagnostico.model.EntidadeBase;

import java.util.Collection;

public final class IdGenerator {
    private IdGenerator() {}

    public static int proximoId(Collection<? extends EntidadeBase> entidades) {
        return entidades.stream()
                .mapToInt(EntidadeBase::getId)
                .max()
                .orElse(0) + 1;
    }
}
