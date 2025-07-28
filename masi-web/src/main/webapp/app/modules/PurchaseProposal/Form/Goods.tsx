import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import Flex from 'app/components/flex/flex';
import FormError from 'app/components/form/form-error';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
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
  ConvertedGoodsType
} from 'app/validation/supplies-request.validation';
import { clone, cloneDeep, sumBy } from 'lodash';
import { Controller, Path, useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import '../../../modules/PurchaseProposal/Form/Goods.scss';

type Props = {
};

const Goods = ({}: Props) => {
  const {
    watch,
    control,
    getValues,
    setValue,
    formState: { errors },
  } = useFormContext<any>();
  const invoiceSupplies = watch('invoiceSupplies');
  const totalInvoice = watch('totalInvoice');
  const contracts = watch('supplierContracts');

  const handleDelete = (id: number) => () => {
    let filterd = invoiceSupplies.filter((_, index) => index !== id);
    setValue('invoiceSupplies', filterd);
  };

  const onAddRow = () => {
    invoiceSupplies.push({
      quantity: 1,
      price: 0,
      totalItem: 0,
      totalItemAfterVat: 0,
      note: '',
    });
    setValue('invoiceSupplies', cloneDeep(invoiceSupplies));
  };

  const onEditData =
    (index: number, key: Path<ConvertedGoodsType>) => (data?: any) => {
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
      setValue(`invoiceSupplies.${index}.vatDTO`, {
        label: '',
        value: data?.vatId,
        percent: data?.vatRate,
      });
      setValue(`invoiceSupplies.${index}.uomDTO`, {
        label: '',
        value: data?.uomId,
      });
    };

  const columns: TableColumns<any> = [
    {
      header: {
        render: 'STT',
      },
      body: {
        render: ({ index }) => index + 1,
      },
    },
    {
      header: {
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
                  value={field.value}
                />
              </FormWrap>
            )}
          />
        ),
      },
    },
    {
      header: {
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
        render: 'Thành tiền (VND)',
      },
      body: {
        render: ({ index }) => convertCurrency(invoiceSupplies?.[index]?.totalItem),
      },
    },
    {
      header: {
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
        render: 'Tổng tiền',
      },
      body: {
        render: ({ data }) => convertCurrency(data?.totalItemAfterVat),
      },
    },
    {
      header: {
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
            custom_body_row={() => (
              <tr>
                <td colSpan={4} />
                <td>Tổng tiền</td>
                <td>
                  {convertCurrency(sumBy(invoiceSupplies, (item) => +item?.['price'] * +item?.['quantity']))}
                </td>
                <td colSpan={2} />
                <td>{convertCurrency(sumBy(invoiceSupplies, 'totalItemAfterVat'))}</td>
                <td colSpan={2} />
              </tr>
            )}
          />
        </Col>
        {errors?.invoiceSupplies && (
          <Col style={{ marginTop: '10px' }}>
            <FormError message="Danh sách hàng hóa là bắt buộc" />
          </Col>
        )}
      </Row>
      <Row>
        <Col md={3}>
          <FormInputV2
            isBorderBottomInput
            isRowLayout
            label="SL đã giao"
            control={control}
            name="deliveredQuantity"
            placeholder="Điền"
          />
        </Col>
        <Col md={{ offset: 1, size: 3 }}>
          <FormInputV2
            isRowLayout
            isBorderBottomInput
            label="SL còn nợ"
            control={control}
            name="remainingQuantity"
            placeholder="Điền"
          />
        </Col>
      </Row>
      <Row>
        <span>
          <Typography level={5}>Liên kết</Typography>
          {contracts?.map(item => (
            <span className="goods__link-contract">
              Hợp đồng mua
              <span className="goods__link-contract--id">
                {item.contractCode}
              </span>
            </span>
          ))}
        </span>
      </Row>
    </>
  );
};

export default Goods;
