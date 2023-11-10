package org.weebook.api.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import org.springframework.data.domain.Range;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import org.weebook.api.web.request.FilterRequest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.data.domain.Sort.Direction.fromString;

/**
 * Utility class for building Specifications and Sort objects to facilitate querying and sorting data
 * in Spring Data JPA repositories.
 *
 * @author Tô Hoàng Tuấn - Yuuta
 */
public class CriteriaUtility {

    private CriteriaUtility() {
    }

    /**
     * Builds a Specification to filter records based on a field value matching a pattern.
     *
     * @param field The name of the field to filter on.
     * @param value The value to match against the field.
     * @return A Specification for filtering records based on the provided field and value.
     * @apiNote Example Usage:
     * <pre>
     * {@code
     *      var spec = buildFieldLike("name", "Yuuta");
     *      List<Entity> entities = entityRepository.findAll(spec);
     * }
     * </pre>
     *
     * <p>
     * This method generates a SQL query similar to:
     * <pre>{@code SELECT * FROM Entity WHERE name LIKE 'Yuuta'}</pre>
     * </p>
     */
    public static <T> Specification<T> buildFieldLike(String field, String value) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(field)) {
                return null;
            }
            Path<String> path = getPath(field, root);
            return criteriaBuilder
                    .like(criteriaBuilder.lower(path), value.toLowerCase());
        };
    }

    /**
     * Builds a Specification to filter records based on a field value matching a pattern.
     *
     * @param field The name of the field to filter on.
     * @param value The value to match against the field.
     * @return A Specification for filtering records based on the provided field and value.
     * @apiNote Example Usage:
     * <pre>
     * {@code
     *      var spec = buildFieldLikeAny("name", "Yuuta");
     *      List<Entity> entities = entityRepository.findAll(spec);
     * }
     * </pre>
     *
     * <p>
     * This method generates a SQL query similar to:
     * <pre>{@code SELECT * FROM Entity WHERE name LIKE '%Yuuta%'}</pre>
     * </p>
     */
    public static <T> Specification<T> buildFieldLikeAny(String field, String value) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(field)) {
                return null;
            }

            Path<String> path = getPath(field, root);
            return criteriaBuilder.like(criteriaBuilder.lower(path), "%" + value.toLowerCase() + "%");
        };
    }

    public static <T> Specification<T> buildFieldSearch(String field, String value) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(field)) {
                return null;
            }

            StringBuilder values = new StringBuilder();

            if (!StringUtils.containsWhitespace(value.trim()) || value.length() <= 1) {
                values.append(value);
            } else {
                values.append(StringUtils.replace(value, " ", "%"));
            }

            Path<String> path = getPath(field, root);
            return criteriaBuilder.like(criteriaBuilder.lower(path),
                    "%" + values.toString().toLowerCase() + "%");
        };
    }

    public static <T> Specification<T> buildFieldLikeLeading(String field, String value) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(field)) {
                return null;
            }

            Path<String> path = getPath(field, root);
            return criteriaBuilder.like(criteriaBuilder.lower(path), value.toLowerCase() + "%");
        };
    }

    public static <T> Specification<T> buildFieldGE(String field, Integer value) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(field)) {
                return null;
            }

            Path<Integer> path = getPath(field, root);
            return criteriaBuilder.greaterThanOrEqualTo(path, value);
        };
    }

    /**
     * Builds a Specification to filter records based on a field having an exact value.
     *
     * @param field The name of the field to filter on.
     * @param value The exact value to match against the field.
     * @return A Specification for filtering records based on the provided field and value.
     * @apiNote Example Usage:
     * <pre>{@code
     * Specification<Entity> spec = buildFieldEquals("age", "18");
     * List<Entity> entities = entityRepository.findAll(spec);
     * }</pre>
     *
     * <p>
     * This method generates a SQL query similar to:
     * <pre>{@code SELECT * FROM Entity WHERE age = '18'}</pre>
     * </p>
     */
    public static <T> Specification<T> buildFieldEquals(String field, Object value) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(field)) {
                return null;
            }
            Path<?> path = getPath(field, root);

            return criteriaBuilder.equal(path, value);
        };
    }

    public static <T> Specification<T> buildFieldRange(String field, Range<BigDecimal> range) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(field)) {
                return null;
            }

            Path<BigDecimal> path = getPath(field, root);
            return criteriaBuilder.between(path,
                    range.getLowerBound().getValue().get(),
                    range.getUpperBound().getValue().get());
        };
    }

    /**
     * Builds a Specification to select specific fields from the records.
     *
     * @param fields The names of the fields to select.
     * @return A Specification for selecting specific fields from the records.
     * @apiNote <strong>Example Usage:</strong>
     * <pre>{@code
     * Specification<Entity> spec = getAnyField("id", "name");
     * List<Entity> projections = entityRepository.findAll(spec); //findOne
     * }</pre>
     *
     * <p>
     * This method generates a SQL query similar to:
     * <pre>{@code SELECT id, name FROM Entity}</pre>
     * </p>
     *
     * <strong>Advance Usage:</strong>
     * <pre>{@code
     * Specification<Entity> spec spec = getAnyField("id", "name")
     *              .and(buildFieldLike("name", "Yuuta"));
     * Entity projections = entityRepository.findOne(spec);
     * }</pre>
     * <p>
     * This method generates a SQL query similar to:
     * <pre>{@code SELECT id, name FROM Entity WHERE name = 'Yuuta'}</pre>
     * </p>
     */
    public static <T> Specification<T> getAnyField(String... fields) {
        return (root, query, criteriaBuilder) -> {
            Stream<String> fieldStream = Arrays.stream(fields);
            query.multiselect(fieldStream.map(root::get).toArray(Expression[]::new));
            return null;
        };
    }

    /**
     * Builds a Specification to select the one field from the records.
     *
     * @param field The name of the field to select as the first field.
     * @return A Specification for selecting the first field from the records.
     * @apiNote Example Usage:
     * <pre>{@code
     * Specification<Entity> spec = getField("name");
     * //List<String> names = entityRepository.findAll(spec, String.class);
     *  List<Entity> names = entityRepository.findAll(spec);
     * }</pre>
     *
     * <p>
     * This method generates a SQL query similar to:
     * <pre>{@code SELECT name FROM Entity}</pre>
     * </p>
     */
    public static <T> Specification<T> getField(String field) {
        return (root, query, criteriaBuilder) -> {
            query.select(root.get(field));
            return null;
        };
    }

    /**
     * Builds a Sort object for sorting records based on a specific field and sort type.
     *
     * @param field    The name of the field to sort on.
     * @param sortType The sort type, either "asc" for ascending or "desc" for descending.
     * @return A Sort object for sorting records based on the provided field and sort type.
     * @throws IllegalArgumentException If the sortType is not valid ("asc" or "desc").
     * @apiNote Example Usage:
     * <pre>{@code
     * Specification<Entity> spec = getField("name");
     * Sort sort = buildSort("name", "asc");
     * List<Entity> sortedEntities = entityRepository.findAll(spec, sort);
     * }</pre>
     *
     * <p>
     * This method generates a SQL query with an {@code ORDER BY} clause based on the provided field and sort type.
     * </p>
     */
    public static Sort buildSort(String field, String sortType) {
        if (!StringUtils.hasText(field)) {
            return Sort.unsorted();
        }

        if (StringUtils.hasText(sortType)) {
            var direction = fromString(sortType);
            return Sort.by(direction, field);
        }

        return Sort.by(field);
    }

    public static <T> Specification<T> buildFieldIn(String field, List<Object> values) {
        return (root, query, criteriaBuilder) -> {
            if (values == null || values.isEmpty()) {
                return null;
            }
            Path<?> path = getPath(field, root);
            CriteriaBuilder.In<Object> inClause = criteriaBuilder.in(path);
            values.forEach(inClause::value);
            return inClause;
        };
    }


    public static <T> Specification<T> toSpecification(FilterRequest filterRequest) {
        String type = filterRequest.getType();

        CriteriaType criteriaType = CriteriaType.valueOf(type.toUpperCase());

        if (criteriaType == CriteriaType.UID) {
            filterRequest.setValue(UUID.fromString(filterRequest.getValue()).toString());
        }

        switch (criteriaType) {
            case UID -> {
                return buildFieldUUID(filterRequest.getField(), filterRequest.getValue());
            }
            case LIKE -> {
                return buildFieldLike(filterRequest.getField(), filterRequest.getValue());
            }
            case LIKE_ANY -> {
                return buildFieldLikeAny(filterRequest.getField(), filterRequest.getValue());
            }
            case EQ -> {
                return buildFieldEquals(filterRequest.getField(), filterRequest.getValue());
            }
            case BETWEEN -> {
                return buildFieldRange(filterRequest.getField(), getRange(filterRequest.getValue()));
            }
            case GE -> {
                return buildFieldGE(filterRequest.getField(), NumberUtils.parseNumber(filterRequest.getValue(), Integer.class));
            }
            case IN -> {
                return buildFieldIn(filterRequest.getField(), filterRequest.getValues());
            }
            default -> throw new IllegalArgumentException("Criteria Type: '%s' is not supported".formatted(type));
        }
    }

    private static <T> Specification<T> buildFieldUUID(String field, String value) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(field)) {
                return null;
            }
            UUID uuid = UUID.fromString(value);

            Path<UUID> path = getPath(field, root);
            return criteriaBuilder.equal(path, uuid);
        };
    }


    private static <T, R> Path<R> getPath(String fieldName, Path<T> path) {
        if (fieldName.contains(".")) {
            int indexOf = fieldName.indexOf(".") + 1;
            String[] tokenize = StringUtils.tokenizeToStringArray(fieldName, ".");
            Path<R> objectPath = path.get(tokenize[0]);
            return getPath(fieldName.substring(indexOf), objectPath);
        }

        return path.get(fieldName);
    }

    public static <T> Specification<T> getSpecification(List<FilterRequest> filterRequests) {
        if (CollectionUtils.isEmpty(filterRequests)) {
            return Specification.where(null);
        }

        return filterRequests.stream()
                .<Specification<T>>map(CriteriaUtility::toSpecification)
                .reduce(Specification.where(null), Specification::and);
    }

    public static Range<BigDecimal> getRange(String value, String prefix) {
        if (!StringUtils.hasText(value)) {
            return Range.unbounded();
        }

        String[] tokenize = StringUtils.tokenizeToStringArray(value, prefix);

        BigDecimal min = NumberUtils.parseNumber(tokenize[0], BigDecimal.class);
        BigDecimal max = NumberUtils.parseNumber(tokenize[1], BigDecimal.class);

        return Range.open(min, max);
    }

    public static Range<BigDecimal> getRange(String value) {
        return getRange(value, "_|-");
    }

    public enum CriteriaType {
        EQ, // Equal
        LT, // Less Than
        GT, // Greater Than
        LE, // Less Than or Equal
        GE, // Greater Than or Equal
        LIKE, // Like (for text search)
        LIKE_ANY,
        UID, BETWEEN,
        IN


    }
}