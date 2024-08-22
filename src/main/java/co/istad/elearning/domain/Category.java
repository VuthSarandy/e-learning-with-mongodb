package co.istad.elearning.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "categories")
@Data
@NoArgsConstructor
public class Category {
    @Id
    private String id;

    private String name;

    private String icon;

    private Boolean isDeleted;

}
