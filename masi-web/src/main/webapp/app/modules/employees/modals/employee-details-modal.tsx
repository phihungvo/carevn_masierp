import dayjs from 'dayjs';
import React, { ReactElement } from 'react';

import Descriptions from '@uiw/react-descriptions';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Badge from 'app/components/badge/badge';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import recruitmentMapping from 'app/modules/recruitment/recruitment-mapping';
import { IFileAttachment, IProfileAttachment } from 'app/shared/model/employee.model';
import { RECRUITMENT_CONTRACT_TYPE } from 'app/shared/model/enumerations/recruitment.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import employeesMapping from '../employees-mapping';
import { getWorkDurationDisplay } from '../util/official-work-duration';

const { recruitmentPositionTextMapping, recruitmentContractTypeTextMapping } = recruitmentMapping;
const { employeeProfileStatusMapping, genderTextMapping, employeeProfileStatusColorMapping } = employeesMapping;
const { useGetEmployeeProfileByIdQuery } = useEmployee;

interface IEmployeeDetailsModalProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

const EmployeeDetailsModal = (props: IEmployeeDetailsModalProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useGetEmployeeProfileByIdQuery(selectedRecord);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      footer={null}
      ok={false}
      cancel={false}
      className="employee-detail-modal"
      titleHeader='Chi tiết nhân viên'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Mã NV">
            <Typography level="text" className="fw-bolder">
              {data?.employeeCode}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Tên NV">
            <Typography level="text" className="fw-bolder">
              {data?.fullName}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ngày sinh">
            <Typography level="text" className="fw-bolder">
              {data?.birthday ? dayjs(data?.birthday)?.format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Giới tính">
            <Typography level="text" className="fw-bolder">
              {genderTextMapping(data?.gender)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="BP / Nhà máy">
            <Typography level="text" className="fw-bolder">
              {data?.workspace?.name}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Chức vụ">
            <Typography level="text" className="fw-bolder">
              {data?.role}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Vị trí">
            <Typography level="text" className="fw-bolder">
              {recruitmentPositionTextMapping(data?.position)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Bậc">
            <Typography level="text" className="fw-bolder">
              {data?.level ?? ''}
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />
        <Descriptions title="Thông tin liên hệ" size="large" bordered column={2}>
          <Descriptions.Item label="SĐT">
            <Typography level="text" className="fw-bolder">
              {data?.phone}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Email">
            <Typography level="text" className="fw-bolder">
              {data?.email}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Địa chỉ thường trú">
            <Typography level="text" className="fw-bolder">
              {data?.residenceAddress}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Địa chỉ tạm trú">
            <Typography level="text" className="fw-bolder">
              {data?.temporaryAddress}
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />
        <Descriptions title="Thông tin khác" size="large" bordered column={2}>
          <Descriptions.Item label="Số CCCD">
            <Typography level="text" className="fw-bolder">
              {data?.citizenId}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="ID máy chấm công">
            <Typography level="text" className="fw-bolder">
              {data?.pin}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Ngày cấp">
            <Typography level="text" className="fw-bolder">
              {data?.citizenIssueDate ? dayjs(data?.citizenIssueDate)?.format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Nơi cấp">
            <Typography level="text" className="fw-bolder">
              {data?.citizenIssuePlace}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Mã ngân hàng">
            <Typography level="text" className="fw-bolder">
              {data?.bankCode}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="TK ngân hàng">
            <Typography level="text" className="fw-bolder">
              {data?.bankNumber}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Thẻ BH">
            <Typography level="text" className="fw-bolder">
              {data?.insuranceCard}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Mức đóng bảo hiểm">
            <Typography level="text" className="fw-bolder">
              {data?.insurancePaymentLevel ? formatDecimalPrecision(data?.insurancePaymentLevel) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Thẻ xe">
            <Typography level="text" className="fw-bolder">
              {data?.parkingCard}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="MST">
            <Typography level="text" className="fw-bolder">
              {data?.taxCode}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ghi chú">
            <Typography level="text" className="fw-bolder">
              {data?.note}
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />
        <Descriptions title="Thông tin công việc" size="large" bordered column={2}>
          <Descriptions.Item label="Số HĐ" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.contractNumber}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Loại HĐ">
            <Typography level="text" className="fw-bolder">
              {recruitmentContractTypeTextMapping(data?.contractType as RECRUITMENT_CONTRACT_TYPE)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Thời hạn">
            <Typography level="text" className="fw-bolder">
              {data?.contractTerm}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ngày vào làm">
            <Typography level="text" className="fw-bolder">
              {data?.startWorkDate ? dayjs(data?.startWorkDate).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ngày HĐ">
            <Typography level="text" className="fw-bolder">
              {data?.contractDate ? dayjs(data?.contractDate).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ngày kết thúc">
            <Typography level="text" className="fw-bolder">
              {data?.contractEndDate ? dayjs(data?.contractEndDate)?.format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Thời gian thử việc">
            <Typography level="text" className="fw-bolder">
              {data?.probationDateFrom ? dayjs(data?.probationDateFrom)?.format(DATE_FORMAT.DATE) : ''}
              {data?.probationDateFrom && data?.probationDateTo && ' - '}
              {data?.probationDateTo ? dayjs(data?.probationDateTo)?.format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Thâm niên">
            <Typography level="text" className="fw-bolder">
              {getWorkDurationDisplay({
                probationEndDate: data?.probationDateTo,
                startWorkDate: data?.startWorkDate,
              })}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Phép năm">
            <Typography level="text" className="fw-bolder">
              {`${data?.useDaysOff ?? 0}/${data?.numberDaysOff ?? 0}`}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Người giới thiệu">
            <Typography level="text" className="fw-bolder">
              {(data?.referrer?.lastName || '') + ' ' + (data?.referrer?.firstName || '')}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ngày chi tiền giới thiệu">
            <Typography level="text" className="fw-bolder">
              {data?.referrerDate ? dayjs(data?.referrerDate)?.format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Tình trạng" span={2}>
            <Typography level="text" className="fw-bolder">
              <Badge color={employeeProfileStatusColorMapping(data?.status)}>{employeeProfileStatusMapping(data?.status)}</Badge>
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Hồ sơ đính kèm" span={2}>
            <Flex flexWrap="wrap" gap={16}>
              {data?.files?.map((element: IProfileAttachment): ReactElement => {
                return (
                  <React.Fragment key={element?.id}>
                    <AttachmentPreview
                      name={`${element?.type} - ${element?.fileAttachment?.name}`}
                      fileUrl={`${FILE_UTIL}/${element?.fileAttachment?.id}`}
                    />
                  </React.Fragment>
                );
              })}
            </Flex>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />
        <Descriptions title="Thông tin nghỉ việc" size="large" bordered column={2}>
          <Descriptions.Item label="Ngày nộp đơn nghỉ việc">
            <Typography level="text" className="fw-bolder">
              {data?.confirmLeave?.submissionDate && dayjs(data?.confirmLeave?.submissionDate)?.format(DATE_FORMAT.DATE)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ngày nghỉ việc">
            <Typography level="text" className="fw-bolder">
              {data?.confirmLeave?.leaveDate && dayjs(data?.confirmLeave?.leaveDate)?.format(DATE_FORMAT.DATE)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Lý do nghỉ việc">
            <Typography level="text" className="fw-bolder">
              {data?.confirmLeave?.reason}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Giải quyết của BP HCNS">
            <Typography level="text" className="fw-bolder">
              {data?.confirmLeave?.hrSolution}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Hồ sơ đính kèm">
            <Flex flexWrap="wrap" gap={16}>
              {data?.confirmLeave?.fileAttachments?.map((element: IFileAttachment): ReactElement => {
                return (
                  <React.Fragment key={element?.id}>
                    <AttachmentPreview name={element.name} fileUrl={`${FILE_UTIL}/${element.id}`} />
                  </React.Fragment>
                );
              })}
            </Flex>
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default EmployeeDetailsModal;
