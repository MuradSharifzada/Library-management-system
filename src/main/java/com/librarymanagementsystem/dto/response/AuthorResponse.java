package com.librarymanagementsystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Represents the details of an author.")
public class AuthorResponse {

    @Schema(description = "The unique identifier of the author.", example = "1")
    Long id;

    @Schema(description = "The first name of the author.", example = "James")
    String firstName;

    @Schema(description = "The last name of the author.", example = "Clear")
    String lastName;

    @Schema(description = "The birth date of the author.", example = "1986-01-22")
    LocalDate birthDay;

    @Schema(description = "Biography of the author.", example = "James Clear is the author of Atomic Habits, a bestseller on building good habits.")
    String biography;

}
