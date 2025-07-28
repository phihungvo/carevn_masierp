import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import { DEFAULT_PAGE } from 'app/constants/common';
import { FACTORIES_IS_ACTIVE } from 'app/shared/model/enumerations/factories.enum';
import { IFactoryParams } from 'app/shared/model/factory.model';
import React, { useState } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';

interface IFactoriesFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IFactoryParams>>;
}

const FactoriesFilterModals = (props: IFactoriesFilterModalsProps) => {
  const { isOpen, toggle, setFilter } = props;
  const [status, setStatus] = useState<string[]>([]);

  const onOk = () => {
    const isActive =
      status.length === 2 || status.length === 0
        ? undefined
        : status.includes(FACTORIES_IS_ACTIVE.IN_USE)
          ? true
          : false;

    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      'isActive.equals': isActive,
    }));
    toggle();
  };

  const onCancel = () => {
    setStatus([]);

    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      'isActive.equals': undefined,
    }));
    toggle();
  };

  const onChangeStatus = (
    e: React.ChangeEvent<HTMLInputElement>,
    value: FACTORIES_IS_ACTIVE,
  ) => {
    if (e.target.checked && e.target.value === (value as string)) {
      setStatus(prev => [...prev, value]);
    } else setStatus(prev => prev.filter(item => item !== (value as string)));
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="factories-filter-modals"
      style={{ width: '500px' }}
      okText="Áp dụng"
      cancelText="Đặt lại"
      onOk={onOk}
      onCancel={onCancel}
    >
      <FormGroup>
        <Label for="status">Tình trạng:</Label>
        <Row>
          <Col md={6}>
            <FormGroup check>
              <Label check for="approved">
                {FACTORIES_IS_ACTIVE.IN_USE}
              </Label>
              <Input
                id="approved"
                name="approved"
                type="checkbox"
                value={FACTORIES_IS_ACTIVE.IN_USE}
                checked={status.includes(FACTORIES_IS_ACTIVE.IN_USE)}
                onChange={e => onChangeStatus(e, FACTORIES_IS_ACTIVE.IN_USE)}
              />
            </FormGroup>
          </Col>
          <Col md={6}>
            <FormGroup check>
              <Label check for="rejected">
                {FACTORIES_IS_ACTIVE.DISCONTINUED}
              </Label>
              <Input
                id="rejected"
                name="rejected"
                type="checkbox"
                value={FACTORIES_IS_ACTIVE.DISCONTINUED}
                checked={status.includes(FACTORIES_IS_ACTIVE.DISCONTINUED)}
                onChange={e =>
                  onChangeStatus(e, FACTORIES_IS_ACTIVE.DISCONTINUED)
                }
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>
    </Modal>
  );
};

export default FactoriesFilterModals;
