import ButtonDropDown from 'app/components/ButtonV2/ButtonDropdown';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Tooltip from 'app/components/tooltip/tooltip';
import { useAppSelector } from 'app/config/store';
import {
  IStocktaking,
  STOCKTAKING_STATUS,
} from 'app/shared/model/stocktaking.model';
import { useContext, useState } from 'react';
import { StocktakingContext } from '../stocktaking-provider';
import { isHasPermission } from 'app/constants/common';

interface IActionsDropdownProps {
  data: IStocktaking;
}

const icon_path = 'content/images/vuesax/linear/';

const ActionsDropdown = ({ data }: IActionsDropdownProps) => {
  const account = useAppSelector(state => state.authentication.account);

  const [closeDropdown, setCloseDropdown] = useState<boolean>(false);

  const {
    toggleApprove,
    toggleConfirmDelete,
    toggleReject,
    toggleReview,
    setSelectedRecord,
  } = useContext(StocktakingContext);

  const handleDelete = () => {
    toggleConfirmDelete();
    setSelectedRecord(data?.id);
  };

  const handleApprove = () => {
    toggleApprove();
    setSelectedRecord(data?.id);
  };

  const handleReject = () => {
    toggleReject();
    setSelectedRecord(data?.id);
  };

  const handleReview = () => {
    toggleReview();
    setSelectedRecord(data?.id);
  };

  const isDisabledReview =
    account &&
    (data?.status === STOCKTAKING_STATUS.NEW ||
      data?.status === STOCKTAKING_STATUS.REJECTED) &&
    data?.createdBy === account.id;

  const isDisabledReject =
    account &&
    data?.status === STOCKTAKING_STATUS.WAITING_APPROVED &&
    data?.requestApprovals?.length > 0 &&
    data?.requestApprovals
      ?.filter(x => !x.approvedSign)
      ?.map(x => x.employeeId)
      .includes(account.id);

  const isDisabledApprove =
    account &&
    data?.status === STOCKTAKING_STATUS.WAITING_APPROVED &&
    data?.requestApprovals?.length > 0 &&
    data?.requestApprovals
      ?.filter(x => !x.approvedSign)
      ?.map(x => x.employeeId)
      .includes(account.id);

  const isDisabledDelete = () => {
    if (data?.createdBy === account.id) {
      if (data?.status === STOCKTAKING_STATUS.NEW) return false;
      return true;
    }
    return true;
  };

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  return (
    <ButtonDropDown
      isClose={closeDropdown}
      items={[
        {
          children: (
            <Tooltip label={'In phiếu'} target={`btn-print`}>
              <ButtonV2
                id="btn-print"
                variant="text"
                left_section={
                  <img
                    src="content/images/vuesax/linear/printer.svg"
                    alt="print"
                  />
                }
              >
                In phiếu
              </ButtonV2>
            </Tooltip>
          ),
          onClick: () => {
            setCloseDropdown(true);
            setTimeout(() => window.print(), 100);
          },
          hidden: !isHasPermission(authorities, 'LOGISTICS_STOCKTAKING.EXPORT')
        },
        {
          children: (
            <Tooltip label={'Hủy'} target={`btn-cancel`}>
              <ButtonV2
                id="btn-cancel"
                variant="text"
                left_section={
                  <img
                    src="content/images/vuesax/linear/x-circle.svg"
                    alt="cancel"
                  />
                }
                disabled={isDisabledDelete()}
              >
                Hủy
              </ButtonV2>
            </Tooltip>
          ),
          disabled: isDisabledDelete(),
          onClick: () => {
            setSelectedRecord(data?.id);
            handleDelete();
          },
          hidden: !isHasPermission(authorities, 'LOGISTICS_STOCKTAKING.EDIT')
        },
        {
          children: (
            <Tooltip label={'Trình duyệt'} target={`btn-send-approve`}>
              <ButtonV2
                id="btn-send-approve"
                variant="text"
                left_section={
                  <img
                    src="content/images/vuesax/linear/file-up.svg"
                    alt="pointer"
                  />
                }
                disabled={!isDisabledReview}
              >
                Trình duyệt
              </ButtonV2>
            </Tooltip>
          ),
          disable: !isDisabledReview,
          onClick: () => {
            setSelectedRecord(data?.id);
            handleReview();
          },
          hidden: !isHasPermission(authorities, 'LOGISTICS_STOCKTAKING.CREATE')
        },
        {
          children: (
            <Tooltip label={'Duyệt'} target={`btn-approve`}>
              <ButtonV2
                id="btn-approve"
                variant="text"
                left_section={
                  <img
                    src="content/images/vuesax/linear/check_green.svg"
                    alt="cancel"
                  />
                }
                disabled={!isDisabledApprove}
              >
                Duyệt
              </ButtonV2>
            </Tooltip>
          ),
          disable: !isDisabledApprove,
          onClick: () => {
            setSelectedRecord(data?.id);
            handleApprove();
          },
          hidden: !isHasPermission(authorities, 'LOGISTICS_STOCKTAKING.EDIT')
        },
        {
          children: (
            <Tooltip label={'Từ chối'} target={`btn-reject`}>
              <ButtonV2
                id="btn-reject"
                variant="text"
                left_section={
                  <img src="content/images/vuesax/linear/x.svg" alt="reject" />
                }
                disabled={!isDisabledReject}
              >
                Từ chối
              </ButtonV2>
            </Tooltip>
          ),
          disabled: !isDisabledReject,
          onClick: () => {
            setSelectedRecord(data?.id);
            handleReject();
          },
          hidden: !isHasPermission(authorities, 'LOGISTICS_STOCKTAKING.EDIT')
        },
      ]}
    >
      <img src={icon_path + 'more-v2.svg'} alt="more" />
    </ButtonDropDown>
  );
};

export default ActionsDropdown;
