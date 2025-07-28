import { ColumnsTypes } from 'app/components/table/table.d';
import { ISupplier } from 'app/shared/model/supplier.model';
import React, { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import supplierMaping from './supplier-maping';

const { mapSupplierStatusBadge } = supplierMaping;

export const generateColumns = (
  toggleUpdate: () => void,
  toggleDelete: () => void,
  setSelectedRecord: (id: string) => void,
  toggleDetail: () => void,
  toggleDispose: () => void,
  toggleActivate: () => void,
): ColumnsTypes<ISupplier> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<ISupplier> = useMemo(() => {
    return [
      {
        title: 'Mã nhà cung cấp',
        key: 'code',
        dataIndex: 'code',
        render: (text, record) => (
          <p className="attachment-link" onClick={() => handleDetail(record?.id)}>
            {text}
          </p>
        ),
      },
      {
        title: 'Tên nhà cung cấp',
        key: 'name',
        dataIndex: 'name',
        render: (text, record) => (
          <Tooltip label={text} target={`name-${record.id}`}>
            <EllipsisParagraph text={text} id={`name-${record.id}`} />
          </Tooltip>
        ),
      },
      {
        title: 'Số điện thoại',
        key: 'phone',
        dataIndex: 'phone',
        render: (text, record) => (
          <Tooltip label={text} target={`phone-${record.id}`}>
            <EllipsisParagraph text={text} width={140} id={`phone-${record.id}`} />
          </Tooltip>
        ),
      },
      {
        title: 'Email',
        key: 'email',
        dataIndex: 'email',
        render: (text, record) => (
          <Tooltip label={text} target={`email-${record.id}`}>
            <EllipsisParagraph text={text} id={`email-${record.id}`} />
          </Tooltip>
        ),
      },
      {
        title: 'Địa chỉ',
        key: 'address',
        dataIndex: 'address',
        render: (text, record) => (
          <Tooltip label={text} target={`address-${record.id}`}>
            <EllipsisParagraph text={text} id={`address-${record.id}`} />
          </Tooltip>
        ),
      },
      {
        title: 'Trạng thái',
        key: 'isActive',
        dataIndex: 'isActive',
        render: text => mapSupplierStatusBadge(text),
      },
      {
        title: 'Thao tác',
        key: 'action',
        width: 106,
        dataIndex: 'action',
        render: (_, record) => (
          <ActionsDropdown
            toggleDetail={toggleDetail}
            toggleUpdate={toggleUpdate}
            toggleDelete={toggleDelete}
            toggleDispose={toggleDispose}
            toggleActivate={toggleActivate}
            record={record}
            setSelectedRecord={setSelectedRecord}
          />
        ),
      },
    ];
  }, [toggleUpdate, toggleDelete, toggleDispose, setSelectedRecord]);

  return columns;
};
