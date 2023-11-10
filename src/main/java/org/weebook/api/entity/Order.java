package org.weebook.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "\"order\"")
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 29380345405553554L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "order_date")
    private Instant orderDate;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "delivery_address", length = Integer.MAX_VALUE)
    private String deliveryAddress;

    @Column(name = "shipping_method", length = Integer.MAX_VALUE)
    private String shippingMethod;

    @Column(name = "delivery_date")
    private Instant deliveryDate;

    @Column(name = "delivery_status", length = Integer.MAX_VALUE)
    private String deliveryStatus;

    @Column(name = "created_date")
    @CreatedDate
    private Instant createdDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Payment> payments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "order")
    @ToString.Exclude
    private Set<Transaction> transactions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "order")
    @ToString.Exclude
    private Set<OrderFeedback> orderFeedbacks = new LinkedHashSet<>();
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<OrderItem> orderItems = new LinkedHashSet<>();
    @OneToMany(mappedBy = "order")
    @ToString.Exclude
    private Set<OrderStatus> orderStatuses = new LinkedHashSet<>();

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
        Order entity = (Order) o;
        return getId() != null && Objects.equals(getId(), entity.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}