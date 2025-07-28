import { zodResolver } from '@hookform/resolvers/zod';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Card from 'app/components/card/card';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import Input from 'app/components/input/input';
import InputFile from 'app/components/input/input-file';
import { DEFAULT_DECIMAL_REGEX, DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX, FILE_UTIL } from 'app/constants/common';
import { UNIFORM_NOT_ENOUGH } from 'app/constants/error';
import useEmployee from 'app/hooks/use-employee';
import useFile from 'app/hooks/use-file';
import useUniform from 'app/hooks/use-uniform';
import useWarehouse from 'app/hooks/use-warehouse';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { UNIFORM_RELEASE_TYPE, UNIFORM_STATUS } from 'app/shared/model/enumerations/uniform.model';
import { IFIle } from 'app/shared/model/file.model';
import { IUniformStock } from 'app/shared/model/uniform.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { handleValidatePaste } from 'app/shared/util/handle-valid-decimal';
import { uniformReleaseSchema, UniformReleaseSchema } from 'app/validation/uniform.validation';
import React, { useEffect, useMemo, useRef, useState } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { Alert, Col, FormGroup, Label, Row } from 'reactstrap';
import uniformMapping from '../uniform-mapping';
import UniformListFields from './uniform-list-fields';

const { uniformReleaseTypeMapping } = uniformMapping;

const { useGetEmployeesQuery } = useEmployee;
const { usePostUniformRelease, useUniformStocks } = useUniform;
const { usePostFile } = useFile;
const { useGetWarehouses } = useWarehouse;

interface IUniformFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
  setSelectedRowKeys?: React.Dispatch<React.SetStateAction<string[]>>;
}

const UniformForm = (props: IUniformFormProps) => {
  const { type, toggle, toggleSuccess } = props;

  const fileInputRef = useRef<HTMLInputElement>(null);

  const [file, setFile] = useState<IFIle | null>(null);

  const onOkSuccess = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
  };

  const { data, isLoading } = useUniformStocks({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE_NAX,
    status: UNIFORM_STATUS.ENABLE,
  });
  const { data: employees, isLoading: empLoading } = useGetEmployeesQuery();
  const { data: warehouses, isLoading: loadingWarehouses } = useGetWarehouses({
    isNotGetWareHouseUniform: false,
  });
  const { mutate: uploadFile, isPending: loadingUpload } = usePostFile(setFile);
  const { mutate: create, error } = usePostUniformRelease(onOkSuccess);

  const methods = useForm<UniformReleaseSchema>({
    resolver: zodResolver(uniformReleaseSchema),
    defaultValues: {
      uniformDetails: [{ uniformId: '', quantity: '' }],
    },
  });

  const { control, setValue, handleSubmit, watch, formState } = methods;

  const [dataSource, setDataSource] = useState<IUniformStock[]>([]);

  const filteredData = useMemo(() => {
    return data?.data?.filter(e => e.warehouseId === watch('warehouseId')) || [];
  }, [data, watch('warehouseId')]);

  useEffect(() => {
    setDataSource(filteredData);
  }, [filteredData]);

  const onSubmit: SubmitHandler<UniformReleaseSchema> = values => {
    create({
      date: values.date?.toDate().toISOString(),
      employeeId: values.employeeId,
      // quantity: Number(values.quantity),
      warehouseId: values.warehouseId,
      note: values.note,
      type: values.type,
      cost: Number(values?.cost?.replace(/,/g, '').replace(/\./g, '')) || 0,
      isReturned: values?.type === UNIFORM_RELEASE_TYPE.SUPPORT ? values.isReturned : undefined,
      details: values?.uniformDetails?.map(item => ({
        uniformId: item.uniformId,
        quantity: Number(item.quantity?.replace(/,/g, '').replace(/\./g, '')),
        uomId: data?.data?.find(e => e.id === item.uniformId)?.uniform?.uomId ?? '',
        uomName: data?.data?.find(e => e.id === item.uniformId)?.uniform?.uomDTO?.name ?? '',
        basePrice: data?.data?.find(e => e.id === item.uniformId)?.uniform?.basePrice ?? 0,
        actualPrice: Number(item.actualPrice?.replace(/,/g, '').replace(/\./g, '')),
      })),
      fileId: file?.id,
      fileName: file?.name,
    });
  };

  const isNotEnoughStock = error?.response?.data?.message === UNIFORM_NOT_ENOUGH;

  const uniformDetails = watch('uniformDetails');
  return (
    <>
      {error && isNotEnoughStock && (
        <Alert color="danger" className="mb-4">
          Không có đủ số lượng đồng phục trong kho
        </Alert>
      )}
      <FormProvider {...methods}>
        <Form id={FORM.ORDER} onSubmit={handleSubmit(onSubmit)}>
          <Card header="Thông tin chung" className="card-body-padding" classNameHeader="card-header-bold">
            <Row>
              <Col md={6}>
                <FormDatePicker setValue={setValue} control={control} id="date" name="date" label="Ngày" formState={formState} />
              </Col>

              <Col md={6}>
                <FormSelect
                  control={control}
                  id="employeeId"
                  name="employeeId"
                  placeholder="Chọn nhân viên"
                  label="Nhân viên"
                  options={employees?.data?.map(e => ({
                    label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                    value: e?.id,
                  }))}
                  isLoading={empLoading}
                />
              </Col>

              <Col md={6}>
                <FormInput control={control} id="type" name="type" label="Loại xuất" type="select">
                  <option selected disabled>
                    Chọn loại xuất
                  </option>
                  <option value={UNIFORM_RELEASE_TYPE.SALE}>{uniformReleaseTypeMapping(UNIFORM_RELEASE_TYPE.SALE)}</option>
                  {/* <option value={UNIFORM_RELEASE_TYPE.SUPPORT}>{uniformReleaseTypeMapping(UNIFORM_RELEASE_TYPE.SUPPORT)}</option> */}
                  <option value={UNIFORM_RELEASE_TYPE.SENIORITY}>{uniformReleaseTypeMapping(UNIFORM_RELEASE_TYPE.SENIORITY)}</option>
                  <option value={UNIFORM_RELEASE_TYPE.OTHER}>{uniformReleaseTypeMapping(UNIFORM_RELEASE_TYPE.OTHER)}</option>
                </FormInput>
              </Col>

              {watch('type') !== UNIFORM_RELEASE_TYPE.SUPPORT && (
                <Col md={6}>
                  <FormInput
                    label="Chi phí"
                    control={control}
                    id="cost"
                    name={`cost`}
                    onChange={e => setValue(`cost`, formatDecimalPrecision(Number(e?.target?.value?.replace(/,/g, ''))))}
                    onPaste={e => handleValidatePaste(e, DEFAULT_DECIMAL_REGEX)}
                  />
                </Col>
              )}

              <Col md={6}>
                <FormSelect
                  control={control}
                  id="warehouseId"
                  name="warehouseId"
                  label="Kho"
                  placeholder="Chọn kho"
                  options={warehouses?.data?.filter(i => i.warehouseTypePage == 'UNIFORM_WAREHOUSE').map(w => ({
                    label: w?.name,
                    value: w?.id,
                  }))}
                  isLoading={loadingWarehouses}
                />
              </Col>

              {watch('type') === UNIFORM_RELEASE_TYPE.SUPPORT && (
                <Col md={6}>
                  <FormGroup check>
                    <Input
                      checked={watch('isReturned') !== undefined && watch('isReturned')}
                      id="isActive"
                      name="isActive"
                      type="checkbox"
                      onChange={() => setValue('isReturned', !watch('isReturned'))}
                    />
                    <Label for="isActive">Đã hoàn ứng</Label>
                  </FormGroup>
                  <FormInput hidden control={control} name="isReturned" />
                </Col>
              )}

              <Col md={12}>
                <FormInput control={control} id="note" name="note" label="Ghi chú" type="textarea" />
              </Col>
            </Row>
          </Card>

          <div className="divider" />

          <Card header="Danh sách đồng phục" className="card-body-padding" classNameHeader="card-header-bold">
            <UniformListFields data={dataSource} isLoading={isLoading} />
          </Card>

          <div className="divider" />

          <Card header="Tải tệp đính kèm" className="card-body-padding" classNameHeader="card-header-bold">
            <Button
              key={new Date().getMilliseconds()}
              type="button"
              color="primary"
              onClick={() => fileInputRef.current?.click()}
              className="btn-upload"
              loading={loadingUpload}
              disabled={loadingUpload}
            >
              <Flex align="center" gap={8}>
                <img src="content/images/vuesax/linear/paperclip.svg" alt="attach" />
                Đính kèm
                <InputFile onFileChange={file => uploadFile(file)} name="fileAttachment" hidden ref={fileInputRef} />
              </Flex>
            </Button>

            {file && (
              <>
                <div className="divider" />
                <AttachmentPreview name={file.name} onClose={() => setFile(null)} fileUrl={`${FILE_UTIL}/${file.id}`} />
              </>
            )}
          </Card>
        </Form>
      </FormProvider>
    </>
  );
};

export default UniformForm;
