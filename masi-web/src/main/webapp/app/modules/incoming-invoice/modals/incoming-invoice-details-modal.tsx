import dayjs from 'dayjs';
import React, { ReactElement } from 'react';

import Descriptions from '@uiw/react-descriptions';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import useIncomingInvoice from 'app/hooks/use-incoming-invoice';
import useWorkspace from 'app/hooks/use-workspace';
import { InvoiceType } from 'app/shared/model/enumerations/invoice';
import { invoiceTypeMapping } from '../incoming-invoice-maping';
import { Col, Row } from 'reactstrap';

const { useGetWorkspaceByIdQuery } = useWorkspace;
const { useGetEmployeeProfileByIdQuery } = useEmployee;
const { useIncomingInvoiceById } = useIncomingInvoice;

interface IIncomingInvoiceDetailsModalProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

const IncomingInvoiceDetailsModal = (props: IIncomingInvoiceDetailsModalProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useIncomingInvoiceById(selectedRecord);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      footer={null}
      ok={false}
      cancel={false}
      className="invoice-detail-modal"
      titleHeader="Chi tiết hoá đơn"
    >
      <Flex direction="column" gap={16}>
        <Row>
          <Col md={7}>
            <Descriptions title="Thông tin chung" size="large" bordered column={2}>
              <Descriptions.Item label="Mẫu số">
                <Typography level="text" className="fw-bolder">
                  {data?.invoiceNo}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Seri">
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Số hóa đơn">
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Ngày hóa đơn">
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Nhà cung cấp" span={2}>
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Địa chỉ" span={2}>
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Người lập">
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Ngày lập">
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Người đại diện" span={2}>
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Diễn dãi" span={2}>
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
            </Descriptions>
          </Col>
          <Col md={5}>
            <Descriptions title="Thông tin chung" size="large" bordered column={2}>
              <Descriptions.Item label="Ngày PS">
                <Typography level="text" className="fw-bolder">
                  {data?.invoiceNo}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Hợp đồng">
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Tel">
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Loại tiền">
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="MST">
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Thanh toán">
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Tình trạng">
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Số ngày công nợ">
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Kho">
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Ngày">
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
              <Descriptions.Item label="Thông tin">
                <Typography level="text" className="fw-bolder">
                  {data?.note}
                </Typography>
              </Descriptions.Item>
            </Descriptions>
          </Col>
        </Row>
      </Flex>
    </Modal>
  );
};

export default IncomingInvoiceDetailsModal;
