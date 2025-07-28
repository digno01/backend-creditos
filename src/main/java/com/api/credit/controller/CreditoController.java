package com.api.credit.controller;

import com.api.credit.dto.CreditoDTO;
import com.api.credit.service.CreditoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/creditos")
@CrossOrigin(origins = "*")
public class CreditoController {

    @Autowired
    private CreditoService creditoService;

    @GetMapping("/{numeroNfse}")
    public ResponseEntity<List<CreditoDTO>> buscarPorNfse(@PathVariable String numeroNfse) {
        List<CreditoDTO> creditos = creditoService.buscarNumeroNfse(numeroNfse);
        return ResponseEntity.ok(creditos);
    }

    @GetMapping("/credito/{numeroCredito}")
    public ResponseEntity<CreditoDTO> buscarPorCredito(@PathVariable String numeroCredito) {
        CreditoDTO credito = creditoService.buscarPorCredito(numeroCredito);
        return ResponseEntity.ok(credito);
    }
}