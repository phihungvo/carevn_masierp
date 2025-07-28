import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import Input from 'app/components/input/input';
import { LEAVE_REQUEST_STATUS, LEAVE_REQUEST_TYPE } from 'app/shared/model/enumerations/leave-request.model';
import { ILeaveRequestParams } from 'app/shared/model/leave-request.model';
import { DEFAULT_PAGE } from 'app/constants/common';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import Select from 'app/components/select/select';
import { BaseOption } from 'app/shared/model/pagination.model';
import useWorkspace from 'app/hooks/use-workspace';
import useEmployee from 'app/hooks/use-employee';

const { useGetWorkspacesQuery } = useWorkspace;
const { useGetEmployeesQuery } = useEmployee;

// MODAL FILTER REQUEST
interface IModalFilterRequest {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<ILeaveRequestParams>>;
}

export const FilterRequestModal = (props: IModalFilterRequest) => {
  const { isOpen, toggle, setFilter } = props;

  const [status, setStatus] = useState([]);
  const [type, setType] = useState([]);
  const [workspaceType, setWorkspaceType] = useState<WORKSPACE_TYPE>();
  const [employeeIds, setEmployeeIds] = useState<string[]>([]);
  const [workspaceIds, setWorkspaceIds] = useState<string[]>([]);

  const { data: workspaces, isLoading } = useGetWorkspacesQuery();
  const { data: employees, isLoading: loadingEmployees } = useGetEmployeesQuery();

  const onChangeStatus = (e: React.ChangeEvent<HTMLInputElement>, value: string) => {
    if (e.target.checked && e.target.value === value) {
      setStatus(prev => [...prev, value]);
    } else {
      setStatus(prev => prev.filter(item => item !== value));
    }
  };

  const onChangeType = (e: React.ChangeEvent<HTMLInputElement>, value: string) => {
    if (e.target.checked && e.target.value === value) {
      setType(prev => [...prev, value]);
    } else {
      setType(prev => prev.filter(item => item !== value));
    }
  };

  const onChangeWorkspaceType = (e: React.ChangeEvent<HTMLInputElement>, value: WORKSPACE_TYPE) => {
    if (e.target.checked && e.target.value === value) {
      setWorkspaceType(value);
    }
  };

  const handleFilter = () => {
    setFilter(prev => ({
      ...prev,
      status,
      type,
      page: DEFAULT_PAGE,
      workspaceType,
      workspaceIds,
      employeeIds
    }));
    toggle();
  };

  const handleCancelFilter = () => {
    setStatus([]);
    setType([]);
    setWorkspaceType(undefined);
    setEmployeeIds([])
    setWorkspaceIds([])
    setFilter(prev => ({
      ...prev,
      status: [],
      type: [],
      page: DEFAULT_PAGE,
      workspaceType: undefined,
      employeeIds: [],
      workspaceIds: []
    }));

    toggle();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-dayoff-filter-request"
      okText="Áp dụng"
      cancelText="Đặt lại"
      onOk={handleFilter}
      onCancel={handleCancelFilter}
    >
      <FormGroup>
        <Label for="approved">Trạng thái</Label>
        <Row>
          <Col md={3}>
            <FormGroup check>
              <Label check for="accepted">
                Đã duyệt
              </Label>
              <Input
                checked={status.includes(LEAVE_REQUEST_STATUS.APPROVED)}
                id="accepted"
                name="accepted"
                type="checkbox"
                value={LEAVE_REQUEST_STATUS.APPROVED}
                onChange={e => onChangeStatus(e, LEAVE_REQUEST_STATUS.APPROVED)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="pending">
                Đợi duyệt
              </Label>
              <Input
                checked={status.includes(LEAVE_REQUEST_STATUS.PENDING)}
                id="pending"
                name="pending"
                type="checkbox"
                value={LEAVE_REQUEST_STATUS.PENDING}
                onChange={e => onChangeStatus(e, LEAVE_REQUEST_STATUS.PENDING)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="rejected">
                Từ chối
              </Label>
              <Input
                checked={status.includes(LEAVE_REQUEST_STATUS.REJECTED)}
                id="rejected"
                name="rejected"
                type="checkbox"
                value={LEAVE_REQUEST_STATUS.REJECTED}
                onChange={e => onChangeStatus(e, LEAVE_REQUEST_STATUS.REJECTED)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="cancelled">
                Đã huỷ
              </Label>
              <Input
                checked={status.includes(LEAVE_REQUEST_STATUS.CANCELLED)}
                id="cancelled"
                name="cancelled"
                type="checkbox"
                value={LEAVE_REQUEST_STATUS.CANCELLED}
                onChange={e => onChangeStatus(e, LEAVE_REQUEST_STATUS.CANCELLED)}
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>
      <FormGroup>
        <Label>Nghỉ phép</Label>
        <Row>
          <Col md={4}>
            <FormGroup check>
              <Label check for="unpaidLeave">
                Không lương
              </Label>
              <Input
                checked={type.includes(LEAVE_REQUEST_TYPE.UNPAID_LEAVE)}
                id="unpaidLeave"
                name="unpaidLeave"
                type="checkbox"
                value={LEAVE_REQUEST_TYPE.UNPAID_LEAVE}
                onChange={e => onChangeType(e, LEAVE_REQUEST_TYPE.UNPAID_LEAVE)}
              />
            </FormGroup>
          </Col>
          <Col md={4}>
            <FormGroup check>
              <Label check for="maternity">
                Thai sản
              </Label>
              <Input
                checked={type.includes(LEAVE_REQUEST_TYPE.MATERNITY_LEAVE)}
                id="maternity"
                name="maternity"
                type="checkbox"
                value={LEAVE_REQUEST_TYPE.MATERNITY_LEAVE}
                onChange={e => onChangeType(e, LEAVE_REQUEST_TYPE.MATERNITY_LEAVE)}
              />
            </FormGroup>
          </Col>
          <Col md={4}>
            <FormGroup check>
              <Label check for="wedding">
                Kết hôn
              </Label>
              <Input
                checked={type.includes(LEAVE_REQUEST_TYPE.WEDDING_LEAVE)}
                id="wedding"
                name="wedding"
                type="checkbox"
                value={LEAVE_REQUEST_TYPE.WEDDING_LEAVE}
                onChange={e => onChangeType(e, LEAVE_REQUEST_TYPE.WEDDING_LEAVE)}
              />
            </FormGroup>
          </Col>
          <Col md={4}>
            <FormGroup check>
              <Label check for="funeral">
                Tang chế
              </Label>
              <Input
                checked={type.includes(LEAVE_REQUEST_TYPE.FUNERAL_LEAVE)}
                id="funeral"
                name="funeral"
                type="checkbox"
                value={LEAVE_REQUEST_TYPE.FUNERAL_LEAVE}
                onChange={e => onChangeType(e, LEAVE_REQUEST_TYPE.FUNERAL_LEAVE)}
              />
            </FormGroup>
          </Col>
          <Col md={4}>
            <FormGroup check>
              <Label check for="sick">
                Bệnh
              </Label>
              <Input
                checked={type.includes(LEAVE_REQUEST_TYPE.SICK_LEAVE)}
                id="sick"
                name="sick"
                type="checkbox"
                value={LEAVE_REQUEST_TYPE.SICK_LEAVE}
                onChange={e => onChangeType(e, LEAVE_REQUEST_TYPE.SICK_LEAVE)}
              />
            </FormGroup>
          </Col>
          <Col md={4}>
            <FormGroup check>
              <Label check for="annual">
                Thường niên
              </Label>
              <Input
                checked={type.includes(LEAVE_REQUEST_TYPE.ANNUAL_LEAVE)}
                id="annual"
                name="annual"
                type="checkbox"
                value={LEAVE_REQUEST_TYPE.ANNUAL_LEAVE}
                onChange={e => onChangeType(e, LEAVE_REQUEST_TYPE.ANNUAL_LEAVE)}
              />
            </FormGroup>
          </Col>
          <Col md={4}>
            <FormGroup check>
              <Label check for="compensation">
                Nghỉ bù
              </Label>
              <Input
                checked={type.includes(LEAVE_REQUEST_TYPE.COMPENSATION_LEAVE)}
                id="compensation"
                name="compensation"
                type="checkbox"
                value={LEAVE_REQUEST_TYPE.COMPENSATION_LEAVE}
                onChange={e => onChangeType(e, LEAVE_REQUEST_TYPE.COMPENSATION_LEAVE)}
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>
      <FormGroup>
        <Label>BP / Nhà máy</Label>
        <Row>
          <Col md={4}>
            <FormGroup check>
              <Label check for="office">
                Văn phòng
              </Label>
              <Input
                checked={workspaceType === WORKSPACE_TYPE.OFFICE}
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
                checked={workspaceType === WORKSPACE_TYPE.FACTORY}
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
        <Label htmlFor="workspaceIsd">Phòng ban</Label>
        <Select<BaseOption, true>
          id="workspaceId"
          name="workspaceId"
          placeholder="Chọn phòng ban"
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

      <FormGroup>
        <Label htmlFor="employeeIds">Nhân viên</Label>
        <Select<BaseOption, true>
          id="employeeIds"
          name="employeeIds"
          placeholder="Chọn nhân viên"
          onChange={value => setEmployeeIds(value?.map(item => item?.value as string))}
          value={employeeIds?.map(item => {
            const findEmp = employees?.data?.find(e => e?.id === item);
            return { label: (findEmp?.lastName || '') + ' ' + (findEmp?.firstName || ''), value: item };
          })}
          options={employees?.data?.map(e => ({
            label: (e?.lastName || '') + ' ' + (e?.firstName || ''),
            value: e?.id,
          }))}
          isMulti
          isLoading={loadingEmployees}
        />
      </FormGroup>
    </Modal>
  );
};

// MODAL NOTE REQUEST
interface IModalNoteRequest {
  isOpen: boolean;
  toggle: () => void;
}

export const NoteRequestModal = (props: IModalNoteRequest) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} cancel={null}>
      <Typography level={3}>Lưu ý quan trọng</Typography>
      <Typography level={4}>Bạn phải bổ sung yêu cầu trước 1 ngày hoặc sau 5 ngày kể từ khi lập đơn nếu có việc đột xuất</Typography>
    </Modal>
  );
};
