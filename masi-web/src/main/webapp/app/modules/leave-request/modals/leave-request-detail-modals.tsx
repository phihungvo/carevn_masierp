import React from 'react';
import dayjs from 'dayjs';
import { Row } from 'reactstrap';

import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import Badge from 'app/components/badge/badge';
import Descriptions from '@uiw/react-descriptions';
import useLeaveRequest from 'app/hooks/use-leave-request';
import leaveRequestMapping from 'app/modules/leave-request/leave-request-mapping';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import { IBodyFile } from 'app/shared/model/file.model';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import { Typography } from 'app/components/typography/typography';
import { LEAVE_REQUEST_DAY_TYPE, LEAVE_REQUEST_STATUS, LEAVE_REQUEST_TYPE } from 'app/shared/model/enumerations/leave-request.model';
import leaveRegisterMapping from 'app/modules/leave-register/leave-register-mapping';

const { useGetLeaveRequestByIdQuery } = useLeaveRequest;
const { mapLeaveRequestType, mapLeaveRequestStatus, mapLeaveRequestStatusColor } = leaveRequestMapping;
const { leaveRegimeTextMapping, } = leaveRegisterMapping;
interface ILeaveRequestDetailModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord?: string | null;
}

const LeaveRequestDetailModals = (props: ILeaveRequestDetailModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useGetLeaveRequestByIdQuery(selectedRecord);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false} ok={false}
      className="modals-detail-leave-request"
      titleHeader='Chi tiết đơn nghỉ phép'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Tên nhân viên">
            <Typography level="text" className="fw-bolder">
              {(data?.data?.employee?.lastName || '') + ' ' + (data?.data?.employee?.firstName || '')}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Loại nghỉ phép">
            <Typography level="text" className="fw-bolder">
              {mapLeaveRequestType(data?.data?.leaveRequestType as LEAVE_REQUEST_TYPE)}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Ngày cuối cùng làm việc">
            <Typography level="text" className="fw-bolder">
              {data?.data?.fromDate ? dayjs(data?.data?.fromDate)?.format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Ngày trở lại làm việc">
            <Typography level="text" className="fw-bolder">
              {data?.data?.toDate ? dayjs(data?.data?.toDate)?.format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Thời gian nghỉ">
            <Typography level="text" className="fw-bolder">
              {data?.data?.leaveRequestDayType === LEAVE_REQUEST_DAY_TYPE.HALF_DAY ? `${leaveRegimeTextMapping(data?.data?.leaveRequestDayType)} ( ${dayjs(data?.data?.fromTime).format('HH:mm') ?? ''} - ${dayjs(data?.data?.toTime).format('HH:mm') ?? ''} )` : leaveRegimeTextMapping(data?.data?.leaveRequestDayType as LEAVE_REQUEST_DAY_TYPE)}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Người duyệt">
            <Typography level="text" className="fw-bolder">
              {data?.data?.reviews?.map(r => (r?.reviewer?.lastName || '') + ' ' + (r?.reviewer?.firstName || '')).join(', ')}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Người thay thế">
            <Typography level="text" className="fw-bolder">
              {(data?.data?.substitute?.lastName || '') + ' ' + (data?.data?.substitute?.firstName || '')}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Trạng thái">
            <Typography level="text" className="fw-bolder">
              <Badge color={mapLeaveRequestStatusColor(data?.data?.status as LEAVE_REQUEST_STATUS)}>
                {mapLeaveRequestStatus(data?.data?.status as LEAVE_REQUEST_STATUS)}
              </Badge>
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Lý do từ chối">
            <Typography level="text" className="fw-bolder">
              {data?.data?.reviews[0]?.reason || data?.data?.reviews[1]?.reason}
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />
        <Descriptions title="Tệp đính kèm" size="large" layout="vertical" bordered column={5}>
          <Descriptions.Item label="Đính kèm">
            <Row>
              <Flex direction="column" gap={16}>
                <Flex gap={12} flexWrap="wrap">
                  {data?.data?.files?.map((item: IBodyFile) => {
                    return <AttachmentPreview key={item?.id} name={item?.fileName} fileUrl={`${FILE_UTIL}/${item?.id}`} />;
                  })}
                </Flex>
              </Flex>
            </Row>
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default LeaveRequestDetailModals;
