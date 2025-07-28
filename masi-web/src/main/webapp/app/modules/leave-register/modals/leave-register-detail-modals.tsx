import Descriptions from '@uiw/react-descriptions';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import SignTable from 'app/components/sign-table/sign-table';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import useAnnualLeave from 'app/hooks/use-annual-leave';
import useLeaveRegime from 'app/hooks/use-leave-regime';
import leaveRequestMapping from 'app/modules/leave-request/leave-request-mapping';
import leaveRegisterMapping from 'app/modules/leave-register/leave-register-mapping';
import { IBodyFile } from 'app/shared/model/file.model';
import dayjs from 'dayjs';
import React from 'react';
import { LEAVE_REQUEST_DAY_TYPE } from 'app/shared/model/enumerations/leave-request.model';

const { useGetLeaveRegimeById } = useLeaveRegime;
const { mapLeaveRequestType, } = leaveRequestMapping;
const { leaveRegimeTextMapping, } = leaveRegisterMapping;
const { useAnnualLeavesEmployee } = useAnnualLeave;

interface ILeaveRegisterDetailModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord?: string | null;
}

const LeaveRegisterDetailModals = (props: ILeaveRegisterDetailModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useGetLeaveRegimeById(selectedRecord);
  const { data: annualLeavesEmployee } = useAnnualLeavesEmployee(data?.employeeId);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      ok={false}
      className="modals-detail-leave-register"
      titleHeader='Chi tiết đăng ký nghỉ chế độ'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Tên nhân viên">
            <Typography level="text" className="fw-bolder">
              {(data?.employee?.lastName || '') + ' ' + (data?.employee?.firstName || '')}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Loại nghỉ phép">
            <Typography level="text" className="fw-bolder">
              {mapLeaveRequestType(data?.leaveType)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ngày cuối cùng làm việc">
            <Typography level="text" className="fw-bolder">
              {data?.lastWorkDate ? dayjs(data?.lastWorkDate)?.format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Người duyệt">
            <Typography level="text" className="fw-bolder">
              {data?.processLeaveRegimeRequests
                ?.map(item => (item?.approver?.lastName || '') + ' ' + (item?.approver?.firstName || ''))
                .join(', ')}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Người thay thế">
            <Typography level="text" className="fw-bolder">
              {(data?.substitute?.lastName || '') + ' ' + (data?.substitute?.firstName || '')}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Tổng số ngày nghỉ">
            <Typography level="text" className="fw-bolder">
              {data?.totalDayOff || (data?.returnWorkDate && data?.lastWorkDate
                ? dayjs(data?.returnWorkDate)
                  .startOf('date')
                  .diff(dayjs(data?.lastWorkDate).startOf('date'), 'day') - 1
                : 0)}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Thời gian nghỉ">
            <Typography level="text" className="fw-bolder">
              {data?.leaveRequestDayType === LEAVE_REQUEST_DAY_TYPE.HALF_DAY ? `${leaveRegimeTextMapping(data?.leaveRequestDayType)} ( ${dayjs(data?.fromTime).format('HH:mm') ?? ''} - ${dayjs(data?.toTime).format('HH:mm') ?? ''} )` : leaveRegimeTextMapping(data?.leaveRequestDayType)}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label={<>Số phép năm còn lại tính đến tháng {dayjs(data?.createdDate)?.month() + 1}</>}>
            <Typography level="text" className="fw-bolder">
              {annualLeavesEmployee?.numberDaysOff}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Đính kèm" span={2}>
            <Flex gap={12} flexWrap="wrap">
              {data?.files?.map((item: IBodyFile) => {
                return <AttachmentPreview name={item?.fileName} fileUrl={`${FILE_UTIL}/${item?.id}`} />;
              })}
            </Flex>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />

        <Descriptions title="Thông tin xét duyệt" size="large" bordered column={1} layout="vertical">
          <Descriptions.Item label="Lý do">
            <Typography level="text" className="fw-bolder">
              {!!data?.processLeaveRegimeRequests?.length && (
                <Typography level="paragraph">{data?.processLeaveRegimeRequests?.map(item => item?.reason)?.join('\n')}</Typography>
              )}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Chữ ký">
            {
              data?.processLeaveRegimeRequests && <SignTable data={
                data?.processLeaveRegimeRequests.map(item => ({
                  employeeId: item.approverId,
                  signName: item.fileName,
                  signId: item.fileId,
                  signAt: item.updatedAt
                }))
              } />
            }
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default LeaveRegisterDetailModals;
