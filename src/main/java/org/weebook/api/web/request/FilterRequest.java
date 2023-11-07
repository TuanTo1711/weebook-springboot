package org.weebook.api.web.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterRequest {
    private String field;
    private String value;
    private String type;
}
