import Descriptions from '@uiw/react-descriptions';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import SignTable from 'app/components/sign-table/sign-table';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT } from 'app/constants/common';
import useOrders from 'app/hooks/use-orders';
import { ORDER_STATUS } from 'app/shared/model/enumerations/order.model';
import dayjs from 'dayjs';
import React, { useEffect } from 'react';
import { useLocation } from 'react-router';
import QualityIndexesTable from '../components/quality-indexes-table';
import ContractsList from '../contracts-list';

const { useGetOrderByIdQuery } = useOrders;

interface IOrdersDetailModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord?: string | null;
}

const OrdersDetailModals = (props: IOrdersDetailModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { search } = useLocation();
  const idQuery = search?.split('=')[1];

  const { data } = useGetOrderByIdQuery(selectedRecord || idQuery);

  useEffect(() => {
    if (idQuery) {
      toggle();
    }
  }, [idQuery]);
  const listReviews = data?.orderReviews?.filter(element => element.status === ORDER_STATUS.APPROVED) || []
  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      ok={false}
      className="modal-default"
      fullscreen
      titleHeader='Chi tiết đơn đặt hàng'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Mã số">
            <Typography level="text" className="fw-bolder">
              {data?.orderCode}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Ngày tạo">
            <Typography level="text" className="fw-bolder">
              {data?.dateOrder ? dayjs(data?.dateOrder).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Quy cách đóng gói">
            <Typography level="text" className="fw-bolder">
              {data?.packageType}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Hợp đồng">
            <Typography level="text" className="fw-bolder">
              {data?.contract?.contractName}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Địa điểm nhận hàng">
            <Typography level="text" className="fw-bolder">
              {data?.deliveryLocation}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Thời hạn giao hàng">
            <Typography level="text" className="fw-bolder">
              {data?.deliveryTermFrom ? dayjs(data?.deliveryTermFrom).format(DATE_FORMAT.DATE) : ''}{' '}
              {data?.deliveryTermFrom && data?.deliveryTermTo ? ' - ' : ''}
              {data?.deliveryTermTo ? dayjs(data?.deliveryTermTo).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Thời hạn thanh toán">
            <Typography level="text" className="fw-bolder">
              {data?.payTerm}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Điều kiện thanh toán">
            <Typography level="text" className="fw-bolder">
              {data?.payCondition}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Ngày hoàn thành" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.finishDate ? dayjs(data?.finishDate).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Ghi chú" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.note}
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        {data?.contractId && (
          <>
            <div className="divider" />
            <Descriptions title="Danh sách thành phẩm" size="large" bordered column={1} layout="vertical">
              <Descriptions.Item label="Chi tiết">
                <ContractsList list={data?.contract?.contractMaterialDTOS?.filter(item => item?.orderId === selectedRecord || idQuery)} />
              </Descriptions.Item>
            </Descriptions>
          </>
        )}

        {data?.qualityIndexes && (
          <>
            <div className="divider" />
            <Descriptions title="Chỉ tiêu chất lượng/ an toàn" size="large" bordered column={1} layout="vertical">
              <Descriptions.Item label="Chi tiết">
                <QualityIndexesTable list={data?.qualityIndexes} />
              </Descriptions.Item>
            </Descriptions>
          </>
        )}

        <div className="divider" />
        <Descriptions title="Thông tin xét duyệt" size="large" bordered column={1} layout="vertical">
          <Descriptions.Item label="Chữ ký">
            {
              <SignTable data={listReviews.map(e => (
                {
                  employeeId: e.employeeId,
                  signId: e.approvalSignFile?.id,
                  signName: e.approvalSignFile?.name,
                  signAt: e.lastUpdated
                })
              )} />
            }
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default OrdersDetailModals;
