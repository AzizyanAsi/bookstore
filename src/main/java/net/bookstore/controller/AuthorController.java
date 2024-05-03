package net.bookstore.controller;

import jakarta.validation.Valid;
import net.bookstore.converter.transferObj.AuthorRequest;
import net.bookstore.converter.transferObj.AuthorResponse;
import net.bookstore.entity.Author;
import net.bookstore.service.entity.AuthorService;
import net.bookstore.common.api.ApiResponse;
import net.bookstore.common.util.LocaleUtils;
import net.bookstore.converter.impl.ResponseMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@Validated
@RestController
@RequestMapping("system/author")
public class AuthorController {
    private final AuthorService genreService;
    private final LocaleUtils localeUtils;
    private ResponseMappers responseMappers;

    public AuthorController(AuthorService genreService, LocaleUtils localeUtils) {
        this.genreService = genreService;
        this.localeUtils = localeUtils;
    }


    @Autowired
    public void setResponseMappers(ResponseMappers responseMappers) {
        this.responseMappers = responseMappers;
    }

    @PostMapping("create")
    @PreAuthorize("hasRole('ADMIN')") //add localize messages
    public ApiResponse<AuthorResponse> createNewAuthor(@Valid @RequestBody AuthorRequest authorRequest) {
        Author newAuthor = genreService.createNewAuthor(authorRequest);
        var genreResp = responseMappers.authorToResponse(newAuthor);
        return ApiResponse.ok(localeUtils.getLocalizedMessage("success.entity.added"), genreResp);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteAuthor(@PathVariable Long id) {
        genreService.deleteAuthorById(id);
        return ApiResponse.ok(localeUtils.getLocalizedMessage("success.entity.deleted"));
    }
}
