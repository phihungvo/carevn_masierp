import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import Flex from 'app/components/flex/flex';
import FormError from 'app/components/form/form-error';
import FormWrap from 'app/components/formV2/form-wrap/form-wrap';
import GoodsSelect, {
  SelectItem,
} from 'app/components/goods-select/GoodsSelect';
import InputDelay from 'app/components/input-delay/InputDelay';
import Input from 'app/components/input/input';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import { Typography } from 'app/components/typography/typography';
import UnitSelectV2 from 'app/components/UnitSelect/UnitSelect';
import VatSelect from 'app/components/vats-select/VatSelect';
import { convertCurrency } from 'app/shared/util/format';
import {
  ConvertedGoodsType,
  ImportConvertedGoodsType
} from 'app/validation/supplies-request.validation';
import { clone, cloneDeep, sumBy } from 'lodash';
import { Controller, Path, useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import '../../../modules/PurchaseProposal/Form/Goods.scss';
import { IncomingInvoiceV2SchemaType } from '../validation/incoming.validate';

type QuickData = Record<string | number, number | string> | undefined;

type Props = {
};

const Goods = ({}: Props) => {
  const {
    watch,
    control,
    getValues,
    setValue,
    formState: { errors },
  } = useFormContext<IncomingInvoiceV2SchemaType>();
  const invoiceSupplies = watch('invoiceSupplies');

  const handleDelete = (id: number) => () => {
    let filterd = invoiceSupplies.filter((_, index) => index !== id);
    setValue('invoiceSupplies', filterd);
  };

  const onAddRow = () => {
    invoiceSupplies.push({});
    setValue('invoiceSupplies', cloneDeep(invoiceSupplies));
  };

  const onEditData =
    (index: number, key: Path<ImportConvertedGoodsType>) => (data?: any) => {
      let clonedInvoiceSupplies = clone(invoiceSupplies);
      let totalInvoice = getValues('totalInvoice');
      let item = clonedInvoiceSupplies[index]
      totalInvoice = totalInvoice || 0;

      item.price = item?.price || 0;
      item.quantity = item?.quantity || 1;
      item.totalItem = item?.totalItem || 0;
      item.totalItemAfterVat = item?.totalItemAfterVat || 0;

      if (key === 'quantity') {
        let total = item.price * data;
        let vat = 0
        if (item?.vatDTO?.percent) {
          vat = total * item?.vatDTO?.percent / 100;
        }
        item.totalItem = total
        totalInvoice -= item.totalItemAfterVat;
        totalInvoice = Math.min(totalInvoice, 0);
        item.totalItemAfterVat = total + vat;
        totalInvoice += item.totalItemAfterVat;
      }

      if (key === 'price') {
        let total = data * item.quantity;
        let vat = 0
        if (item?.vatDTO?.percent) {
          vat = total * item?.vatDTO?.percent / 100;
        }
        item.totalItem = total;
        totalInvoice -= item.totalItemAfterVat;
        totalInvoice = Math.min(totalInvoice, 0);
        item.totalItemAfterVat = total + vat;
        totalInvoice += item.totalItemAfterVat;
      }

      if (key === 'vatDTO' && item) {
        if (data?.percent) {
          let { percent: percentNew } = data;
          let total = item.price * item.quantity;
          let vat = (total * percentNew) / 100;
          totalInvoice -= item.totalItemAfterVat;
          item.totalItemAfterVat = total + vat;
          totalInvoice += item.totalItemAfterVat;
        }
        item.vatDTO = data;
      }

      if (key === 'importPercent' && item) {
        let total = item.price * item.quantity || 0;
        let vat = (total * data) / 100 || 0;
        (item as any).importTax = vat;
      }

      if (key === 'envPercent' && item) {
        let total = item.price * item.quantity || 0;
        let vat = (total * data) / 100 || 0;
        (item as any).envTax = vat;
      }

      item[key] = data;
      setValue('invoiceSupplies', clonedInvoiceSupplies);
      setValue('totalInvoice', totalInvoice);
    };

  const onChooseGood =
    (index: number) =>
    (
      data: ConvertedGoodsType['itemDTO'] & {
        vatId: string;
        vatRate: number;
        uomId: string;
      },
      selected: SelectItem,
    ) => {
      let item = invoiceSupplies[index];
      item.itemDTO = data;
      setValue(`invoiceSupplies.${index}.itemDTO`, {
        label: data?.label,
        id: data?.id,
        code: data?.code,
        name: data?.name,
      });
      if (data?.vatId) {
        setValue(`invoiceSupplies.${index}.vatDTO`, {
          label: '',
          value: data?.vatId,
          percent: data?.vatRate,
        })
      }
      setValue(`invoiceSupplies.${index}.uomDTO`, {
        label: '',
        value: data?.uomId,
      });
    };

  const columns: TableColumns<
    ImportConvertedGoodsType
  > = [
    {
      header: {
        th_style: {
          width: '80px',
        },
        render: 'STT',
      },
      body: {
        render: ({ index }) => index + 1,
      },
    },
    {
      header: {
        th_style: {
          width: '250px',
        },
        render: 'Hàng hóa',
      },
      body: {
        render: ({ data, index }) => (
          <Controller
            control={control}
            name={`invoiceSupplies.${index}.itemDTO`}
            render={({ field, fieldState: { error } }) => (
              <FormWrap error={error?.message}>
                <GoodsSelect
                  onChooseGood={onChooseGood(index)}
                  value={field?.value && [{
                    label: field?.value?.label,
                    value: field?.value?.id,
                  }]}
                />
              </FormWrap>
            )}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: '250px',
        },
        render: 'Đơn vị tính',
      },
      body: {
        render: ({ data, index }) => {
          return (
            <Controller
              control={control}
              name={`invoiceSupplies.${index}.uomDTO`}
              render={({ field }) => (
                <UnitSelectV2
                  onChange={onEditData(index, 'uomDTO')}
                  isDisabled={true}
                  selectedId={field?.value?.value}
                />
              )}
            />
          );
        },
      },
    },
    {
      header: {
        th_style: {
          width: '200px',
        },
        render: 'Số lượng',
      },
      body: {
        render: ({ index }) => (
          <Controller
            control={control}
            name={`invoiceSupplies.${index}.quantity`}
            render={({ field, fieldState: { error } }) => (
              <FormWrap error={error?.message}>
                <InputDelay
                  regrex={/\D+/ig}
                  onCompletedChange={onEditData(index, 'quantity')}
                  defaultValue={convertCurrency(field?.value)}
                  value={convertCurrency(field?.value)}
                />
              </FormWrap>
            )}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: '200px',
        },
        render: 'Đơn giá',
      },
      body: {
        render: ({ data, index }) => {
          return (
            <Controller
              control={control}
              name={`invoiceSupplies.${index}.price`}
              render={({ field, fieldState: { error } }) => (
                <FormWrap error={error?.message}>
                  <InputDelay
                    regrex={/\D+/ig}
                    onCompletedChange={(content) => {
                      let value = content?.replace(/\D+/ig, '');
                      onEditData(index, 'price')(value)
                    }}
                    defaultValue={convertCurrency(field.value)}
                    convertValue={convertCurrency as any}
                  />
                </FormWrap>
              )}
            />
          );
        },
      },
    },
    {
      header: {
        th_style: {
          width: '200px',
        },
        render: 'Thành tiền (VND)',
      },
      body: {
        render: ({ data }) => {
          return convertCurrency(data?.quantity * data?.price);
        }
      },
    },
    {
      header: {
        th_style: {
          width: '250px',
        },
        render: '% VAT',
      },
      body: {
        render: ({ index }) => (
          <Controller
            control={control}
            name={`invoiceSupplies.${index}.vatDTO`}
            render={({ field }) => (
              <VatSelect
                onChange={onEditData(index, 'vatDTO')}
                selectedID={field?.value?.value}
              />
            )}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: '200px',
        },
        render: 'VAT',
      },
      body: {
        render: ({ data }) => {
          let total = data?.price * data?.quantity;
          let vat = (total * data?.vatDTO?.percent) / 100;
          return convertCurrency(vat);
        },
      },
    },
    {
      header: {
        th_style: {
          width: '200px',
        },
        render: 'Phí trước nhập khẩu',
      },
      body: {
        render: ({ index }) => (
          <Controller
            control={control}
            name={`invoiceSupplies.${index}.feesBeforeImport`}
            render={({ field, fieldState: { error } }) => (
              <FormWrap error={error?.message}>
                <InputDelay
                  regrex={/\D+/ig}
                  onCompletedChange={onEditData(index, 'feesBeforeImport')}
                  defaultValue={convertCurrency(field.value)}
                  // convertValue={convertCurrency as any}
                />
              </FormWrap>
            )}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: '200px',
        },
        render: '% NK',
      },
      body: {
        td_props: {
          style: {
            width: '100px',
          }
        },
        render: ({ index }) => (
          <Controller
            control={control}
            name={`invoiceSupplies.${index}.importPercent`}
            render={({ field, fieldState: { error } }) => (
              <FormWrap error={error?.message}>
                <InputDelay
                  regrex={/\D+/ig}
                  onCompletedChange={onEditData(index, 'importPercent')}
                  defaultValue={convertCurrency(field?.value)}
                />
              </FormWrap>
            )}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: '200px',
        },
        render: 'Thuế NK',
      },
      body: {
        render: ({ data }) => convertCurrency(data?.importTax)
      },
    },
    {
      header: {
        th_style: {
          width: '200px',
        },
        render: '% MT',
      },
      body: {
        render: ({ index }) => (
          <Controller
            control={control}
            name={`invoiceSupplies.${index}.envPercent`}
            render={({ field, fieldState: { error } }) => (
              <FormWrap error={error?.message}>
                <InputDelay
                  regrex={/\D+/ig}
                  onCompletedChange={onEditData(index, 'envPercent')}
                  defaultValue={convertCurrency(field?.value)}
                />
              </FormWrap>
            )}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: '200px',
        },
        render: 'Thuế MT',
      },
      body: {
        render: ({ data }) => convertCurrency(data?.envTax)
      },
    },
    {
      header: {
        th_style: {
          width: '200px',
        },
        render: 'Phí sau NK',
      },
      body: {
        render: ({ index }) => (
          <Controller
            control={control}
            name={`invoiceSupplies.${index}.feesAfterImport`}
            render={({ field, fieldState: { error } }) => (
              <FormWrap error={error?.message}>
                <InputDelay
                  regrex={/\D+/ig}
                  onCompletedChange={onEditData(index, 'feesAfterImport')}
                  defaultValue={convertCurrency(field.value)}
                  // convertValue={convertCurrency as any}
                />
              </FormWrap>
            )}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: '200px',
        },
        render: 'Tổng tiền',
      },
      body: {
        render: ({ data }) => {
          let total = data?.price * data?.quantity;
          let vat = (total * data?.vatDTO?.percent) / 100;
          let importTax = data?.importTax || 0;
          let envTax = data?.envTax || 0;
          return convertCurrency(total + vat + importTax + envTax);
        }
      },
    },
    {
      header: {
        th_style: {
          width: '200px',
        },
        render: 'Ghi chú',
      },
      body: {
        render: ({ index }) => (
          <Controller
            control={control}
            name={`invoiceSupplies.${index}.note`}
            render={({ field, fieldState }) => (
              <FormWrap
                error={fieldState?.error?.message}
              >
                <Input
                  {...field}
                  placeholder="Nhập ghi chú"
                />
              </FormWrap>
            )}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: '80px',
        },
        render: <></>,
      },
      body: {
        render: ({ data, index }) => (
          <ButtonDelete
            onClick={handleDelete(index)}
          />
        ),
      },
    },
  ];

  const grandTotal = sumBy(invoiceSupplies, (item: ImportConvertedGoodsType) => {
    let total = item?.price * item?.quantity;
    let vat = (total * item?.vatDTO?.percent) / 100 || 0;
    let importTax = item?.importTax || 0;
    let envTax = item?.envTax || 0;
    return total + vat + importTax + envTax;
  });

  return (
    <>
      <Row>
        <Col md={12}>
          <Flex align="center" columnGap={32}>
            <Typography level={5}>Danh sách hàng hóa</Typography>
            <ButtonAdd text="Thêm" onClick={onAddRow} />
          </Flex>
        </Col>
      </Row>
      <Row>
        <Col md={12}>
          <TableV2<any>
            table_id="purchase-proposal-goods"
            columns={columns}
            data={invoiceSupplies}
            tableFixedLayout
            custom_body_row={() => (
              <tr>
                <td colSpan={13} />
                <td>Tổng tiền</td>
                <td colSpan={3} >{convertCurrency(grandTotal)}</td>
              </tr>
            )}
          />
        </Col>
        {errors?.invoiceSupplies && (
          <Col style={{ marginTop: '10px' }}>
            <FormError message='Danh sách hàng hóa là bắt buộc' />
          </Col>
        )}
      </Row>
    </>
  );
};

export default Goods;
