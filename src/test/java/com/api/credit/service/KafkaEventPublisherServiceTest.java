package com.api.credit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaEventPublisherServiceTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafkaEventPublisherService kafkaEventPublisherService;

    @Test
    void publishConsultaEvent_DeveEnviarEventoParaTopico_ComDadosCorretos() {
        String tipoConsulta = "CONSULTA_POR_NFSE";
        String parametro = "NF001";
        String expectedTopic = "creditos-consultas";

        ArgumentCaptor<Map<String, Object>> eventoCaptor = ArgumentCaptor.forClass(Map.class);

        kafkaEventPublisherService.publishConsultaEvent(tipoConsulta, parametro);

        verify(kafkaTemplate).send(eq(expectedTopic), eventoCaptor.capture());
        
        Map<String, Object> eventoEnviado = eventoCaptor.getValue();
        assertNotNull(eventoEnviado);
        assertEquals(tipoConsulta, eventoEnviado.get("tipoConsulta"));
        assertEquals(parametro, eventoEnviado.get("parametro"));
        assertEquals("sistema", eventoEnviado.get("usuario"));
        assertNotNull(eventoEnviado.get("timestamp"));
        assertTrue(eventoEnviado.get("timestamp") instanceof String);
    }

    @Test
    void publishConsultaEvent_DeveEnviarEventoParaTopico_QuandoConsultaPorCredito() {
        String tipoConsulta = "CONSULTA_POR_CREDITO";
        String parametro = "CR001";
        String expectedTopic = "creditos-consultas";

        ArgumentCaptor<Map<String, Object>> eventoCaptor = ArgumentCaptor.forClass(Map.class);

        kafkaEventPublisherService.publishConsultaEvent(tipoConsulta, parametro);

        verify(kafkaTemplate).send(eq(expectedTopic), eventoCaptor.capture());
        
        Map<String, Object> eventoEnviado = eventoCaptor.getValue();
        assertNotNull(eventoEnviado);
        assertEquals(tipoConsulta, eventoEnviado.get("tipoConsulta"));
        assertEquals(parametro, eventoEnviado.get("parametro"));
        assertEquals("sistema", eventoEnviado.get("usuario"));
        assertNotNull(eventoEnviado.get("timestamp"));
    }

    @Test
    void publishConsultaEvent_DeveIncluirTimestampAtual() {
        String tipoConsulta = "CONSULTA_TESTE";
        String parametro = "PARAM_TESTE";
        
        ArgumentCaptor<Map<String, Object>> eventoCaptor = ArgumentCaptor.forClass(Map.class);

        kafkaEventPublisherService.publishConsultaEvent(tipoConsulta, parametro);

        verify(kafkaTemplate).send(eq("creditos-consultas"), eventoCaptor.capture());
        
        Map<String, Object> eventoEnviado = eventoCaptor.getValue();
        String timestamp = (String) eventoEnviado.get("timestamp");
        
        assertNotNull(timestamp);
        assertFalse(timestamp.isEmpty());
        // Verifica se o timestamp contém a data atual (formato ISO)
        assertTrue(timestamp.contains("2024") || timestamp.contains("2023") || timestamp.contains("2025"));
    }
}