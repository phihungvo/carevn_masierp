import { ColumnsTypes } from 'app/components/table/table.d';
import { DATE_FORMAT } from 'app/constants/common';
import { IIncomingInvoice } from 'app/shared/model/incoming-invoice.model';
import dayjs from 'dayjs';
import React, { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';

import useEmployee from 'app/hooks/use-employee';
import useWorkspace from 'app/hooks/use-workspace';
import { invoiceTypeMapping } from './incoming-invoice-maping';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';

const { useGetEmployeesQuery } = useEmployee;
const { useGetWorkspacesQuery } = useWorkspace;

export const generateColumns = (
  toggleDetail: () => void,
  toggleUpdate: () => void,
  toggleDelete: () => void,
  togglePropose: () => void,
  toggleApprove: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<IIncomingInvoice> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const { data: employees } = useGetEmployeesQuery();
  const { data: workspaces } = useGetWorkspacesQuery();

  const columns: ColumnsTypes<IIncomingInvoice> = useMemo(() => {
    return [
      {
        title: 'Mã hóa đơn',
        key: 'invoiceNo',
        dataIndex: 'invoiceNo',
        render: (text, record) => (
          <Tooltip label={text} target={`invoiceNo-${record.id}`}>
            <EllipsisParagraph
              text={
                <p
                  className="attachment-link"
                  onClick={() => handleDetail(record.id)}
                >
                  {text}
                </p>
              }
              width={130}
              id={`invoiceNo-${record.id}`}
            />
          </Tooltip>
        ),
      },
      {
        title: 'Ngày',
        key: 'invoiceDate',
        dataIndex: 'invoiceDate',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        title: 'Ngày PS',
        key: 'invoiceDate',
        dataIndex: 'invoiceDate',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        title: 'Nhân viên',
        key: 'employeeId',
        dataIndex: 'employeeId',
        render: (text, record) => {
          const employee = employees?.data.find(e => e.id === text);
          return (
            <Tooltip
              label={
                employee ? `${employee.firstName} ${employee.lastName}` : ''
              }
              target={`employeeId-${record.id}`}
            >
              <EllipsisParagraph
                text={
                  employee ? `${employee.firstName} ${employee.lastName}` : ''
                }
                id={`employeeId-${record.id}`}
              />
            </Tooltip>
          );
        },
      },
      {
        title: 'MST',
        key: 'totalAmount',
        dataIndex: 'totalAmount',
        render: (text, record) => (
          <Tooltip
            label={text.toLocaleString('vi-VN', {
              style: 'currency',
              currency: 'VND',
            })}
            target={`totalAmount-${record.id}`}
          >
            <EllipsisParagraph
              text={text.toLocaleString('vi-VN', {
                style: 'currency',
                currency: 'VND',
              })}
              id={`totalAmount-${record.id}`}
            />
          </Tooltip>
        ),
      },
      {
        title: 'Số tiền',
        key: 'totalAmount',
        dataIndex: 'totalAmount',
        render: (text, record) => (
          <Tooltip
            label={text.toLocaleString('vi-VN', {
              style: 'currency',
              currency: 'VND',
            })}
            target={`totalAmount-${record.id}`}
          >
            <EllipsisParagraph
              text={text.toLocaleString('vi-VN', {
                style: 'currency',
                currency: 'VND',
              })}
              id={`totalAmount-${record.id}`}
            />
          </Tooltip>
        ),
      },
      {
        title: 'Thuế VAT',
        key: 'totalAmount',
        dataIndex: 'totalAmount',
        render: (text, record) => (
          <Tooltip
            label={text.toLocaleString('vi-VN', {
              style: 'currency',
              currency: 'VND',
            })}
            target={`totalAmount-${record.id}`}
          >
            <EllipsisParagraph
              text={text.toLocaleString('vi-VN', {
                style: 'currency',
                currency: 'VND',
              })}
              id={`totalAmount-${record.id}`}
            />
          </Tooltip>
        ),
      },
      {
        title: 'Tổng tiền',
        key: 'totalAmount',
        dataIndex: 'totalAmount',
        render: (text, record) => (
          <Tooltip
            label={text.toLocaleString('vi-VN', {
              style: 'currency',
              currency: 'VND',
            })}
            target={`totalAmount-${record.id}`}
          >
            <EllipsisParagraph
              text={text.toLocaleString('vi-VN', {
                style: 'currency',
                currency: 'VND',
              })}
              id={`totalAmount-${record.id}`}
            />
          </Tooltip>
        ),
      },
      {
        title: 'Thanh toán',
        key: 'totalAmount',
        dataIndex: 'totalAmount',
        render: (text, record) => (
          <Tooltip
            label={text.toLocaleString('vi-VN', {
              style: 'currency',
              currency: 'VND',
            })}
            target={`totalAmount-${record.id}`}
          >
            <EllipsisParagraph
              text={text.toLocaleString('vi-VN', {
                style: 'currency',
                currency: 'VND',
              })}
              id={`totalAmount-${record.id}`}
            />
          </Tooltip>
        ),
      },
      {
        title: 'Còn lại',
        key: 'totalAmount',
        dataIndex: 'totalAmount',
        render: (text, record) => (
          <Tooltip
            label={text.toLocaleString('vi-VN', {
              style: 'currency',
              currency: 'VND',
            })}
            target={`totalAmount-${record.id}`}
          >
            <EllipsisParagraph
              text={text.toLocaleString('vi-VN', {
                style: 'currency',
                currency: 'VND',
              })}
              id={`totalAmount-${record.id}`}
            />
          </Tooltip>
        ),
      },
      {
        title: 'Thông tin',
        key: 'totalAmount',
        dataIndex: 'totalAmount',
        render: (text, record) => (
          <Tooltip
            label={text.toLocaleString('vi-VN', {
              style: 'currency',
              currency: 'VND',
            })}
            target={`totalAmount-${record.id}`}
          >
            <EllipsisParagraph
              text={text.toLocaleString('vi-VN', {
                style: 'currency',
                currency: 'VND',
              })}
              id={`totalAmount-${record.id}`}
            />
          </Tooltip>
        ),
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
            record={record}
            setSelectedRecord={setSelectedRecord}
          />
        ),
      },
    ];
  }, [employees, workspaces]);

  return columns;
};
