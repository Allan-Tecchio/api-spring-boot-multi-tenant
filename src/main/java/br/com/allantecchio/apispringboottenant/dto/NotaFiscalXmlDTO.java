package br.com.allantecchio.apispringboottenant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotaFiscalXmlDTO {

    private Integer id;
    private Integer idNotaFiscal;
    private Integer numNota;
    private String xml;
}
