import Badge from 'app/components/badge/badge';
import { ColumnsTypes } from 'app/components/table/table.d';
import { ICustomer } from 'app/shared/model/customer.model';
import React from 'react';
import { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import customerMapping from './customer-mapping';
import dayjs from 'dayjs';
import { DATE_FORMAT } from 'app/constants/common';
import { checkHighlightBirthday } from './check-highlight-birthday';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Tooltip from 'app/components/tooltip/tooltip';

const { mapCustomerStatusText } = customerMapping;

export const generateColumns = (
  toggleDetail: () => void,
  toggleUpdate: () => void,
  toggleDispose: () => void,
  toggleTransfer: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<ICustomer> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };
  const columns: ColumnsTypes<ICustomer> = useMemo(() => {
    return [
      {
        key: 'customerCode',
        title: 'Mã KH',
        width: 150,
        dataIndex: 'customerCode',
        render: (text, record) => (
          <p className="attachment-link" onClick={() => handleDetail(record.id)}>
            {text}
          </p>
        ),
      },
      {
        key: 'companyName',
        title: 'Tên Cty',
        dataIndex: 'companyName',
        render: (text, record) => (
          <Tooltip label={text} target={`companyName-${record.id}`}>
            <EllipsisParagraph text={text} id={`companyName-${record.id}`} />
          </Tooltip>
        )
      },
      {
        key: 'name',
        title: 'Tên người liên hệ',
        dataIndex: 'name',
        render: (text, record) => (
          <Tooltip label={(record?.lastName || '') + ' ' + (record?.firstName || '')} target={`name-${record.id}`}>
            <EllipsisParagraph text={(record?.lastName || '') + ' ' + (record?.firstName || '')} id={`name-${record.id}`} />
          </Tooltip>
        )
      },
      {
        key: 'email',
        title: 'Email',
        dataIndex: 'email',
        render: (text, record) => (
          <Tooltip label={text} target={`email-${record.id}`}>
            <EllipsisParagraph text={text} id={`email-${record.id}`} />
          </Tooltip>
        )
      },
      {
        key: 'customerOwner',
        title: 'Người phụ trách',
        dataIndex: 'customerOwner',
        render: (text, record) => (
          <Tooltip label={record?.customerOwnerDTO?.fullName || ''} target={`customerOwner-${record.id}`}>
            <EllipsisParagraph text={record?.customerOwnerDTO?.fullName || ''} id={`customerOwner-${record.id}`} />
          </Tooltip>
        )
      },
      {
        key: 'birthday',
        title: 'Ngày sinh',
        dataIndex: 'birthday',
        render: text => {
          return text ? (
            <span className={`${checkHighlightBirthday(text) ? 'next-month-birthday-highlight' : ''}`}>
              {dayjs(text).format(DATE_FORMAT.DATE)}
            </span>
          ) : (
            ''
          );
        },
      },
      {
        key: 'contractSigned',
        title: 'Ngày tạo',
        dataIndex: 'contractSigned',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        key: 'customerStatus',
        title: 'Trạng thái',
        dataIndex: 'customerStatus',
        render: text => <Badge color={'success'}>{mapCustomerStatusText(text)}</Badge>,
      },
      {
        key: 'action',
        title: 'Thao tác',
        width: 106,
        render: (_, record) => (
          <ActionsDropdown
            toggleDetail={toggleDetail}
            toggleUpdate={toggleUpdate}
            toggleDispose={toggleDispose}
            toggleTransfer={toggleTransfer}
            record={record}
            setSelectedRecord={setSelectedRecord}
          />
        ),
      },
    ];
  }, []);

  return columns;
};
