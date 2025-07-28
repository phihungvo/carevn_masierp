import ButtonPrint from 'app/components/ButtonV2/ButtonPrint';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import { useAppSelector } from 'app/config/store';
import { PATH } from 'app/constants/path';
import { MANUFACTURE_ORDER_STATUS } from 'app/shared/model/enumerations/production-command.model';
import { PRODUCTION_QUALITY_STATUS } from 'app/shared/model/enumerations/production-quality-control.model';
import { IQualityCheckSample } from 'app/shared/model/production-quality-control.model';
import { useNavigate, useParams } from 'react-router';
import { useSearchParams } from 'react-router-dom';

interface IButtonGroupHeaderProps {
  status: string;
  toggleReject: () => void;
  toggleCancel: () => void;
  toggleSendApprove: () => void;
  setSelectedRecord?: (id) => void;
  toggleCancelTesting?: () => void;
  detail?: IQualityCheckSample;
  statusM: string;
}
export const ButtonGroupHeader = (props: IButtonGroupHeaderProps) => {
  const {
    status,
    toggleReject,
    toggleCancel,
    toggleSendApprove,
    setSelectedRecord,
    toggleCancelTesting,
    detail,
    statusM,
  } = props;

  const { id } = useParams();
  const navigate = useNavigate();
  const [_searchParams, setSearchParams] = useSearchParams();

  const account = useAppSelector(state => state.authentication.account);

  const styleApprove = {
    color: '#475467',
    borderColor: '#98A2B3',
    fontWeight: '600',
  };

  const disabledRejected =
    statusM === (MANUFACTURE_ORDER_STATUS.CANCELLED as string) ||
    status === (PRODUCTION_QUALITY_STATUS.DISPOSED as string) ||
    status === (PRODUCTION_QUALITY_STATUS.REJECTED as string);

  const disabledApprove =
    statusM === (MANUFACTURE_ORDER_STATUS.CANCELLED as string) ||
    status === (PRODUCTION_QUALITY_STATUS.DISPOSED as string) ||
    status === (PRODUCTION_QUALITY_STATUS.REJECTED as string);

  const disabledConfirmReject =
    statusM === (MANUFACTURE_ORDER_STATUS.CANCELLED as string) ||
    status === (PRODUCTION_QUALITY_STATUS.REJECTED as string);

  return (
    <Flex align="center" gap={10}>
      <ButtonV2
        style={{ ...styleApprove }}
        onClick={() => navigate(PATH.PRODUCTION_QUALITY)}
      >
        Đóng
      </ButtonV2>
      <AuthGuard permissionKey='PRODUCTION_QUALITY.EDIT'>
        {id &&
          detail?.disposal?.id &&
          detail?.disposal?.reviewerId === account?.id &&
          !disabledRejected && (
            <ButtonV2
              style={{
                color: '#B42318',
                borderColor: '#FDA29B',
                fontWeight: '600',
              }}
              onClick={() => {
                toggleReject();
                setSelectedRecord(detail?.disposal?.id);
              }}
              disabled={disabledRejected}
            >
              Từ chối
            </ButtonV2>
          )}
      </AuthGuard>
      <AuthGuard permissionKey='PRODUCTION_QUALITY.CREATE'>
        {id && !detail?.disposal?.id && (
          <ButtonV2
            style={{ ...styleApprove }}
            onClick={() => toggleCancelTesting()}
          >
            Tạo đơn hủy mẫu
          </ButtonV2>
        )}
      </AuthGuard>
      <AuthGuard permissionKey='PRODUCTION_QUALITY.EDIT'>
      {id && detail?.disposal?.id && (
        <ButtonV2
          style={{ ...styleApprove }}
          onClick={() => {
            toggleCancelTesting();
            setSelectedRecord(detail?.disposal?.id);
          }}
        >
          Đơn hủy mẫu
        </ButtonV2>
      )}</AuthGuard>

    <AuthGuard permissionKey='PRODUCTION_QUALITY.EXPORT'>
      {id && detail?.disposal?.id && (
        <ButtonPrint
          style={{ ...styleApprove }}
          onClick={() => {
            setSearchParams({
              printId: id,
              printType: 'PRODUCTION_QUALITY_CANCEL_TESTING',
            });
            setTimeout(() => window.print(), 500);
          }}
          disabled={!id}
        >
          In đơn hủy mẫu
        </ButtonPrint>
      )}</AuthGuard>

      <AuthGuard permissionKey='PRODUCTION_QUALITY.EDIT'>
      {id &&
        detail?.disposal?.id &&
        detail?.disposal?.reviewerId === account?.id &&
        !disabledApprove && (
          <ButtonV2
            style={{ ...styleApprove }}
            onClick={() => {
              setSelectedRecord(detail?.disposal?.id);
              toggleSendApprove();
            }}
            disabled={disabledApprove}
          >
            Duyệt
          </ButtonV2>
        )}

      {id &&
        detail?.disposal?.id &&
        disabledApprove &&
        !disabledConfirmReject && (
          <ButtonV2
            style={{ ...styleApprove }}
            onClick={() => {
              setSelectedRecord(detail?.id);
              toggleCancel();
            }}
            disabled={disabledConfirmReject}
          >
            Xác nhận hủy
          </ButtonV2>
        )}

      <ButtonV2
        variant="solid"
        color="blue"
        type="submit"
        disabled={disabledApprove}
      >
        Lưu
      </ButtonV2>
      </AuthGuard>
    </Flex>
  );
};
