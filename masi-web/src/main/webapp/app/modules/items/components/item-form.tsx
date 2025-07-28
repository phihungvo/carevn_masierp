import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import useItems from 'app/hooks/use-items';
import useItemCategory from 'app/hooks/use-items-category';
import useUom from 'app/hooks/use-uom';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { ItemCategoryCode } from 'app/shared/model/enumerations/item-category.model';
import { IItem } from 'app/shared/model/item.model';
import { itemSchema, ItemSchema } from 'app/validation/items.validation';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { useGetUoms } = useUom;
const { useGetItemsCategoryQuery } = useItemCategory;
const { useCreateItem, useGetItemByIdQuery, useUpdateItemMutation } = useItems;
interface IItemFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
}

const ItemForm = (props: IItemFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, setValue, handleSubmit, watch } = useForm<ItemSchema>({
    resolver: zodResolver(itemSchema),
  });

  const onOkSuccess = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
    setSelectedRecord && setSelectedRecord(null);
  };

  const { data: detail } = useGetItemByIdQuery(selectedRecord);
  const { mutate: update } = useUpdateItemMutation(selectedRecord, onOkSuccess);
  const { mutate: create } = useCreateItem(onOkSuccess);
  const { data: uom } = useGetUoms();
  const { data: itemCategory } = useGetItemsCategoryQuery();

  const onSubmit: SubmitHandler<ItemSchema> = values => {
    const itemData: IItem = {
      code: values.code,
      name: values.name,
      uomId: values.uomId,
      itemCategoryId: values.itemCategoryId,
      percentProtein: Number(values.percentProtein ?? 0),
      notes: values.notes,
    };
    if (type === 'update') {
      update(itemData);
      return;
    }
    create(itemData);
  };

  useEffect(() => {
    if (detail) {
      setValue('code', detail.code);
      setValue('name', detail.name);
      setValue('uomId', detail.uomId);
      setValue('itemCategoryId', detail.itemCategoryId);
      setValue('percentProtein', `${detail.percentProtein ?? ""}`);
      setValue('notes', detail.notes);
    }
  }, [detail]);
  const selectedCode = itemCategory?.data?.find(item => item.id === watch('itemCategoryId'))?.code;
  return (
    <Form id={FORM.ITEM} onSubmit={handleSubmit(onSubmit)}>
      <Row>
        <Col md={6}>
          <FormInput control={control} id="code" name="code" label="Mã vật phẩm" />
        </Col>
        <Col md={6}>
          <FormInput control={control} id="name" name="name" label="Tên vật phẩm" />
        </Col>
        <Row>
          <Col md={6}>
            <FormSelect
              control={control}
              id="uom"
              name="uomId"
              label="Đơn vị"
              options={uom?.data?.map(uomDetail => ({
                value: uomDetail?.id,
                label: uomDetail?.name,
              }))}
            />
          </Col>
          <Col md={6}>
            <FormSelect
              control={control}
              id="itemCategory"
              name="itemCategoryId"
              label="Chọn loại item"
              options={itemCategory?.data?.map(ic => ({
                value: ic?.id,
                label: ic?.name,
              }))}
            />
          </Col>
        </Row>
        {selectedCode === ItemCategoryCode.FINISHED_PRODUCT && (
          <FormInput control={control} id="percentProtein" name="percentProtein" label="Phần trăm protein" />
        )}
      </Row>
      <Row>
        <Col>
          <FormInput type="textarea" control={control} id="notes" name="notes" label="Ghi chú" />
        </Col>
      </Row>
    </Form>
  );
};

export default ItemForm;
