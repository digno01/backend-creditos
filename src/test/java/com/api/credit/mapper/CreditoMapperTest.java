package com.api.credit.mapper;

import com.api.credit.dto.CreditoDTO;
import com.api.credit.entity.Credito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreditoMapperTest {

    private CreditoMapper creditoMapper;
    private Credito credito;

    @BeforeEach
    void setUp() {
        creditoMapper = Mappers.getMapper(CreditoMapper.class);
        
        credito = new Credito();
        credito.setId(1L);
        credito.setNumeroCredito("CR001");
        credito.setNumeroNfse("NF001");
        credito.setDataConstituicao(LocalDate.of(2024, 1, 15));
        credito.setValorIssqn(new BigDecimal("100.00"));
        credito.setTipoCredito("ISSQN");
        credito.setSimplesNacional(true);
        credito.setAliquota(new BigDecimal("5.00"));
        credito.setValorFaturado(new BigDecimal("2000.00"));
        credito.setValorDeducao(new BigDecimal("0.00"));
        credito.setBaseCalculo(new BigDecimal("2000.00"));
    }

    @Test
    void toDTO_DeveConverterCreditoParaDTO_ComSimplesNacionalTrue() {
        CreditoDTO resultado = creditoMapper.toDTO(credito);

        assertNotNull(resultado);
        assertEquals(credito.getNumeroCredito(), resultado.getNumeroCredito());
        assertEquals(credito.getNumeroNfse(), resultado.getNumeroNfse());
        assertEquals(credito.getDataConstituicao(), resultado.getDataConstituicao());
        assertEquals(credito.getValorIssqn(), resultado.getValorIssqn());
        assertEquals(credito.getTipoCredito(), resultado.getTipoCredito());
        assertEquals("Sim", resultado.getSimplesNacional()); // boolean true -> "Sim"
        assertEquals(credito.getAliquota(), resultado.getAliquota());
        assertEquals(credito.getValorFaturado(), resultado.getValorFaturado());
        assertEquals(credito.getValorDeducao(), resultado.getValorDeducao());
        assertEquals(credito.getBaseCalculo(), resultado.getBaseCalculo());
    }

    @Test
    void toDTO_DeveConverterCreditoParaDTO_ComSimplesNacionalFalse() {
        credito.setSimplesNacional(false);

        CreditoDTO resultado = creditoMapper.toDTO(credito);

        assertNotNull(resultado);
        assertEquals("Não", resultado.getSimplesNacional()); // boolean false -> "Não"
        assertEquals(credito.getNumeroCredito(), resultado.getNumeroCredito());
        assertEquals(credito.getNumeroNfse(), resultado.getNumeroNfse());
    }

    @Test
    void toDTO_DeveRetornarNull_QuandoCreditoForNull() {
        CreditoDTO resultado = creditoMapper.toDTO(null);

        assertNull(resultado);
    }

    @Test
    void toDTOList_DeveConverterListaDeCreditosParaDTOs() {
        Credito credito2 = new Credito();
        credito2.setId(2L);
        credito2.setNumeroCredito("CR002");
        credito2.setNumeroNfse("NF002");
        credito2.setDataConstituicao(LocalDate.of(2024, 2, 15));
        credito2.setValorIssqn(new BigDecimal("200.00"));
        credito2.setTipoCredito("ISSQN");
        credito2.setSimplesNacional(false);
        credito2.setAliquota(new BigDecimal("3.00"));
        credito2.setValorFaturado(new BigDecimal("6666.67"));
        credito2.setValorDeducao(new BigDecimal("0.00"));
        credito2.setBaseCalculo(new BigDecimal("6666.67"));

        List<Credito> creditos = Arrays.asList(credito, credito2);

        List<CreditoDTO> resultado = creditoMapper.toDTOList(creditos);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        
        // Primeiro crédito
        assertEquals("CR001", resultado.get(0).getNumeroCredito());
        assertEquals("Sim", resultado.get(0).getSimplesNacional());
        
        // Segundo crédito
        assertEquals("CR002", resultado.get(1).getNumeroCredito());
        assertEquals("Não", resultado.get(1).getSimplesNacional());
    }

    @Test
    void toDTOList_DeveRetornarListaVazia_QuandoListaForVazia() {
        List<Credito> creditosVazios = Arrays.asList();

        List<CreditoDTO> resultado = creditoMapper.toDTOList(creditosVazios);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void toDTOList_DeveRetornarNull_QuandoListaForNull() {
        List<CreditoDTO> resultado = creditoMapper.toDTOList(null);

        assertNull(resultado);
    }

    @Test
    void booleanToString_DeveConverterTrueParaSim() {
        String resultado = creditoMapper.booleanToString(true);

        assertEquals("Sim", resultado);
    }

    @Test
    void booleanToString_DeveConverterFalseParaNao() {
        String resultado = creditoMapper.booleanToString(false);

        assertEquals("Não", resultado);
    }
}