import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import Tooltip from 'app/components/tooltip/tooltip';
import { DATE_FORMAT, DEFAULT_PAGE, ICON_PATH, isHasPermission } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { useGetStocktaking } from 'app/hooks/use-stocktaking';
import { IStocktaking } from 'app/shared/model/stocktaking.model';
import dayjs from 'dayjs';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import ActionsDropdown from './components/actions-dropdown';
import { StocktakingStatusBadgeMapping } from './stocktaking-mapping';
import { StocktakingContext } from './stocktaking-provider';
import { useAppSelector } from 'app/config/store';
import AuthGuard from 'app/components/guards/auth-guard';

const StocktakingTable = () => {
  const { filter, setFilter } = useContext(StocktakingContext);
  const navigate = useNavigate();

  const { data } = useGetStocktaking({ ...filter });

  const toUpdate = (id: string) => {
    navigate(PATH.STOCKTAKING_UPDATE.replace(':id', id));
  };

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const columns: TableColumns<IStocktaking> = [
    {
      header: { render: 'Mã kiểm kê' },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.code} target={`code-${data?.id}`}>
            <EllipsisParagraph
              text={data?.code}
              width={200}
              id={`code-${data?.id}`}
              className="attachment-link"
              onClick={() => {
                if (!isHasPermission(authorities, 'LOGISTICS_STOCKTAKING.VIEW')) return
                toUpdate(data?.id)
              }}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Ngày kiểm' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={dayjs(data?.checkDate).format(DATE_FORMAT.DATE)}
            target={`checkDate-${data?.id}`}
          >
            <EllipsisParagraph
              text={dayjs(data?.checkDate).format(DATE_FORMAT.DATE)}
              width={200}
              id={`checkDate-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Kho' },
      body: {
        render: ({ data }) => {
          const whName = data?.warehouse?.id
            ? `${data?.warehouse?.code} - ${data?.warehouse?.name}`
            : '';
          return (
            <Tooltip label={whName} target={`warehouseName-${data?.id}`}>
              <EllipsisParagraph
                text={whName}
                width={300}
                id={`warehouseName-${data?.id}`}
              />
            </Tooltip>
          );
        },
      },
    },
    {
      header: { render: 'SL chênh lệch' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={`${data?.amountOfDifference ?? 0}`}
            target={`amountOfDifference-${data?.id}`}
          >
            <EllipsisParagraph
              text={`${data?.amountOfDifference ?? 0}`}
              width={200}
              id={`amountOfDifference-${data?.id}`}
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
              width={200}
              id={`note-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Trạng thái' },
      body: {
        render: ({ data }) => StocktakingStatusBadgeMapping(data?.status),
      },
    },
    {
      header: { render: '' },
      body: {
        render: ({ data }) => (
          <Flex align="center">
            <AuthGuard permissionKey='LOGISTICS_STOCKTAKING.VIEW'>
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
    <TablePagination<IStocktaking>
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

export default StocktakingTable;
