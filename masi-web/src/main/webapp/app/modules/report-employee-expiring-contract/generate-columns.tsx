import { ColumnsTypes } from 'app/components/table/table.d';
import { DATE_FORMAT } from 'app/constants/common';
import { IEmployeeExpiringContract } from 'app/shared/model/report.model';
import dayjs from 'dayjs';
import { useMemo } from 'react';
import recruitmentMapping from '../recruitment/recruitment-mapping';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import React from 'react';

const { recruitmentPositionTextMapping } = recruitmentMapping;

export const generateColumns = (): ColumnsTypes<IEmployeeExpiringContract> => {
  const columns: ColumnsTypes<IEmployeeExpiringContract> = useMemo(() => {
    return [
      {
        title: 'Mã nhân viên',
        key: 'employeeCode',
        dataIndex: 'employeeCode',
        render: (text, record) => (
          <Tooltip label={text} target={`employeeCode-${record.id}`}>
            <EllipsisParagraph text={text} id={`employeeCode-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Tên nhân viên',
        key: 'fullName',
        dataIndex: 'fullName',
        render: (text, record) => (
          <Tooltip label={text} target={`fullName-${record.id}`}>
            <EllipsisParagraph text={text} id={`fullName-${record.id}`} />
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
        title: 'Ngày bắt đầu làm việc',
        key: 'startWorkDate',
        dataIndex: 'startWorkDate',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        title: 'Số HĐ',
        key: 'contractNumber',
        dataIndex: 'contractNumber',
      },
      {
        title: 'Ngày HĐ',
        key: 'contractDate',
        dataIndex: 'contractDate',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        title: 'Ngày hết hạn HĐ',
        key: 'contractEndDate',
        dataIndex: 'contractEndDate',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        title: 'Thời hạn',
        key: 'contractTerm',
        dataIndex: 'contractTerm',
      },
    ];
  }, []);

  return columns;
};
