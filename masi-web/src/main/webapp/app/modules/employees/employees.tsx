import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import { useModalsEmployee } from 'app/hooks/use-modals-employee';
import { IEmployee, IEmployeeParams } from 'app/shared/model/employee.model';
import { EMPLOYEE_STATUS, PROFILE_STATES } from 'app/shared/model/enumerations/employee.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import React, { useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import EmployeesHeader from './employees-header';
import EmployeesTable from './employees-table';
import './employees.scss';
import EmployeeActivateModal from './modals/employee-activate-modal';
import { EmployeeActivateSuccessModals } from './modals/employee-activate-success-modals';
import EmployeeConfirmLeaveModal from './modals/employee-confirm-leave-modal';
import { EmployeeConfirmLeaveSuccessModals } from './modals/employee-confirm-leave-success-modals';
import EmployeeDeactivateModal from './modals/employee-deactivate-modal';
import { EmployeeDeactivateSuccessModals } from './modals/employee-deactivate-success-modals';
import EmployeeDetailsModal from './modals/employee-details-modal';
import EmployeeFilterModal from './modals/employee-filter-modal';
import EmployeeImportModals from './modals/employee-import-modals';
import { EmployeeImportSuccessModals } from './modals/employee-import-success-modals';
import EmployeeProvideAccountModals from './modals/employee-provide-account-modals';
import { ProvideAccountSuccessModals } from './modals/employee-provide-success-modals';
import EmployeeUploadModal from './modals/employee-upload-modal';
import { EmployeeUploadSuccessModals } from './modals/employee-upload-success-modals';
import { EmployeeActivateTimkeepingDeviceSuccessModals } from './modals/employee-activate-timkeeping-device-success-modals';

const Employees = () => {
  const [
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
    { openDeactivate, toggleDeactivate },
    { openDeactivateSuccess, toggleDeactivateSuccess },
    { openActivate, toggleActivate },
    { openActivateSuccess, toggleActivateSuccess },
    { openUpload, toggleUpload },
    { openUploadSuccess, toggleUploadSuccess },
    { openConfirmLeave, toggleConfirmLeave },
    { opeConfirmLeaveSuccess, toggleConfirmLeaveSuccess },
    { openImport, toggleImport },
    { openHistory, toggleHistory },
    { openImportSuccess, toggleImportSuccess },
    { openProvideAccount, toggleProvideAccount },
    { openProvideAccountSuccess, toggleProvideAccountSuccess },
    { openActivateTimkeepingDevice, toggleActivateTimkeepingDevicer },
  ] = useModalsEmployee();

  const [searchParams] = useSearchParams();

  const searchParamsFilter: IEmployeeParams = {
    page: parseInt(searchParams.get('page') || '0'),
    size: parseInt(searchParams.get('size') || '10'),
    search: searchParams.get('search') || '',
    workspaceIds: searchParams.get('workspaceIds')?.split(',')[0] === '' ? [] : searchParams.get('workspaceIds')?.split(','),
    workspaceTypes: searchParams.get('workspaceTypes') as WORKSPACE_TYPE || undefined,
    employeeStatuses: searchParams.get('employeeStatuses') as EMPLOYEE_STATUS || undefined,
    profileStates: searchParams.get('profileStates')
      ?.split(',')
      .filter(state => state !== '') as PROFILE_STATES[] || [],
  };

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRows, setSelectedRows] = useState<IEmployee[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IEmployeeParams>(searchParamsFilter);

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách NV</Typography>

      <Card header={<EmployeesHeader setSearchText={setSearchText} toggleFilter={toggleFilter} toggleImport={toggleImport} />}>
        <EmployeesTable
          toggleDetail={toggleDetail}
          toggleDeactivate={toggleDeactivate}
          toggleActivate={toggleActivate}
          toggleUpload={toggleUpload}
          toggleConfirmLeave={toggleConfirmLeave}
          searchText={searchText}
          filter={filter}
          setFilter={setFilter}
          setSelectedRecord={setSelectedRecord}
          selectedRows={selectedRows}
          setSelectedRows={setSelectedRows}
          toggleProvideAccount={toggleProvideAccount}
          toggleActivateTimkeepingDevicer={toggleActivateTimkeepingDevicer}
        />
      </Card>

      {/* MODAL FILTER EMPLOYEES */}
      <EmployeeFilterModal isOpen={openFilter} toggle={toggleFilter} filter={filter} setFilter={setFilter} />

      {/* MODAL EMPLOYEE'S DETAILS */}
      <EmployeeDetailsModal isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />

      {/* MODAL DEACTIVATE EMPLOYEE */}
      <EmployeeDeactivateModal
        isOpen={openDeactivate}
        toggle={toggleDeactivate}
        toggleSuccess={toggleDeactivateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <EmployeeDeactivateSuccessModals isOpen={openDeactivateSuccess} toggle={toggleDeactivateSuccess} />

      {/* MODAL ACTIVATE EMPLOYEE */}
      <EmployeeActivateModal
        isOpen={openActivate}
        toggle={toggleActivate}
        toggleSuccess={toggleActivateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <EmployeeActivateSuccessModals isOpen={openActivateSuccess} toggle={toggleActivateSuccess} />

      {/* MODAL UPLOAD FILE ATTACHMENT  */}
      <EmployeeUploadModal
        isOpen={openUpload}
        toggle={toggleUpload}
        toggleSuccess={toggleUploadSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <EmployeeUploadSuccessModals isOpen={openUploadSuccess} toggle={toggleUploadSuccess} />

      {/* MODAL CONFIRM LEAVE */}
      <EmployeeConfirmLeaveModal
        isOpen={openConfirmLeave}
        toggle={toggleConfirmLeave}
        toggleSuccess={toggleConfirmLeaveSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <EmployeeConfirmLeaveSuccessModals isOpen={opeConfirmLeaveSuccess} toggle={toggleConfirmLeaveSuccess} />

      <EmployeeImportModals isOpen={openImport} toggle={toggleImport} toggleSuccess={toggleImportSuccess} />
      <EmployeeImportSuccessModals isOpen={openImportSuccess} toggle={toggleImportSuccess} />
      <EmployeeProvideAccountModals isOpen={openProvideAccount} toggle={toggleProvideAccount} toggleSuccess={toggleProvideAccountSuccess} selectedRecord={selectedRecord} setSelectedRecord={setSelectedRecord} />
      <ProvideAccountSuccessModals isOpen={openProvideAccountSuccess} toggle={toggleProvideAccountSuccess} />

      <EmployeeActivateTimkeepingDeviceSuccessModals isOpen={openActivateTimkeepingDevice} toggle={toggleActivateTimkeepingDevicer} />
    </div>
  );
};

export default Employees;
