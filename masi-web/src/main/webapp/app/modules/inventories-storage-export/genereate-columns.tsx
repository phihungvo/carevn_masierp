import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import { TableColumns } from 'app/components/table-v2/Table';
import Tooltip from 'app/components/tooltip/tooltip';
import { DATE_FORMAT, ICON_PATH, isHasPermission } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { IInventoriesStorage } from 'app/shared/model/inventories-storage.model';
import { convertCurrency } from 'app/shared/util/format';
import dayjs from 'dayjs';
import { useNavigate } from 'react-router';
import ActionsDropdown from './components/actions-dropdown';
import { InventoriesStorageExportStatusBadgeMapping } from './inventories-export-mapping';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';
import { IWorkspace } from 'app/shared/model/workspace.model';
import { useAppSelector } from 'app/config/store';
import AuthGuard from 'app/components/guards/auth-guard';
import { ICustomer } from 'app/shared/model/customer.model';

const columnsCommerceExport = (customers?: any) => {
  return [
  {
    header: { render: 'KH' },
    body: {
      render: ({ data }) => {
        const customer = customers?.data?.find(it => it.id === data?.customerId);
       return (<Tooltip
          label={`$${customer?.customerCode} - ${customer?.name}`}
          target={`customer-${customer?.id}`}
        >
          <EllipsisParagraph
            text={`${customer?.customerCode ?? ''} - ${customer?.companyName ?? ''}`}
            width={200}
            id={`customer-${customer?.id}`}
          />
        </Tooltip>)
      },
    },
  },
  {
    header: { render: 'Người nhận' },
    body: {
      render: ({ data }) => (
        <Tooltip
          label={`${data?.attribute?.receiverName}`}
          target={`receiverName-${data?.id}`}
        >
          <EllipsisParagraph
            text={data?.attribute?.receiverName}
            width={200}
            id={`receiverName-${data?.id}`}
          />
        </Tooltip>
      ),
    },
  },
  {
    header: { render: 'Địa chỉ' },
    body: {
      render: ({ data }) => (
        <Tooltip
          label={`${data?.attribute?.shipperAddress}`}
          target={`shipperAddress-${data?.id}`}
        >
          <EllipsisParagraph
            text={data?.attribute?.shipperAddress}
            width={200}
            id={`shipperAddress-${data?.id}`}
          />
        </Tooltip>
      ),
    },
  },
  {
    header: { render: 'Ghi chú' },
    body: {
      render: ({ data }) => (
        <Tooltip label={`${data?.note}`} target={`note-${data?.id}`}>
          <EllipsisParagraph
            text={data?.note}
            width={200}
            id={`note-${data?.id}`}
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
          label={convertCurrency(data?.totalAmount)}
          target={`totalAmount-${data?.id}`}
        >
          <EllipsisParagraph
            text={convertCurrency(data?.totalAmount)}
            width={200}
            id={`totalAmount-${data?.id}`}
          />
        </Tooltip>
      ),
    },
  },
]
};

const selectWorkspaceById = (workspaces: IWorkspace[], id: string) => {
  const wp = workspaces?.find(x => x.id === id);
  if (wp) return wp.name;
};

const columnsDepreciationExport = (workspaces: IWorkspace[]) =>
  [
    {
      header: { render: 'Nơi nhận' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={selectWorkspaceById(
              workspaces,
              data?.attribute?.workspaceId,
            )}
            target={`workspaceId-${data?.id}`}
          >
            <EllipsisParagraph
              text={selectWorkspaceById(
                workspaces,
                data?.attribute?.workspaceId,
              )}
              width={200}
              id={`workspaceId-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Số lượng' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={convertCurrency(data?.totalQuantity, false)}
            target={`totalQuantity-${data?.id}`}
          >
            <EllipsisParagraph
              text={convertCurrency(data?.totalQuantity, false)}
              width={100}
              id={`totalQuantity-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Thành tiền' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={convertCurrency(data?.totalAmount)}
            target={`totalAmount-${data?.id}`}
          >
            <EllipsisParagraph
              text={convertCurrency(data?.totalAmount)}
              width={200}
              id={`totalAmount-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Ghi chú' },
      body: {
        render: ({ data }) => (
          <Tooltip label={`${data?.note}`} target={`note-${data?.id}`}>
            <EllipsisParagraph
              text={data?.note}
              width={200}
              id={`note-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
  ];

export const generate_columns = (
  type: InventoriesWarehouse,
  workspaces: IWorkspace[],
  customers?: any[],
) => {
  let columnMid = columnsCommerceExport(customers);
  if (type === InventoriesWarehouse.WAREHOUSE_DEPRECIATION_EXPORT)
    columnMid = columnsDepreciationExport(workspaces);

  const navigate = useNavigate();

  const toUpdate = (id: string) => {
    navigate(
      PATH.INVENTORIES_STORAGE_EXPORT_UPDATE.replace(':id', id) +
        location?.search,
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
                // if (!isHasPermission(authorities, 'LOGISTICS_INVENTORIES_STORAGE.VIEW')) return;
                toUpdate(data?.id)
              }}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Ngày xuất' },
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
    {
      header: { render: 'Kho' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={
              data?.incomingWarehouse
                ? `${data?.incomingWarehouse?.code} - ${data?.incomingWarehouse?.name}`
                : ''
            }
            target={`warehouse-${data?.id}`}
          >
            <EllipsisParagraph
              text={
                data?.incomingWarehouse
                  ? `${data?.incomingWarehouse?.code} - ${data?.incomingWarehouse?.name}`
                  : ''
              }
              width={200}
              id={`warehouse-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    ...columnMid,
    {
      header: { render: 'Trạng thái' },
      body: {
        render: ({ data }) =>
          InventoriesStorageExportStatusBadgeMapping(data?.status),
      },
    },
    {
      header: { render: '' },
      body: {
        render: ({ data }) => (
          <Flex align="center">
           <AuthGuard permissionKey='LOGISTICS_INVENTORIES_STORAGE_EXPORT.EDIT'>
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

  return columns;
};
