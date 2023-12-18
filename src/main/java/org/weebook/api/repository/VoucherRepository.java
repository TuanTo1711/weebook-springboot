package org.weebook.api.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.weebook.api.entity.User;
import org.weebook.api.entity.Voucher;
import org.weebook.api.web.request.VoucherRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    @Query("""
                select new Voucher (v.code,v.condition, v.discountAmount, v.validFrom, v.validTo, v.description, v.type)
                from Voucher v
                where v.validFrom <= current_timestamp and v.validTo >= current_timestamp
                and v.user.id = null or v.user.id = :id
                group by v.code,v.condition,v.discountAmount, v.validFrom, v.validTo, v.description, v.type
            """)
    List<Voucher> userGetAll(Long id);


    @Query("""
                select new Voucher (v.code,v.condition, v.discountAmount, v.validFrom, v.validTo, v.description, v.type)
                from Voucher v
                where v.validTo >= current_timestamp
                group by v.code,v.condition,v.discountAmount, v.validFrom, v.validTo, v.description, v.type
            """)
    List<Voucher> adminGetAll(Pageable pageable);

    @Query("""
                select new Voucher (v.code,v.condition, v.discountAmount, v.validFrom, v.validTo, v.description, v.type)
                from Voucher v
                where v.code = :code
                group by v.code,v.condition,v.discountAmount, v.validFrom, v.validTo, v.description, v.type
            """)
    Voucher findByCode(String code);

    @Query("""
                select new Voucher(v.code,v.condition, v.discountAmount, v.validFrom, v.validTo, v.description, v.type)
                from Voucher v
                where v.code = :code and (v.user = :user or v.user = null)
                group by v.code,v.condition,v.discountAmount, v.validFrom, v.validTo, v.description, v.type
            """)
    Optional<Voucher> checkVoucherUse(User user, String code);


    List<Voucher> findByCodeEquals(String code);

    void deleteVoucherByCodeEquals(String code);

    @Modifying
    @Query("""
        delete from Voucher v where v.code = :code and v.user = null
""")
    void deleteVoucher(String code);


    @Query("""
                select u from User u join u.vouchers v
                where v.code = :code
            """)
    List<User> findByVoucherCode(String code);

    @Modifying
    @Query("""
        update Voucher v
        set v.type = :type, v.description = :description, v.validTo = :validTo, v.validFrom = :validFrom,
        v.discountAmount = :discountAmount, v.condition = :condition
        where v.code = :code
""")
    void updateVoucher(String type, String description, Instant validTo, Instant validFrom, BigDecimal discountAmount, BigDecimal condition, String code);

}
