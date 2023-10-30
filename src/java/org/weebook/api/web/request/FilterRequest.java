package org.weebook.api.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {
    private String field;
    private String value;
    private String type;
}
