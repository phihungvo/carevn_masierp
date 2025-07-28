import './purchase.scss';
import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import PurchaseHeader from './purchase-header';
import PurchaseTable from './purchase-table';
import { useModalsPurchase } from 'app/hooks/use-modals-purchase';
import PurchaseFilterModals from './modals/purchase-filter-modals';
import PurchaseDetailModals from './modals/purchase-detail-modals';
import PurchaseCreateModals from './modals/purchase-create-modals';
import PurchaseCreateSuccessModals from './modals/purchase-create-success-modals';
import PurchaseUpdateModals from './modals/purchase-update-modals';
import PurchaseUpdateSuccessModals from './modals/purchase-update-success-modals';
import PurchaseDeleteModals from './modals/purchase-delete-modals';
import PurchaseDeleteSuccessModals from './modals/purchase-delete-success-modals';
import PurchaseDeleteErrorModals from './modals/purchase-delete-error-modals';
import PurchaseStatusModals from './modals/purchase-status-modals';
import PurchaseStatusSuccessModals from './modals/purchase-status-success-modals';
import PurchaseRequestModals from './modals/purchase-request-modals';
import PurchaseRequestSuccessModals from './modals/purchase-request-success-modals';
import PurchaseRequestErrorModals from './modals/purchase-request-error-modals';
import PurchaseApproveModals from './modals/purchase-approve-modals';
import PurchaseApproveSignModals from './modals/purchase-approve-sign-modals';
import PurchaseRejectModals from './modals/purchase-reject-modals';
import PurchaseApproveSuccessModals from './modals/purchase-approve-sign-success';
import PurchaseRejectSuccessModals from './modals/purchase-reject-success';
import { IPurchaseParams } from 'app/shared/model/purchase.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';

const Purchase = () => {
  const [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openDetail, toggleDetail },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openDeleteError, toggleDeleteError },
    { openStatus, toggleStatus },
    { openStatusSuccess, toggleStatusSuccess },
    { openRequest, toggleRequest },
    { openRequestSuccess, toggleRequestSuccess },
    { openRequestError, toggleRequestError },
    { openApprove, toggleApprove },
    { openApproveSign, toggleApproveSign },
    { openApproveSignSuccess, toggleApproveSignSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
  ] = useModalsPurchase();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);

  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IPurchaseParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    searchString: '',
  });

  return (
    <>
      <Typography level={3}>Đề nghị thu mua</Typography>
      <Card header={<PurchaseHeader toggleCreate={toggleCreate} toggleFilter={toggleFilter} setSearchText={setSearchText} />}>
        <PurchaseTable
          toggleDetail={toggleDetail}
          toggleUpdate={toggleUpdate}
          toggleDelete={toggleDelete}
          toggleRequest={toggleRequest}
          toggleApprove={toggleApprove}
          toggleStatus={toggleStatus}
          searchText={searchText}
          filter={filter}
          setFilter={setFilter}
          setSelectedRecord={setSelectedRecord}
        />
      </Card>

      <PurchaseFilterModals isOpen={openFilter} toggle={toggleFilter} setFilter={setFilter} />
      <PurchaseDetailModals isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />
      <PurchaseCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <PurchaseCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
      <PurchaseUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <PurchaseUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <PurchaseDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        toggleError={toggleDeleteError}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <PurchaseDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
      <PurchaseDeleteErrorModals isOpen={openDeleteError} toggle={toggleDeleteError} />
      <PurchaseStatusModals
        isOpen={openStatus}
        toggle={toggleStatus}
        toggleSuccess={toggleStatusSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <PurchaseStatusSuccessModals isOpen={openStatusSuccess} toggle={toggleStatusSuccess} />
      <PurchaseRequestModals
        isOpen={openRequest}
        toggle={toggleRequest}
        toggleSuccess={toggleRequestSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <PurchaseRequestSuccessModals isOpen={openRequestSuccess} toggle={toggleRequestSuccess} />
      <PurchaseRequestErrorModals isOpen={openRequestError} toggle={toggleRequestError} />
      <PurchaseApproveModals isOpen={openApprove} toggle={toggleApprove} toggleApprove={toggleApproveSign} toggleReject={toggleReject} />
      <PurchaseApproveSignModals
        isOpen={openApproveSign}
        toggle={toggleApproveSign}
        toggleSuccess={toggleApproveSignSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <PurchaseRejectModals
        isOpen={openReject}
        toggle={toggleReject}
        toggleSuccess={toggleRejectSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <PurchaseApproveSuccessModals isOpen={openApproveSignSuccess} toggle={toggleApproveSignSuccess} />
      <PurchaseRejectSuccessModals isOpen={openRejectSuccess} toggle={toggleRejectSuccess} />
    </>
  );
};

export default Purchase;
