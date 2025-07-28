import { Typography } from 'app/components/typography/typography';
import './contracts.scss';
import React, { useState } from 'react';
import Card from 'app/components/card/card';
import ContractTableDeleted from './contracts-table-deleted';
import ContractsHeaderDeleted from './contracts-header-deleted';
import { useModalsContractsDeleted } from 'app/hooks/use-modals-contracts';

import { IContract, IContractParams } from 'app/shared/model/contract.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import ContractsFilterModals from '../contracts/modals/contracts-filter-modals';
import ContractsCreateModals from '../contracts/modals/contracts-create-modals';
import ContractCreateSuccessModals from '../contracts/modals/contracts-create-success-modals';
import ContractsRestoreModals from '../contracts/modals/contracts-restore-modals';
import ContractRestoreSuccessModals from '../contracts/modals/contracts-restore-success-modals';

const ContractsDeleted = () => {
  const [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openRestore, toggleRestore },
    { openRestoreSuccess, toggleRestoreSuccess },
  ] = useModalsContractsDeleted();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<IContract[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IContractParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: '',
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách hợp đồng đã xoá</Typography>

      <Card header={<ContractsHeaderDeleted setSearchText={setSearchText} toggleFilter={toggleFilter} toggleCreate={toggleCreate} />}>
        <ContractTableDeleted
          toggleRestore={toggleRestore}
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

      <ContractsFilterModals isOpen={openFilter} toggle={toggleFilter} setFilter={setFilter} deleted />
      <ContractsCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <ContractCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />

      <ContractsRestoreModals
        isOpen={openRestore}
        toggle={toggleRestore}
        toggleSuccess={toggleRestoreSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <ContractRestoreSuccessModals isOpen={openRestoreSuccess} toggle={toggleRestoreSuccess} />
    </div>
  );
};

export default ContractsDeleted;
