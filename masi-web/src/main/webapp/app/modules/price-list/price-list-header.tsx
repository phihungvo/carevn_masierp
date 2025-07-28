import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import { useAppSelector } from 'app/config/store';
import { QUOTATION_STATUS } from 'app/shared/model/enumerations/quotation.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { IQuotation } from 'app/shared/model/quotation.model';
import React from 'react';
import { useNavigate } from 'react-router';
import { checkCompanyCreatePath } from './util/check-company-path';

interface IPriceListHeaderProps {
  setSearchText: (searchText: string) => void;
  toggleFilter: () => void;
  toggleDelete: () => void;
  toggleInApprove: () => void;
  toggleCancel: () => void;
  toggleCusSend: () => void;
  selectedRows: IQuotation[];
}

const PriceListHeader = (props: IPriceListHeaderProps) => {
  const { setSearchText, toggleFilter, toggleDelete, toggleInApprove, toggleCancel, toggleCusSend, selectedRows } = props;

  const account = useAppSelector(state => state.authentication.account);
  const navigate = useNavigate();

  const handleNavigateCreate = () => {
    navigate(checkCompanyCreatePath(account?.company?.normalizedName));
  };

  // const disabledExport = selectedRows.length !== 1 || selectedRows.some(row => row?.status !== QUOTATION_STATUS.APPROVED);
  const disabledCancel =
    selectedRows.length === 0 ||
    selectedRows.some(
      row =>
        row?.status === QUOTATION_STATUS.SENT ||
        row?.status === QUOTATION_STATUS.CUSTOMER_APPROVED ||
        row?.status === QUOTATION_STATUS.REJECTED ||
        row?.status === QUOTATION_STATUS.CANCELLED,
    );
  const disabledDelete =
    selectedRows.length === 0 ||
    selectedRows.some(row => row?.status === QUOTATION_STATUS.CUSTOMER_APPROVED || row?.status === QUOTATION_STATUS.REJECTED);
  const disabledCusSend =
    selectedRows.length === 0 ||
    selectedRows.some(row => row?.status === QUOTATION_STATUS.SENT || row?.status !== QUOTATION_STATUS.APPROVED);
  const disabledInApprove =
    selectedRows.length === 0 ||
    selectedRows.some(
      row =>
        !(
          row?.status === QUOTATION_STATUS.NEW ||
          row?.status === QUOTATION_STATUS.NEED_UPDATE ||
          row?.status === QUOTATION_STATUS.REJECTED
        ),
    );

  return (
    <div className="card-header-container">
      <InputSearch className="card-header-extra" onChange={e => setSearchText(e.target.value)} />
      <div className="card-header-extra">
        <Button className="btn-filter" onClick={toggleFilter}>
          Lọc <img src="content/images/vuesax/linear/sort.svg" alt="filter" />
        </Button>
        <AuthGuard permissionKey='PRICE_LIST.CREATE'>
          <Button className='default-buttton' color="primary" onClick={() => handleNavigateCreate()}>
            Tạo mới
          </Button>
          </AuthGuard>
          <AuthGuard permissionKey='PRICE_LIST.EDIT'>
          <Button className='default-buttton' onClick={toggleCusSend} color="primary" disabled={disabledCusSend}>
            Gửi KH
          </Button>
          </AuthGuard>
          <AuthGuard permissionKey='PRICE_LIST.CREATE'>
          <Button className='default-buttton' color="primary" onClick={toggleInApprove} disabled={disabledInApprove}>
            Gửi duyệt
          </Button>
        </AuthGuard>
        <AuthGuard permissionKey='PRICE_LIST.EDIT'>
          <Button className='default-buttton' color="primary" onClick={toggleDelete} disabled={disabledDelete}>
            Xoá
          </Button>
        </AuthGuard>
      </div>
    </div>
  );
};

export default PriceListHeader;
