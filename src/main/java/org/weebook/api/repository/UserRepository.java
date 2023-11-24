package org.weebook.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
                select n from Notification n left join n.users u
                where n.user.id = :id or (n.user.id = null and (u.id = null or u.id != :id))
                group by n
            """)
    Page<Notification> getAllNotification(Long id, Pageable pageable);

    @Query("""
                select count(n.user) from Notification n
                where n.user.id = :id and n.isRead = false
                group by n.user
            """)
    Long getAllNotificationTotal(Long id);

    @Query("""
        select u from User u
        where u in :userSepec and u not in :userVoucher
    """)
    List<User> getUser(List<User> userSepec, List<User> userVoucher);



    //Dừng có xóa cái này có j sau này t coi lại sao lúc đó t lại code cái này
//    @Query("""
//    SELECT u FROM User u
//               WHERE u.deletedDate IS NULL AND
//               (
//                   (SELECT COALESCE(COUNT(o.user), 0) FROM Order o
//                   WHERE o.status = 'bom'
//                   AND CAST(o.orderDate AS localdate) BETWEEN :dateMin AND :dateMax
//                   AND o.user = u
//                   GROUP BY o.user) >
//                   ((SELECT COALESCE(COUNT(o.user), 0) FROM Order o
//                   WHERE o.status = 'success'
//                   AND CAST(o.orderDate AS localdate) BETWEEN :dateMin AND :dateMax
//                   AND o.user = u
//                   GROUP BY o.user) + :maxBom)
//               )
//""")
//    List<User> get강아지(LocalDate dateMin, LocalDate dateMax, Integer maxBom, Pageable pageable);

    @Query("""
        SELECT u FROM User u join u.orders o
        WHERE u.deletedDate IS NULL
            AND CAST(o.orderDate AS localdate) BETWEEN :dateMin AND :dateMax
        group by u
        HAVING SUM(CASE WHEN o.status = 'bom' THEN 1 ELSE 0 END) >
        (SUM(CASE WHEN o.status = 'success' THEN 1 ELSE 0 END) + :maxBom)
        
""")
    List<User> get강아지(LocalDate dateMin, LocalDate dateMax, Integer maxBom, Pageable pageable);

    List<User> findByRole_NameIn(List<String> role);
}
