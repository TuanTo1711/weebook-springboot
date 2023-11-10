package org.weebook.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.weebook.api.entity.Author;
import org.weebook.api.entity.Genre;
import org.weebook.api.entity.ProductsImage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link org.weebook.api.entity.Product}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {
    Long id;
    String name;
    BigDecimal price;
    Integer discount;
    String thumbnail;
    String productCode;
    String supplierName;
    Integer weight;
    String packageSize;
    String content;
    String publisher;
    String publishYear;
    String translator;
    String language;
    String chapter;
    Integer pageNumber;
    String brand;
    String origin;
    String madeIn;
    String color;
    String material;
    String formality;
    Integer quantity;
    Instant estimatedDate;
    String status;
    Instant createdDate;
    Instant updatedDate;
    Set<ProductsImage> images;
    List<Author> authors;
    List<Genre> genres;

    public void setValuesFromArray(String[] values) {
        this.id = Long.parseLong(removeQuotes(values[0]));
        this.name = removeQuotes(values[1]);
        this.price = new BigDecimal(removeQuotes(values[2]));
        this.discount = Integer.parseInt(removeQuotes(values[3]));
        this.thumbnail = removeQuotes(values[4]);
        this.productCode = removeQuotes(values[5]);
        this.supplierName = removeQuotes(values[6]);
        this.weight = Integer.parseInt(removeQuotes(values[7]));
        this.packageSize = removeQuotes(values[8]);
        this.content = removeQuotes(values[9]);
        this.publisher = removeQuotes(values[10]);
        this.publishYear = removeQuotes(values[11]);
        this.translator = removeQuotes(values[12]);
        this.language = removeQuotes(values[13]);
        this.chapter = removeQuotes(values[14]);
        this.pageNumber = Integer.parseInt(removeQuotes(values[15]));
        this.brand = removeQuotes(values[16]);
        this.origin = removeQuotes(values[17]);
        this.madeIn = removeQuotes(values[18]);
        this.color = removeQuotes(values[19]);
        this.material = removeQuotes(values[20]);
        this.formality = removeQuotes(values[21]);
        this.quantity = Integer.parseInt(removeQuotes(values[22]));
        this.status = removeQuotes(values[23]);
    }

    private String removeQuotes(String input) {
        if (input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1);
        }
        return input;
    }
}