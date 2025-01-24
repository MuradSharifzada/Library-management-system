package com.librarymanagementsystem.mapper;

import com.librarymanagementsystem.dto.request.StudentRequest;
import com.librarymanagementsystem.dto.response.StudentResponse;
import com.librarymanagementsystem.model.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mappings(value = {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "orders", ignore = true),
            @Mapping(target = "enrollmentDate", expression = "java(java.time.LocalDateTime.now())")
    })
    Student requestToEntity(StudentRequest request);

    StudentResponse entityToResponse(Student student);

}
