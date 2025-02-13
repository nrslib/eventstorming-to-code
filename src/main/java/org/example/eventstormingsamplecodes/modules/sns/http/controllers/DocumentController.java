package org.example.eventstormingsamplecodes.modules.sns.http.controllers;

import lombok.RequiredArgsConstructor;
import org.example.eventstormingsamplecodes.http.controllers.NotFoundException;
import org.example.eventstormingsamplecodes.modules.sns.app.application.services.document.DocumentApplicationService;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.DocumentId;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.UserId;
import org.example.eventstormingsamplecodes.modules.sns.app.infrastucture.persistence.document.jpa.DocumentJpaRepository;
import org.example.eventstormingsamplecodes.modules.sns.http.models.documents.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentApplicationService applicationService;
    private final DocumentJpaRepository documentJpaRepository;

    @GetMapping("/{documentId}")
    public GetDocumentResponse get(@PathVariable String documentId) {
        return documentJpaRepository.findById(documentId)
                .map(GetDocumentResponse::from)
                .orElseThrow(() -> new NotFoundException("Document not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDocumentResponse post(@RequestBody PostDocumentRequest request) {
        var documentId = applicationService.createDocument(new UserId(request.userId()), request.content());

        return new PostDocumentResponse(documentId.value());
    }

    @PutMapping("/{documentId}")
    public void put(
            @PathVariable String documentId,
            @RequestBody PutDocumentRequest request
    ) {
        applicationService.updateDocument(
                new DocumentId(documentId),
                new UserId(request.userId()),
                request.content()
        );
    }

    @PostMapping("/{documentId}/effective")
    public void markEffective(
            @PathVariable String documentId,
            @RequestBody MarkEffectiveRequest request
    ) {
        applicationService.markEffective(
                new DocumentId(documentId),
                new UserId(request.userId())
        );
    }

    @DeleteMapping("/{documentId}/effective")
    public void unmarkEffective(
            @PathVariable String documentId,
            @RequestBody UnmarkEffectiveRequest request
    ) {
        applicationService.unmarkEffective(
                new DocumentId(documentId),
                new UserId(request.userId())
        );
    }
}
