import Badge from 'app/components/badge/badge';
import { ColumnsTypes } from 'app/components/table/table.d';
import React from 'react';
import { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import priceListMapping from './price-list-mapping';
import { IQuotation } from 'app/shared/model/quotation.model';
import { useNavigate } from 'react-router';
import { useAppSelector } from 'app/config/store';
import { checkCompanyDetailPath } from './util/check-company-path';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';

const { priceListMappingColors, priceListMappingText } = priceListMapping;

export const generateColumns = (
  toggleDelete: () => void,
  toggleInApprove: () => void,
  toggleCancel: () => void,
  toggleCusSend: () => void,
  toggleApproveInternal: () => void,
  toggleApprove: () => void,
  toggleReject: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<IQuotation> => {
  const navigate = useNavigate();
  const account = useAppSelector(state => state.authentication.account);

  const handleDetail = (id: string) => {
    navigate(`${checkCompanyDetailPath(account?.company?.normalizedName, id)}`);
  };

  const columns: ColumnsTypes<IQuotation> = useMemo(() => {
    return [
      {
        title: 'Tên bảng báo giá',
        key: 'name',
        dataIndex: 'name',
        render: (text, record) => (
          <Tooltip label={text} target={`name-${record.id}`}>
            <p className="attachment-link" onClick={() => handleDetail(record?.id)}>
              <EllipsisParagraph text={text} id={`name-${record.id}`} />
            </p>
          </Tooltip>
        )
      },
      {
        title: 'Khách hàng',
        key: 'customer',
        dataIndex: 'customer',
        render: (text, record) => (
          <Tooltip label={record?.customer?.companyName} target={`customer-${record.id}`}>
            <EllipsisParagraph text={record?.customer?.companyName} width={300} id={`customer-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Trạng thái',
        key: 'status',
        dataIndex: 'status',
        render: text => <Badge color={priceListMappingColors(text)}>{priceListMappingText(text)}</Badge>,
      },
      {
        title: 'Thao tác',
        key: 'action',
        width: 106,
        dataIndex: 'action',
        render: (_, record) => (
          <ActionsDropdown
            toggleDelete={toggleDelete}
            toggleInApprove={toggleInApprove}
            toggleCancel={toggleCancel}
            toggleCusSend={toggleCusSend}
            toggleApproveInternal={toggleApproveInternal}
            toggleApprove={toggleApprove}
            toggleReject={toggleReject}
            record={record}
            setSelectedRecord={setSelectedRecord}
          />
        ),
      },
    ];
  }, []);

  return columns;
};
