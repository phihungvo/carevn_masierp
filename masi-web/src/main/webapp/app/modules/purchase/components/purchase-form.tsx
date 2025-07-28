import { zodResolver } from '@hookform/resolvers/zod';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormInput from 'app/components/form/form-input';
import InputFile from 'app/components/input/input-file';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { PURCHASE_STATUS, PURCHASE_UNIT } from 'app/shared/model/enumerations/purchase.model';
import { PurchaseFormSchema, purchaseSchema } from 'app/validation/purchase.validation';
import React, { useEffect, useRef, useState } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import purchaseMapping from '../purchase-mapping';
import usePurchase from 'app/hooks/use-purchase';
import { DateObject } from 'react-multi-date-picker';
import { handleValidatePaste, handleValidDecimal } from 'app/shared/util/handle-valid-decimal';
import { DEFAULT_DECIMAL_REGEX, FILE_UTIL } from 'app/constants/common';
import { IFIle } from 'app/shared/model/file.model';
import useFile from 'app/hooks/use-file';
import Card from 'app/components/card/card';

const { purchaseUnitTextMapping } = purchaseMapping;
const { usePostPurchase, usePatchPurchase, useGetPurchaseById } = usePurchase;
const { usePostFiles } = useFile;

interface IPurchaseFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
}

const PurchaseForm = (props: IPurchaseFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const fileInputRef = useRef<HTMLInputElement>(null);

  const [fileList, setFileList] = useState<IFIle[] | null>([]);

  const { control, setValue, handleSubmit, watch, formState } = useForm<PurchaseFormSchema>({
    resolver: zodResolver(purchaseSchema),
    defaultValues: {
      createDate: new DateObject(),
    },
  });
  const unitPrice = watch('unitPrice');
  const quantity = watch('quantity');

  const { data } = useGetPurchaseById(selectedRecord);
  const { mutate: create } = usePostPurchase(toggle, toggleSuccess);
  const { mutate: update } = usePatchPurchase(selectedRecord, toggle, toggleSuccess);
  const { mutate: uploadFile, isPending: loadingUpload } = usePostFiles(setFileList);

  const onSubmit: SubmitHandler<PurchaseFormSchema> = async values => {
    if (type === 'update') {
      update({
        requestStatus: PURCHASE_STATUS.NEW,
        productName: values.productName,
        totalPrice: Number(values.totalPrice),
        unit: values.unit,
        supplier: values.supplier,
        quantity: Number(values.quantity),
        createDate: values.createDate.toDate().toISOString(),
        unitPrice: Number(values.unitPrice),
        note: values.note,
        files: fileList?.map(file => file?.id),
      });
      setFileList([]);
      setSelectedRecord(null);
      return;
    }
    create({
      requestStatus: PURCHASE_STATUS.NEW,
      productName: values.productName,
      totalPrice: Number(values.totalPrice),
      unit: values.unit,
      supplier: values.supplier,
      quantity: Number(values.quantity),
      createDate: values.createDate.toDate().toISOString(),
      unitPrice: Number(values.unitPrice),
      note: values.note,
      files: fileList?.map(file => file?.id),
    });
    setFileList([]);
  };

  useEffect(() => {
    setFileList([]);
    if (data) {
      setValue('productName', data?.productName);
      setValue('totalPrice', data?.totalPrice.toString());
      setValue('unit', data?.unit);
      setValue('supplier', data?.supplier);
      setValue('quantity', data?.quantity?.toString());
      setValue('createDate', new DateObject(data?.createDate).add(7, 'hours'));
      setValue('unitPrice', data?.unitPrice?.toString());
      setValue('note', data?.note || '');

      if (data?.purchaseRequestFiles) {
        setFileList([...data?.purchaseRequestFiles]);
      }
    }
  }, [data]);

  useEffect(() => {
    setValue('totalPrice', unitPrice && quantity ? (Number(unitPrice) * Number(quantity)).toString() : '0');
  }, [watch('unitPrice'), watch('quantity')]);

  return (
    <Form id={FORM.PURCHASE} onSubmit={handleSubmit(onSubmit)}>
      <Card header='Thông tin chung' className='card-body-padding' classNameHeader='card-header-bold'>
        <Row>
          <Col md={6}>
            <FormInput control={control} id="name" name="productName" label="Tên hàng hóa" />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="price" name="totalPrice" label="Thành tiền" disabled />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="unit" name="unit" label="Đơn vị" type="select">
              <option selected disabled>
                Chọn đơn vị
              </option>
              <option value={PURCHASE_UNIT.KG}>{purchaseUnitTextMapping(PURCHASE_UNIT.KG)}</option>
              <option value={PURCHASE_UNIT.TON}>{purchaseUnitTextMapping(PURCHASE_UNIT.TON)}</option>
              <option value={PURCHASE_UNIT.PIECE}>{purchaseUnitTextMapping(PURCHASE_UNIT.PIECE)}</option>
            </FormInput>
          </Col>

          <Col md={6}>
            <FormInput control={control} id="supplier" name="supplier" label="Nhà cung cấp" />
          </Col>

          <Col md={6}>
            <FormInput
              control={control}
              id="quantity"
              name="quantity"
              label="Số lượng"
              onChange={e => handleValidDecimal<PurchaseFormSchema>(e.target.value, 'quantity', DEFAULT_DECIMAL_REGEX, setValue)}
              onPaste={e => handleValidatePaste(e, DEFAULT_DECIMAL_REGEX)}
            />
          </Col>

          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} id="createDate" name="createDate" label="Ngày tạo" formState={formState} />
          </Col>

          <Col md={6}>
            <FormInput
              control={control}
              id="unitPrice"
              name="unitPrice"
              label="Đơn giá (vnđ)"
              onChange={e => handleValidDecimal<PurchaseFormSchema>(e.target.value, 'unitPrice', DEFAULT_DECIMAL_REGEX, setValue)}
              onPaste={e => handleValidatePaste(e, DEFAULT_DECIMAL_REGEX)}
            />
          </Col>

          <Col md={6}>
            <FormInput rows={5} control={control} id="note" name="note" label="Ghi chú" type="textarea" />
          </Col>
        </Row>

      </Card>

      <div className="divider" />

      <Card header='Tải tệp đính kèm' className='card-body-padding' classNameHeader='card-header-bold'>
        <Col md={12}>
          <Button
            key={new Date().getMilliseconds()}
            type="button"
            color="primary"
            onClick={() => fileInputRef.current?.click()}
            className="btn-upload"
            loading={loadingUpload}
          >
            <Flex align="center" gap={8}>
              <img src="content/images/vuesax/linear/paperclip.svg" alt="attach" />
              Đính kèm
              <InputFile onFileChange={file => uploadFile(file)} name="fileAttachment" hidden ref={fileInputRef} />
            </Flex>
          </Button>
        </Col>

        {!!fileList?.length && (
          <>
            <div className="divider" />
            <Flex gap={8} flexWrap="wrap">
              {fileList?.map((file, index) => (
                <React.Fragment key={file.name}>
                  <AttachmentPreview
                    name={file.name}
                    onClose={() => setFileList(prev => prev.filter(curFile => curFile?.id !== file.id))}
                    fileUrl={`${FILE_UTIL}/${file?.id}`}
                  />
                </React.Fragment>
              ))}
            </Flex>
          </>
        )}
      </Card>
    </Form>
  );
};

export default PurchaseForm;
