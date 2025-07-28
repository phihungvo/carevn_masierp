import { zodResolver } from '@hookform/resolvers/zod';
import { Button, Flex, Tabs } from 'antd';
import Form from 'app/components/form/form';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { IItem } from 'app/shared/model/item.model';
import { InventoryStorageSchema, inventoryStorageSchema } from 'app/validation/machinery-equipment.validation';
import { useEffect } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { useParams } from 'react-router';
import MachineryEquipmentGeneralForm from './machinery-equipment-form-general';
import MachineryEquipmentSpecificationsForm from './machinery-equipment-form-specifications';
import MachineryEquipmentMaintenanceForm from './machinery-equipment-form-maintenance';
import { IInventoriesStorage } from 'app/shared/model/transfer-assets.model';
import useInventoriesStorage from 'app/hooks/use-inventories-storage';


const { useUpdateInventoriesStorageMutation} = useInventoriesStorage;

interface IMachineryEquipmentFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
  detail?: IInventoriesStorage;
}

const MachineryEquipmentForm = (props: IMachineryEquipmentFormProps) => {
  const {
    type,
    toggle,
    toggleSuccess,
    selectedRecord,
    setSelectedRecord,
    detail,
  } = props;

  const { id } = useParams();
  const methods =  useForm<InventoryStorageSchema>({ resolver: zodResolver(inventoryStorageSchema), });
  const { control, setValue, handleSubmit, formState, setError } = methods

  const onOkSuccess = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
    setSelectedRecord && setSelectedRecord(null);
  };
  const { mutate: updateInventoriesStorage } = useUpdateInventoriesStorageMutation( id || selectedRecord,  onOkSuccess );

  const onSubmit: SubmitHandler<InventoryStorageSchema> = values => {
    const itemData: any = {
      attribute:  values.attribute,
     };
    if (detail) {
      updateInventoriesStorage(itemData);
    }
  };

  useEffect(() => {
    if (detail) {
      setValue('code', detail.code);
      setValue('item', detail.item);
      setValue('price', detail.price);
      setValue('quantity', detail.quantity);
      setValue('attribute', detail.attribute);
      if(!detail.attribute?.general?.status) {
        setValue('attribute.general.status', 'NEW');
      }
      if (detail?.attribute?.general?.history) {
        setValue("attribute.general.history", detail.attribute.general.history);
      }
      if (detail?.attribute?.maintenance?.materials) {
        setValue("attribute.maintenance.materials", detail.attribute.maintenance.materials);
      }
      if (detail?.attribute?.specification?.advanceInfo) {
        setValue("attribute.specification.advanceInfo", detail.attribute.specification.advanceInfo);
      }
      if (detail?.attribute?.specification?.attachments) {
        setValue("attribute.specification.attachments", detail.attribute.specification.attachments);
      }
      setValue('supplier', detail.supplier);
    }
  }, [detail]);

  const items = [
    {
      label: `Thông tin chung`,
      key: 'general',
      children: <MachineryEquipmentGeneralForm data={detail} />,
    },
    {
      label: `Vật tư bảo trì`,
      key: 'maintenance',
      children: <MachineryEquipmentMaintenanceForm data={detail} />,
    },
    {
      label: `Thông số kỹ thuật`,
      key: 'specifications',
      children: <MachineryEquipmentSpecificationsForm data={detail} />,
    },
  ]

  const CustomTabBar = (props) => {
    const { activeKey, onTabClick } = props;
    return (
      <>
        <Flex gap="small" wrap>
          {items.map(tab => (
            <Button
              key={tab.key}
              variant={activeKey === tab.key ? 'filled' : 'text'}
              color="default"
              onClick={() => onTabClick(tab.key)}
            >
              {tab.label}
            </Button>
          ))}
        </Flex>
        <hr />
      </>
    );
  };

  return (
    <FormProvider {...methods} >
      <Form id={FORM.MACHINERY_EQUIPMENT} onSubmit={handleSubmit(onSubmit)}>
        <Tabs defaultActiveKey="general" renderTabBar={CustomTabBar} items={items} />
      </Form>
    </FormProvider>
  );
};

export default MachineryEquipmentForm;
