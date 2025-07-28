import Modal from 'app/components/modal/modal';
import Select from 'app/components/select/select';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE } from 'app/constants/common';
import useFactory from 'app/hooks/use-factory';
import useWarehouse from 'app/hooks/use-warehouse';
import { BaseOption } from 'app/shared/model/pagination.model';
import { IProductionRoutingParams } from 'app/shared/model/production-routing.model';
import React from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';

const { useFactories } = useFactory;
const { useGetWarehouses } = useWarehouse;
interface IProductionRoutingsFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IProductionRoutingParams>>;
}

const ProductionRoutingsFilterModals = (props: IProductionRoutingsFilterModalsProps) => {
  const { isOpen, toggle, setFilter } = props;
  const [storageId, setStorageId] = React.useState<string | null>(null);
  const [factoryId, setFactoryId] = React.useState<string | null>(null);

  const { data: factoryData } = useFactories();
  const { data: listStore } = useGetWarehouses()

  const onCancel = () => {
    setStorageId(null);
    setFactoryId(null);
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      factoryId: null,
      storageId: null,
    }));
    toggle();
  };

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      factoryId,
      storageId,
    }));
    toggle();
  };

  const factoryOptions = factoryData?.data?.map(factory => ({
    label: factory?.name,
    value: factory?.id,
  }));

  const storageOptions = listStore?.data?.map(storage => ({
    label: storage?.name,
    value: storage?.id,
  }));

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="production-routings-filter-modals"
      okText="Áp dụng"
      cancelText="Đặt lại"
      onCancel={onCancel}
      onOk={onOk}
    >
      <Row>
        <Col md={6}>
          <FormGroup>
            <Label for="place">Nơi sản xuất</Label>

            <Select<BaseOption>
              id="place"
              name="place"
              placeholder="Chọn nơi SX"
              onChange={(value: any) => setFactoryId(value?.value)}
              value={factoryOptions?.filter((option: any) => option.value === factoryId)}
              options={factoryOptions}
            />
          </FormGroup>
        </Col>
        <Col md={6}>
          <FormGroup>
            <Label for="storage">Nơi lưu trữ</Label>

            <Select<BaseOption>
              id="storage"
              name="storage"
              placeholder="Chọn nơi lưu trữ"
              onChange={(value: any) => setStorageId(value?.value)}
              value={storageOptions?.filter((option: any) => option.value === storageId)}
              options={storageOptions}
            />
          </FormGroup>
        </Col>
      </Row>
    </Modal>
  );
};

export default ProductionRoutingsFilterModals;
