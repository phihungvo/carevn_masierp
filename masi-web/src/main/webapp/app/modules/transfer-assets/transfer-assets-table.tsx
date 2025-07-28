import BadgeV2 from 'app/components/badge/badge-v2';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import Tooltip from 'app/components/tooltip/tooltip';
import { useAppSelector } from 'app/config/store';
import { DATE_FORMAT, DEFAULT_PAGE, ICON_PATH } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { useDebounce } from 'app/hooks/use-debounce';
import useTransferAssets from 'app/hooks/use-transfer-assets';
import { ITransferAssets, ITransferAssetsParams } from 'app/shared/model/transfer-assets.model';
import React from 'react';
import { useNavigate } from 'react-router';
import ActionsDropdown from './components/actions-dropdown';
import dayjs from 'dayjs';
import { mapTransferAssetsStatusBadge } from './transfer-assets-mapping';

const { useGetTransferAssetsQuery } = useTransferAssets;

interface IDocumentaryTableProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  togglePropose: () => void;
  toggleApprove: () => void;
  toggleDispose: () => void;
  toggleActivate: () => void;
  searchText: string;
  filter: ITransferAssetsParams;
  setFilter: React.Dispatch<React.SetStateAction<ITransferAssetsParams>>;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  selectedRows: ITransferAssets[];
  setSelectedRows: React.Dispatch<React.SetStateAction<ITransferAssets[]>>;
}

export default function TransferAssetssTable({
  togglePropose,
  toggleApprove,
  searchText,
  filter,
  setFilter,
  setSelectedRecord,
  toggleDispose,
  toggleActivate,
}: IDocumentaryTableProps) {
  const searchDebounce = useDebounce(searchText, 500);

  const navigate = useNavigate();

  const account = useAppSelector(state => state.authentication.account);

  const { data, isLoading } = useGetTransferAssetsQuery({
    ...filter,
    "search": searchDebounce,
  });

  const cols: TableColumns<ITransferAssets> = [
    {
      header: {
        render: 'Mã điều chyển',
      },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.code} target={`code-${data?.id}`}>
            <EllipsisParagraph
              text={data?.code}
              width={150}
              id={`code-${data?.id}`}
              className="attachment-link"
              onClick={() =>
                navigate(PATH.TRANSFER_ASSETS_DETAIL.replace(':id', data?.id))
              }
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Ngày xuất',
      },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={`${dayjs(data.transferDate).format(
                DATE_FORMAT.DATE,
              )}`}
            target={`transferDate-${data.id}`}
          >
            <EllipsisParagraph
              text={`${dayjs(data.transferDate).format(
                DATE_FORMAT.DATE,
              )}`}
              width={100}
              id={`transferDate-${data.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Loại PS',
      },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={ data?.transactionType?.name ? `${data?.transactionType?.code} - ${data?.transactionType?.name}` : ""}
            target={`transaction-type-${data?.transactionTypeId}`}
          >
            <EllipsisParagraph
              text={ data?.transactionType?.name ? `${data?.transactionType?.code} - ${data?.transactionType?.name}` : ""}
              width={200}
              id={`transaction-type-${data?.transactionTypeId}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Diễn giải',
      },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={`${data?.description ?? ''}`}
            target={`type-${data?.id}`}
          >
            <EllipsisParagraph
              text={`${data?.description ?? ''}`}
              width={200}
              id={`type-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Trạng thái' },
      body: {
        render: ({ data }) => mapTransferAssetsStatusBadge(data?.status ?? '')
      },
    },
    {
      header: {
        render: '',
      },
      body: {
        render: ({ data }) => (
          <Flex align="center">
            <ActionsDropdown
              record={data}
              togglePropose={togglePropose}
              toggleApprove={toggleApprove}
              toggleDispose={toggleDispose}
              toggleActivate={toggleActivate}
              setSelectedRecord={setSelectedRecord}
            />
          </Flex>
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
    <TablePagination<ITransferAssets>
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
