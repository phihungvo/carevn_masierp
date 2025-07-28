import './invoices.scss';

import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useModalsIncomingInvoice } from 'app/hooks/use-modals-incoming-invoice';
import { IIncomingInvoice, IIncomingInvoiceParams } from 'app/shared/model/incoming-invoice.model';
import React, { useState } from 'react';
import { DeleteRequestSuccessModal } from '../time-sheet-explanation/modals/time-sheet-explanation-delete-modals';
import IncomingInvoiceHeader from './incoming-invoice-header';
import IncomingInvoiceTable from './incoming-invoice-table';
import IncomingInvoiceCreateModals from './modals/incoming-invoice-create-modals';
import IncomingInvoiceCreateSuccessModals from './modals/incoming-invoice-create-success-modals';
import IncomingInvoiceDeleteModals from './modals/incoming-invoice-delete-modals';
import IncomingInvoiceDetailsModal from './modals/incoming-invoice-details-modal';
import IncomingInvoiceFilterModals from './modals/incoming-invoice-filter-modals';
import IncomingInvoiceUpdateModals from './modals/incoming-invoice-update-modals';
import IncomingInvoiceUpdateSuccessModals from './modals/incoming-invoice-update-success-modals';

export default function IncomingInvoices() {
  const {
    create: { openCreate, toggleCreate },
    filter: { openFilter, toggleFilter },
    approve: { toggleApprove },
    createSuccess: { openCreateSuccess, toggleCreateSuccess },
    delete: { openDelete, toggleDelete },
    deleteSuccess: { openDeleteSuccess, toggleDeleteSuccess },
    detail: { openDetail, toggleDetail },
    propose: { togglePropose },
    update: { openUpdate, toggleUpdate },
    updateSuccess: { openUpdateSuccess, toggleUpdateSuccess },
  } = useModalsIncomingInvoice();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<IIncomingInvoice[]>([]);
  const [searchText, setSearchText] = useState<string>('');

  const [filter, setFilter] = useState<IIncomingInvoiceParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: '',
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách hoá đơn đầu vào</Typography>
      <Card header={<IncomingInvoiceHeader setSearchText={setSearchText} toggleCreate={toggleCreate} toggleFilter={toggleFilter} />}>
        <IncomingInvoiceTable
          filter={filter}
          searchText={searchText}
          selectedRowKeys={selectedRowKeys}
          selectedRows={selectedRows}
          setFilter={setFilter}
          setSelectedRecord={setSelectedRecord}
          setSelectedRowKeys={setSelectedRowKeys}
          setSelectedRows={setSelectedRows}
          toggleApprove={toggleApprove}
          toggleDelete={toggleDelete}
          toggleDetail={toggleDetail}
          togglePropose={togglePropose}
          toggleUpdate={toggleUpdate}
        />
      </Card>
      <IncomingInvoiceCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <IncomingInvoiceUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        selectedRecord={selectedRecord}
        toggleSuccess={toggleUpdateSuccess}
      />
      <IncomingInvoiceCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
      <IncomingInvoiceUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <DeleteRequestSuccessModal isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
      <IncomingInvoiceDeleteModals
        toggleSuccess={toggleDeleteSuccess}
        isOpen={openDelete}
        toggle={toggleDelete}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <IncomingInvoiceFilterModals isOpen={openFilter} toggle={toggleFilter} setFilter={setFilter} />
      <IncomingInvoiceDetailsModal isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />
    </div>
  );
}
