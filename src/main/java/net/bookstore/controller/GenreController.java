package net.bookstore.controller;


import jakarta.validation.Valid;
import net.bookstore.converter.transferObj.GenreRequest;
import net.bookstore.converter.transferObj.GenreResponse;
import net.bookstore.entity.Genre;
import net.bookstore.service.entity.GenreService;
import net.bookstore.common.api.ApiResponse;
import net.bookstore.common.util.LocaleUtils;
import net.bookstore.converter.impl.ResponseMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping("system/genre")
public class GenreController {
    private final GenreService genreService;
    private final LocaleUtils localeUtils;
    private ResponseMappers responseMappers;

    public GenreController(GenreService genreService, LocaleUtils localeUtils) {
        this.genreService = genreService;
        this.localeUtils = localeUtils;
    }

    @Autowired
    public void setResponseMappers(ResponseMappers responseMappers) {
        this.responseMappers = responseMappers;
    }

    @PostMapping("create")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<GenreResponse> createNewGenre(@Valid @RequestBody GenreRequest genreRequest) {
        Genre newGenre = genreService.createNewGenre(genreRequest);
        var genreResp = responseMappers.genreToResponse(newGenre);
        return ApiResponse.ok(localeUtils.getLocalizedMessage("success.entity.added"), genreResp);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenreById(id);
        return ApiResponse.ok(localeUtils.getLocalizedMessage("success.entity.deleted"));
    }

}
