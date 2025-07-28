import Modal from 'app/components/modal/modal';
import Select from 'app/components/select/select';
import { DEFAULT_PAGE } from 'app/constants/common';
import useProductionCommand from 'app/hooks/use-production-command';
import { PRODUCTION_COMMAND_STATUS } from 'app/shared/model/enumerations/production-command.model';
import { BaseOption } from 'app/shared/model/pagination.model';
import { IProductionPackageParams } from 'app/shared/model/production-package.model';
import React, { useState } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import { mapProductionPackagesStatusOptions } from '../production-packages-mapping';
import { useForm, useFormContext } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import {
  productionPackageFilterSchema,
  ProductionPackageFilterSchema,
} from 'app/validation/production-packages.validation';
import FormSelect from 'app/components/form/form-select';

const { useGetProductionCommandsQuery } = useProductionCommand;

interface IProductionPackagesFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IProductionPackageParams>>;
}

const ProductionPackagesFilterModals = (
  props: IProductionPackagesFilterModalsProps,
) => {
  const { isOpen, toggle, setFilter } = props;

  const { control, watch, setValue } = useForm<ProductionPackageFilterSchema>({
    resolver: zodResolver(productionPackageFilterSchema),
  });
  const statusWatch = watch('status');
  const manufactureOrderIdWatch = watch('manufactureOrderId');

  const { data: productionCommands, isLoading: loadingProdCommands } =
    useGetProductionCommandsQuery();

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      manufactureOrderIds: [manufactureOrderIdWatch],
      statuses: [statusWatch],
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const onCancel = () => {
    setValue('manufactureOrderId', undefined);
    setValue('status', undefined);
    setFilter(prev => ({
      ...prev,
      manufactureOrderIds: [],
      statuses: [],
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="production-packages-filter-modals"
      okText="Áp dụng"
      cancelText="Đặt lại"
      onOk={onOk}
      onCancel={onCancel}
    >
      <Row>
        <Col md={6}>
          <FormSelect
            control={control}
            id="manufactureOrderId"
            name="manufactureOrderId"
            placeholder="Chọn lệnh sản xuất"
            options={productionCommands?.data?.map(e => ({
              label: e?.name,
              value: e?.id,
            }))}
            isLoading={loadingProdCommands}
          />
        </Col>
        <Col md={6}>
          <FormSelect
            control={control}
            id="status"
            name="status"
            placeholder="Chọn trạng thái"
            options={mapProductionPackagesStatusOptions}
          />
        </Col>
      </Row>
    </Modal>
  );
};

export default ProductionPackagesFilterModals;
