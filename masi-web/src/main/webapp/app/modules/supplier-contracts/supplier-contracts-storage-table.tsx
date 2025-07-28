import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import Tooltip from 'app/components/tooltip/tooltip';
import { DATE_FORMAT, DEFAULT_PAGE, ICON_PATH, isHasPermission } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { useGetSupplierContracts } from 'app/hooks/use-supplier-contract';
import { ISupplierContract } from 'app/shared/model/supplier-contract.model';
import { convertCurrency } from 'app/shared/util/format';
import dayjs from 'dayjs';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import ActionsDropdown from './components/actions-dropdown';
import { SupplierContractsDeliveryStatusBadgeMapping, SupplierContractsStatusBadgeMapping } from './supplier-contracts-mapping';
import { SupplierContractsContext } from './supplier-contracts-storage-provider';
import { useAppSelector } from 'app/config/store';
import AuthGuard from 'app/components/guards/auth-guard';

const SupplierContractsTable = () => {
  const { filter, setFilter } = useContext(SupplierContractsContext);
  const navigate = useNavigate();

  const { data } = useGetSupplierContracts({ ...filter });

  const toUpdate = (id: string) => {
    navigate(PATH.SUPPLIER_CONTRACTS_UPDATE.replace(':id', id));
  };

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const columns: TableColumns<ISupplierContract> = [
    {
      header: { render: 'Mã HĐ' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={data?.contractCode}
            target={`contractCode-${data?.id}`}
          >
            <EllipsisParagraph
              text={data?.contractCode}
              width={200}
              id={`contractCode-${data?.id}`}
              className="attachment-link"
              onClick={() => {
                if (!isHasPermission(authorities, 'LOGISTICS_SUPPLIER_CONTRACTS.VIEW')) return;
                toUpdate(data?.id)
              }}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'NCC' },
      body: {
        render: ({ data }) => {
          const supplier = data?.supplier
            ? `${data?.supplier?.code} - ${data?.supplier?.name}`
            : '';
          return (
            <Tooltip label={supplier} target={`supplierId-${data?.id}`}>
              <EllipsisParagraph
                text={supplier}
                width={200}
                id={`supplierId-${data?.id}`}
              />
            </Tooltip>
          );
        },
      },
    },
    {
      header: { render: 'Địa chỉ NCC' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={data?.supplier?.address}
            target={`supplierAddress-${data?.id}`}
          >
            <EllipsisParagraph
              text={data?.supplier?.address}
              width={200}
              id={`supplierAddress-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Người đại diện' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={data?.supplierFullName}
            target={`supplierFullName-${data?.id}`}
          >
            <EllipsisParagraph
              text={data?.supplierFullName}
              width={200}
              id={`supplierFullName-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Ngày HĐ' },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.startDate} target={`startDate-${data?.id}`}>
            <EllipsisParagraph
              text={dayjs(data?.startDate).format(DATE_FORMAT.DATE)}
              width={200}
              id={`startDate-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Ngày hết hạn' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={dayjs(data?.endDate).format(DATE_FORMAT.DATE)}
            target={`endDate-${data?.id}`}
          >
            <EllipsisParagraph
              text={dayjs(data?.endDate).format(DATE_FORMAT.DATE)}
              width={200}
              id={`endDate-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Tổng giá trị' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={convertCurrency(data?.contractAmount, false)}
            target={`contractAmount-${data?.id}`}
          >
            <EllipsisParagraph
              text={convertCurrency(data?.contractAmount, false)}
              width={200}
              id={`contractAmount-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Trạng thái' },
      body: {
        render: ({ data }) => SupplierContractsStatusBadgeMapping(data?.status as any),
      },
    },
    {
      header: { render: 'Trạng thái giao hàng' },
      body: {
        render: ({ data }) => SupplierContractsDeliveryStatusBadgeMapping(data?.deliveryStatus ?? ''),
      },
    },
    {
      header: { render: '' },
      body: {
        render: ({ data }) => (
          <Flex align="center">
            <AuthGuard permissionKey='LOGISTICS_SUPPLIER_CONTRACTS.VIEW'>
              <ButtonV2
                variant="text"
                isBoxShadow={false}
                onClick={() => toUpdate(data?.id)}
              >
                <img src={ICON_PATH + 'edit-3.svg'} alt="edit" />
              </ButtonV2>
            </AuthGuard>

            <ActionsDropdown data={data} />
          </Flex>
        ),
      },
    },
  ];

  const handlePageChange = (page: number) => {
    setFilter({ ...filter, page: page });
  };

  const handlePageSizeChange = (pageSize: number) => {
    setFilter({ ...filter, page: DEFAULT_PAGE, size: pageSize });
  };

  return (
    <TablePagination<ISupplierContract>
      table_id="factories"
      columns={columns}
      data={data?.data || []}
      total_pages={data?.totalRecord || 0}
      itemsPerPage={filter?.size}
      handlePageClick={handlePageChange}
      handlePageSizeChange={handlePageSizeChange}
    />
  );
};

export default SupplierContractsTable;
