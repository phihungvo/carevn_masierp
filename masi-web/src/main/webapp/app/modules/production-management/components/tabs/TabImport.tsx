import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import AuthGuard from 'app/components/guards/auth-guard';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useWarehouse from 'app/hooks/use-warehouse';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { MANUFACTURE_ORDER_TYPE } from 'app/shared/model/enumerations/production-command.model';
import {
  ManufactureOrderSchema,
  productionSaveInventorySchema,
  ProductionSaveInventorySchema,
} from 'app/validation/manufacture-order.validation';
import { useEffect, useState } from 'react';
import { FormProvider, useForm, useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { enable } from '../../production-ultis';

const { useGetWarehouses } = useWarehouse;
export const TabImport = ({
  onSubmit,
  type,
}: {
  onSubmit: (values, complete) => void;
  type?: string;
}) => {
  const [completeState, setCompleteState] = useState<boolean>(false);

  const methods = useForm<ProductionSaveInventorySchema>({
    resolver: zodResolver(productionSaveInventorySchema),
  });
  const { control, setValue, formState, handleSubmit, watch } = methods;

  const methodPrimary = useFormContext<ManufactureOrderSchema>();
  const { watch: watchPrimary, control: controlPrimary } = methodPrimary;

  const statusWatch = watchPrimary('status');
  const typeWatch = watchPrimary('typePage');

  const productionSaveInventoryWatch = watchPrimary('productionSaveInventory');

  const { data: warehouses } = useGetWarehouses({
    size: DEFAULT_PAGE_SIZE_NAX,
    'warehouseTypePage.contains': typeWatch
      ? typeWatch ===
        (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_STANDARD as string)
        ? 'SEMI_FINISHED_PRODUCTS_STORAGE'
        : 'FINISHED_PRODUCTS_STORAGE'
      : undefined,
  });

  const disabled = enable(statusWatch);

  useEffect(() => {
    if (productionSaveInventoryWatch) {
      setValue('id', productionSaveInventoryWatch?.id);
      setValue('name', productionSaveInventoryWatch?.name);
      setValue('storageId', productionSaveInventoryWatch?.storageId);
      setValue('warehouseDate', productionSaveInventoryWatch?.warehouseDate);
    }
  }, [productionSaveInventoryWatch]);

  useEffect(() => {
    if (completeState) setCompleteState(false);
  }, [watch()]);

  return (
    <FormProvider {...methods}>
      <Form
        id={FORM.MANUFACTURE_ORDER_PRODUCTION_SAVE_INVENTORY}
        onSubmit={handleSubmit(values => onSubmit(values, completeState))}
      >
        <Row>
          <Col md={4}>
            <FormInputV2
              control={control}
              name="name"
              label="Mã lưu kho"
              placeholder="Vui lòng nhập mã lưu kho"
              disabled={disabled}
            />
          </Col>
          <Col md={4}>
            <FormDatePickerV2
              control={control}
              formState={formState}
              setValue={setValue}
              name="warehouseDate"
              label="Ngày lưu kho"
              placeholder="Vui lòng chọn ngày lưu kho"
              disabled={disabled}
            />
          </Col>
          <Col md={4}>
            <FormSelect
              control={control}
              name="storageId"
              label="Nơi lưu trữ"
              placeholder="Vui lòng chọn nơi lưu trữ"
              options={warehouses?.data?.map(w => ({
                label: `${w?.code} - ${w?.name}`,
                value: w?.id,
              }))}
              disabled={disabled}
            />
          </Col>
          <Col md={4}>
            <FormInputV2
              control={controlPrimary}
              name="productionBatch.productBatchCode"
              label="Lô hàng"
              placeholder="Vui lòng chọn lô hàng"
              disabled
            />
          </Col>
          <Col md={4}>
            <FormInputV2
              control={controlPrimary}
              name="productionPackaging.packageCode"
              label="Mã đóng gói"
              placeholder="Vui lòng chọn mã đóng gói"
              disabled
            />
          </Col>
          <Col md={4}>
            <FormInputV2
              control={controlPrimary}
              name="productionPackaging.weight"
              label="Khối lượng (Kg)"
              placeholder="Vui lòng nhập khối lượng (Kg)"
              disabled
            />
          </Col>
        </Row>
        <div className="production-card__footer">
          <AuthGuard
            permissionKey={
              type == MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_ORDER
                ? 'PRODUCTION_MANUFACTURE_ORDER.EDIT'
                : 'PRODUCTION_MANUFACTURE_ORDER_STANDARD.EDIT'
            }
          >
            <ButtonV2
              type="submit"
              onClick={() => setCompleteState(true)}
              disabled={disabled}
              style={{ borderColor: '#027A48', color: '#027A48' }}
            >
              Hoàn thành
            </ButtonV2>
            <ButtonV2
              variant="solid"
              color="blue"
              type="submit"
              disabled={disabled}
            >
              Lưu
            </ButtonV2>
          </AuthGuard>
        </div>
      </Form>
    </FormProvider>
  );
};
