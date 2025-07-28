import { Typography } from 'app/components/typography/typography';
import './price-list.scss';
import React, { useState } from 'react';
import Card from 'app/components/card/card';
import PriceListTable from './price-list-table';
import PriceListHeader from './price-list-header';
import { useModalPriceList } from 'app/hooks/use-modals-price-list';
import PriceListFilterModals from './modals/price-list-filter-modals';
import PriceListUpdateErrorModals from './modals/price-list-update-error-modals';
import PriceListDeleteModals from './modals/price-list-delete-modals';
import PriceListDeleteSuccessModals from './modals/price-list-delete-success-modals';
import PriceListDeleteErrorModals from './modals/price-list-delete-error-modals';
import PriceListInternalApproveModals from './modals/price-list-internal-approve-modals';
import PriceListInternalApproveSuccess from './modals/price-list-internal-approve-success';
import PriceListCancelModals from './modals/price-list-cancel-modals';
import PriceListCancelSuccessModals from './modals/price-list-cancel-success-modals';
import PriceListCancelErrorModals from './modals/price-list-cancel-error-modals';
import PriceListCusSendModals from './modals/price-list-cus-send-modals';
import PriceListCusSendSuccessModals from './modals/price-list-cus-send-success-modals';
import PriceListApproveModals from './modals/price-list-approve-modals';
import PriceListApproveSuccessModals from './modals/price-list-approve-success-modals';
import PriceListRejectModals from './modals/price-list-reject-modals';
import PriceListRejectSuccessModals from './modals/price-list-reject-success-modals';
import { IQuotation, IQuotationParams } from 'app/shared/model/quotation.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import PriceListCusSendErrorModals from './modals/price-list-cus-send-error-modals';
import PriceListApproveInternalModals from './modals/price-list-approve-internal-modals';
import PriceListApproveInternalSuccessModals from './modals/price-list-approve-internal-success-modals';
import PriceListApproveSignModals from './modals/price-list-approve-sign-modals';
import PriceListRejectInternalModals from './modals/price-list-reject-internal-modals';
import PriceListRejectInternalSuccessModals from './modals/price-list-reject-internal-success';

const PriceList = () => {
  const [
    { openFilter, toggleFilter },
    { openUpdateError, toggleUpdateError },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openDeleteError, toggleDeleteError },
    { openInApprove, toggleInApprove },
    { openInApproveSuccess, toggleInApproveSuccess },
    { openCancel, toggleCancel },
    { openCancelSuccess, toggleCancelSuccess },
    { openCancelError, toggleCancelError },
    { openCusSend, toggleCusSend },
    { openCusSendSuccess, toggleCusSendSuccess },
    { openCusSendError, toggleCusSendError },
    { openApprove, toggleApprove },
    { openApproveSuccess, toggleApproveSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
    { openApproveInternal, toggleApproveInternal },
    { openApproveInternalSuccess, toggleApproveInternalSuccess },
    { openApproveSign, toggleApproveSign },
    { openApproveSignSuccess, toggleApproveSignSuccess },
    { openRejectInternal, toggleRejectInternal },
    { openRejectInternalSucess, toggleRejectInternalSucess },
  ] = useModalPriceList();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<IQuotation[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IQuotationParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: '',
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Bảng báo giá</Typography>
      <Card
        header={
          <PriceListHeader
            setSearchText={setSearchText}
            toggleFilter={toggleFilter}
            toggleDelete={toggleDelete}
            toggleInApprove={toggleInApprove}
            toggleCancel={toggleCancel}
            toggleCusSend={toggleCusSend}
            selectedRows={selectedRows}
          />
        }
      >
        <PriceListTable
          toggleDelete={toggleDelete}
          toggleInApprove={toggleInApprove}
          toggleCancel={toggleCancel}
          toggleCusSend={toggleCusSend}
          toggleApproveInternal={toggleApproveInternal}
          toggleApprove={toggleApprove}
          toggleReject={toggleReject}
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
      <PriceListFilterModals isOpen={openFilter} toggle={toggleFilter} setFilter={setFilter} />
      <PriceListUpdateErrorModals isOpen={openUpdateError} toggle={toggleUpdateError} />
      <PriceListDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        toggleError={toggleDeleteError}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <PriceListDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
      <PriceListDeleteErrorModals isOpen={openDeleteError} toggle={toggleDeleteError} />
      <PriceListInternalApproveModals
        isOpen={openInApprove}
        toggle={toggleInApprove}
        toggleSuccess={toggleInApproveSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <PriceListInternalApproveSuccess isOpen={openInApproveSuccess} toggle={toggleInApproveSuccess} />
      <PriceListCancelModals
        isOpen={openCancel}
        toggle={toggleCancel}
        toggleSuccess={toggleCancelSuccess}
        toggleError={toggleCancelError}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <PriceListCancelSuccessModals isOpen={openCancelSuccess} toggle={toggleCancelSuccess} />

      <PriceListCancelErrorModals isOpen={openCancelError} toggle={toggleCancelError} />

      <PriceListCusSendModals
        isOpen={openCusSend}
        toggle={toggleCusSend}
        toggleSuccess={toggleCusSendSuccess}
        toggleError={toggleCusSendError}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />

      <PriceListCusSendSuccessModals isOpen={openCusSendSuccess} toggle={toggleCusSendSuccess} />

      <PriceListCusSendErrorModals isOpen={openCusSendError} toggle={toggleCusSendError} />

      <PriceListApproveModals
        isOpen={openApprove}
        toggle={toggleApprove}
        toggleSuccess={toggleApproveSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <PriceListApproveInternalSuccessModals isOpen={openApproveInternalSuccess} toggle={toggleApproveInternalSuccess} />

      <PriceListApproveInternalModals
        isOpen={openApproveInternal}
        toggle={toggleApproveInternal}
        toggleApprove={toggleApproveSign}
        toggleReject={toggleRejectInternal}
      />

      <PriceListApproveSignModals
        isOpen={openApproveSign}
        toggle={toggleApproveSign}
        toggleSuccess={toggleApproveSignSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />

      <PriceListApproveSuccessModals
        isOpen={openApproveSignSuccess}
        toggle={toggleApproveSignSuccess}
      />

      <PriceListRejectInternalModals
        isOpen={openRejectInternal}
        toggle={toggleRejectInternal}
        toggleSuccess={toggleRejectInternalSucess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />

      <PriceListRejectInternalSuccessModals
        isOpen={openRejectInternalSucess}
        toggle={toggleRejectInternalSucess}
      />

      <PriceListApproveSuccessModals isOpen={openApproveSuccess} toggle={toggleApproveSuccess} />

      <PriceListRejectModals
        isOpen={openReject}
        toggle={toggleReject}
        toggleSuccess={toggleRejectSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />

      <PriceListRejectSuccessModals isOpen={openRejectSuccess} toggle={toggleRejectSuccess} />
    </div>
  );
};

export default PriceList;
