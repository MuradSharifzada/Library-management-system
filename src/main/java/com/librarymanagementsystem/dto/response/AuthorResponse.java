package com.librarymanagementsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Represents the details of an author.")
@Builder
public class AuthorResponse {

    @Schema(description = "The unique identifier of the author.", example = "1")
    Long id;

    @Schema(description = "The first name of the author.", example = "James")
    String firstName;

    @Schema(description = "The last name of the author.", example = "Clear")
    String lastName;

    @Schema(description = "The birth date of the author.", example = "1986-01-22")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate birthDay;

    @Schema(description = "Biography of the author.", example = "James Clear is the author of Atomic Habits, a bestseller on building good habits.")
    String biography;

}
