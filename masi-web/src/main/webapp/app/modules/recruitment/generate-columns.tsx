import React from 'react';
import dayjs from 'dayjs';
import { useMemo } from 'react';
import { Link } from "react-router-dom";

import Badge from 'app/components/badge/badge';
import recruitmentMapping from './recruitment-mapping';
import PopoverApproval from './components/popover-approval';
import ActionsDropdown from './components/actions-dropdown';
import { DATE_FORMAT } from 'app/constants/common';
import { ColumnsTypes } from 'app/components/table/table.d';
import { IRecruitment } from 'app/shared/model/recruitment.model';
import { RECRUITMENT_STATUS } from 'app/shared/model/enumerations/recruitment.model';
import { checkHighlightDeadline } from "app/modules/recruitment/check-highlight-deadline";
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';

const { recruitmentStatusColorMapping, recruitmentStatusTextMapping, recruitmentPositionTextMapping } = recruitmentMapping;

export const generateColumns = (
  toggleDetail: () => void,
  toggleUpdate: () => void,
  toggleApprove: () => void,
  toggleReject: () => void,
  toggleSchedule: () => void,
  toggleDelete: () => void,
  toggleRenew: () => void,
  toggleHistory: () => void,
  setSelectedRecord: (id: string) => void,
  setRecord: React.Dispatch<React.SetStateAction<IRecruitment>>
): ColumnsTypes<IRecruitment> => {

  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<IRecruitment> = useMemo(() => {
    return [
      {
        title: 'BP yêu cầu',
        key: 'department',
        dataIndex: 'department',
        render: (text, record) => (
          <Tooltip label={record?.department?.name} target={`department-${record.id}`}>
            <p className="attachment-link" onClick={() => handleDetail(record?.id)}>
              <EllipsisParagraph text={
                record?.department?.name
              } width={160} id={`department-${record.id}`} />
            </p>
          </Tooltip>
        )
      },
      {
        title: 'Vị trí',
        key: 'position',
        dataIndex: 'position',
        render: (text, record) => (
          <Tooltip label={recruitmentPositionTextMapping(text)} target={`position-${record.id}`}>
            <EllipsisParagraph text={recruitmentPositionTextMapping(text)} id={`position-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Chức vụ',
        key: 'jobTitle',
        dataIndex: 'jobTitle',
        render: (text, record) => (
          <Tooltip label={text} target={`jobTitle-${record.id}`}>
            <EllipsisParagraph text={text} width={100} id={`jobTitle-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Số lượng',
        key: 'quantity',
        dataIndex: 'quantity',
        width: 110,
        render: (text) => <p>{formatDecimalPrecision(text)}</p>
      },
      {
        title: 'Cấp bậc',
        key: 'level',
        dataIndex: 'level',
        width: 110,
      },
      {
        title: 'Số lượng ứng viên',
        key: 'numberOfCandidates',
        dataIndex: 'numberOfCandidates',
        width: 170,
        render: (text, record) => (
          <Link to={`/recruitment/candidates?id=${record.id}`} className="attachment-link">
            {formatDecimalPrecision(text)}
          </Link>
        )
      },
      {
        title: 'Số lần gia hạn',
        key: 'numberAdjourn',
        dataIndex: 'numberAdjourn',
        width: 170,

      },
      {
        title: 'Ngày bắt đầu đi làm',
        key: 'startDate',
        dataIndex: 'startDate',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        title: 'Ngày hết hạn',
        key: 'deadline',
        dataIndex: 'deadline',
        width: 140,
        render: (text, record) => text && (
          <span className={`${checkHighlightDeadline(text) && record?.status !== RECRUITMENT_STATUS.COMPLETED ? 'highlight-overdue-deadline' : ''}`}>
            {dayjs(text).format(DATE_FORMAT.DATE)}
          </span>
        )
      },
      {
        title: 'Mục đích tuyển dụng',
        key: 'recruitmentPurposes',
        dataIndex: 'recruitmentPurposes',
        render: (text, record) => (
          <Tooltip label={text} target={`recruitmentPurposes-${record.id}`}>
            <EllipsisParagraph text={text} id={`recruitmentPurposes-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Trạng thái',
        key: 'status',
        dataIndex: 'status',
        render: (text, record) => {
          return (
            <>
              {record?.status === RECRUITMENT_STATUS.WAITING_APPROVAL || record?.status === RECRUITMENT_STATUS.WAITING_RENEW ? (
                <PopoverApproval data={record?.listRecruitmentReviews} id={`review-${record.id}`}>
                  <Badge id={`review-${record.id}`} color={recruitmentStatusColorMapping(text)}>
                    {recruitmentStatusTextMapping(text)}
                  </Badge>
                </PopoverApproval>
              ) : (
                <Badge id={`review-${record.id}`} color={recruitmentStatusColorMapping(text)}>
                  {recruitmentStatusTextMapping(text)}
                </Badge>
              )}
            </>
          );
        },
      },
      {
        title: 'Thao tác',
        key: 'action',
        width: 106,
        render: (_, record) => (
          <ActionsDropdown
            toggleDetail={toggleDetail}
            toggleUpdate={toggleUpdate}
            toggleApprove={toggleApprove}
            toggleReject={toggleReject}
            toggleSchedule={toggleSchedule}
            toggleDelete={toggleDelete}
            toggleRenew={toggleRenew}
            toggleHistory={toggleHistory}
            setSelectedRecord={setSelectedRecord}
            record={record}
            setRecord={setRecord}
          />
        ),
      },
    ];
  }, []);

  return columns;
};
