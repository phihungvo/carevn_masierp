package com.masi.sale.service;

import com.masi.sale.repository.ContractFileRepository;
import com.masi.sale.service.dto.ContractFile;
import com.masi.sale.service.dto.ContractDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ContractFileService {

    private static final Logger log = LoggerFactory.getLogger(ContractService.class);

    private final ContractFileRepository contractFileRepository;

    public ContractFileService(ContractFileRepository contractFileRepository) {
        this.contractFileRepository = contractFileRepository;
    }


//    public Mono<com.masi.sale.domain.ContractFile> saveContractFiles(ContractDTO contractDTO) {
//        ContractFile base64Request = contractDTO.getContractFile();
//        boolean hasContractFileData = StringUtils.isNotBlank(base64Request.getContractFile())
//            || StringUtils.isNotBlank(base64Request.getAppendixFile());
//
//        if (!hasContractFileData) {
//            return Mono.empty();
//        }
//        String savedContractName = base64Request.getContractFile();
//        String savedAppendixName = base64Request.getAppendixFile();
//
//        com.masi.sale.domain.ContractFile contractFile = new com.masi.sale.domain.ContractFile();
//        contractFile.setId(UUID.randomUUID());
//        contractFile.setCreatedDate(ZonedDateTime.now());
//        contractFile.setLastUpdated(ZonedDateTime.now());
//        contractFile.setContractId(contractDTO.getId());
//        contractFile.setIsDeleted(false);
//        contractFile.setContractFilePath(savedContractName);
//        contractFile.setContractIndexFilePath(savedAppendixName);
//        return contractFileRepository.save(contractFile);
//    }

    public Mono<List<com.masi.sale.domain.ContractFile>> saveContractFiles(ContractDTO contractDTO) {
        List<String> contractFileNewList = contractDTO.getContractFile().getContractFileNew();
        return contractFileRepository.deleteByContractId(contractDTO.getId())
                .thenMany(Flux.fromIterable(contractFileNewList)
                        .flatMap(fileName -> {
                            com.masi.sale.domain.ContractFile contractFile = new com.masi.sale.domain.ContractFile();
                            contractFile.setId(UUID.randomUUID());
                            contractFile.setCreatedDate(ZonedDateTime.now());
                            contractFile.setLastUpdated(ZonedDateTime.now());
                            contractFile.setContractId(contractDTO.getId());
                            contractFile.setIsDeleted(false);
                            contractFile.setContractFilePath(fileName);
                            return contractFileRepository.save(contractFile);
                        })
                )
                .collectList(); // Collect all ContractFiles into a List and wrap it in a Mono
    }

}
