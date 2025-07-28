import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import Flex from 'app/components/flex/flex';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import TotalMoney from 'app/components/TotalMoney/TotalMoney';
import { DATE_FORMAT } from 'app/constants/common';
import useIncomingInvoice from 'app/hooks/use-incoming-invoice';
import { PAYMENT_REQUEST_STATUS } from 'app/shared/model/enumerations/payment-request';
import { IIncomingInvoice } from 'app/shared/model/incoming-invoice.model';
import dayjs from 'dayjs';
import { useState } from 'react';
import { useFormContext } from 'react-hook-form';
import IncomingInvoiceModals from '../ModalSelectIncomingInvoice';
import './InvoiceTable.scss';
import { convertCurrency } from 'app/shared/util/format';

const { useNextIncomingInvoiceNumber } = useIncomingInvoice;

const InvoiceTable = () => {
  const methods = useFormContext();
  const { watch, setValue, control } = methods;
  const watchPaymentDetails = watch('paymentDetails');

  const [openModal, setOpenModal] = useState<boolean>(false);
  const toggleModal = () => setOpenModal(prev => !prev);

  const disabled =
    watch('status') === (PAYMENT_REQUEST_STATUS.WAITING_APPROVE as string) ||
    watch('status') === (PAYMENT_REQUEST_STATUS.CANCELLED as string) ||
    watch('status') === (PAYMENT_REQUEST_STATUS.APPROVED as string);

  const defaultPaymentDetail = {
    new: true,
    invoiceId: '',
    incomingInvoice: {
      invoiceNo: '',
      invoiceDate: new Date().toISOString(),
      totalAmount: '0',
      note: '',
    },
  };

  const { data: nextInvoiceCode } = useNextIncomingInvoiceNumber();

  const addRecordInvoice = () => {
    const existEmpty = watchPaymentDetails?.filter(
      x =>
        x.incomingInvoice.totalAmount === '' ||
        x.incomingInvoice.totalAmount === '0',
    );
    if (existEmpty.length === 0) {
      if (
        nextInvoiceCode?.nextAndIncrement &&
        nextInvoiceCode?.currentSequence
      ) {
        const startNbr = watchPaymentDetails.filter(x => !x.invoiceId);
        const nextCode = (nextInvoiceCode?.currentSequence + startNbr.length)
          .toString()
          .padStart(4, '0');
        const suffix = dayjs(new Date()).format(DATE_FORMAT.MONTH_YEAR);
        defaultPaymentDetail.incomingInvoice.invoiceNo = `HDNK${suffix}/${nextCode}`;
      }
      setValue('paymentDetails', [
        ...watchPaymentDetails,
        { ...defaultPaymentDetail },
      ]);
    }
  };

  const columns: TableColumns<any> = [
    {
      header: { render: 'Số CT' },
      body: {
        render: ({ data, index }) => {
          if (data?.invoiceId) return <>{data?.incomingInvoice?.invoiceNo}</>;
          else
            return (
              <FormInputV2
                control={control}
                id={`paymentDetails.${index}.incomingInvoice.invoiceNo`}
                name={`paymentDetails.${index}.incomingInvoice.invoiceNo`}
                placeholder="Vui lòng nhập số HĐ"
                disabled
              />
            );
        },
      },
    },
    {
      header: { render: 'Ngày CT' },
      body: {
        render: ({ data, index }) => {
          if (data?.invoiceId)
            return (
              <>
                {dayjs(data?.incomingInvoice?.invoiceDate).format(
                  DATE_FORMAT.DATE,
                )}
              </>
            );
          else
            return (
              <FormDatePickerV2
                control={control}
                id="invoiceDate"
                name={`paymentDetails.${index}.incomingInvoice.invoiceDate`}
                disabled
              />
            );
        },
      },
    },
    {
      header: { render: 'Số tiền' },
      body: {
        render: ({ data, index }) => {
          if (data?.invoiceId)
            return (
              <>
                {convertCurrency(
                  data?.incomingInvoice?.totalAmount ?? 0,
                  false,
                )}
              </>
            );
          else
            return (
              <FormInputV2
                control={control}
                id={`paymentDetails.${index}.incomingInvoice.totalAmount`}
                name={`paymentDetails.${index}.incomingInvoice.totalAmount`}
                onChange={() => addRecordInvoice()}
                disabled={disabled}
              />
            );
        },
      },
    },
    {
      header: { render: 'Ghi chú' },
      body: {
        render: ({ data, index }) => {
          if (data?.invoiceId) return <>{data?.incomingInvoice?.note}</>;
          else
            return (
              <FormInputV2
                control={control}
                id={`paymentDetails.${index}.incomingInvoice.note`}
                name={`paymentDetails.${index}.incomingInvoice.note`}
                placeholder="Vui lòng nhập ghi chú"
                disabled={disabled}
              />
            );
        },
      },
    },
    {
      header: { render: <></> },
      body: {
        render: ({ data, index }) => (
          <ButtonDelete
            onClick={() => {
              const newArr = JSON.parse(
                JSON.stringify(watchPaymentDetails),
              ).filter((_x, idx) => idx !== index);

              if (newArr.length === 0) {
                if (watchPaymentDetails[0].incomingInvoice.totalAmount !== '0')
                  setValue('paymentDetails', [{ ...defaultPaymentDetail }]);
              } else {
                setValue('paymentDetails', newArr);
              }
            }}
            disabled={
              (watchPaymentDetails?.filter(x => x.new).length === 1 &&
                data?.new) ||
              disabled
            }
          />
        ),
      },
    },
  ];

  const onChange = (data: IIncomingInvoice[]) => {
    if (data?.length) {
      const tmp = [...data]?.map(x => ({
        id: x?.['headerId'],
        invoiceId: x.id,
        incomingInvoice: {
          invoiceNo: x.invoiceNo,
          invoiceDate: x.createAt,
          content: x.note,
          totalAmount: `${x.totalAmount ?? 0}`,
          series: x.seriNumber,
          currencyCode: x.currencyCode,
          currencyId: x.currencyId,
          note: x.note,
          documentId: x.documentId,
        },
      }));
      const tmpEdit = watchPaymentDetails?.filter(x => x.invoiceId === '');
      setValue('paymentDetails', [...tmp, ...tmpEdit]);
    } else if (data !== undefined) {
      const tmpEdit = watchPaymentDetails?.filter(x => x.invoiceId === '');
      setValue('paymentDetails', [...tmpEdit]);
    }
  };

  return (
    <Flex direction="column" gap={8}>
      <div className="invoice_table_header">
        <div className="invoice_table_header_left">
          <span>Hóa đơn</span>
          <ButtonAdd
            text="Thêm hóa đơn"
            onClick={toggleModal}
            disabled={disabled}
          />
        </div>
        <div className="invoice_table_header_right">
          <TotalMoney
            total={watchPaymentDetails.reduce(
              (acc, obj) =>
                acc + (Number(obj?.incomingInvoice?.totalAmount) ?? 0),
              0,
            )}
          />
        </div>
      </div>

      <TableV2<any>
        table_id="invoice_table"
        columns={columns}
        data={[...(watchPaymentDetails ?? [])]}
        className={{ table: 'at__table' }}
      />

      <IncomingInvoiceModals
        isOpen={openModal}
        toggle={toggleModal}
        setListInComingInvoice={onChange}
      />
    </Flex>
  );
};

export default InvoiceTable;
