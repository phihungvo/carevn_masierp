import dayjs from 'dayjs';
import React, { useEffect } from 'react';
import { useLocation } from 'react-router';
import Descriptions from '@uiw/react-descriptions';

import Flex from 'app/components/flex/flex';
import useOrders from 'app/hooks/use-orders';
import Modal from 'app/components/modal/modal';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import { Typography } from 'app/components/typography/typography';
import { ORDER_STATUS } from 'app/shared/model/enumerations/order.model';

const { useGetOrderByIdQuery } = useOrders;

interface ILogisticsMaterialProposalDetailModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord?: string | null;
}

const LogisticsMaterialProposalDetailModals = (props: ILogisticsMaterialProposalDetailModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { search } = useLocation();
  const idQuery = search?.split('=')[1];

  const { data } = useGetOrderByIdQuery(selectedRecord || idQuery);

  useEffect(() => {
    if (idQuery) {
      toggle();
    }
  }, [idQuery]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      ok={false}
      className="logistics-material-proposal-detail-modals"
    >
      <Typography level={4}>Chi tiết đề xuất vật tư</Typography>

      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Số CT">
            <Typography level="text" className="fw-bolder">
              CT-001
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Ngày CT">
            <Typography level="text" className="fw-bolder">
              28/02/2024
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Nhân viên">
            <Typography level="text" className="fw-bolder">
              Hà Hoàng Quân
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Bộ phận">
            <Typography level="text" className="fw-bolder">
              Developer
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Tên hàng hóa" >
            <Typography level="text" className="fw-bolder">
              Goat
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Nhà cung cấp" >
            <Typography level="text" className="fw-bolder">
              Quinn
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Đơn vị" >
            <Typography level="text" className="fw-bolder">
              Kg
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Số lượng" >
            <Typography level="text" className="fw-bolder">
              2
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Đơn giá" >
            <Typography level="text" className="fw-bolder">
              2.222.222,222
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        <Descriptions layout='vertical' size="large" bordered column={2}>
          <Descriptions.Item label="ghi chú" >
            <Typography level="text" className="fw-bolder">
              Lorem ipsum, dolor sit amet consectetur adipisicing elit. Consequuntur iure amet et! Consequuntur praesentium adipisci sit tempore non animi et blanditiis quisquam. Natus, maxime. Voluptatem nemo officia minima ullam iste?
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />

        <Descriptions title="Thông tin xét duyệt" size="large" bordered column={1} layout="vertical">
          <Descriptions.Item label="Chữ ký">
            <Flex flexWrap='wrap' gap={16}>
              {
                Array.from({ length: 6 }).map(() => <AttachmentPreview name={'213'} fileUrl={`${FILE_UTIL}/${123}`} />)
              }
            </Flex>
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default LogisticsMaterialProposalDetailModals;
