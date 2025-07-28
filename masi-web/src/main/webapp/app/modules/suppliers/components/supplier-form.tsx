import { zodResolver } from '@hookform/resolvers/zod';
import BadgeV2 from 'app/components/badge/badge-v2';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import Table from 'app/components/table/table';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT } from 'app/constants/common';
import useContactType from 'app/hooks/use-contact-type';
import useSupplier from 'app/hooks/use-supplier';
import useSupplierGroup from 'app/hooks/use-supplier-group';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { ISupplier } from 'app/shared/model/supplier.model';
import {
  supplierSchema,
  SupplierSchema,
} from 'app/validation/supplier.validation';
import dayjs from 'dayjs';
import { useEffect } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useParams } from 'react-router';
import { Col, Row } from 'reactstrap';
import SupplierAttachment from './supplier-attachment';
import { convertCurrency } from 'app/shared/util/format';
import { SupplierContractsStatusBadgeMapping } from 'app/modules/supplier-contracts/supplier-contracts-mapping';
import { SUPPLIER_CONTRACT_STATUS } from 'app/shared/model/supplier-contract.model';

const { usePatchSupplier, usePostSupplier, useGetSupplierTypes } = useSupplier;
const { useGetSupplierGroups } = useSupplierGroup;
const { useGetContactTypes } = useContactType;

interface ISupplierFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
  toggleSelectItem?: () => void;
  detail?: ISupplier;
}

const SupplierForm = (props: ISupplierFormProps) => {
  const {
    type,
    toggle,
    toggleSuccess,
    selectedRecord,
    setSelectedRecord,
    toggleSelectItem,
    detail,
  } = props;
  const { id } = useParams();

  const methods = useForm<SupplierSchema>({
    resolver: zodResolver(supplierSchema),
  });

  const { control, setValue, handleSubmit, watch, formState } = methods;

  const onOkSuccess = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
    setSelectedRecord && setSelectedRecord(null);
  };

  const { mutate: update } = usePatchSupplier(id, onOkSuccess);
  const { mutate: create } = usePostSupplier(onOkSuccess);

  const { data: dataSupplierGroups, isLoading } = useGetSupplierGroups();
  const { data: dataContactTypes, isLoading: isLoadingContactType } =
    useGetContactTypes();
  const { data: dataSupplierType } = useGetSupplierTypes();

  const watchSupplierContractsDetailFormItem = watch('supplierContracts');
  const watchContactFormFormItem = watch('contacts');

  const onSubmit: SubmitHandler<SupplierSchema> = values => {
    const supplierData: ISupplier = {
      code: values?.code,
      name: values?.name,
      note: values?.note,
      email: values?.email,
      address: values?.address,
      addressService: values?.addressService,
      phone: values?.phone,
      supplierTypeId: values?.supplierTypeId,
      supplierGroupId: values?.supplierGroupId,
      fax: '',
      birthday: values?.birthday.toDate()?.toISOString(),
      paymentTermText: values?.paymentTermText,
      taxCode: values?.taxCode,
      attribute: values?.attribute,
      contacts: values.contacts ?? [],
      fullName: values?.fullName,
      position: values?.position,
      attachment: values?.attachment?.map(f => ({
        fileId: f?.fileId,
        fileName: f?.fileName,
        createdAt: f?.createdAt,
      })),
    };
    if (type === 'update') update(supplierData);
    else create(supplierData);
  };

  useEffect(() => {
    if (detail) {
      setValue('code', detail?.code);
      setValue('name', detail?.name);
      setValue('note', detail?.note);
      setValue('email', detail?.email);
      setValue('address', detail?.address);
      setValue('supplierTypeId', detail?.supplierTypeId);
      setValue('phone', detail?.phone);
      setValue('taxCode', detail?.taxCode);
      setValue('birthday', new DateObject(detail?.birthday));
      setValue('fullName', detail?.fullName);
      setValue('position', detail?.position);
      setValue('attribute.type', detail.attribute?.type)
      setValue('paymentTermText', detail?.paymentTermText);
      setValue('addressService', detail?.addressService);
      setValue(
        'contacts',
        detail?.contacts?.length
          ? detail?.contacts.map(x => ({
              ...x,
              birthDate: new Date(x.birthDate),
            }))
          : [],
      );
      setValue('supplierContracts', detail?.supplierContracts);
      setValue('supplierGroupId', detail?.supplierGroupId);

      const existAttachments = detail?.attachment?.map(x => ({
        ...x,
        createdAt: new Date(x.createdAt),
      }));
      setValue('attachment', existAttachments);
    } else {
      setValue('contacts', [
        {
          id: null,
          contactInfo: '',
          email: '',
          phone: '',
          position: '',
          birthDate: new Date(),
        },
      ]);
    }
  }, [detail]);

  return (
    <FormProvider {...methods}>
      <Form id={FORM.SUPPLIER} onSubmit={handleSubmit(onSubmit)}>
        <Flex direction="column" gap={20}>
          <Typography level={4}>Thông tin chung</Typography>
          <Row>
            <Col md={4}>
              <FormInputV2
                control={control}
                name="code"
                label="Mã nhà cung cấp"
                placeholder="Vui lòng nhập mã nhà cung cấp"
                disabled={type === 'update'}
              />
            </Col>

            <Col md={4}>
              <FormInputV2
                control={control}
                name="name"
                label="Tên nhà cung cấp"
                placeholder="Điền"
              />
            </Col>

            <Col md={4}>
              <FormInputV2
                control={control}
                name="taxCode"
                label="Mã số thuế"
                placeholder="Điền"
              />
            </Col>

            <Col md={4}>
              <FormInputV2
                control={control}
                name="addressService"
                label="Địa điểm cung cấp dịch vụ"
                placeholder="Điền"
              />
            </Col>

            <Col md={8}>
              <FormInputV2
                control={control}
                name="address"
                label="Địa chỉ"
                placeholder="Điền"
              />
            </Col>

            <Col md={4}>
              <FormSelect
                id="supplierGroupId"
                name="supplierGroupId"
                label="Nhóm"
                control={control}
                placeholder="Chọn"
                options={dataSupplierGroups?.data?.map(e => ({
                  label: `${e?.name || ''}`,
                  value: e?.id,
                }))}
                isLoading={isLoading}
              />
            </Col>

            <Col md={4}>
              <FormSelect
                id="supplierTypeId"
                name="supplierTypeId"
                label="Loại"
                control={control}
                placeholder="Chọn"
                options={dataSupplierType?.data?.map(e => ({
                  label: `${e?.name || ''}`,
                  value: e?.id,
                }))}
              />
            </Col>

            <Col md={4}>
              <FormInputV2
                control={control}
                name="paymentTermText"
                label="Thời hạn thanh toán"
                placeholder="Điền"
              />
            </Col>

            <Col md={4}>
              <FormInputV2
                control={control}
                name="note"
                label="Ghi chú"
                placeholder="Điền"
              />
            </Col>
          </Row>

          <Typography level={4}>Người đại diện</Typography>
          <Row>
            <Col md={4}>
              <FormInputV2
                control={control}
                name="fullName"
                label="Họ tên"
                placeholder="Điền"
              />
            </Col>

            <Col md={4}>
              <FormInputV2
                control={control}
                name="position"
                label="Chức vụ"
                placeholder="Điền"
              />
            </Col>

            <Col md={4}>
              <FormDatePickerV2
                control={control}
                name="birthday"
                label="Ngày sinh"
                placeholder="Chọn"
                setValue={setValue}
              />
            </Col>

            <Col md={4}>
              <FormInputV2
                type="number"
                control={control}
                name="phone"
                label="Số điện thoại"
                placeholder="Vui lòng nhập số điện thoại"
                min={0}
              />
            </Col>
            <Col md={4}>
              <FormInputV2
                control={control}
                name="email"
                label="Email"
                type="email"
                placeholder="Vui lòng nhập email"
              />
            </Col>
          </Row>

          <Typography level={4}>Hợp đồng</Typography>
          <Table
            showIndex={false}
            rowKey="id"
            columns={[
              {
                title: 'Số HĐ',
                dataIndex: 'name',
                render: (_, record) => record?.contractCode,
              },
              {
                title: 'Ngày kí',
                dataIndex: 'name',
                render: (_, record) =>
                  record?.contractDate
                    ? dayjs(record?.contractDate).format(DATE_FORMAT.DATE)
                    : '',
              },
              {
                title: 'Ngày lập',
                dataIndex: 'name',
                render: (_, record) =>
                  record?.endDate
                    ? dayjs(record?.createdAt).format(DATE_FORMAT.DATE)
                    : '',
              },
              {
                title: 'Giá trị hợp đồng',
                dataIndex: 'name',
                render: (_, record) =>
                  convertCurrency(record?.contractAmount ?? 0, false),
              },
              {
                title: '% VAT',
                dataIndex: 'name',
                render: (_, record) => '',
              },
              {
                title: 'Trạng thái',
                dataIndex: 'name',
                render: (_, record) =>
                  SupplierContractsStatusBadgeMapping(
                    record?.status as SUPPLIER_CONTRACT_STATUS,
                  ),
              },
            ]}
            dataSource={watchSupplierContractsDetailFormItem}
          />

          <Typography level={4}>Liên hệ</Typography>
          <Table
            showIndex={false}
            rowKey="id"
            columns={[
              {
                title: 'Loại',
                dataIndex: 'contactTypeId',
                render: (_text, _record, index) => (
                  <FormSelect
                    id="supplierGroup"
                    control={control}
                    name={`contacts.${index}.contactTypeId`}
                    placeholder="Chọn loại"
                    options={dataContactTypes?.data?.map(e => ({
                      label: `${e?.name || ''}`,
                      value: e?.id,
                    }))}
                    isLoading={isLoadingContactType}
                  />
                ),
              },
              {
                title: 'Liên hệ',
                dataIndex: 'contactInfo',
                render: (_text, _record, index) => (
                  <FormInputV2
                    control={control}
                    name={`contacts.${index}.contactInfo`}
                    placeholder="Vui lòng nhập liên hệ"
                  />
                ),
              },
              {
                title: 'Chức vụ',
                dataIndex: 'position',
                render: (_text, _record, index) => (
                  <FormInputV2
                    control={control}
                    name={`contacts.${index}.position`}
                    placeholder="Vui lòng nhập chức vụ"
                  />
                ),
              },
              {
                title: 'Email',
                dataIndex: 'email',
                render: (_text, _record, index) => (
                  <FormInputV2
                    control={control}
                    name={`contacts.${index}.email`}
                    placeholder="Vui lòng nhập email"
                  />
                ),
              },
              {
                title: 'Ngày sinh',
                dataIndex: 'birthDate',
                render: (_text, _record, index) => (
                  <div style={{ minWidth: '200px' }}>
                    <FormDatePicker
                      setValue={setValue}
                      control={control}
                      id={`contacts.${index}.birthDate`}
                      name={`contacts.${index}.birthDate`}
                      formState={formState}
                      placeholder="Vui lòng chọn ngày sinh"
                    />
                  </div>
                ),
              },
            ]}
            dataSource={watchContactFormFormItem}
          />

          <SupplierAttachment />
        </Flex>
      </Form>
    </FormProvider>
  );
};

export default SupplierForm;
