package co.istad.elearning.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    private String id;

    private String title;

    private String slug;

    private String description;

    private String thumbnail;

    private Double price;

    private Double discount;

    private String content;

    private String code;

    private Boolean isDeleted;

    private Boolean isDraft;

    private LocalDateTime createAt;

    private LocalDateTime updatedAt;

    private Boolean isPaid;

    private String instructorName;

    private String categoryName;

    private List<Section> sections;

}
