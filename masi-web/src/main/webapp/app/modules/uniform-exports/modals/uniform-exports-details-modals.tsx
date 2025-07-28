import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';
import Flex from 'app/components/flex/flex';
import { Col, Label, Row } from 'reactstrap';
import useUniform from 'app/hooks/use-uniform';
import dayjs from 'dayjs';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import Descriptions from '@uiw/react-descriptions';
import Table from 'app/components/table/table';

const { useUniformReleaseById } = useUniform;

interface IUniformExportsDetailsModals {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

const UniformExportsDetailsModals = (props: IUniformExportsDetailsModals) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useUniformReleaseById(selectedRecord);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      ok={false}
      className="modals-details-uniform-export"
      titleHeader='Chi tiết xuất kho'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Tên nhân viên">
            <Typography level="text" className="fw-bolder">
              {(data?.employee?.lastName || '') + ' ' + (data?.employee?.firstName || '')}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ngày">
            <Typography level="text" className="fw-bolder">
              {data?.date ? dayjs(data?.date).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Số lượng">
            <Typography level="text" className="fw-bolder">
              {formatDecimalPrecision(data?.quantity)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Chi phí">
            <Typography level="text" className="fw-bolder">
              {formatDecimalPrecision(data?.cost)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Mã phiếu xuất" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.code}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ghi chú" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.note}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Đính kèm" span={2}>
            {data?.fileId && <AttachmentPreview name={data?.fileName} fileUrl={`${FILE_UTIL}/${data?.fileId}`} />}
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />
        <Descriptions title="Danh sách đồng phục" size="large" bordered column={1} layout="vertical">
          <Descriptions.Item label="Chi tiết">
            <Table
              rowKey="id"
              columns={[
                { title: 'Đồng phục', dataIndex: 'name', render: (_, record) => record?.uniform?.name },
                { title: 'Đơn vị', dataIndex: 'unit', render: (_, record) => record?.uomName },
                { title: 'Số lượng', dataIndex: 'quantity', render: text => formatDecimalPrecision(text) },
                { title: 'Đơn giá', dataIndex: 'basePrice', render: (_, record) => formatDecimalPrecision(record?.uniform?.basePrice) },
                { title: 'Giá mua', dataIndex: 'actualPrice', render: text => formatDecimalPrecision(text) },
              ]}
              dataSource={data?.uniformFormDetails}
            />
          </Descriptions.Item>
        </Descriptions>

        {data?.uniformReturn?.length > 0 && (
          <Row>
            <Label className="fw-bold">Lịch sử hoàn ứng:</Label>
            {data?.uniformReturn?.map(item => (
              <React.Fragment key={item?.id}>
                <Typography level="paragraph" className="mt-2">
                  -Ngày {dayjs(item?.date)?.format(DATE_FORMAT.DATE)}:
                </Typography>
                {item?.uniformFormDetail?.map(detail => (
                  <React.Fragment key={detail?.id}>
                    <Col md={6} style={{ width: 360 }}>
                      <Flex gap={16}>
                        <Typography level="paragraph" className="bold test-check-heading">
                          Đồng phục:
                        </Typography>
                        <Typography level="paragraph">{detail?.uniform?.name}</Typography>
                      </Flex>
                    </Col>

                    <Col md={6} style={{ width: 360 }}>
                      <Flex gap={16}>
                        <Typography level="paragraph" className="bold test-check-heading">
                          Số lượng:
                        </Typography>
                        <Typography level="paragraph">{formatDecimalPrecision(detail?.quantity)}</Typography>
                      </Flex>
                    </Col>
                  </React.Fragment>
                ))}
              </React.Fragment>
            ))}
          </Row>
        )}
      </Flex>
    </Modal>
  );
};

export default UniformExportsDetailsModals;
