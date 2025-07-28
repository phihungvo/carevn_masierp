import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import { DEFAULT_PAGE } from 'app/constants/common';
import { IsActiveOptions } from 'app/shared/model/enumerations/isActive.enum';
import { ISupplierParams } from 'app/shared/model/supplier.model';
import React, { useState } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';

interface ISupplierFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<ISupplierParams>>;
}

const SupplierFilterModals = (props: ISupplierFilterModalsProps) => {
  const { isOpen, toggle, setFilter } = props;

  const [status, setStatus] = useState<number>(1);

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      status: Boolean(status),
    }));
    toggle();
  };

  const onCancel = () => {
    setStatus(undefined);
    setFilter(prev => ({ ...prev, page: DEFAULT_PAGE, status: undefined }));
    toggle();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="invoice-filter-modals"
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
              onChange={e => setStatus(Number(e.target.value ?? 1))}
              value={status}
            >
              <option selected disabled>
                Chọn trạng thái
              </option>
              {IsActiveOptions.map(x => (
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

export default SupplierFilterModals;
