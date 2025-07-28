import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useRecruitment from 'app/hooks/use-recruitment';
import React from 'react';
import recruitmentMapping from '../recruitment-mapping';
import dayjs from 'dayjs';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Descriptions from '@uiw/react-descriptions';
import Badge from 'app/components/badge/badge';

const {
  recruitmentPositionTextMapping,
  recruitmentProcessTextMapping,
  interviewModeTextMapping,
  interviewModeColorMapping,
  interviewResultTextMapping,
  recruitmentProcessColorMapping,
} = recruitmentMapping;
const { useGetRecruitmentsCandidateById } = useRecruitment;

interface IRecruitmentDetailModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

const RecruitmentDetailModalsCandidates = (props: IRecruitmentDetailModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useGetRecruitmentsCandidateById(selectedRecord);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      ok={false}
      className="modals-detail-recruitment"
      titleHeader='Chi tiết ứng viên'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Tên ứng viên">
            <Typography level="text" className="fw-bolder">
              {data?.candidateName}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Email">
            <Typography level="text" className="fw-bolder">
              {data?.email}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Vị trí">
            <Typography level="text" className="fw-bolder">
              {recruitmentPositionTextMapping(data?.recruitmentRequest?.position)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Số điện thoại">
            <Typography level="text" className="fw-bolder">
              {data?.phoneNumber}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ngày phỏng vấn">
            <Typography level="text" className="fw-bolder">
              {data?.interviewDate ? dayjs(data?.interviewDate).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Giờ phỏng vấn">
            <Typography level="text" className="fw-bolder">
              {data?.interviewDate ? dayjs(data?.interviewDate).format(DATE_FORMAT.TIME_ONLY) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Người phỏng vấn">
            <Typography level="text" className="fw-bolder">
              {(data?.interviewer?.lastName || '') + ' ' + (data?.interviewer?.firstName || '')}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Tiến trình">
            <Typography level="text" className="fw-bolder">
              <Badge color={recruitmentProcessColorMapping(data?.process)}>{recruitmentProcessTextMapping(data?.process)}</Badge>
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Hình thức">
            <Typography level="text" className="fw-bolder">
              <Badge color={interviewModeColorMapping(data?.interviewMode)}>{interviewModeTextMapping(data?.interviewMode)}</Badge>
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Kết quả">
            <Typography level="text" className="fw-bolder">
              {interviewResultTextMapping(data?.interviewResult)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Đánh giá" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.rate}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="CV" span={2}>
            {data?.cvFile && <AttachmentPreview name={data?.cvFileAttachment?.name} fileUrl={`${FILE_UTIL}/${data?.cvFile}`} />}
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default RecruitmentDetailModalsCandidates;
