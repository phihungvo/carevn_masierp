import React, { Dispatch, SetStateAction, useEffect } from 'react';

import TablePagination from 'app/components/table-v2/TablePagination';
import { DEFAULT_PAGE } from 'app/constants/common';
import { useDebounce } from 'app/hooks/use-debounce';
import useProductionStandard from 'app/hooks/use-production-standard';
import useUom from 'app/hooks/use-uom';
import { IProductionStandardParams } from 'app/shared/model/production-command.model';
import { IProductionStandard } from 'app/shared/model/production-standard.model';
import { generateColumns } from './generate-columns';

const { useGetProductionStandards } = useProductionStandard;
const { useGetUoms } = useUom;
interface IProductionStandardTable {
  toggleUpdateProductionStandard: () => void;
  toggleDisposeProductionStandard: () => void;
  setSelectedRecord: (id: string) => void;
  setSelectedRowKeys: (ids: string[]) => void;
  selectedRowKeys: string[];
  searchText: string;
  filter: IProductionStandardParams;
  setFilter: React.Dispatch<React.SetStateAction<IProductionStandardParams>>;
  setRecord: Dispatch<SetStateAction<IProductionStandard[]>>;
}

export const ProductionStandardTable = (props: IProductionStandardTable) => {
  const {
    toggleUpdateProductionStandard,
    toggleDisposeProductionStandard,
    setSelectedRecord,
    selectedRowKeys,
    setSelectedRowKeys,
    searchText,
    filter,
    setFilter,
    setRecord,
  } = props;

  const name = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, name, page: DEFAULT_PAGE }));
  }, [name]);

  const { data, isLoading } = useGetProductionStandards(filter);
  const { data: uomData } = useGetUoms();

  const handleEditRecord = (id: string) => {
    toggleUpdateProductionStandard();
    setSelectedRecord(id);
  };

  const handleDisposeRecord = (id: string) => {
    setSelectedRecord(id);
    toggleDisposeProductionStandard();
  };

  const columns = generateColumns(
    handleEditRecord,
    handleDisposeRecord,
    uomData?.data,
  );

  const handlePageChange = (page: number) => {
    setFilter({ ...filter, page: page });
  };

  const handlePageSizeChange = (pageSize: number) => {
    setFilter({ ...filter, page: DEFAULT_PAGE, size: pageSize });
  };

  return (
    <TablePagination<IProductionStandard>
      table_id="standards"
      columns={columns}
      data={data?.data || []}
      total_pages={data?.totalRecord || 0}
      itemsPerPage={filter?.size}
      handlePageClick={handlePageChange}
      handlePageSizeChange={handlePageSizeChange}
    />
  );
};
