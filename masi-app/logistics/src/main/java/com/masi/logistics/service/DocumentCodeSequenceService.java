package com.masi.logistics.service;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.DocumentCodeSequence;
import com.masi.logistics.repository.DocumentCodeSequenceRepository;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.DocumentCodeSequence}.
 */
@Service
@Transactional
public class DocumentCodeSequenceService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentCodeSequenceService.class);

    private final DocumentCodeSequenceRepository documentCodeSequenceRepository;
    private final String[] VALID_COMPANY = {"KIM_LONG", "MMS"};

    public DocumentCodeSequenceService(
        DocumentCodeSequenceRepository documentCodeSequenceRepository
    ) {
        this.documentCodeSequenceRepository = documentCodeSequenceRepository;
    }
    public Mono<Void> makeSureDocumentCodeSequenceExist(String documentType) {
        return this.makeSureDocumentCodeSequenceExist(documentType, "%05d");
    }
    public Mono<Void> makeSureDocumentCodeSequenceExist(String documentType,String format) {
        return Flux.fromArray(VALID_COMPANY)
            .flatMap(company -> documentCodeSequenceRepository.findFirstByCompanyAndDocumentType(company, documentType)
                .switchIfEmpty(Mono.defer(() -> {
                    DocumentCodeSequence documentCodeSequence = new DocumentCodeSequence();
                    documentCodeSequence.setCompany(company);
                    documentCodeSequence.setDocumentType(documentType);
                    documentCodeSequence.setCurrentSequence(1);
                    documentCodeSequence.setJavaFormat(format);
                    documentCodeSequence.setId(UUID.randomUUID());
                    return documentCodeSequenceRepository.save(documentCodeSequence);
                }))
            )
            .then();
    }

    public Mono<DocumentCodeSequence> getByDocumentType(String documentType, String company) {
        return documentCodeSequenceRepository.findFirstByCompanyAndDocumentType(company, documentType);
    }

    public Mono<DocumentCodeSequence> getByDocumentType(String documentType) {
        return SecurityUtils.getCompanyId().flatMap(company -> getByDocumentType(documentType, company));
    }


    public Mono<Void> updateSequence(DocumentCodeSequence documentCodeSequence) {
        return documentCodeSequenceRepository.save(documentCodeSequence).then();
    }

    public  Mono<Void> updateCurrentSequence(String entityName) {
        return getByDocumentType(entityName)
            .flatMap(documentCodeSequence -> {
                documentCodeSequence.setCurrentSequence(documentCodeSequence.getCurrentSequence() + 1);
                return updateSequence(documentCodeSequence);
            });
    }
}
