import { ColumnsTypes } from 'app/components/table/table.d';
import { useMemo } from 'react';
import dayjs from 'dayjs';
import { DATE_FORMAT } from 'app/constants/common';
import { IUniformRelease } from 'app/shared/model/uniform.model';
import React from 'react';
import uniformMapping from '../uniform/uniform-mapping';
import Input from 'app/components/input/input';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';

const { uniformReleaseTypeMapping } = uniformMapping;

export const generateColumns = (
  setSelectedRecord: React.Dispatch<React.SetStateAction<string>>,
  toggleDetail: () => void,
): ColumnsTypes<IUniformRelease> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<IUniformRelease> = useMemo(() => {
    return [
      {
        title: 'Ngày',
        key: 'date',
        dataIndex: 'date',
        render: (text, record) => (
          <Tooltip label={text ? dayjs(text).format(DATE_FORMAT.DATE) : ''} target={`date-${record.id}`}>
            <EllipsisParagraph text={
              <p className="attachment-link" onClick={() => handleDetail(record.id)}>
                {text ? dayjs(text).format(DATE_FORMAT.DATE) : ''}
              </p>
            } id={`date-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Nhân viên',
        key: 'employee',
        dataIndex: 'employee',
        render: (text, record) => (
          <Tooltip label={(record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')} target={`employee-${record.id}`}>
            <EllipsisParagraph text={(record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')} id={`employee-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Đồng phục',
        key: 'uniform',
        dataIndex: 'uniform',
        render: (text, record) => (
          <Tooltip label={record?.uniformFormDetails?.map(item => item.uniform?.name).join(', ')} target={`uniform-${record.id}`}>
            <EllipsisParagraph text={record?.uniformFormDetails?.map(item => item.uniform?.name).join(', ')} id={`uniform-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Số lượng',
        key: 'quantity',
        dataIndex: 'quantity',
        render: text => formatDecimalPrecision(text),
      },
      // {
      //   title: 'Còn lại',
      //   key: 'remaining',
      //   dataIndex: 'remaining',
      //   render: text => formatDecimalPrecision(text),
      // },
      {
        title: 'Mã phiếu xuất',
        key: 'code',
        dataIndex: 'code',
      },
      {
        title: 'Loại xuất',
        key: 'type',
        dataIndex: 'type',
        render: (text, record) => (
          <Tooltip label={uniformReleaseTypeMapping(text)} target={`type-${record.id}`}>
            <EllipsisParagraph text={uniformReleaseTypeMapping(text)} id={`type-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Chi phí',
        key: 'cost',
        dataIndex: 'cost',
        render: text => formatDecimalPrecision(text),
      },
      // {
      //   title: 'Đã hoàn ứng',
      //   key: 'isReturned',
      //   dataIndex: 'isReturned',
      //   render: text => <Input checked={text} readOnly type="checkbox" />,
      // },
    ];
  }, []);

  return columns;
};
