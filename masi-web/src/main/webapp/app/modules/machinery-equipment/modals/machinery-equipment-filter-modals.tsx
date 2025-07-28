import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import { DEFAULT_PAGE } from 'app/constants/common';
import { IsActiveOptions } from 'app/shared/model/enumerations/isActive.enum';
import { IItemParams } from 'app/shared/model/item.model';
import React, { useState } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import machineryEquipmentMapping from '../machinery-equipment-mapping';
const { machineryEquipmentStatusMap } = machineryEquipmentMapping;

interface IMachineryEquipmentFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IItemParams>>;
}

const MachineryEquipmentFilterModals = (props: IMachineryEquipmentFilterModalsProps) => {
  const { isOpen, toggle, setFilter } = props;
  const [status, setStatus] = useState<string>();

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      statusDevice: status,
    }));
    toggle();
  };

  const onCancel = () => {
    setStatus(undefined);
    setFilter(prev => ({ ...prev, page: DEFAULT_PAGE, statusDevice: undefined }));
    toggle();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="machinery-equipment-filter-modals"
      style={{ width: '500px' }}
      okText="Áp dụng"
      cancelText="Đặt lại"
      onOk={onOk}
      onCancel={onCancel}
    >
      <FormGroup>
        <Row>
          <Col md={6}>
            <Label htmlFor="status">Trạng thái</Label>
            <Input
              label="Trạng thái"
              name="status"
              id="status"
              type="select"
              onChange={e => setStatus(e.target.value)}
              value={status}
            >
              <option selected disabled>
                Chọn trạng thái
              </option>
              {machineryEquipmentStatusMap.map(x => (
                <option key={x.value} value={x.value}>
                  {x.label}
                </option>
              ))}
            </Input>
          </Col>
        </Row>
      </FormGroup>
    </Modal>
  );
};

export default MachineryEquipmentFilterModals;
