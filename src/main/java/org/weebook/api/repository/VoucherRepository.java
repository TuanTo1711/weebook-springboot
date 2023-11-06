package org.weebook.api.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.weebook.api.entity.User;
import org.weebook.api.entity.Voucher;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    @Query("""
        select new Voucher (v.code,v.condition, v.discountAmount, v.validFrom, v.validTo, v.description)
        from Voucher v
        where v.validFrom <= current_timestamp and v.validTo >= current_timestamp
        and v.user = null or v.user = :user
        group by v.code,v.condition,v.discountAmount, v.validFrom, v.validTo, v.description
    """)
    List<Voucher> userGetAll(User user);



    @Query("""
        select new Voucher (v.code,v.condition, v.discountAmount, v.validFrom, v.validTo, v.description)
        from Voucher v
        where v.validTo >= current_timestamp
        group by v.code,v.condition,v.discountAmount, v.validFrom, v.validTo, v.description
    """)
    List<Voucher> adminGetAll(Pageable pageable);

    @Query("""
        select new Voucher (v.code,v.condition, v.discountAmount, v.validFrom, v.validTo, v.description)
        from Voucher v
        where v.code = :code
        group by v.code,v.condition,v.discountAmount, v.validFrom, v.validTo, v.description
    """)
    Voucher findByCode(String code);

    @Query("""
        select new Voucher(v.code,v.condition, v.discountAmount, v.validFrom, v.validTo, v.description)
        from Voucher v
        where v.code = :code and (v.user = :user or v.user = null)
        group by v.code,v.condition,v.discountAmount, v.validFrom, v.validTo, v.description
    """)
    List<Voucher> checkVoucherUse(User user, String code);


    List<Voucher> findByCodeEquals(String code);

    void deleteVoucherByCodeEquals(String code);


    @Query("""
        select u from User u join u.vouchers v
        where v.code = :code
    """)
    List<User> findByVoucherCode(String code);

}
