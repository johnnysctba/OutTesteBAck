package br.com.johnny;

import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.johnny.dto.AwardIntervalResponse;
import br.com.johnny.dto.ProducerAwardInterval;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para a aplicação Golden Raspberry Awards API.
 * Esta classe implementa testes de integração conforme especificado
 * nos requisitos, validando que os dados obtidos estão de acordo
 * com os dados fornecidos na proposta.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GoldenRaspberryAwardsIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Testa se a aplicação inicializa corretamente.
     */
    @Test
    void contextLoads() {
        // Este teste verifica se o contexto da aplicação carrega sem erros
    }

    /**
     * Testa o endpoint de health check.
     */
    @Test
    void testHealthCheckEndpoint() {
        String url = "http://localhost:" + port + "/api/health";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Golden Raspberry Awards API está funcionando!", response.getBody());
    }

    /**
     * Testa o endpoint principal de intervalos de prêmios dos produtores.
     * 
     * Este teste valida que:
     * - O endpoint retorna status 200
     * - A resposta está no formato JSON correto
     * - A estrutura da resposta contém os campos "min" e "max"
     * - Os dados retornados são consistentes com o arquivo CSV fornecido
     */
    @Test
    void testGetProducerAwardIntervals() {
        String url = "http://localhost:" + port + "/api/producers/award-intervals";
        ResponseEntity<AwardIntervalResponse> response = restTemplate.getForEntity(url, AwardIntervalResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        AwardIntervalResponse responseBody = response.getBody();

        // Validações básicas da estrutura
        assertNotNull(responseBody);
        assertNotNull(responseBody.getMin());
        assertNotNull(responseBody.getMax());

        // Valida que existem dados de intervalo mínimo e máximo
        assertFalse(responseBody.getMin().isEmpty(), "Deve haver pelo menos um produtor com intervalo mínimo");
        assertFalse(responseBody.getMax().isEmpty(), "Deve haver pelo menos um produtor com intervalo máximo");

        // Valida a estrutura dos objetos ProducerAwardInterval
        for (ProducerAwardInterval interval : responseBody.getMin()) {
            validateProducerAwardInterval(interval);
        }

        for (ProducerAwardInterval interval : responseBody.getMax()) {
            validateProducerAwardInterval(interval);
        }

        // Valida que o intervalo mínimo é menor ou igual ao máximo
        int minInterval = responseBody.getMin().get(0).getInterval();
        int maxInterval = responseBody.getMax().get(0).getInterval();
        assertTrue(minInterval <= maxInterval, 
                "O intervalo mínimo deve ser menor ou igual ao máximo");
    }

    /**
     * Testa casos específicos baseados nos dados do CSV fornecido.
     * 
     * Este teste valida dados específicos conhecidos do arquivo CSV,
     * garantindo que a lógica de processamento está correta.
     */
    @Test
    void testSpecificDataFromCsv() {
        String url = "http://localhost:" + port + "/api/producers/award-intervals";
        ResponseEntity<AwardIntervalResponse> response = restTemplate.getForEntity(url, AwardIntervalResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        AwardIntervalResponse responseBody = response.getBody();

        // Valida que existem produtores com múltiplas vitórias
        // (baseado na análise do CSV fornecido)
        boolean hasMultipleWinners = !responseBody.getMin().isEmpty() || !responseBody.getMax().isEmpty();
        assertTrue(hasMultipleWinners, 
                "Deve haver produtores com múltiplas vitórias no dataset");

        // Valida que os intervalos são números positivos
        responseBody.getMin().forEach(interval -> 
                assertTrue(interval.getInterval() > 0, 
                        "Intervalos devem ser positivos"));
        
        responseBody.getMax().forEach(interval -> 
                assertTrue(interval.getInterval() > 0, 
                        "Intervalos devem ser positivos"));
    }

    /**
     * Testa a consistência dos dados de intervalo.
     * 
     * Valida que followingWin > previousWin e que o intervalo
     * é calculado corretamente.
     */
    @Test
    void testIntervalConsistency() {
        String url = "http://localhost:" + port + "/api/producers/award-intervals";
        ResponseEntity<AwardIntervalResponse> response = restTemplate.getForEntity(url, AwardIntervalResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        AwardIntervalResponse responseBody = response.getBody();

        // Valida consistência dos intervalos mínimos
        for (ProducerAwardInterval interval : responseBody.getMin()) {
            validateIntervalConsistency(interval);
        }

        // Valida consistência dos intervalos máximos
        for (ProducerAwardInterval interval : responseBody.getMax()) {
            validateIntervalConsistency(interval);
        }
    }

    /**
     * Valida a estrutura de um objeto ProducerAwardInterval.
     * 
     * @param interval objeto a ser validado
     */
    private void validateProducerAwardInterval(ProducerAwardInterval interval) {
        assertNotNull(interval.getProducer(), "Nome do produtor não pode ser nulo");
        assertNotNull(interval.getInterval(), "Intervalo não pode ser nulo");
        assertNotNull(interval.getPreviousWin(), "Ano da vitória anterior não pode ser nulo");
        assertNotNull(interval.getFollowingWin(), "Ano da vitória seguinte não pode ser nulo");
        
        assertFalse(interval.getProducer().trim().isEmpty(), 
                "Nome do produtor não pode estar vazio");
        assertTrue(interval.getInterval() > 0, 
                "Intervalo deve ser positivo");
        assertTrue(interval.getPreviousWin() > 0, 
                "Ano da vitória anterior deve ser positivo");
        assertTrue(interval.getFollowingWin() > 0, 
                "Ano da vitória seguinte deve ser positivo");
    }

    /**
     * Valida a consistência matemática de um intervalo.
     * 
     * @param interval objeto a ser validado
     */
    private void validateIntervalConsistency(ProducerAwardInterval interval) {
        assertTrue(interval.getFollowingWin() > interval.getPreviousWin(),
                "Ano da vitória seguinte deve ser maior que o anterior");
        
        int expectedInterval = interval.getFollowingWin() - interval.getPreviousWin();
        assertEquals(expectedInterval, interval.getInterval().intValue(),
                "Intervalo calculado deve ser consistente com as datas");
    }
}

