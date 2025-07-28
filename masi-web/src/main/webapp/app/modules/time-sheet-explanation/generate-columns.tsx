import { ColumnsTypes } from 'app/components/table/table.d';
import { ITimeKeepingExplanation } from 'app/shared/model/time-keeping-explanation.model';
import React, { useMemo } from 'react';
import timeSheetExplanationMapping from './time-sheet-explanation-mapping';
import dayjs from 'dayjs';
import { DATE_FORMAT } from 'app/constants/common';
import { Link } from 'react-router-dom';
import { PATH } from 'app/constants/path';
import Badge from 'app/components/badge/badge';
import ActionsDropdown from './actions-dropdown';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Tooltip from 'app/components/tooltip/tooltip';

const { mapTimeKeepingExplanationReasonToText, mapTimeKeepingExplanationStatusColor, mapTimeKeepingExplanationStatusToText } =
  timeSheetExplanationMapping;

export const generateColumns = (
  toggleAcceptReq: () => void,
  toggleRejectReq: () => void,
  toggleUpdateReq: () => void,
  toggleCancelReq: () => void,
  toggleDeleteReq: () => void,
  setSelectedRecord: (record: string) => void,
  setSelectedRows: (selectedRows: ITimeKeepingExplanation[]) => void,
): ColumnsTypes<ITimeKeepingExplanation> => {
  const columns: ColumnsTypes<ITimeKeepingExplanation> = useMemo(() => {
    return [
      {
        key: 'employee',
        title: 'Tên NV',
        render: (text, record) => (
          <Tooltip label={(record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')} target={`employee-${record.id}`}>
            <EllipsisParagraph text={(record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')} width={180} id={`employee-${record.id}`} />
          </Tooltip>
        )
      },
      {
        key: 'reason',
        title: 'Lý do vi phạm',
        width: '20%',
        dataIndex: 'reason',
        render: (text, record) => (
          <Tooltip label={mapTimeKeepingExplanationReasonToText(text)} target={`reason-${record.id}`}>
            <EllipsisParagraph text={mapTimeKeepingExplanationReasonToText(text)} id={`reason-${record.id}`} />
          </Tooltip>
        )
      },
      {
        key: 'explanation',
        title: 'Giải trình',
        width: '20%',
        dataIndex: 'explanation',
        render: (text, record) => (
          <Tooltip label={text} target={`explanation-${record.id}`}>
            <EllipsisParagraph text={
              <p className="explanation-text">{text}</p>
            } id={`explanation-${record.id}`} />
          </Tooltip>
        )
      },
      { key: 'date', title: 'Ngày', dataIndex: 'createdAt', render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : '') },
      {
        key: 'violationIds',
        title: 'Vi phạm',
        render: (_, record) => (
          <Link to={PATH.TIME_SHEET_EXPLANATION_VIOLATION.replace(':id', record.id)} className="attachment-link">
            link
          </Link>
        ),
      },
      {
        key: 'status',
        title: 'Trạng thái',
        dataIndex: 'status',
        render: text => <Badge color={mapTimeKeepingExplanationStatusColor(text)}>{mapTimeKeepingExplanationStatusToText(text)}</Badge>,
      },
      {
        key: 'actions',
        title: 'Thao tác',
        width: 106,
        render: (_, record) => (
          <ActionsDropdown
            record={record}
            toggleRejectReq={toggleRejectReq}
            toggleAcceptReq={toggleAcceptReq}
            toggleUpdateReq={toggleUpdateReq}
            toggleCancelReq={toggleCancelReq}
            toggleDeleteReq={toggleDeleteReq}
            setSelectedRecord={setSelectedRecord}
            setSelectedRows={setSelectedRows}
          />
        ),
      },
    ];
  }, []);

  return columns;
};
