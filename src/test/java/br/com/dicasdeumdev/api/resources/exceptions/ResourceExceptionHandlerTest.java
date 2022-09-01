package br.com.dicasdeumdev.api.resources.exceptions;

import br.com.dicasdeumdev.api.services.exceptions.DataIntegratyViolationException;
import br.com.dicasdeumdev.api.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ResourceExceptionHandlerTest {

    //Mockando as Constantes que serao utilizadas
    private static final String OBJETO_NAO_ENCONTRADO = "Objeto não encontrado";
    private static final String E_MAIL_JA_CADASTRADO = "E-mail já cadastrado";

    //@InjectMocks - Cria uma instância real do Objeto
    @InjectMocks
    private ResourceExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        //Iniciar os Mocks da classe informada, nesse caso essa mesma classe
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("objectNotFound")
    void whenObjectNotFoundExceptionThenReturnAResponseEntity() {
        ResponseEntity<StandardError> response = exceptionHandler
                .objectNotFound(
                        //Mockando o ObjectNotFoundException para passar como parametro
                        new ObjectNotFoundException(OBJETO_NAO_ENCONTRADO),
                        //Mockando o HttpServletRequest para passar como parametro
                        new MockHttpServletRequest());

        /* ***** Verificações ***** */
        //Assegura que o Response não é nulo
        assertNotNull(response);
        //Assegura que o body(corpo) do Response não é nulo
        assertNotNull(response.getBody());
        //Assegura que o StatusHttp esperado é o mesmo do Response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        //Assegura que a classe ResponseEntity(esperada) é do tipo da classe do Response
        assertEquals(ResponseEntity.class, response.getClass());
        //Assegura que a classe StandardError é do mesmo tipo da classe do Body no Response
        assertEquals(StandardError.class, response.getBody().getClass());
        //Verifica que os valores esperados são iguais aos valores do response
        assertEquals(OBJETO_NAO_ENCONTRADO, response.getBody().getError());
        //Assegura que o Status esperado é o mesmo body do Response
        assertEquals(404, response.getBody().getStatus());
        //Assegura que o Path "/user/2" esperado não é igual ao path do Response
        assertNotEquals("/user/2", response.getBody().getPath());
        //Assegura que a dataHora atual não é igual ao Timestamp do Response
        assertNotEquals(LocalDateTime.now(), response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("dataIntegrityViolation")
    void dataIntegrityViolationException() {
        ResponseEntity<StandardError> response = exceptionHandler
                .dataIntegrityViolationException(
                        //Mockando o DataIntegratyViolationException para passar como parametro
                        new DataIntegratyViolationException(E_MAIL_JA_CADASTRADO),
                        //Mockando o HttpServletRequest para passar como parametro
                        new MockHttpServletRequest());

        /* ***** Verificações ***** */
        //Assegura que o Response não é nulo
        assertNotNull(response);
        //Assegura que o body(corpo) do Response não é nulo
        assertNotNull(response.getBody());
        //Assegura que o StatusHttp esperado é o mesmo do Response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //Assegura que a classe ResponseEntity(esperada) é do tipo da classe do Response
        assertEquals(ResponseEntity.class, response.getClass());
        //Assegura que a classe StandardError é do mesmo tipo da classe do Body no Response
        assertEquals(StandardError.class, response.getBody().getClass());
        //Assegura que os valores esperados são iguais aos valores do response
        assertEquals(E_MAIL_JA_CADASTRADO, response.getBody().getError());
        //Assegura que o Status esperado é o mesmo body do Response
        assertEquals(400, response.getBody().getStatus());
    }
}