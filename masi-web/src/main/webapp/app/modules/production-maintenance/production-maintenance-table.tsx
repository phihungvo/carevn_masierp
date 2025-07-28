import TablePagination from 'app/components/table-v2/TablePagination';
import { DEFAULT_PAGE } from 'app/constants/common';
import { useDebounce } from 'app/hooks/use-debounce';
import useProductionMaintain from 'app/hooks/use-production-maintain';
import {
  IProductionMaintain,
  IProductionMaintainParams,
} from 'app/shared/model/production-maintain.model';
import React, { useEffect } from 'react';
import { generateColumns } from './generate-columns';

const { useGetProductionMaintains } = useProductionMaintain;

interface IProductionMaintenanceTableProps {
  toggleUpdate: () => void;
  toggleDelete: () => void;
  searchText: string;
  filter: IProductionMaintainParams;
  setFilter: React.Dispatch<React.SetStateAction<IProductionMaintainParams>>;
  setSelectedRecord: (id: string) => void;
  setSelectedRowKeys: (ids: string[]) => void;
}

const ProductionMaintenanceTable = (
  props: IProductionMaintenanceTableProps,
) => {
  const {
    toggleDelete,
    toggleUpdate,
    searchText,
    filter,
    setFilter,
    setSelectedRecord,
  } = props;

  const search = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, search, page: DEFAULT_PAGE }));
  }, [search]);

  const { data } = useGetProductionMaintains(filter);

  const handleUpdate = (id: string) => {
    setSelectedRecord(id);
    toggleUpdate();
  };

  const handleDelete = (id: string) => {
    setSelectedRecord(id);
    toggleDelete();
  };

  const columns = generateColumns(handleUpdate, handleDelete);

  const handlePageChange = (page: number) => {
    setFilter({ ...filter, page: page });
  };

  const handlePageSizeChange = (pageSize: number) => {
    setFilter({ ...filter, page: DEFAULT_PAGE, size: pageSize });
  };

  return (
    <TablePagination<IProductionMaintain>
      table_id="maintains"
      columns={columns}
      data={data?.data || []}
      total_pages={data?.totalRecord || 0}
      itemsPerPage={filter?.size}
      handlePageClick={handlePageChange}
      handlePageSizeChange={handlePageSizeChange}
    />
  );
};

export default ProductionMaintenanceTable;
