import './documentary.scss';
import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useModalsDocumentary } from 'app/hooks/use-modals-documentary';
import DocumentaryHeader from './documentary-header';
import DocumentaryTable from './documentary-table';
import DocumentaryFilterModals from './modals/documentary-filter-modals';
import DocumentaryDetailModals from './modals/documentary-detail-modals';
import DocumentaryCreateModals from './modals/documentary-create-modals';
import DocumentaryCreateSuccessModals from './modals/documentary-create-success-modals';
import DocumentaryUpdateModals from './modals/documentary-update-modals';
import DocumentaryUpdateSuccessModals from './modals/documentary-update-success-modals';
import DocumentaryDeleteModals from './modals/documentary-delete-modals';
import DocumentaryDeleteSuccessModals from './modals/documentary-delete-success-modals';
import DocumentaryProposeModals from './modals/documentary-propose-modals';
import DocumentaryProposeSuccessModals from './modals/documentary-propose-success-modals';
import DocumentaryApproveModals from './modals/documentary-approve-modals';
import DocumentaryApproveSignModals from './modals/documentary-approve-sign-modals';
import DocumentaryApproveSuccessModals from './modals/documentary-approve-success-modals';
import DocumentaryRejectModals from './modals/documentary-reject-modals';
import DocumentaryRejectSuccessModals from './modals/documentary-reject-success-modals';
import { IDocumentary, IDocumentaryParams } from 'app/shared/model/documentary.model';

const Documentary = () => {
  const [
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openPropose, togglePropose },
    { openProposeSuccess, toggleProposeSuccess },
    { openApprove, toggleApprove },
    { openApproveSign, toggleApproveSign },
    { openApproveSuccess, toggleApproveSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
  ] = useModalsDocumentary();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<IDocumentary[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IDocumentaryParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: '',
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách công văn</Typography>

      <Card header={<DocumentaryHeader setSearchText={setSearchText} toggleFilter={toggleFilter} toggleCreate={toggleCreate} />}>
        <DocumentaryTable
          toggleDetail={toggleDetail}
          toggleUpdate={toggleUpdate}
          toggleDelete={toggleDelete}
          togglePropose={togglePropose}
          toggleApprove={toggleApprove}
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

      <DocumentaryFilterModals isOpen={openFilter} toggle={toggleFilter} setFilter={setFilter} />
      <DocumentaryDetailModals isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />
      <DocumentaryCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <DocumentaryCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />

      <DocumentaryUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord || selectedRowKeys?.[0]}
        setSelectedRecord={setSelectedRecord}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <DocumentaryUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <DocumentaryDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <DocumentaryDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
      <DocumentaryProposeModals
        isOpen={openPropose}
        toggle={togglePropose}
        toggleSuccess={toggleProposeSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <DocumentaryProposeSuccessModals isOpen={openProposeSuccess} toggle={toggleProposeSuccess} />

      <DocumentaryApproveModals isOpen={openApprove} toggle={toggleApprove} toggleApprove={toggleApproveSign} toggleReject={toggleReject} />
      <DocumentaryApproveSignModals
        isOpen={openApproveSign}
        toggle={toggleApproveSign}
        toggleSuccess={toggleApproveSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <DocumentaryApproveSuccessModals isOpen={openApproveSuccess} toggle={toggleApproveSuccess} />
      <DocumentaryRejectModals
        isOpen={openReject}
        toggle={toggleReject}
        toggleSuccess={toggleApproveSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <DocumentaryRejectSuccessModals isOpen={openRejectSuccess} toggle={toggleRejectSuccess} />
    </div>
  );
};

export default Documentary;
