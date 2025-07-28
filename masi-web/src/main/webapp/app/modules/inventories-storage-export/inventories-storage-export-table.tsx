import TablePagination from 'app/components/table-v2/TablePagination';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { useInventories } from 'app/hooks/use-inventories';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';
import { IInventoriesStorage } from 'app/shared/model/inventories-storage.model';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { useSearchParams } from 'react-router-dom';
import { generate_columns } from './genereate-columns';
import { InventoriesExportStorageContext } from './inventories-storage-export-provider';
import useWorkspace from 'app/hooks/use-workspace';
import useCustomers from 'app/hooks/use-customers';

const { useGetWorkspacesQuery } = useWorkspace;
const { useGetEnabledCustomers } = useCustomers;

const InventoriesExportStorageTable = () => {
  const { filter, setFilter } = useContext(InventoriesExportStorageContext);
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const warehouseExportType = searchParams.get('warehouse');

  const { data: customers } = useGetEnabledCustomers({
    size: DEFAULT_PAGE_SIZE_NAX,
  });
  const { data: workspaces } = useGetWorkspacesQuery();

  const { data } = useInventories({
    ...filter,
    'warehouseGroupType.equals': warehouseExportType,
  });

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

  const columns = generate_columns(
    warehouseExportType as InventoriesWarehouse,
    workspaces?.data ?? [],
    customers ?? []
  );

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

export default InventoriesExportStorageTable;
