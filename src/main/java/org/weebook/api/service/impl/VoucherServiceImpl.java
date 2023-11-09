package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.weebook.api.dto.VoucherDTO;
import org.weebook.api.dto.mapper.NotificationMapper;
import org.weebook.api.dto.mapper.VoucherMapper;
import org.weebook.api.entity.Notification;
import org.weebook.api.entity.User;
import org.weebook.api.entity.Voucher;
import org.weebook.api.exception.StringException;
import org.weebook.api.repository.NotificationRepository;
import org.weebook.api.repository.UserRepository;
import org.weebook.api.repository.VoucherRepository;
import org.weebook.api.service.VoucherService;
import org.weebook.api.util.CriteriaUtil;
import org.weebook.api.web.request.AddVoucherVaoUserRequest;
import org.weebook.api.web.request.FilterRequest;
import org.weebook.api.web.request.PagingRequest;
import org.weebook.api.web.request.VoucherRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    final UserRepository userRepository;
    final VoucherRepository voucherRepository;
    final VoucherMapper voucherMapper;
    final NotificationMapper notificationMapper;
    final NotificationRepository notificationRepository;
    final UserDetailsService userDetailsService;
    private final SecurityContextHolderStrategy securityContextHolder
            = SecurityContextHolder.getContextHolderStrategy();
    //    final OrderRepo orderRepo;
    final String MESSAGE_DELETE = "Voucher đã bị xóa : ";
    final String TITLE = "Voucher";
    final String TYPE = "voucher";

    @Override
    public VoucherDTO create(VoucherRequest voucherRequest) {
        Specification<User> specification = getSpecificationOr(voucherRequest.getFilterRequest());
        List<User> users = userRepository.findAll(specification);
        Voucher voucher = voucherMapper.requestToEntity(voucherRequest);
        if (voucherRequest.getFilterRequest() == null) {
            userAddNotifications(users, TITLE, "Bạn có 1 voucher mới :" + voucher.getCode(), TYPE);
            voucherRepository.save(voucher);
        } else {
            userAddNotificationsAndVoucher(users, voucher, TITLE, "Bạn có 1 voucher mới :" + voucher.getCode(), TYPE);
        }
        return voucherMapper.entityToDto(voucher);
    }

    @Transactional
    @Override
    public VoucherDTO create(AddVoucherVaoUserRequest addVoucherVaoUserRequest) {
        List<Voucher> vouchers = voucherRepository
                .findByCodeEquals(addVoucherVaoUserRequest
                        .getVoucherDTO().getCode());
        if (vouchers.isEmpty()) {
            return null;
        }

        Voucher checkVoucher = vouchers.get(0);
        if (checkVoucher.getUser() == null) {
            List<User> users = userRepository.findAll();
            String message = "Xin loi vì voucher : " + addVoucherVaoUserRequest.getVoucherDTO().getCode() + " chỉ dành cho những khách đặc biệt";
            userAddNotifications(users, "Voucher", message, "voucher");
            voucherRepository.deleteVoucherByCodeEquals(addVoucherVaoUserRequest
                    .getVoucherDTO().getCode());
        }

        Specification<User> specification = getSpecificationOr(addVoucherVaoUserRequest.getFilterRequest());
        List<User> users = userRepository.findAll(specification);

        if (addVoucherVaoUserRequest.getFilterRequest() == null) {
            throw new StringException("Lần trước nhậu quên nhập giờ thì nhớ nha.");
        }

        Voucher voucher = voucherMapper.dtoToEntity(addVoucherVaoUserRequest.getVoucherDTO());

        userAddNotificationsAndVoucher(users, voucher, TITLE, "Bạn có 1 voucher mới :" + voucher.getCode(), TYPE);

        return addVoucherVaoUserRequest.getVoucherDTO();
    }

    @Override
    public List<VoucherDTO> userGetAll() {
        Authentication currentUser = this.securityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(currentUser)) {
            throw new AccessDeniedException("Can't order as no Authentication object found in context for current user.");
        }

        String userName = currentUser.getName();
        User user = (User) userDetailsService.loadUserByUsername(userName);
        System.out.println(user.getId());
        List<Voucher> vouchers = voucherRepository.userGetAll(user.getId());
        return voucherMapper.entityToDtos(vouchers);
    }

    @Override
    public List<VoucherDTO> adminGetAll(PagingRequest pagingRequest) {
        List<Voucher> vouchers = voucherRepository
                .adminGetAll(PageRequest.of(pagingRequest.getPageNumber() - 1, pagingRequest.getPageSize()));
        return voucherMapper.entityToDtos(vouchers);
    }

    @Transactional
    @Override
    public String delete(String code) {
        List<User> users = voucherRepository.findByVoucherCode(code);
        userAddNotifications(users, TITLE, MESSAGE_DELETE + code, TYPE);
        userRepository.saveAllAndFlush(users);
        voucherRepository.deleteVoucherByCodeEquals(code);
        return "Delete successfully";
    }

    @Override
    public VoucherDTO findByCode(String code) {
        Voucher voucher = voucherRepository.findByCode(code);
        return voucherMapper.entityToDto(voucher);
    }

    public void userAddNotifications(List<User> users, String title, String message, String type) {
        if (users.size() == 0) {
            return;
        }
        for (User user : users) {
            Notification notification = notificationMapper.notification(title, message, type, user);
            notification = notificationRepository.save(notification);
            user.getNotifications().add(notification);
        }
    }

    public void userAddNotificationsAndVoucher(List<User> users, Voucher voucher, String title, String message, String type) {
        if (users.isEmpty()) {
            return;
        }
        for (User user : users) {
            Notification notification = notificationMapper.notification(title, message, type, user);
            notification = notificationRepository.save(notification);
            user.getNotifications().add(notification);

            Voucher voucherNew = voucherMapper.entityToEntity(voucher, user);
            voucherNew = voucherRepository.save(voucherNew);
            user.getVouchers().add(voucherNew);
        }
    }

    <T> Specification<T> getSpecificationOr(List<FilterRequest> filterRequests) {
        if (CollectionUtils.isEmpty(filterRequests)) {
            return Specification.where(null);
        }

        return filterRequests.stream()
                .<Specification<T>>map(CriteriaUtil::toSpecification)
                .reduce(Specification.where(null), Specification::or);
    }


}
