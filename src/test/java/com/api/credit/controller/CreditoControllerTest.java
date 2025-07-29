package com.api.credit.controller;

import com.api.credit.dto.CreditoDTO;
import com.api.credit.exception.NotFoundException;
import com.api.credit.service.CreditoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreditoController.class)
class CreditoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreditoService creditoService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreditoDTO creditoDTO;

    @BeforeEach
    void setUp() {
        creditoDTO = new CreditoDTO();
        creditoDTO.setNumeroCredito("CR001");
        creditoDTO.setNumeroNfse("NF001");
        creditoDTO.setDataConstituicao(LocalDate.of(2024, 1, 15));
        creditoDTO.setValorIssqn(new BigDecimal("100.00"));
        creditoDTO.setTipoCredito("ISSQN");
        creditoDTO.setSimplesNacional("Sim");
        creditoDTO.setAliquota(new BigDecimal("5.00"));
        creditoDTO.setValorFaturado(new BigDecimal("2000.00"));
        creditoDTO.setValorDeducao(new BigDecimal("0.00"));
        creditoDTO.setBaseCalculo(new BigDecimal("2000.00"));
    }

    @Test
    void buscarPorNfse_DeveRetornarListaCreditos_QuandoEncontrarCreditos() throws Exception {
        String numeroNfse = "NF001";
        List<CreditoDTO> creditos = Arrays.asList(creditoDTO);
        when(creditoService.buscarNumeroNfse(numeroNfse)).thenReturn(creditos);

        mockMvc.perform(get("/api/creditos/{numeroNfse}", numeroNfse)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].numeroCredito").value("CR001"))
                .andExpect(jsonPath("$[0].numeroNfse").value("NF001"))
                .andExpect(jsonPath("$[0].valorIssqn").value(100.00))
                .andExpect(jsonPath("$[0].tipoCredito").value("ISSQN"))
                .andExpect(jsonPath("$[0].simplesNacional").value("Sim"));
    }

    @Test
    void buscarPorNfse_DeveRetornarNotFound_QuandoNaoEncontrarCreditos() throws Exception {
        String numeroNfse = "NF999";
        when(creditoService.buscarNumeroNfse(numeroNfse))
                .thenThrow(new NotFoundException("Nenhum crédito encontrado para a NFS-e: " + numeroNfse));

        mockMvc.perform(get("/api/creditos/{numeroNfse}", numeroNfse)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Não encontrado"))
                .andExpect(jsonPath("$.message").value("Nenhum crédito encontrado para a NFS-e: " + numeroNfse));
    }

    @Test
    void buscarPorCredito_DeveRetornarCredito_QuandoEncontrarCredito() throws Exception {
        String numeroCredito = "CR001";
        when(creditoService.buscarPorCredito(numeroCredito)).thenReturn(creditoDTO);

        mockMvc.perform(get("/api/creditos/credito/{numeroCredito}", numeroCredito)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numeroCredito").value("CR001"))
                .andExpect(jsonPath("$.numeroNfse").value("NF001"))
                .andExpect(jsonPath("$.valorIssqn").value(100.00))
                .andExpect(jsonPath("$.tipoCredito").value("ISSQN"))
                .andExpect(jsonPath("$.simplesNacional").value("Sim"))
                .andExpect(jsonPath("$.dataConstituicao").value("2024-01-15"));
    }

    @Test
    void buscarPorCredito_DeveRetornarNotFound_QuandoNaoEncontrarCredito() throws Exception {
        String numeroCredito = "CR999";
        when(creditoService.buscarPorCredito(numeroCredito))
                .thenThrow(new NotFoundException("Crédito não encontrado: " + numeroCredito));

        mockMvc.perform(get("/api/creditos/credito/{numeroCredito}", numeroCredito)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Não encontrado"))
                .andExpect(jsonPath("$.message").value("Crédito não encontrado: " + numeroCredito));
    }

    @Test
    void buscarPorNfse_DevePermitirCORS() throws Exception {
        String numeroNfse = "NF001";
        List<CreditoDTO> creditos = Arrays.asList(creditoDTO);
        when(creditoService.buscarNumeroNfse(numeroNfse)).thenReturn(creditos);

        mockMvc.perform(get("/api/creditos/{numeroNfse}", numeroNfse)
                .header("Origin", "http://localhost:3000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
    }
}