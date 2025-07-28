import { zodResolver } from '@hookform/resolvers/zod';
import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import Tooltip from 'app/components/tooltip/tooltip';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useInventoriesStorage from 'app/hooks/use-inventories-storage';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { MANUFACTURE_ORDER_STATUS } from 'app/shared/model/enumerations/production-command.model';
import {
  ManufactureOrderSchema,
  rawMaterial2Schema,
  RawMaterial2Schema,
} from 'app/validation/manufacture-order.validation';
import { useEffect, useState } from 'react';
import { FormProvider, useForm, useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { IconInfo } from '../icons';
import AuthGuard from 'app/components/guards/auth-guard';
import { enableDirectAdditives } from '../../production-ultis';

const { useGetFishMealInventoriesStorageQuery } = useInventoriesStorage;

export const TabMaterial2 = ({
  onSubmit,
}: {
  onSubmit: (values, complete) => void;
}) => {
  const [completeState, setCompleteState] = useState<boolean>(false);

  const methods = useForm<RawMaterial2Schema>({
    resolver: zodResolver(rawMaterial2Schema),
  });
  const { control, setValue, formState, watch, handleSubmit, resetField } =
    methods;
  const rawMaterialItems = watch('items');

  const methodPrimary = useFormContext<ManufactureOrderSchema>();
  const { watch: watchPrimary } = methodPrimary;
  const statusWatch = watchPrimary('status');
  const rawMaterial2Watch = watchPrimary('rawMaterial2');

  const { data: depreciations, refetch } =
    useGetFishMealInventoriesStorageQuery({
      size: DEFAULT_PAGE_SIZE_NAX,
    });

  const defaultTmp = {
    id: '',
    productionMaintenanceId: '',
    percentProtein: ' ',
    material: '',
    quantity: '',
    quantityUse: '',
  };

  const columns: TableColumns<any> = [
    { header: { render: 'STT' }, body: { render: ({ index }) => index + 1 } },
    {
      header: { render: 'Lô hàng' },
      body: {
        render: ({ index }) => (
          <div style={{ width: '400px' }}>
            <FormSelect
              control={control}
              id={`items.${index}.productionMaintainId`}
              name={`items.${index}.productionMaintainId`}
              placeholder="Vui lòng chọn lô hàng"
              disabled={disabled}
              options={depreciations?.data?.map(x => ({
                value: x.id,
                label: `${x.code} ( ${x.item?.percentProtein} % đạm )`,
              }))}
              onChanges={e => {
                const selected = depreciations?.data?.find(x => x.id === e);
                if (selected) {
                  setValue(
                    `items.${index}.percentProtein`,
                    `${selected?.item?.percentProtein ?? 0}`,
                  );
                  setValue(`items.${index}.material`, selected?.item?.name);
                  setValue(
                    `items.${index}.quantity`,
                    selected?.quantity?.toString(),
                  );
                }
              }}
              isOptionDisabled={e =>
                rawMaterialItems
                  ?.map(x => x?.productionMaintainId)
                  ?.includes(e?.value)
              }
            />
          </div>
        ),
      },
    },
    {
      header: { render: 'Nguyên liệu' },
      body: {
        render: ({ index }) => (
          <FormInputV2
            control={control}
            id={`items.${index}.material`}
            name={`items.${index}.material`}
            placeholder="Vui lòng chọn lô hàng"
            disabled
          />
        ),
      },
    },
    {
      header: { render: 'Tồn kho (Kg)' },
      body: {
        render: ({ index }) => (
          <FormInputV2
            control={control}
            id={`items.${index}.quantity`}
            name={`items.${index}.quantity`}
            placeholder="Vui lòng chọn lô hàng"
            disabled
          />
        ),
      },
    },
    {
      header: { render: 'KL sử dụng (Kg)' },
      body: {
        render: ({ index }) => (
          <Flex align="center" gap={8}>
            <FormInputV2
              control={control}
              id={`items.${index}.quantityUse`}
              name={`items.${index}.quantityUse`}
              placeholder="Nhập KL sử dụng"
              disabled={disabled}
            />
            <Tooltip
              label={'Số lượng sử dụng không được lớn hơn số lượng tồn kho.'}
              target="info-quantityUse"
            >
              {IconInfo('info-quantityUse')}
            </Tooltip>
          </Flex>
        ),
      },
    },
    {
      header: { render: <></> },
      body: {
        render: ({ index }) => (
          <ButtonDelete
            onClick={() => {
              const newArr = JSON.parse(
                JSON.stringify(rawMaterialItems ?? []),
              ).filter((_x, idx) => idx !== index);

              if (newArr.length === 0) {
                setValue('items', [defaultTmp]);
              } else {
                setValue('items', newArr);
              }
            }}
            disabled={rawMaterialItems?.length === 1 || disabled}
          />
        ),
      },
    },
  ];

  const disabled = enableDirectAdditives(statusWatch);

  useEffect(() => {
    if (rawMaterial2Watch) {
      setValue('mixingDate', rawMaterial2Watch?.mixingDate);
      setValue('manufactureDate', rawMaterial2Watch?.manufactureDate);
      setValue('items', rawMaterial2Watch?.items);

      if (!rawMaterial2Watch?.items && !rawMaterialItems?.length) {
        setValue('items', [defaultTmp]);
      }
    }
  }, [rawMaterial2Watch]);

  useEffect(() => {
    refetch();
  }, [statusWatch]);

  useEffect(() => {
    const cloneItems = JSON.parse(JSON.stringify(rawMaterialItems ?? []));
    cloneItems?.map((e, index) => {
      const selected = depreciations?.data?.find(
        x => x?.id === e?.productionMaintainId,
      );
      if (selected) {
        setValue(
          `items.${index}.percentProtein`,
          `${selected?.item?.percentProtein ?? 0}`,
        );
        setValue(`items.${index}.material`, selected?.item?.name);
        setValue(`items.${index}.quantity`, selected?.quantity?.toString());
      }
    });
  }, [rawMaterialItems, depreciations, statusWatch]);

  const calcTotalProteinWeight = () => {
    return rawMaterialItems?.reduce(
      (acc, e) =>
        (acc += Number(e?.quantityUse ?? 0) * Number(e?.percentProtein ?? 0)),
      0,
    );
  };

  const calcTotalWeight = () => {
    return rawMaterialItems?.reduce(
      (acc, e) => (acc += Number(e?.quantityUse ?? 0)),
      0,
    );
  };

  useEffect(() => {
    if (completeState) setCompleteState(false);
  }, [watch()]);

  return (
    <FormProvider {...methods}>
      <Form
        id={FORM.MANUFACTURE_ORDER_RAW_MATERIAL_2}
        onSubmit={handleSubmit(values => onSubmit(values, completeState))}
      >
        <Flex direction="column" gap={8}>
          <Typography level="paragraph" className="bold test-check-heading">
            Thông tin chung
          </Typography>
          <Row>
            <Col md={4}>
              <FormDatePickerV2
                control={control}
                formState={formState}
                setValue={setValue}
                name="mixingDate"
                label="Ngày trộn"
                placeholder="Vui lòng chọn ngày trộn"
                disabled={disabled}
              />
            </Col>
            <Col md={4}>
              <FormDatePickerV2
                control={control}
                formState={formState}
                setValue={setValue}
                name="manufactureDate"
                label="Ngày sản xuất"
                placeholder="Vui lòng chọn ngày sản xuất"
                disabled={disabled}
              />
            </Col>
          </Row>

          <Flex align="center" gap={20}>
            <Typography level="paragraph" className="bold test-check-heading">
              Thành phẩm
            </Typography>
            <ButtonAdd
              text="Thêm"
              onClick={() =>
                setValue('items', [...rawMaterialItems, defaultTmp])
              }
              disabled={disabled || rawMaterialItems?.length === 2}
            />
          </Flex>

          <Flex direction="column" gap={8}>
            <TableV2<any>
              table_id="maintenance_table"
              columns={columns}
              data={[...(rawMaterialItems || [])]}
              className={{ table: 'at__table' }}
            />
          </Flex>

          <Flex
            gap={140}
            justify="end"
            style={{ padding: '12px 48px 0px 0px' }}
          >
            <Typography level={6}>
              Đạm ước tính (%):{' '}
              {(calcTotalProteinWeight() / calcTotalWeight() || 0).toFixed(3)}
            </Typography>
            <Typography level={6}>
              Khối lượng ước tính (Kg): {calcTotalWeight()}
            </Typography>
          </Flex>

          <div className="divider" />
        </Flex>
        <div className="production-card__footer">
          <AuthGuard permissionKey="PRODUCTION_MANUFACTURE_ORDER.EDIT">
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
