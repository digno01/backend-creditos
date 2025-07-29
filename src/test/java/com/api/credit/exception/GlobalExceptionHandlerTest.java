package com.api.credit.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleCreditoNotFoundException_DeveRetornarResponseEntity_ComStatusNotFound() {
        String mensagemErro = "Crédito não encontrado: CR999";
        NotFoundException exception = new NotFoundException(mensagemErro);

        ResponseEntity<Map<String, Object>> response =
            globalExceptionHandler.handleCreditoNotFoundException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.get("status"));
        assertEquals("Não encontrado", body.get("error"));
        assertEquals(mensagemErro, body.get("message"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleCreditoNotFoundException_DeveIncluirTimestampAtual() {
        NotFoundException exception = new NotFoundException("Teste de erro");
        LocalDateTime antes = LocalDateTime.now().minusSeconds(1);

        ResponseEntity<Map<String, Object>> response =
            globalExceptionHandler.handleCreditoNotFoundException(exception);

        LocalDateTime depois = LocalDateTime.now().plusSeconds(1);
        LocalDateTime timestampResponse = (LocalDateTime) response.getBody().get("timestamp");
        
        assertTrue(timestampResponse.isAfter(antes));
        assertTrue(timestampResponse.isBefore(depois));
    }

    @Test
    void handleCreditoNotFoundException_DeveManterMensagemOriginal() {
        String mensagemPersonalizada = "Nenhum crédito encontrado para a NFS-e: NF999";
        NotFoundException exception = new NotFoundException(mensagemPersonalizada);

        ResponseEntity<Map<String, Object>> response =
            globalExceptionHandler.handleCreditoNotFoundException(exception);

        Map<String, Object> body = response.getBody();
        assertEquals(mensagemPersonalizada, body.get("message"));
    }

    @Test
    void handleCreditoNotFoundException_DeveConterTodosOsCamposObrigatorios() {
        NotFoundException exception = new NotFoundException("Erro de teste");

        ResponseEntity<Map<String, Object>> response =
            globalExceptionHandler.handleCreditoNotFoundException(exception);

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        
        // Verifica se todos os campos obrigatórios estão presentes
        assertTrue(body.containsKey("timestamp"));
        assertTrue(body.containsKey("status"));
        assertTrue(body.containsKey("error"));
        assertTrue(body.containsKey("message"));
        
        // Verifica os tipos dos campos
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
        assertTrue(body.get("status") instanceof Integer);
        assertTrue(body.get("error") instanceof String);
        assertTrue(body.get("message") instanceof String);
    }
}