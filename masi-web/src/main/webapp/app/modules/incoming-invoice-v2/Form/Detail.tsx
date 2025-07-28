import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { Typography } from 'app/components/typography/typography';
import { IncomingInvoiceSchema } from 'app/validation/incoming-invoice.validation';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import './Detail.scss';
import { convertCurrency } from 'app/shared/util/format';
import { IncomingInvoiceV2SchemaType } from '../validation/incoming.validate';
import { sumBy } from 'lodash';
import { ImportConvertedGoodsType } from 'app/validation/supplies-request.validation';

type Props = {};

const Detail = (props: Props) => {
  const { control, watch } = useFormContext();
  const invoiceSupplies = watch('invoiceSupplies')
  const totalRoot = watch('totalRoot')

  const total = +sumBy(invoiceSupplies, (item: ImportConvertedGoodsType) => {
    return +item?.price * +item?.quantity || 0
  })

  const feeBeforeImport = +sumBy(invoiceSupplies, (item: ImportConvertedGoodsType) => {
    return +item?.feesBeforeImport || 0
  })

  const taxAfterImport = +sumBy(invoiceSupplies, (item: ImportConvertedGoodsType) => {
    return +item?.importTax || 0
  })

  const envTax = +sumBy(invoiceSupplies, (item: ImportConvertedGoodsType) => {
    return +item?.envTax || 0
  })

  const feeAfterImport = +sumBy(invoiceSupplies, (item: ImportConvertedGoodsType) => {
    return +item?.feesAfterImport || 0
  })

  const vat = +sumBy(invoiceSupplies, (item: ImportConvertedGoodsType) => {
    let total = +item?.price * +item?.quantity || 0
    return total * +item?.vatDTO?.percent / 100 || 0
  })

  const grandTotal = total + taxAfterImport + envTax + vat

  const money = total + taxAfterImport + envTax + vat

  return (
    <div className='import-detail'>
      <Row>
        <Col md={6}>
          <Row>
            <Col md={6}>
              <Typography level={6}>
                Tiền hàng
              </Typography>
            </Col>
            <Col md={6}>
              <FormInputV2
                control={control}
                name="address"
                disabled
                value={convertCurrency(total || 0)}
              />
            </Col>
          </Row>
        </Col>
        <Col md={6}>
          <Row>
            <Col md={6}>
              <Typography level={6}>
                VAT
              </Typography>
            </Col>
            <Col md={6}>
              <FormInputV2
                control={control}
                name="address"
                disabled
                value={convertCurrency(sumBy(invoiceSupplies, (item: ImportConvertedGoodsType) => {
                  let total = item?.price * item?.quantity || 0
                  return total * item?.vatDTO?.percent / 100 || 0
                }))}
              />
            </Col>
          </Row>
        </Col>
      </Row>
      <Row>
        <Col md={6}>
          <Row>
            <Col md={6}>
              <Typography level={6}>
                Phí trước nhập khẩu
              </Typography>
            </Col>
            <Col md={6}>
              <FormInputV2
                control={control}
                name="address"
                disabled
                value={convertCurrency(feeBeforeImport)}
              />
            </Col>
          </Row>
        </Col>
        <Col md={6}>
          <Row>
            <Col md={6}>
              <Typography level={6}>
                Tổng tiền
              </Typography>
            </Col>
            <Col md={6}>
              <FormInputV2
                control={control}
                name="address"
                disabled
                value={totalRoot && convertCurrency(parseFloat(totalRoot.toFixed(2)) || 0)}
              />
            </Col>
          </Row>
        </Col>
      </Row>
      <Row>
        <Col md={6}>
          <Row>
            <Col md={6}>
              <Typography level={6}>
                Thuế nhập khẩu
              </Typography>
            </Col>
            <Col md={6}>
              <FormInputV2
                control={control}
                name="address"
                disabled
                value={convertCurrency(taxAfterImport)}
              />
            </Col>
          </Row>
        </Col>
        <Col md={6}>
          <Row>
            <Col md={6}>
              <Typography level={6}>
                Phí sau nhập khẩu
              </Typography>
            </Col>
            <Col md={6}>
              <FormInputV2
                control={control}
                name="address"
                disabled
                value={convertCurrency(feeAfterImport)}
              />
            </Col>
          </Row>
        </Col>
      </Row>
      <Row>
        <Col md={6}>
          <Row>
            <Col md={6}>
              <Typography level={6}>
                Thuế môi trường
              </Typography>
            </Col>
            <Col md={6}>
              <FormInputV2
                control={control}
                name="address"
                disabled
                value={convertCurrency(envTax)}
              />
            </Col>
          </Row>
        </Col>
        <Col md={6}>
          <Row>
            <Col md={6}>
              <Typography level={6}>
                Tiền nhập kho
              </Typography>
            </Col>
            <Col md={6}>
              <FormInputV2
                control={control}
                name="address"
                disabled
                value={money && convertCurrency(parseFloat(money.toFixed(2)) || 0)}
              />
            </Col>
          </Row>
        </Col>
      </Row>
    </div>
  );
};

export default Detail;
