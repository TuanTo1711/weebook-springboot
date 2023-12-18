package org.weebook.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.weebook.api.dto.VoucherDTO;
import org.weebook.api.entity.User;
import org.weebook.api.entity.Voucher;
import org.weebook.api.web.request.VoucherRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring",
        imports = {UUID.class, Instant.class, List.class, ArrayList.class})
public interface VoucherMapper {
    @Mapping(target = "validFrom", source = "validFrom", qualifiedByName = "localdate")
    @Mapping(target = "validTo", source = "validTo", qualifiedByName = "localdate")
//    @Mapping(target = "code", expression = "java(UUID.randomUUID().toString())")
    Voucher requestToEntity(VoucherRequest voucherRequest);

    VoucherDTO entityToDto(Voucher voucher);

    List<VoucherDTO> entityToDtos(List<Voucher> vouchers);


    //xóa tham chiếu chạy bẳng cơm
    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", ignore = true)
    Voucher entityToEntity(Voucher voucherOld, User user);

    Voucher dtoToEntity(VoucherDTO voucherDTO);

    @Named("localdate")
    default Instant localdate(LocalDateTime localDateTime) {
        if(localDateTime == null){
            localDateTime = LocalDateTime.now();
        }
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toInstant();
    }
}
