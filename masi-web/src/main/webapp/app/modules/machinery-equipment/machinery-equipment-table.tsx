import BadgeV2 from 'app/components/badge/badge-v2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import Tooltip from 'app/components/tooltip/tooltip';
import { useAppSelector } from 'app/config/store';
import { DATE_FORMAT, DEFAULT_PAGE, ICON_PATH, isHasPermission } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { useDebounce } from 'app/hooks/use-debounce';
import { IItem, IItemParams } from 'app/shared/model/item.model';
import React from 'react';
import { useNavigate } from 'react-router';
import useInventoriesStorage from 'app/hooks/use-inventories-storage';
import { IInventoriesStorage } from 'app/shared/model/transfer-assets.model';
import { convertCurrency } from 'app/shared/util/format';
import dayjs from 'dayjs';
import machineryEquipmentMapping from './machinery-equipment-mapping';
import { permissions } from 'app/config/permission';

const { machineryEquipmentStatusBadgeMapping } = machineryEquipmentMapping;
const {
  useGetInventoriesStorageQuery,
  useGetDepreciationInventoriesStorageQuery,
} = useInventoriesStorage;

interface IDocumentaryTableProps {
  toggleUpdate: () => void;
  searchText: string;
  filter: IItemParams;
  setFilter: React.Dispatch<React.SetStateAction<IItemParams>>;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  selectedRows: IItem[];
  setSelectedRows: React.Dispatch<React.SetStateAction<IItem[]>>;
}

export default function MachineryEquipmentTable({
  toggleUpdate,
  searchText,
  filter,
  setFilter,
  setSelectedRecord,
}: IDocumentaryTableProps) {
  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const searchDebounce = useDebounce(searchText, 500);

  const navigate = useNavigate();

  const account = useAppSelector(state => state.authentication.account);

  const { data, isLoading } = useGetDepreciationInventoriesStorageQuery({
    ...filter,
    'code.contains': searchDebounce,
    'userPosition.equals': 'FACTORY'
  });

  const cols: TableColumns<IInventoriesStorage> = [
    {
      header: {
        render: 'Mã thiết bị',
      },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.code} target={`code-${data?.id}`}>
            <EllipsisParagraph
              text={data?.code}
              width={150}
              id={`code-${data?.id}`}
              className="attachment-link"
              onClick={() => {
                if (!isHasPermission(authorities, 'TECHNICAL_MACHINERY_EQUIPMENT.VIEW')) return
                navigate(
                  PATH.MACHINERY_EQUIPMENT_UPDATE.replace(':id', data?.id),
                )
              }}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Tên tài sản',
      },
      body: {
        render: ({ data }) => {
          return (
            <Tooltip label={data?.item?.name} target={`name-${data?.id}`}>
              <EllipsisParagraph
                text={data?.item?.name}
                width={200}
                id={`name-${data?.id}`}
              />
            </Tooltip>
          );
        },
      },
    },
    {
      header: {
        render: 'Loại',
      },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={`${data?.item?.itemCategory?.code} - ${data?.item?.itemCategory?.name}`}
            target={`type-${data?.id}`}
          >
            <EllipsisParagraph
              text={`${data?.item?.itemCategory?.code} - ${data?.item?.itemCategory?.name}`}
              width={200}
              id={`type-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Nguyên giá',
      },
      body: {
        render: ({ data }) => (
          <Tooltip label={`${data?.price}`} target={`type-${data?.id}`}>
            <EllipsisParagraph
              text={`${data?.price ? convertCurrency(data?.price ?? 0) : ''}`}
              width={200}
              id={`type-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Ghi chú',
      },
      body: {
        render: ({ data }) => (
          <Tooltip label={`${data?.notes ?? ''}`} target={`type-${data?.id}`}>
            <EllipsisParagraph
              text={`${data?.notes ?? ''}`}
              width={200}
              id={`type-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Ngày vận hành',
      },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={`${data?.attribute?.general?.operationDate
                ? dayjs(data.attribute?.general?.operationDate).format(
                  DATE_FORMAT.DATE,
                )
                : ''
              }`}
            target={`type-${data?.id}`}
          >
            <EllipsisParagraph
              text={`${data?.attribute?.general?.operationDate
                  ? dayjs(data.attribute?.general?.operationDate).format(
                    DATE_FORMAT.DATE,
                  )
                  : ''
                }`}
              width={200}
              id={`type-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Trạng thái',
      },
      body: {
        render: ({ data }) =>
          machineryEquipmentStatusBadgeMapping(
            data?.attribute?.general?.status ?? 'NEW',
          ),
      },
    },
  ];

  const totalCount = data?.totalRecord || 0;

  const handlePageChange = (page: number) => {
    setFilter({
      ...filter,
      page: page,
    });
  };

  const handlePageSizeChange = (pageSize: number) => {
    setFilter({
      ...filter,
      page: DEFAULT_PAGE,
      size: pageSize,
    });
  };

  return (
    <TablePagination<any>
      table_id="items-table"
      data={data?.data || []}
      columns={cols}
      options={[
        { value: '10', label: '10 Dòng' },
        { value: '20', label: '20 Dòng' },
        { value: '30', label: '30 Dòng' },
      ]}
      total_pages={totalCount}
      itemsPerPage={filter?.size}
      handlePageClick={handlePageChange}
      handlePageSizeChange={handlePageSizeChange}
    />
  );
}
