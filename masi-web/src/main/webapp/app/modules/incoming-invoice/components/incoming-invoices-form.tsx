import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import TabContentItem from 'app/components/tabContent/tabContent';
import Table from 'app/components/table/table';
import Tabs from 'app/components/tabs/tabs';
import useEmployee from 'app/hooks/use-employee';
import useIncomingInvoice from 'app/hooks/use-incoming-invoice';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  incomingInvoiceSchema,
  IncomingInvoiceSchema,
} from 'app/validation/incoming-invoice.validation';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { useGetEmployeesQuery } = useEmployee;
const {
  useCreateIncomingInvoice,
  useIncomingInvoiceById,
  useNextIncomingInvoiceNumber,
  useUpdateIncomingInvoice,
} = useIncomingInvoice;

interface IIncomingInvoiceFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
  setSelectedRowKeys?: React.Dispatch<React.SetStateAction<string[]>>;
}

export default function IncomingInvoicesForm(props: IIncomingInvoiceFormProps) {
  const { type, toggle, toggleSuccess, selectedRecord } = props;

  const onOk = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
  };

  const { data, isLoading } = useGetEmployeesQuery();
  const { data: detail } = useIncomingInvoiceById(selectedRecord);
  const { mutate: create } = useCreateIncomingInvoice(onOk);
  const { mutate: update } = useUpdateIncomingInvoice(onOk);
  const { data: nextNo } = useNextIncomingInvoiceNumber();

  const { control, setValue, handleSubmit, watch } =
    useForm<IncomingInvoiceSchema>({
      resolver: zodResolver(incomingInvoiceSchema),
    });

  const watchSupplierGoodsDetailFormItem = watch('goods');

  const onSubmit: SubmitHandler<IncomingInvoiceSchema> = values => {
    // if (type === 'update') {
    //   update({
    //     totalAmount: Number(values.totalAmount?.replace(/,/g, '').replace(/\./g, '')) || 0,
    //     content: values.content,
    //     files: [],
    //     departmentId: values.departmentId,
    //     documentId: values.documentId,
    //     employeeId: values.employeeId,
    //     invoiceDate: values.invoiceDate.toDate().toISOString(),
    //     invoiceType: values.invoiceType,
    //     note: values.note,
    //     invoiceNo: values.invoiceNo,
    //     id: selectedRecord,
    //   });
    //   return;
    // }
    // create({
    //   totalAmount: Number(values.totalAmount?.replace(/,/g, '').replace(/\./g, '')) || 0,
    //   content: values.content,
    //   files: [],
    //   departmentId: values.departmentId,
    //   documentId: values.documentId,
    //   employeeId: values.employeeId,
    //   invoiceDate: values.invoiceDate.toDate().toISOString(),
    //   invoiceType: values.invoiceType,
    //   note: values.note,
    //   invoiceNo: 'HD001',
    // });
  };

  useEffect(() => {
    if (nextNo?.nextAndIncrement && type === 'create')
      setValue('invoiceNo', nextNo?.nextAndIncrement);
  }, [nextNo, type]);

  useEffect(() => {
    if (detail) {
      setValue('invoiceNo', detail?.invoiceNo);
      setValue('seriNumber', detail?.seriNumber);
      setValue('nbr', detail?.nbr);
      setValue('createAt', detail?.createAt);
      setValue('supplierId', detail?.supplierId);
      setValue('address', detail?.address);
      setValue('createBy', detail?.createBy);
      setValue('representative', detail?.representative);
      setValue('note', detail?.note);
      setValue('phone', detail?.phone);
      setValue('taxCode', detail?.taxCode);
      setValue('paymentMethod', detail?.paymentMethod);
      setValue('status', detail?.status);
      setValue('info', detail?.info);
      setValue('warehouse', detail?.warehouse);
      setValue('goods', detail?.goods);
    } else {
      setValue('goods', [
        {
          id: '',
          code: '',
          name: '',
          detail: '',
          unit: '',
          quantity: '',
          unitPrice: '',
          totalAmount: '',
          tax: '',
        },
      ]);
    }
  }, [detail]);

  return (
    <Form id={FORM.INCOMING_INVOICE} onSubmit={handleSubmit(onSubmit)}>
      <Row>
        <Col md={6}>
          <Row>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Mẫu số"
              />
            </Col>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Seri"
              />
            </Col>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Số hóa đơn"
              />
            </Col>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Ngày hóa đơn"
              />
            </Col>
            <Col md={12}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Nhà cung cấp"
              />
            </Col>
            <Col md={12}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Địa chỉ"
              />
            </Col>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Người lập"
              />
            </Col>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Ngày lập"
              />
            </Col>
            <Col md={12}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Người đại diện"
              />
            </Col>
            <Col md={12}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Diễn dãi"
              />
            </Col>
          </Row>
        </Col>
        <Col md={1}></Col>
        <Col md={5}>
          <Row>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Ngày PS"
              />
            </Col>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Hoa hồng"
              />
            </Col>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Số điện thoại"
              />
            </Col>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Loại tiền"
              />
            </Col>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="MST"
              />
            </Col>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Thanh toán"
              />
            </Col>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Tình trạng"
              />
            </Col>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Số ngày công nợ"
              />
            </Col>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Kho"
              />
            </Col>
            <Col md={6}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Ngày"
              />
            </Col>
            <Col md={12}>
              <FormInput
                control={control}
                id="documentaryNum"
                name="invoiceNo"
                label="Thông tin"
              />
            </Col>
          </Row>
        </Col>
        <Col md={12}>
          <Tabs
            header={[
              { id: 'items', title: 'Hàng hóa' },
              { id: 'ts-ccdc', title: 'Tài sản công cụ' },
            ]}
          >
            <TabContentItem id="items">
              <Table
                rowKey="id"
                columns={[
                  {
                    title: 'Hàng hóa',
                    dataIndex: 'code',
                    render: (_, record) => record?.code,
                  },
                  {
                    title: 'TKTH',
                    dataIndex: 'code',
                    render: (_, record) => record?.code,
                  },
                  {
                    title: 'Chi tiết 1',
                    dataIndex: 'detail',
                    render: (_, record) => record?.detail,
                  },
                  {
                    title: 'Chi tiết 2',
                    dataIndex: 'detail',
                    render: (_, record) => record?.detail,
                  },
                  {
                    title: 'Diễn giải',
                    dataIndex: 'detail',
                    render: (_, record) => record?.detail,
                  },
                  {
                    title: 'ĐVT',
                    dataIndex: 'unit',
                    render: (_, record) => record?.unit,
                  },
                  {
                    title: 'Số lượng',
                    dataIndex: 'quantity',
                    render: (_, record) => record?.quantity,
                  },
                  {
                    title: 'Đơn giá',
                    dataIndex: 'unitPrice',
                    render: (_, record) => record?.unitPrice,
                  },
                  {
                    title: 'Thành tiền',
                    dataIndex: 'totalAmount',
                    render: (_, record) => record?.totalAmount,
                  },
                  {
                    title: 'Thuế',
                    dataIndex: 'tax',
                    render: (_, record) => record?.tax,
                  },
                ]}
                dataSource={watchSupplierGoodsDetailFormItem}
              />
            </TabContentItem>
            <TabContentItem id="ts-ccdc">
              <Table
                rowKey="id"
                columns={[
                  {
                    title: 'Hàng hóa',
                    dataIndex: 'code',
                    render: (_, record) => record?.code,
                  },
                  {
                    title: 'TKTH',
                    dataIndex: 'code',
                    render: (_, record) => record?.code,
                  },
                  {
                    title: 'Chi tiết 1',
                    dataIndex: 'detail',
                    render: (_, record) => record?.detail,
                  },
                  {
                    title: 'Chi tiết 2',
                    dataIndex: 'detail',
                    render: (_, record) => record?.detail,
                  },
                  {
                    title: 'Diễn giải',
                    dataIndex: 'detail',
                    render: (_, record) => record?.detail,
                  },
                  {
                    title: 'ĐVT',
                    dataIndex: 'unit',
                    render: (_, record) => record?.unit,
                  },
                  {
                    title: 'Số lượng',
                    dataIndex: 'quantity',
                    render: (_, record) => record?.quantity,
                  },
                  {
                    title: 'Đơn giá',
                    dataIndex: 'unitPrice',
                    render: (_, record) => record?.unitPrice,
                  },
                  {
                    title: 'Thành tiền',
                    dataIndex: 'totalAmount',
                    render: (_, record) => record?.totalAmount,
                  },
                  {
                    title: 'Thuế',
                    dataIndex: 'tax',
                    render: (_, record) => record?.tax,
                  },
                ]}
                dataSource={watchSupplierGoodsDetailFormItem}
              />
            </TabContentItem>
          </Tabs>
        </Col>
      </Row>
    </Form>
  );
}
