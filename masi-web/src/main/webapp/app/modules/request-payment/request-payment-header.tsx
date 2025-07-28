import SplitButton from 'app/components/ButtonV2/SplitButton';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { isHasPermission } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { useNavigate } from 'react-router';

function RequestPaymentHeader() {
  const navigate = useNavigate();

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  let isHasRequestPaymentCreate = isHasPermission(authorities, 'REQUEST_PAYMENT.CREATE');
  let isHasAdvanceRequestCreate = isHasPermission(authorities, 'REQUEST_PAYMENT.CREATE');
  let isHasRefundRequestCreate = isHasPermission(authorities, 'REQUEST_PAYMENT.CREATE');
  let isHasOneOfThemPermission = isHasRequestPaymentCreate || isHasAdvanceRequestCreate || isHasRefundRequestCreate;

  return (
    <Flex justify="space-between" align="center">
      <Typography level={4}>DNTT - DNTU - DNHU</Typography>
      {isHasOneOfThemPermission && (
        <SplitButton
          items={[
            {
              children: 'Thanh toán',
              onClick: () => navigate(PATH.PAYMENT_REQUEST),
              hidden: !isHasRequestPaymentCreate,
            },
            {
              children: 'Tạm ứng',
              onClick: () => navigate(PATH.ADVANCE_REQUEST),
              hidden: !isHasAdvanceRequestCreate,
            },
            {
              children: 'Hoàn ứng',
              onClick: () => navigate(PATH.REFUND_REQUEST),
              hidden: !isHasRefundRequestCreate,
            },
          ]}
        >
          <span>Tạo mới</span>
          <div className="rp__split-btn--divider" />
        </SplitButton>
      )}
    </Flex>
  );
}

export default RequestPaymentHeader;
