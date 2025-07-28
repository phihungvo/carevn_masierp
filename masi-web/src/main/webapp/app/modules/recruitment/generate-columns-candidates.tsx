import { ColumnsTypes } from 'app/components/table/table.d';
import React from 'react';
import { useMemo } from 'react';
import { IInterviewSchedule } from 'app/shared/model/recruitment.model';
import recruitmentMapping from './recruitment-mapping';
import Badge from 'app/components/badge/badge';
import ActionsDropdownCandidates from './components/actions-dropdown-candidate';
import { FILE_UTIL } from 'app/constants/common';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';

const { recruitmentPositionTextMapping, recruitmentProcessTextMapping, recruitmentProcessColorMapping } = recruitmentMapping;

export const generateColumnsCandidates = (
  toggleDetail: () => void,
  toggleUpdate: () => void,
  setSelectedRecord: (id: string) => void,
  toggleUpdateCandidates: () => void
): ColumnsTypes<IInterviewSchedule> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<IInterviewSchedule> = useMemo(() => {
    return [
      {
        title: 'Tên ứng viên',
        key: 'candidateName',
        dataIndex: 'candidateName',
        render: (text, record) => (
          <Tooltip label={text} target={`candidateName-${record.id}`}>
            <p className="attachment-link" onClick={() => handleDetail(record?.id)}>
              <EllipsisParagraph text={
                text
              } width={160} id={`candidateName-${record.id}`} />
            </p>
          </Tooltip>
        )
      },
      {
        title: 'Vị trí',
        key: 'position',
        dataIndex: 'position',
        render: (text, record) => (
          <Tooltip label={recruitmentPositionTextMapping(record?.recruitmentRequest?.position)} target={`position-${record.id}`}>
            <EllipsisParagraph text={recruitmentPositionTextMapping(record?.recruitmentRequest?.position)} id={`position-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Chức vụ',
        key: 'jobTitle',
        dataIndex: 'jobTitle',
        render: (text, record) => (
          <Tooltip label={record?.recruitmentRequest?.jobTitle} target={`jobTitle-${record.id}`}>
            <EllipsisParagraph text={record?.recruitmentRequest?.jobTitle} width={100} id={`jobTitle-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Số điện thoại',
        key: 'phoneNumber',
        dataIndex: 'phoneNumber',
      },
      {
        title: 'Email',
        key: 'email',
        dataIndex: 'email',
        render: (text, record) => (
          <Tooltip label={text} target={`email-${record.id}`}>
            <EllipsisParagraph text={text} width={200} id={`email-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'CV',
        key: 'cvFile',
        dataIndex: 'cvFile',
        render: (_, record) =>
          record?.cvFile && (
            <a className="file-attachment" href={`${FILE_UTIL}/${record?.cvFile}`} target="_blank">
              File
            </a>
          ),
      },

      {
        title: 'Trạng thái',
        key: 'process',
        dataIndex: 'process',
        render: text => <Badge color={recruitmentProcessColorMapping(text)}>{recruitmentProcessTextMapping(text)}</Badge>,
      },
      {
        title: 'Thao tác',
        key: 'action',
        width: 106,
        render: (_, record) => (
          <ActionsDropdownCandidates
            toggleDetail={toggleDetail}
            toggleUpdate={toggleUpdate}
            setSelectedRecord={setSelectedRecord}
            record={record}
            toggleUpdateCandidates={toggleUpdateCandidates}
          />
        ),
      },
    ];
  }, []);

  return columns;
};
