package net.bookstore.converter.transferObj;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    @NotBlank(message = "{validation.empty}")
    @Size(min = 2, message = "{validation.size.min}")
    private String title;

    @NotNull(message = "{validation.empty}")
    @Positive(message = "{validation.positive}")
    private Double price;
    @NotNull(message = "{validation.empty}")
    @Positive(message = "{validation.positive}")
    private Long genreId;
    @NotNull(message = "{validation.empty}")
    @Positive(message = "{validation.positive}")
    private Long authorId;
}
