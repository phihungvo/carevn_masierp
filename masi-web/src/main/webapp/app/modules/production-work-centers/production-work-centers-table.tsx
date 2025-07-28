import TablePagination from 'app/components/table-v2/TablePagination';
import { DEFAULT_PAGE } from 'app/constants/common';
import { useDebounce } from 'app/hooks/use-debounce';
import useWorkCenter from 'app/hooks/use-work-center';
import {
  IWorkCenter,
  IWorkCenterParams,
} from 'app/shared/model/work-center.model';
import React, { useEffect } from 'react';
import { generateColumns } from './generate-columns';

const { useGetWorkCentersQuery } = useWorkCenter;

interface IProductionWorkCentersTableProps {
  toggleUpdate: () => void;
  toggleDelete: () => void;
  searchText: string;
  filter: IWorkCenterParams;
  setFilter: React.Dispatch<React.SetStateAction<IWorkCenterParams>>;
  setSelectedRecord: (id: string) => void;
}

const ProductionWorkCentersTable = (
  props: IProductionWorkCentersTableProps,
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

  const { data } = useGetWorkCentersQuery(filter);

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
    <TablePagination<IWorkCenter>
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

export default ProductionWorkCentersTable;
