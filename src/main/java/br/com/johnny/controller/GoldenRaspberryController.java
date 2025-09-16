package br.com.johnny.controller;

import br.com.johnny.dto.AwardIntervalResponse;
import br.com.johnny.service.GoldenRaspberryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GoldenRaspberryController {

    private static final Logger logger = LoggerFactory.getLogger(GoldenRaspberryController.class);

    private final GoldenRaspberryService goldenRaspberryService;

    @Autowired
    public GoldenRaspberryController(GoldenRaspberryService goldenRaspberryService) {
        this.goldenRaspberryService = goldenRaspberryService;
    }

    @GetMapping("/producers/award-intervals")
    public ResponseEntity<AwardIntervalResponse> getProducerAwardIntervals() {
        logger.info("Recebida requisição para obter intervalos de prêmios dos produtores");
        
        try {
            AwardIntervalResponse response = goldenRaspberryService.getProducerAwardIntervals();
            
            logger.info("Intervalos de prêmios calculados com sucesso");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Erro ao calcular intervalos de prêmios: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        logger.debug("Health check solicitado");
        return ResponseEntity.ok("Golden Raspberry Awards API está funcionando!");
    }
}

