import ButtonPrint from 'app/components/ButtonV2/ButtonPrint';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import { useAppSelector } from 'app/config/store';
import { PATH } from 'app/constants/path';
import { PAYMENT_REQUEST_STATUS } from 'app/shared/model/enumerations/payment-request';
import { useNavigate, useParams } from 'react-router';
import { useSearchParams } from 'react-router-dom';

interface IButtonGroupHeaderProps {
  status: string;
  createdBy: string;
  toggleCancel: () => void;
  toggleSendApprove: () => void;
  handleSubmit?: () => void;
  disableSendApproved?: boolean;
  type: string;
}
export const ButtonGroupHeader = (props: IButtonGroupHeaderProps) => {
  const {
    status,
    createdBy,
    toggleCancel,
    toggleSendApprove,
    disableSendApproved = false,
    type,
  } = props;

  const { id } = useParams();
  const navigate = useNavigate();
  const [_searchParams, setSearchParams] = useSearchParams();

  const account = useAppSelector(state => state.authentication.account);

  return (
    <Flex align="center" gap={10}>
      <ButtonV2
        style={{
          color: '#475467',
          borderColor: '#98A2B3',
          fontWeight: '600',
        }}
        onClick={() => navigate(PATH.REQUEST_PAYMENT)}
      >
        Đóng
      </ButtonV2>
      {status === (PAYMENT_REQUEST_STATUS.NEW as string) &&
        createdBy === account.id &&
        id && (
          <ButtonV2
            style={{
              color: '#B42318',
              borderColor: '#FDA29B',
              fontWeight: '600',
            }}
            onClick={() => toggleCancel()}
          >
            Hủy phiếu
          </ButtonV2>
        )}
      {(status === (PAYMENT_REQUEST_STATUS.NEW as string) ||
        status === (PAYMENT_REQUEST_STATUS.REJECTED as string)) &&
        id && (
          <ButtonV2
            style={{
              color: '#475467',
              borderColor: '#98A2B3',
              fontWeight: '600',
            }}
            onClick={() => toggleSendApprove()}
            disabled={disableSendApproved || createdBy !== account.id}
          >
            Trình phiếu
          </ButtonV2>
        )}
      <ButtonV2
        variant="solid"
        color="blue"
        type="submit"
        disabled={
          status === (PAYMENT_REQUEST_STATUS.APPROVED as string) ||
          status === (PAYMENT_REQUEST_STATUS.WAITING_APPROVE as string) ||
          status === (PAYMENT_REQUEST_STATUS.CANCELLED as string)
        }
      >
        Lưu
      </ButtonV2>
      {id && (
        <ButtonPrint
          onClick={() => {
            if (id && type) {
              setSearchParams({ printId: id, printType: type });
              setTimeout(() => window.print(), 500);
            }
          }}
          disabled={!id || !type}
        />
      )}
    </Flex>
  );
};
