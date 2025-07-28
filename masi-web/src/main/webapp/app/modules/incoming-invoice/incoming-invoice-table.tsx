import Table from 'app/components/table/table';
import { useDebounce } from 'app/hooks/use-debounce';
import useIncomingInvoice from 'app/hooks/use-incoming-invoice';
import { IIncomingInvoice, IIncomingInvoiceParams } from 'app/shared/model/incoming-invoice.model';
import React from 'react';
import { generateColumns } from './generate-columns';

const { useIncomingInvoices } = useIncomingInvoice;
interface IDocumentaryTableProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  togglePropose: () => void;
  toggleApprove: () => void;
  searchText: string;
  filter: IIncomingInvoiceParams;
  setFilter: React.Dispatch<React.SetStateAction<IIncomingInvoiceParams>>;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  selectedRows: IIncomingInvoice[];
  setSelectedRows: React.Dispatch<React.SetStateAction<IIncomingInvoice[]>>;
}

export default function IncomingInvoiceTable({
  toggleDetail,
  toggleUpdate,
  toggleDelete,
  togglePropose,
  toggleApprove,
  searchText,
  filter,
  setFilter,
  setSelectedRecord,
  selectedRowKeys,
  setSelectedRowKeys,
  selectedRows,
  setSelectedRows,
}: IDocumentaryTableProps) {
  const searchDebounce = useDebounce(searchText, 500);

  const { data, isLoading } = useIncomingInvoices({
    ...filter,
    sort: 'create_at,desc',
    search: searchDebounce,
  });
  const cols = generateColumns(toggleDetail, toggleUpdate, toggleDelete, togglePropose, toggleApprove, setSelectedRecord);

  return (
    <Table<IIncomingInvoice>
      rowKey="id"
      loading={isLoading}
      dataSource={data?.data || []}
      columns={cols}
      pagination={{
        page: filter.page,
        size: filter.size,
        totalCount: data?.totalRecord || 1,
        onPageChange(page1, size1) {
          setFilter(prev => ({ ...prev, page: page1, size: size1 }));
        },
        showJumper: true,
      }}
    />
  );
}
