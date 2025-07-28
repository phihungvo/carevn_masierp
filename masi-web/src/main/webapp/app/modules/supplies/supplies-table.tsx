import BadgeV2 from 'app/components/badge/badge-v2';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import Tooltip from 'app/components/tooltip/tooltip';
import { useAppSelector } from 'app/config/store';
import { DEFAULT_PAGE, ICON_PATH, isHasPermission } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { useDebounce } from 'app/hooks/use-debounce';
import useItems from 'app/hooks/use-items';
import { IItem, IItemParams } from 'app/shared/model/item.model';
import React from 'react';
import { useNavigate } from 'react-router';
import ActionsDropdown from './components/actions-dropdown';
import AuthGuard from 'app/components/guards/auth-guard';

const { useGetItemsQuery } = useItems;

interface IDocumentaryTableProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  togglePropose: () => void;
  toggleApprove: () => void;
  toggleDispose: () => void;
  toggleActivate: () => void;
  searchText: string;
  filter: IItemParams;
  setFilter: React.Dispatch<React.SetStateAction<IItemParams>>;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  selectedRows: IItem[];
  setSelectedRows: React.Dispatch<React.SetStateAction<IItem[]>>;
}

export default function ItemsTable({
  toggleDetail,
  toggleUpdate,
  toggleDelete,
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

  const { data, isLoading } = useGetItemsQuery({
    ...filter,
    search: searchDebounce,
  });

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const cols: TableColumns<IItem> = [
    {
      header: {
        render: 'Mã hàng hoá',
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
                if (!isHasPermission(authorities, 'LOGISTICS_SUPPLIES.VIEW')) return;
                navigate(PATH.SUPPLIES_UPDATE.replace(':id', data?.id))
              }}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Tên hàng hoá',
      },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.name} target={`name-${data?.id}`}>
            <EllipsisParagraph
              text={data?.name}
              width={200}
              id={`name-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Nhóm',
      },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={`${data?.itemCategory?.code} - ${data?.itemCategory?.name}`}
            target={`category-${data?.id}`}
          >
            <EllipsisParagraph
              text={`${data?.itemCategory?.code} - ${data?.itemCategory?.name}`}
              width={200}
              id={`category-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Loại',
      },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={`${data?.itemTypes?.code} - ${data?.itemTypes?.name}`}
            target={`type-${data?.id}`}
          >
            <EllipsisParagraph
              text={`${data?.itemTypes?.code} - ${data?.itemTypes?.name}`}
              width={200}
              id={`type-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'ĐVT',
      },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.uom?.name} target={`uom-${data?.id}`}>
            <EllipsisParagraph
              text={data?.uom?.name}
              width={100}
              id={`uom-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Đơn giá',
      },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={data?.unitPrice?.toString()}
            target={`unitPrice-${data?.id}`}
          >
            <EllipsisParagraph
              text={data?.unitPrice}
              width={100}
              id={`unitPrice-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    // {
    //   header: {
    //     render: 'Trạng thái',
    //   },
    //   body: {
    //     render: ({ data }) => (
    //       <BadgeV2 color={data?.isActive ? 'success' : 'gray'}>
    //         {data?.isActive ? 'Hoạt động' : 'Hủy'}
    //       </BadgeV2>
    //     ),
    //   },
    // },
    // {
    //   header: {
    //     render: '',
    //   },
    //   body: {
    //     render: ({ data }) => (
    //       <Flex align="center">
    //         <AuthGuard permissionKey='LOGISTICS_SUPPLIES.EDIT'>
    //           <ButtonV2
    //             variant="text"
    //             isBoxShadow={false}
    //             onClick={() =>
    //               navigate(PATH.SUPPLIES_UPDATE.replace(':id', data?.id))
    //             }
    //           >
    //             <img src={ICON_PATH + 'edit-3.svg'} alt="edit" />
    //           </ButtonV2>
    //         </AuthGuard>

    //         <ActionsDropdown
    //           record={data}
    //           toggleDetail={toggleDetail}
    //           toggleUpdate={toggleUpdate}
    //           toggleDelete={toggleDelete}
    //           togglePropose={togglePropose}
    //           toggleApprove={toggleApprove}
    //           toggleDispose={toggleDispose}
    //           toggleActivate={toggleActivate}
    //           setSelectedRecord={setSelectedRecord}
    //         />
    //       </Flex>
    //     ),
    //   },
    // },
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
    <TablePagination<IItem>
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
