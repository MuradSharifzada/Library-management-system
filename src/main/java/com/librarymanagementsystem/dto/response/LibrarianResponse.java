package com.librarymanagementsystem.dto.response;

import com.librarymanagementsystem.model.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LibrarianResponse {
    Long id;
    String username;
    String email;
    Role role;

}
