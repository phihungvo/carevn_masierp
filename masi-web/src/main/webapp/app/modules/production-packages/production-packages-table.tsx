import TablePagination from 'app/components/table-v2/TablePagination';
import { DEFAULT_PAGE } from 'app/constants/common';
import { useDebounce } from 'app/hooks/use-debounce';
import useProductionCommand from 'app/hooks/use-production-command';
import useProductionPackage from 'app/hooks/use-production-package';
import { PRODUCTION_COMMAND_STATUS } from 'app/shared/model/enumerations/production-command.model';
import {
  IProductionPackage,
  IProductionPackageParams,
} from 'app/shared/model/production-package.model';
import { DebouncedFunc, debounce, keyBy } from 'lodash';
import React, { useEffect } from 'react';
import { generateColumns } from './generate-columns';

const { useProductionPackages } = useProductionPackage;
const { useGetProductionCommandsQuery } = useProductionCommand;

interface IProductionPackagesTableProps {
  toggleUpdate: () => void;
  toggleDelete: () => void;
  searchText: string;
  filter: IProductionPackageParams;
  setFilter: React.Dispatch<React.SetStateAction<IProductionPackageParams>>;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  selectedRows: IProductionPackage[];
  setSelectedRows: React.Dispatch<React.SetStateAction<IProductionPackage[]>>;
}

const ProductionPackagesTable = (props: IProductionPackagesTableProps) => {
  const {
    toggleDelete,
    toggleUpdate,
    searchText,
    filter,
    setFilter,
    setSelectedRecord,
    selectedRowKeys,
    setSelectedRowKeys,
    selectedRows,
    setSelectedRows,
  } = props;

  const search = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, search, page: DEFAULT_PAGE }));
  }, [search]);

  const { data, isLoading, isRefetching } = useProductionPackages(filter);
  const { data: productionCommands } = useGetProductionCommandsQuery();

  const handleUpdate = (id: string) => {
    setSelectedRecord(id);
    toggleUpdate();
  };

  const handleDelete = (id: string) => {
    setSelectedRecord(id);
    toggleDelete();
  };

  const columns = generateColumns(
    handleUpdate,
    handleDelete,
    productionCommands?.data,
  );

  useEffect(() => {
    let statusDebounce: DebouncedFunc<() => void>;
    if (data) {
      statusDebounce = debounce(() => {
        const obj = keyBy(data.data, 'id');
        setSelectedRows(pre => [...pre.map(e => obj[e.id])]);
      }, 500);

      statusDebounce();
    }

    return () => statusDebounce && statusDebounce.cancel();
  }, [isRefetching]);

  const handlePageChange = (page: number) => {
    setFilter({ ...filter, page: page });
  };

  const handlePageSizeChange = (pageSize: number) => {
    setFilter({ ...filter, page: DEFAULT_PAGE, size: pageSize });
  };

  return (
    <TablePagination<IProductionPackage>
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

export default ProductionPackagesTable;
