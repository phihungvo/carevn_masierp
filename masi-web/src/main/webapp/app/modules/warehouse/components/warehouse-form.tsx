import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import useWarehouse from 'app/hooks/use-warehouse';
import useWarehouseType from 'app/hooks/use-warehouse-type';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { IWarehouse } from 'app/shared/model/warehouse.model';
import { warehouseSchema, WarehouseSchema } from 'app/validation/warehouse.validation';
import React, { useEffect } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import warehouseMapping from "app/modules/warehouse/warehouse-mapping";
import { DEFAULT_PAGE } from 'app/constants/common';
import { useGetInventoriesStorageItemsAllByWarehouse } from 'app/hooks/use-inventories';

const { useCreateWarehouse, useUpdateWarehouse, useGetWarehouseById } = useWarehouse;
const { useGetWarehouseTypes } = useWarehouseType;
const { warehouseTypeSelect } = warehouseMapping

interface IWarehouseFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
}

const WarehouseForm = (props: IWarehouseFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const methods = useForm<WarehouseSchema>({
    resolver: zodResolver(warehouseSchema),
  });

  const { control, setValue, handleSubmit } = methods;

  const onOkSuccess = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
    setSelectedRecord && setSelectedRecord(null);
  };

  const warehouseTypes = warehouseTypeSelect;
  // const { data: warehouseTypes, isLoading: loadingWarehouseTypes } = useGetWarehouseTypes();
  const { data: detail } = useGetWarehouseById(selectedRecord);
  const { data: itemInventories, refetch } = useGetInventoriesStorageItemsAllByWarehouse(selectedRecord, { page: DEFAULT_PAGE,size: 1,});
  const { mutate: update } = useUpdateWarehouse(selectedRecord, onOkSuccess);
  const { mutate: create } = useCreateWarehouse(onOkSuccess);

  const onSubmit: SubmitHandler<WarehouseSchema> = values => {
    const submitValues: IWarehouse = {
      name: values.name,
      address: values.address,
      warehouseTypePage: values.warehouseTypePage,
    };

    if (type === 'update') {
      update(submitValues);
      return;
    }

    create(submitValues);
  };

  useEffect(() => {
    if (detail) {
      setValue('name', detail.name);
      setValue('address', detail.address);
      setValue('warehouseTypePage', detail.warehouseTypePage);
    }
  }, [detail]);

  const isDisabled = () => {
    return (type === "update" && detail?.createBy == 'SYSTEM') || itemInventories?.data?.length > 0;
  }

  return (
    <FormProvider {...methods}>
      <Form id={FORM.WAREHOUSE} onSubmit={handleSubmit(onSubmit)}>
        <Row>
          <Col md={6}>
            <FormInput control={control} id="name" name="name" label="Tên kho" />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="address" name="address" label="Địa chỉ" />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="warehouseTypePage"
              name="warehouseTypePage"
              placeholder="Chọn loại kho"
              label="Loại kho"
              disabled={ isDisabled() }
              options={warehouseTypes}
            />
          </Col>
        </Row>
      </Form>
    </FormProvider>
  );
};

export default WarehouseForm;
