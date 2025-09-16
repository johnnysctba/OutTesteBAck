package br.com.johnny.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

/**
 * DTO (Data Transfer Object) que representa a resposta da API com os intervalos
 * mínimos e máximos entre prêmios dos produtores.
 * Esta classe encapsula a resposta da API conforme especificado no documento
 * de requisitos, seguindo o padrão DTO e o princípio de responsabilidade única.
 */
public class AwardIntervalResponse {

    @JsonProperty("min")
    private List<ProducerAwardInterval> min;

    @JsonProperty("max")
    private List<ProducerAwardInterval> max;


    /**
     * Construtor com parâmetros.
     * 
     * @param min lista de produtores com menor intervalo entre prêmios
     * @param max lista de produtores com maior intervalo entre prêmios
     */
    public AwardIntervalResponse(List<ProducerAwardInterval> min, List<ProducerAwardInterval> max) {
        this.min = min;
        this.max = max;
    }

    public List<ProducerAwardInterval> getMin() {
        return min;
    }

    public List<ProducerAwardInterval> getMax() {
        return max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AwardIntervalResponse that = (AwardIntervalResponse) o;
        return Objects.equals(min, that.min) && Objects.equals(max, that.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }

    @Override
    public String toString() {
        return "AwardIntervalResponse{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}

