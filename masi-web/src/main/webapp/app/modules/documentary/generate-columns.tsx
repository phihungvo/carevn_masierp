import { ColumnsTypes } from 'app/components/table/table.d';
import React from 'react';
import { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import dayjs from 'dayjs';
import { DATE_FORMAT } from 'app/constants/common';
import { IDocumentary } from 'app/shared/model/documentary.model';
import documentaryMapping from './documentary-mapping';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';

const { documentaryGroupMapping, documentaryTypeMapping, documentaryStatusColorMapping, documentaryStatusTextMapping } = documentaryMapping;

export const generateColumns = (
  toggleDetail: () => void,
  toggleUpdate: () => void,
  toggleDelete: () => void,
  togglePropose: () => void,
  toggleApprove: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<IDocumentary> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<IDocumentary> = useMemo(() => {
    return [
      {
        title: 'Số công văn',
        key: 'documentNumber',
        dataIndex: 'documentNumber',
        render: (text, record) => (
          <Tooltip label={text} target={`documentNumber-${record.id}`}>
            <EllipsisParagraph text={
              <p className="attachment-link" onClick={() => handleDetail(record.id)}>
                {text}
              </p>
            } width={100} id={`documentNumber-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Ngày',
        key: 'dateStart',
        dataIndex: 'dateStart',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        title: 'Nhóm',
        key: 'group',
        dataIndex: 'group',
        render: (text, record) => (
          <Tooltip label={documentaryGroupMapping(text)} target={`group-${record.id}`}>
            <EllipsisParagraph text={documentaryGroupMapping(text)} id={`group-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Loại',
        key: 'type',
        dataIndex: 'type',
        render: (text, record) => (
          <Tooltip label={documentaryTypeMapping(text)} target={`type-${record.id}`}>
            <EllipsisParagraph text={documentaryTypeMapping(text)} id={`type-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Nội dung',
        key: 'content',
        dataIndex: 'content',
        render: (text, record) => (
          <Tooltip label={text} target={`content-${record.id}`}>
            <EllipsisParagraph text={text} id={`content-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Người ký',
        key: 'employeeProfileSigner',
        dataIndex: 'employeeProfileSigner',
        render: (text, record) => (
          <Tooltip label={record?.employeeProfileSigner?.fullName} target={`employeeProfileSigner-${record.id}`}>
            <EllipsisParagraph text={record?.employeeProfileSigner?.fullName} id={`employeeProfileSigner-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Người nhận',
        key: 'employeeProfileSender',
        dataIndex: 'employeeProfileSender',
        render: (text, record) => (
          <Tooltip label={record?.employeeProfileSender?.fullName} target={`employeeProfileSender-${record.id}`}>
            <EllipsisParagraph text={record?.employeeProfileSender?.fullName} id={`employeeProfileSender-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Nơi nhận',
        key: 'recipient',
        dataIndex: 'recipient',
        render: (text, record) => (
          <Tooltip label={text} target={`recipient-${record.id}`}>
            <EllipsisParagraph text={text} id={`recipient-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Nơi lưu',
        key: 'archiveLocation',
        dataIndex: 'archiveLocation',
        render: (text, record) => (
          <Tooltip label={text} target={`archiveLocation-${record.id}`}>
            <EllipsisParagraph text={text} id={`archiveLocation-${record.id}`} />
          </Tooltip>
        )
      },
      // {
      //   title: 'Trạng thái',
      //   key: 'status',
      //   dataIndex: 'status',
      //   render: text => <Badge color={documentaryStatusColorMapping(text)}>{documentaryStatusTextMapping(text)}</Badge>,
      // },
      {
        title: 'Thao tác',
        key: 'action',
        dataIndex: 'action',
        width: 106,
        render: (_, record) => (
          <ActionsDropdown
            toggleDetail={toggleDetail}
            toggleUpdate={toggleUpdate}
            toggleDelete={toggleDelete}
            togglePropose={togglePropose}
            toggleApprove={toggleApprove}
            record={record}
            setSelectedRecord={setSelectedRecord}
          />
        ),
      },
    ];
  }, []);

  return columns;
};
