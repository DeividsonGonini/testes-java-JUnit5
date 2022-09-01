package br.com.dicasdeumdev.api.resources;

import br.com.dicasdeumdev.api.domain.User;
import br.com.dicasdeumdev.api.domain.dto.UserDTO;
import br.com.dicasdeumdev.api.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserResourceTest {

    private static final Integer ID      = 1;
    private static final Integer INDEX   = 0;
    private static final String NAME     = "Valdir";
    private static final String EMAIL    = "valdir@mail.com";
    private static final String PASSWORD = "123";

    //Cobertura de classes que tem anotação @NoArgsConstructor
    //Mockar também os Objetos utilizados no Controller
    private User user = new User();
    private UserDTO userDTO = new UserDTO();

    @InjectMocks
    private UserResource resource;

    @Mock
    private UserServiceImpl service;

    @Mock
    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
        //Iniciar os Mocks da classe informada, nesse caso essa mesma classe
        MockitoAnnotations.openMocks(this);

        //Atribui valores aos atributos dos objetos
        startUser();
    }

    //FindByID
    @Test
    @DisplayName("Consulta por ID - com sucesso")
    void whenFindByIdThenReturnSuccess() {
        //Mockando o retorno do Service.FindById, por qualquer valor inteiro
        when(service.findById(anyInt())).thenReturn(user);
        //Mockando o mapper - caso utilize o mapper para converter uma Classe para DTO/Request
        when(mapper.map(any(), any())).thenReturn(userDTO);

        //Mockando o retorno do Resource.findById
        ResponseEntity<UserDTO> response = resource.findById(ID);

        /* ***** Verificações ***** */
        //Assegura que o Response não é nulo
        assertNotNull(response);
        //Assegura que o body(corpo) do Response não é nulo
        assertNotNull(response.getBody());
        //Assegura que a classe ResponseEntity é do tipo da classe do Response
        assertEquals(ResponseEntity.class, response.getClass());
        //Assegura que a classe UserDTO é do mesmo tipo da classe do Body no Response
        assertEquals(UserDTO.class, response.getBody().getClass());

        //Verifica que os valores esperados são iguais aos valores do response
        assertEquals(ID, response.getBody().getId());
        assertEquals(NAME, response.getBody().getName());
        assertEquals(EMAIL, response.getBody().getEmail());
        assertEquals(PASSWORD, response.getBody().getPassword());
    }

    //FindAll
    @Test
    @DisplayName("Lista todos os Usuarios - com sucesso")
    void whenFindAllThenReturnAListOfUserDTO() {
        //Mockando o retorno do Service.FindAll, que ira retornar uma lista de usuarios
        when(service.findAll()).thenReturn(List.of(user));

        //Mockando o mapper - caso utilize o mapper para converter uma Classe para DTO/Request
        when(mapper.map(any(), any())).thenReturn(userDTO);

        //Mockando o retorno do Resource.findAll
        ResponseEntity<List<UserDTO>> response = resource.findAll();

        /* ***** Verificações ***** */
        //Assegura que o Response não é nulo
        assertNotNull(response);
        //Assegura que o body(corpo) do Response não é nulo
        assertNotNull(response.getBody());
        //Assegura que o StatusHttp esperado é o mesmo do Response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //Assegura que a classe ResponseEntity(esperada) é do tipo da classe do Response
        assertEquals(ResponseEntity.class, response.getClass());
        //Assegura que o body do response é um ArrayList
        assertEquals(ArrayList.class, response.getBody().getClass());
        //Assegura que a classe UserDTO é do mesmo tipo da classe do Body no Response (no index 0 que esta preenchido)
        assertEquals(UserDTO.class, response.getBody().get(INDEX).getClass());

        //Verifica que os valores esperados são iguais aos valores do response (no index 0 que esta preenchido)
        assertEquals(ID, response.getBody().get(INDEX).getId());
        assertEquals(NAME, response.getBody().get(INDEX).getName());
        assertEquals(EMAIL, response.getBody().get(INDEX).getEmail());
        assertEquals(PASSWORD, response.getBody().get(INDEX).getPassword());
    }

    //Create / Save
    @Test
    @DisplayName("Cria Usuario - com sucesso")
    void whenCreateThenReturnCreated() {
        //Mockando o retorno do Service.Create, independente do parametro de entrada
        when(service.create(any())).thenReturn(user);

        //Mockando o retorno do Resource.Create
        ResponseEntity<UserDTO> response = resource.create(userDTO);

        //Verifica se a classe do response é do tipo ResponseEntity no Response
        assertEquals(ResponseEntity.class, response.getClass());
        //Verifica se o StatusHttp esperado é o mesmo do Response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        //Verifica se o Header Location esta preenchido no Response
        assertNotNull(response.getHeaders().get("Location"));
    }

    //Update
    @Test
    @DisplayName("Atualiza Usuario - com sucesso")
    void whenUpdateThenReturnSuccess() {
        //Mockando o retorno do Service.update, que é utilizada no mapper.map
        when(service.update(userDTO)).thenReturn(user);
        //Mockando o mapper - caso utilize o mapper para converter uma Classe para DTO/Request
        when(mapper.map(any(), any())).thenReturn(userDTO);

        //Mockando o retorno do Resource.Update
        ResponseEntity<UserDTO> response = resource.update(ID, userDTO);

        /* ***** Verificações ***** */
        //Assegura que o Response não é nulo
        assertNotNull(response);
        //Assegura que o body(corpo) do Response não é nulo
        assertNotNull(response.getBody());
        //Assegura que o StatusHttp esperado é o mesmo do Response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //Assegura que a classe ResponseEntity(esperada) é do tipo da classe do Response
        assertEquals(ResponseEntity.class, response.getClass());
        //Assegura que a classe UserDTO é do mesmo tipo da classe do Body no Response
        assertEquals(UserDTO.class, response.getBody().getClass());

        //Verifica que os valores esperados são iguais aos valores do response
        assertEquals(ID, response.getBody().getId());
        assertEquals(NAME, response.getBody().getName());
        assertEquals(EMAIL, response.getBody().getEmail());
    }

    //Delete
    @Test
    @DisplayName("Deleta Usuario - com sucesso")
    void whenDeleteThenReturnSuccess() {
        //Mockando o service.delete para não lançar exceção caso nao encontre o ID
        //Mockito.doNothing - não fara nada / Utilizado quando o metodo não tem retorno
        doNothing().when(service).delete(anyInt());

        //Mockando o retorno do Resource.Delete
        ResponseEntity<UserDTO> response = resource.delete(ID);

        /* ***** Verificações ***** */
        //Assegura que o Response não é nulo
        assertNotNull(response);
        //Assegura que a classe ResponseEntity(esperada) é do tipo da classe do Response
        assertEquals(ResponseEntity.class, response.getClass());
        //Assegura que o StatusHttp esperado é o mesmo do Response
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        //Verifica no repository quantas vezes o DeleteById foi chamado, se for mais de 1 o metodo esta errado
        //Mockito.verify - verificação do Mockito
        verify(service, times(1)).delete(anyInt());
    }

    //Startando os usuarios para terem valor.
    private void startUser() {
        user = new User(ID, NAME, EMAIL,  PASSWORD);
        userDTO = new UserDTO(ID, NAME, EMAIL, PASSWORD);
    }
}