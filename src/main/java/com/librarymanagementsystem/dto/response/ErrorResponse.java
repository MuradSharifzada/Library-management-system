package com.librarymanagementsystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents the details of an error response returned by the API.")
public class ErrorResponse {

    @Schema(description = "The HTTP status of the error response.", example = "BAD_REQUEST")
    HttpStatus status;

    @Schema(description = "The date and time when the error occurred.", example = "2025-01-06T15:30:45")
    LocalDateTime date;

    @Schema(description = "The detailed error message describing the issue.", example = "Invalid request parameters")
    String errorMessage;

    @Schema(description = "The specific error code for identifying the type of error.",example = "404")
    int errorCode;

}
