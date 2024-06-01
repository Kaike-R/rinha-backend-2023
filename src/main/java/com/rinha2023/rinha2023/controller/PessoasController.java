package com.rinha2023.rinha2023.controller;

import com.rinha2023.rinha2023.Entity.PessoaEntity;
import com.rinha2023.rinha2023.repository.PessoasRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import org.slf4j.Logger;
import java.util.List;
import java.util.UUID;

@RestController
public class PessoasController {

    private static final Logger logger = LoggerFactory.getLogger(PessoasController.class);
    @Autowired
    public PessoasRepository pessoasRepository;


    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @GetMapping(value = "/pessoas/{id}")
    public ResponseEntity<PessoaEntity> getPessoaById(@PathVariable UUID id) {
        logger.info("getPessoaById: " + id);

        try {

            PessoaEntity x = pessoasRepository.getEntityById(id);
            logger.info("Return entity: " + x);
            return ResponseEntity.ok(x);
        } catch (Exception e){
            logger.info("Exception Not Found: " + e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/pessoas")
    public ResponseEntity<PessoaEntity> createPessoa(@RequestBody(required = false) @Validated PessoaEntity pessoa) {


        if (pessoa.getStack() == null)
        {
            if (isNumeric(pessoa.getNome()) == true || isNumeric(pessoa.getApelido()) == true)
            {
                logger.info("pessoa possui um campo que pode ser trasformado em double");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            if (isNumeric(pessoa.getNome()) == true || isNumeric(pessoa.getApelido()) == true || pessoa.getStack().stream().anyMatch(this::isNumeric) == true)
            {
                logger.info("pessoa possui um campo que pode ser trasformado em double");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }



        logger.info("createPessoa: " + pessoa);
        try {
            PessoaEntity newPessoa = pessoasRepository.insertPessoa(pessoa);
            logger.info("newPessoa: " + newPessoa);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, "/pessoas/" + newPessoa.getId().toString());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.info("Erro createPessoa: " + e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

//    @GetMapping(value = "/")
//    public ResponseEntity<List<PessoaEntity>> getAll() {
//
//
//        List<PessoaEntity> x = pessoasRepository.getAllEntities();
//
//        return ResponseEntity.ok(x);
//    }

    @GetMapping(value = "/contagem-pessoas")
    public ResponseEntity<Integer> getCount() {
        return ResponseEntity.ok(pessoasRepository.getCount());
    }

    @GetMapping(value = "/pessoas")
    public ResponseEntity<List<PessoaEntity>> getSearch(@RequestParam(value = "t", required = true) String place)
    {
        logger.info("getSearch: " + place);
        if (place.isEmpty()) {
            logger.info("Place: "+ place + "Fora do Padrao");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<PessoaEntity> x = pessoasRepository.search(place);

        return ResponseEntity.ok(x);
    }
}
