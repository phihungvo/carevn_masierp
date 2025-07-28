import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { DATE_FORMAT, DEFAULT_INTEGER_REGEX } from 'app/constants/common';
import useProductionStandard from 'app/hooks/use-production-standard';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { generateMonthYearOptions } from 'app/shared/util/format';
import { handleValidatePaste } from 'app/shared/util/handle-valid-decimal';
import {
  ProductionStandardFormSchema,
  productionStandardSchema,
} from 'app/validation/production-standard.validation';
import dayjs from 'dayjs';
import { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { usePostProductionStandard, usePatchProductionStandard } =
  useProductionStandard;

interface IProductionStandardForm {
  type: 'create' | 'update';
  toggle: () => void;
  defaultValues?: ProductionStandardFormSchema;
  id?: string;
  setSelectedRecord?: (record: string | null) => void;
}

export const ProductionStandardForm = (props: IProductionStandardForm) => {
  const { type, toggle, defaultValues, id, setSelectedRecord } = props;

  const { control, handleSubmit, setValue, setError } =
    useForm<ProductionStandardFormSchema>({
      resolver: zodResolver(productionStandardSchema),
    });

  const onOk = () => {
    toggle();
    setSelectedRecord && setSelectedRecord(null);
  };

  const { mutate: create } = usePostProductionStandard(onOk);
  const { mutate: update } = usePatchProductionStandard(onOk);

  useEffect(() => {
    if (defaultValues) {
      setValue('code', defaultValues.code);
      setValue('name', defaultValues.name);
      setValue('dueDate', defaultValues.dueDate);
      setValue(
        'productionPowderQty',
        formatDecimalPrecision(defaultValues.productionPowderQty),
      );
      setValue('quantity', formatDecimalPrecision(defaultValues.quantity));
      setValue('note', defaultValues.note);
    }
  }, [defaultValues]);

  const toggleError = error => {
    const errorCode = error.response.data?.message;
    if (errorCode === 'error.codeexists') {
      setError('code', { message: 'Mã định mức đã tồn tại' });
    }
  };

  const onSubmit: SubmitHandler<ProductionStandardFormSchema> = values => {
    const submitValues = {
      code: values.code,
      name: values.name,
      dueDate: dayjs(values.dueDate, 'MM/YYYY')
        .endOf('month')
        .format(DATE_FORMAT.YEAR_DATE),
      productionPowderQty: Number(
        values.productionPowderQty?.replace(/,/g, '').replace(/\./g, ''),
      ),
      quantity: Number(values.quantity?.replace(/,/g, '').replace(/\./g, '')),
      note: values.note,
    };

    if (type === 'create')
      create({ ...submitValues }, { onError: toggleError });
    else update({ id, ...submitValues });
  };

  return (
    <Form<ProductionStandardFormSchema>
      id={FORM.PRODUCTION_STANDARD}
      onSubmit={handleSubmit(onSubmit)}
    >
      <Row>
        <Col md={6}>
          <FormInputV2
            control={control}
            label="Mã định mức"
            id="code"
            name="code"
            placeholder="Vui lòng nhập mã định mức"
            disabled
          />
        </Col>
        <Col md={6}>
          <FormInputV2
            control={control}
            label="Tên định mức"
            id="name"
            name="name"
            placeholder="Vui lòng nhập tên định mức"
          />
        </Col>
        <Col md={6}>
          <FormInputV2
            control={control}
            label="Khối lượng (Kg)"
            id="productionPowderQty"
            name="productionPowderQty"
            onChange={e =>
              setValue(
                'productionPowderQty',
                formatDecimalPrecision(
                  Number(e?.target?.value?.toString()?.replace(/,/g, '')),
                ),
              )
            }
            onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
            placeholder="Vui lòng nhập khối lượng (kg)"
          />
        </Col>
        <Col md={6}>
          <FormInputV2
            control={control}
            label="Kế hoạch thu mua"
            id="quantity"
            name="quantity"
            onChange={e =>
              setValue(
                'quantity',
                formatDecimalPrecision(
                  Number(e?.target?.value?.toString()?.replace(/,/g, '')),
                ),
              )
            }
            onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
            placeholder="Vui lòng nhập kế hoạch thu mua"
          />
        </Col>
        <Col md={6}>
          <FormSelect
            control={control}
            id="dueDate"
            name="dueDate"
            placeholder="Vui lòng chọn tháng"
            label="Tháng/Năm"
            options={generateMonthYearOptions(2024, 1, 2050, 1)}
            onChanges={e => {
              if (type === 'create') {
                if (e) {
                  const code = dayjs(e, 'MM/YYYY')
                    .startOf('month')
                    .format('DD_MM_YYYY');
                  setValue('code', code);
                } else setValue('code', '');
              }
            }}
          />
        </Col>
        <Col md={12}>
          <FormInputV2
            type="textarea"
            label="Ghi chú"
            control={control}
            id="note"
            name="note"
            placeholder="Nhập ghi chú"
          />
        </Col>
      </Row>
    </Form>
  );
};
