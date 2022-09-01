package br.com.dicasdeumdev.api.resources.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
//Classe será utilizada pela classe que irá manipular as exceções.
public class StandardError {
    //Momento que ocorreu o erro
    private LocalDateTime timestamp;
    //Status Http
    private Integer status;
    //mensagem do erro
    private String error;
    private String path;
}
