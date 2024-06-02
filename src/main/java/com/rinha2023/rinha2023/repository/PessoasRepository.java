package com.rinha2023.rinha2023.repository;

import com.rinha2023.rinha2023.Entity.PessoaEntity;
import com.rinha2023.rinha2023.controller.PessoasController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.relational.core.sql.SQL;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.rinha2023.rinha2023.Util;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class PessoasRepository {
    Util util = new Util();
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(PessoasController.class);
    public List<PessoaEntity> getAllEntities() {
        String sql = "SELECT id, nome, apelido, nascimento FROM pessoas;";


        List<PessoaEntity> pessoas = jdbcTemplate.query(sql, (rs, rowNum) -> new PessoaEntity(
                (UUID) rs.getObject("id"), rs.getString("nome"), rs.getString("apelido"),
                rs.getDate("nascimento")//, util.desfazerFlatData(rs.getString("stack"))
        ));
        return pessoas;
    }

    public PessoaEntity getEntityById(UUID id) {
        String sql = "SELECT id, nome, apelido, nascimento, stack FROM pessoas WHERE id = ?;";


        return jdbcTemplate.queryForObject(sql, new RowMapper<PessoaEntity>() {
            public PessoaEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                PessoaEntity pessoaEntity = new PessoaEntity();
                pessoaEntity.setId((UUID) rs.getObject("id"));
                pessoaEntity.setNome(rs.getString("nome"));
                pessoaEntity.setApelido(rs.getString("apelido"));
                pessoaEntity.setNascimento(rs.getDate("nascimento"));
                pessoaEntity.setStack(util.desfazerFlatData(rs.getString("stack")));
                return pessoaEntity;
            }

        }, id);
    }

    public Integer getCount() {
        String sql = "SELECT count(*) FROM pessoas";

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public PessoaEntity insertPessoa(PessoaEntity pessoaEntity) throws SQLException {
        String sql = "INSERT INTO pessoas (id ,nome, apelido, nascimento, stack) VALUES (?, ?, ?, ?, ?)";
        String sqlVerify = "SELECT count(*) FROM pessoas WHERE apelido = ?";

        pessoaEntity.setId(UUID.randomUUID());
        Date date = pessoaEntity.getNascimento();//pode ser melhorado

        String stack = util.criarFlatData(pessoaEntity.getStack());

//        if (jdbcTemplate.queryForObject(sqlVerify, Integer.class, pessoaEntity.getApelido()) > 0){
//            System.out.println("Já existe alguem com esse apelido");
//            return pessoaEntity;
//        }

        try {
            jdbcTemplate.update(sql, pessoaEntity.getId(), pessoaEntity.getNome(), pessoaEntity.getApelido(), date, stack);
            return pessoaEntity;
        } catch (DataAccessException e) {
            logger.info("Não foi possivel fazer o insert: " + e.getMessage());
            throw new SQLException();
        }


    }

    public List<PessoaEntity> search(String string) {
        //String sql = "SELECT * FROM pessoas WHERE apelido ILIKE '%' || ? || '%' OR nome ILIKE '%' || ? || '%' OR stack ILIKE '%' || ? || '%' LIMIT 50";
        String sql = "SELECT * FROM pessoas WHERE searchable ILIKE '%' || ? || '%' ";
        return jdbcTemplate.query(sql, new RowMapper<PessoaEntity>() {
            public PessoaEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                PessoaEntity pessoaEntity = new PessoaEntity();
                pessoaEntity.setId((UUID) rs.getObject("id"));
                pessoaEntity.setNome(rs.getString("nome"));
                pessoaEntity.setApelido(rs.getString("apelido"));
                pessoaEntity.setNascimento(rs.getDate("nascimento"));
                pessoaEntity.setStack(util.desfazerFlatData(rs.getString("stack")));
                return pessoaEntity;
            }

        }, string);


    }

}
