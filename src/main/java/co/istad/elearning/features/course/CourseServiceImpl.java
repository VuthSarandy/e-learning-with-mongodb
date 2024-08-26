package co.istad.elearning.features.course;

import co.istad.elearning.domain.Course;
import co.istad.elearning.domain.Section;
import co.istad.elearning.domain.Video;
import co.istad.elearning.features.category.CategoryRepository;
import co.istad.elearning.features.course.dto.*;
import co.istad.elearning.mapper.CourseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final CategoryRepository categoryRepository;

    private final CourseMapper courseMapper;

    private final MongoTemplate mongoTemplate;

    @Override
    public void createCourse(CourseCreateRequest courseCreateRequest) {

        if (!categoryRepository.existsByNameAndIsDeletedIsFalse(courseCreateRequest.categoryName())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Category not found...!"
            );
        }

        Course course = new Course();

        course.setTitle(courseCreateRequest.title());
        course.setSlug(courseCreateRequest.slug());
        course.setDescription(courseCreateRequest.description());
        course.setThumbnail(courseCreateRequest.thumbnail());
        course.setPrice(courseCreateRequest.price());
        course.setContent(courseCreateRequest.content());
        course.setCategoryName(courseCreateRequest.categoryName());
        course.setIsDraft(true);
        course.setIsPaid(false);
        course.setIsDeleted(false);
        course.setCreateAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        course.setSections(List.of());
        course.setCode(UUID.randomUUID().toString().substring(0,5));
        course.setInstructorName("Vuth Sarandy");

        courseRepository.save(course);

    }

    @Override
    public Page<?> findAllCourse(int page, int size,String response) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> findAllCourses = courseRepository.findAllByIsDeletedIsFalseAndIsDraftIsFalse(pageRequest);

        if (response.equals("COURSE_DETAIL")){
            return findAllCourses.map(courseMapper::toCourseDetailResponse);
        }
        return findAllCourses.map(courseMapper::toCourseSnippetResponse);

    }

    @Override
    public ResponseEntity<?> findByCourseId(String id,String response) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found"));
        if (response.equals("COURSE_DETAIL")){
            CourseDetailResponse detailResponse = courseMapper.toCourseDetailResponse(course);
            return ResponseEntity.ok(courseMapper.toCourseDetailResponse(course));
        }
        CourseSnippetResponse courseSnippetResponse = courseMapper.toCourseSnippetResponse(course);
        return ResponseEntity.ok(courseMapper.toCourseSnippetResponse(course));

    }

    @Override
    public void deleteCourseById(String id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found"));
        courseRepository.delete(course);
    }

    @Override
    public void updateVisibility(String id, Boolean visibility) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found"));
        course.setIsDraft(visibility);
        courseRepository.save(course);
    }

    @Override
    public void enableCourse(String id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found"));
        course.setIsDeleted(false);
        courseRepository.save(course);
    }

    @Override
    public void disableCourse(String id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found"));
        course.setIsDeleted(true);
        courseRepository.save(course);
    }

// today work
    @Override
    public List<CourseDetailResponse> findAllCourseByInstructorName(String instructorName) {
        List<Course> courses = courseRepository.findAllByInstructorNameAndIsDeletedIsFalse(instructorName);

        return courses.stream()
                .map(courseMapper::toCourseDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateVideoInSection(String id, VideoUpdateRequest videoUpdateRequest) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found"));

        List<Section> sections = course.getSections();
        if (sections == null || sections.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No sections available in the course..."
            );
        }

        Integer sectionOrderNo = videoUpdateRequest.sectionOrderNo();
        Section targetSection = sections.stream()
                .filter(section -> section.getOrderNo().equals(sectionOrderNo))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                         "Section with order number " + sectionOrderNo + "not found...!"));

        List<Video> updatedVideos = videoUpdateRequest.videos().stream()
                .collect(Collectors.toMap(
                        Video::getOrderNo,
                        video -> video,
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .sorted(Comparator.comparingInt(Video::getOrderNo))
                .collect(Collectors.toList());

        targetSection.setVideos(updatedVideos);

        courseRepository.save(course);
    }

    @Override
    public void createCourseSection(String id, SectionCreateRequest sectionCreateRequest) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found...!"
                )
        );
        List<Section> sections = course.getSections();
        if (sections == null) {
            sections = new ArrayList<>();
        }

        Integer newOrderNo = sectionCreateRequest.orderNo();
        for (Section section : sections) {
            if (section.getOrderNo().equals(newOrderNo)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Section with order number " + newOrderNo + " already exists...!"
                );
            }
        }

        Section newSection = Section.builder()
                .title(sectionCreateRequest.title())
                .orderNo(newOrderNo)
                .videos(sectionCreateRequest.videos())
                .build();

        sections.add(newSection);

        course.setSections(sections);

        courseRepository.save(course);
    }

    @Override
    public void createVideoInSection(String id, VideoCreateRequest videoCreateRequest) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found...!"
                )
        );
        List<Section> sections = course.getSections();
        if (sections == null || sections.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No sections available in the course..."
            );
        }

        Integer sectionOrderNo = videoCreateRequest.sectionOrderNo();
        Section targetSection = sections.stream()
                .filter(section -> section.getOrderNo().equals(sectionOrderNo))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                         "Section with order number " + sectionOrderNo + " not found...!"));

        Video newVideo = Video.builder()
                .lectureNo(videoCreateRequest.lectureNo())
                .orderNo(videoCreateRequest.orderNo())
                .title(videoCreateRequest.title())
                .fileName(videoCreateRequest.fileName())
                .build();

        List<Video> videos = targetSection.getVideos();
        if (videos == null) {
            videos = new ArrayList<>();
            targetSection.setVideos(videos);
        } else {
            boolean isDuplicate = videos.stream()
                    .anyMatch(video -> video.getOrderNo().equals(newVideo.getOrderNo()));
            if (isDuplicate) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Video order number must be unique within the section....! "
                );
            }
        }
        videos.add(newVideo);

        courseRepository.save(course);


    }

    @Override
    public void updateCourse(String id, CourseUpdateRequest courseUpdateRequest) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found...!"
                )
        );
        if (!categoryRepository.existsByNameAndIsDeletedIsFalse(courseUpdateRequest.categoryName())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Category not found"
            );
        }
        courseMapper.updateCourseFromRequest(courseUpdateRequest, course);

        courseRepository.save(course);
    }

    @Override
    public Page<?> findAllCourseIsFree(int page, int size, String response) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> findAllCourses = courseRepository.findAllCourseIsFree( pageRequest);

        if (response.equals("COURSE_DETAIL")){
            return findAllCourses.map(courseMapper::toCourseDetailResponse);
        }
        return findAllCourses.map(courseMapper::toCourseSnippetResponse);
    }

    @Override
    public Page<?> findAllCoursePrivate(int page, int size, String response) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> findAllCourses = courseRepository.findByIsDeletedIsFalseAndIsDraftIsTrue(pageRequest);

        if (response.equals("COURSE_DETAIL")){
            return findAllCourses.map(courseMapper::toCourseDetailResponse);
        }

        return findAllCourses.map(courseMapper::toCourseSnippetResponse);
    }

    @Override
    public Page<?> findAllCoursePublic(int page, int size, String response) {
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Course> findAllCourses = courseRepository.findAllByIsDeletedIsFalseAndIsDraftIsFalse(pageRequest);

        if (response.equals("COURSE_DETAIL")){
            return findAllCourses.map(courseMapper::toCourseDetailResponse);
        }
        return findAllCourses.map(courseMapper::toCourseSnippetResponse);
    }

    @Override
    public void updateIsPaid(String id, Boolean isPaid) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found...!"
                )
        );
        course.setIsPaid(isPaid);
        courseRepository.save(course);
    }

    @Override
    public void updateThumbnail(String id, CourseUpdateThumbnail courseUpdateThumbnail) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found...!"
                )
        );
        course.setThumbnail(courseUpdateThumbnail.thumbnail());
        courseRepository.save(course);
    }

    @Override
    public ResponseEntity<?> findCourseBySlug(String slug, String response) {
        Course course = courseRepository.findBySlugAndIsDeletedIsFalseAndIsDraftIsFalse(slug).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found...!"
                )
        );

        if (response.equals("COURSE_DETAIL")){
            return new ResponseEntity<>(courseMapper.toCourseDetailResponse(course), HttpStatus.OK);
        }
        return new ResponseEntity<>(courseMapper.toCourseSnippetResponse(course), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findCoursePublic(String slug, String response) {
        return null;
    }

    public Page<?> advancedSearchCourse(int page, int size, String filterAnd, String filterOr, String orders, String response) {
        log.info("Searching students with filterAnd: {}, filterOr: {}, orders: {}", filterAnd, filterOr, orders);

        Query query = new Query();

        // Add AND filters
        if (filterAnd != null && !filterAnd.isEmpty()) {
            List<Criteria> andCriteria = parseFilterCriteria(filterAnd);
            query.addCriteria(new Criteria().andOperator(andCriteria.toArray(new Criteria[0])));
        }

        // Add OR filters
        if (filterOr != null && !filterOr.isEmpty()) {
            List<Criteria> orCriteria = parseFilterCriteria(filterOr);
            query.addCriteria(new Criteria().orOperator(orCriteria.toArray(new Criteria[0])));
        }

        // Add sorting
        if (orders != null && !orders.isEmpty()) {
            Sort sort = parseSortOrders(orders);
            query.with(sort);
        }

        // Apply pagination
        PageRequest pageRequest = PageRequest.of(page, size);
        query.with(pageRequest);

        // Execute query
        if (response.equals("COURSE_DETAIL")) {
            List<CourseDetailResponse> courses = mongoTemplate.find(query, Course.class)
                    .stream()
                    .map(courseMapper::toCourseDetailResponse)
                    .collect(Collectors.toList());
            long count = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Course.class);
            return new PageImpl<>(courses, pageRequest, count);
        }

        List<CourseSnippetResponse> courses = mongoTemplate.find(query, Course.class)
                .stream()
                .map(courseMapper::toCourseSnippetResponse)
                .collect(Collectors.toList());

        // Clone query for count operation to avoid conflict
        Query countQuery = Query.of(query).limit(-1).skip(-1);
        long count = mongoTemplate.count(countQuery, Course.class);

        return new PageImpl<>(courses, pageRequest, count);
    }
    private List<Criteria> parseFilterCriteria(String filter) {
        List<Criteria> criteriaList = new ArrayList<>();
        String[] conditions = filter.split(",");

        for (String condition : conditions) {
            String[] parts = condition.split("\\|");
            if (parts.length == 3) {
                String field = parts[0];       // Field name, e.g., "name", "address", etc.
                String operator = parts[1];    // Operator, e.g., "eq", "gt", "regex", etc.
                String value = parts[2];       // Value to compare against, e.g., "mengseu", "pp", etc.


                switch (operator.toLowerCase()) {
                    case "eq":  // Equals
                        criteriaList.add(Criteria.where(field).is(value));
                        break;
                    case "ne":  // Not Equals
                        criteriaList.add(Criteria.where(field).ne(value));
                        break;
                    case "gt":  // Greater Than
                        criteriaList.add(Criteria.where(field).gt(value));
                        break;
                    case "lt":  // Less Than
                        criteriaList.add(Criteria.where(field).lt(value));
                        break;
                    case "gte": // Greater Than or Equal To
                        criteriaList.add(Criteria.where(field).gte(value));
                        break;
                    case "lte": // Less Than or Equal To
                        criteriaList.add(Criteria.where(field).lte(value));
                        break;
                    case "in":  // In List (multiple values separated by ";")
                        criteriaList.add(Criteria.where(field).in(value.split(";")));
                        break;
                    case "nin": // Not In List (multiple values separated by ";")
                        criteriaList.add(Criteria.where(field).nin(value.split(";")));
                        break;
                    case "regex": // Regular Expression (case-insensitive)
                        criteriaList.add(Criteria.where(field).regex(value, "i"));
                        break;
                    case "exists": // Field Exists (true/false)
                        criteriaList.add(Criteria.where(field).exists(Boolean.parseBoolean(value)));
                        break;
                    default:
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operator: " + operator);
                        // Add more operators as needed
                }
            }
        }
        return criteriaList;
    }

    private Sort parseSortOrders(String orders) {
        List<Sort.Order> sortOrders = new ArrayList<>();
        String[] orderConditions = orders.split(",");
        for (String orderCondition : orderConditions) {
            String[] parts = orderCondition.split("\\|");
            if (parts.length == 2) {
                String field = parts[0];
                Sort.Direction direction = "desc".equalsIgnoreCase(parts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC;
                sortOrders.add(new Sort.Order(direction, field));
            }
        }
        return Sort.by(sortOrders);
    }



}
