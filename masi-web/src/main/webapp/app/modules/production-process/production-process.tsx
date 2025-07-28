import React, { useState } from 'react';

import './production-process.scss';
import ProductionProcessBody from 'app/modules/production-process/production-process-body';
import ProductionProcessModalFilter from 'app/modules/production-process/production-process-modal-filter';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { IProductionProcess } from 'app/shared/model/production-process.model';
import { useModalsProductionProcess } from 'app/hooks/use-modals-production-process';
import { IProductionCommandWithProcessParams } from 'app/shared/model/production-command.model';
import { ModalStopProcess } from 'app/modules/production-process/modals/production-process-stop-modal';
import { ModalStartProcess } from 'app/modules/production-process/modals/production-process-start-modal';
import { ModalDeleteProcess } from 'app/modules/production-process/modals/production-process-delete-modal';
import { ModalCompleteProcess } from 'app/modules/production-process/modals/production-process-complete-modals';
import { ModalCreateProcess, ModalCreateProcessSuccess } from 'app/modules/production-process/modals/production-process-create-modal';
import { ModalUpdateProcess, ModalUpdateProcessSuccess } from 'app/modules/production-process/modals/production-process-update-modal';
import ProductionProcessNoticesUpdateModals from './modals/production-process-notices-update-modals';
import { DateObject } from 'react-multi-date-picker';
import { PRODUCTION_PROCESS_STATUS } from 'app/shared/model/enumerations/production-process.model';

const ProductionProcess = () => {
  const [
    { isOpenModalCreate, toggleModalCreate },
    { isOpenModalCreateSuccess, toggleModalCreateSuccess },
    { isOpenModalUpdate, toggleModalUpdate },
    { isOpenModalUpdateSuccess, toggleModalUpdateSuccess },
    { isOpenModalDelete, toggleModalDelete },
    { isOpenModalStart, toggleModalStart },
    { isOpenModalStop, toggleModalStop },
    { isOpenModalComplete, toggleModalComplete },
    { isOpenModalFilter, toggleModalFilter },
    { isOpenNoticesUpdate, toggleNoticesUpdate },
  ] = useModalsProductionProcess();

  const [selectedRecord, setSelectedRecord] = useState<string>('');
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<IProductionProcess[]>([]);
  const [filter, setFilter] = useState<IProductionCommandWithProcessParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    fromDate: '',
    toDate: '',
    statuses: [],
    searchString: '',
  });
  const [searchText, setSearchText] = useState<string>('');
  const [selectedDate, setSelectedDate] = useState<DateObject[]>([]);
  const [status, setStatus] = useState<PRODUCTION_PROCESS_STATUS[]>([]);


  return (
    <div className='page_container'>
      <Typography level={4}>Quản lý công đoạn sản xuất</Typography>

      <ProductionProcessBody
        toggleModalUpdate={toggleModalUpdate}
        toggleModalDelete={toggleModalDelete}
        toggleModalStart={toggleModalStart}
        toggleModalStop={toggleModalStop}
        toggleModalCreate={toggleModalCreate}
        toggleModalComplete={toggleModalComplete}
        toggleModalFilter={toggleModalFilter}
        toggleNoticesUpdate={toggleNoticesUpdate}
        setSelectedRecord={setSelectedRecord}
        setSearchText={setSearchText}
        searchText={searchText}
        selectedRowKeys={selectedRowKeys}
        selectedRows={selectedRows}
        filter={filter}
        setSelectedRowKeys={setSelectedRowKeys}
        setSelectedRows={setSelectedRows}
        setFilter={setFilter}
        setSelectedDate={setSelectedDate}
        setStatus={setStatus}
      />

      {/* MODAL CREATE PRODUCTION PROCESS  */}
      <ModalCreateProcess isOpen={isOpenModalCreate} toggle={toggleModalCreate} toggleSuccess={toggleModalCreateSuccess} />

      {/* MODAL CREATE PRODUCTION PROCESS SUCCESS */}
      <ModalCreateProcessSuccess isOpen={isOpenModalCreateSuccess} toggle={toggleModalCreateSuccess} />

      {/* MODAL UPDATE PRODUCTION PROCESS */}
      <ModalUpdateProcess
        isOpen={isOpenModalUpdate}
        toggle={toggleModalUpdate}
        toggleSuccess={toggleModalUpdateSuccess}
        selectedRecord={selectedRecord}
      />

      {/* MODAL UPDATE PRODUCTION PROCESS SUCCESS */}
      <ModalUpdateProcessSuccess isOpen={isOpenModalUpdateSuccess} toggle={toggleModalUpdateSuccess} />

      {/* MODAL DELETE PRODUCTION PROCESS */}
      <ModalDeleteProcess
        isOpen={isOpenModalDelete}
        toggle={toggleModalDelete}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />

      {/* MODAL START PRODUCTION PROCESS */}
      <ModalStartProcess isOpen={isOpenModalStart} toggle={toggleModalStart} selectedRecord={selectedRecord} />

      {/* MODAL STOP PRODUCTION PROCESS */}
      <ModalStopProcess isOpen={isOpenModalStop} toggle={toggleModalStop} selectedRecord={selectedRecord} />

      {/* MODAL COMPLETE PRODUCTION PROCESS  */}
      <ModalCompleteProcess isOpen={isOpenModalComplete} toggle={toggleModalComplete} selectedRecord={selectedRecord} />

      {/* MODAL FILTER PRODUCTION PROCESS */}
      <ProductionProcessModalFilter
        isOpen={isOpenModalFilter}
        toggle={toggleModalFilter}
        setFilter={setFilter}
        selectedDate={selectedDate}
        setSelectedDate={setSelectedDate}
        status={status}
        setStatus={setStatus}
      />

      <ProductionProcessNoticesUpdateModals isOpen={isOpenNoticesUpdate} toggle={toggleNoticesUpdate} />
    </div>
  );
}; 1

export default ProductionProcess;
