import './customers-disposed.scss';
import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import CustomersHeader from '../customers/customers-header';
import CustomersDisposedTable from './customers-disposed-table';
import { useModalsCustomersDisposed } from 'app/hooks/use-modals-customers-disposed';
import CustomerCreateModals from '../customers/modals/customers-create-modals';
import CustomersFilterModals from '../customers/modals/customers-filter-modals';
import CustomersActivateModals from './modals/customers-activate-modals';
import CustomerActivateSuccessModals from './modals/customers-activate-success-modals';
import CustomersDeleteModals from './modals/customers-delete-modals';
import CustomerDeleteSuccessModals from './modals/customers-delete-success-modals';
import { ICustomer, ICustomerParams } from 'app/shared/model/customer.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import CustomerCreateSuccessModals from '../customers/modals/customers-create-success-modals';

const CustomersDisposed = () => {
  const [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openActivate, toggleActivate },
    { openActivateSuccess, toggleActivateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
  ] = useModalsCustomersDisposed();

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
    <div className='page_container'>
      <Typography level={4}>Danh sách vô hiệu hóa</Typography>
      <Card header={<CustomersHeader setSearchText={setSearchText} toggleCreate={toggleCreate} toggleFilter={toggleFilter} />}>
        <CustomersDisposedTable
          toggleActivate={toggleActivate}
          toggleDelete={toggleDelete}
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
      <CustomersActivateModals
        isOpen={openActivate}
        toggle={toggleActivate}
        toggleSuccess={toggleActivateSuccess}
        selectedRecord={selectedRecord}
      />
      <CustomerActivateSuccessModals isOpen={openActivateSuccess} toggle={toggleActivateSuccess} />
      <CustomersDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
      />
      <CustomerDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
    </div>
  );
};

export default CustomersDisposed;
