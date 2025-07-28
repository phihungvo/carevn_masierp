import BadgeV2 from 'app/components/badge/badge-v2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import { TableColumns } from 'app/components/table-v2/Table';
import Tooltip from 'app/components/tooltip/tooltip';
import { DATE_FORMAT } from 'app/constants/common';
import { ICustomer } from 'app/shared/model/customer.model';
import { IQualityCheckSample } from 'app/shared/model/production-quality-control.model';
import { convertCurrency } from 'app/shared/util/format';
import dayjs from 'dayjs';
import { useMemo } from 'react';
import { productionQualityStatusBadgeMapping } from '../production-quality-mapping';
import ActionsDropdown from './actions-dropdown';
import { PRODUCTION_QUALITY_STATUS } from 'app/shared/model/enumerations/production-quality-control.model';

export const generateColumns = (
  handleViewDetail: (id: string) => void,
  handleDelete: (id: string) => void,
  listCustomers: ICustomer[],
): TableColumns<IQualityCheckSample> => {
  const mapCustomerName = (id: string): string => {
    const customer = listCustomers?.find(item => item?.id === id);
    return customer?.companyName || '';
  };

  const columns: TableColumns<IQualityCheckSample> = useMemo(() => {
    return [
      {
        header: { render: 'Ngày lấy mẫu' },
        body: {
          render: ({ data }) => (
            <p
              className="attachment-link"
              onClick={() => handleViewDetail(data.id)}
            >
              {dayjs(data?.samplingDate).format(DATE_FORMAT.DATE)}
            </p>
          ),
        },
      },
      {
        header: { render: 'Mã mẫu' },
        body: {
          render: ({ data }) => (
            <Tooltip label={data?.sampleNo} target={`sampleNo-${data.id}`}>
              <EllipsisParagraph
                text={data?.sampleNo}
                width={150}
                id={`sampleNo-${data.id}`}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Lệnh sản xuất' },
        body: {
          render: ({ data }) => {
            const label = data?.manufactureOrder?.name;
            return (
              <Tooltip label={label} target={`manufactureOrder-${data.id}`}>
                <EllipsisParagraph
                  text={label}
                  width={150}
                  id={`manufactureOrder-${data.id}`}
                />
              </Tooltip>
            );
          },
        },
      },
      {
        header: { render: 'Loại hàng' },
        body: {
          render: ({ data }) => (
            <Tooltip
              label={data?.productType}
              target={`productType-${data.id}`}
            >
              <EllipsisParagraph
                text={data?.productType}
                width={150}
                id={`productType-${data.id}`}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Số lượng mẫu/khối lượng' },
        body: {
          render: ({ data }) => {
            const label = convertCurrency(data?.sampleWeight);
            return (
              <Tooltip label={label} target={`sampleWeight-${data.id}`}>
                <EllipsisParagraph
                  text={label}
                  width={150}
                  id={`sampleWeight-${data.id}`}
                />
              </Tooltip>
            );
          },
        },
      },
      {
        header: { render: 'Khách hàng' },
        body: {
          render: ({ data }) => {
            const label = mapCustomerName(data?.customer);
            return (
              <Tooltip label={label} target={`customer-${data.id}`}>
                <EllipsisParagraph
                  text={label}
                  width={250}
                  id={`customer-${data.id}`}
                />
              </Tooltip>
            );
          },
        },
      },
      {
        header: { render: 'Lý do' },
        body: {
          render: ({ data }) => (
            <Tooltip label={data?.reason} target={`reason-${data.id}`}>
              <EllipsisParagraph
                text={data?.reason}
                width={150}
                id={`reason-${data.id}`}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Trạng thái' },
        body: {
          render: ({ data }) => {
            return data?.itemId && data?.proteinPercentageApply ? (
              <BadgeV2 className="bv2 pr-approved">Đã có kết quả</BadgeV2>
            ) : (
              <BadgeV2 className="bv2 pr-new">Chờ kết quả</BadgeV2>
            );
          },
        },
      },
      {
        header: { render: 'Trạng thái đơn hủy mẫu' },
        body: {
          render: ({ data }) =>
            productionQualityStatusBadgeMapping(data?.status),
        },
      },
      {
        header: { render: 'Lý do từ chối' },
        body: {
          render: ({ data }) => {
            const rejectNote =
              data?.status === PRODUCTION_QUALITY_STATUS.DISPOSED ||
              data?.status === PRODUCTION_QUALITY_STATUS.REJECTED
                ? ''
                : data?.disposal?.reviewerNote || '';
            return (
              <Tooltip label={rejectNote} target={`reasonReject-${data.id}`}>
                <EllipsisParagraph
                  text={rejectNote}
                  width={150}
                  id={`reasonReject-${data.id}`}
                />
              </Tooltip>
            );
          },
        },
      },
      {
        header: { render: 'Thao tác' },
        body: {
          render: ({ data }) => (
            <ActionsDropdown record={data} handleDelete={handleDelete} />
          ),
        },
      },
    ];
  }, [listCustomers]);

  return columns;
};
