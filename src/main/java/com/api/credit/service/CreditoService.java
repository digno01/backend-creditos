package com.api.credit.service;

import com.api.credit.dto.CreditoDTO;
import com.api.credit.entity.Credito;
import com.api.credit.exception.NotFoundException;
import com.api.credit.mapper.CreditoMapper;
import com.api.credit.repository.CreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CreditoService {

    @Autowired
    private CreditoRepository creditoRepository;

    @Autowired
    private CreditoMapper creditoMapper;

    @Autowired
    private KafkaEventPublisherService kafkaEventPS;

    public List<CreditoDTO> buscarNumeroNfse(String numeroNfse) {
        List<Credito> creditos = creditoRepository.findByNumeroNfse(numeroNfse);

        if (creditos.isEmpty()) {
            throw new NotFoundException("Nenhum crédito encontrado para a NFS-e: " + numeroNfse);
        }

        // Publicar evento de consulta
        kafkaEventPS.publishConsultaEvent("CONSULTA_POR_NFSE", numeroNfse);

        return creditoMapper.toDTOList(creditos);
    }

    public CreditoDTO buscarPorCredito(String numeroCredito) {
        Credito credito = creditoRepository.findByNumeroCredito(numeroCredito)
                .orElseThrow(() -> new NotFoundException("Crédito não encontrado: " + numeroCredito));

        // Publicar evento de consulta
        kafkaEventPS.publishConsultaEvent("CONSULTA_POR_CREDITO", numeroCredito);

        return creditoMapper.toDTO(credito);
    }
}