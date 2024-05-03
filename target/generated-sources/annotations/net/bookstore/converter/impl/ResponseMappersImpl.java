package net.bookstore.converter.impl;

import javax.annotation.processing.Generated;
import net.bookstore.converter.transferObj.AuthorResponse;
import net.bookstore.converter.transferObj.BookResponse;
import net.bookstore.converter.transferObj.GenreResponse;
import net.bookstore.converter.transferObj.RoleResponse;
import net.bookstore.converter.transferObj.SystemUserResponse;
import net.bookstore.entity.Author;
import net.bookstore.entity.Book;
import net.bookstore.entity.Genre;
import net.bookstore.entity.Role;
import net.bookstore.entity.SystemUser;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-03T19:55:54+0400",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (JetBrains s.r.o.)"
)
@Component
public class ResponseMappersImpl extends ResponseMappers {

    @Override
    public RoleResponse roleToResponse(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleResponse roleResponse = new RoleResponse();

        if ( role.getRoleType() != null ) {
            roleResponse.setRoleType( role.getRoleType().name() );
        }
        roleResponse.setDescription( role.getDescription() );

        return roleResponse;
    }

    @Override
    public BookResponse bookToResponse(Book book) {
        if ( book == null ) {
            return null;
        }

        BookResponse bookResponse = new BookResponse();

        bookResponse.setId( book.getId() );
        bookResponse.setTitle( book.getTitle() );
        bookResponse.setPrice( book.getPrice() );
        bookResponse.setGenre( genreToResponse( book.getGenre() ) );
        bookResponse.setAuthor( authorToResponse( book.getAuthor() ) );

        return bookResponse;
    }

    @Override
    public GenreResponse genreToResponse(Genre genre) {
        if ( genre == null ) {
            return null;
        }

        GenreResponse genreResponse = new GenreResponse();

        genreResponse.setId( genre.getId() );
        genreResponse.setName( genre.getName() );

        return genreResponse;
    }

    @Override
    public AuthorResponse authorToResponse(Author author) {
        if ( author == null ) {
            return null;
        }

        AuthorResponse authorResponse = new AuthorResponse();

        authorResponse.setId( author.getId() );
        authorResponse.setFirstName( author.getFirstName() );
        authorResponse.setLastName( author.getLastName() );

        return authorResponse;
    }

    @Override
    public SystemUserResponse systemUserToResponse(SystemUser systemUser) {
        if ( systemUser == null ) {
            return null;
        }

        SystemUserResponse systemUserResponse = new SystemUserResponse();

        systemUserResponse.setId( systemUser.getId() );
        systemUserResponse.setFirstName( systemUser.getFirstName() );
        systemUserResponse.setLastName( systemUser.getLastName() );
        systemUserResponse.setEmail( systemUser.getEmail() );
        systemUserResponse.setPhoneNumber( systemUser.getPhoneNumber() );
        systemUserResponse.setActive( systemUser.getActive() );
        systemUserResponse.setRole( roleToResponse( systemUser.getRole() ) );
        systemUserResponse.setCreated( systemUser.getCreated() );
        systemUserResponse.setUpdated( systemUser.getUpdated() );

        return systemUserResponse;
    }
}
