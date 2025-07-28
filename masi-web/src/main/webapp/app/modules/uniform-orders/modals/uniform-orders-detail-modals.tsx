import Descriptions from '@uiw/react-descriptions';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import Table from 'app/components/table/table';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import useUniform from 'app/hooks/use-uniform';
import { IUniformOrderProcesses } from 'app/shared/model/uniform.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import dayjs from 'dayjs';
import React, { useEffect } from 'react';
import { useLocation } from 'react-router';

const { useUniformOrderById } = useUniform;

interface IUniformOrdersDetailModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord?: string | null;
}

const UniformOrdersDetailModals = (props: IUniformOrdersDetailModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { search } = useLocation();
  const idQuery = search?.split('=')[1];

  const { data } = useUniformOrderById(selectedRecord);

  useEffect(() => {
    if (idQuery) {
      toggle();
    }
  }, [idQuery]);
  const uniform = data?.uniformFormDetails?.map(item => item?.uniform?.basePrice);
  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      ok={false}
      className="modals-detail-uniform-orders"
      titleHeader='Chi tiết đơn hàng'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Tên đơn hàng">
            <Typography level="text" className="fw-bolder">
              {data?.name}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ngày">
            <Typography level="text" className="fw-bolder">
              {data?.date ? dayjs(data?.date).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Nhà cung cấp">
            <Typography level="text" className="fw-bolder">
              {data?.supplierName}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Mã phiếu nhập kho">
            <Typography level="text" className="fw-bolder">
              {data?.code}
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />
        <Descriptions title="Danh sách đồng phục" size="large" bordered column={1} layout="vertical">
          <Descriptions.Item label="Chi tiết">
            <Table
              rowKey="id"
              columns={[
                { title: 'Đồng phục', dataIndex: 'name', render: (_, record) => record?.uniform?.name },
                { title: 'Số lượng', dataIndex: 'quantity', render: text => formatDecimalPrecision(text) },
                { title: 'Đơn vị', dataIndex: 'unit', render: (_, record) => record?.uomName },
                { title: 'Giá cơ bản', dataIndex: 'basePrice', render: (_, record) => formatDecimalPrecision(record?.uniform?.basePrice) },
                { title: 'Giá mua', dataIndex: 'actualPrice', render: text => formatDecimalPrecision(text) },
              ]}
              dataSource={data?.uniformFormDetails}
            />
          </Descriptions.Item>
        </Descriptions>

        {data?.uniformOrderStockDTOS?.length > 0 && (
          <>
            <div className="divider" />
            <Descriptions title="Lịch sử nhập kho" size="large" bordered column={1} layout="vertical">
              {data?.uniformOrderStockDTOS?.map(item => (
                <Descriptions.Item
                  key={item?.id}
                  label={
                    <>
                      Mã phiếu nhập kho: <b>{item?.code}</b>
                    </>
                  }
                >
                  <Table
                    rowKey="id"
                    columns={[
                      { title: 'Đồng phục', dataIndex: 'name', render: (_, record) => record?.uniform?.name },
                      { title: 'Số lượng', dataIndex: 'quantity', render: text => formatDecimalPrecision(text) },
                    ]}
                    dataSource={item?.uniformFormDetail}
                  />
                </Descriptions.Item>
              ))}
            </Descriptions>
          </>
        )}
        <div className="divider" />
        <Descriptions title="Thông tin xét duyệt" size="large" bordered column={1} layout="vertical">
          <Descriptions.Item label="Lý do">
            {data?.uniformOrderProcesses?.map((item: IUniformOrderProcesses) => (
              <Typography level="text" className="fw-bolder">
                {item?.reason}
              </Typography>
            ))}
          </Descriptions.Item>
          <Descriptions.Item label="Chữ ký">
            {data?.uniformOrderProcesses?.map(
              (item: IUniformOrderProcesses) =>
                item?.file?.id && (
                  <AttachmentPreview key={item?.file?.name} name={item?.file?.name} fileUrl={`${FILE_UTIL}/${item?.file?.id}`} />
                ),
            )}
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default UniformOrdersDetailModals;
