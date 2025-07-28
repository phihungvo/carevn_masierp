import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { Typography } from 'app/components/typography/typography';
import { convertCurrency } from 'app/shared/util/format';
import { RefundRequestSchema } from 'app/validation/request-payment.validation';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const RefundRequestDeviation = () => {
  const { watch } = useFormContext<RefundRequestSchema>();
  const reimbursementWatch = watch('reimbursements');
  const paymentDetailWatch = watch('paymentDetails');

  // I. Phiếu tạm ứng
  const calcTotalReimbursement = () => {
    return reimbursementWatch?.reduce(
      (acc, obj) =>
        acc + (Number(obj?.advancement?.paymentVoucherAmount ?? 0) ?? 0),
      0,
    );
  };

  // II. Số tiền đã chi
  const calcTotalPaymentDetails = () => {
    return paymentDetailWatch?.reduce(
      (acc, obj) => acc + (Number(obj?.incomingInvoice?.totalAmount ?? 0) ?? 0),
      0,
    );
  };

  const calcRemainingBalance = () => {
    const calc =
      (calcTotalReimbursement() ?? 0) - (calcTotalPaymentDetails() ?? 0);
    return calc > 0 ? calc : 0;
  };

  const calcOverSpent = () => {
    const calc =
      (calcTotalPaymentDetails() ?? 0) - (calcTotalReimbursement() ?? 0);
    return calc > 0 ? calc : 0;
  };

  return (
    <section>
      <Typography
        level={5}
        style={{
          fontSize: '18px',
          fontWeight: '500',
          letterSpacing: '-0.5px',
        }}
      >
        III. Chênh lệch
      </Typography>
      <Row>
        <Col md={6}>
          <FormInputV2
            label="Số tạm ứng chi không hết (I-II)"
            name=""
            placeholder="Số tạm ứng chi không hết (I-II)"
            disabled
            value={convertCurrency(calcRemainingBalance())}
          />
        </Col>
        <Col md={6}>
          <FormInputV2
            label="Chi quá tạm ứng (II-I)"
            name=""
            placeholder="Chi quá tạm ứng (II-I)"
            disabled
            value={convertCurrency(calcOverSpent())}
          />
        </Col>
      </Row>
    </section>
  );
};

export default RefundRequestDeviation;
