import Table from 'app/components/table/table';
import React, { useEffect } from 'react';
import purchaseMapping from './purchase-mapping';
import { IPurchase, IPurchaseParams } from 'app/shared/model/purchase.model';
import { useDebounce } from 'app/hooks/use-debounce';
import { DEFAULT_PAGE } from 'app/constants/common';
import { generateColumns } from './generate-columns';
import usePurchase from 'app/hooks/use-purchase';

const { purchaseColorMapping } = purchaseMapping;
const { useGetPurchases } = usePurchase;

interface IPurchaseTableProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleRequest: () => void;
  toggleApprove: () => void;
  toggleStatus: () => void;
  searchText: string;
  filter: IPurchaseParams;
  setFilter: React.Dispatch<React.SetStateAction<IPurchaseParams>>;
  setSelectedRecord: (id: string) => void;
}

const PurchaseTable = (props: IPurchaseTableProps) => {
  const {
    toggleDetail,
    toggleUpdate,
    toggleDelete,
    toggleRequest,
    toggleApprove,
    toggleStatus,
    searchText,
    filter,
    setFilter,
    setSelectedRecord,
  } = props;

  const searchString = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, searchString, page: DEFAULT_PAGE }));
  }, [searchString]);

  const { data, isLoading } = useGetPurchases(filter);

  const columns = generateColumns(toggleDetail, toggleUpdate, toggleDelete, toggleRequest, toggleApprove, toggleStatus, setSelectedRecord);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IPurchase>
      rowKey="id"
      loading={isLoading}
      dataSource={data?.data}
      columns={columns}
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
      }}
    />
  );
};

export default PurchaseTable;
