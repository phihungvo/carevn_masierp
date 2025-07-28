import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import useTransactionType from 'app/hooks/use-transaction-type';
import useTransferAssets from 'app/hooks/use-transfer-assets';
import useEmployee from 'app/hooks/use-employee';
import userWorkspace from 'app/hooks/use-workspace';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { ITransferAssets } from 'app/shared/model/transfer-assets.model';
import { transferAssetsSchema, TransferAssetsSchema } from 'app/validation/transfer-assets.validation';
import { useEffect, useState } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { useParams } from 'react-router';
import { Col, Row } from 'reactstrap';
import ItemTable from './ItemTable/ItemTable';
import { useAppSelector } from 'app/config/store';
import { DateObject } from 'react-multi-date-picker';
import dayjs from 'dayjs';
import FormError from 'app/components/form/form-error';


const { useGetWorkspacesQuery } = userWorkspace;
const { useCreateTransferAssets, useUpdateTransferAssetsMutation, useNextCodeTransferAssets } = useTransferAssets
const { useGetTransactionTypeQuery } = useTransactionType;
const { useGetEmployeeProfileByIdQuery } = useEmployee;


interface ITransferAssetsFormProps {
  type: 'create' | 'detail' | 'edit';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
  detail?: ITransferAssets;
}

const TransferAssetsForm = (props: ITransferAssetsFormProps) => {
  const {
    type,
    toggle,
    toggleSuccess,
    selectedRecord,
    setSelectedRecord,
    detail,
  } = props;

  const { id } = useParams();
  const methods = useForm<TransferAssetsSchema>({
    resolver: zodResolver(transferAssetsSchema),
  });

  const { control, setValue, handleSubmit, formState, getValues, watch, setError } = methods

  const toDepartmentId = watch("toDepartmentId");
  const fromDepartmentId = watch("fromDepartmentId");
  const employeeId = watch("createdBy");
  const assetTransferDetailsDTOS = watch("assetTransferDetailsDTOS");

  const onOkSuccess = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
    setSelectedRecord && setSelectedRecord(null);
  };

  const account = useAppSelector(state => state.authentication.account);
  const { mutateAsync: create } = useCreateTransferAssets(onOkSuccess);
  const { mutateAsync: update } = useUpdateTransferAssetsMutation(id,onOkSuccess);
  const { data: transactionType } = useGetTransactionTypeQuery();
  const { data: workSpace } = useGetWorkspacesQuery();
  const { data: employee, refetch: refetchEmployee } = useGetEmployeeProfileByIdQuery(employeeId ?? account.id);
  const [listItems, setListItems] = useState([])
  const [itemTableFilter, setItemTableFilter] = useState<any>()
  const nextCode = useNextCodeTransferAssets()
  const { useGetEmployeeProfilesQuery } = useEmployee;
  const { data: employeeOfFromDepartment } = useGetEmployeeProfilesQuery({
    workspaceIds: fromDepartmentId ? [fromDepartmentId] : []
  });

  const { data: employeeOfToDepartment } = useGetEmployeeProfilesQuery({
    workspaceIds: toDepartmentId ? [toDepartmentId] : []
  });

  const [fromType, setFromType] = useState(type)

  const onSubmit: SubmitHandler<TransferAssetsSchema> = values => {
    const itemData: ITransferAssets = {
      code: values.code,
      transactionTypeId: values.transactionTypeId,
      transferDate: values.transferDate ? values.transferDate.toDate().toISOString() : '',
      toDepartmentId: values.toDepartmentId,
      fromDepartmentId: values.fromDepartmentId,
      attribute: values.attribute,
      description: values.description,
      toPersonId: values.toPersonId,
      toAddress: values.toAddress,
      createdBy: values.createdBy,
      createdAt: values.createdAt.toISOString(),
      assetTransferDetailsDTOS: values.assetTransferDetailsDTOS,
    };
    if (fromType === 'detail') {
      return;
    }

    if(fromType === "edit") {
      update(itemData);
      return;
    }

    create(itemData)
      .then(data => { })
      .catch(error => {
        const codeError = error?.response?.data?.message;
        if (codeError === 'error.CODE_EXISTS') {
          setError('code', {
            type: 'manual',
            message: 'Mã điều chuyển TS đã tồn tại',
          });
        }
      });
  };

  useEffect(() => {
    if (detail) {
      setValue('code', detail.code);
      setValue('transactionTypeId', detail.transactionTypeId);
      setValue('toDepartmentId', detail.toDepartmentId);
      setValue('transferDate', detail.transferDate ? new DateObject(detail.transferDate) : null);
      setValue('fromDepartmentId', detail.fromDepartmentId);
      setValue('toAddress', detail?.toAddress);
      setValue('attribute', detail.attribute);
      setValue('description', detail.description);
      setValue('toPersonId', detail.toPersonId);
      setValue('createdAt', detail.createdAt ? new Date(detail.createdAt) : null);
      setValue('createdBy', detail?.createdBy);
      refetchEmployee().then(() => {
        const createdByName = [
          employee?.employeeCode,
          employee?.fullName,
        ].join(' - ');
        setValue('createdByName', createdByName);
      })
      setValue('assetTransferDetailsDTOS', detail.assetTransferDetailsDTOS);
      setListItems(detail.assetTransferDetailsDTOS);
      setFromType(['NEW'].indexOf(detail?.status ?? '') !== -1 ? "edit" : "detail");
    } else {
      const createdByName = [
        employee?.employeeCode,
        employee?.fullName,
      ].join(' - ');
      setValue('code', nextCode.data);
      setValue('createdBy', account.employeeId);
      setValue('createdByName', createdByName);
      setValue('createdAt', new Date());
      setListItems([]);
    }
    refetchEmployee()
  }, [detail, employee]);

  useEffect(() => {
    setItemTableFilter({
      toDepartmentId, fromDepartmentId
    })
  }, [toDepartmentId, fromDepartmentId])

  return (
    <FormProvider {...methods} >
      <Form id={FORM.TRANSFER_ASSETS} onSubmit={handleSubmit(onSubmit)}>
        <Row>
          <Col md={6} style={{
            borderRight: "1px solid #bfc1c5",
          }}>
            <Row>
              <Col md={6}>
                <FormInputV2
                  control={control}
                  id="code"
                  name="code"
                  label="Mã điều chuyển"
                  placeholder="Điền"
                  disabled
                />
              </Col>
              <Col md={6}>
                <FormSelect
                  control={control}
                  id="transactionTypeId"
                  name="transactionTypeId"
                  label="Loại phát sinh"
                  placeholder="Vui lòng chọn"
                  options={transactionType?.data?.map(s => ({
                    value: s?.id,
                    label: `${s?.code} - ${s?.name}`,
                  }))}
                  disabled={type === 'detail'}
                />
              </Col>

              <Col md={6}>
                <FormDatePickerV2
                  setValue={setValue}
                  control={control}
                  label="Ngày điều chuyển"
                  id="transferDate"
                  name="transferDate"
                  formState={formState}
                  disabled={type === 'detail'}
                />
              </Col>
              <Col md={12}>
                <FormInputV2
                  control={control}
                  id="description"
                  name="description"
                  type='textarea'
                  rows={2}
                  label="Diễn giải"
                  placeholder="Vui lòng điền"
                  disabled={type === 'detail'}
                />
              </Col>
            </Row>
          </Col>
          <Col md={6}>
            <Row>
              <Col md={6}>
                <FormSelect
                  control={control}
                  id="fromDepartmentId"
                  name="fromDepartmentId"
                  label="Từ bộ phận"
                  options={workSpace?.data?.map(s => ({
                    value: s?.id,
                    label: `${s?.name}`,
                  }))}
                  placeholder="Vui lòng chọn"
                  disabled={type === 'detail' || assetTransferDetailsDTOS?.length > 0}
                />
              </Col>
              <Col md={6}>
                <FormSelect
                  control={control}
                  id="toDepartmentId"
                  name="toDepartmentId"
                  label="Đến bộ phận"
                  options={workSpace?.data?.map(s => ({
                    value: s?.id,
                    label: `${s?.name}`,
                  }))}
                  placeholder="Vui lòng chọn"
                  disabled={type === 'detail'}
                />
              </Col>
              <Col md={6}>
                <FormSelect
                  control={control}
                  id="toPersonId"
                  name="toPersonId"
                  label="Đến người"
                  options={employeeOfToDepartment?.data?.map(s => ({
                    value: s?.id,
                    label: `${s.employeeCode} - ${s?.fullName || ''}`,
                  }))}
                  placeholder="Vui lòng chọn"
                  disabled={type === 'detail'}
                />
              </Col>

              <Col md={6}>
                <FormInputV2
                  control={control}
                  id="toAddress"
                  name="toAddress"
                  label="Địa chỉ"
                  placeholder="Vui lòng điền"
                  disabled={type === 'detail'}
                />
              </Col>

              <Col md={6}>
                <FormInputV2
                  control={control}
                  id="createdByName"
                  name="createdByName"
                  label="Người tạo"
                  disabled
                />
              </Col>
              <Col md={6}>
                <FormDatePickerV2
                  control={control}
                  formState={formState}
                  id="createdAt"
                  name="createdAt"
                  label="Ngày tạo"
                  disabled
                />
              </Col>
            </Row>
          </Col>
        </Row>
        <Row>
          <ItemTable filter={itemTableFilter}
            listItems={listItems}
            type={type}
            employeeOfFromDepartment={employeeOfFromDepartment}
            employeeOfToDepartment={employeeOfToDepartment}
            setListItems={setListItems}
          />
           {(formState.errors?.assetTransferDetailsDTOS?.message ||
            formState.errors?.assetTransferDetailsDTOS?.root) && (
            <FormError message={String(formState.errors?.assetTransferDetailsDTOS?.message ?? formState.errors?.assetTransferDetailsDTOS?.root?.message ?? '')} />
          )}
        </Row>
      </Form>
    </FormProvider>
  );
};

export default TransferAssetsForm;
