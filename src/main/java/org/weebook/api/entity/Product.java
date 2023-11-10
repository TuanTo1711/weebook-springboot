package org.weebook.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1867021887085033211L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "price", nullable = false, precision = 10)
    private BigDecimal price;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "thumbnail", nullable = false, length = Integer.MAX_VALUE)
    private String thumbnail;

    @Column(name = "product_code", length = Integer.MAX_VALUE)
    private String productCode;

    @Column(name = "supplier_name", length = Integer.MAX_VALUE)
    private String supplierName;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "package_size", length = Integer.MAX_VALUE)
    private String packageSize;

    @Column(name = "content", length = Integer.MAX_VALUE)
    private String content;

    @Column(name = "publisher", length = Integer.MAX_VALUE)
    private String publisher;

    @Column(name = "publish_year", length = Integer.MAX_VALUE)
    private String publishYear;

    @Column(name = "translator", length = Integer.MAX_VALUE)
    private String translator;

    @Column(name = "language", length = Integer.MAX_VALUE)
    private String language;

    @Column(name = "chapter")
    private String chapter;

    @Column(name = "page_number")
    private Integer pageNumber;

    @Column(name = "brand", length = Integer.MAX_VALUE)
    private String brand;

    @Column(name = "origin", length = Integer.MAX_VALUE)
    private String origin;

    @Column(name = "made_in", length = Integer.MAX_VALUE)
    private String madeIn;

    @Column(name = "color", length = Integer.MAX_VALUE)
    private String color;

    @Column(name = "material", length = Integer.MAX_VALUE)
    private String material;

    @Column(name = "formality", length = Integer.MAX_VALUE)
    private String formality;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "estimated_date")
    private Instant estimatedDate;

    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private Instant updatedDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @ToString.Exclude

    private List<Author> authors = new LinkedList<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "product_genres",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "genres_id"))
    @ToString.Exclude
    private List<Genre> genres = new LinkedList<>();


    @OneToMany(mappedBy = "products", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<ProductsImage> images = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    @ToString.Exclude

    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    @ToString.Exclude

    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    @ToString.Exclude

    private Set<Favorite> favorites = new LinkedHashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Product entity = (Product) o;
        return getId() != null && Objects.equals(getId(), entity.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}