import dayjs from 'dayjs';
import React from 'react';
import { Row } from 'reactstrap';

import Descriptions from '@uiw/react-descriptions';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Badge from 'app/components/badge/badge';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import SignTable from 'app/components/sign-table/sign-table';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import useContracts from 'app/hooks/use-contracts';
import ContractsList from 'app/modules/orders/contracts-list';
import { Color } from 'app/shared/model/enumerations/color.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { unitTextMapping } from 'app/shared/util/unit-mapping';
import contractsMapping from '../contracts-mapping';

const { useGetContractById } = useContracts;
const { contractStatusMapping, contractStatusTextMapping, contractTypeTextMapping } = contractsMapping;

interface IContractsDetailModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

const ContractsDetailModals = (props: IContractsDetailModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useGetContractById(selectedRecord);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      ok={false}
      cancel={false}
      className="modal-default"
      fullscreen
      titleHeader='Chi tiết hợp đồng'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={3}>
          <Descriptions.Item label="Số HĐ">
            <Typography level="text" className="fw-bolder">
              {data?.contractName}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Tên công ty">
            <Typography level="text" className="fw-bolder">
              {data?.customer?.companyName}
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />
        <Descriptions title="Thông tin chi tiết" size="large" bordered column={2}>
          <Descriptions.Item label="Loại HĐ">
            <Typography level="text" className="fw-bolder">
              {contractTypeTextMapping(data?.contractType)}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Người phụ trách">
            <Typography level="text" className="fw-bolder">
              {(data?.owner?.lastName || '') + ' ' + (data?.owner?.firstName || '')}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Tình trạng HĐ">
            <Typography level="text" className="fw-bolder">
              <Badge
                color={
                  data?.contractValidTo && dayjs(data?.contractValidTo).isBefore(dayjs())
                    ? Color.ERROR
                    : contractStatusMapping(data?.status)
                }
              >
                {contractStatusTextMapping(data?.status)}
              </Badge>
            </Typography>
          </Descriptions.Item>

          {/* <Descriptions.Item label="Địa điểm nhận hàng">
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
          </Descriptions.Item> */}

          <Descriptions.Item label="Thời hạn HĐ">
            <Typography level="text" className="fw-bolder">
              {data?.contractValidFrom ? dayjs(data?.contractValidFrom).format(DATE_FORMAT.DATE) : ''}{' '}
              {data?.contractValidFrom && data?.contractValidTo ? ' - ' : ''}
              {data?.contractValidTo ? dayjs(data?.contractValidTo).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>

          {/* <Descriptions.Item label="Thời hạn thanh toán">
            <Typography level="text" className="fw-bolder">
              {data?.payTerm}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Điều kiện thanh toán">
            <Typography level="text" className="fw-bolder">
              {data?.payCondition}
            </Typography>
          </Descriptions.Item> */}

          <Descriptions.Item label="Đơn vị tiền tệ">
            <Typography level="text" className="fw-bolder">
              {unitTextMapping(data?.monetaryUnit)}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Tỉ giá">
            <Typography level="text" className="fw-bolder">
              {formatDecimalPrecision(data?.exchangeRate)}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Giá trị HĐ">
            <Typography level="text" className="fw-bolder">
              {formatDecimalPrecision(data?.contractTotal)} VND
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />
        <Descriptions title="Danh sách thành phẩm" size="large" layout="vertical" bordered column={1}>
          <Descriptions.Item label="Chi tiết">
            <ContractsList list={data?.contractMaterialDTOS} />
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />
        <Descriptions title="Tệp đính kèm" size="large" layout="vertical" bordered column={5}>
          <Descriptions.Item label="Đính kèm">
            <Row>
              <Flex direction="column" gap={16}>
                {!!data?.fileAttachments?.length && (
                  <>
                    <Flex gap={12} flexWrap="wrap">
                      {data?.fileAttachments?.map(file => (
                        <React.Fragment key={file?.id}>
                          <AttachmentPreview name={file?.name} fileUrl={`${FILE_UTIL}/${file?.id}`} />
                        </React.Fragment>
                      ))}
                    </Flex>
                  </>
                )}
              </Flex>
            </Row>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />

        <Descriptions title="Thông tin xét duyệt" size="large" layout="vertical" bordered column={1}>
          <Descriptions.Item label="Chữ ký">
            <Row>
              <SignTable data={data?.normalApprovals?.map(item => ({
                employeeId: item?.employeeId,
                signId: item?.approvedSign,
                signName: item?.approvedSignName
              }))} />
            </Row>
          </Descriptions.Item>

          <Descriptions.Item label="Lý do">
            {data?.normalApprovals?.map(item => {
              if (item?.rejectNote) return (
                <Flex gap={16}>
                  <Typography level="paragraph">{item?.employee?.fullName}</Typography>
                  :
                  <Typography level="paragraph">{item?.rejectNote}</Typography>
                </Flex>
              )
              else return null;
            })}
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />

        <Descriptions title="Thông tin xét duyệt thanh lý" size="large" layout="vertical" bordered column={1}>
          <Descriptions.Item label="Chữ ký">
            <Row>
              <SignTable data={data?.requestApprovals?.map(item => ({
                employeeId: item?.employeeId,
                signId: item?.approvedSign,
                signName: item?.approvedSignName
              }))} />
            </Row>
          </Descriptions.Item>

          <Descriptions.Item label="Lý do">
            {data?.requestApprovals?.map(item => {
              if (item?.rejectNote) return (
                <Flex gap={16}>
                  <Typography level="paragraph" className='active'>{item?.employee?.fullName}:</Typography>
                  <Typography level="paragraph">{item?.rejectNote}</Typography>
                </Flex>
              )
              else return null;
            })}
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default ContractsDetailModals;
