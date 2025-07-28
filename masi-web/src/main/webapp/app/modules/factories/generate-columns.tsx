import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import { ColumnsTypes } from 'app/components/table/table.d';
import Tooltip from 'app/components/tooltip/tooltip';
import { COMPANY } from 'app/shared/model/enumerations/company.model';
import { IFactoryLogistics } from 'app/shared/model/factory-logistics.model';
import React, { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import factoriesMapping from './factories-mapping';

const { mapFactoriesActiveBadge } = factoriesMapping;

export const generateColumns = (
  toggleDetail: () => void,
  toggleUpdate: () => void,
  toggleDelete: () => void,
  togglePropose: () => void,
  toggleApprove: () => void,
  setSelectedRecord: (id: string) => void,
  toggleDispose: () => void,
  toggleActivate: () => void,
): ColumnsTypes<IFactoryLogistics> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<IFactoryLogistics> = useMemo(() => {
    const col: ColumnsTypes<IFactoryLogistics> = [
      {
        title: 'Mã nhà máy',
        key: 'code',
        dataIndex: 'code',
        render: (text, record) => (
          <Tooltip label={text} target={`name-${record.id}`}>
            <EllipsisParagraph
              text={text}
              width={200}
              id={`name-${record.id}`}
              className="attachment-link"
              onClick={() => handleDetail(record?.id)}
            />
          </Tooltip>
        ),
      },
      {
        title: 'Tên nhà máy',
        key: 'name',
        dataIndex: 'name',
        render: (text, record) => (
          <Tooltip label={text} target={`name-${record.id}`}>
            <EllipsisParagraph
              text={text}
              width={200}
              id={`name-${record.id}`}
            />
          </Tooltip>
        ),
      },
      {
        title: 'Địa chỉ',
        key: 'address',
        dataIndex: 'address',
        render: (text, record) => (
          <Tooltip label={text} target={`address-${record.id}`}>
            <EllipsisParagraph
              text={text}
              width={300}
              id={`address-${record.id}`}
            />
          </Tooltip>
        ),
      },
      {
        title: 'Mã địa chỉ',
        key: 'location',
        dataIndex: 'attribute',
        render: (text, record) => (
          <Tooltip
            label={[text?.location?.x ?? 0, text?.location?.y ?? 0].join(',')}
            target={`attribute-${record.id}`}
          >
            <EllipsisParagraph
              text={[text?.location?.x ?? 0, text?.location?.y ?? 0].join(',')}
              width={300}
              id={`attribute-${record.id}`}
            />
          </Tooltip>
        ),
      },
      {
        title: 'Trực thuộc',
        key: 'company',
        dataIndex: 'company',
        render: (text, record) => (
          <Tooltip
            label={text === COMPANY.KIM_LONG ? 'Kim Long' : 'MMS'}
            target={`company-${record.id}`}
          >
            <EllipsisParagraph
              text={text === COMPANY.KIM_LONG ? 'Kim Long' : 'MMS'}
              width={200}
              id={`company-${record.id}`}
            />
          </Tooltip>
        ),
      },
      {
        title: 'Người quản lí',
        key: 'employeeOwner',
        dataIndex: 'employeeOwner',
        render: (text, record) => (
          <Tooltip label={text?.fullName} target={`employeeOwner-${record.id}`}>
            <EllipsisParagraph
              text={`${text?.code ? text?.code + ' - ' : ''} ${text?.fullName}`}
              width={300}
              id={`employeeOwner-${record.id}`}
            />
          </Tooltip>
        ),
      },
      {
        title: 'Ghi chú',
        key: 'note',
        dataIndex: 'note',
        render: (text, record) => (
          <Tooltip label={text} target={`note-${record.id}`}>
            <EllipsisParagraph
              text={text}
              width={120}
              id={`note-${record.id}`}
            />
          </Tooltip>
        ),
      },
      {
        title: 'Tình trạng',
        key: 'isActive',
        dataIndex: 'isActive',
        render: text => mapFactoriesActiveBadge(text),
      },
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
            toggleDispose={toggleDispose}
            toggleActivate={toggleActivate}
            record={record}
            setSelectedRecord={setSelectedRecord}
          />
        ),
      },
    ];

    return col;
  }, [
    toggleUpdate,
    toggleDelete,
    toggleDispose,
    toggleActivate,
    setSelectedRecord,
  ]);

  return columns;
};
