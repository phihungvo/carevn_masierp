import Table from 'app/components/table/table';
import { DEFAULT_PAGE } from 'app/constants/common';
import { useDebounce } from 'app/hooks/use-debounce';
import useItems from 'app/hooks/use-items';
import { IItem, IItemParams } from 'app/shared/model/item.model';
import { ISuppliesDetails } from 'app/shared/model/supplier-detail.model';
import React, { useEffect } from 'react';
import { generateColumns } from './generate-columns';

const { useGetItemsQuery } = useItems;
export interface ISupplierDetailsItem {
  selectedRowKeys: string[];
  selectedRowsFromChild: ISuppliesDetails[];
}
interface IItemTable {
  filter: IItemParams;
  setFilter: React.Dispatch<React.SetStateAction<IItemParams>>;
  toggleUpdate?: () => void;
  toggleDelete?: () => void;
  toggleDetail?: () => void;
  searchText?: string;
  isHasCheckbox?: boolean;
  setSelectedRecord: (id: string) => void;
  haveActions?: boolean;
  selectedRecords?: {
    selectedRowKeys: string[];
    selectedRows: IItem[];
  };
  setSelectedRecords?: (value: { selectedRowKeys: string[]; selectedRows: IItem[] }) => void;
}

const ItemTable = (props: IItemTable) => {
  const {
    filter,
    setFilter,
    toggleDelete,
    toggleUpdate,
    searchText,
    toggleDetail,
    setSelectedRecord,
    isHasCheckbox,
    haveActions,
    selectedRecords,
    setSelectedRecords,
  } = props;

  const columns = generateColumns(toggleUpdate, toggleDelete, setSelectedRecord, toggleDetail, haveActions);

  // Gọi query để lấy dữ liệu
  const { data, isLoading } = useGetItemsQuery(filter);
  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;
  const name = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, 'name.contains': name, page: DEFAULT_PAGE }));
  }, [name]);

  const onRowSelectionChange = (selectedRowKeys: string[], selectedRows: IItem[]) => {
    setSelectedRecords && setSelectedRecords({ selectedRowKeys, selectedRows });
  };

  return (
    <Table<IItem>
      rowKey="id"
      rowSelection={
        isHasCheckbox
          ? {
              type: 'checkbox',
              onChange: onRowSelectionChange, // Khi lựa chọn hàng thay đổi, gọi hàm xử lý
              selectedRowKeys: selectedRecords?.selectedRowKeys, // Lấy selectedRowKeys từ form
            }
          : undefined
      }
      loading={isLoading}
      columns={columns}
      dataSource={data?.data}
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
      }}
    />
  );
};

export default ItemTable;
