package br.com.johnny.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * DTO (Data Transfer Object) que representa o intervalo de prêmios de um produtor.
 * Esta classe segue o padrão DTO para transferência de dados entre camadas,
 * aplicando o princípio de responsabilidade única (SRP).
 */
public class ProducerAwardInterval {

    @JsonProperty("producer")
    private String producer;

    @JsonProperty("interval")
    private Integer interval;

    @JsonProperty("previousWin")
    private Integer previousWin;

    @JsonProperty("followingWin")
    private Integer followingWin;

    /**
     * Construtor padrão.
     */
    public ProducerAwardInterval() {
    }

    /**
     * Construtor com todos os parâmetros.
     * 
     * @param producer nome do produtor
     * @param interval intervalo entre os prêmios
     * @param previousWin ano do prêmio anterior
     * @param followingWin ano do prêmio seguinte
     */
    public ProducerAwardInterval(String producer, Integer interval, Integer previousWin, Integer followingWin) {
        this.producer = producer;
        this.interval = interval;
        this.previousWin = previousWin;
        this.followingWin = followingWin;
    }


    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getPreviousWin() {
        return previousWin;
    }

    public void setPreviousWin(Integer previousWin) {
        this.previousWin = previousWin;
    }

    public Integer getFollowingWin() {
        return followingWin;
    }

    public void setFollowingWin(Integer followingWin) {
        this.followingWin = followingWin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProducerAwardInterval that = (ProducerAwardInterval) o;
        return Objects.equals(producer, that.producer) &&
               Objects.equals(interval, that.interval) &&
               Objects.equals(previousWin, that.previousWin) &&
               Objects.equals(followingWin, that.followingWin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producer, interval, previousWin, followingWin);
    }

    @Override
    public String toString() {
        return "ProducerAwardInterval{" +
                "producer='" + producer + '\'' +
                ", interval=" + interval +
                ", previousWin=" + previousWin +
                ", followingWin=" + followingWin +
                '}';
    }
}

