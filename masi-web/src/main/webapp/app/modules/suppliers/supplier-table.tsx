import Table from 'app/components/table/table';
import { useAppSelector } from 'app/config/store';
import { DATE_FORMAT, DEFAULT_PAGE, ICON_PATH, isHasPermission } from 'app/constants/common';
import { useDebounce } from 'app/hooks/use-debounce';
import useSupplier from 'app/hooks/use-supplier';
import { ISupplier, ISupplierParams } from 'app/shared/model/supplier.model';
import React, { useEffect } from 'react';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import dayjs from 'dayjs';
import BadgeV2 from 'app/components/badge/badge-v2';
import { Flex } from 'antd';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import { useNavigate } from 'react-router';
import { PATH } from 'app/constants/path';
import ActionsDropdown from './components/actions-dropdown';
import AuthGuard from 'app/components/guards/auth-guard';

const { useGetSuppliers } = useSupplier;

interface ISupplierTable {
  filter: ISupplierParams;
  setFilter: React.Dispatch<React.SetStateAction<ISupplierParams>>;
  setSelectedRecord: (id: string) => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleDetail: () => void;
  toggleDispose: () => void;
  toggleActivate: () => void;
  searchText: string;
}

const SupplierTable = (props: ISupplierTable) => {
  const {
    filter,
    setFilter,
    toggleDelete,
    toggleUpdate,
    setSelectedRecord,
    searchText,
    toggleDetail,
    toggleDispose,
    toggleActivate,
  } = props;

  const navigate = useNavigate();

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const handleDetail = (id: string) => {
    navigate(PATH.SUPPLIERS_UPDATE.replace(':id', id));
  };

  const handleUpdate = (id: string) => {
    navigate(PATH.SUPPLIERS_UPDATE.replace(':id', id));
  };

  const handleDelete = (id: string) => {
    toggleDelete();
    setSelectedRecord(id);
  };

  const columns: TableColumns<ISupplier> = [
    {
      header: {
        render: 'Mã NCC',
      },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.code} target={`code-${data?.id}`}>
            <EllipsisParagraph
              text={data?.code}
              width={200}
              id={`code-${data?.id}`}
              className="attachment-link"
              onClick={() => {
                if (!isHasPermission(authorities, 'LOGISTICS_SUPPLIERS.EDIT')) return
                handleDetail(data?.id)
              }}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Tên NCC',
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
        render: 'Địa chỉ',
      },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.address} target={`address-${data?.id}`}>
            <EllipsisParagraph
              text={data?.address}
              width={200}
              id={`address-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'MST',
      },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.taxCode} target={`taxCode-${data?.id}`}>
            <EllipsisParagraph
              text={data?.taxCode}
              width={200}
              id={`taxCode-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Người đại diện',
      },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.fullName} target={`representative-${data?.id}`}>
            <EllipsisParagraph
              text={data?.fullName}
              width={200}
              id={`representative-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Tel',
      },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.phone} target={`phone-${data?.id}`}>
            <EllipsisParagraph
              text={data?.phone}
              width={200}
              id={`phone-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Email',
      },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.email} target={`email-${data?.id}`}>
            <EllipsisParagraph
              text={data?.email}
              width={200}
              id={`email-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Hạn thanh toán',
      },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={data?.paymentTermText}
            target={`paymentTermNumber-${data?.id}`}
          >
            <EllipsisParagraph
              text={data?.paymentTermText}
              width={200}
              id={`paymentTermNumber-${data?.id}`}
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
        render: ({ data }) => (
          <BadgeV2 color={data?.isActive ? 'success' : 'gray'}>
            {data?.isActive ? 'Hoạt động' : 'Huỷ'}
          </BadgeV2>
        ),
      },
    },
    {
      header: { render: '' },
      body: {
        render: ({ data }) => (
          <Flex align="center">
            <AuthGuard permissionKey='LOGISTICS_SUPPLIERS.EDIT'>
              <ButtonV2
                variant="text"
                isBoxShadow={false}
                onClick={() => handleUpdate(data?.id)}
              >
                <img src={ICON_PATH + 'edit-3.svg'} alt="edit" />
              </ButtonV2>
            </AuthGuard>

            <ActionsDropdown
              record={data}
              toggleDelete={toggleDelete}
              toggleUpdate={toggleUpdate}
              setSelectedRecord={setSelectedRecord}
              toggleDetail={toggleDetail}
              toggleDispose={toggleDispose}
              toggleActivate={toggleActivate}
            />
          </Flex>
        ),
      },
    },
  ];

  const account = useAppSelector(state => state.authentication.account);

  const { data } = useGetSuppliers({
    ...filter,
    companyId: account?.authorities?.includes('ROLE_SUPER_ADMIN')
      ? undefined
      : account?.companyId,
  });

  const totalCount = data?.totalRecord || 0;
  const search = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, search, page: DEFAULT_PAGE }));
  }, [search]);

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
    <TablePagination<ISupplier>
      table_id="supplier-table"
      columns={columns}
      data={data?.data || []}
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
};

export default SupplierTable;
