import Descriptions from '@uiw/react-descriptions';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useFactoryLogistics from 'app/hooks/use-factory-logistics';
import { COMPANY } from 'app/shared/model/enumerations/company.model';
import React from 'react';
import factoriesMapping from '../factories-mapping';

const { useGetFactoryByIdQuery } = useFactoryLogistics;
const { mapFactoriesActiveText } = factoriesMapping;
interface IFactoriesDetailsModalProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

const FactoriesDetailsModal = (props: IFactoriesDetailsModalProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useGetFactoryByIdQuery(selectedRecord);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      footer={null}
      ok={false}
      cancel={false}
      className="factories-detail-modal"
      titleHeader="Chi tiết nhà máy"
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Mã nhà máy">
            <Typography level="text" className="fw-bolder">
              {data?.code}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Tên nhà máy">
            <Typography level="text" className="fw-bolder">
              {data?.name}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Địa chỉ" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.address}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Vị trí">
            <Typography level="text" className="fw-bolder">
              {[
                data?.attribute?.location?.x ?? 0,
                data?.attribute?.location?.y ?? 0,
              ].join(',')}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Trạng thái">
            <Typography level="text" className="fw-bolder">
              {mapFactoriesActiveText(data?.isActive ?? false)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Trực thuộc">
            <Typography level="text" className="fw-bolder">
              {data?.employeeOwner?.fullName ?? ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Người quản lý">
            <Typography level="text" className="fw-bolder">
              {data?.company === (COMPANY.KIM_LONG as string)
                ? 'Kim Long'
                : 'MMS'}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ghi chú" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.note}
            </Typography>
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default FactoriesDetailsModal;
