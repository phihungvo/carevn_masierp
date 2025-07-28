import './customers.scss';
import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import React, { useEffect, useState } from 'react';
import CustomersTable from './customers-table';
import CustomersHeader from './customers-header';
import { useModalsCustomers } from 'app/hooks/use-modals-customers';
import CustomersFilterModals from './modals/customers-filter-modals';
import CustomersDetailModals from './modals/customers-detail-modals';
import CustomerCreateModals from './modals/customers-create-modals';
import CustomersUpdateModals from './modals/customers-update-modals';
import CustomerCreateSuccessModals from './modals/customers-create-success-modals';
import CustomerUpdateSuccessModals from './modals/customers-update-success-modals';
import CustomersDisposeModals from './modals/customers-dispose-modals';
import CustomerDisposeSuccessModals from './modals/customers-dispose-success-modals';
import CustomersTransferModals from './modals/customers-transfer-modals';
import CustomerTransferSuccessModals from './modals/customers-transfer-success-modals';
import { ICustomer, ICustomerParams } from 'app/shared/model/customer.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useLocation } from 'react-router';
import { useGetBirthdayParams } from '../../hooks/use-get-birthday-params';
import { useSearchParams } from 'react-router-dom';

const Customers = () => {
  const [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
    { openDispose, toggleDispose },
    { openDisposeSuccess, toggleDisposeSuccess },
    { openTransfer, toggleTransfer },
    { openTransferSuccess, toggleTransferSuccess },
  ] = useModalsCustomers();
  const { search } = useLocation();

  const [searchParams] = useSearchParams();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<ICustomer[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<ICustomerParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: '',
    sort: ['created_date,desc', 'customer_code,asc'],
  });

  useGetBirthdayParams(search, setFilter);

  useEffect(() => {
    if (searchParams) setFilter(prev => ({ ...prev, customerId: searchParams.get('customerId') }))
  }, [searchParams])

  return (
    <div className='page_container'>
      <Typography level={4}>Quản lý khách hàng</Typography>
      <Card header={<CustomersHeader setSearchText={setSearchText} toggleCreate={toggleCreate} toggleFilter={toggleFilter} />}>
        <CustomersTable
          toggleDetail={toggleDetail}
          toggleUpdate={toggleUpdate}
          toggleDispose={toggleDispose}
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
      <CustomersDetailModals isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />
      <CustomerCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <CustomerCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
      <CustomersUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <CustomerUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <CustomersDisposeModals
        isOpen={openDispose}
        toggle={toggleDispose}
        toggleSuccess={toggleDisposeSuccess}
        selectedRecord={selectedRecord}
      />
      <CustomerDisposeSuccessModals isOpen={openDisposeSuccess} toggle={toggleDisposeSuccess} />
      <CustomersTransferModals
        isOpen={openTransfer}
        toggle={toggleTransfer}
        toggleSuccess={toggleTransferSuccess}
        selectedRecord={selectedRecord}
      />
      <CustomerTransferSuccessModals isOpen={openTransferSuccess} toggle={toggleTransferSuccess} />
    </div>
  );
};

export default Customers;
