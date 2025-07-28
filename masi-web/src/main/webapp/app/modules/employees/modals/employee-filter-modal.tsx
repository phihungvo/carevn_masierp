import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import { IEmployeeParams } from 'app/shared/model/employee.model';
import { EMPLOYEE_STATUS, PROFILE_STATES } from 'app/shared/model/enumerations/employee.model';
import React, { useState } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import employeesMapping from '../employees-mapping';
import Select from 'app/components/select/select';
import { BaseOption } from 'app/shared/model/pagination.model';
import useWorkspace from 'app/hooks/use-workspace';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { useSearchParams } from 'react-router-dom';

const { profileStatesMapping, employeeProfileStatusMapping } = employeesMapping;

const { useGetWorkspacesQuery } = useWorkspace;
interface IEmployeeFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  filter: IEmployeeParams
  setFilter: React.Dispatch<React.SetStateAction<IEmployeeParams>>;
}

const EmployeeFilterModal = (props: IEmployeeFilterModalsProps) => {
  const { isOpen, toggle, filter, setFilter } = props;

  const [workspaceIds, setWorkspaceIds] = React.useState<string[]>(filter?.workspaceIds || []);
  const [profileStates, setProfileStates] = React.useState<PROFILE_STATES[]>(filter?.profileStates || []);
  const [workspaceTypes, setWorkspaceTypes] = useState<WORKSPACE_TYPE | null>(filter?.workspaceTypes || null);
  const [employeeStatuses, setEmployeeStatuses] = useState<EMPLOYEE_STATUS | null>(filter?.employeeStatuses || null);

  const [searchParams, setSearchParams] = useSearchParams();

  const { data: workspaces, isLoading } = useGetWorkspacesQuery();

  const onChangeStatus = (e: React.ChangeEvent<HTMLInputElement>, value: PROFILE_STATES) => {
    if (e.target.checked && e.target.value === value) {
      setProfileStates(prev => [...prev, value]);
    } else {
      setProfileStates(prev => prev.filter(item => item !== value));
    }
  };

  const onOk = () => {
    setFilter(prev => ({ ...prev, profileStates, workspaceIds, workspaceTypes, employeeStatuses }));
    setSearchParams({});
    toggle();
  };

  const onCancel = () => {
    setProfileStates([]);
    setWorkspaceIds([]);
    setWorkspaceTypes(null);
    setEmployeeStatuses(null);
    setFilter(prev => ({ ...prev, workspaceIds: undefined, profileStates: [], workspaceTypes: undefined, employeeStatuses: undefined }));
    toggle();
  };

  const onChangeWorkspaceType = (e: React.ChangeEvent<HTMLInputElement>, value: WORKSPACE_TYPE) => {
    if (e.target.checked && e.target.value === value) {
      setWorkspaceTypes(value);
    }
  };

  const onChangeEmployeeStatuses = (e: React.ChangeEvent<HTMLInputElement>, value: EMPLOYEE_STATUS) => {
    if (e.target.checked && e.target.value === value) {
      setEmployeeStatuses(value);
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-employees-filter"
      okText="Áp dụng"
      cancelText="Đặt lại"
      onOk={onOk}
      onCancel={onCancel}
    >
      <FormGroup>
        <Label for="status">Trạng thái</Label>
        <Row>
          <Col md={3}>
            <FormGroup check>
              <Label for="isActive" check>
                {profileStatesMapping(PROFILE_STATES.ACTIVE)}
              </Label>
              <Input
                id="isActive"
                name="isActive"
                type="checkbox"
                value={PROFILE_STATES.ACTIVE}
                checked={profileStates.includes(PROFILE_STATES.ACTIVE)}
                onChange={e => onChangeStatus(e, PROFILE_STATES.ACTIVE)}
              />
            </FormGroup>
          </Col>

          <Col md={3}>
            <FormGroup check>
              <Label for="notIsActive" check>
                {profileStatesMapping(PROFILE_STATES.INACTIVE)}
              </Label>
              <Input
                id="notIsActive"
                name="notIsActive"
                type="checkbox"
                value={PROFILE_STATES.INACTIVE}
                checked={profileStates.includes(PROFILE_STATES.INACTIVE)}
                onChange={e => onChangeStatus(e, PROFILE_STATES.INACTIVE)}
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>
      <FormGroup>
        <Label>Tình trạng</Label>
        <Row>
          <Col md={4}>
            <FormGroup check>
              <Label check for="resinged">
                {employeeProfileStatusMapping(EMPLOYEE_STATUS.RESIGNED)}
              </Label>
              <Input
                checked={employeeStatuses === EMPLOYEE_STATUS.RESIGNED}
                id="resinged"
                name="resinged"
                type="checkbox"
                value={EMPLOYEE_STATUS.RESIGNED}
                onChange={e => onChangeEmployeeStatuses(e, EMPLOYEE_STATUS.RESIGNED)}
              />
            </FormGroup>
          </Col>
          <Col md={4}>
            <FormGroup check>
              <Label check for="working">
                {employeeProfileStatusMapping(EMPLOYEE_STATUS.WORKING)}
              </Label>
              <Input
                checked={employeeStatuses === EMPLOYEE_STATUS.WORKING}
                id="working"
                name="working"
                type="checkbox"
                value={EMPLOYEE_STATUS.WORKING}
                onChange={e => onChangeEmployeeStatuses(e, EMPLOYEE_STATUS.WORKING)}
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>
      <FormGroup>
        <Label>Loại</Label>
        <Row>
          <Col md={4}>
            <FormGroup check>
              <Label check for="office">
                Văn phòng
              </Label>
              <Input
                checked={workspaceTypes === WORKSPACE_TYPE.OFFICE}
                id="office"
                name="office"
                type="checkbox"
                value={WORKSPACE_TYPE.OFFICE}
                onChange={e => onChangeWorkspaceType(e, WORKSPACE_TYPE.OFFICE)}
              />
            </FormGroup>
          </Col>
          <Col md={4}>
            <FormGroup check>
              <Label check for="factory">
                Nhà máy
              </Label>
              <Input
                checked={workspaceTypes === WORKSPACE_TYPE.FACTORY}
                id="factory"
                name="factory"
                type="checkbox"
                value={WORKSPACE_TYPE.FACTORY}
                onChange={e => onChangeWorkspaceType(e, WORKSPACE_TYPE.FACTORY)}
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>

      <FormGroup>
        <Label htmlFor="workspaceIsd">BP / Nhà máy</Label>
        <Select<BaseOption, true>
          id="workspaceId"
          name="workspaceId"
          placeholder="Chọn BP / Nhà máy"
          onChange={value => setWorkspaceIds(value?.map(item => item?.value as string))}
          value={workspaceIds?.map(item => {
            const findWsp = workspaces?.data?.find(e => e?.id === item);
            return { label: findWsp?.name, value: item };
          })}
          options={workspaces?.data?.map(wsp => ({
            label: wsp?.name,
            value: wsp?.id,
          }))}
          isMulti
          isLoading={isLoading}
        />
      </FormGroup>
    </Modal>
  );
};

export default EmployeeFilterModal;
