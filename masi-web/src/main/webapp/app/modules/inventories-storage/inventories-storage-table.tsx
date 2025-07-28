import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import Tooltip from 'app/components/tooltip/tooltip';
import { DATE_FORMAT, DEFAULT_PAGE, ICON_PATH, isHasPermission } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { useInventories } from 'app/hooks/use-inventories';
import { IInventoriesStorage } from 'app/shared/model/inventories-storage.model';
import { convertCurrency } from 'app/shared/util/format';
import dayjs from 'dayjs';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { useSearchParams } from 'react-router-dom';
import ActionsDropdown from './components/actions-dropdown';
import { InventoriesStorageStatusBadgeMapping } from './inventories-mapping';
import { InventoriesStorageContext } from './inventories-storage-provider';
import { useAppSelector } from 'app/config/store';
import AuthGuard from 'app/components/guards/auth-guard';

const InventoriesStorageTable = () => {
  const { filter, setFilter } = useContext(InventoriesStorageContext);
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const { data } = useInventories({
    ...filter,
    'warehouseGroupType.equals': warehouseImportType,
  });

  const toUpdate = (id: string) => {
    navigate(
      PATH.INVENTORIES_STORAGE_UPDATE.replace(':id', id) + location?.search,
    );
  };

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const columns: TableColumns<IInventoriesStorage> = [
    {
      header: { render: 'Mã phiếu' },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.code} target={`code-${data?.id}`}>
            <EllipsisParagraph
              text={data?.code}
              width={200}
              id={`code-${data?.id}`}
              className="attachment-link"
              onClick={() => {
                if (!isHasPermission(authorities, 'LOGISTICS_INVENTORIES_STORAGE.VIEW')) return;
                toUpdate(data?.id)
              }}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Ngày nhập kho' },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.dateCreate} target={`dateCreate-${data?.id}`}>
            <EllipsisParagraph
              text={dayjs(data?.dateCreate).format(DATE_FORMAT.DATE)}
              width={200}
              id={`dateCreate-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    // {
    //   header: { render: 'Loại kho' },
    //   body: {
    //     render: ({ data }) => (
    //       <Tooltip
    //         label={
    //           data?.inventoriesType
    //             ? `${data?.inventoriesType?.code} - ${data?.inventoriesType?.name}`
    //             : ''
    //         }
    //         target={`type-${data?.id}`}
    //       >
    //         <EllipsisParagraph
    //           text={
    //             data?.inventoriesType
    //               ? `${data?.inventoriesType?.code} - ${data?.inventoriesType?.name}`
    //               : ''
    //           }
    //           width={200}
    //           id={`type-${data?.id}`}
    //         />
    //       </Tooltip>
    //     ),
    //   },
    // },
    {
      header: { render: 'NCC' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={`${data?.customer?.code} - ${data?.customer?.name}`}
            target={`customerRecipient-${data?.id}`}
          >
            <EllipsisParagraph
              text={`${data?.customer?.code} - ${data?.customer?.name}`}
              width={200}
              id={`customerRecipient-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Kho nhập' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={`${data?.incomingWarehouse?.code} - ${data?.incomingWarehouse?.name}`}
            target={`incomingWarehouse-${data?.id}`}
          >
            <EllipsisParagraph
              text={data?.incomingWarehouse?.name}
              width={200}
              id={`incomingWarehouse-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Tổng SL' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={convertCurrency(data?.totalQuantity, false)}
            target={`quantity-${data?.id}`}
          >
            <EllipsisParagraph
              text={convertCurrency(data?.totalQuantity, false)}
              width={200}
              id={`quantity-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Tổng tiền' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={convertCurrency(data?.totalAmount ?? 0)}
            target={`totalAmount-${data?.id}`}
          >
            <EllipsisParagraph
              text={convertCurrency(data?.totalAmount ?? 0)}
              width={200}
              id={`totalAmount-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Hợp đồng' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={
              data?.purchaseContract
                ? [
                    data?.purchaseContract?.contractCode,
                    data?.purchaseContract?.contractName,
                  ].join(' - ')
                : ''
            }
            target={`purchaseContractId-${data?.id}`}
          >
            <EllipsisParagraph
              text={
                data?.purchaseContract
                  ? [
                      data?.purchaseContract?.contractCode,
                      data?.purchaseContract?.contractName,
                    ].join(' - ')
                  : ''
              }
              width={200}
              id={`purchaseContractId-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Trạng thái' },
      body: {
        render: ({ data }) =>
          InventoriesStorageStatusBadgeMapping(data?.status),
      },
    },
    {
      header: { render: '' },
      body: {
        render: ({ data }) => (
          <Flex align="center">
            <AuthGuard permissionKey='LOGISTICS_INVENTORIES_STORAGE.VIEW'>
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
    <TablePagination<IInventoriesStorage>
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

export default InventoriesStorageTable;
