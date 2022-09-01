package br.com.dicasdeumdev.api.services.impl;

import br.com.dicasdeumdev.api.domain.User;
import br.com.dicasdeumdev.api.domain.dto.UserDTO;
import br.com.dicasdeumdev.api.repositories.UserRepository;
import br.com.dicasdeumdev.api.services.exceptions.DataIntegratyViolationException;
import br.com.dicasdeumdev.api.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    private static final Integer ID      = 1;
    private static final Integer INDEX   = 0;
    private static final String NAME     = "Valdir";
    private static final String EMAIL    = "valdir@mail.com";
    private static final String PASSWORD = "123";

    private static final String OBJETO_NAO_ENCONTRADO = "Objeto não encontrado";
    private static final String E_MAIL_JA_CADASTRADO_NO_SISTEMA = "E-mail já cadastrado no sistema";

    //@InjectMocks - Cria uma instância real do Objeto
    @InjectMocks
    private UserServiceImpl service;

    //@Mock- Cria uma instância fictícia do Objeto
    @Mock
    private UserRepository repository;

    @Mock
    private ModelMapper mapper;

    private User user;
    private UserDTO userDTO;
    private Optional<User> optionalUser;


    @BeforeEach
    void setUp() {
        //Iniciar os Mocks da classe informada, nesse caso essa mesma classe
        MockitoAnnotations.openMocks(this);
        startUser();
    }

    //findByID
    @Test
    void whenFindByIdThenReturnAnUserInstance() {

        //Mockando a resposta do obj
        //Quando o Objeto for chamado, passando qualquer valor Inteiro, retorne um OptionalUser(retorno do metodo)
        Mockito.when(repository.findById(anyInt())).thenReturn(optionalUser);

        //com o obj Mockado chame o findById do service
        User response = service.findById(ID);

        //Verifica se o response é nulo
        Assertions.assertNotNull(response);

        //Verifica se Classe esperada é do mesmo tipo da classe retornada no response
        Assertions.assertEquals(User.class, response.getClass());
        //Verifica se o ID esperado é igual ao retornado no response
        Assertions.assertEquals(ID, response.getId());
        //Verifica se o Nome esperado é igual ao retornado no response
        Assertions.assertEquals(NAME, response.getName());
        //Verifica se o Email esperado é igual ao retornado no response
        Assertions.assertEquals(EMAIL, response.getEmail());
    }

    //Objeto não encontrado - Exception
    @Test
    void whenFindByIdThenReturnAnObjectNotFoundException() {
        //Mockando a resposta do obj findById
        //Quando chamar o FindById, lance uma exceção de Objeto Não encontrado
        Mockito.when(repository.findById(anyInt()))
                .thenThrow(new ObjectNotFoundException(OBJETO_NAO_ENCONTRADO));
        //Testando o lançamento da Exceção
        try{
            service.findById(ID);
        } catch (Exception ex) {
            //Verifique que a exceção lançada é do mesmo tipo da classe ObjectNotFound
            assertEquals(ObjectNotFoundException.class, ex.getClass());

            //Verifique que a mensagem da exceção lançada é igual mensagem da exceção Lançada
            assertEquals(OBJETO_NAO_ENCONTRADO, ex.getMessage());
        }
    }

    //ListAll
    @Test
    void whenFindAllThenReturnAnListOfUsers() {
        //Mockando a resposta do obj
        //Quando o Repository.FindAll for chamado, retorne uma lista de User (retorno do metodo)
        when(repository.findAll()).thenReturn(List.of(user));

        //com o obj Mockado chame o findAll do service
        List<User> response = service.findAll();

        //Verifica se o response é nulo
        assertNotNull(response);
        assertEquals(1, response.size());
        //Assegura que o objeto da classe User é do mesmo tipo do Objeto retornado no index 0
        assertEquals(User.class, response.get(INDEX).getClass());

        //Assegura que o atributo esperado é igual ao atributo do response no index 0
        assertEquals(ID, response.get(INDEX).getId());
        assertEquals(NAME, response.get(INDEX).getName());
        assertEquals(EMAIL, response.get(INDEX).getEmail());
        assertEquals(PASSWORD, response.get(INDEX).getPassword());
    }

    //Create/Save - Sucesso
    @Test
    void whenCreateThenReturnSuccess() {
        //Mockando a resposta do Repository.save
        when(repository.save(any())).thenReturn(user);

        //Mockando a resposta do Service.create
        User response = service.create(userDTO);

        /* ***** Verificações ***** */
        //Verifica se o response é nulo
        assertNotNull(response);
        //Verifica se Classe esperada é do mesmo tipo da classe retornada no response
        assertEquals(User.class, response.getClass());
        //Verifica se os atributos esperados são iguais aos atributos salvos
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PASSWORD, response.getPassword());
    }

    //Create/Save - Com Exceção
    @Test
    void whenCreateThenReturnAnDataIntegrityViolationException() {
        //Mockando retorno do findById do repository que verifica se ja tem algum e-mail salvo
        when(repository.findByEmail(anyString())).thenReturn(optionalUser);

        try{
            //Força a exceção pois o email ja foi cadastrado no ID =1
            optionalUser.get().setId(2);
            service.create(userDTO);
        } catch (Exception ex) {

            //Verifique que a exceção lançada é do mesmo tipo da classe DataIntegratyViolationException
            assertEquals(DataIntegratyViolationException.class, ex.getClass());

            //Verifique que a mensagem da exceção lançada é igual mensagem da exceção Lançada
            assertEquals(E_MAIL_JA_CADASTRADO_NO_SISTEMA, ex.getMessage());
        }
    }

    //Update - Com Sucesso
    @Test
    void whenUpdateThenReturnSuccess() {
        //Mockando a resposta do Repository.save
        when(repository.save(any())).thenReturn(user);

        User response = service.update(userDTO);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PASSWORD, response.getPassword());
    }

    //Update - Com Exceção
    @Test
    void whenUpdateThenReturnAnDataIntegrityViolationException() {
        //Mockando retorno do findByEmail do repository para localizar por e-mail o registro que será alterado
        when(repository.findByEmail(anyString())).thenReturn(optionalUser);

        try{
            optionalUser.get().setId(2);
            service.create(userDTO);
        } catch (Exception ex) {
            //Verifique que a exceção lançada é do mesmo tipo da classe DataIntegratyViolationException
            assertEquals(DataIntegratyViolationException.class, ex.getClass());
            assertEquals(E_MAIL_JA_CADASTRADO_NO_SISTEMA, ex.getMessage());
        }
    }

    //Delete - Com Sucesso
    @Test
    void deleteWithSuccess() {
        //Mockando retorno do findById do repository para localizar por e-mail o registro que será excluido
        when(repository.findById(anyInt())).thenReturn(optionalUser);

        //Mockando o repository.deleteById para não lançar exceção caso nao encontre o ID
        //Mockito.doNothing - não fara nada / Utilizado quando o metodo não tem retorno
        doNothing().when(repository).deleteById(anyInt());

        service.delete(ID);

        //Verifica no repository quantas vezes o DeleteById foi chamado, s for mais de 1 o metodo esta errado
        //Mockito.verify - verificação do Mockito
        verify(repository, times(1)).deleteById(anyInt());
    }

    //Delete - Com Exceção
    @Test
    void whenDeleteThenReturnObjectNotFoundException() {
        //Mockando retorno do findById do repository para localizar por e-mail o registro que será excluido
        //Força o laçamento da exceção
        when(repository.findById(anyInt()))
                .thenThrow(new ObjectNotFoundException(OBJETO_NAO_ENCONTRADO));
        try {
            service.delete(ID);
        } catch (Exception ex) {
            //Verifique que a exceção lançada é do mesmo tipo da classe ObjectNotFoundException
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals(OBJETO_NAO_ENCONTRADO, ex.getMessage());
        }
    }

    //Atribuindo valores para os Objetos para não serem nulos
    private void startUser() {
        user = new User(ID, NAME, EMAIL,  PASSWORD);
        userDTO = new UserDTO(ID, NAME, EMAIL, PASSWORD);
        optionalUser = Optional.of(new User(ID, NAME, EMAIL, PASSWORD));
    }
}