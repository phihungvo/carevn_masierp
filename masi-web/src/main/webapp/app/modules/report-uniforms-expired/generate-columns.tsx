import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import { ColumnsTypes } from 'app/components/table/table.d';
import Tooltip from "app/components/tooltip/tooltip";
import { DATE_FORMAT } from 'app/constants/common';
import { IUniformExpiring } from 'app/shared/model/report.model';
import dayjs from 'dayjs';
import { useMemo } from 'react';
import ActionsDropdown from './actions-dropdown';

export const generateColumns = (
  toggleModalHistory: () => void,
  setEmployeeIds: (id: string) => void
): ColumnsTypes<IUniformExpiring> => {
  const columns: ColumnsTypes<IUniformExpiring> = useMemo(() => {

    const handleShowModalHistory = (id: string) => {
      toggleModalHistory()
      setEmployeeIds(id)
    }

    return [
      {
        title: 'Mã nhân viên',
        key: 'employeeCode',
        dataIndex: 'employeeCode',
        render: (text, record) => (
          <Tooltip label={text} target={`employeeCode-${record?.employeeId}`}>
            <EllipsisParagraph text={text} width={100} id={`employeeCode-${record?.employeeId}`} />
          </Tooltip>
        )
      },
      {
        title: 'Tên nhân viên',
        key: 'fullName',
        dataIndex: 'fullName',
        render: (text, record) => (
          <Tooltip label={text} target={`fullName-${record?.employeeId}`}>
            <EllipsisParagraph text={text} id={`fullName-${record?.employeeId}`} />
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
        title: 'Ngày hết hạn',
        key: 'expired_date',
        dataIndex: 'expired_date',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      // {
      //   title: 'Trạng thái',
      //   key: 'type',
      //   dataIndex: 'type',
      //   render: text =>
      //     text !== 'UNKNOWN' && (
      //       <Badge color={reportUniformsExpiringTypeColorMapping(text)}>{reportUniformsExpiringTypeMapping(text)}</Badge>
      //     ),
      // },
      {
        key: 'actions',
        title: 'Thao tác',
        width: 106,
        render(_, record) {
          return (
            <ActionsDropdown
              handleShowModalHistory={handleShowModalHistory}
              record={record}
            />
          )
        }
      },
    ];
  }, []);

  return columns;
};
