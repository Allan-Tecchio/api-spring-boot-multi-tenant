package br.com.allantecchio.apispringboottenant.api;

import br.com.allantecchio.apispringboottenant.dto.NotaFiscalXmlDTO;
import br.com.allantecchio.apispringboottenant.service.NotaFiscalXmlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/xml")
@RequiredArgsConstructor
public class NotaFiscalRest {

    private final NotaFiscalXmlService service;

    @GetMapping("/top")
    public List<NotaFiscalXmlDTO> buscarTop100() {
        List<NotaFiscalXmlDTO> nff = service.buscarTop100();
        return nff;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }
}
