package com.masi.sale.service;

import com.masi.sale.domain.DeliveryDetail;
import com.masi.sale.domain.Order;
import com.masi.sale.domain.OrderReview;
import com.masi.sale.domain.enumeration.OrderReviewStatus;
import com.masi.sale.domain.enumeration.OrderStatus;
import com.masi.sale.repository.ContractMaterialRepository;
import com.masi.sale.repository.DeliveryDetailRepository;
import com.masi.sale.repository.OrderRepository;
import com.masi.sale.repository.OrderReviewRepository;
import com.masi.sale.service.dto.OrderReviewDTO;
import com.masi.sale.service.dto.RequestReviewDTO;
import com.masi.sale.service.mapper.OrderReviewMapper;
import com.masi.sale.web.rest.errors.BadRequestAlertException;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.sale.domain.OrderReview}.
 */
@Service
@Transactional
@AllArgsConstructor
public class OrderReviewService {

    private static final Logger log = LoggerFactory.getLogger(OrderReviewService.class);

    private final OrderReviewRepository orderReviewRepository;

    private final OrderReviewMapper orderReviewMapper;

    private static final int MAX_REVIEWERS = 8;

    private final OrderService orderService;

    private final OrderRepository orderRepository;
    private final DeliveryScheduleService deliveryScheduleService;

    private final DeliveryDetailRepository deliveryDetailRepository;
    private final ContractMaterialRepository contractMaterialRepository;

    public Mono<Void> requestReview(UUID orderId, RequestReviewDTO requestReviewDTO) {
        Collection<UUID> reviewers = requestReviewDTO.getEmployeeIds();
        if (reviewers.size() > MAX_REVIEWERS) {
            return Mono.error(new BadRequestAlertException("Too many reviewers", "orderReview", "tooManyReviewers"));
        }
        return orderService.findOne(orderId)
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Order not found", "orderReview", "orderNotFound")))
            .flatMap(orderDTO -> {
                orderDTO.setStatus(OrderStatus.WAITING_APPROVAL);
                return orderService.update(orderDTO);
            })
            .flatMap(orderDTO -> Flux.fromIterable(reviewers)
                .flatMap(reviewerId -> {
                    OrderReview orderReview = new OrderReview()
                        .id(UUID.randomUUID())
                        .lastUpdated(ZonedDateTime.now())
                        .createdDate(ZonedDateTime.now())
                        .employeeId(reviewerId)
                        .status(OrderReviewStatus.PENDING)
                        .orderId(orderId);
                    return orderReviewRepository.save(orderReview);
                }).then());

    }

    public Mono<OrderReviewDTO> review(OrderReviewDTO dto) {
        return orderReviewRepository.findById(dto.getId())
            .switchIfEmpty(Mono.error(new BadRequestAlertException("OrderReview not found", "orderReview", "orderReviewNotFound")))
            .<OrderReview>handle((orderReview, sink) -> {
                if (!OrderStatus.WAITING_APPROVAL.equals(orderReview.getOrder().getStatus())) {
                    sink.error(new BadRequestAlertException("Order is not waiting for approval", "orderReview",
                        "orderNotWaiting"));
                    return;
                }

                sink.next(dto.applyReview(orderReview).setIsPersisted());
            })
            .flatMap(orderReviewRepository::save)
            .flatMap(entity -> autoUpdateStatus(entity.getOrderId()).thenReturn(entity))
            .map(orderReviewMapper::toDto);
    }

    public Mono<Order> autoUpdateStatus(UUID orderId) {
        // ● Trạng thái của chứng từ hoặc báo cáo sẽ chuyển sang “Đã duyệt” hoặc “Từ chối” tùy thuộc vào lựa chọn của người xét duyệt.
        // ● Nếu có ít nhất 1 người không xét duyệt, trạng thái của chứng từ hoặc báo cáo sẽ vẫn là “Chờ duyệt”.
        // ● Nếu có ít nhất 1 người từ chối, trạng thái sẽ chuyển sang “Từ chối” và kết thúc tiến trình xét duyệt ngay lập tức.
        Mono<Order> orderDto = orderRepository.findByIdAndIsDeletedIsFalse(orderId);
        return orderDto.zipWith(orderReviewRepository.findByOrder(orderId).collectList())
                .flatMap(tuple -> {
                    Order order = tuple.getT1();
                    List<OrderReview> reviews = tuple.getT2();
                    if (reviews.stream().anyMatch(review -> OrderReviewStatus.REJECTED.equals(review.getStatus()))) {
                        order.setStatus(OrderStatus.REJECTED);
                    } else if (reviews.stream().anyMatch(review -> OrderReviewStatus.PENDING.equals(review.getStatus()))) {
                        order.setStatus(OrderStatus.WAITING_APPROVAL);
                    } else {
                        order.setStatus(OrderStatus.APPROVED);
//                        return deliveryScheduleService.createDeliveryOrder(orderId)
//                                .then(Mono.just(order.setIsPersisted()));
                        return contractMaterialRepository.findAllByOrderId(orderId)
                            .collectList()
                            .flatMap(contractMaterialFull -> {
                                log.info("contractMaterialFull: {}", contractMaterialFull);
                                var listDelivery = new ArrayList<DeliveryDetail>();
                                for (var contractMaterial : contractMaterialFull) {
                                    var deliveryDetail = new DeliveryDetail();
                                    deliveryDetail.setContractMaterialId(contractMaterial.getIdMaterial());
                                    deliveryDetail.setOrderId(orderId);
                                    deliveryDetail.setAddress(order.getDeliveryLocation());
                                    ZonedDateTime deliveryDate = ZonedDateTime.of(order.getDeliveryTermFrom(), LocalTime.of(0,0,0), ZoneId.of("UTC"));
                                    deliveryDetail.setDeliveryDate(deliveryDate);
                                    deliveryDetail.setContractId(contractMaterial.getIdContract());
                                    int orderQuantity = 0;
                                    try {
                                        orderQuantity = contractMaterial.getQuantity().intValue();
                                    }
                                    catch (Exception ignored) {
                                    }
                                    deliveryDetail.setQuantity(orderQuantity);
                                    listDelivery.add(deliveryDetail);
                                }
                                return deliveryDetailRepository.saveAll(listDelivery)
                                    .then(Mono.just(order.setIsPersisted()))
                                    .doOnError(e -> log.error("Error when create delivery order", e));
                            });
                    }
                    return Mono.just(order.setIsPersisted());
                })
                .flatMap(orderRepository::save)
                .then(orderDto);
    }


    public Mono<LocalDate> findLatestAwaitingDate(UUID orderId) {
        return orderReviewRepository.findLatestAwaitingDate(orderId);
    }

    public Flux<OrderReviewDTO> findByOrder(UUID orderId) {
        return orderReviewRepository.findByOrder(orderId).map(orderReviewMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Mono<OrderReviewDTO> findOne(UUID id) {
        log.debug("Request to get OrderReview : {}", id);
        return orderReviewRepository.findById(id).map(orderReviewMapper::toDto);
    }


    public Mono<Void> deleteByOrderId(UUID orderId) {
        return orderRepository.findByIdAndIsDeletedIsFalse(orderId)
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Order not found", "orderReview", "orderNotFound")))
            .flatMap(order -> {
                //Đã duyệt” hoặc “Từ chối”.
                if (OrderStatus.APPROVED.equals(order.getStatus()) || OrderStatus.REJECTED.equals(order.getStatus())) {
                    return Mono.error(new BadRequestAlertException("Order is already approved or rejected", "orderReview", "orderAlreadyApprovedOrRejected"));
                }
                order.setStatus(OrderStatus.CANCELLED);
                return orderRepository.save(order).then(orderReviewRepository.deleteByOrderId(orderId));
            }).then();
    }

    public Flux<OrderReviewDTO> findOrderReviewById(List<UUID> orderIds) {
        if (orderIds.isEmpty()) {
            return Flux.empty();
        }
        return orderReviewRepository.findOrderReviewByOrderIds(orderIds)
            .map(OrderReview::toDto); // Assuming you have a toDto() method for conversion
    }
}
