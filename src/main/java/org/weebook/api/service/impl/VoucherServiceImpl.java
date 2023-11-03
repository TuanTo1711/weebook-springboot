package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.weebook.api.dto.VoucherDTO;
import org.weebook.api.dto.mapper.NotificationMapper;
import org.weebook.api.dto.mapper.VoucherMapper;
import org.weebook.api.entity.Notification;
import org.weebook.api.entity.User;
import org.weebook.api.entity.Voucher;
import org.weebook.api.repository.OrderRepo;
import org.weebook.api.repository.UserRepo;
import org.weebook.api.repository.VoucherRepo;
import org.weebook.api.service.VoucherService;
import org.weebook.api.util.CriteriaUtility;
import org.weebook.api.web.request.FilterRequest;
import org.weebook.api.web.request.VoucherRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    final UserRepo userRepo;
    final VoucherRepo voucherRepo;
    final VoucherMapper voucherMapper;
    final NotificationMapper notificationMapper;
//    final OrderRepo orderRepo;
    final String messageDelete = "Voucher đã bị xóa : ";
    final String title = "Voucher";
    final String type = "voucher";
    @Override
    public VoucherDTO create(VoucherRequest voucherRequest) {
        Specification specification = getSpecificationOr(voucherRequest.getFilterRequest());
        List<User> users = userRepo.findAll(specification);
        Voucher voucher = voucherMapper.requestToEntity(voucherRequest);
        if(voucherRequest.getFilterRequest() == null){
            userAddNotifications(users, title, "Bạn có 1 voucher mới :" + voucher.getCode(), type);
            voucherRepo.save(voucher);
        }else {
            userAddNotificationsAndVoucher(users, voucher, title, "Bạn có 1 voucher mới :" + voucher.getCode(), type);
        }
        userRepo.saveAllAndFlush(users);
        return voucherMapper.entityToDto(voucher);
    }

    @Override
    public List<VoucherDTO> userGetAll(Long id) {
        List<Voucher> vouchers = voucherRepo
                    .userGetAll(User.builder().id(id).build());
        return voucherMapper.entityToDtos(vouchers);
    }

    @Override
    public List<VoucherDTO> adminGetAll(Integer page) {
        return voucherRepo.adminGetAll(PageRequest.of(page-1,5)).getContent();
    }

    @Transactional
    @Override
    public String delete(String code) {
        List<User> users = voucherRepo.findByVoucherCode(code);
        userAddNotifications(users,title,messageDelete+code,type);
        userRepo.saveAllAndFlush(users);
        voucherRepo.delete(code);
        return "Delete successfully";
    }

    @Override
    public VoucherDTO findByCode(String code) {
        return voucherRepo.findByCode(code);
    }

    public void userAddNotifications(List<User> users, String title, String message, String type) {
        if(users.size() == 0){
            return;
        }
        for(User user : users){
            Notification notification = notificationMapper.notification(title,message,type, user);
            user.getNotifications().add(notification);
        }
    }

    public void userAddNotificationsAndVoucher(List<User> users, Voucher voucher,String title, String message, String type) {
        if(users.size() == 0){
            return;
        }
        for(User user : users){
            Notification notification = notificationMapper.notification(title,message,type, user);
            user.getNotifications().add(notification);
            Voucher voucherNew = voucherMapper.entityToEntity(voucher, user);
            user.getVouchers().add(voucherNew);
        }
    }

    <T> Specification<T> getSpecificationOr(List<FilterRequest> filterRequests) {
        if (CollectionUtils.isEmpty(filterRequests)) {
            return Specification.where(null);
        }

        return filterRequests.stream()
                .<Specification<T>>map(CriteriaUtility::toSpecification)
                .reduce(Specification.where(null), Specification::or);
    }


}
