import React from 'react';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import Descriptions from '@uiw/react-descriptions';
import Table from 'app/components/table/table';
import useUom from 'app/hooks/use-uom';

const { useGetUomGroupById } = useUom;

interface IUomGroupDetailModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord?: string | null;
}

const UomGroupDetailModals = (props: IUomGroupDetailModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useGetUomGroupById(selectedRecord);

  return (
    <Modal isOpen={isOpen} toggle={toggle} cancel={false} ok={false} className="modals-detail-uom-group">
      <Typography level={4}>Chi tiết nhóm đơn vị</Typography>

      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Tên nhóm đơn vị" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.name}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Đơn vị cơ bản">
            <Typography level="text" className="fw-bolder">
              {data?.baseUom?.name}
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />
        <Descriptions title="Danh sách đồng phục" size="large" bordered column={1} layout="vertical">
          <Descriptions.Item label="Chi tiết">
            <Table
              rowKey="id"
              columns={[
                {
                  title: 'Tên',
                  dataIndex: 'name',
                  render: text => text,
                },
                { title: 'Đơn vị', dataIndex: 'baseUomId', render: (_, record) => record?.baseUom?.name },
                { title: 'Số lượng', dataIndex: 'baseQty', render: text => formatDecimalPrecision(text) },
                { title: 'Đơn vị quy đổi', dataIndex: 'altUomId', render: (_, record) => record?.altUom?.name },
                { title: 'Số lượng quy đổi', dataIndex: 'altQty', render: text => formatDecimalPrecision(text) },
              ]}
              dataSource={data?.uomGroupDetailsDTOs}
            />
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default UomGroupDetailModals;
