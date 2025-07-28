import Descriptions from '@uiw/react-descriptions';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import SignTable from 'app/components/sign-table/sign-table';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT } from 'app/constants/common';
import useRecruitment from 'app/hooks/use-recruitment';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import dayjs from 'dayjs';
import React, { useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import recruitmentMapping from '../recruitment-mapping';

const { recruitmentPositionTextMapping, recruitmentContractTypeTextMapping } = recruitmentMapping;
const { useGetRecruitmentById } = useRecruitment;

interface IRecruitmentDetailModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

const RecruitmentDetailModals = (props: IRecruitmentDetailModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;
  const [searchParams, setSearchParams] = useSearchParams();

  const id = searchParams.get('id');
  const { data } = useGetRecruitmentById(id ?? selectedRecord);

  useEffect(() => {
    id && toggle();
  }, [id]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      ok={false}
      className="modals-detail-recruitment"
      onCancel={() => setSearchParams({})}
      titleHeader='Chi tiết tuyển dụng'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="BP Yêu cầu">
            <Typography level="text" className="fw-bolder">
              {data?.department?.name}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Vị trí">
            <Typography level="text" className="fw-bolder">
              {recruitmentPositionTextMapping(data?.position)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Chức vụ">
            <Typography level="text" className="fw-bolder">
              {data?.jobTitle}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Số lượng">
            <Typography level="text" className="fw-bolder">
              {formatDecimalPrecision(data?.quantity)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Cấp bậc">
            <Typography level="text" className="fw-bolder">
              {data?.level}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Mức lương">
            <Typography level="text" className="fw-bolder">
              {formatDecimalPrecision(data?.wage)} {data?.salaryUnit}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ngày bắt đầu đi làm">
            <Typography level="text" className="fw-bolder">
              {data?.startDate ? dayjs(data?.startDate).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ngày hết hạn">
            <Typography level="text" className="fw-bolder">
              {data?.deadline ? dayjs(data?.deadline).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Mục đích tuyển dụng">
            <Typography level="text" className="fw-bolder">
              {data?.recruitmentPurposes}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Loại HĐ">
            <Typography level="text" className="fw-bolder">
              {recruitmentContractTypeTextMapping(data?.contractType)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Người thay thế">
            <Typography level="text" className="fw-bolder">
              {(data?.replaceFor?.lastName || '') + ' ' + (data?.replaceFor?.firstName || '')}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Số lượng ứng viên">
            <Typography level="text" className="fw-bolder">
              {data?.numberOfCandidates ?? 0}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Yêu cầu cho ứng viên" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.requestNotes}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Mô tả công việc" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.description}
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />
        <Descriptions title="Thông tin xét duyệt" size="large" bordered column={1} layout="vertical">
          <Descriptions.Item label="Chữ ký">
            <Typography level="text">
              {
                <SignTable data={data?.listRecruitmentReviews?.map(item => ({
                  signName: item?.approvalSignFileAttachment?.name,
                  signId: item?.approvalSignFileAttachment?.id,
                  employeeId: item?.employeeId,
                  signAt: item?.updatedAt,
                })) || []} />
              }
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Lý do">
            <Typography level="text" className="fw-bolder">
              {data?.listRecruitmentReviews.map(item => item?.rejectNote)?.join('\n')}
            </Typography>
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default RecruitmentDetailModals;
