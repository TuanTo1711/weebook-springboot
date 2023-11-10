package org.weebook.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.weebook.api.entity.Notification;
import org.weebook.api.entity.Transaction;
import org.weebook.api.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaSpecificationExecutor<User>, JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String emailUser);

    void deleteByUsername(String username);


    @Query("""
        select t from Transaction t
        where t.user.id = :id
    """)
    Page<Transaction> getAllTransaction(Long id, Pageable pageable);

    @Query("""
        select n from Notification n
        where n.user.id = :id
    """)
    Page<Notification> getAllNotification(Long id, Pageable pageable);

    @Query("""
        select count(n.user) from Notification n
        where n.user.id = :id and n.isRead = false
        group by n.user
    """)
    Long getAllNotificationTotal(Long id);

    @Query("""
        select u from User u join u.orders o
        where o.status = 'cancel' and u.deletedDate != null
            and CAST(o.orderDate AS localdate) >= :dateMin
            and CAST(o.orderDate AS localdate) <= :dateMax
            and count(u) > :max
        group by u
        order by count(u) desc
    """)
    List<User> get강아지(LocalDate dateMin, LocalDate dateMax, Integer max, Pageable pageable);

}
