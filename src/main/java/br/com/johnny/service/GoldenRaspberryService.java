package br.com.johnny.service;

import br.com.johnny.dto.AwardIntervalResponse;
import br.com.johnny.dto.ProducerAwardInterval;
import br.com.johnny.exception.CsvProcessingException;
import br.com.johnny.model.Movie;
import br.com.johnny.repository.MovieRepository;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
/**
 * Serviço responsável pela lógica de negócio relacionada aos dados
 * do Golden Raspberry Awards.
 * Esta classe implementa o padrão Service Layer, encapsulando a lógica
 * de negócio e seguindo os princípios SOLID, especialmente SRP e DIP.
 */
@Service
@Transactional
public class GoldenRaspberryService {

    private static final Logger logger = LoggerFactory.getLogger(GoldenRaspberryService.class);
    private static final String CSV_FILE_PATH = "movielist.csv";

    private final MovieRepository movieRepository;

    /**
     * Construtor com injeção de dependência.
     * 
     * @param movieRepository repositório de filmes
     */
    @Autowired
    public GoldenRaspberryService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Método executado após a construção do bean para carregar os dados do CSV.
     * Aplica o padrão de inicialização automática.
     */
    @PostConstruct
    public void initializeData() {
        if (!movieRepository.existsAny()) {
            logger.info("Carregando dados do arquivo CSV...");
            loadMoviesFromCsv();
            logger.info("Dados carregados com sucesso!");
        } else {
            logger.info("Dados já existem no banco. Pulando carregamento do CSV.");
        }
    }

    /**
     * Carrega os dados dos filmes a partir do arquivo CSV.
     * 
     * @throws CsvProcessingException se houver erro no processamento do CSV
     */
    private void loadMoviesFromCsv() {
        try {
            ClassPathResource resource = new ClassPathResource(CSV_FILE_PATH);

            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .withIgnoreQuotations(true)
                    .build();

            try (var isr = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
                 var csvReader = new CSVReaderBuilder(isr)
                         .withCSVParser(parser)
                         .build()) {

                List<String[]> records = csvReader.readAll();

                if (!records.isEmpty()) {
                    records.remove(0);
                }

                List<Movie> movies = records.stream()
                        .map(this::parseMovieFromCsvRecord)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                movieRepository.saveAll(movies);
                logger.info("Carregados {} filmes do arquivo CSV", movies.size());
            }
        } catch (IOException | CsvException e) {
            logger.error("Erro ao processar arquivo CSV: {}", e.getMessage(), e);
            throw new CsvProcessingException("Erro ao carregar dados do arquivo CSV", e);
        }
    }

    /**
     * Converte um registro do CSV em uma entidade Movie.
     * 
     * @param record array com os dados do registro CSV
     * @return objeto Movie ou null se houver erro na conversão
     */
    private Movie parseMovieFromCsvRecord(String[] record) {
        try {
            if (record.length < 5) {
                logger.warn("Registro CSV inválido: número insuficiente de campos");
                return null;
            }

            Integer year = Integer.parseInt(record[0].trim());
            String title = record[1].trim();
            String studios = record[2].trim();
            String producers = record[3].trim();
            Boolean winner = "yes".equalsIgnoreCase(record[4].trim());

            return new Movie(year, title, studios, producers, winner);
            
        } catch (NumberFormatException e) {
            logger.warn("Erro ao converter ano do filme: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            logger.warn("Erro ao processar registro CSV: {}", e.getMessage());
            return null;
        }
    }
    /**
     * Obtém os intervalos de prêmios dos produtores (mínimo e máximo).
     * Este método implementa a lógica principal de negócio da aplicação,
     * calculando os intervalos entre prêmios consecutivos dos produtores.
     * @return resposta com os intervalos mínimos e máximos
     */
    public AwardIntervalResponse getProducerAwardIntervals() {
        List<Movie> winnerMovies = movieRepository.findWinnerMoviesOrderedByYear();
        
        Map<String, List<Integer>> producerWins = extractProducerWins(winnerMovies);
        List<ProducerAwardInterval> intervals = calculateIntervals(producerWins);
        
        return buildResponse(intervals);
    }

    /**
     * Extrai os anos de vitória de cada produtor.
     * 
     * @param winnerMovies lista de filmes vencedores
     * @return mapa com produtor e seus anos de vitória
     */
    private Map<String, List<Integer>> extractProducerWins(List<Movie> winnerMovies) {
        Map<String, List<Integer>> producerWins = new HashMap<>();
        
        for (Movie movie : winnerMovies) {
            String[] producers = parseProducers(movie.getProducers());
            
            for (String producer : producers) {
                producerWins.computeIfAbsent(producer, k -> new ArrayList<>())
                           .add(movie.getYear());
            }
        }
        
        producerWins.forEach((producer, years) -> {
            Set<Integer> uniqueYears = new TreeSet<>(years);
            producerWins.put(producer, new ArrayList<>(uniqueYears));
        });
        
        return producerWins;
    }

    /**
     * Analisa a string de produtores e separa os nomes individuais.
     * 
     * @param producersString string com nomes dos produtores
     * @return array com nomes individuais dos produtores
     */
    private String[] parseProducers(String producersString) {
        if (producersString == null || producersString.trim().isEmpty()) {
            return new String[0];
        }
        
        String[] producers = producersString.split(",|\\band\\b");
        
        return Arrays.stream(producers)
                .map(String::trim)
                .filter(producer -> !producer.isEmpty())
                .toArray(String[]::new);
    }

    /**
     * Calcula os intervalos entre prêmios consecutivos para cada produtor.
     * 
     * @param producerWins mapa com produtor e seus anos de vitória
     * @return lista de intervalos calculados
     */
    private List<ProducerAwardInterval> calculateIntervals(Map<String, List<Integer>> producerWins) {
        List<ProducerAwardInterval> intervals = new ArrayList<>();
        
        for (Map.Entry<String, List<Integer>> entry : producerWins.entrySet()) {
            String producer = entry.getKey();
            List<Integer> years = entry.getValue();
            
            if (years.size() >= 2) {
                for (int i = 1; i < years.size(); i++) {
                    int previousWin = years.get(i - 1);
                    int followingWin = years.get(i);
                    int interval = followingWin - previousWin;
                    
                    intervals.add(new ProducerAwardInterval(producer, interval, previousWin, followingWin));
                }
            }
        }
        
        return intervals;
    }

    /**
     * Constrói a resposta com os intervalos mínimos e máximos.
     * 
     * @param intervals lista de todos os intervalos calculados
     * @return resposta formatada conforme especificação da API
     */
    private AwardIntervalResponse buildResponse(List<ProducerAwardInterval> intervals) {
        if (intervals.isEmpty()) {
            return new AwardIntervalResponse(new ArrayList<>(), new ArrayList<>());
        }
        
        int minInterval = intervals.stream()
                .mapToInt(ProducerAwardInterval::getInterval)
                .min()
                .orElse(0);
        
        int maxInterval = intervals.stream()
                .mapToInt(ProducerAwardInterval::getInterval)
                .max()
                .orElse(0);
        
        List<ProducerAwardInterval> minIntervals = intervals.stream()
                .filter(interval -> interval.getInterval().equals(minInterval))
                .collect(Collectors.toList());
        
        List<ProducerAwardInterval> maxIntervals = intervals.stream()
                .filter(interval -> interval.getInterval().equals(maxInterval))
                .collect(Collectors.toList());
        
        return new AwardIntervalResponse(minIntervals, maxIntervals);
    }
}

