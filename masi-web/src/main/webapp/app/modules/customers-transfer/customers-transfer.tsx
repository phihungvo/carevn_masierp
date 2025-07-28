import './customers-transfer.scss';
import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import { useModalsCustomersTransfer } from 'app/hooks/use-modals-customers-transfer';
import CustomerTransferHeader from './customers-transfer-header';
import CustomersTransferTable from './customers-transfer-table';
import CustomersFilterModals from '../customers/modals/customers-filter-modals';
import CustomerCreateModals from '../customers/modals/customers-create-modals';
import CustomersTransferModals from '../customers/modals/customers-transfer-modals';
import CustomerTransferSuccessModals from '../customers/modals/customers-transfer-success-modals';
import CustomerCreateSuccessModals from '../customers/modals/customers-create-success-modals';
import { ICustomer, ICustomerParams } from 'app/shared/model/customer.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';

const CustomersTransfer = () => {
  const [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openTransfer, toggleTransfer },
    { openTransferSuccess, toggleTransferSuccess },
  ] = useModalsCustomersTransfer();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<ICustomer[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<ICustomerParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: '',
  });

  return (
    <>
      <Typography level={3}>Khách hàng cần chuyển giao</Typography>

      <Card header={<CustomerTransferHeader setSearchText={setSearchText} toggleCreate={toggleCreate} toggleFilter={toggleFilter} />}>
        <CustomersTransferTable
          toggleTransfer={toggleTransfer}
          searchText={searchText}
          filter={filter}
          setFilter={setFilter}
          setSelectedRecord={setSelectedRecord}
          selectedRowKeys={selectedRowKeys}
          setSelectedRowKeys={setSelectedRowKeys}
          selectedRows={selectedRows}
          setSelectedRows={setSelectedRows}
        />
      </Card>

      <CustomersFilterModals isOpen={openFilter} toggle={toggleFilter} setFilter={setFilter} />
      <CustomerCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <CustomerCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
      <CustomersTransferModals
        isOpen={openTransfer}
        toggle={toggleTransfer}
        toggleSuccess={toggleTransferSuccess}
        selectedRecord={selectedRecord}
      />
      <CustomerTransferSuccessModals isOpen={openTransferSuccess} toggle={toggleTransferSuccess} />
    </>
  );
};

export default CustomersTransfer;
