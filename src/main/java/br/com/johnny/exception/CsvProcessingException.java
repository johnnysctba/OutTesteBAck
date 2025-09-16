package br.com.johnny.exception;

/**
 * Exceção personalizada para erros durante o processamento de arquivos CSV.
 * Esta classe segue o padrão de exceções personalizadas e o princípio
 * de responsabilidade única (SRP), sendo específica para erros de CSV.
 */
public class
CsvProcessingException extends RuntimeException {

    /**
     * Construtor com mensagem de erro e causa.
     * 
     * @param message mensagem descritiva do erro
     * @param cause causa raiz da exceção
     */
    public CsvProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

}

