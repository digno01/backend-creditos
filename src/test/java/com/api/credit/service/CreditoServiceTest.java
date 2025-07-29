package com.api.credit.service;

import com.api.credit.dto.CreditoDTO;
import com.api.credit.entity.Credito;
import com.api.credit.exception.NotFoundException;
import com.api.credit.mapper.CreditoMapper;
import com.api.credit.repository.CreditoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditoServiceTest {

    @Mock
    private CreditoRepository creditoRepository;

    @Mock
    private CreditoMapper creditoMapper;

    @Mock
    private KafkaEventPublisherService kafkaEventPS;

    @InjectMocks
    private CreditoService creditoService;

    private Credito credito;
    private CreditoDTO creditoDTO;

    @BeforeEach
    void setUp() {
        credito = new Credito();
        credito.setId(1L);
        credito.setNumeroCredito("CR001");
        credito.setNumeroNfse("NF001");
        credito.setDataConstituicao(LocalDate.now());
        credito.setValorIssqn(new BigDecimal("100.00"));
        credito.setTipoCredito("ISSQN");
        credito.setSimplesNacional(true);
        credito.setAliquota(new BigDecimal("5.00"));
        credito.setValorFaturado(new BigDecimal("2000.00"));
        credito.setValorDeducao(new BigDecimal("0.00"));
        credito.setBaseCalculo(new BigDecimal("2000.00"));

        creditoDTO = new CreditoDTO();
        creditoDTO.setNumeroCredito("CR001");
        creditoDTO.setNumeroNfse("NF001");
        creditoDTO.setDataConstituicao(LocalDate.now());
        creditoDTO.setValorIssqn(new BigDecimal("100.00"));
        creditoDTO.setTipoCredito("ISSQN");
        creditoDTO.setSimplesNacional("Sim");
        creditoDTO.setAliquota(new BigDecimal("5.00"));
        creditoDTO.setValorFaturado(new BigDecimal("2000.00"));
        creditoDTO.setValorDeducao(new BigDecimal("0.00"));
        creditoDTO.setBaseCalculo(new BigDecimal("2000.00"));
    }

    @Test
    void buscarNumeroNfse_DeveRetornarListaCreditos_QuandoEncontrarCreditos() {
        String numeroNfse = "NF001";
        List<Credito> creditos = Arrays.asList(credito);
        List<CreditoDTO> creditosDTO = Arrays.asList(creditoDTO);

        when(creditoRepository.findByNumeroNfse(numeroNfse)).thenReturn(creditos);
        when(creditoMapper.toDTOList(creditos)).thenReturn(creditosDTO);

        List<CreditoDTO> resultado = creditoService.buscarNumeroNfse(numeroNfse);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(creditoDTO.getNumeroCredito(), resultado.get(0).getNumeroCredito());
        
        verify(creditoRepository).findByNumeroNfse(numeroNfse);
        verify(creditoMapper).toDTOList(creditos);
        verify(kafkaEventPS).publishConsultaEvent("CONSULTA_POR_NFSE", numeroNfse);
    }

    @Test
    void buscarNumeroNfse_DeveLancarNotFoundException_QuandoNaoEncontrarCreditos() {
        String numeroNfse = "NF999";
        when(creditoRepository.findByNumeroNfse(numeroNfse)).thenReturn(Collections.emptyList());

        NotFoundException exception = assertThrows(NotFoundException.class,
            () -> creditoService.buscarNumeroNfse(numeroNfse));
        
        assertEquals("Nenhum crédito encontrado para a NFS-e: " + numeroNfse, exception.getMessage());
        verify(creditoRepository).findByNumeroNfse(numeroNfse);
        verify(creditoMapper, never()).toDTOList(any());
        verify(kafkaEventPS, never()).publishConsultaEvent(anyString(), anyString());
    }

    @Test
    void buscarPorCredito_DeveRetornarCredito_QuandoEncontrarCredito() {
        String numeroCredito = "CR001";
        when(creditoRepository.findByNumeroCredito(numeroCredito)).thenReturn(Optional.of(credito));
        when(creditoMapper.toDTO(credito)).thenReturn(creditoDTO);

        CreditoDTO resultado = creditoService.buscarPorCredito(numeroCredito);

        assertNotNull(resultado);
        assertEquals(creditoDTO.getNumeroCredito(), resultado.getNumeroCredito());
        
        verify(creditoRepository).findByNumeroCredito(numeroCredito);
        verify(creditoMapper).toDTO(credito);
        verify(kafkaEventPS).publishConsultaEvent("CONSULTA_POR_CREDITO", numeroCredito);
    }

    @Test
    void buscarPorCredito_DeveLancarNotFoundException_QuandoNaoEncontrarCredito() {
        String numeroCredito = "CR999";
        when(creditoRepository.findByNumeroCredito(numeroCredito)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
            () -> creditoService.buscarPorCredito(numeroCredito));
        
        assertEquals("Crédito não encontrado: " + numeroCredito, exception.getMessage());
        verify(creditoRepository).findByNumeroCredito(numeroCredito);
        verify(creditoMapper, never()).toDTO(any());
        verify(kafkaEventPS, never()).publishConsultaEvent(anyString(), anyString());
    }
}