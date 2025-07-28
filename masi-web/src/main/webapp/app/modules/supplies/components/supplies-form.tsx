import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import useItems from 'app/hooks/use-items';
import useItemCategory from 'app/hooks/use-items-category';
import useItemType from 'app/hooks/use-items-type';
import useSupplier from 'app/hooks/use-supplier';
import useUom from 'app/hooks/use-uom';
import useVatRate from 'app/hooks/use-vat-rate';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { IItem } from 'app/shared/model/item.model';
import { itemSchema, ItemSchema } from 'app/validation/items.validation';
import { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useParams } from 'react-router';
import { Col, Row } from 'reactstrap';

const { useGetUoms } = useUom;
const { useGetItemsCategoryQuery } = useItemCategory;
const { useGetItemsTypeQuery } = useItemType;
const { useCreateItem, useUpdateItemMutation } = useItems;
const { useGetSuppliers } = useSupplier;
const { useGetVatRates } = useVatRate;

interface IItemFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
  detail?: IItem;
}

const SuppliesForm = (props: IItemFormProps) => {
  const {
    type,
    toggle,
    toggleSuccess,
    selectedRecord,
    setSelectedRecord,
    detail,
  } = props;

  const { id } = useParams();

  const { control, setValue, handleSubmit, formState, setError } =
    useForm<ItemSchema>({
      resolver: zodResolver(itemSchema),
    });

  const onOkSuccess = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
    setSelectedRecord && setSelectedRecord(null);
  };

  const { mutate: update } = useUpdateItemMutation(
    id || selectedRecord,
    onOkSuccess,
  );
  const { mutateAsync: create } = useCreateItem(onOkSuccess);

  const { data: uom } = useGetUoms();
  const { data: itemType } = useGetItemsTypeQuery();
  const { data: itemCategory } = useGetItemsCategoryQuery();
  const { data: suppliers } = useGetSuppliers();
  const { data: vatRates } = useGetVatRates();

  const onSubmit: SubmitHandler<ItemSchema> = values => {
    const itemTypeSelected = itemType?.data?.find(
      x => x?.id === values?.itemTypeId,
    );

    itemType;
    const itemData: IItem = {
      code: values.code,
      name: values.name,
      uomId: values.uomId,
      itemCategoryId: values.itemCategoryId,
      supplierId: values.supplierId,
      percentProtein: Number(values.percentProtein ?? 0),
      vatRate: values.vatRate,
      attribute: {
        nameEng: values.nameEng ?? undefined,
        origin: values?.origin ?? undefined,
      },
      notes: values.notes,
      unitPrice: Number(values.unitPrice ?? 0),
      itemTypeId: values?.itemTypeId ?? '',
      itemType: itemTypeSelected ? itemTypeSelected?.itemType : undefined,
    };
    if (type === 'update') {
      update(itemData);
      return;
    }
    create(itemData)
      .then(data => {})
      .catch(error => {
        const codeError = error?.response?.data?.message;
        if (codeError === 'error.CODE_EXISTS') {
          setError('code', {
            type: 'manual',
            message: 'Mã hàng hoá đã tồn tại',
          });
        }
      });
  };

  useEffect(() => {
    if (detail) {
      setValue('code', detail.code);
      setValue('name', detail.name);
      setValue('uomId', detail.uomId);
      setValue('itemCategoryId', detail.itemCategoryId);
      setValue('notes', detail.notes);
      setValue('supplierId', detail.supplierId);
      setValue('vatRate', detail.vatRate);
      setValue('percentProtein', `${detail.percentProtein ?? ''}`);
      setValue(
        'origin',
        Object.keys(detail?.attribute).length
          ? detail.attribute?.['origin']
          : '',
      );
      setValue(
        'nameEng',
        Object.keys(detail?.attribute).length
          ? detail.attribute?.['nameEng']
          : '',
      );
      setValue('unitPrice', detail?.unitPrice.toString());
      setValue('itemTypeId', detail?.itemTypeId);
    }
  }, [detail, itemCategory, itemType]);

  return (
    <Form id={FORM.SUPPLIES} onSubmit={handleSubmit(onSubmit)}>
      <Row>
        <Col md={3}>
          <FormInputV2
            control={control}
            id="code"
            name="code"
            label="Mã hàng hoá"
            placeholder="Điền"
            disabled={type === 'update'}
          />
        </Col>
        <Col md={3}>
          <FormInputV2
            control={control}
            id="name"
            name="name"
            label="Tên hàng hóa"
            placeholder="Điền"
          />
        </Col>
        <Col md={3}>
          <FormInputV2
            control={control}
            id="name"
            name="nameEng"
            label="Tên tiếng Anh"
            placeholder="Điền"
          />
        </Col>

        <Col md={3}>
          <FormInputV2
            control={control}
            id="code"
            name="origin"
            label="Xuất xứ"
            placeholder="Vui lòng nhập xuất xứ"
          />
        </Col>

        <Col md={3}>
          <FormSelect
            control={control}
            id="itemCategory"
            name="itemCategoryId"
            label="Nhóm"
            placeholder="Vui lòng chọn nhóm"
            options={itemCategory?.data?.map(s => ({
              value: s?.id,
              label: `${s?.code} - ${s?.name}`,
            }))}
          />
        </Col>
        <Col md={3}>
          <FormSelect
            control={control}
            id="itemType"
            name="itemTypeId"
            label="Loại"
            options={itemType?.data?.map(s => ({
              value: s?.id,
              label: `${s?.code} - ${s?.name}`,
            }))}
            placeholder="Vui lòng chọn loại"
          />
        </Col>
        <Col md={3}>
          <FormSelect
            control={control}
            id="uom"
            name="uomId"
            label="Đơn vị tính"
            options={uom?.data?.map(uomDetail => ({
              value: uomDetail?.id,
              label: uomDetail?.name,
            }))}
            placeholder="Vui lòng chọn DVT"
          />
        </Col>

        <Col md={3}>
          <FormSelect
            control={control}
            id="vatRate"
            name="vatRate"
            label="VAT"
            placeholder="VAT"
            options={vatRates?.data?.map(s => ({
              value: s?.value,
              label: `${s?.name}`,
            }))}
          />
        </Col>

        <Col md={3}>
          <FormInputV2
            control={control}
            type="number"
            id="name"
            name="unitPrice"
            label="Đơn giá"
            placeholder="Điền"
            min={0}
          />
        </Col>

        <Col md={3}>
          <FormSelect
            control={control}
            id="supplier"
            name="supplierId"
            label="Nhà cung cấp"
            placeholder="Mã - Tên NCC"
            options={suppliers?.data?.map(ic => ({
              value: ic?.id,
              label: `${ic?.code} - ${ic?.name}`,
            }))}
          />
        </Col>

        <Col md={3}>
          <FormInputV2
            control={control}
            type="number"
            id="percentProtein"
            name="percentProtein"
            label="Tỉ lệ Đạm"
            placeholder="Điền"
            min={0}
          />
        </Col>

        <Col md={12}>
          <FormInputV2
            type="textarea"
            control={control}
            id="notes"
            name="notes"
            label="Ghi chú"
            placeholder="Vui lòng nhập ghi chú"
          />
        </Col>
      </Row>
    </Form>
  );
};

export default SuppliesForm;
