package co.istad.elearning.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class Video {

    private Integer orderNo;

    private String lectureNo;

    private String title;

    private String fileName;


}
