import { useDebounce } from 'app/hooks/use-debounce';
import useFactoryLogistics from 'app/hooks/use-factory-logistics';
import {
  IFactoryLogistics,
  IFactoryLogisticsParams,
} from 'app/shared/model/factory-logistics.model';
import React from 'react';
import { generateColumns } from './generate-columns';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import { COMPANY } from 'app/shared/model/enumerations/company.model';
import factoriesMapping from './factories-mapping';
import Flex from 'app/components/flex/flex';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import { useNavigate } from 'react-router';
import { PATH } from 'app/constants/path';
import ActionsDropdown from './components/actions-dropdown';
import { DEFAULT_PAGE, isHasPermission } from 'app/constants/common';
import { useAppSelector } from 'app/config/store';

const icon_path = 'content/images/vuesax/linear/';

const { useGetFactoriesQuery } = useFactoryLogistics;
const { mapFactoriesActiveBadge } = factoriesMapping;

interface IDocumentaryTableProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  togglePropose: () => void;
  toggleApprove: () => void;
  toggleDispose: () => void;
  toggleActivate: () => void;
  searchText: string;
  filter: IFactoryLogisticsParams;
  setFilter: React.Dispatch<React.SetStateAction<IFactoryLogisticsParams>>;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  selectedRows: IFactoryLogistics[];
  setSelectedRows: React.Dispatch<React.SetStateAction<IFactoryLogistics[]>>;
}

export default function FactoriesTable({
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

  const { data } = useGetFactoriesQuery({
    ...filter,
    'code.contains': searchDebounce,
    'name.contains': searchDebounce,
  });

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const columns: TableColumns<IFactoryLogistics> = [
    {
      header: {
        render: 'Mã nhà máy',
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
                if (!isHasPermission(authorities, 'LOGISTICS_FACTORIES.EDIT')) return
                navigate(PATH.FACTORIES_UPDATE.replace(':id', data.id));
              }}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Tên nhà máy' },
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
      header: { render: 'Địa chỉ' },
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
      header: { render: 'Mã địa chỉ' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={[data?.attribute?.location?.x, data?.attribute?.location?.y]
              .filter(item => item === '0' || !!item)
              .join(',')}
            target={`attribute-${data.id}`}
          >
            <EllipsisParagraph
              text={[
                data?.attribute?.location?.x || 0,
                data?.attribute?.location?.y || 0,
              ]
                .filter(item => item === '0' || !!item)
                .join(',')}
              width={300}
              id={`attribute-${data.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Trực thuộc' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={data?.company === (COMPANY.MASI as string) ? 'Masi' : 'MMS'}
            target={`company-${data?.id}`}
          >
            <EllipsisParagraph
              text={data?.company === (COMPANY.MASI as string) ? 'Masi' : 'MMS'}
              width={200}
              id={`company-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Người quản lý' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={`${
              data?.employeeOwner?.employeeCode
                ? data?.employeeOwner?.employeeCode + ' - '
                : ''
            } ${data?.employeeOwner?.fullName || ''}`}
            target={`employeeOwner-${data?.id}`}
          >
            <EllipsisParagraph
              text={`${
                data?.employeeOwner?.employeeCode
                  ? data?.employeeOwner?.employeeCode + ' - '
                  : ''
              } ${data?.employeeOwner?.fullName || ''}`}
              width={300}
              id={`employeeOwner-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Ghi chú' },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.note} target={`note-${data?.id}`}>
            <EllipsisParagraph
              text={data?.note}
              width={120}
              id={`note-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Trạng thái' },
      body: { render: ({ data }) => mapFactoriesActiveBadge(data?.isActive) },
    },
    {
      header: { render: <></> },
      body: {
        render: ({ data }) => (
          <ActionsDropdown
            toggleDetail={toggleDetail}
            toggleUpdate={toggleUpdate}
            toggleDelete={toggleDelete}
            togglePropose={togglePropose}
            toggleApprove={toggleApprove}
            toggleDispose={toggleDispose}
            toggleActivate={toggleActivate}
            record={data}
            setSelectedRecord={setSelectedRecord}
          />
        ),
      },
    },
  ];

  const handlePageChange = (page: number) => {
    setFilter({
      ...filter,
      page,
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
    <>
      <TablePagination<IFactoryLogistics>
        table_id="factories"
        columns={columns}
        data={data?.data || []}
        options={[
          { value: '10', label: '10 Dòng' },
          { value: '20', label: '20 Dòng' },
          { value: '30', label: '30 Dòng' },
        ]}
        total_pages={data?.totalRecord || 0}
        itemsPerPage={filter?.size || 10}
        handlePageClick={handlePageChange}
        handlePageSizeChange={handlePageSizeChange}
      />
    </>
  );
}
