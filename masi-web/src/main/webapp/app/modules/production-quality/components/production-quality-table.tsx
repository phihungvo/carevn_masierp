import TablePagination from 'app/components/table-v2/TablePagination';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import useCustomers from 'app/hooks/use-customers';
import { useDebounce } from 'app/hooks/use-debounce';
import useProductionQualityControl from 'app/hooks/use-production-quality-control';
import {
  IProductionQualityParams,
  IQualityCheckSample,
} from 'app/shared/model/production-quality-control.model';
import React, { useEffect } from 'react';
import { useNavigate } from 'react-router';
import { generateColumns } from './generate-columns';

const { useGetQualityCheckSamples } = useProductionQualityControl;
const { useGetEnabledCustomers } = useCustomers;

interface IProductionQualityTable {
  toggleDeleteTestingSample: () => void;
  setSelectedRecord: (id: string) => void;
  filter: IProductionQualityParams;
  setFilter: React.Dispatch<React.SetStateAction<IProductionQualityParams>>;
  searchText: string;
}

export const ProductionQualityTable = (props: IProductionQualityTable) => {
  const {
    toggleDeleteTestingSample,
    setSelectedRecord,
    filter,
    setFilter,
    searchText,
  } = props;

  const navigate = useNavigate();

  const search = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, page: DEFAULT_PAGE, search }));
  }, [search]);

  const { data, isLoading } = useGetQualityCheckSamples(filter);
  const { data: listCustomers } = useGetEnabledCustomers({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const handleViewDetail = (id: string) => {
    navigate(PATH.PRODUCTION_QUALITY_UPDATE.replace(':id', id));
  };

  const handleDelete = (id: string) => {
    setSelectedRecord(id);
    toggleDeleteTestingSample();
  };

  const columns = generateColumns(
    handleViewDetail,
    handleDelete,
    listCustomers?.data,
  );

  const handlePageChange = (page: number) => {
    setFilter({ ...filter, page: page });
  };

  const handlePageSizeChange = (pageSize: number) => {
    setFilter({ ...filter, page: DEFAULT_PAGE, size: pageSize });
  };

  return (
    <TablePagination<IQualityCheckSample>
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
