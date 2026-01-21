package br.com.allantecchio.apispringboottenant.service;

import br.com.allantecchio.apispringboottenant.dto.NotaFiscalXmlDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class NotaFiscalXmlService {

    private final NamedParameterJdbcTemplate jdbc;
    private final String eventos = "100,101,102,110,135,205";

    public List<NotaFiscalXmlDTO> buscarTop100() {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append("    nfe.id AS id, ");
        sql.append("    nf.num_nota AS \"numNota\" ");
        sql.append(" FROM nota_fiscal_evento nfe ");
        sql.append(" inner join nota_fiscal nf on nf.id = nfe.id_nota_fiscal ");
        sql.append(" WHERE nfe.codigo_sefaz in (").append(eventos).append(")");
        sql.append(" ORDER BY id DESC");
        sql.append(" LIMIT :limit ");

        Map<String, Object> params = Map.of(
                "limit", 100
        );

        return jdbc.query(
                sql.toString(),
                params,
                BeanPropertyRowMapper.newInstance(NotaFiscalXmlDTO.class)
        );
    }
}
