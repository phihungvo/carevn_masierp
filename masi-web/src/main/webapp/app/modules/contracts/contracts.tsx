import React, { useState } from 'react';

import './contracts.scss';
import Card from 'app/components/card/card';
import ContractsTable from './contracts-table';
import ContractsHeader from './contracts-header';
import ContractsFilterModals from './modals/contracts-filter-modals';
import ContractsDetailModals from './modals/contracts-detail-modals';
import ContractsCreateModals from './modals/contracts-create-modals';
import ContractCreateSuccessModals from './modals/contracts-create-success-modals';
import ContractsUpdateModals from './modals/contracts-update-modals';
import ContractUpdateErrorModals from './modals/contracts-update-error-modals';
import ContractDeleteModals from './modals/contracts-delete-modals';
import ContractDeleteSuccessModals from './modals/contracts-delete-success-modals';
import ContractsRestoreModals from './modals/contracts-restore-modals';
import ContractsProposeApproveModals from './modals/contracts-propose-approve-modals';
import ContractApproveModals from './modals/contracts-approve-modals';
import ContractApproveSuccessModals from './modals/contracts-approve-success-modals';
import ContractApproveLiquidModals from './modals/contracts-approve-liquid-modals';
import ContractApproveLiquidSignModals from './modals/contracts-approve-liquid-sign-modals';
import ContractApproveLiquidSuccessModals from './modals/contracts-approve-liquid-success-modals';
import ContractRejectLiquidModals from './modals/contracts-reject-liquid-modals';
import ContractsProposeLiquidModals from './modals/contracts-propose-liquid-modals';
import ContractRestoreSuccessModals from './modals/contracts-restore-success-modals';
import ContractProposeApproveSuccessModals from './modals/contracts-propose-approve-success-modals';
import ContractRejectLiquidSuccessModals from './modals/contracts-reject-liquid-success-modals';
import ContractUpdateSuccessModals from './modals/contracts-update-success-modals';
import ContractProposeLiquidSuccessModals from './modals/contracts-propose-liquid-success-modals';
import ContractsApproveSignModals from './modals/contracts-approve-sign-modals';
import ContractsApproveSuccessModals from './modals/contracts-approve-sign-success-modals';
import ContractsRejectModals from './modals/contracts-reject-modals';
import ContractsRejectSuccessModals from './modals/contracts-reject-success';
import ContractsMaskFinishedModals from './modals/contracts-mask-finished-modals';
import ContractMaskFinishedSuccessModals from './modals/contracts-mask-finished-success-modals';
import { Typography } from 'app/components/typography/typography';
import { useModalsContracts } from 'app/hooks/use-modals-contracts';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { IContract, IContractParams } from 'app/shared/model/contract.model';

const Contracts = () => {
  const [
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openUpdateError, toggleUpdateError },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openRestore, toggleRestore },
    { openRestoreSuccess, toggleRestoreSuccess },
    { openProposeApprove, toggleProposeApprove },
    { openProposeApproveSuccess, toggleProposeApproveSuccess },
    { openApprove, toggleApprove },
    { openApproveSuccess, toggleApproveSuccess },
    { openApproveLiquid, toggleApproveLiquid },
    { openApproveLiquidSign, toggleApproveLiquidSign },
    { openApproveLiquidSuccess, toggleApproveLiquidSuccess },
    { openRejectLiquid, toggleRejectLiquid },
    { openRejectLiquidSuccess, toggleRejectLiquidSuccess },
    { openProposeLiquid, toggleProposeLiquid },
    { openProposeLiquidSuccess, toggleProposeLiquidSuccess },
    { openApproveSign, toggleApproveSign },
    { openApproveSignSuccess, toggleApproveSignSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
    { openMaskFinished, toggleMaskFinished },
    { openMaskFinishedSuccess, toggleMaskFinishedSuccess },
  ] = useModalsContracts();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<IContract[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [contractStatus, setContractStatus] = useState<string>('');
  const [record, setRecord] = useState<IContract>();
  const [filter, setFilter] = useState<IContractParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: '',
    withTotal: true,
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách tất cả hợp đồng</Typography>

      <Card header={<ContractsHeader setSearchText={setSearchText} toggleFilter={toggleFilter} toggleCreate={toggleCreate} />}>
        <ContractsTable
          toggleDetail={toggleDetail}
          toggleUpdate={toggleUpdate}
          toggleDelete={toggleDelete}
          toggleRestore={toggleRestore}
          toggleProposeApprove={toggleProposeApprove}
          toggleApprove={toggleApprove}
          toggleProposeLiquid={toggleProposeLiquid}
          toggleApproveLiquid={toggleApproveLiquid}
          searchText={searchText}
          filter={filter}
          setFilter={setFilter}
          setSelectedRecord={setSelectedRecord}
          selectedRows={selectedRows}
          setSelectedRows={setSelectedRows}
          toggleMaskFinished={toggleMaskFinished}
          setContractStatus={setContractStatus}
          setRecord={setRecord}
        />
      </Card>

      <ContractsFilterModals isOpen={openFilter} toggle={toggleFilter} setFilter={setFilter} />
      <ContractsDetailModals isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />
      <ContractsCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <ContractCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
      <ContractsUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        contractStatus={contractStatus}
      />
      <ContractUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <ContractUpdateErrorModals isOpen={openUpdateError} toggle={toggleUpdateError} />
      <ContractDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <ContractDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
      <ContractsRestoreModals
        isOpen={openRestore}
        toggle={toggleRestore}
        toggleSuccess={toggleRestoreSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <ContractRestoreSuccessModals isOpen={openRestoreSuccess} toggle={toggleRestoreSuccess} />
      <ContractsProposeApproveModals
        isOpen={openProposeApprove}
        toggle={toggleProposeApprove}
        toggleSuccess={toggleProposeApproveSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <ContractProposeApproveSuccessModals isOpen={openProposeApproveSuccess} toggle={toggleProposeApproveSuccess} />
      <ContractApproveModals
        isOpen={openApprove}
        toggle={toggleApprove}
        toggleApprove={toggleApproveSign}
        toggleReject={toggleReject}
      />
      <ContractApproveSuccessModals isOpen={openApproveSuccess} toggle={toggleApproveSuccess} />
      <ContractsProposeLiquidModals
        isOpen={openProposeLiquid}
        toggle={toggleProposeLiquid}
        toggleSuccess={toggleProposeLiquidSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <ContractProposeLiquidSuccessModals isOpen={openProposeLiquidSuccess} toggle={toggleProposeLiquidSuccess} />
      <ContractApproveLiquidModals
        isOpen={openApproveLiquid}
        toggle={toggleApproveLiquid}
        toggleApprove={toggleApproveLiquidSign}
        toggleReject={toggleRejectLiquid}
        record={record}
      />
      <ContractApproveLiquidSignModals
        isOpen={openApproveLiquidSign}
        toggle={toggleApproveLiquidSign}
        toggleSuccess={toggleApproveLiquidSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        record={record}
      />
      <ContractApproveLiquidSuccessModals isOpen={openApproveLiquidSuccess} toggle={toggleApproveLiquidSuccess} />
      <ContractRejectLiquidModals
        isOpen={openRejectLiquid}
        toggle={toggleRejectLiquid}
        toggleSuccess={toggleRejectLiquidSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        record={record}
      />
      <ContractRejectLiquidSuccessModals isOpen={openRejectLiquidSuccess} toggle={toggleRejectLiquidSuccess} />

      <ContractsApproveSignModals
        isOpen={openApproveSign}
        toggle={toggleApproveSign}
        toggleSuccess={toggleApproveSignSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        record={record}
      />

      <ContractsApproveSuccessModals
        isOpen={openApproveSignSuccess}
        toggle={toggleApproveSignSuccess}
      />

      <ContractsRejectModals
        isOpen={openReject}
        toggle={toggleReject}
        toggleSuccess={toggleRejectSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        record={record}
      />

      <ContractsRejectSuccessModals
        isOpen={openRejectSuccess}
        toggle={toggleRejectSuccess}
      />

      <ContractsMaskFinishedModals
        isOpen={openMaskFinished}
        toggle={toggleMaskFinished}
        toggleSuccess={toggleMaskFinishedSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />

      <ContractMaskFinishedSuccessModals
        isOpen={openMaskFinishedSuccess}
        toggle={toggleMaskFinishedSuccess}
      />
    </div>
  );
};

export default Contracts;
